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

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutQuotationRequest.java,v 1.6.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CalloutQuotationRequest extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	/**
	 * Lot Item
	 */
	public String lotItem(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer AFGO_LotItem_ID = (Integer)value;
		
		Integer AFGO_PurchaseLot_ID = ctx.getContextAsInt(WindowNo, "AFGO_PurchaseLot_ID");
		
		if (AFGO_LotItem_ID != null && AFGO_LotItem_ID > 0)
		{
			MAFGOLotItem lotItem = MAFGOLotItem.getLotItem(ctx, AFGO_LotItem_ID);
			if (lotItem == null)
			{
				log.severe("Invalid AFGO_LotItem_ID: " + AFGO_LotItem_ID);
			}
			
			//log.info("AFGO_PurchaseLot_ID=" + AFGO_PurchaseLot_ID);
			if (AFGO_PurchaseLot_ID == null || lotItem.getAFGO_PurchaseLot_ID() != AFGO_PurchaseLot_ID)
			{
				return "PurchaseLotItemMismatch";
			}
		}
		
		return "";
	}
}
