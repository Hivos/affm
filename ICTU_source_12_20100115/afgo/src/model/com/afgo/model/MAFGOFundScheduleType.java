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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.compiere.util.CompiereSystemException;
import org.compiere.util.Ctx;
import org.compiere.util.Env;

import compiere.model.X_AFGO_FundScheduleType;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFundScheduleType.java,v 1.5.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MAFGOFundScheduleType extends X_AFGO_FundScheduleType
{

	public MAFGOFundScheduleType (Ctx ctx, int AFGO_FundScheduleType_ID, String trxName)
	{
		super(ctx, AFGO_FundScheduleType_ID, trxName);

	}
	
	public MAFGOFundScheduleType (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	public Timestamp getDateInvoiced(ProgramPeriod period)
	{
		Date offset = (MAFGOFundScheduleType.FUNDSCHEDULEPERIODTYPE_PeriodStart.equals(this.getFundSchedulePeriodType()) ? period.getStartDate() : period.getEndDate());

		Calendar cal = Calendar.getInstance();
		cal.setTime(offset);
		cal.add(Calendar.DATE, this.getNetDays());

		return new Timestamp(cal.getTimeInMillis());
	}
	
	public BigDecimal getRoundedAmt(BigDecimal amt, int currencyPrecision) throws CompiereSystemException
	{
		BigDecimal oldAmt = amt;
		if (MAFGOFundScheduleType.STD_ROUNDING_CurrencyPrecision.equals(this.getStd_Rounding()))
			amt = amt.divide(Env.ONE, 2, BigDecimal.ROUND_HALF_UP);
		else if (MAFGOFundScheduleType.STD_ROUNDING_WholeNumber00.equals(this.getStd_Rounding()))
			amt = amt.divide(Env.ONE, 0, BigDecimal.ROUND_HALF_UP);
		else
			throw new CompiereSystemException("RoundingModeNotImplemented" + ": " + this.getStd_Rounding());
		
		return amt;
	}

}
