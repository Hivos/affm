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
package com.afgo.process;


import java.math.BigDecimal;
import java.util.Iterator;

import org.compiere.model.MCurrency;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import com.afgo.model.MAFGOCalendar;
import com.afgo.model.MAFGOFund;
import com.afgo.model.MAFGOFundLine;
import com.afgo.model.MAFGOFundSchedule;
import com.afgo.model.MAFGOFundScheduleLine;
import com.afgo.model.MAFGOFundScheduleType;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.ProgramPeriod;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CreateFundInvoiceSchedule.java,v 1.16.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CreateFundInvoiceSchedule extends SvrProcess
{
	private int AD_Org_ID = -1;
	
	private int AFGO_Program_ID = -1;
	
	private int AFGO_FundProvider_ID = -1;
	
	private int AFGO_Fund_ID = -1;
	
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_FundProvider_ID"))
				this.AFGO_FundProvider_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Fund_ID"))
				this.AFGO_Fund_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt () throws Exception
	{
		log.info("Create schedule: AFGO_Fund_ID=" + this.AFGO_Fund_ID);
		
		try
		{
			// check for existing schedule
			// if there are existing schedule records, the user has to update/add/delete them manually
			if (DB.getSQLValue(this.get_TrxName(), "SELECT COUNT(*) FROM AFGO_FundSchedule WHERE AFGO_Fund_ID=?", this.AFGO_Fund_ID) > 0)
				return "@FundScheduleAlreadyExists@";
			
			MAFGOFund fund = new MAFGOFund(this.getCtx(), this.AFGO_Fund_ID, this.get_TrxName());
			
			if ((fund.getAFGO_Fund_ID() == 0) || !(fund.getAFGO_Fund_ID() == this.AFGO_Fund_ID))
				return "@InvalidFund@ AFGO_Fund_ID=" + this.AFGO_Fund_ID;
			
			MAFGOProgram program = fund.getProgram();
			
			BigDecimal totalDays = new BigDecimal(MAFGOCalendar.getDays(this.getCtx(), fund.getStartDate(), fund.getEndDate()));
			BigDecimal percent = totalDays.divide(Env.ONEHUNDRED);
			
			//BigDecimal totalAmt = Env.ZERO;
			
			log.fine("TotalDays=" + totalDays + ", percent=" + percent);
			
			MAFGOFundScheduleType scheduleType = fund.getFundScheduleType();
			
			MCurrency currency = MCurrency.get(this.getCtx(), fund.getC_Currency_ID());
			
			MAFGOFundSchedule firstSchedule = null;
			MAFGOFundSchedule schedule = null;
			
			for (Iterator<ProgramPeriod> periods = program.getPeriods(fund.getStartDate(), fund.getEndDate()).iterator(); periods.hasNext();)
			{
				ProgramPeriod period = periods.next();
				
				if (!period.isPeriodOpen())
				{
					this.addLog(Msg.getMsg(this.getCtx(), "PeriodClosed") + ": " + period.getName());
					log.warning("PeriodClosed: " + period.toString());
					continue;
				}
				
				BigDecimal days = new BigDecimal(period.getDays(fund.getStartDate(), fund.getEndDate()));
				BigDecimal percentage = days.divide(percent, 8, BigDecimal.ROUND_HALF_UP);
				
				log.fine("Days=" + days + ", percentage=" + percentage);
				
				schedule = new MAFGOFundSchedule(this.getCtx(), 0, this.get_TrxName());
				
				if (firstSchedule == null)
					firstSchedule = schedule;
			
				schedule.setAD_Org_ID(fund.getAD_Org_ID());
				schedule.setDateInvoiced(scheduleType.getDateInvoiced(period));
				schedule.setC_Currency_ID(fund.getC_Currency_ID());
				schedule.set_ValueNoCheck("AFGO_Fund_ID", fund.getAFGO_Fund_ID());
				//schedule.setAFGO_Fund_ID(fund.getAFGO_Fund_ID());
				schedule.setAFGO_Quarter_ID(period.getAFGO_Quarter_ID());
				schedule.setAFGO_Month_ID(period.getAFGO_Month_ID());
				schedule.setC_DocType_ID(scheduleType.getC_DocTypeFundingSchedule_ID());
				schedule.save();
				this.addLog(schedule.getDocumentNo() + " - " + schedule.getPeriod().getName());
				
				for (Iterator<MAFGOFundLine> lines = fund.getLines(true).iterator(); lines.hasNext();)
				{
					MAFGOFundLine line = lines.next();
					MAFGOFundScheduleLine scheduleLine = new MAFGOFundScheduleLine(schedule, line);
	
					scheduleLine.setAD_Org_ID(line.getAD_Org_ID());
					BigDecimal amt = line.getLineNetAmt().subtract(line.getScheduledAmt()).divide(Env.ONEHUNDRED).multiply(percentage);
					amt = scheduleType.getRoundedAmt(amt, currency.getStdPrecision());
					scheduleLine.setLineNetAmt(amt);
					scheduleLine.save();
				}
			}
		}
		catch(Exception e)
		{
			log.severe("A problem occured while creating Funding Invoice Schedule: " + e.getMessage());
		}
		
		return "@OK@";
	}

}
