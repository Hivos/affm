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

import org.compiere.model.MDocType;
import org.compiere.util.CCache;
import org.compiere.util.Ctx;

import compiere.model.X_AFGO_PurchaseCommitmentType;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPurchaseCommitmentType.java,v 1.2.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class MAFGOPurchaseCommitmentType extends X_AFGO_PurchaseCommitmentType
{

	public MAFGOPurchaseCommitmentType(Ctx ctx, int AFGO_PurchaseCommitmentType_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseCommitmentType_ID, trxName);

	}
	
	public MAFGOPurchaseCommitmentType(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	
	//
	
	private static CCache<Integer,MAFGOPurchaseCommitmentType> s_cache = new CCache<Integer,MAFGOPurchaseCommitmentType>(MAFGOLotItem.Table_Name, 120);
	
	public static MAFGOPurchaseCommitmentType getPurchaseCommitmentType(Ctx ctx, int AFGO_PurchaseCommitmentType_ID)
	{
		MAFGOPurchaseCommitmentType purchaseCommitmentType = (AFGO_PurchaseCommitmentType_ID > 0) ? s_cache.get(ctx, AFGO_PurchaseCommitmentType_ID) : null;
		
		if (purchaseCommitmentType == null && AFGO_PurchaseCommitmentType_ID > 0)
		{
			purchaseCommitmentType = new MAFGOPurchaseCommitmentType(ctx, AFGO_PurchaseCommitmentType_ID, null);
			
			if (AFGO_PurchaseCommitmentType_ID == purchaseCommitmentType.getAFGO_PurchaseCommitmentType_ID())
				s_cache.put(purchaseCommitmentType.getAFGO_PurchaseCommitmentType_ID(), purchaseCommitmentType);
			else
				purchaseCommitmentType = null;
		}
		
		return purchaseCommitmentType;
	}
	
	public static MAFGOPurchaseCommitmentType getPurchaseCommitmentType(MDocType dt)
	{
		Integer AFGO_PurchaseCommitmentType_ID = (Integer)dt.get_Value("AFGO_PurchaseCommitmentType_ID");
		return getPurchaseCommitmentType(dt.getCtx(), AFGO_PurchaseCommitmentType_ID);
	}
}
