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

import org.compiere.util.CCache;
import org.compiere.util.Ctx;


import compiere.model.X_AFGO_PurchaseLot;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPurchaseLot.java,v 1.9.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOPurchaseLot extends X_AFGO_PurchaseLot
{

	public MAFGOPurchaseLot(Ctx ctx, int AFGO_PurchaseLot_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseLot_ID, trxName);
		
	}
	
	public MAFGOPurchaseLot(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		
	}

	public MAFGOPurchaseDomain getPurchaseDomain()
	{
		return MAFGOPurchaseDomain.getPurchaseDomain(this.getCtx(), this.getAFGO_PurchaseDomain_ID());
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// use org of domain
		this.setAD_Org_ID(this.getPurchaseDomain().getAD_Org_ID());
		
		return true;
	}
	
	private static CCache<Integer,MAFGOPurchaseLot> s_cache = new CCache<Integer,MAFGOPurchaseLot>(MAFGOPurchaseLot.Table_Name, 10);
	
	public static MAFGOPurchaseLot getPurchaseLot(Ctx ctx, int AFGO_PurchaseLot_ID)
	{
		MAFGOPurchaseLot purchaseLot = s_cache.get(ctx, AFGO_PurchaseLot_ID);
		
		if (purchaseLot == null && AFGO_PurchaseLot_ID > 0)
		{
			purchaseLot = new MAFGOPurchaseLot(ctx, AFGO_PurchaseLot_ID, null);
			if (purchaseLot.getAFGO_PurchaseLot_ID() == AFGO_PurchaseLot_ID)
				s_cache.put(AFGO_PurchaseLot_ID, purchaseLot);
			else
				purchaseLot = null;
		}
		
		return purchaseLot;
	}
}
