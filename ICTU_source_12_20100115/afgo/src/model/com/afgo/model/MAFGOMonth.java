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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import compiere.model.X_AFGO_Month;

import org.compiere.framework.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;



/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOMonth.java,v 1.12.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MAFGOMonth extends X_AFGO_Month implements ProgramPeriod
{

	public MAFGOMonth (Ctx ctx, int AFGO_Month_ID, String trxName)
	{
		super(ctx, AFGO_Month_ID, trxName);

	}
	
	public MAFGOMonth (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOQuarter quarter = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public PO getPO()
	{
		return this;
	}
	
	public String toString()
	{
		return "AFGO_Month_ID=" + this.getAFGO_Month_ID() + ", Name=" + this.getName();
	}
	
	public MAFGOQuarter getQuarter()
	{
		if (this.quarter == null)
			this.quarter = MAFGOQuarter.getQuarter(this.getCtx(), this.getAFGO_Quarter_ID());
		return this.quarter;
	}
	
	public int getAFGO_Year_ID()
	{
		return this.getQuarter().getAFGO_Year_ID();
	}
	
	public ArrayList<MAFGOMonth> getMonths()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		months.add(this);
		return months;
	}

	public int getDays () throws CompiereUserException
	{
		return this.getDays(this.getStartDate(), this.getEndDate());
	}

	public int getDays (Date startDate, Date endDate) throws CompiereUserException
	{
		return MAFGOCalendar.getDays(this.getCtx(), this.getStartDate(), this.getEndDate(), startDate, endDate);
	}

	public boolean isDefault ()
	{
		return (this.getQuarter().getAFGO_Month_ID() == this.getAFGO_Month_ID());
	}
	
	public boolean isPeriodOpen()
	{
		// Never cache this value
		String sql = "SELECT CASE WHEN q.IsClosed='Y' THEN q.IsClosed ELSE m.IsClosed END AS IsClosed"
			+ " FROM AFGO_Month m"
			+ " INNER JOIN AFGO_Quarter q ON (q.AFGO_Quarter_ID=m.AFGO_Quarter_ID)"
			+ " WHERE m.AFGO_Month_ID=?";
		
		if ("Y".equals(DB.getSQLValueString(null, sql, this.getAFGO_Month_ID())))
			return false;
		else
			return true;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getStartDate().after(this.getEndDate()))
		{
			this.log.saveError("StartDateAfterEndDate", "start=" + this.getStartDate() + ", end=" + this.getEndDate());
			return false;
		}
		
		// Open/close quarter when standard month is opened/closed
		if ((this.getIsClosed() != null) && !(this.getIsClosed().equals(this.get_ValueOld("IsClosed"))) 
				&& (this.getQuarter().getAFGO_Month_ID() == this.getAFGO_Month_ID()))
		{
			if (!this.getQuarter().getIsClosed().equals(this.getIsClosed()))
			{
				this.getQuarter().setIsClosed(this.getIsClosed());
				if (!this.getQuarter().save(this.get_TrxName()))
				{
					this.log.saveError("CannotOpenCloseQuarter", this.getQuarter().getName());
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static CCache<Integer,MAFGOMonth> s_cache = new CCache<Integer,MAFGOMonth>(MAFGOMonth.Table_Name, 120);
	
	public static MAFGOMonth getMonth(Ctx ctx, int AFGO_Month_ID)
	{
		MAFGOMonth month = (AFGO_Month_ID > 0) ? s_cache.get(ctx, AFGO_Month_ID) : null;
		
		if (month == null && AFGO_Month_ID > 0)
		{
			month = new MAFGOMonth(ctx, AFGO_Month_ID, null);
			
			if (AFGO_Month_ID == month.getAFGO_Month_ID())
				s_cache.put(month.getAFGO_Month_ID(), month);
			else
				month = null;
		}
		
		return month;
	}
	
	public static ArrayList<MAFGOMonth> getClosedMonths(ArrayList<MAFGOMonth> months)
	{
		ArrayList<MAFGOMonth> closedMonths = new ArrayList<MAFGOMonth>();
		
		for (Iterator<MAFGOMonth> monthIterator = months.iterator(); monthIterator.hasNext();)
		{
			MAFGOMonth month = monthIterator.next();
			if (!month.isPeriodOpen())
				closedMonths.add(month);
		}
		
		return closedMonths;
	}
	
}
