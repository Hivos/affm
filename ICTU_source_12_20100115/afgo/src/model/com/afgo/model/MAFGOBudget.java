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

import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.process.DocumentEngine;


import compiere.model.X_AFGO_Budget;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOBudget.java,v 1.17.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class MAFGOBudget extends X_AFGO_Budget implements AllocatableDocument
{

	public MAFGOBudget(Ctx ctx, int AFGO_Budget_ID, String trxName) 
	{
		super(ctx, AFGO_Budget_ID, trxName);
	}
	
	public MAFGOBudget(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOProgram program = null;
	
	private ArrayList<MAFGOBudgetLine> lines = null;
	
	private String processMsg = null;
	
	private MBPartner bpartner = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String toString()
	{
		return this.getDocumentInfo();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public MAFGOProgram getProgram()
	{
		if (this.program == null && this.getAFGO_Program_ID() > 0)
			this.program = new MAFGOProgram(this.getCtx(), this.getAFGO_Program_ID(), this.get_TrxName());
		return this.program;
	}
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
		{
			this.bpartner = this.getProgram().getBPartner();
		}
		return this.bpartner;
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
		
		// TODO: Move to ICTU ModelValidator
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
	
	public ArrayList<MAFGOBudgetLine> getLines()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOBudgetLine>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_BudgetLine"
				+ " WHERE AFGO_Budget_ID=?"
				+ " ORDER BY Line";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_Budget_ID());
				
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					this.lines.add(new MAFGOBudgetLine(this.getCtx(), rs, this.get_TrxName()));
				}
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				log.severe("Unable to load lines: AFGO_Budget_ID=" + this.getAFGO_Budget_ID());
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.lines;
	}
	
	public ArrayList<MAFGOMonth> getMonths()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		
		for (Iterator<MAFGOBudgetLine> lines = this.getLines().iterator(); lines.hasNext();)
			months.add(lines.next().getMonth());
		
		return months;
	}
	
	public boolean processIt (String processAction) throws Exception
	{
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}
	
	public String prepareIt ()
	{
		log.fine(toString());
		processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_BEFORE_PREPARE);
		if (processMsg != null)
			return DocActionConstants.STATUS_Invalid;
		
		ArrayList<MAFGOBudgetLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		this.setDocAction(DOCACTION_Complete);
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt ()
	{
		log.info(toString());
		return true;
	}
	
	public boolean rejectIt ()
	{
		log.info(toString());
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}

	public String completeIt ()
	{
		log.info(toString());
		this.processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_AFTER_COMPLETE);
		if (this.processMsg != null)
			return DOCSTATUS_Invalid;
		
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}
	
	public boolean reActivateIt ()
	{
		log.info(toString());
		return false;
	}

	public boolean voidIt ()
	{
		log.info(toString());
		if (DOCSTATUS_Completed.equals(this.getDocStatus()) || DOCSTATUS_Closed.equals(this.getDocStatus()))
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		
		return true;
	}
	
	public boolean invalidateIt ()
	{
		log.info(toString());
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}
	
	public boolean reverseAccrualIt ()
	{
		log.info(toString());
		return false;
	}
	

	public boolean reverseCorrectIt ()
	{
		log.info(toString());
		return false;
	}

	public boolean unlockIt ()
	{
		this.log.info("unlockIt: " + this.toString());
		this.setProcessing(false);
		return true;
	}

	public File createPDF ()
	{
		throw new IllegalStateException("No Implemented");
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

}
