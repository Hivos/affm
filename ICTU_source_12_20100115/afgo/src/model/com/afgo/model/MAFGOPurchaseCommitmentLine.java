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
import java.util.Iterator;

import compiere.model.X_AFGO_PurchaseCommitmentLine;

import org.compiere.framework.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.wf.MWFActivity;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPurchaseCommitmentLine.java,v 1.17.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOPurchaseCommitmentLine extends X_AFGO_PurchaseCommitmentLine implements AllocatableDocumentLine
{

	public MAFGOPurchaseCommitmentLine (Ctx ctx, int AFGO_PurchaseCommitmentLine_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseCommitmentLine_ID, trxName);
		
	}
	
	public MAFGOPurchaseCommitmentLine (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		
	}
	
	private MAFGOPurchaseCommitment commitment = null;
	
	private MAFGOLotItem lotItem = null; 
	
	private MAFGOActivity activity = null;
	
	private CLogger log = CLogger.getCLogger(getClass());

	public MAFGOPurchaseCommitment getPurchaseCommitment()
	{
		if (this.commitment == null)
			this.commitment = new MAFGOPurchaseCommitment(this.getCtx(), this.getAFGO_PurchaseCommitment_ID(), this.get_TrxName());
		return this.commitment;
	}
	
	public MAFGOPurchaseCommitment getHeader()
	{
		return this.getPurchaseCommitment();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}
	
	public MAFGOLotItem getLotItem()
	{
		if (this.lotItem == null && this.getAFGO_LotItem_ID() > 0)
		{
			this.lotItem = MAFGOLotItem.getLotItem(this.getCtx(), this.getAFGO_LotItem_ID());
		}
		return this.lotItem;
	}
	
	public MAFGOActivity getActivity()
	{
		if (this.activity == null && this.getAFGO_Activity_ID() > 0)
			this.activity = MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
		return this.activity;
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}
	
	public MAFGOMonth getMonth()
	{
		return MAFGOMonth.getMonth(this.getCtx(), this.getAFGO_Month_ID());
	}
	
	public boolean updateInvoicedAmount(int C_Invoice_ID)
	{
		// invoice specified by C_Invoice_ID
		// might not be completed yet
		String sql = "UPDATE AFGO_PurchaseCommitmentLine pcl"
			+ " SET pcl.InvoicedAmt=(SELECT COALESCE((SELECT SUM(LineNetAmt) FROM C_InvoiceLine il INNER JOIN C_Invoice i ON (i.C_Invoice_ID=il.C_Invoice_ID) WHERE il.AFGO_PurchaseCommitmentLine_ID=pcl.AFGO_PurchaseCommitmentLine_ID AND (i.Processed='Y' OR i.DocStatus IN ('CO', 'CL') OR i.C_Invoice_ID=" + C_Invoice_ID + ")), 0) FROM DUAL)"
			+ " WHERE pcl.AFGO_PurchaseCommitmentLine_ID=?";
		
		if (DB.executeUpdate(sql, this.getAFGO_PurchaseCommitmentLine_ID(), this.get_TrxName()) != 1)
			return false;
		
		return this.updateHeader();
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// Set AD_Org_ID to org of header
		this.setAD_Org_ID(this.getHeader().getAD_Org_ID());
		
		if (this.getLotItem() != null)
		{
			if (this.getLotItem().getM_Product_ID() > 0)
			{
				this.setM_Product_ID(this.getLotItem().getM_Product_ID());
				this.setC_Charge_ID(0);
			}
			else if (this.getLotItem().getC_Charge_ID() > 0)
			{
				this.setC_Charge_ID(this.getLotItem().getC_Charge_ID());
				this.setM_Product_ID(0);
			}
		}
		
		this.setPlannedAmt(this.getQtyEntered().multiply(this.getPriceEntered()));
		
		// 2242
		if (this.getHeader().isTransferCommitment())
		{
			this.setQty(this.getQtyEntered());
			this.setPrice(this.getPriceEntered());
		}
		
		
		try
		{
			// 2005
			if (this.getHeader().isSecondmentCommitment())
			{
				this.setQty(this.getQtyEntered());
				this.setPrice(this.getPriceEntered());
			}
		}
		catch(NoSuchFieldError e)
		{
			// ICTU module not installed
		}
		
		
		// 1997
		if (this.getHeader().isAdditionalCommitment())
		{
			this.setQty(this.getQtyEntered());
			this.setPrice(this.getPriceEntered());
		}
		
		this.setLineNetAmt(this.getQty().multiply(this.getPrice()));
		
		if (this.getM_Product_ID() < 1 && this.getC_Charge_ID() < 1)
		{
			this.log.saveError("NoProductOrCharge", "");
			return false;
		}
		
		return true;
	}
	
	public boolean afterSave(boolean isNew, boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	public boolean afterDelete(boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	private boolean updateHeader()
	{
		MAFGOPurchaseCommitment header = this.getPurchaseCommitment();
		return header.updateTotal();
	}
	
	public MAFGOQuotationResponseLine getQuotationResponseLine(MAFGOQuotationResponse quotationResponse)
	{
		MAFGOQuotationResponseLine quotationResponseLine = null;
		
		if (quotationResponse == null)
			throw new IllegalStateException("No Quotation Response");
		
		if (this.getAFGO_PurchaseCommitment_ID() != quotationResponse.getAFGO_PurchaseCommitment_ID())
			throw new IllegalStateException("Invalid Quotation Response, AFGO_QuotationResponse_ID=" + quotationResponse.getAFGO_QuotationResponse_ID() + ", AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID());
		
		for (Iterator<MAFGOQuotationResponseLine> lines = quotationResponse.getLines().iterator(); lines.hasNext();)
		{
			MAFGOQuotationResponseLine line = lines.next();
			
			if (this.getAFGO_Activity_ID() != line.getAFGO_Activity_ID())
				continue;
			else if (this.getAFGO_Month_ID() != line.getAFGO_Month_ID())
				continue;
			else if (this.getAFGO_ServiceType_ID() != line.getAFGO_ServiceType_ID())
				continue;
			else if (this.getM_Product_ID() > 0 && this.getM_Product_ID() != line.getM_Product_ID())
				continue;
			else if (this.getC_Charge_ID() > 0 && this.getC_Charge_ID() != line.getC_Charge_ID())
				continue;
			
			quotationResponseLine = line;
		}
		
		return quotationResponseLine;
	}

}
