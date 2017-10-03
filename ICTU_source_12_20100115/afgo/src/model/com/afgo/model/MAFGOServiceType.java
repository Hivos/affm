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
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;


import compiere.model.X_AFGO_ServiceType;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOServiceType.java,v 1.8.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOServiceType extends X_AFGO_ServiceType
{

	public MAFGOServiceType (Ctx ctx, int AFGO_ServiceType_ID, String trxName)
	{
		super(ctx, AFGO_ServiceType_ID, trxName);
		
	}
	
	public MAFGOServiceType (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		
	}
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOServiceType.class);
	
	
	public static String validateServiceType(PO po)
	{
		s_log.fine("Validate Service Type: " + po);
		
		String result = null;
	
		Integer AFGO_ServiceType_ID = (Integer)po.get_Value("AFGO_ServiceType_ID");
		if (AFGO_ServiceType_ID == null || AFGO_ServiceType_ID < 0)
		{
			if (MAFGOQuotationResponseLine.Table_Name.equals(po.get_TableName()))
				return null;
			return Msg.getMsg(po.getCtx(), "NoServiceType");
		}
		
		Integer AFGO_ProjectCluster_ID = (Integer)po.get_Value("AFGO_ProjectCluster_ID");
		if (AFGO_ProjectCluster_ID == null || AFGO_ProjectCluster_ID < 1)
			return Msg.getMsg(po.getCtx(), "NoProjectCluster");
		
		String sql = "SELECT st.AFGO_ServiceType_ID"
			+ " FROM AFGO_ProjectCluster pc"
			+ " INNER JOIN AFGO_ServiceType st ON (st.AFGO_Program_ID=pc.AFGO_Program_ID OR st.AFGO_Program_ID IS NULL)"
			+ " WHERE st.AFGO_ServiceType_ID=?"
			+ " AND pc.AFGO_ProjectCluster_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, po.get_TrxName());
			pstmt.setInt(1, AFGO_ServiceType_ID);
			pstmt.setInt(2, AFGO_ProjectCluster_ID);
			
			rs = pstmt.executeQuery();
			if (!rs.next())
				result = Msg.getMsg(po.getCtx(), "InvalidServiceType");
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
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		if (result == null)
			s_log.fine("Valid service type");
		
		return result;
	}
	
	// Estimated 50 service types on average
	private static final transient int CACHE_SIZE = 50;
	
	public static int getCacheSize()
	{
		return CACHE_SIZE;
	}
	
	private static CCache<Integer,MAFGOServiceType> s_cache = new CCache<Integer,MAFGOServiceType>(MAFGOServiceType.Table_Name, getCacheSize());
	
	public static MAFGOServiceType getServiceType(Ctx ctx, int AFGO_ServiceType_ID)
	{
		MAFGOServiceType serviceType = (AFGO_ServiceType_ID > 0) ? s_cache.get(ctx, AFGO_ServiceType_ID) : null;
		
		if (AFGO_ServiceType_ID > 0 && serviceType == null)
		{
			serviceType = new MAFGOServiceType(ctx, AFGO_ServiceType_ID, null);
			
			if (serviceType.getAFGO_ServiceType_ID() == AFGO_ServiceType_ID)
				s_cache.put(serviceType.getAFGO_ServiceType_ID(), serviceType);
			else
				serviceType = null;
		}
		
		return serviceType;
	}
}
