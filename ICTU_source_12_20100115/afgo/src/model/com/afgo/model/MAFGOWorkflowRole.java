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

import org.compiere.util.CCache;
import org.compiere.util.Ctx;


import compiere.model.X_AFGO_WorkflowRole;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOWorkflowRole.java,v 1.7.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOWorkflowRole extends X_AFGO_WorkflowRole
{

	public MAFGOWorkflowRole (Ctx ctx, int AFGO_WorkflowRole_ID, String trxName)
	{
		super(ctx, AFGO_WorkflowRole_ID, trxName);
	}
	
	public MAFGOWorkflowRole (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private static CCache<Integer,MAFGOWorkflowRole> s_cache = new CCache<Integer,MAFGOWorkflowRole>(MAFGOWorkflowRole.Table_Name, 5);
	
	public static MAFGOWorkflowRole getWorkflowRole(Ctx ctx, int AFGO_WorkflowRole_ID)
	{
		MAFGOWorkflowRole workflowRole = (AFGO_WorkflowRole_ID > 0) ? s_cache.get(ctx, AFGO_WorkflowRole_ID) : null;
		
		if (workflowRole == null && AFGO_WorkflowRole_ID > 0)
		{
			workflowRole = new MAFGOWorkflowRole(ctx, AFGO_WorkflowRole_ID, null);
			if (AFGO_WorkflowRole_ID == workflowRole.getAFGO_WorkflowRole_ID())
				s_cache.put(AFGO_WorkflowRole_ID, workflowRole);
			else
				workflowRole = null;
		}
		
		return workflowRole;
	}
}
