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



import compiere.model.X_AFGO_Project;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOProject.java,v 1.12.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOProject extends X_AFGO_Project implements AllocationLevel
{

	public MAFGOProject(Ctx ctx, int AFGO_Project_ID, String trxName) 
	{
		super(ctx, AFGO_Project_ID, trxName);
	}
	
	public MAFGOProject(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	public MAFGOProject(MAFGOProjectCluster projectCluster)
	{
		this(projectCluster.getCtx(), 0, projectCluster.get_TrxName());
		
		this.projectCluster = projectCluster;
		this.setAFGO_ProjectCluster_ID(this.projectCluster.getAFGO_ProjectCluster_ID());
		this.setName(Msg.getMsg(this.getCtx(), "Standard") + " " + Msg.translate(this.getCtx(), "AFGO_Project_ID"));
	}
	
	private MAFGOProjectCluster projectCluster = null;
	
	private MAFGOPhase phase = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public int getLevel()
	{
		return LEVEL;
	}
	
	public MAFGOProjectCluster getParent()
	{
		return this.getProjectCluster();
	}
	
	public MAFGOPhase getDefaultChild()
	{
		return this.getPhase();
	}
	
	public String getQualifiedName()
	{
		return this.getProjectCluster().getQualifiedName() + ".[" + this.getName() + "]";
	}
	
	public MAFGOProjectCluster getProjectCluster()
	{
		if (this.projectCluster == null)
			this.projectCluster = MAFGOProjectCluster.getProjectCluster(this.getCtx(), this.getAFGO_ProjectCluster_ID());
		return this.projectCluster;
	}
	
	public MAFGOPhase getPhase()
	{
		if (this.phase == null)
			this.phase = MAFGOPhase.getPhase(this.getCtx(), this.getAFGO_Phase_ID());
		return this.phase;
	}
	
	public MAFGOServiceType getServiceType()
	{
		return MAFGOServiceType.getServiceType(this.getCtx(), this.getAFGO_ServiceType_ID());
	}
	
	public MAFGOServiceType getDefaultServiceType()
	{
		MAFGOServiceType defaultServiceType = this.getServiceType();
		
		if (defaultServiceType == null)
			defaultServiceType = this.getProjectCluster().getDefaultServiceType();
		
		return defaultServiceType;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		this.setAD_Org_ID(this.getProjectCluster().getAD_Org_ID());
		
		if (!newRecord && this.getProjectCluster().getAFGO_Project_ID() == this.getAFGO_Project_ID())
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
		
		// create the default Phase
		MAFGOPhase phase = new MAFGOPhase(this);
		
		if (phase.save())
		{
			this.setAFGO_Phase_ID(phase.getAFGO_Phase_ID());
			return this.save();
		}
		
		return false;
	}
	
	public static final transient int LEVEL			= 30;
	
	// Estimated 5 projects per project cluster on average
	private static final transient int CACHE_SIZE = 5;
	
	public static int getCacheSize()
	{
		return MAFGOProjectCluster.getCacheSize() * CACHE_SIZE;
	}
	
	private static CCache<Integer,MAFGOProject> s_cache = new CCache<Integer,MAFGOProject>(MAFGOProject.Table_Name, getCacheSize());

	public static MAFGOProject getProject(Ctx ctx, int AFGO_Project_ID)
	{
		MAFGOProject project = (AFGO_Project_ID > 0) ? s_cache.get(ctx, AFGO_Project_ID) : null;
		
		if (AFGO_Project_ID > 0 && project == null)
		{
			project = new MAFGOProject(ctx, AFGO_Project_ID, null);
			
			if (project.getAFGO_Project_ID() == AFGO_Project_ID)
				s_cache.put(project.getAFGO_Project_ID(), project);
			else
				project = null;
		}
		
		return project;
	}
	
	public static MAFGOProject getWorkflowProject(MWFActivity workflowActivity, AllocatableItem doc)
	{
		MAFGOProject project = null;
		
		for (Iterator<? extends AllocatableItemLine> lines = doc.getLines().iterator(); lines.hasNext();)
		{
			AllocatableItemLine line = lines.next();
			
			if (project != null && project.getAFGO_Project_ID() != line.getWorkflowActivity(workflowActivity).getPhase().getProject().getAFGO_Project_ID())
			{
				project = null;
				break;
			}
			
			project = line.getWorkflowActivity(workflowActivity).getPhase().getProject();
		}
		
		return project;
	}

}
