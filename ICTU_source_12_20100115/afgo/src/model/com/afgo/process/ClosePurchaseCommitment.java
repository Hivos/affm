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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

import com.afgo.model.MAFGOYear;
import com.afgo.model.MasterPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ClosePurchaseCommitment.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ClosePurchaseCommitment extends SvrProcess
{
	private int AD_Org_ID = 0;
	
	private int AFGO_Program_ID = 0;
	
	private int AFGO_PurchaseCommitment_ID = 0;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_PurchaseCommitment_ID"))
				this.AFGO_PurchaseCommitment_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}

	protected String doIt() throws Exception
	{
		if (this.AFGO_PurchaseCommitment_ID < 1)
		{
			throw new IllegalStateException("No AFGO_PurchaseCommitment_ID");
		}
		
		MasterPurchaseCommitment purchaseCommitment = new MasterPurchaseCommitment(this.getCtx(), this.AFGO_PurchaseCommitment_ID, this.get_TrxName());

		boolean hasOpenAmt = false;
		
		// there should not be an open amount in any of the years covered by this commitment
		for (Iterator<MAFGOYear> years = purchaseCommitment.getOrderedYears().iterator(); years.hasNext();)
		{
			MAFGOYear year = years.next();
			BigDecimal openAmt = purchaseCommitment.getOpenAmt(year.getAFGO_Year_ID());
			
			if (openAmt.compareTo(Env.ZERO) != 0)
			{
				hasOpenAmt = true;
				//log.warning("OpenAmt: " + year.getName() + "=" + openAmt);
				super.addLog("OpenAmt: " + year.getName() + "=" + openAmt);
			}
		}
		
		if (hasOpenAmt)
		{
			return "@CannotClose@";
		}
		
		Date today = new Date();
		if (today.before(purchaseCommitment.getMinimumCloseDate()))
		{
			return "@CannotCloseBefore@ " + DisplayType.getDateFormat().format(purchaseCommitment.getMinimumCloseDate());
		}
		
		purchaseCommitment.closeDocument();
		
		return "@DocumentClosed@";
	}

}
