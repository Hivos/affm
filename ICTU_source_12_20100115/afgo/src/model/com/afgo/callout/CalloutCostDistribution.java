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
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import com.afgo.model.MAFGOCostDistrLine;
import com.afgo.model.MAFGOInternalCommitmentLine;
import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOQuarter;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutCostDistribution.java,v 1.14.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class CalloutCostDistribution extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String invoice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("C_Invoice_ID=" + value);
		
		if (value != null)
			mTab.setValue("C_Currency_ID", DB.getSQLValue(null, "SELECT C_Currency_ID FROM C_Invoice WHERE C_Invoice_ID=?", (Integer)value));
		
		return "";
	}
	
	public String invoiceLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("C_InvoiceLine_ID=" + value);
		
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "AFGO_Program_ID");
		
		MAFGOProgram program = null;
		
		if (AFGO_Program_ID > 0)
			program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		
		if (value != null)
		{
			int C_InvoiceLine_ID = (Integer)value;
			MInvoiceLine invoiceLine = new MInvoiceLine(ctx, C_InvoiceLine_ID, null);
			
			mTab.setValue("C_Charge_ID", invoiceLine.getC_Charge_ID());
			//mTab.setValue("LineNetAmt", invoiceLine.getLineNetAmt());
			mTab.setValue("Price", invoiceLine.getPriceActual());
			mTab.setValue("Qty", invoiceLine.getQtyInvoiced());
			mTab.setValue("AFGO_ProjectCluster_ID", invoiceLine.getAFGO_ProjectCluster_ID());
			mTab.setValue("AFGO_Project_ID", invoiceLine.getAFGO_Project_ID());
			mTab.setValue("AFGO_Phase_ID", invoiceLine.getAFGO_Phase_ID());
			mTab.setValue("AFGO_Activity_ID", invoiceLine.getAFGO_Activity_ID());
			mTab.setValue("AFGO_ServiceType_ID", invoiceLine.getAFGO_ServiceType_ID());
		}
		
		return "";
	}

	public String costDistributionLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_CostDistrLine_ID=" + value);
		
		if (value != null)
		{
			MAFGOCostDistrLine costDistrLine = new MAFGOCostDistrLine(ctx, (Integer)value, null);
			mTab.setValue("AFGO_Program_ID", costDistrLine.getHeader().getTo_Program_ID());
			mTab.setValue("C_Charge_ID", costDistrLine.getC_Charge_ID());
		}
		
		return "";
	}
	
	public String internalCommitmentLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_InternalCommitmentLine_ID=" + value);
		
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "AFGO_Program_ID");

		MAFGOProgram program = null;
		
		if (AFGO_Program_ID > 0)
			program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		
		
		if (value != null)
		{
			MAFGOInternalCommitmentLine internalCommitmentLine = new MAFGOInternalCommitmentLine(ctx, (Integer)value, null);
			mTab.setValue("C_Charge_ID", internalCommitmentLine.getC_Charge_ID());
			//mTab.setValue("Qty", internalCommitmentLine.getQty());
			//mTab.setValue("Price", internalCommitmentLine.getPrice());
			//mTab.setValue("LineNetAmt", internalCommitmentLine.getLineNetAmt());
			
			mTab.setValue("AFGO_ProjectCluster_ID", internalCommitmentLine.getAFGO_ProjectCluster_ID());
			mTab.setValue("AFGO_Project_ID", internalCommitmentLine.getAFGO_Project_ID());
			mTab.setValue("AFGO_Phase_ID", internalCommitmentLine.getAFGO_Phase_ID());
			mTab.setValue("AFGO_Activity_ID", internalCommitmentLine.getAFGO_Activity_ID());
			mTab.setValue("AFGO_ServiceType_ID", internalCommitmentLine.getAFGO_ServiceType_ID());
		}
		
		return "";
	}
	
	public String month (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "To_Program_ID");
		MAFGOProgram program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		MAFGOMonth month = null;
		
		if (super.isCalloutActive())
			return "";
				
		super.setCalloutActive(true);
		
		// derived from DateAcct
		if (value == null)
		{
			java.sql.Date dateAcct = null;
			if (ctx.getContextAsTime(WindowNo, "DateAcct") > 0)
				dateAcct = new java.sql.Date(ctx.getContextAsTime(WindowNo, "DateAcct"));
			
			if (dateAcct != null && program != null)
			{
				month = program.getMonth(dateAcct, false);
				
				if (month == null)
				{
					return "NoPeriod: " + dateAcct;
				}
				else if (!month.isPeriodOpen())
				{
					return "PeriodClosed: " + dateAcct;
				}
				
				mTab.setValue("AFGO_Quarter_ID", month.getAFGO_Quarter_ID());
				mTab.setValue("AFGO_Month_ID", month.getAFGO_Month_ID());
			}
			
		}
		
		super.setCalloutActive(false);
		
		return "";
		
	}
	
}
