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

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

import com.afgo.model.MAFGOPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CompletePurchaseCommitment.java,v 1.6.2.1 2010/01/06 11:45:30 tomassen Exp $
 * 
 * Complete PC without executing the workflow.
 * For development/testing purpose only.
 *
 */
public class CompletePurchaseCommitment extends SvrProcess
{
	private int AFGO_PurchaseCommitment_ID = -1;
	
	private int C_BPartner_ID = -1;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AFGO_PurchaseCommitment_ID"))
				this.AFGO_PurchaseCommitment_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				this.C_BPartner_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		log.info("AFGO_PurchaseCommitment_ID=" + this.AFGO_PurchaseCommitment_ID);
		
		String sql = "UPDATE AFGO_PurchaseCommitmentLine"
			 + " SET Price=PriceEntered, Qty=QtyEntered, LineNetAmt=PlannedAmt"
			 + " WHERE AFGO_PurchaseCommitment_ID=?"
			 + " AND NOT LineNetAmt <> 0";
		
		int lines = DB.executeUpdate(sql, this.AFGO_PurchaseCommitment_ID, this.get_TrxName());
	
		sql = "UPDATE AFGO_PurchaseCommitment"
			+ " SET DocStatus='CO', DocAction='CL', Processed='Y', Processing='N',"
			+ " PurchaseCommitmentStatus='FR'"
			+ " WHERE AFGO_PurchaseCommitment_ID=?";
		
		int doc = DB.executeUpdate(sql, this.AFGO_PurchaseCommitment_ID, this.get_TrxName());
		
		MAFGOPurchaseCommitment pc = new MAFGOPurchaseCommitment(this.getCtx(), this.AFGO_PurchaseCommitment_ID, this.get_TrxName());
		if (pc.getC_BPartner_ID() < 1 && this.C_BPartner_ID > 0)
			pc.setC_BPartner_ID(this.C_BPartner_ID);
		pc.updateTotal();
		pc.save();
		
		return "lines=" + lines;
	}

}
