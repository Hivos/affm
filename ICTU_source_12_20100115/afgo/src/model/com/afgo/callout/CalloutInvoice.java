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

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import com.afgo.model.MAFGOFund;
import com.afgo.model.MAFGOFundProvider;
import com.afgo.model.MAFGOFundSchedule;
import com.afgo.model.MAFGOFundScheduleLine;
import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOPurchaseCommitmentLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutInvoice.java,v 1.11.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class CalloutInvoice extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String fundSchedule (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_FundSchedule_ID=" + value);
		
		Integer AFGO_FundSchedule_ID = (Integer)value;
		
		if (AFGO_FundSchedule_ID != null && AFGO_FundSchedule_ID.intValue() > 0)
		{
			MAFGOFundSchedule fundSchedule= new MAFGOFundSchedule(ctx, AFGO_FundSchedule_ID, null);
			MAFGOFund fund = fundSchedule.getFund();
			MAFGOFundProvider fundProvider = fund.getFundProvider();
			
			//mTab.setValue("C_BPartner_ID", fundProvider.getC_BPartner_ID());
			//mTab.setValue("C_BPartner_Location_ID", fundProvider.getC_BPartner_Location_ID());
			//mTab.setValue("AD_User_ID", fundProvider.getAD_User_ID());
			
			//if (fundProvider.getM_PriceList_ID() > 0)
			//	mTab.setValue("M_PriceList_ID", fundProvider.getM_PriceList_ID());
		}
		
		return "";
	}
	
	public String fundScheduleLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_FundScheduleLine_ID=" + value);
		
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "AFGO_Program_ID");
		int AFGO_ProjectCluster_ID = 0;
		int AFGO_Project_ID = 0;
		int AFGO_Phase_ID = 0;
		int AFGO_Activity_ID = 0;
		int AFGO_ServiceType_ID = 0;
		
		MAFGOProgram program = null;
		
		if (AFGO_Program_ID > 0)
			program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		
		if ((value == null || ((Integer)value).intValue() < 1))
		{	
			if (AFGO_Program_ID > 1)
			{
				// TODO: implement default allocation
				if (program != null && program.getAFGO_Program_ID() == AFGO_Program_ID)
				{
					/*
					AFGO_ProjectCluster_ID = program.getAFGO_ProjectCluster_ID();
					AFGO_Project_ID = program.getProjectCluster().getAFGO_Project_ID();
					AFGO_Phase_ID = program.getProjectCluster().getProject().getAFGO_Phase_ID();
					AFGO_Activity_ID = program.getProjectCluster().getProject().getPhase().getAFGO_Activity_ID();
					*/
				}
			}
		}
		else
		{
			MAFGOFundScheduleLine line = new MAFGOFundScheduleLine(ctx, (Integer)value, null);
			MAFGOFundSchedule schedule = line.getFundSchedule();
			
			AFGO_ProjectCluster_ID = line.getAFGO_ProjectCluster_ID();
			AFGO_Project_ID = line.getAFGO_Project_ID();
			AFGO_Phase_ID = line.getAFGO_Phase_ID();
			AFGO_Activity_ID = line.getAFGO_Activity_ID();
			AFGO_ServiceType_ID = line.getAFGO_ServiceType_ID();
			
			mTab.setValue("C_Charge_ID", line.getFundLine().getC_Charge_ID());
			mTab.setValue("Quantity", 1);
			mTab.setValue("PriceEntered", line.getLineNetAmt().subtract(line.getInvoicedAmt()));
		}
	
		mTab.setValue("AFGO_ProjectCluster_ID", AFGO_ProjectCluster_ID);
		mTab.setValue("AFGO_Project_ID", AFGO_Project_ID);
		mTab.setValue("AFGO_Phase_ID", AFGO_Phase_ID);
		mTab.setValue("AFGO_Activity_ID", AFGO_Activity_ID);
		mTab.setValue("AFGO_ServiceType_ID", AFGO_ServiceType_ID);
		
		return "";
	}
	
	public String purchaseCommitment(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_PurchaseCommitment_ID=" + value);
		
		Integer AFGO_PurchaseCommitment_ID = (Integer)value;
		Integer C_BPartner_ID = null;
		
		/*
		if (AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID > 0)
		{
			MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(ctx, AFGO_PurchaseCommitment_ID, null);
			mTab.setValue("C_BPartner_ID", purchaseCommitment.getC_BPartner_ID());
			mTab.setValue("Description", purchaseCommitment.getDescription());
		}
		*/
					
		return "";
	}
	
	public String purchaseCommitmentLine (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_PurchaseCommitmentLine_ID=" + value);
		
		Integer AFGO_PurchaseCommitmentLine_ID = (Integer)value;
		
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "AFGO_Program_ID");
		int AFGO_ProjectCluster_ID = 0;
		int AFGO_Project_ID = 0;
		int AFGO_Phase_ID = 0;
		int AFGO_Activity_ID = 0;
		int AFGO_ServiceType_ID = 0;
		String description = null;
		
		MAFGOProgram program = null;
		
		if (AFGO_Program_ID > 0)
			program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		
		 if (program != null && AFGO_PurchaseCommitmentLine_ID != null)
		 {
			 MAFGOPurchaseCommitmentLine line = new MAFGOPurchaseCommitmentLine(ctx, AFGO_PurchaseCommitmentLine_ID, null);
			 MAFGOPurchaseCommitment commitment = line.getPurchaseCommitment();
			 
			 //AFGO_Quarter_ID = line.getAFGO_Quarter_ID();
			 //AFGO_Month_ID = line.getAFGO_Month_ID();
			 AFGO_ProjectCluster_ID = line.getAFGO_ProjectCluster_ID();
			 AFGO_Project_ID = line.getAFGO_Project_ID();
			 AFGO_Phase_ID = line.getAFGO_Phase_ID();
			 AFGO_Activity_ID = line.getAFGO_Activity_ID();
			 AFGO_ServiceType_ID = line.getAFGO_ServiceType_ID();
			 description = line.getDescription();
			 
			 if (line.getM_Product_ID() > 0)
				 mTab.setValue("M_Product_ID", line.getM_Product_ID());
			 else if (line.getC_Charge_ID() > 0)
				 mTab.setValue("C_Charge_ID", line.getC_Charge_ID());
			 
			 // 1962
			 //mTab.setValue("QtyEntered", line.getQty());
			// mTab.setValue("PriceEntered", line.getPrice());
		 }
		 
		 mTab.setValue("AFGO_ProjectCluster_ID", AFGO_ProjectCluster_ID);
		 mTab.setValue("AFGO_Project_ID", AFGO_Project_ID);
		 mTab.setValue("AFGO_Phase_ID", AFGO_Phase_ID);
		 mTab.setValue("AFGO_Activity_ID", AFGO_Activity_ID);
		 mTab.setValue("AFGO_ServiceType_ID", AFGO_ServiceType_ID);

		 if (description != null)
			 mTab.setValue("Description", description);
		
		return "";
	}
	
}
