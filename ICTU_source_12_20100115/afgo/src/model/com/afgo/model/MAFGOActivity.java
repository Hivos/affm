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


import compiere.model.X_AFGO_Activity;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOActivity.java,v 1.12.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOActivity extends X_AFGO_Activity implements AllocationLevel
{
	public MAFGOActivity(Ctx ctx, int AFGO_Activity_ID, String trxName) 
	{
		super(ctx, AFGO_Activity_ID, trxName);
	}
	
	public MAFGOActivity(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	public MAFGOActivity(MAFGOPhase phase)
	{
		this(phase.getCtx(), 0, phase.get_TrxName());
		
		this.phase = phase;
		this.setAFGO_Phase_ID(this.phase.getAFGO_Phase_ID());
		this.setName(Msg.getMsg(this.getCtx(), "Standard") + " " + Msg.translate(this.getCtx(), "AFGO_Activity_ID"));
	}
	
	private MAFGOPhase phase = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public int getLevel()
	{
		return LEVEL;
	}

	public AllocationLevel getParent()
	{
		return this.getPhase();
	}
	
	public AllocationLevel getDefaultChild()
	{
		return null;
	}
	
	public String getQualifiedName()
	{
		return this.getPhase().getQualifiedName() + ".[" + this.getName() + "]";
	}

	public MAFGOPhase getPhase()
	{
		if (this.phase == null)
			this.phase = MAFGOPhase.getPhase(this.getCtx(), this.getAFGO_Phase_ID());
		return this.phase;
	}
	
	public MAFGOServiceType getServiceType()
	{
		return MAFGOServiceType.getServiceType(this.getCtx(),this.getAFGO_ServiceType_ID());
	}
	
	public MAFGOServiceType getDefaultServiceType()
	{
		MAFGOServiceType defaultServiceType = this.getServiceType();
		
		if (defaultServiceType == null)
			defaultServiceType = this.getPhase().getDefaultServiceType();
		
		return defaultServiceType;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		this.setAD_Org_ID(this.getPhase().getAD_Org_ID());
		
		if (!newRecord && this.getPhase().getAFGO_Activity_ID() == this.getAFGO_Activity_ID())
		{
			this.log.saveError(Msg.getMsg(this.getCtx(), "CannotUpdateStandardRecord"), "");
			return false;
		}
		
		return true;
	}
	
	public static final transient int LEVEL			= 10;
	
	// Estimated 5 activities per phase on average
	private static final transient int CACHE_SIZE = 5;
	
	public static int getCacheSize()
	{
		return MAFGOPhase.getCacheSize() * CACHE_SIZE;
	}
	
	private static CCache<Integer,MAFGOActivity> s_cache = new CCache<Integer,MAFGOActivity>(MAFGOActivity.Table_Name, getCacheSize());
	
	public static MAFGOActivity getActivity(Ctx ctx, int AFGO_Activity_ID)
	{
		MAFGOActivity activity = (AFGO_Activity_ID > 0) ? s_cache.get(ctx, AFGO_Activity_ID) : null;
		
		if (AFGO_Activity_ID > 0 && activity == null)
		{
			activity = new MAFGOActivity(ctx, AFGO_Activity_ID, null);
			
			if (activity.getAFGO_Activity_ID() == AFGO_Activity_ID)
				s_cache.put(activity.getAFGO_Activity_ID(), activity);
			else
				activity = null;
		}
		
		return activity;
	}
	
	public static MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity, AllocatableDocument doc)
	{
		MAFGOActivity activity = null;
		
		for (Iterator<? extends AllocatableDocumentLine> lines = doc.getLines().iterator(); lines.hasNext();)
		{
			AllocatableDocumentLine line = lines.next();
			
			if (activity != null && line.getWorkflowActivity(workflowActivity).getAFGO_Activity_ID() != activity.getAFGO_Activity_ID())
			{
				activity = null;
				break;
			}
			
			activity = line.getWorkflowActivity(workflowActivity);
		}
		
		return activity;
	}
}
