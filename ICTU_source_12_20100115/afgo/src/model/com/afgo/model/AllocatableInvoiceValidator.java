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

import java.util.ArrayList;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.framework.POInfo;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AllocatableInvoiceValidator.java,v 1.14.2.1 2010/01/06 11:30:56 tomassen Exp $
 * 
 * Update Fund Schedule / Purchase Commitment "Invoiced Amt"
 * Verify Invoice / Purchase Commitment matching BPartner 
 */
public class AllocatableInvoiceValidator implements ModelValidator
{
	
	public AllocatableInvoiceValidator()
	{

	}
	
	private int AD_Client_ID  = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(this.getClass());
	
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		
		this.log.info("AllocatableInvoiceValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		// C_Invoice
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addModelChange(MInvoice.Table_Name, this);
		
		// C_InvoiceLine
		engine.addModelChange(MInvoiceLine.Table_Name, this);
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String docValidate (PO po, int docTiming)
	{
		if (po.get_Table_ID() == MInvoice.Table_ID)
		{
			MInvoice invoice = (MInvoice)po;
			AllocatableInvoice allocatableInvoice = new AllocatableInvoice(invoice);
			
			if (docTiming == ModelValidator.DOCTIMING_AFTER_COMPLETE)
			{
				return allocatableInvoice.afterComplete();
			}
			else if (docTiming == ModelValidator.DOCTIMING_BEFORE_PREPARE)
			{
				return allocatableInvoice.beforePrepare();
			}
		}
		
		return null;
	}

	public String modelChange (PO po, int type) throws Exception
	{
		String result = null;
		
		POInfo poinfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID());
	
		if (result == null && MInvoice.Table_Name.equals(poinfo.getTableName()))
		{
			MInvoice invoice = (MInvoice)po;
			Integer C_BPartner_ID = invoice.getC_BPartner_ID();
			Integer AFGO_PurchaseCommitment_ID = invoice.getAFGO_PurchaseCommitment_ID();
			String poReference = invoice.getPOReference();
			
			if (C_BPartner_ID != null && C_BPartner_ID > 0 && AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID > 0)
			{
				if (DB.getSQLValue(po.get_TrxName(), "SELECT C_BPartner_ID FROM AFGO_PurchaseCommitment WHERE AFGO_PurchaseCommitment_ID=?", AFGO_PurchaseCommitment_ID) != C_BPartner_ID)
					result = Msg.getMsg(invoice.getCtx(), "InvalidBPartnerPurchaseCommitment");
			}
			
			if (C_BPartner_ID != null && C_BPartner_ID > 0 && poReference != null && !"".equals(poReference))
			{
				//if (DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(*) FROM C_Invoice WHERE C_BPartner_ID")
				//		result = "DuplicatePOReference";
			}
			
			
		}

		if (result == null && MInvoiceLine.Table_Name.equals(poinfo.getTableName()))
		{
			MInvoiceLine il = (MInvoiceLine)po;
			// Lines without product or charge are not permitted
			if (il.getM_Product_ID() < 1 && il.getC_Charge_ID() < 1)
				result = Msg.getMsg(il.getCtx(), "NoProductOrCharge");
			
			// Lines with an AD_Org_ID different from that of the header are not allowed
			// request AFP10001198
			MInvoice i = new MInvoice(il.getCtx(), il.getC_Invoice_ID(), il.get_TrxName());
			il.setAD_Org_ID(i.getAD_Org_ID());
			
			// AFP10003140
			// Explicitly check that the Purchase Commitment Line selected on the Invoice Line
			// belongs to the Purchase Commitment selected on the Invoice Header
			AllocatableInvoice ai = new AllocatableInvoice(i);
			AllocatableInvoiceLine ail = new AllocatableInvoiceLine(il);
			if (ail.getPurchaseCommitmentLine() != null)
			{
				// AFP10003171
				// also allow lines from "additional" & "transfer" commitments that are linked to the master
				// selected on the invoice header
				if (ai.getPurchaseCommitment() == null || ai.getPurchaseCommitment().getAFGO_PurchaseCommitment_ID() != ail.getPurchaseCommitmentLine().getPurchaseCommitment().getMasterPurchaseCommitment().getAFGO_PurchaseCommitment_ID())
				{
					result = Msg.getMsg(il.getCtx(), "InvalidPurchaseCommitmentLine");
				}
			}
		}
				
		return result;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
