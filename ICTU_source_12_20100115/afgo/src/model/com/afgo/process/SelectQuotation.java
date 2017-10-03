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

import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOQuotationResponse;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: SelectQuotation.java,v 1.10.2.1 2010/01/06 11:45:33 tomassen Exp $
 *
 */
public class SelectQuotation extends SvrProcess
{

	private int AFGO_PurchaseCommitment_ID = -1;
	
	private int AFGO_QuotationResponse_ID = -1;
	
	private boolean overrideExistingQuotation = false;
	
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AFGO_QuotationResponse_ID"))
				this.AFGO_QuotationResponse_ID = para[i].getParameterAsInt();
			else if (name.equals("IsOverrideExistingQuotation"))
				this.overrideExistingQuotation = "Y".equals(para[i].getParameter());
		}
		
		this.AFGO_PurchaseCommitment_ID = this.getRecord_ID();
	}
	

	protected String doIt () throws Exception
	{
		MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.AFGO_PurchaseCommitment_ID, this.get_TrxName());
		if (purchaseCommitment.getAFGO_PurchaseCommitment_ID() < 1 || purchaseCommitment.getAFGO_PurchaseCommitment_ID() != this.AFGO_PurchaseCommitment_ID)
			throw new IllegalStateException("Invalid Purchase Commitment: AFGO_PurchaseCommitment_ID=" + this.AFGO_PurchaseCommitment_ID);
		
		MAFGOQuotationResponse quotationResponse = new MAFGOQuotationResponse(this.getCtx(), this.AFGO_QuotationResponse_ID, this.get_TrxName());
		if (quotationResponse.getAFGO_QuotationResponse_ID() < 1 || quotationResponse.getAFGO_QuotationResponse_ID() != this.AFGO_QuotationResponse_ID)
			throw new IllegalStateException("Invalid Quotation Response: AFGO_QuotationResponse_ID=" + this.AFGO_QuotationResponse_ID);
		
		if (purchaseCommitment.isAdditionalCommitment())
		{
			if (purchaseCommitment.getMasterPurchaseCommitment().getC_BPartner_ID() != quotationResponse.getC_BPartner_ID())
				return "@InvalidBPartner@";
			
		}
		
		purchaseCommitment.setQuotationResponse(quotationResponse, this.overrideExistingQuotation);
		
		return null;
	}
}
