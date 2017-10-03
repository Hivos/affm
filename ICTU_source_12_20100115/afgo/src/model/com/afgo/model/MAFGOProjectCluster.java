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



import compiere.model.X_AFGO_ProjectCluster;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOProjectCluster.java,v 1.13.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOProjectCluster extends X_AFGO_ProjectCluster implements AllocationLevel
{
	public MAFGOProjectCluster(Ctx ctx, int AFGO_ProjectCluster_ID, String trxName)
	{
		super(ctx, AFGO_ProjectCluster_ID, trxName);
	}
	
	public MAFGOProjectCluster(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	public MAFGOProjectCluster(MAFGOProgram program)
	{
		this(program.getCtx(), 0, program.get_TrxName());
		
		this.program = program;
		
		this.setAFGO_Program_ID(this.program.getAFGO_Program_ID());
		this.setName(Msg.getMsg(this.getCtx(), "Standard") + " " + Msg.translate(this.getCtx(), "AFGO_ProjectCluster_ID"));
	}
	
	private MAFGOProgram program = null;
	
	private MAFGOProject project = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public int getLevel()
	{
		return LEVEL;
	}
	
	public MAFGOProgram getParent()
	{
		return this.getProgram();
	}
	
	public MAFGOProject getDefaultChild()
	{
		return this.getProject();
	}
	
	public String getQualifiedName()
	{
		return this.getProgram().getQualifiedName() + ".[" + this.getName() + "]";
	}
	
	public MAFGOProgram getProgram()
	{
		if (this.program == null)
			this.program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
		return this.program;
	}
	
	public MAFGOProject getProject()
	{
		if (this.project == null)
			this.project = MAFGOProject.getProject(this.getCtx(), this.getAFGO_Project_ID());
		return this.project;
	}
	
	public MAFGOServiceType getServiceType()
	{
		return MAFGOServiceType.getServiceType(this.getCtx(), this.getAFGO_ServiceType_ID());
	}
	
	public MAFGOServiceType getDefaultServiceType()
	{
		MAFGOServiceType defaultServiceType = this.getServiceType();
		
		if (defaultServiceType == null)
			defaultServiceType = this.getProgram().getDefaultServiceType();
		
		return defaultServiceType;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		this.setAD_Org_ID(this.getProgram().getAD_Org_ID());
		
		if (!newRecord && this.getProgram().getAFGO_ProjectCluster_ID() == this.getAFGO_ProjectCluster_ID())
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
		
		// create the default project
		MAFGOProject project = new MAFGOProject(this);

		if (project.save())
		{
			this.setAFGO_Project_ID(project.getAFGO_Project_ID());
			return this.save();
		}
		
		return false;
	}
	
	// Estimated 5 project clusters per program on average
	private static final transient int CACHE_SIZE = 5;
	
	public static int getCacheSize()
	{
		return MAFGOProgram.getCacheSize() * CACHE_SIZE;
	}
	
	public static final transient int LEVEL	= 40;
	
	private static CCache<Integer,MAFGOProjectCluster> s_cache = new CCache<Integer,MAFGOProjectCluster>(MAFGOProjectCluster.Table_Name, getCacheSize());

	public static MAFGOProjectCluster getProjectCluster(Ctx ctx, int AFGO_ProjectCluster_ID)
	{
		MAFGOProjectCluster projectCluster = (AFGO_ProjectCluster_ID > 0) ? s_cache.get(ctx, AFGO_ProjectCluster_ID) : null;
		
		if (AFGO_ProjectCluster_ID > 0 && projectCluster == null)
		{
			projectCluster = new MAFGOProjectCluster(ctx, AFGO_ProjectCluster_ID, null);
			
			if (projectCluster.getAFGO_ProjectCluster_ID() == AFGO_ProjectCluster_ID)
				s_cache.put(projectCluster.getAFGO_ProjectCluster_ID(), projectCluster);
			else
				projectCluster = null;
		}
		
		return projectCluster;
	}
	
	public static MAFGOProjectCluster getWorkflowProjectCluster(MWFActivity workflowActivity, AllocatableDocument doc)
	{
		MAFGOProjectCluster projectCluster = null;
		
		for (Iterator<? extends AllocatableDocumentLine> lines = doc.getLines().iterator(); lines.hasNext();)
		{
			AllocatableDocumentLine line = lines.next();
			
			if (projectCluster != null && projectCluster.getAFGO_ProjectCluster_ID() != line.getWorkflowActivity(workflowActivity).getPhase().getProject().getProjectCluster().getAFGO_ProjectCluster_ID())
			{
				projectCluster = null;
				break;
			}
			
			projectCluster = line.getWorkflowActivity(workflowActivity).getPhase().getProject().getProjectCluster();
		}
		
		return projectCluster;
	}
}
