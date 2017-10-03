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
import java.util.ArrayList;
import org.compiere.util.Ctx;

import compiere.model.X_AFGO_Fund;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFund.java,v 1.18.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOFund extends X_AFGO_Fund implements AllocatableDocument
{

	public MAFGOFund(Ctx ctx, int AFGO_Fund_ID, String trxName) 
	{
		super(ctx, AFGO_Fund_ID, trxName);
	}
	
	public MAFGOFund(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOProgram program = null;
	
	private MAFGOFundProvider fundProvider = null;
	
	private MAFGOFundScheduleType scheduleType = null;
	
	private ArrayList<MAFGOFundLine> lines = null;
	
	private String processMsg = null;
	
	private CLogger log = CLogger.getCLogger(this.getClass());
	
	public PO getPO()
	{
		return this;
	}
	
	public MAFGOProgram getProgram()
	{
		if (this.program == null)
			this.program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
		return this.program;
	}
	
	public MBPartner getBPartner()
	{
		return this.getFundProvider().getBPartner();
	}
	
	public MDocType getDocType()
	{
		return null;
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
		
		// TODO: move to ICTU ModelValidator
		log.warning("reset approval to ICTU validator");
		//this.setIsBudgetMaintainerApproved(false);
		//this.setIsBudgetOwnerApproved(false);
		//this.setIsSelectionIntakeApproved(false);
		//this.setIsExecutiveApproved(false);
		
		this.setDocStatus(DOCSTATUS_Drafted);
		this.unlockIt();
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public MAFGOFundProvider getFundProvider()
	{
		if (this.fundProvider == null)
			this.fundProvider = new MAFGOFundProvider(this.getCtx(), this.getAFGO_FundProvider_ID(), this.get_TrxName());
		return this.fundProvider;
	}
	
	public MAFGOFundScheduleType getFundScheduleType()
	{
		if (this.scheduleType == null)
			this.scheduleType = new MAFGOFundScheduleType(this.getCtx(), this.getAFGO_FundScheduleType_ID(), this.get_TrxName());
		return this.scheduleType;
	}
	
	public ArrayList<MAFGOFundLine> getLines ()
	{
		return this.getLines(false);
	}
	
	public ArrayList<MAFGOFundLine> getLines(boolean reload)
	{
		if (this.lines == null || reload)
		{
			this.lines = new ArrayList<MAFGOFundLine>();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String sql = "SELECT *"
				+ " FROM AFGO_FundLine"
				+ " WHERE AFGO_Fund_ID=?"
				+ " ORDER BY Line";
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_Fund_ID());
				rs = pstmt.executeQuery();
				
				while(rs.next())
					lines.add(new MAFGOFundLine(this.getCtx(), rs, this.get_TrxName()));
				
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
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return lines;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getStartDate().after(this.getEndDate()))
		{
			this.log.saveError("StartDateAfterEndDate", Msg.getMsg(this.getCtx(), "StartDate") + "=" + this.getStartDate() + ", " + Msg.getMsg(this.getCtx(), "EndDate") + "=" + this.getEndDate());
			return false;
		}
		
		if (this.getStartDate().before(this.getProgram().getStartDate()))
		{
			log.saveError("StartDateBeforeProgramStartDate", Msg.translate(this.getCtx(), "AFGO_Program_ID") + "." + Msg.getMsg(this.getCtx(), "StartDate") + "=" + this.getProgram().getStartDate() + ", " + Msg.translate(this.getCtx(), "AFGO_Fund_ID") + "." + Msg.getMsg(this.getCtx(), "StartDate") + "=" + this.getStartDate());
			return false;
		}
		
		if (this.getEndDate().after(this.getProgram().getEndDate()))
		{
			log.saveError("EndDateAfterProgramEndDate", Msg.translate(this.getCtx(), "AFGO_Program_ID") + "." + Msg.getMsg(this.getCtx(), "EndDate=") + this.getProgram().getEndDate() + ", " + Msg.translate(this.getCtx(), "AFGO_Fund_ID") + "." + Msg.getMsg(this.getCtx(), "EndDate") + this.getEndDate());
			return false;
		}
		return true;
	}
	
	public ArrayList<MAFGOMonth> getMonths ()
	{
		return this.getProgram().getCalendar().getMonths(this.getStartDate(), this.getEndDate());
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
		
		ArrayList<MAFGOFundLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		// TODO: move to ICTU MOdelValidator
		//log.warning("reset approval to ICTU validator");
		// remove approvals that may have been set by previous workflow process
		//this.setIsBudgetMaintainerApproved(false);
		//this.setIsBudgetOwnerApproved(false);
		//this.setIsSelectionIntakeApproved(false);
		//this.setIsExecutiveApproved(false);
		
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

}
