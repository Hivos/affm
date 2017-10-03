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

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;


import compiere.model.X_AFGO_QuotationResponse;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOQuotationResponse.java,v 1.12.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class MAFGOQuotationResponse extends X_AFGO_QuotationResponse implements DocAction
{

	public MAFGOQuotationResponse (Ctx ctx, int AFGO_QuotationResponse_ID, String trxName)
	{
		super(ctx, AFGO_QuotationResponse_ID, trxName);

	}
	
	public MAFGOQuotationResponse (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOPurchaseCommitment purchaseCommitment = null;
	
	private String processMsg = null;
	
	private ArrayList<MAFGOQuotationResponseLine> lines = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public MAFGOPurchaseCommitment getPurchaseCommitment()
	{
		if (this.purchaseCommitment == null && this.getAFGO_PurchaseCommitment_ID() > 0)
			this.purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.getAFGO_PurchaseCommitment_ID(), this.get_TrxName());
		return this.purchaseCommitment;
	}
	
	public ArrayList<MAFGOQuotationResponseLine> getLines()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOQuotationResponseLine>();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String sql = "SELECT *"
				+ " FROM AFGO_QuotationResponseLine"
				+ " WHERE AFGO_QuotationResponse_ID=?"
				+ " ORDER BY Line";
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_QuotationResponse_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					this.lines.add(new MAFGOQuotationResponseLine(this.getCtx(), rs, this.get_TrxName()));
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		return this.lines;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getAFGO_QuotationRequest_ID() < 1 && this.getAFGO_PurchaseCommitment_ID() < 1)
		{
			this.log.saveError("NoQuotationRequestOrPurchaseCommitment", ""); 
			return false;
		}
		else if (this.getAFGO_PurchaseCommitment_ID() > 0)
		{
			if (this.getAD_Org_ID() != this.getPurchaseCommitment().getAD_Org_ID())
			{
				this.setAD_Org_ID(this.getPurchaseCommitment().getAD_Org_ID());
			}
			if (this.getPurchaseCommitment().isAdditionalCommitment())
			{
				if (this.getC_BPartner_ID() != this.getPurchaseCommitment().getMasterPurchaseCommitment().getC_BPartner_ID())
				{
				this.log.saveError("QuotationResponseInvalidBPartner", "");
				return false;
				}
			}
		}
			
		return true;
	}

	public boolean processIt (String processAction) throws Exception
	{
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}
	
	public String prepareIt ()
	{
		log.fine(toString());
		processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_BEFORE_PREPARE);
		if (processMsg != null)
			return DOCSTATUS_Invalid;
		
		ArrayList<MAFGOQuotationResponseLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt ()
	{
		return true;
	}
	
	public boolean rejectIt ()
	{
		return true;
	}

	public String completeIt ()
	{
		if (this.getAFGO_PurchaseCommitment_ID() > 0)
		{
			// check if Purchase Commitment is open for quote responses
			MAFGOPurchaseCommitment purchaseCommitment = this.getPurchaseCommitment();
			if (!MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_QuoteInvitation.equals(purchaseCommitment.getPurchaseCommitmentStatus()))
			{
				this.processMsg = "@PurchaseCommitmentNotOpenForQuoteResponses@";
				return this.getDocStatus();
			}
			
			// check if the response date is not exceeded
			if (purchaseCommitment.getCloseDate() != null && purchaseCommitment.getCloseDate().before(this.getDateDoc()))
			{
				this.processMsg = "@ResponseDateExceeded@";
				return this.getDocStatus();
			}
			
		}
		
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{

		this.setDocAction(DOCACTION_None);
		this.setDocStatus(DOCSTATUS_Closed);
		return true;
	}
	
	public boolean reActivateIt ()
	{
		return true;
	}

	public boolean voidIt ()
	{
		return true;
	}
	
	public boolean invalidateIt ()
	{
		return true;
	}
	
	public boolean reverseAccrualIt ()
	{
		return true;
	}
	

	public boolean reverseCorrectIt ()
	{
		return true;
	}

	public boolean unlockIt ()
	{
		this.log.info("unlockIt: " + this.toString());
		this.setProcessing(false);
		return true;
	}

	public File createPDF ()
	{
		throw new IllegalStateException("Not Implemented");
	}

	public BigDecimal getApprovalAmt ()
	{
		return this.getGrandTotal();
	}

	public int getDoc_User_ID ()
	{
		return this.getCreatedBy();
	}

	public String getDocumentInfo ()
	{
		return Table_Name + " " + this.getDocumentNo();
	}

	public String getSummary ()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.getDocumentNo());
		sb.append(": ");
		sb.append(Msg.translate(getCtx(),"GrandTotal"));
		sb.append("=");
		sb.append(this.getGrandTotal());
		sb.append(" ");
		if (this.getDescription() != null && this.getDescription().length() > 0)
			sb.append(this.getDescription());
		return sb.toString();
	}
	
	public String getProcessMsg ()
	{
		return this.processMsg;
	}

}
