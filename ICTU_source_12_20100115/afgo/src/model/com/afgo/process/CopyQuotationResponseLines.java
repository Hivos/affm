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

import java.util.Iterator;

import org.compiere.process.SvrProcess;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;

import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOPurchaseCommitmentLine;
import com.afgo.model.MAFGOQuotationResponse;
import com.afgo.model.MAFGOQuotationResponseLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CopyQuotationResponseLines.java,v 1.6.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CopyQuotationResponseLines extends SvrProcess
{
	
	protected void prepare ()
	{

	}

	protected String doIt () throws Exception
	{
		int AFGO_QuotationResponse_ID = this.getRecord_ID();
		
		if (AFGO_QuotationResponse_ID < 1)
			return "@NoQuotationResponse@";
		
		MAFGOQuotationResponse quotationResponse = new MAFGOQuotationResponse(this.getCtx(), AFGO_QuotationResponse_ID, this.get_TrxName());
		if (quotationResponse.getAFGO_QuotationResponse_ID() != AFGO_QuotationResponse_ID)
			return "@InvalidQuotationResponse@";
		
		if (DB.getSQLValue(this.get_TrxName(), "SELECT COUNT(*) FROM AFGO_QuotationResponseLine WHERE AFGO_QuotationResponse_ID=?", quotationResponse.getAFGO_QuotationResponse_ID()) > 0)
			return "@LinesAlreadyExist@";
		
		if (quotationResponse.getAFGO_PurchaseCommitment_ID() < 1)
			return "@NoPurchaseCommitment@";
		
		MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), quotationResponse.getAFGO_PurchaseCommitment_ID(), this.get_TrxName());
		if (purchaseCommitment.getAFGO_PurchaseCommitment_ID() != quotationResponse.getAFGO_PurchaseCommitment_ID())
			return "@InvalidPurchaseCommitment@";
		
		for (Iterator<MAFGOPurchaseCommitmentLine> lines = purchaseCommitment.getLines().iterator(); lines.hasNext();)
		{
			MAFGOPurchaseCommitmentLine purchaseCommitmentLine = lines.next();
			
			MAFGOQuotationResponseLine quotationResponseLine = new MAFGOQuotationResponseLine(this.getCtx(), 0, this.get_TrxName());
			quotationResponseLine.setLine(DB.getSQLValue(this.get_TrxName(), "SELECT COALESCE(MAX(Line), 0) + 10 FROM AFGO_QuotationResponseLine WHERE AFGO_QuotationResponse_ID=?", quotationResponse.getAFGO_QuotationResponse_ID()));
			quotationResponseLine.setAFGO_QuotationResponse_ID(quotationResponse.getAFGO_QuotationResponse_ID());
			quotationResponseLine.setAFGO_LotItem_ID(purchaseCommitmentLine.getAFGO_LotItem_ID());
			quotationResponseLine.setM_Product_ID(purchaseCommitmentLine.getM_Product_ID());
			quotationResponseLine.setC_Charge_ID(purchaseCommitmentLine.getC_Charge_ID());
			quotationResponseLine.setQty(purchaseCommitmentLine.getQtyEntered());
			quotationResponseLine.setPrice(purchaseCommitmentLine.getPriceEntered());
			quotationResponseLine.setAFGO_Quarter_ID(purchaseCommitmentLine.getAFGO_Quarter_ID());
			quotationResponseLine.setAFGO_Month_ID(purchaseCommitmentLine.getAFGO_Month_ID());
			quotationResponseLine.setAFGO_ProjectCluster_ID(purchaseCommitmentLine.getAFGO_ProjectCluster_ID());
			quotationResponseLine.setAFGO_Project_ID(purchaseCommitmentLine.getAFGO_Project_ID());
			quotationResponseLine.setAFGO_Phase_ID(purchaseCommitmentLine.getAFGO_Phase_ID());
			quotationResponseLine.setAFGO_Activity_ID(purchaseCommitmentLine.getAFGO_Activity_ID());
			quotationResponseLine.setAFGO_ServiceType_ID(purchaseCommitmentLine.getAFGO_ServiceType_ID());
			
			if (!quotationResponseLine.save())
				return "@SaveError@ AFGO_QuotationResponseLine_ID=" + quotationResponseLine.getAFGO_QuotationResponse_ID();
		}
		
		return "@OK@";
	}



}
