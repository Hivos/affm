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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.compiere.framework.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.Msg;


import compiere.model.X_AFGO_Year;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOYear.java,v 1.10.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOYear extends X_AFGO_Year implements ProgramPeriod
{

	public MAFGOYear (Ctx ctx, int AFGO_Year_ID, String trxName)
	{
		super(ctx, AFGO_Year_ID, trxName);

	}
	
	public MAFGOYear (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private DateFormat df = null;
	
	private MAFGOQuarter quarter = null;
	
	private MAFGOCalendar calendar = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public boolean beforeSave(boolean isNew)
	{
		if (this.getStartDate() != null && this.getEndDate() != null && this.getStartDate().after(this.getEndDate()))
		{
			this.log.saveError("StartDateAfterEndDate", Msg.getMsg(this.getCtx(), "StartDate") + this.getStartDate() + ", " + Msg.getMsg(this.getCtx(), "EndDate") + this.getEndDate());
			return false;
		}
		
		if (isNew)
		{
			int year = Integer.parseInt(this.getName());
			if (year < 1970 && year > 2050)
				return false;
			
			if (this.df == null)
				this.df = new SimpleDateFormat("yyyyMMdd");
			
			try
			{
				this.setStartDate(new Timestamp(df.parse(this.getName() + "0101").getTime()));
				this.setEndDate(new Timestamp(df.parse(this.getName() + "1231").getTime()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (this.df == null)
			this.df = new SimpleDateFormat("yyyyMMdd");
		
		// create quarters
		if (newRecord && success)
		{
			try
			{
				// Standard / Unknown
				MAFGOQuarter standardQuarter = this.createQuarter(this.getName() + " " + Msg.getMsg(this.getCtx(), "Standard"), this.getStartDate(), this.getEndDate());
				if (standardQuarter == null)
					return false;
				else
				{
					this.setAFGO_Quarter_ID(standardQuarter.getAFGO_Quarter_ID());
					this.save();
				}
				
				// Q1
				if (this.createQuarter(this.getName() + " " + Msg.getMsg(this.getCtx(), "Quarter") + "1", df.parse(this.getName() + "0101"), df.parse(this.getName() + "0331")) == null)
					return false;
				
				// Q2
				if (this.createQuarter(this.getName() + " " + Msg.getMsg(this.getCtx(), "Quarter") + "2", df.parse(this.getName() + "0401"), df.parse(this.getName() + "0630")) == null)
					return false;
				
				// Q3
				if (this.createQuarter(this.getName() + " " + Msg.getMsg(this.getCtx(), "Quarter") + "3", df.parse(this.getName() + "0701"), df.parse(this.getName() + "0930")) == null)
					return false;
				
				// Q4
				if (this.createQuarter(this.getName() + " " + Msg.getMsg(this.getCtx(), "Quarter") + "4", df.parse(this.getName() + "1001"), df.parse(this.getName() + "1231")) == null)
					return false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		
		return success;
	}
	
	private MAFGOQuarter createQuarter(String name, Date startDate, Date endDate)
	{
		MAFGOQuarter q = new MAFGOQuarter(this.getCtx(), 0, this.get_TrxName());
		q.setAFGO_Year_ID(this.getAFGO_Year_ID());
		q.setName(name);
		q.setStartDate(new Timestamp(startDate.getTime()));
		q.setEndDate(new Timestamp(endDate.getTime()));
		if (!q.save())
			return null;
		return q;
	}
	
	public MAFGOQuarter getQuarter()
	{
		if (this.quarter == null && this.getAFGO_Quarter_ID() > 0)
			this.quarter = MAFGOQuarter.getQuarter(this.getCtx(), this.getAFGO_Quarter_ID());
		return this.quarter;
	}
	
	public MAFGOCalendar getCalendar()
	{
		if (this.calendar == null && this.getAFGO_Calendar_ID() > 0)
			this.calendar = new MAFGOCalendar(this.getCtx(), this.getAFGO_Calendar_ID(), this.get_TrxName());
		return this.calendar;
	}
	
	public int getAFGO_Month_ID()
	{
		return this.getQuarter().getMonth().getAFGO_Month_ID();
	}

	public int getDays() throws CompiereUserException
	{
		return 0;
	}

	public int getDays(Date startDate, Date endDate) throws CompiereUserException
	{
		return 0;
	}

	public String getIsClosed()
	{
		return null;
	}

	public ArrayList<MAFGOMonth> getMonths()
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	public PO getPO()
	{
		return this;
	}

	public boolean isDefault()
	{
		return false;
	}

	public boolean isPeriodOpen()
	{
		return false;
	}

	public void setIsClosed(String isClosed)
	{
		
	}

	//
	
	private static CCache<Integer,MAFGOYear> s_cache = new CCache<Integer,MAFGOYear>(MAFGOYear.Table_Name, 5);
	
	public static MAFGOYear getYear(Ctx ctx, int AFGO_Year_ID)
	{
		MAFGOYear year = (AFGO_Year_ID > 0) ? s_cache.get(ctx, AFGO_Year_ID) : null;
		
		if (year == null && AFGO_Year_ID > 0)
		{
			year = new MAFGOYear(ctx, AFGO_Year_ID, null);
			if (AFGO_Year_ID == year.getAFGO_Year_ID())
				s_cache.put(AFGO_Year_ID, year);
			else
				year = null;
		}
		
		return year;
	}

}
