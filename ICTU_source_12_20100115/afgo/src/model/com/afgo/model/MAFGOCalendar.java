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
import java.util.ArrayList;
import java.util.Date;

import org.compiere.framework.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.Msg;

import org.compiere.util.DB;


import compiere.model.X_AFGO_Calendar;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOCalendar.java,v 1.8.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class MAFGOCalendar extends X_AFGO_Calendar
{

	public MAFGOCalendar (Ctx ctx, int AFGO_Calendar_ID, String trxName)
	{
		super(ctx, AFGO_Calendar_ID, trxName);

	}
	
	public MAFGOCalendar (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	public ArrayList<MAFGOMonth> getMonths(Date startDate, Date endDate)
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		
		String sql = "SELECT m.AFGO_Month_ID"
			+ " FROM AFGO_Year y"
			+ " INNER JOIN AFGO_Quarter q ON (q.AFGO_Year_ID=y.AFGO_Year_ID)"
			+ " INNER JOIN AFGO_Month m ON (m.AFGO_Quarter_ID=q.AFGO_Qaurter_ID)"
			+ " WHERE y.AFGO_Calendar_ID=?" 										// 1
			+ " AND m.EndDate >=?" 													// 2
			+ " AND m.StartDate <=?";												// 3
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Calendar_ID());
			pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				int AFGO_Month_ID = rs.getInt(1);
				MAFGOMonth month = MAFGOMonth.getMonth(this.getCtx(), AFGO_Month_ID);
				
				if (month != null && month.getAFGO_Month_ID() == AFGO_Month_ID)
					months.add(month);
				
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			s_log.severe("Unable to load months: AFGO_Calendar_ID=" + this.getAFGO_Calendar_ID() +  ", startDate=" + startDate + ", endDate=" + endDate);
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return months;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.isAllowStandardQuarter() && !this.isAllowStandardMonth())
			this.setIsAllowStandardQuarter(false);
		
		return true;
	}
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOCalendar.class);
	
	private static CCache<Integer,MAFGOCalendar> s_cache = new CCache<Integer,MAFGOCalendar>(MAFGOCalendar.Table_Name, 4);
	
	public static MAFGOCalendar getCalendar(Ctx ctx, int AFGO_Calendar_ID)
	{
		MAFGOCalendar calendar = (AFGO_Calendar_ID > 0) ? s_cache.get(ctx, AFGO_Calendar_ID) : null;
		
		if (AFGO_Calendar_ID > 0 && calendar == null)
		{
			calendar = new MAFGOCalendar(ctx, AFGO_Calendar_ID, null);
			
			if (calendar.getAFGO_Calendar_ID() == AFGO_Calendar_ID)
				s_cache.put(calendar.getAFGO_Calendar_ID(), calendar);
			else
				calendar = null;
		}
		
		return calendar;
	}
	
	public static int getDays(Ctx ctx, Date startDate, Date endDate) throws CompiereUserException
	{
		return getDays(ctx, startDate, endDate, startDate, endDate);
	}
	
	public static int getDays(Ctx ctx, Date periodStart, Date periodEnd, Date startDate, Date endDate) throws CompiereUserException
	{
		if (periodStart.after(periodEnd))
			throw new CompiereUserException(Msg.getMsg(ctx, "PeriodStartDateAfterPeriodEndDate"));
		if (startDate.after(endDate))
			throw new CompiereUserException(Msg.getMsg(ctx, "StartDateAfterEndDate"));
		
		int days = 0;
		
		Date start = null;
		Date end = null;
		
		// S E PS PE 
		if (endDate.before(periodStart))
			return days;
				
		// S PS E PE
		else if ((startDate.equals(periodStart) || startDate.before(periodStart)) && endDate.before(periodEnd))
		{
			start = periodStart;
			end = endDate;
		}
		
		// S PS PE E
		else if ((startDate.equals(periodStart) || startDate.before(periodStart)) && (endDate.equals(periodEnd) || endDate.after(periodEnd)))
		{
			start = periodStart;
			end = periodEnd;
		}
		
		// PS S E PE
		else if (startDate.after(periodStart) && startDate.before(periodEnd) && endDate.after(periodStart) && endDate.before(periodEnd))
		{
			start = startDate;
			end = endDate;
		}
		
		// PS S PE E
		else if (startDate.after(periodStart) && startDate.before(periodEnd) && (endDate.equals(periodEnd) || endDate.after(periodEnd)))
		{
			start = startDate;
			end = periodEnd;
		}
		
		// PS PE S E
		else if (startDate.after(periodEnd))
			return days;
		
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append(Msg.getMsg(ctx, "InvalidDataRanges"));
			sb.append(": " + Msg.getMsg(ctx, "PeriodStart") + "=" + periodStart);
			sb.append(", " + Msg.getMsg(ctx, "PeriodEnd") + "=" + periodEnd);
			sb.append(", " + Msg.getMsg(ctx, "StartDate") + "=" + startDate);
			sb.append(", " + Msg.getMsg(ctx, "EndDate") + "=" + endDate);
			throw new CompiereUserException(sb.toString());
		}
			
		days = 1 + Math.round((end.getTime() - start.getTime()) / (1000*60*60*24));
		
		return days;
	}
	
	
	public static String validatePeriod(PO po)
	{
		s_log.fine("Validate Period: " + po);
		
		String result = null;
		
		Integer AFGO_ProjectCluster_ID = (Integer)po.get_Value("AFGO_ProjectCluster_ID");
		if (AFGO_ProjectCluster_ID == null || AFGO_ProjectCluster_ID < 1)
		{
			if (MAFGOQuotationResponseLine.Table_Name.equals(po.get_TableName()))
				return null;
			return "Validate Period: No Project Cluster"; // TODO: use AD_Message
		}
		
		Integer AFGO_Quarter_ID = (Integer)po.get_Value("AFGO_Quarter_ID");
		if (AFGO_Quarter_ID == null || AFGO_Quarter_ID < 1)
			return "Validate Period: No Quarter"; // TODO: use AD_Message
		
		Integer AFGO_Month_ID = (Integer)po.get_Value("AFGO_Month_ID");
		if (AFGO_Month_ID == null || AFGO_Month_ID < 1)
			return "Validate Period: No Month"; // TODO: use AD_Message
		
		String sql = "SELECT COUNT(*)"
			 + " FROM AFGO_Program p"
			 + " INNER JOIN AFGO_Year y ON (y.AFGO_Calendar_ID=p.AFGO_Calendar_ID)"
			 + " INNER JOIN AFGO_Quarter q ON (q.AFGO_Year_ID=y.AFGO_Year_ID)"
			 + " INNER JOIN AFGO_Month m ON (m.AFGO_Quarter_ID=q.AFGO_Quarter_ID)"
			 + " INNER JOIN AFGO_ProjectCluster pc ON (pc.AFGO_Program_ID=p.AFGO_Program_ID)"
			 + " WHERE pc.AFGO_ProjectCluster_ID=?"
			 + " AND m.AFGO_Month_ID=?";
		
		if (DB.getSQLValue(po.get_TrxName(), sql, AFGO_ProjectCluster_ID, AFGO_Month_ID) != 1)
			return "Validate Period: Invalid Quarter / Month Period"; // TODO: use AD_Message
		
		if (result == null)
			s_log.info("Valid period"); // TODO: use AD_Message
		
		return result;
	}

}
