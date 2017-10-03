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

import java.util.ArrayList;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import com.afgo.model.MAFGOPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ForcePurchaseCommitment.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ForcePurchaseCommitment extends SvrProcess
{
	private int AFGO_PurchaseCommitment_ID = 0;
	
	private String PurchaseCommitmentStatus = null;
	
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
			else if (name.equals("PurchaseCommitmentStatus"))
				this.PurchaseCommitmentStatus = (String)para[i].getParameter();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.AFGO_PurchaseCommitment_ID, this.get_TrxName());
		if (purchaseCommitment.getAFGO_PurchaseCommitment_ID() < 1 || purchaseCommitment.getAFGO_PurchaseCommitment_ID() != this.AFGO_PurchaseCommitment_ID)
			throw new IllegalStateException("Invalid AFGO_PurchaseCommitment_ID: " + this.AFGO_PurchaseCommitment_ID);
		
		String purchaseCommitmentStatus = ((this.PurchaseCommitmentStatus != null && !"".equals(this.PurchaseCommitmentStatus)) ? this.PurchaseCommitmentStatus : purchaseCommitment.getPurchaseCommitmentStatus());
		
		log.info("AFGO_PurchaseCommitment_ID=" + purchaseCommitment.getAFGO_PurchaseCommitment_ID() + ", PurchaseCommitmentStatus: " + purchaseCommitment.getPurchaseCommitmentStatus() + "->" + purchaseCommitmentStatus);
		
		int updated = 0;
		
		if (!purchaseCommitmentStatus.equals(purchaseCommitment.getPurchaseCommitmentStatus()))
		{
    		String sql = "UPDATE AFGO_PurchaseCommitment"
    			+ " SET PurchaseCommitmentStatus=?"
    			+ " WHERE AFGO_PurchaseCommitment_ID=?";
    		
    		ArrayList<Object> params = new ArrayList<Object>();
    		params.add(this.PurchaseCommitmentStatus);
    		params.add(purchaseCommitment.getAFGO_PurchaseCommitment_ID());
    		
    		updated = DB.executeUpdate(sql, params, false, this.get_TrxName());
		}
		
		return "@Updated@=" + updated;
	}

}
