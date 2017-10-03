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
import java.sql.Timestamp;
import java.util.Iterator;

import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: TransferredPurchaseCommitment.java,v 1.8.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class TransferredPurchaseCommitment extends MAFGOPurchaseCommitment
{

	public TransferredPurchaseCommitment(MasterPurchaseCommitment originalCommitment, int C_DocType_ID)
	{
		super(originalCommitment.getCtx(), 0, originalCommitment.get_TrxName());
		
		this.originalCommitment = originalCommitment;
		this.setC_DocType_ID(C_DocType_ID);
		
		// copy values from original
		this.setAD_Org_ID(originalCommitment.getAD_Org_ID());
		this.setAFGO_Program_ID(originalCommitment.getAFGO_Program_ID());
		this.setMasterPurchaseCommitment_ID(originalCommitment.getAFGO_PurchaseCommitment_ID());
		this.setC_Currency_ID(originalCommitment.getC_Currency_ID());
		this.setC_BPartner_ID(originalCommitment.getC_BPartner_ID());
		this.setDateDoc(new Timestamp(this.getCtx().getContextAsTime("#Date")));
		this.setDateAcct(new Timestamp(this.getCtx().getContextAsTime("#Date")));
		this.setDateFrom(originalCommitment.getDateFrom());
		this.setDateTo(originalCommitment.getDateTo());
	}
	
	private MasterPurchaseCommitment originalCommitment = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public void setDateFrom(Timestamp from)
	{
		if (from == null || from.before(this.getProgram().getStartDate()))
			super.setDateFrom(this.getProgram().getStartDate());
		else
			super.setDateFrom(from);
	}
	
	public void setDateTo(Timestamp to)
	{
		if (to == null || to.after(this.getProgram().getEndDate()))
			super.setDateTo(this.getProgram().getEndDate());
		else
			super.setDateTo(to);
	}
	
	public MasterPurchaseCommitment getOriginalCommitment()
	{
		return this.originalCommitment;
	}
	
	public int transferCommitment(int AFGO_Activity_ID, int AFGO_ServiceType_ID, int M_Product_ID, int C_Charge_ID, int fromAFGO_Month_ID, int toAFGO_Month_ID, BigDecimal transferAmt, BigDecimal closeAmt) throws CompiereUserException
	{
		int lines = 0;
		
		if (transferAmt == null)
			transferAmt = Env.ZERO;
		if (closeAmt == null)
			closeAmt = Env.ZERO;
		
		
		// from
		MAFGOMonth fromMonth = MAFGOMonth.getMonth(this.getCtx(), fromAFGO_Month_ID);
		System.err.println("FromMonth=" + fromMonth);
		/*
		if (fromMonth.getStartDate().before(this.getDateFrom()))
				this.setDateFrom(fromMonth.getStartDate());
		*/
		if (fromMonth.getEndDate().after(this.getDateTo()))
			this.setDateTo(fromMonth.getEndDate());
		
		// to
		MAFGOMonth toMonth = MAFGOMonth.getMonth(this.getCtx(), toAFGO_Month_ID);
		System.err.println("ToMonth=" + toMonth);
		if (toMonth.getEndDate().after(this.getDateTo()))
			this.setDateTo(toMonth.getEndDate());
		/*
		if (toMonth.getStartDate().before(this.getDateFrom()))
			this.setDateFrom(toMonth.getStartDate());
		*/
		
		for (Iterator<MAFGOYear> years = this.getOriginalCommitment().getOrderedYears().iterator(); years.hasNext();)
		{
			MAFGOYear year = years.next();
			if (year.getAFGO_Year_ID() == toMonth.getQuarter().getAFGO_Year_ID())
				break;
			
			if (!years.hasNext())
			{
				log.warning("Invalid Year: " + toMonth.getQuarter().getYear().getName() + ", AFGO_PurchaseCommitment_ID=" + this.getOriginalCommitment().getAFGO_PurchaseCommitment_ID());
				return lines;
			}
		}
		
		if (!this.save())
			throw new CompiereUserException("NotSaved");
		
		MAFGOPurchaseCommitmentLine closeLine = this.createLine(AFGO_Activity_ID, AFGO_ServiceType_ID, M_Product_ID, C_Charge_ID, fromAFGO_Month_ID);
		closeLine.setQty(Env.ONE.negate());
		closeLine.setQtyEntered(Env.ONE.negate());
		closeAmt = transferAmt.add(closeAmt);
		closeLine.setPrice(closeAmt);
		closeLine.setPriceEntered(closeAmt);
		if (closeAmt.compareTo(Env.ZERO) != 0)
		{
			if (closeLine.save())
				lines++;
		}
		
		MAFGOPurchaseCommitmentLine transferLine = this.createLine(AFGO_Activity_ID, AFGO_ServiceType_ID, M_Product_ID, C_Charge_ID, toAFGO_Month_ID);
		transferLine.setQty(Env.ONE);
		transferLine.setQtyEntered(Env.ONE);
		transferLine.setPrice(transferAmt);
		transferLine.setPriceEntered(transferAmt);
		if (transferAmt.compareTo(Env.ZERO) != 0)
		{
			if (transferLine.save())
				lines++;
		}
		
		return lines;
	}
	
	private MAFGOPurchaseCommitmentLine createLine(int AFGO_Activity_ID, int AFGO_ServiceType_ID, int M_Product_ID, int C_Charge_ID, int AFGO_Month_ID) throws CompiereUserException
	{
		if (this.getAFGO_PurchaseCommitment_ID() < 1)
			throw new CompiereUserException("NotSaved");
		
		MAFGOPurchaseCommitmentLine line = new MAFGOPurchaseCommitmentLine(this.getCtx(), 0, this.get_TrxName());
		
		line.setLine(DB.getSQLValue(this.get_TrxName(), "SELECT COALESCE(MAX(Line), 0) + 10 FROM AFGO_PurchaseCommitmentLine WHERE AFGO_PurchaseCommitment_ID=?", this.getAFGO_PurchaseCommitment_ID()));
		
		line.setAFGO_PurchaseCommitment_ID(this.getAFGO_PurchaseCommitment_ID());
		line.setM_Product_ID(M_Product_ID);
		line.setC_Charge_ID(C_Charge_ID);
		
		MAFGOActivity activity = MAFGOActivity.getActivity(this.getCtx(), AFGO_Activity_ID);
		line.setAFGO_ProjectCluster_ID(activity.getPhase().getProject().getAFGO_ProjectCluster_ID());
		line.setAFGO_Project_ID(activity.getPhase().getAFGO_Project_ID());
		line.setAFGO_Phase_ID(activity.getAFGO_Phase_ID());
		line.setAFGO_Activity_ID(activity.getAFGO_Activity_ID());
		
		line.setAFGO_ServiceType_ID(AFGO_ServiceType_ID);
		
		MAFGOMonth month = MAFGOMonth.getMonth(this.getCtx(), AFGO_Month_ID);
		// AFGO_PurchaseCommitmentLine.AFGO_Year_ID is part of ICTU module
		if (line.get_ColumnIndex("AFGO_Year_ID") > -1)
		{
			line.set_Value("AFGO_Year_ID", (month.getQuarter().getAFGO_Year_ID()));
		}
		line.setAFGO_Quarter_ID(month.getAFGO_Quarter_ID());
		line.setAFGO_Month_ID(month.getAFGO_Month_ID());
		
		return line;
	}
	
}
