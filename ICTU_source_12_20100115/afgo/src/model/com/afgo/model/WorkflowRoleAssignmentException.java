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

import org.compiere.model.MMessage;
import org.compiere.model.MOrg;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.wf.MWFActivity;

import com.afgo.util.UserNotification;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: WorkflowRoleAssignmentException.java,v 1.7.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class WorkflowRoleAssignmentException extends Exception
{
	public WorkflowRoleAssignmentException(AllocatableDocument doc, MWFActivity workflowActivity, MAFGOWorkflowRole workflowRole, AllocationLevel level, String msgValue)
	{
		this.workflowRole = workflowRole;
		this.level = level;
		this.msgValue = msgValue;
		
		try
		{
			MOrg docOrg = MOrg.get(doc.getCtx(), doc.getAD_Org_ID());

			if (workflowActivity.getAD_WF_Node_ID() != MAFGOWorkflowRoleAssignment.getWFNodeByValue(workflowActivity,  WORKFLOW_NODE_DOCAUTO).getAD_WF_Node_ID())
			{
				// document owner
				UserNotification notification = new UserNotification(doc.getCtx(), 0, doc.getDoc_User_ID(), this.getADMessage(), this.getMessageValue(), this.getMessage() + ": " + doc.getDocumentInfo(), doc.getPO());
				notification.setAD_WF_Activity_ID(workflowActivity.getAD_WF_Activity_ID());
				notification.send();
				
				// supervisor
				notification = new UserNotification(doc.getCtx(), 0, docOrg.getInfo().getSupervisor_ID(), this.getADMessage(), this.getMessageValue(), this.getMessage() + ": " + doc.getDocumentInfo(), doc.getPO());
				notification.setAD_WF_Activity_ID(workflowActivity.getAD_WF_Activity_ID());
				notification.send();
				
				doc.resetWorkflow();
				doc.save();
				
				workflowActivity.setAD_WF_Node_ID(MAFGOWorkflowRoleAssignment.getWFNodeByValue(workflowActivity,  WORKFLOW_NODE_DOCAUTO).getAD_WF_Node_ID());
				workflowActivity.addTextMsg(this.getMessage());
				workflowActivity.forwardTo(docOrg.getInfo().getSupervisor_ID(), this.getMessageValue());
			}
		}
		catch(Exception e)
		{
			log.severe("Workflow routing problem: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private MAFGOWorkflowRole workflowRole = null;
	
	private AllocationLevel level = null;
	
	private String msgValue = null;
	
	private MMessage message = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public Ctx getCtx()
	{
		return this.workflowRole.getCtx();
	}
	
	public MAFGOWorkflowRole getWorkflowRole()
	{
		return this.workflowRole;
	}
	
	public AllocationLevel getLevel()
	{
		return this.level;
	}
	
	public String getMessageValue()
	{
		return this.msgValue;
	}
	
	public MMessage getADMessage()
	{
		if (this.message == null && this.msgValue != null)
			this.message = MMessage.get(this.getCtx(), this.msgValue);
		
		return this.message;
	}
	
	public int getAD_Message_ID()
	{
		if (this.getADMessage() != null)
			return this.getADMessage().getAD_Message_ID();
		
		return 0;
	}
	
	public String getMessage()
	{
		return "Workflow Role: " + this.getWorkflowRole().getName() + ", Level: " + this.getLevel().getQualifiedName() + " " + (this.getADMessage() != null ? this.getADMessage().getMsgText() : this.msgValue);
	}
	
	public static final transient String WORKFLOW_NODE_DOCAUTO = "(DocAuto)";
	
	public static final transient String WORKFLOWROLEASSIGNMENTCONFLICT = "WorkflowRoleAssignmentConflict";
	
	public static final transient String NOVALIDWORKFLOWRESPONSIBLE = "NoValidWorkflowResponsible";
	
}
