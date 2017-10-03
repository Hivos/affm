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

import java.sql.ResultSet;
import java.util.Iterator;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.wf.MWFActivity;



import compiere.model.X_AFGO_Phase;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPhase.java,v 1.12.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class MAFGOPhase extends X_AFGO_Phase implements AllocationLevel
{
	public MAFGOPhase(Ctx ctx, int AFGO_Phase_ID, String trxName) 
	{
		super(ctx, AFGO_Phase_ID, trxName);
	}
	
	public MAFGOPhase(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	public MAFGOPhase(MAFGOProject project)
	{
		this(project.getCtx(), 0, project.get_TrxName());
		
		this.project = project;
		this.setAFGO_Project_ID(this.project.getAFGO_Project_ID());
		this.setName(Msg.getMsg(this.getCtx(), "Standard") + " " + Msg.translate(this.getCtx(), "AFGO_Phase_ID"));
	}
	
	private MAFGOProject project = null;
	
	private MAFGOActivity activity = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public int getLevel()
	{
		return LEVEL;
	}
	
	public MAFGOProject getParent()
	{
		return this.getProject();
	}
	
	public MAFGOActivity getDefaultChild()
	{
		return this.getActivity();
	}
	
	public String getQualifiedName()
	{
		return this.getProject().getQualifiedName() + ".[" + this.getName() + "]";
	}
	
	public MAFGOProject getProject()
	{
		if (this.project == null)
			this.project = MAFGOProject.getProject(this.getCtx(), this.getAFGO_Project_ID());
		return this.project;
	}
	
	public MAFGOActivity getActivity()
	{
		if (this.activity == null)
			this.activity = MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
		return this.activity;
	}
	
	public MAFGOServiceType getServiceType()
	{
		return MAFGOServiceType.getServiceType(this.getCtx(), this.getAFGO_ServiceType_ID());
	}
	
	public MAFGOServiceType getDefaultServiceType()
	{
		MAFGOServiceType defaultServiceType = this.getServiceType();
		
		if (defaultServiceType == null)
			defaultServiceType = this.getProject().getDefaultServiceType();
		
		return defaultServiceType;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		this.setAD_Org_ID(this.getProject().getAD_Org_ID());
		
		if (!newRecord && this.getProject().getAFGO_Phase_ID() == this.getAFGO_Phase_ID())
		{
			this.log.saveError("CannotUpdateStandardRecord", "");
			return false;
		}
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (!newRecord || !success)
			return success;
		
		//create the default Activity
		MAFGOActivity activity = new MAFGOActivity(this);

		if (activity.save())
		{
			this.setAFGO_Activity_ID(activity.getAFGO_Activity_ID());
			return this.save();
		}
		
		return false;
	}
	
	public static final transient int LEVEL			= 20;
	
	// Estimated 5 phases per project
	private static final transient int CACHE_SIZE = 5;
	
	public static int getCacheSize()
	{
		return MAFGOProject.getCacheSize() * CACHE_SIZE;
	}
	
	private static CCache<Integer,MAFGOPhase> s_cache = new CCache<Integer,MAFGOPhase>(MAFGOPhase.Table_Name, getCacheSize());
	
	public static MAFGOPhase getPhase(Ctx ctx, int AFGO_Phase_ID)
	{
		MAFGOPhase phase = (AFGO_Phase_ID > 0) ? s_cache.get(ctx, AFGO_Phase_ID) : null;
		
		if (AFGO_Phase_ID > 0 && phase == null)
		{
			phase = new MAFGOPhase(ctx, AFGO_Phase_ID, null);
			
			if (phase.getAFGO_Phase_ID() == AFGO_Phase_ID)
				s_cache.put(phase.getAFGO_Phase_ID(), phase);
			else
				phase = null;
		}
		
		return phase;
	}
	
	public static MAFGOPhase getWorkflowPhase(MWFActivity workflowActivity, AllocatableDocument doc)
	{
		MAFGOPhase phase = null;
		
		for (Iterator<? extends AllocatableDocumentLine> lines = doc.getLines().iterator(); lines.hasNext();)
		{
			AllocatableDocumentLine line = lines.next();
			
			if (phase != null && phase.getAFGO_Phase_ID() != line.getWorkflowActivity(workflowActivity).getPhase().getAFGO_Phase_ID())
			{
				phase = null;
				break;
			}
			
			phase = line.getWorkflowActivity(workflowActivity).getPhase();
		}
		
		return phase;
	}

}
