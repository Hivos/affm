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

import java.math.BigDecimal;

import org.compiere.framework.PO;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.wf.MWFActivity;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AllocatableInvoiceLine.java,v 1.8.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class AllocatableInvoiceLine implements AllocatableDocumentLine
{
	
	public AllocatableInvoiceLine(AllocatableInvoice invoice, MInvoiceLine line)
	{
		if (invoice == null)
			throw new IllegalStateException("No Invoice");
		this.header = invoice;
		
		if (line == null)
			throw new IllegalStateException("No Invoice LIne");
		this.invoiceLine = line;
	}
	
	public AllocatableInvoiceLine(MInvoiceLine line)
	{
		this((line != null) ? new AllocatableInvoice(new MInvoice(line.getCtx(), line.getC_Invoice_ID(), line.get_TrxName())) : null, line);
	}
	
	private AllocatableInvoice header = null;
	
	private MInvoiceLine invoiceLine = null;
	
	private MAFGOPurchaseCommitmentLine purchaseCommitmentLine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public PO getPO()
	{
		return this.invoiceLine;
	}
	
	public MAFGOPurchaseCommitmentLine getPurchaseCommitmentLine()
	{
		if (this.purchaseCommitmentLine == null && this.invoiceLine.get_ValueAsInt("AFGO_PurchaseCommitmentLine_ID") > 0)
			this.purchaseCommitmentLine = new MAFGOPurchaseCommitmentLine(this.invoiceLine.getCtx(), this.invoiceLine.get_ValueAsInt("AFGO_PurchaseCommitmentLine_ID"), this.invoiceLine.get_TrxName());
		return this.purchaseCommitmentLine;
	}

	public int getAFGO_Month_ID()
	{
		return this.invoiceLine.getAFGO_Month_ID();
	}

	public int getAFGO_ServiceType_ID()
	{
		return this.invoiceLine.getAFGO_ServiceType_ID();
	}

	public int getC_Charge_ID()
	{
		return this.invoiceLine.getC_Charge_ID();
	}

	public AllocatableDocument getHeader ()
	{
		return this.header;
	}

	public int getM_Product_ID ()
	{
		return this.invoiceLine.getM_Product_ID();
	}

	public BigDecimal getPrice ()
	{
		return this.invoiceLine.getPriceActual();
	}

	public BigDecimal getQty ()
	{
		return this.invoiceLine.getQtyInvoiced();
	}

	public int get_OldAFGO_Month_ID ()
	{
		return this.invoiceLine.get_ValueOld("AFGO_Month_ID") != null ? (Integer)this.invoiceLine.get_ValueOld("AFGO_Month_ID") : -1;
	}

	public MAFGOActivity getActivity ()
	{
		return MAFGOActivity.getActivity(this.invoiceLine.getCtx(), this.invoiceLine.getAFGO_Activity_ID());
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}

}
