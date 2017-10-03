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
package com.afgo.callout;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrgInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutProgramMaintainance.java,v 1.6.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class CalloutProgramMaintainance extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String org (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AD_Org_ID=" + value);
		
		Integer AD_Org_ID = (Integer)value;
		
		String sql = "SELECT COALESCE(oi.AFGO_Calendar_ID, ci.AFGO_Calendar_ID)"
			+ " FROM AD_OrgInfo oi"
			+ " INNER JOIN AD_ClientInfo ci ON (ci.AD_client_ID=oi.AD_Client_ID)"
			+ " WHERE oi.AD_Org_ID=?";
		
		mTab.setValue("AFGO_Calendar_ID", (AD_Org_ID == 0 ? null : DB.getSQLValue(null, sql, AD_Org_ID)));
			
		return "";
	}
	
	public String phase (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Phase_ID=" + value);
		
		mTab.setValue("AFGO_Project_ID", (value == null ? value : DB.getSQLValue(null, "SELECT AFGO_Project_ID FROM AFGO_Phase WHERE AFGO_Phase_ID=?", ((Integer)value).intValue())));
		
		return "";
	}
	
	public String project (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Project_ID=" + value);
		
		mTab.setValue("AFGO_ProjectCluster_ID", (value == null ? value : DB.getSQLValue(null, "SELECT AFGO_ProjectCluster_ID FROM AFGO_Project WHERE AFGO_Project_ID=?", ((Integer)value).intValue())));
		
		return "";
	}
	
	public String projectCluster (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_ProjectCluster_ID=" + value);
		
		mTab.setValue("AFGO_Program_ID", (value == null ? value : DB.getSQLValue(null, "SELECT AFGO_Program_ID FROM AFGO_ProjectCluster WHERE AFGO_ProjectCluster_ID=?", ((Integer)value).intValue())));
		
		return "";
	}
	
}
