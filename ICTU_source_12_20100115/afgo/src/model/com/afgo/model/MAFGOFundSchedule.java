/******************************************************************************
 * Product: AFGO                                                              *
 * Copyright (C) 2007-2010 Stichting ICTU. All Rights Reserved.               *
 *                                                                            *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 3 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * Stichting ICTU                                                             *
 * Postbus 84011                                                              *
 * 2508 AA Den Haag                                                           *
 * The Netherlands                                                            *
 * +31(0)70 888 77 77                                                         *
 * www.ictu.nl / info@ictu.nl                                                 *
 ******************************************************************************/
package com.afgo.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.compiere.util.Ctx;

import compiere.model.X_AFGO_FundSchedule;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;



/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFundSchedule.java,v 1.22.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOFundSchedule extends X_AFGO_FundSchedule implements AllocatableDocument
{

	public MAFGOFundSchedule (Ctx ctx, int AFGO_FundSchedule_ID, String trxName)
	{
		super(ctx, AFGO_FundSchedule_ID, trxName);
		
		if (AFGO_FundSchedule_ID == 0)
		{
			this.setDocStatus(DOCSTATUS_Drafted);
			this.setDocAction(DOCACTION_Complete);
			
			this.setDateAcct(new Timestamp (System.currentTimeMillis ()));
			this.setDateDoc(new Timestamp (System.currentTimeMillis ()));
			
			this.setGrandTotal(Env.ZERO);
			this.setInvoicedAmt(Env.ZERO);
		}

	}
	
	public MAFGOFundSchedule (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOProgram program = null;
	
	private MAFGOFund fund = null;
	
	private MAFGOQuarter quarter = null;
	
	private MAFGOMonth month = null;
	
	private String processMsg = null;
	
	private CLogger log = CLogger.getCLogger(this.getClass());
	
	public PO getPO()
	{
		return this;
	}
	
	public MAFGOProgram getProgram ()
	{
		if (this.program == null)
			this.program = MAFGOProgram.getProgram(this.getCtx(), this.getFund().getAFGO_Program_ID());
		return this.program;
	}
	
	public MBPartner getBPartner()
	{
		return this.getFund().getFundProvider().getBPartner();
	}
	
	public MDocType getDocType()
	{
		return MDocType.get(this.getCtx(), this.getC_DocType_ID());
	}
	
	public MAFGOProgram getWorkflowProgram(MWFActivity activity)
	{
		return this.getProgram();
	}
	
	public void resetWorkflow()
	{
		if (DOCSTATUS_Completed.equals(this.getDocStatus()))
			return;
		if (DOCSTATUS_Closed.equals(this.getDocStatus()))
			return;
		
		this.setDocStatus(DOCSTATUS_Drafted);
		this.unlockIt();
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public MAFGOQuarter getQuarter()
	{
		if (this.quarter == null)
			this.quarter = MAFGOQuarter.getQuarter(this.getCtx(), this.getAFGO_Quarter_ID());
		return this.quarter;
	}
	
	public MAFGOMonth getMonth()
	{
		if (this.month == null && this.getAFGO_Month_ID() > 0)
			this.month = MAFGOMonth.getMonth(this.getCtx(), this.getAFGO_Month_ID());
		return this.month;
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}
	
	public MAFGOMonth getOldMonth()
	{
		return MAFGOMonth.getMonth(this.getCtx(), this.get_OldAFGO_Month_ID());
	}
	
	public ProgramPeriod getPeriod()
	{
		if (this.getMonth().isDefault())
			return this.getQuarter();
		else
			return this.getMonth();
	}
	
	public MAFGOFund getFund()
	{
		if (this.fund == null)
			this.fund = new MAFGOFund(this.getCtx(), this.getAFGO_Fund_ID(), this.get_TrxName());
		return this.fund;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// AFGO_Program_ID is a virtual column now
		//if (this.getAFGO_Program_ID() < 1 && this.getFund() != null)
		//	this.setAFGO_Program_ID(this.getFund().getAFGO_Program_ID());
		
		// always org of funding doc
		this.setAD_Org_ID(this.getFund().getAD_Org_ID());
		
		// set default DocType
		if (this.getC_DocType_ID() < 1)
		{
			String sql = "SELECT C_DocType_ID"
				+ " FROM C_DocType"
				+ " WHERE DocBaseType=?"
				+ " AND IsDefault='Y'"
				+ " AND AD_Client_ID=?"
				+ " AND AD_Org_ID IN (0, ?)"
				+ " AND IsActive='Y'"
				+ " ORDER BY AD_Org_ID DESC";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setString(1, DocBaseType);
				pstmt.setInt(2, this.getAD_Client_ID());
				pstmt.setInt(3, this.getAD_Org_ID());
				
				rs = pstmt.executeQuery();
				if (rs.next())
					this.setC_DocType_ID(rs.getInt(1));
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				log.severe("Error loading default C_DocType, AFGO_FindSchedule_ID=" + this.getAFGO_FundSchedule_ID());
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		if (this.getC_DocType_ID() < 1)
		{
			log.saveError("NoDocType", "");
			return false;
		}
		
		return true;
	}
	
	public MInvoice createInvoice() throws CompiereSystemException
	{
		MInvoice invoice = null;

		if (this.getDateInvoiced().after(new Date(this.getCtx().getContextAsTime("#Date"))))
			return invoice;
		
		MAFGOFundScheduleType scheduleType = this.getFund().getFundScheduleType();
		
		for (Iterator<MAFGOFundScheduleLine> lines = this.getLines().iterator(); lines.hasNext();)
		{
			MAFGOFundScheduleLine line = lines.next();
			if (line.getLineNetAmt().equals(line.getInvoicedAmt()))
				continue;
			
			if (invoice == null)
			{
				invoice = new MInvoice(this.getCtx(), 0, this.get_TrxName());
				invoice.setAD_Org_ID(this.getAD_Org_ID());
				invoice.setAFGO_Program_ID(line.getFundSchedule().getFund().getAFGO_Program_ID());
				invoice.setAFGO_FundSchedule_ID(line.getAFGO_FundSchedule_ID());
				invoice.setC_BPartner_ID(this.getFund().getFundProvider().getC_BPartner_ID());
				invoice.setAD_User_ID(this.getFund().getFundProvider().getAD_User_ID());
				invoice.setSalesRep_ID(this.getCtx().getContextAsInt("#SalesRep_ID"));
				invoice.setC_BPartner_Location_ID(this.getFund().getFundProvider().getC_BPartner_Location_ID());
				invoice.setDescription(Msg.translate(this.getCtx(), "AFGO_Fund_ID") + ": " + this.getFund().getDocumentNo() + ", " + Msg.translate(this.getCtx(), "ProgramPeriod") + this.getPeriod().getName()); 
				
				if (scheduleType.getC_DocTypeInvoice_ID() > 0)
					invoice.setC_DocType_ID(scheduleType.getC_DocTypeInvoice_ID());
				
				if (scheduleType.getC_PaymentTerm_ID() > 0)
					invoice.setC_PaymentTerm_ID(scheduleType.getC_PaymentTerm_ID());
				
				if (scheduleType.getPaymentRule() != null)
					invoice.setPaymentRule(scheduleType.getPaymentRule());
				
				if (!invoice.save())
					return invoice;
			}
			
			MInvoiceLine il = new MInvoiceLine(this.getCtx(), 0, this.get_TrxName());
			il.setAD_Org_ID(invoice.getAD_Org_ID());
			il.setC_Invoice_ID(invoice.getC_Invoice_ID());
			il.setTax();
			il.setQty(Env.ONE);
			il.setC_Charge_ID(line.getFundLine().getC_Charge_ID());
			il.setPrice(line.getLineNetAmt().subtract(line.getInvoicedAmt()));
			il.setAFGO_FundScheduleLine_ID(line.getAFGO_FundScheduleLine_ID());
			MAFGOProgram program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
			MAFGOMonth month = program.getMonth(new java.sql.Date(invoice.getDateAcct().getTime()), false);
			il.setAFGO_Quarter_ID(month.getAFGO_Quarter_ID());
			il.setAFGO_Month_ID(month.getAFGO_Month_ID());
			//il.setAFGO_Quarter_ID(line.getFundSchedule().getAFGO_Quarter_ID());
			//il.setAFGO_Month_ID(line.getFundSchedule().getAFGO_Month_ID());
			il.setAFGO_ProjectCluster_ID(line.getAFGO_ProjectCluster_ID());
			il.setAFGO_Project_ID(line.getAFGO_Project_ID());
			il.setAFGO_Phase_ID(line.getAFGO_Phase_ID());
			il.setAFGO_Activity_ID(line.getAFGO_Activity_ID());
			il.setAFGO_ServiceType_ID(line.getAFGO_ServiceType_ID());
			il.setDescription(line.getDescription());
			il.save();
			
			line.save();
		}
		
		return invoice;
	}
	
	public ArrayList<MAFGOFundScheduleLine> getLines()
	{
		ArrayList<MAFGOFundScheduleLine> lines = new ArrayList<MAFGOFundScheduleLine>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT *" 
			+ " FROM AFGO_FundScheduleLine"
			+ " WHERE AFGO_FundSchedule_ID=?"
			+ " ORDER BY Line";
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_FundSchedule_ID());
			rs = pstmt.executeQuery();
			
			while(rs.next())
				lines.add(new MAFGOFundScheduleLine(this.getCtx(), rs, this.get_TrxName()));
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}
		
		return lines;
	}
	
	public ArrayList<MAFGOMonth> getMonths ()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		months.add(this.getMonth());
		
		return months;
	}
	
	public boolean updateInvoicedAmount(int C_Invoice_ID)
	{
		for (Iterator<MAFGOFundScheduleLine> lines = this.getLines().iterator(); lines.hasNext();)
		{
			MAFGOFundScheduleLine line = lines.next();
			if (!line.updateInvoicedAmount(C_Invoice_ID))
				return false;
			line.save(this.get_TrxName());
		}
		
		return true;
	}
	
	public boolean processIt (String processAction) throws Exception
	{
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}
	
	public String prepareIt ()
	{
		processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_BEFORE_PREPARE);
		if (processMsg != null)
			return DocActionConstants.STATUS_Invalid;
		
		ArrayList<MAFGOFundScheduleLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt ()
	{
		return true;
	}
	
	public boolean rejectIt ()
	{
		return true;
	}

	public String completeIt ()
	{
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{
		return true;
	}
	
	public boolean reActivateIt ()
	{
		return true;
	}

	public boolean voidIt ()
	{
		return true;
	}
	
	public boolean invalidateIt ()
	{
		return true;
	}
	
	public boolean reverseAccrualIt ()
	{
		return true;
	}
	

	public boolean reverseCorrectIt ()
	{
		return true;
	}

	public boolean unlockIt ()
	{
		this.log.info("unlockIt: " + this.toString());
		this.setProcessing(false);
		return true;
	}

	public File createPDF ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public BigDecimal getApprovalAmt ()
	{
		return this.getGrandTotal();
	}

	public int getDoc_User_ID ()
	{
		return this.getCreatedBy();
	}

	public String getDocumentInfo ()
	{
		return Table_Name + " " + this.getDocumentNo();
	}

	public String getSummary ()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.getDocumentNo());
		sb.append(": ");
		sb.append(Msg.translate(getCtx(),"GrandTotal"));
		sb.append("=");
		sb.append(this.getGrandTotal());
		sb.append(" ");
		if (this.getDescription() != null && this.getDescription().length() > 0)
			sb.append(this.getDescription());
		return sb.toString();
	}

	public String getProcessMsg ()
	{
		return this.processMsg;
	}
	
	public static final transient String DocBaseType = "XFS";

}
