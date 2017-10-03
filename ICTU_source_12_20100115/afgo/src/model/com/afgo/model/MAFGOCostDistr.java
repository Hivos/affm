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

import compiere.model.X_AFGO_CostDistr;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOCostDistr.java,v 1.22.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOCostDistr extends X_AFGO_CostDistr implements AllocatableDocument
{
	public MAFGOCostDistr (Ctx ctx, int AFGO_CostDistr_ID, String trxName)
	{
		super(ctx, AFGO_CostDistr_ID, trxName);
	}
	
	public MAFGOCostDistr (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private MInvoice invoice = null;
	
	private ArrayList<MAFGOCostDistrLine> lines = null;
	
	private String processMsg = null;
	
	private MBPartner bpartner = null;

	private CLogger log = CLogger.getCLogger(getClass());
	
	public PO getPO()
	{
		return this;
	}

	public MAFGOProgram getProgram()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
	}
	
	public MAFGOProgram getToProgram()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getTo_Program_ID());
	}
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
			this.bpartner = this.getToProgram().getBPartner();
		return this.bpartner;
	}
	
	public MDocType getDocType()
	{
		return MDocType.get(this.getCtx(), this.getC_DocType_ID());
	}
	
	public MAFGOProgram getWorkflowProgram(MWFActivity activity)
	{
		// during and after allocation: use target program
		// note: all WF nodes with "Allocation" in the search key will be treated as part of the target program
		if (activity != null && activity.getNode().getValue().indexOf("Allocation") > 0)
			return this.getToProgram();
		
		// otherwise: use source program
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
		//this.setIsFinancialApproved(false);
		//this.setIsFinancialDirectorApproved(false);
		//this.setIsAcceptAllocation(false);
		
		this.setDocStatus(DOCSTATUS_Drafted);
		
		this.unlockIt();
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		// move to target program for allocation
		
		
		// route back to allocation when not correctly allocated
		// Compiere WF engine does not support explicit circular WF definitions (see CMP10018779)
		if (WF_NODE_RETRY_ALLOCATION.equals(activity.getNode().getValue()))
		{
			MWFNode nodeAllocation = MAFGOWorkflowRoleAssignment.getWFNodeByValue(activity, WF_NODE_CREATE_ALLOCATION);
			if (nodeAllocation != null && nodeAllocation.getAD_WF_Node_ID() != activity.getAD_WF_Node_ID())
			{
				activity.setAD_WF_Node_ID(nodeAllocation.getAD_WF_Node_ID());
			}
		}

		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);

	}
	
	public void setProcessingAllocation(boolean processingAllocation)
	{
		//TODO: Implement as ModelValidator
		// (M_ level set methods are being bypassed by the Workflow Engine)
		//System.err.println("================>PROCESSING ALLOCATION: " + processingAllocation);
		super.setProcessingAllocation(processingAllocation);
		for (Iterator<MAFGOCostDistrLine> lines = this.getLines(true).iterator(); lines.hasNext();)
		{
			for (Iterator<MAFGOCostDistrAllocation> allocations = lines.next().getAllocations().iterator(); allocations.hasNext();)
			{
				MAFGOCostDistrAllocation allocation = allocations.next();
				allocation.setProcessing(processingAllocation);
			}
		}
	}
	
	public ArrayList<MAFGOCostDistrLine> getLines()
	{
		return this.getLines(false);
	}
	
	public ArrayList<MAFGOCostDistrLine> getLines(boolean reload)
	{
		if (reload)
			this.lines = null;
		
		//System.err.println("CDL: reload=" + (this.lines == null));
		
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOCostDistrLine>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_CostDistrLine"
				+ " WHERE AFGO_CostDistr_ID=?"
				+ " ORDER BY Line";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_CostDistr_ID());
				rs = pstmt.executeQuery();
				
				while(rs.next())
					this.lines.add(new MAFGOCostDistrLine(this.getCtx(), rs, this.get_TrxName()));
				
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (rs != null) try {rs.close();rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close();pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.lines;
	}

	public ArrayList<MAFGOMonth> getMonths ()
	{
		return null;
	}
	
	public boolean isAllocated()
	{
		for(Iterator<MAFGOCostDistrLine> lines = this.getLines().iterator(); lines.hasNext();)
		{
			if (!lines.next().isAllocated())
				return false;
		}
		
		return true;
	}
	
	
	public MInvoice getInvoice()
	{
		if (this.invoice == null && this.getC_Invoice_ID() > 0)
			this.invoice = new MInvoice(this.getCtx(), this.getC_Invoice_ID(), this.get_TrxName());
		return this.invoice;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (success)
		{
			//System.err.println("UPDATE  ICL: " + this.getDocStatus() + " <-> " + DB.getSQLValueString(null, "SELECT DocStatus FROM AFGO_CostDistr WHERE AFGO_CostDistr_ID=?", this.getAFGO_CostDistr_ID()));
			// update internal commitment documents
			for (Iterator<MAFGOCostDistrLine> lines = this.getLines(true).iterator(); lines.hasNext();)
			{
				for (Iterator<MAFGOCostDistrAllocation> allocations = lines.next().getAllocations().iterator(); allocations.hasNext();)
				{
					MAFGOInternalCommitmentLine internalCommitmentLine = allocations.next().getInternalCommitmentLine();
					if (internalCommitmentLine == null)
						continue;
					
					log.fine(internalCommitmentLine.getHeader().getDocumentNo() + " - " + internalCommitmentLine.getLine());
					
					internalCommitmentLine.updateAllocatedAmt();
					internalCommitmentLine.save();

				}
			}
		}
		
		return success;
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
		
		ArrayList<MAFGOCostDistrLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		// TODO: move to ICTU ModelValidator
		// clear existing approvals
		log.warning("reset approval to ICTU validator");
		//this.setIsBudgetMaintainerApproved(false);
		//this.setIsBudgetOwnerApproved(false);
		//this.setIsFinancialApproved(false);
		
		this.setProcessingAllocation(false);
		
		this.setIsAcceptAllocation(false);
		
		this.setDocAction(DOCACTION_Complete);
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt ()
	{
		return true;
	}
	

	public boolean rejectIt ()
	{
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}
	
	public String completeIt ()
	{
		if (!this.isAllocated())
		{
			this.processMsg = "@NotAllocated@";
			return this.getDocStatus();
		}
				
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{
		setProcessed(true);
		setDocAction(DOCACTION_None);
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
		return null;
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
		MDocType dt = MDocType.get(this.getCtx(), this.getC_DocType_ID());
		return dt.getName() + " " + this.getDocumentNo();
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
	
	public static final transient String WF_NODE_CREATE_ALLOCATION = "(CreateAllocation)";
	
	public static final transient String WF_NODE_RETRY_ALLOCATION = "(RetryAllocation)";
	
}
