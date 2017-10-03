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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.compiere.framework.PO;
import org.compiere.model.MClient;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;


import compiere.model.X_AFGO_Quarter;



/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOQuarter.java,v 1.8.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOQuarter extends X_AFGO_Quarter implements ProgramPeriod
{

	public MAFGOQuarter (Ctx ctx, int AFGO_Quarter_ID, String trxName)
	{
		super(ctx, AFGO_Quarter_ID, trxName);

	}
	
	public MAFGOQuarter (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOYear year = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private ArrayList<MAFGOMonth> months = null;
	
	public PO getPO()
	{
		return this;
	}
	
	public String toString()
	{
		return "AFGO_Quarter_ID=" + this.getAFGO_Quarter_ID() + ", Name=" + this.getName();
	}
	
	public MAFGOYear getYear()
	{
		if (this.year == null)
			this.year = MAFGOYear.getYear(this.getCtx(), this.getAFGO_Year_ID());
		return this.year;
	}
	
	public ArrayList<MAFGOMonth> getMonths()
	{
		if (this.months == null)
		{
			this.months = new ArrayList<MAFGOMonth>();
			
			String sql = "SELECT AFGO_Month_ID"
				+ " FROM AFGO_Month"
				+ " WHERE AFGO_Quarter_ID=?";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_Quarter_ID());
				
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					MAFGOMonth month = MAFGOMonth.getMonth(this.getCtx(), rs.getInt(1));
					if (month != null)
						this.months.add(month);
				}
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				log.severe("Unable to load months: AFGO_Quarter_ID=" + this.getAFGO_Quarter_ID());
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.months;
	}
	
	public boolean isDefault()
	{
		return (this.getYear().getAFGO_Quarter_ID() == this.getAFGO_Quarter_ID());
	}
	
	public boolean isPeriodOpen()
	{
		return this.getMonth().isPeriodOpen();
	}
	
	public MAFGOMonth getMonth()
	{
		return MAFGOMonth.getMonth(this.getCtx(), this.getAFGO_Month_ID());
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getStartDate().after(this.getEndDate()))
		{
			this.log.saveError("StartDateAfterEndDate", Msg.getMsg(this.getCtx(), "StartDate") + "=" + this.getStartDate() + ", " + Msg.getMsg(this.getCtx(), "EndDate") + "=" + this.getEndDate());
			return false;
		}
		
		// Also close/open months
		if ((this.getIsClosed() != null) && !this.getIsClosed().equals(this.get_ValueOld("IsClosed")))
		{
			for (Iterator<MAFGOMonth> months = this.getMonths().iterator(); months.hasNext();)
			{
				MAFGOMonth month = months.next();
				if (month.getIsClosed().equals(this.getIsClosed()))
					continue;
				month.setIsClosed(this.getIsClosed());
				if (!month.save(this.get_TrxName()))
				{
					this.log.saveError("CannotOpenCloseMonth", month.getName());
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		// create months
		if (newRecord && success)
		{
			MClient client = MClient.get(getCtx());
			Locale locale = client.getLocale();

			if (locale == null && Language.getLoginLanguage() != null)
				locale = Language.getLoginLanguage().getLocale();
			if (locale == null)
				locale = Env.getLanguage(getCtx()).getLocale();

			MAFGOYear year = this.getYear();
			
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			
			MAFGOMonth standardMonth = this.createMonth(Msg.getMsg(this.getCtx(), "Standard"), this.getStartDate(), this.getEndDate());
			
			if (standardMonth == null)
				return false;
			else
			{
				this.setAFGO_Month_ID(standardMonth.getAFGO_Month_ID());
				this.save();
			}
			
			// standard quarter
			if (this.getStartDate().getMonth() == 0 && this.getEndDate().getMonth() == 11)
				return true;
			
			String[] months = null;
			
			try
			{
				DateFormatSymbols symbols = new DateFormatSymbols(locale);
				months = symbols.getShortMonths();
			}
			catch(Exception e)
			{
				months = new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Okt","Nov","Dec"};
			}
			
			try
			{
				// TODO: proper use of calendar
				
				switch (this.getStartDate().getMonth())
				{
					case 0:
					{
						// Jan
						if (this.createMonth(months[0], df.parse(year.getName() + "0101"), df.parse(year.getName() + "0131")) == null)
							return false;
						// Feb
						if (this.createMonth(months[1], df.parse(year.getName() + "0201"), df.parse(year.getName() + "0228")) == null)
							return false;
						// Mar
						if (this.createMonth(months[2], df.parse(year.getName() + "0301"), df.parse(year.getName() + "0331")) == null)
							return false;
						
						break;
					}
					
					case 3:
					{
						// Apr
						if (this.createMonth(months[3], df.parse(year.getName() + "0401"), df.parse(year.getName() + "0430")) == null)
							return false;
						// May
						if (this.createMonth(months[4], df.parse(year.getName() + "0501"), df.parse(year.getName() + "0531")) == null)
							return false;
						// Jun
						if (this.createMonth(months[5], df.parse(year.getName() + "0601"), df.parse(year.getName() + "0630")) == null)
							return false;
						
						break;
					}
					
					case 6:
					{
						// Jul
						if (this.createMonth(months[6], df.parse(year.getName() + "0701"), df.parse(year.getName() + "0731")) == null)
							return false;
						// Aug
						if (this.createMonth(months[7], df.parse(year.getName() + "0801"), df.parse(year.getName() + "0831")) == null)
							return false;
						// Sep
						if (this.createMonth(months[8], df.parse(year.getName() + "0901"), df.parse(year.getName() + "0930")) == null)
							return false;
						
						break;
					}
					
					case 9:
					{
						// Okt
						if (this.createMonth(months[9], df.parse(year.getName() + "1001"), df.parse(year.getName() + "1031")) == null)
							return false;
						// Nov
						if (this.createMonth(months[10], df.parse(year.getName() + "1101"), df.parse(year.getName() + "1130")) == null)
							return false;
						// Dec
						if (this.createMonth(months[11], df.parse(year.getName() + "1201"), df.parse(year.getName() + "1231")) == null)
							return false;
						
						break;
					}
					
					default:
					{
						log.warning("No such month: " + this.getStartDate().getMonth());
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	private MAFGOMonth createMonth(String name, Date startDate, Date endDate)
	{
		MAFGOMonth m = new MAFGOMonth(this.getCtx(), 0, this.get_TrxName());
		m.setAFGO_Quarter_ID(this.getAFGO_Quarter_ID());
		m.setName(this.getName() + " " + name);
		m.setStartDate(new Timestamp(startDate.getTime()));
		m.setEndDate(new Timestamp(endDate.getTime()));
		if (!m.save())
			return null;
		return m;
	}

	public int getDays() throws CompiereUserException
	{
		return this.getDays(this.getStartDate(), this.getEndDate());
	}

	public int getDays (Date startDate, Date endDate) throws CompiereUserException
	{
		return MAFGOCalendar.getDays(this.getCtx(), this.getStartDate(), this.getEndDate(), startDate, endDate);
	}
	
	private static CCache<Integer,MAFGOQuarter> s_cache = new CCache<Integer,MAFGOQuarter>(MAFGOQuarter.Table_Name, 40);
	
	public static MAFGOQuarter getQuarter(Ctx ctx, int AFGO_Quarter_ID)
	{
		MAFGOQuarter quarter = (AFGO_Quarter_ID > 0) ? s_cache.get(ctx, AFGO_Quarter_ID) : null;
		
		if (quarter == null && AFGO_Quarter_ID > 0)
		{
			quarter = new MAFGOQuarter(ctx, AFGO_Quarter_ID, null);
			if (AFGO_Quarter_ID == quarter.getAFGO_Quarter_ID())
				s_cache.put(AFGO_Quarter_ID, quarter);
			else
				quarter = null;
		}
		
		return quarter;
	}

}
