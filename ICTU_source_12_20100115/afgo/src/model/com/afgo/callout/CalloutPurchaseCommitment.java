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

import java.math.BigDecimal;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MDocType;
import org.compiere.util.DB;

import com.afgo.model.MAFGOLotItem;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOPurchaseCommitmentType;
import com.afgo.model.MAFGOQuotationResponse;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutPurchaseCommitment.java,v 1.12.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CalloutPurchaseCommitment extends CalloutEngine 
{
	private CLogger log = CLogger.getCLogger(getClass());
	

	public String purchaseCommitmentType(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_PurchaseCommitmentType_ID=" + value);
		
		Integer AFGO_PurchaseCommitmentType_ID = (Integer)value;
		//System.err.println("AFGO_PurchaseCommitmentType_ID=" + AFGO_PurchaseCommitmentType_ID);
		
		if (AFGO_PurchaseCommitmentType_ID != null)
		{
			//System.err.println("AFGO_PurchaseCommitmentType_ID=" + AFGO_PurchaseCommitmentType_ID);
			// clear master when not applicable
			//if (!(MAFGOPurchaseCommitment.PURCHASECOMMITMENTTYPE_AdditionalCommitment.equals(purchaseCommitmentType))
			//		|| (MAFGOPurchaseCommitment.PURCHASECOMMITMENTTYPE_TransferedCommitment.equals(purchaseCommitmentType)))
			//		mTab.setValue("MasterPurchaseCommitment_ID", null);
			
		}
			
		
		return "";
	}
	
	public String masterPurchaseCommitment (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("MasterPurchaseComitment_ID=" + value);
		
		Integer AFGO_PurchaseDomain_ID = null;
		Integer AFGO_PurchaseLot_ID = null;
		Integer C_BPartner_ID = null;
		
		if (value != null)
		{
			MAFGOPurchaseCommitment masterPurchaseCommitment = new MAFGOPurchaseCommitment(ctx, (Integer)value, null);
			AFGO_PurchaseDomain_ID = masterPurchaseCommitment.getAFGO_PurchaseDomain_ID();
			AFGO_PurchaseLot_ID = masterPurchaseCommitment.getAFGO_PurchaseLot_ID();
			C_BPartner_ID = masterPurchaseCommitment.getC_BPartner_ID();
			
			if (AFGO_PurchaseDomain_ID > 0)
				mTab.setValue("AFGO_PurchaseDomain_ID", AFGO_PurchaseDomain_ID);
			if (AFGO_PurchaseLot_ID > 0)
				mTab.setValue("AFGO_PurchaseLot_ID", AFGO_PurchaseLot_ID);
			if (C_BPartner_ID > 0)
				mTab.setValue("C_BPartner_ID", C_BPartner_ID);
			
		}
		
		return "";
	}
	
	public String quotationResponse(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_QuotationResponse_ID=" + value);
		
		Integer AFGO_QuotationResponse_ID = (Integer)value;
		Integer C_BPartner_ID = null;
		
		if (AFGO_QuotationResponse_ID != null && AFGO_QuotationResponse_ID > 0)
			C_BPartner_ID = new MAFGOQuotationResponse(ctx, AFGO_QuotationResponse_ID, null).getC_BPartner_ID();
		
		mTab.setValue("C_BPartner_ID", C_BPartner_ID);
	
		return "";
	}
	
	/**
	 * PriceEntered (on line level)
	 */
	public String priceEntered (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("PriceEntered=" + value);
		
		BigDecimal price = (BigDecimal)value;
		BigDecimal qty = (BigDecimal)mTab.getValue("QtyEntered");
		mTab.setValue("PlannedAmt", price.multiply(qty));
		return "";
	}
	
	/**
	 * QuantityEntered (on line level)
	 */
	public String qtyEntered (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("QtyEntered=" + value);
		
		BigDecimal price = (BigDecimal)mTab.getValue("PriceEntered");
		BigDecimal qty = (BigDecimal)value;
		mTab.setValue("PlannedAmt", price.multiply(qty));
		return "";
	}
	
	
	/**
	 * Lot Item (on line level)
	 */
	public String lotItem (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_LotItem_ID=" + value);
		
		Integer AFGO_LotItem_ID = (Integer)value;
		
		MAFGOLotItem lotItem = null;
		
		if (AFGO_LotItem_ID != null && AFGO_LotItem_ID > 0)
		{
			lotItem = MAFGOLotItem.getLotItem(ctx, AFGO_LotItem_ID);
		}
		
		if (lotItem == null)
		{
			mTab.setValue("M_Product_ID", null);
			mTab.setValue("C_Charge_ID", null);
		}
		else
		{
			if (lotItem.getM_Product_ID() > 0)
			{
				mTab.setValue("M_Product_ID", lotItem.getM_Product_ID());
				mTab.setValue("C_Charge_ID", null);
			}
			else if (lotItem.getC_Charge_ID() > 0)
			{
				mTab.setValue("C_Charge_ID", lotItem.getC_Charge_ID());
				mTab.setValue("M_Product_ID", null);
			}
		}
		
		return "";
	}
	
	/**
	 * Document Type
	 */
	public String docType (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("C_DocType_ID=" + value);
		
		if (value != null)
		{
			Integer C_DocType_ID = (Integer)value;
			
			if (C_DocType_ID != null && C_DocType_ID > 0)
			{
				MDocType docType = MDocType.get(ctx, C_DocType_ID);
				MAFGOPurchaseCommitmentType purchaseCommitmentType = MAFGOPurchaseCommitmentType.getPurchaseCommitmentType(docType);
				mTab.setValue("AFGO_PurchaseCommitmentType_ID", purchaseCommitmentType.getAFGO_PurchaseCommitmentType_ID());
				
				// vendor selection enabled/disabled based on doctype
				//boolean allowVendorSelection = (Boolean)docType.get_Value("IsAllowVendorSelection");
				//mTab.setValue("IsAllowVendorSelection", allowVendorSelection);
				//if (!allowVendorSelection)
				//	mTab.setValue("C_BPartner_ID", null);
				
				// copy settings from type to document
				
				
				GridField[] fields = mTab.getFields();
				for (int i = 0; i < fields.length; i++)
				{
					if ("AD_Client_ID".equals(fields[i].getColumnName()))
						continue;
					else if ("AD_Org_ID".equals(fields[i].getColumnName()))
						continue;
					else if ("IsActive".equals(fields[i].getColumnName()))
						continue;
					else if ("Created".equals(fields[i].getColumnName()))
						continue;
					else if ("CreatedBy".equals(fields[i].getColumnName()))
						continue;
					else if ("Updated".equals(fields[i].getColumnName()))
						continue;
					else if ("UpdatedBy".equals(fields[i].getColumnName()))
						continue;
					else if ("AFGO_PurchaseCommitmentType_ID".equals(fields[i].getColumnName()))
						continue;
					else if ("Description".equals(fields[i].getColumnName()))
						continue;
					
					if (purchaseCommitmentType.get_ColumnIndex(fields[i].getColumnName()) < 0)
						continue;
					
					mTab.setValue(fields[i].getColumnName(), purchaseCommitmentType.get_Value(fields[i].getColumnName()));
					
					log.fine(fields[i].getColumnName() + "=" + purchaseCommitmentType.get_Value(fields[i].getColumnName()));
				}

				// prevent Transfer documents from being created through the transaction window 10002549
				if (purchaseCommitmentType.isDisableManualCreation())
				{
					mTab.setValue("C_DocType_ID", null);
				}
				
				
			}
		}
		
		return "";
	}
	
}
