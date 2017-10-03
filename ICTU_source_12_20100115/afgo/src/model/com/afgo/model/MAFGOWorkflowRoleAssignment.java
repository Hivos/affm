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

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import org.compiere.framework.PO;
import org.compiere.model.MOrg;
import org.compiere.model.MTable;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;
import org.compiere.wf.MWFResponsible;
import org.compiere.wf.MWorkflow;

import com.afgo.util.UserNotification;

import compiere.model.X_AFGO_WorkflowRoleAssignment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOWorkflowRoleAssignment.java,v 1.20.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MAFGOWorkflowRoleAssignment extends X_AFGO_WorkflowRoleAssignment
{

	public MAFGOWorkflowRoleAssignment (Ctx ctx, int AFGO_WorkflowRoleAssignment_ID, String trxName)
	{
		super(ctx, AFGO_WorkflowRoleAssignment_ID, trxName);
	}
	
	public MAFGOWorkflowRoleAssignment (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOActivity activity = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String toString()
	{
		return "AFGO_WorkflowRoleAssignment {AFGO_WorkflowRoleAssignment_ID=" + this.getAFGO_WorkflowRoleAssignment_ID() + ", AFGO_WorkflowRole_ID=" + this.getAFGO_WorkflowRole_ID() + ", AD_User_ID=" + this.getAD_User_ID() + "}";
	}

	public MAFGOActivity getActivity ()
	{
		if (this.activity == null)
			this.activity = MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
		return this.activity;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// activity
		MAFGOActivity activity = null;
		if (this.getAFGO_Activity_ID() > 0)
			activity = MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
		// phase
		MAFGOPhase phase = null;
		if (activity != null)
			phase = activity.getPhase();
		else if (this.getAFGO_Phase_ID() > 0)
			phase = MAFGOPhase.getPhase(this.getCtx(), this.getAFGO_Phase_ID());
		// project
		MAFGOProject project = null;
		if (phase != null)
			project = phase.getProject();
		else if (this.getAFGO_Project_ID() > 0)
			project = MAFGOProject.getProject(this.getCtx(), this.getAFGO_Project_ID());
		// project cluster
		MAFGOProjectCluster projectCluster= null;
		if (project != null)
			projectCluster = project.getProjectCluster();
		else if (this.getAFGO_ProjectCluster_ID() > 0)
			projectCluster = MAFGOProjectCluster.getProjectCluster(this.getCtx(), this.getAFGO_ProjectCluster_ID());
		// program
		MAFGOProgram program = null;
		if (projectCluster != null)
			program = projectCluster.getProgram();
		else if (this.getAFGO_Program_ID() > 0)
			program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
		
		if (program == null)
		{
			log.saveError("NoProgram", "AFGO_WorkflowRoleAssignment_ID=" + this.getAFGO_WorkflowRoleAssignment_ID());
			return false;
		}
		
		this.setAD_Org_ID(program.getAD_Org_ID());

		return true;
	}
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOWorkflowRoleAssignment.class);
	
	public static void routeWorkflowActivity(MWFActivity activity) throws CompiereSystemException, WorkflowRoleAssignmentException
	{
		AllocatableDocument doc = AFGOModelValidator.castAllocatableDocument(activity.getPO());
		routeWorkflowActivity(activity, doc);
	}
	
	public static void routeWorkflowActivity(MWFActivity activity, AllocatableDocument doc) throws WorkflowRoleAssignmentException, CompiereSystemException
	{
		if (doc == null)
			throw new CompiereSystemException("NoAllocatableDocument");
		
		if (activity.getPO() != null && activity.getPO().get_ID() != doc.get_ID())
			throw new CompiereSystemException("InvalidWorkflowDocument");
		
		MWFResponsible responsible = MWFResponsible.get(activity.getCtx(), activity.getAD_WF_Responsible_ID());
		
		if (!MWFResponsible.RESPONSIBLETYPE_ContextDefinedResponsible.equals(responsible.getResponsibleType()))
			return;
		
		// resolve Workflow Role assignment, based on current document allocation.

		MAFGOWorkflowRole workflowRole = MAFGOWorkflowRole.getWorkflowRole(doc.getCtx(), responsible.getAFGO_WorkflowRole_ID());
		MAFGOWorkflowRoleAssignment workflowAssignment = MAFGOWorkflowRoleAssignment.getWorkflowRoleAssignment(doc, activity, workflowRole);
		activity.setAD_User_ID(workflowAssignment.getAD_User_ID());
	}
	
	public static MAFGOWorkflowRoleAssignment getWorkflowRoleAssignment(AllocatableDocument doc, MWFActivity workflowActivity, MAFGOWorkflowRole workflowRole) throws WorkflowRoleAssignmentException
	{
		s_log.info(MTable.getTableName(doc.getCtx(), doc.get_Table_ID()) + ", DocumentNo=" + doc.getDocumentNo());
		
		MAFGOWorkflowRoleAssignment workflowRoleAssignment = null;
		int priority = -1;
		AllocationLevel level = null;
		
		String sql = "SELECT wfra.AFGO_WorkflowRoleAssignment_ID, wra.Priority"
			+ " FROM AFGO_RV_WorkflowRoleAssignment wra"
			+ " INNER JOIN AFGO_WorkflowRoleAssignment wfra ON (wfra.AFGO_WorkflowRoleAssignment_ID=wra.AFGO_WorkflowRoleAssignment_ID)"
			+ " WHERE wra.IsActive='Y'"
			+ " AND wra.AFGO_WorkflowRole_ID=?"
			+ " AND "
			+ " ( "
			+ " 		wra.AFGO_Program_ID=?";
		
		// Program
		level = doc.getWorkflowProgram(workflowActivity);
		s_log.fine("AFGO_Program_ID=" + doc.getWorkflowProgram(workflowActivity).getAFGO_Program_ID());
		
		// ProjectCluster
		MAFGOProjectCluster projectCluster = MAFGOProjectCluster.getWorkflowProjectCluster(workflowActivity, doc);
		if (projectCluster != null)
		{
			level = projectCluster;
			s_log.fine("AFGO_ProjectCluster_ID=" + projectCluster.getAFGO_ProjectCluster_ID());
			sql = sql + " 	OR wra.AFGO_ProjectCluster_ID=?";
		}

		// Project
		MAFGOProject project = MAFGOProject.getWorkflowProject(workflowActivity, doc);
		if (project != null)
		{
			level = project;
			s_log.fine("AFGO_Project_ID=" + project.getAFGO_Project_ID());
			sql = sql + " 	OR wra.AFGO_Project_ID=?";
		}
		
		// Phase
		MAFGOPhase phase = MAFGOPhase.getWorkflowPhase(workflowActivity, doc);
		if (phase != null)
		{
			level = phase;
			s_log.fine("AFGO_Phase_ID=" + phase.getAFGO_Phase_ID());
			sql = sql + " 	OR wra.AFGO_Phase_ID=?";
		}
		
		// Activity
		MAFGOActivity activity = MAFGOActivity.getWorkflowActivity(workflowActivity, doc);
		if (activity != null)
		{
			level = activity;
			s_log.fine("AFGO_Activity_ID" + activity.getAFGO_Activity_ID());
			sql = sql + " 	OR wra.AFGO_Activity_ID=?";
		}
		
		sql = sql + " )"
			+ " ORDER BY wra.Priority";

		
		s_log.fine(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, doc.get_TrxName());
			
			pstmt.setInt(1, workflowRole.getAFGO_WorkflowRole_ID());
			
			s_log.fine("AFGO_Program_ID=" + doc.getWorkflowProgram(workflowActivity).getAFGO_Program_ID());
			pstmt.setInt(2, doc.getWorkflowProgram(workflowActivity).getAFGO_Program_ID());
			
			// ProjectCluster
			if (projectCluster != null)
			{
				s_log.fine("AFGO_ProjectCluster_ID=" + projectCluster.getAFGO_ProjectCluster_ID());
				pstmt.setInt(3, projectCluster.getAFGO_ProjectCluster_ID());
			}
			
			// Project
			if (project != null)
			{
				s_log.fine("AFGO_Project_ID=" + project.getAFGO_Project_ID());
				pstmt.setInt(4, project.getAFGO_Project_ID());
			}
			
			// Phase
			if (phase != null)
			{
				s_log.fine("AFGO_Phase_ID=" + phase.getAFGO_Phase_ID());
				pstmt.setInt(5, phase.getAFGO_Phase_ID());
			}
			
			// Activity
			if (activity != null)
			{
				s_log.fine("AFGO_Activity_ID=" + activity.getAFGO_Activity_ID());
				pstmt.setInt(6, activity.getAFGO_Activity_ID());
			}
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				//workflowRoleAssignment = new MAFGOWorkflowRoleAssignment(doc.getCtx(), rs.getInt(1), doc.get_TrxName());
				workflowRoleAssignment = MAFGOWorkflowRoleAssignment.getWorkflowRoleAssignment(doc.getCtx(), rs.getInt(1));
				priority = rs.getInt(2);
			}

			while(rs.next())
			{	
				// Two assignments of the same role on the same level => error condition
				if (rs.getInt(2) == priority)
				{
					workflowRoleAssignment = null;
				}
			}
			
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
			if (rs != null) try{rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try{pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		if (workflowRoleAssignment == null)
		{
			if (priority > 0)
			{
				switch (priority)
				{
					case MAFGOProgram.LEVEL: level = doc.getWorkflowProgram(workflowActivity);
					case MAFGOProjectCluster.LEVEL: if (projectCluster != null) level = projectCluster; 
					case MAFGOProject.LEVEL: if (project != null) level = project; 
					case MAFGOPhase.LEVEL: if (phase != null) level = phase;
					case MAFGOActivity.LEVEL: if (activity != null) level = activity; 
				}
				
				throw new WorkflowRoleAssignmentException(doc, workflowActivity, workflowRole, level, WorkflowRoleAssignmentException.WORKFLOWROLEASSIGNMENTCONFLICT);
			}
			else
				throw new WorkflowRoleAssignmentException(doc, workflowActivity, workflowRole, level, WorkflowRoleAssignmentException.NOVALIDWORKFLOWRESPONSIBLE);
		}
		
		return workflowRoleAssignment;
	}
	
	public static MWFNode getWFNodeByValue(MWFActivity activity, String nodeName)
	{
		MWorkflow workflow = activity.getNode().getWorkflow();
		PO doc = activity.getPO();
		
		MWFNode[] nodes = workflow.getNodes(false, activity.getPO_AD_Client_ID(null));
		
		for (int i = 0; i < nodes.length; i++)
		{
			// 2969 search for node by Value instead of name
			if (nodes[i].getValue().equals(nodeName))
				return nodes[i];
		}
		
		return null;
	}
	
	
	private static CCache<Integer,MAFGOWorkflowRoleAssignment> s_cache = new CCache<Integer,MAFGOWorkflowRoleAssignment>(MAFGOWorkflowRoleAssignment.Table_Name, 5);
	
	public static MAFGOWorkflowRoleAssignment getWorkflowRoleAssignment(Ctx ctx, int AFGO_WorkflowRoleAssignment_ID)
	{
		MAFGOWorkflowRoleAssignment workflowRoleAssignment = (AFGO_WorkflowRoleAssignment_ID > 0) ? s_cache.get(ctx, AFGO_WorkflowRoleAssignment_ID) : null;
		
		if (workflowRoleAssignment == null && AFGO_WorkflowRoleAssignment_ID > 0)
		{
			workflowRoleAssignment = new MAFGOWorkflowRoleAssignment(ctx, AFGO_WorkflowRoleAssignment_ID, null);
			if (AFGO_WorkflowRoleAssignment_ID == workflowRoleAssignment.getAFGO_WorkflowRoleAssignment_ID())
				s_cache.put(AFGO_WorkflowRoleAssignment_ID, workflowRoleAssignment);
			else
				workflowRoleAssignment = null;
		}
		
		return workflowRoleAssignment;
	}
	
	
	public static void main(String[] args)
	{
		org.compiere.Compiere.startup(true);
		
		AllocatableDocument doc = new MAFGOFundSchedule(Env.getCtx(), 1000000, null);
		try
		{
			MAFGOWorkflowRoleAssignment workflowRoleAssignment = MAFGOWorkflowRoleAssignment.getWorkflowRoleAssignment(doc, null, MAFGOWorkflowRole.getWorkflowRole(Env.getCtx(), 1000002));
		}
		catch(WorkflowRoleAssignmentException e)
		{
			System.err.println(e.getMessage());
		}
	}

}
