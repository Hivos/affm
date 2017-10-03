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

import org.compiere.util.Ctx;

import compiere.model.X_AFGO_T_RevenueExpense;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGORevenueExpense.java,v 1.2.2.1 2010/01/06 11:45:23 tomassen Exp $
 *
 */
public class MAFGORevenueExpense extends X_AFGO_T_RevenueExpense
{
	public MAFGORevenueExpense(Ctx ctx, String trx_Name)
	{
		super(ctx, 0, trx_Name);
	}
}