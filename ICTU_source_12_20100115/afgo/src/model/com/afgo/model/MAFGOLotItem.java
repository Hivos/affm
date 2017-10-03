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

import org.compiere.framework.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;


import compiere.model.X_AFGO_LotItem;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOLotItem.java,v 1.9.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class MAFGOLotItem extends X_AFGO_LotItem
{

	public MAFGOLotItem (Ctx ctx, int AFGO_LotItem_ID, String trxName)
	{
		super(ctx, AFGO_LotItem_ID, trxName);

	}
	
	public MAFGOLotItem (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		
	}
	
	public MAFGOPurchaseLot getPurchaseLot()
	{
		return MAFGOPurchaseLot.getPurchaseLot(this.getCtx(), this.getAFGO_PurchaseLot_ID());
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// use org of lot
		this.setAD_Org_ID(this.getPurchaseLot().getAD_Org_ID());
		
		if (this.getM_Product_ID() < 1 && this.getC_Charge_ID() < 1)
		{
			super.log.saveError("NoProductCharge", "");
			return false;
		}
		else if (this.getM_Product_ID() > 0 && this.getC_Charge_ID() > 0)
		{
			super.log.saveError("SelectProductOrCharge", "");
			return false;
		}
		return true;
	}
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOLotItem.class);
	
	private static CCache<Integer,MAFGOLotItem> s_cache = new CCache<Integer,MAFGOLotItem>(MAFGOLotItem.Table_Name, 120);
	
	public static MAFGOLotItem getLotItem(Ctx ctx, int AFGO_LotItem_ID)
	{
		MAFGOLotItem lotItem = (AFGO_LotItem_ID > 0) ? s_cache.get(ctx, AFGO_LotItem_ID) : null;
		
		if (lotItem == null && AFGO_LotItem_ID > 0)
		{
			lotItem = new MAFGOLotItem(ctx, AFGO_LotItem_ID, null);
			
			if (AFGO_LotItem_ID == lotItem.getAFGO_LotItem_ID())
				s_cache.put(lotItem.getAFGO_LotItem_ID(), lotItem);
			else
				lotItem = null;
		}
		
		return lotItem;
	}
	
	public static String validateLotItemProductCharge(PO po)
	{
		s_log.fine("Validate Item / Product / Charge: " + po);
		
		Integer AFGO_LotItem_ID = (Integer)po.get_Value("AFGO_LotItem_ID");
		if (AFGO_LotItem_ID == null || AFGO_LotItem_ID < 1)
			return null;
		
		Integer M_Product_ID = (Integer)po.get_Value("M_Product_ID");
		Integer C_Charge_ID = (Integer)po.get_Value("C_Charge_ID");
		
		if ((M_Product_ID == null || M_Product_ID < 1) && (C_Charge_ID == null | C_Charge_ID < 1))
			return Msg.getMsg(po.getCtx(), "NoProductOrCharge");
		
		else if (M_Product_ID != null && M_Product_ID > 0)
		{
			if (DB.getSQLValue(po.get_TrxName(), "SELECT M_Product_ID FROM AFGO_LotItem WHERE AFGO_LotItem_ID=?", AFGO_LotItem_ID) != M_Product_ID)
				return Msg.getMsg(po.getCtx(), "InvalidProduct");
		}
		
		else if (C_Charge_ID != null && C_Charge_ID > 0)
		{
			if (DB.getSQLValue(po.get_TrxName(), "SELECT C_Charge_ID FROM AFGO_LotItem WHERE AFGO_LotItem_ID=?", AFGO_LotItem_ID) != C_Charge_ID)
				return Msg.getMsg(po.getCtx(), "InvalidCharge");
		}
		
		else if ((M_Product_ID != null && M_Product_ID > 0) && (C_Charge_ID != null && C_Charge_ID > 0))
			return Msg.getMsg(po.getCtx(), "ProductAndCharge");
		
		else {}
		
		s_log.info("Valid Lot Item => Product / Charge");
		
		return null;
	}
	

}
