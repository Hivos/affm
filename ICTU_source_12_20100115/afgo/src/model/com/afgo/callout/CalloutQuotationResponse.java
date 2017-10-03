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
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;

import com.afgo.model.MAFGOLotItem;
import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOQuotationRequest;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutQuotationResponse.java,v 1.11.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class CalloutQuotationResponse extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	/**
	 * Purchase Commitment
	 */
	public String purchaseCommitment(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_PurchaseCommitment_ID=" + value);
		
		Integer AFGO_PurchaseCommitment_ID = (Integer)value;
		MAFGOPurchaseCommitment purchaseCommitment = null;
		
		if (AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID > 0)
		{
			purchaseCommitment = new MAFGOPurchaseCommitment(ctx, AFGO_PurchaseCommitment_ID, null);
			mTab.setValue("ProgramPeriodType", purchaseCommitment.getProgram().getProgramPeriodType());
		}

		mTab.setValue("C_BPartner_ID", (purchaseCommitment != null ? purchaseCommitment.getC_BPartner_ID() : null));
		mTab.setValue("AFGO_PurchaseDomain_ID", (purchaseCommitment != null ? purchaseCommitment.getAFGO_PurchaseDomain_ID() : null));
		mTab.setValue("AFGO_PurchaseLot_ID", (purchaseCommitment != null ? purchaseCommitment.getAFGO_PurchaseLot_ID() : null));
		mTab.setValue("C_Currency_ID", (purchaseCommitment != null ? purchaseCommitment.getC_Currency_ID() : null));

		return "";
	}
	
	/**
	 * QuotationRequest
	 */
	public String quotationRequest(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_QuotationRequest_ID=" + value);
		
		Integer AFGO_QuotationRequest_ID = (Integer)value;
		MAFGOQuotationRequest quotationRequest = null;
		
		if (AFGO_QuotationRequest_ID != null && AFGO_QuotationRequest_ID > 0)
			quotationRequest = new MAFGOQuotationRequest(ctx, AFGO_QuotationRequest_ID, null);
		
		mTab.setValue("AFGO_PurchaseDomain_ID", (quotationRequest != null ? quotationRequest.getAFGO_PurchaseDomain_ID() : null));
		mTab.setValue("AFGO_PurchaseLot_ID", (quotationRequest != null ? quotationRequest.getAFGO_PurchaseLot_ID() : null));
		mTab.setValue("C_Currency_ID", (quotationRequest != null ? quotationRequest.getC_Currency_ID() : null));
		mTab.setValue("C_BPartner_ID", null);

		return "";
	}
	
	/**
	 * Lot Item (on line level)
	 * 
	 */
	public String lotItem(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_LotITem_ID=" + value);
		
		Integer AFGO_LotItem_ID = (Integer)value;
		MAFGOLotItem lotItem = null;

		if (AFGO_LotItem_ID != null && AFGO_LotItem_ID > 0)
		{
			lotItem = MAFGOLotItem.getLotItem(ctx, AFGO_LotItem_ID);
		}
		
		mTab.setValue("M_Product_ID", ((lotItem != null && lotItem.getM_Product_ID() > 0) ? lotItem.getM_Product_ID() : null));
		mTab.setValue("C_Charge_ID", ((lotItem != null && lotItem.getC_Charge_ID() > 0) ? lotItem.getC_Charge_ID() : null));
			
		return "";
	}
	
	/**
	 * BPartner
	 */
	public String bpartner (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("C_BPartner_ID=" + value);
		
		mTab.setValue("AD_User_ID", null);
		return "";
	}
	
	/**
	 * PurchaseLot
	 */
	public String purchaseLot (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_PurchaseLog_ID=" + value);
		
		Integer AFGO_PurchaseLot_ID = (Integer)value;
		
		Integer AFGO_PurchaseCommitment_ID = (Integer)mTab.getValue("AFGO_PurchaseCommitment_ID");
		
		if (AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID < 1)
			mTab.setValue("C_BPartner_ID", null);
		
		String bpartnerSQL = "1=1";
		
		// SQL will be used by AD_Val_Rule
		if (AFGO_PurchaseLot_ID != null && AFGO_PurchaseLot_ID > 0 && AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID > 0)
		{
			bpartnerSQL = "C_BPartner.C_BPartner_ID IN"
				+ " ("
				+ " 	SELECT C_BPartner_ID"
				+ " 	FROM AFGO_QuotationResponse"
				+ " 	WHERE AFGO_PurchaseLot_ID=" + AFGO_PurchaseLot_ID
				+ " 	AND DocStatus IN ('CO','CL')"
				+ " 	AND IsPurchaseDomain='Y'"
				+ " ) ";
			
		}
		
		ctx.setContext(WindowNo, "BPartnerSQL", bpartnerSQL);
		
		return "";
	}
}
