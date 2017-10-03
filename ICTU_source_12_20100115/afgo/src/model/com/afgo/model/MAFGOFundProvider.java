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

import org.compiere.model.MBPartner;
import org.compiere.util.Ctx;

import compiere.model.X_AFGO_FundProvider;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFundProvider.java,v 1.4.2.1 2010/01/06 11:45:24 tomassen Exp $
 *
 */
public class MAFGOFundProvider extends X_AFGO_FundProvider
{

	public MAFGOFundProvider (Ctx ctx, int AFGO_FundProvider_ID, String trxName)
	{
		super(ctx, AFGO_FundProvider_ID, trxName);

	}

	public MAFGOFundProvider (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MBPartner bpartner = null;
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
			this.bpartner = new MBPartner(this.getCtx(), this.getC_BPartner_ID(), this.get_TrxName());
		return this.bpartner;
	}
	
	public MAFGOProgram getProgram()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// always org of program
		this.setAD_Org_ID(this.getProgram().getAD_Org_ID());
		
		return true;
	}
	
}
