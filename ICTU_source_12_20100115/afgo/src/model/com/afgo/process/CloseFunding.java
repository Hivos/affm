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
package com.afgo.process;

import org.compiere.process.SvrProcess;

import com.afgo.model.MAFGOFund;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CloseFunding.java,v 1.2.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CloseFunding extends SvrProcess
{
	
	private int AFGO_Fund_ID = 0;

	protected void prepare()
	{
		this.AFGO_Fund_ID = this.getRecord_ID();
	}
	
	protected String doIt() throws Exception
	{
		if (this.AFGO_Fund_ID < 1)
			throw new IllegalStateException("NoFunding: AFGO_Fund_ID=" + this.AFGO_Fund_ID);
		
		MAFGOFund fund = new MAFGOFund(this.getCtx(), this.AFGO_Fund_ID, this.get_TrxName());
		if (fund.getAFGO_Fund_ID() != this.AFGO_Fund_ID)
			throw new IllegalStateException("InvalidFunding: AFGO_Fund_ID=" + this.AFGO_Fund_ID);
		
		if (!fund.isProcessed())
			throw new IllegalStateException("CannotClose: AFGO_Fund_ID=" + fund.getAFGO_Fund_ID());
		
		boolean closeOrReopen = "N".equals(fund.getIsClosed()) ? true : false;
		
		log.info(closeOrReopen ? "close: " : "reopen: " + fund);
		
		fund.setIsClosed(closeOrReopen ? "Y" : "N");
		
		if (fund.save())
			return null;
		else
			return "CannotSave";
		
	}

}
