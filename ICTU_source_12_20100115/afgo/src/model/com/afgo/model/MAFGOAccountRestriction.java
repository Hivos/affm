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


import compiere.model.X_AFGO_AccountRestriction;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOAccountRestriction.java,v 1.5.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOAccountRestriction extends X_AFGO_AccountRestriction
{

	public MAFGOAccountRestriction(Ctx ctx, int AFGO_AccountRestriction_ID, String trxName)
	{
		super(ctx, AFGO_AccountRestriction_ID, trxName);
	}
	
	public MAFGOAccountRestriction(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	/************************************************************/
	
	private static CCache<Integer,MAFGOAccountRestriction> s_cache = new CCache<Integer,MAFGOAccountRestriction>(MAFGOAccountRestriction.Table_Name, 5);

	public static MAFGOAccountRestriction get(Ctx ctx, int AFGO_AccountRestriction_ID)
	{
		MAFGOAccountRestriction restriction = s_cache.get(ctx, AFGO_AccountRestriction_ID);
		
		if (restriction == null && AFGO_AccountRestriction_ID > 0)
		{
			restriction = new MAFGOAccountRestriction(ctx, AFGO_AccountRestriction_ID, null);
			if (restriction.getAFGO_AccountRestriction_ID() == AFGO_AccountRestriction_ID)
				s_cache.put(AFGO_AccountRestriction_ID, restriction);
			else
				restriction = null;
		}
		
		return restriction;
	}
	
	public static String getSQL(Ctx ctx, int AFGO_AccountRestriction_ID)
	{
		MAFGOAccountRestriction restriction = get(ctx, AFGO_AccountRestriction_ID);
		
		return restriction != null ? restriction.getWhereClause() : null;
	}
}
