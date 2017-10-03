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


import java.util.ArrayList;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.framework.POInfo;
import org.compiere.util.CLogger;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFProcess;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @versioN $Id: WorkflowRoutingValidator.java,v 1.13.2.1 2010/01/06 11:45:29 tomassen Exp $
 * 
 * Reassign AD_WF_Activity based on workflow responsibles.
 * (defined in AFGO_WorkflowRoleAssignment)
 */
public class WorkflowRoutingValidator implements ModelValidator
{
	public WorkflowRoutingValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());

	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		
		this.log.info("WorkflowRoutingValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		// AD_WF_Process
		engine.addModelChange(MWFProcess.Table_Name, this);
		
		// AD_WF_Activity
		engine.addModelChange(MWFActivity.Table_Name, this);
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String modelChange (PO po, int type) throws Exception
	{
		POInfo poinfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID());
		
		if (MWFProcess.Table_Name.equals(poinfo.getTableName()))
		{
			MWFProcess process = (MWFProcess)po;
			AllocatableDocument doc = AFGOModelValidator.castAllocatableDocument(process.getPO());
			
			if (doc != null)
			{
	 			if (doc.getAD_Org_ID() != process.getAD_Org_ID())
    			{
    				log.fine("DocOrg=" + doc.getAD_Org_ID() + ", ProcessOrg=" + process.getAD_Org_ID());
    				process.setAD_Org_ID(doc.getAD_Org_ID());
    			}
			}
		}
		
		// dynamic routing of workflow activities
		else if (MWFActivity.Table_Name.equals(poinfo.getTableName()))
		{
			MWFActivity activity = (MWFActivity)po;
			AllocatableDocument doc = AFGOModelValidator.castAllocatableDocument(activity.getPO());
			
			if (doc != null)
			{
    			if (doc.getAD_Org_ID() != activity.getAD_Org_ID())
    			{
    				log.fine("DocOrg=" + doc.getAD_Org_ID() + ", ActivityOrg=" + activity.getAD_Org_ID());
    				activity.setAD_Org_ID(doc.getAD_Org_ID());
    			}
    			
    			if (activity.get_ValueDifference("AD_WF_Responsible_ID") != null)
    			{
    				try 
    				{
    					doc.routeWorkflowActivity(activity);
    				}
    				catch(WorkflowRoleAssignmentException e)
    				{
    					log.warning(e.getMessage());
    				}
    				//MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity);
    			}
			}
		}
		
		return null;
	}
	
	public String docValidate (PO arg0, int arg1)
	{
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
