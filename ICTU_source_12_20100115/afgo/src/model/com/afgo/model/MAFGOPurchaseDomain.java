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


import compiere.model.X_AFGO_PurchaseDomain;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPurchaseDomain.java,v 1.7.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOPurchaseDomain extends X_AFGO_PurchaseDomain
{

	public MAFGOPurchaseDomain (Ctx ctx, int AFGO_PurchaseDomain_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseDomain_ID, trxName);

	}
	
	public MAFGOPurchaseDomain (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private static CCache<Integer,MAFGOPurchaseDomain> s_cache = new CCache<Integer,MAFGOPurchaseDomain>(MAFGOPurchaseDomain.Table_Name, 10);
	
	public static MAFGOPurchaseDomain getPurchaseDomain(Ctx ctx, int AFGO_PurchaseDomain_ID)
	{
		MAFGOPurchaseDomain purchaseDomain = s_cache.get(ctx, AFGO_PurchaseDomain_ID);
		
		if (purchaseDomain == null && AFGO_PurchaseDomain_ID > 0)
		{
			purchaseDomain = new MAFGOPurchaseDomain(ctx, AFGO_PurchaseDomain_ID, null);
			if (purchaseDomain.getAFGO_PurchaseDomain_ID() == AFGO_PurchaseDomain_ID)
				s_cache.put(AFGO_PurchaseDomain_ID, purchaseDomain);
			else
				purchaseDomain = null;
		}
		
		return purchaseDomain;
	}

}
