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
import java.util.ArrayList;
import java.util.Iterator;

import compiere.model.X_AFGO_InternalCommitment;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOInternalCommitment.java,v 1.17.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOInternalCommitment extends X_AFGO_InternalCommitment implements AllocatableDocument
{

	public MAFGOInternalCommitment (Ctx ctx, int AFGO_InternalCommitment_ID, String trxName)
	{
		super(ctx, AFGO_InternalCommitment_ID, trxName);
	}
	
	public MAFGOInternalCommitment (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private String processMsg = null;
	
	private ArrayList<MAFGOInternalCommitmentLine> lines = null;
	
	private MDocType docType = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String toString()
	{
		return this.getDocumentInfo();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public MBPartner getBPartner()
	{
		return this.getProviderProgram().getBPartner();
	}
	
	public MDocType getDocType()
	{
		return MDocType.get(this.getCtx(), this.getC_DocType_ID());
	}
	
	public ArrayList<MAFGOInternalCommitmentLine> getLines ()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOInternalCommitmentLine>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_InternalCommitmentLine"
				+ " WHERE AFGO_InternalCommitment_ID=?"
				+ " ORDER BY Line";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_InternalCommitment_ID());
				
				rs = pstmt.executeQuery();
				
				while (rs.next())
					this.lines.add(new MAFGOInternalCommitmentLine(this.getCtx(), rs, this.get_TrxName()));
				
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				this.log.severe("Unable to load Internal Commitment Lines: AFGO_InternalCommitment_ID=" + this.getAFGO_InternalCommitment_ID() + ", msg=" + e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.lines;
	}

	public ArrayList<MAFGOMonth> getMonths ()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		for (Iterator<MAFGOInternalCommitmentLine> lines = this.getLines().iterator(); lines.hasNext();)
			months.add(MAFGOMonth.getMonth(this.getCtx(), lines.next().getAFGO_Month_ID()));
		return months;
	}

	public MAFGOProgram getProgram ()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
	}
	
	public MAFGOProgram getProviderProgram()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getProviderProgram_ID());
	}

	public MAFGOProgram getWorkflowProgram (MWFActivity activity)
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
		
		this.setDocStatus(DOCSTATUS_Drafted);
		this.unlockIt();
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public boolean processIt (String processAction) throws Exception
	{
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}
	
	public String prepareIt()
	{
		log.fine(toString());
		processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_BEFORE_PREPARE);
		if (processMsg != null)
			return DOCSTATUS_Invalid;
		
		ArrayList<MAFGOInternalCommitmentLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		// TODO: move to ICTU ModelValidator
		//log.warning("reset approval to ICTU validator");
		//this.setIsBudgetMaintainerApproved(false);
		//this.setIsBudgetOwnerApproved(false);

		this.setDocAction(DOCACTION_Complete);
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt()
	{
		log.info(toString());
		return true;
	}
	
	public boolean rejectIt()
	{
		log.info(toString());
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}

	public String completeIt()
	{
		log.info(toString());
		this.processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_AFTER_COMPLETE);
		if (this.processMsg != null)
			return DOCSTATUS_Invalid;
		
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}
	
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
	}

	public boolean voidIt()
	{
		if (DOCSTATUS_Completed.equals(this.getDocStatus()) || DOCSTATUS_Closed.equals(this.getDocStatus()))
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		
		return true;
	}
	
	public boolean invalidateIt()
	{
		log.info(toString());
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}
	
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}
	

	public boolean reverseCorrectIt()
	{
		log.info(toString());
		return false;
	}

	public boolean unlockIt()
	{
		this.log.info("unlockIt: " + this.toString());
		this.setProcessing(false);
		return true;
	}

	public File createPDF()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public BigDecimal getApprovalAmt()
	{
		return this.getGrandTotal();
	}

	public int getDoc_User_ID()
	{
		return this.getCreatedBy();
	}

	public String getDocumentInfo ()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}

	public String getProcessMsg()
	{
		return this.processMsg;
	}

	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
			append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
			.append(" (#").append(getLines().size()).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	public int getC_Currency_ID()
	{
		return MClient.get(getCtx()).getC_Currency_ID();
	}

}
