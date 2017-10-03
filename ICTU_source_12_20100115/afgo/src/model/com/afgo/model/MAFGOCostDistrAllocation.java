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

import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;

import compiere.model.X_AFGO_CostDistrAllocation;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOCostDistrAllocation.java,v 1.16.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MAFGOCostDistrAllocation extends X_AFGO_CostDistrAllocation implements AllocatableDocument, AllocatableDocumentLine
{

	public MAFGOCostDistrAllocation (Ctx ctx, int AFGO_CostDistrAllocation_ID, String trxName)
	{
		super(ctx, AFGO_CostDistrAllocation_ID, trxName);
	}
	
	public MAFGOCostDistrAllocation (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOCostDistrLine line = null;
	
	private ArrayList<MAFGOCostDistrAllocation> lines = null;
	
	private MAFGOInternalCommitmentLine internalCommitmentLine = null;
	
	private MBPartner bpartner = null;
	
	private MDocType docType = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
			this.bpartner = this.getCostDistributionLine().getHeader().getProgram().getBPartner();
		return this.bpartner;
	}
	
	public MDocType getDocType()
	{
		return this.getCostDistributionLine().getHeader().getDocType();
	}
	
	public void resetWorkflow()
	{
		if (DocActionConstants.STATUS_Completed.equals(this.getDocStatus()))
			return;
		if (DocActionConstants.STATUS_Closed.equals(this.getDocStatus()))
			return;
		
		this.setDocStatus(DocActionConstants.STATUS_Drafted);
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public MAFGOCostDistrLine getCostDistributionLine()
	{
		if (this.line == null && this.getAFGO_CostDistrLine_ID() > 0)
			this.line = new MAFGOCostDistrLine(this.getCtx(), this.getAFGO_CostDistrLine_ID(), this.get_TrxName());
		return this.line;
	}
	
	public ArrayList<MAFGOCostDistrAllocation> getLines()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOCostDistrAllocation>();
			this.lines.add(this);
		}
		
		return this.lines;
	}
	
	public MAFGOInternalCommitmentLine getInternalCommitmentLine()
	{
		if (this.internalCommitmentLine == null && this.getAFGO_InternalCommitmentLine_ID() > 0)
		{
			//System.err.println("ICL: reload=" + true);
			this.internalCommitmentLine = new MAFGOInternalCommitmentLine(this.getCtx(), this.getAFGO_InternalCommitmentLine_ID(), this.get_TrxName());
		}
		return this.internalCommitmentLine;
	}
	
	public MAFGOActivity getActivity()
	{
		return MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}
	
	public BigDecimal getQty()
	{
		return Env.ONE;
	}
	
	public BigDecimal getPrice()
	{
		return this.getLineNetAmt();
	}
	
	public int getM_Product_ID()
	{
		return 0;
	}
	
	public MAFGOCostDistrAllocation getHeader()
	{
		return this;
	}
	
	public ArrayList<MAFGOMonth> getMonths()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		months.add(MAFGOMonth.getMonth(this.getCtx(), this.getAFGO_Month_ID()));
		
		return months;
	}

	public PO getPO()
	{
		return this;
	}

	public MAFGOProgram getProgram()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getCostDistributionLine().getHeader().getTo_Program_ID());
	}
	
	public MAFGOProgram getWorkflowProgram(MWFActivity activity)
	{
		return this.getCostDistributionLine().getHeader().getWorkflowProgram(activity);
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		//BigDecimal allocatedAmt = DB.getSQLValueBD(this.get_TrxName(), "", this.getAFGO_CostDistrAllocation_ID());
		
		// always org of target organization
		this.setAD_Org_ID(this.getProgram().getAD_Org_ID());
		
		return true;
	}

	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	public boolean afterDelete(boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	private boolean updateHeader()
	{
		boolean result = true;
		
		PreparedStatement pstmt = null;
		
		String 	sql = "UPDATE AFGO_CostDistrLine cdl"
			+ " SET cdl.AllocatedAmt=(SELECT SUM(LineNetAmt) FROM AFGO_CostDistrAllocation WHERE AFGO_CostDistrLine_ID=cdl.AFGO_CostDistrLine_ID)"
			+ " WHERE cdl.AFGO_CostDistrLine_ID=?";
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_CostDistrLine_ID());
			if (pstmt.executeUpdate() != 1)
				result = false;
			
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return result;
	}
	
	public boolean processIt (String processAction) throws Exception
	{
		throw new IllegalStateException("Not Implemented");
	}

	public String prepareIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public boolean approveIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	

	public boolean rejectIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public String completeIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public boolean closeIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public boolean reActivateIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public boolean voidIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public boolean invalidateIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public boolean reverseAccrualIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public boolean reverseCorrectIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public boolean unlockIt ()
	{
		throw new IllegalStateException("Not Implemented");
	}
	
	public File createPDF ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public BigDecimal getApprovalAmt ()
	{
		return this.getLineNetAmt();
	}

	public int getDoc_User_ID ()
	{
		return this.getCreatedBy();
	}

	public String getDocumentInfo ()
	{
		return this.getDocumentNo();
	}

	public String getSummary ()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.getDocumentNo());
		sb.append(": ");
		sb.append(Msg.translate(getCtx(),"GrandTotal"));
		sb.append("=");
		sb.append(this.getLineNetAmt());
		sb.append(" ");
		if (this.getDescription() != null && this.getDescription().length() > 0)
			sb.append(this.getDescription());
		return sb.toString();
	}
	
	public String getProcessMsg ()
	{
		return null;
	}

	public int getC_Currency_ID ()
	{
		return 0;
	}

	public String getDocAction ()
	{
		return null;
	}

	public String getDocStatus ()
	{
		return null;
	}

	public String getDocumentNo ()
	{
		return null;
	}

	public void setDocStatus (String docStatus)
	{
		
	}

}
