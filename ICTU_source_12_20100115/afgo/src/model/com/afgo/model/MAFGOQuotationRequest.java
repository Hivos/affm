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

import compiere.model.X_AFGO_QuotationRequest;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOQuotationRequest.java,v 1.10.2.1 2010/01/06 11:45:26 tomassen Exp $
 * 
 * TODO: implement AllocatableDocument instead of DocAction
 *
 */
public class MAFGOQuotationRequest extends X_AFGO_QuotationRequest implements DocAction
{

	public MAFGOQuotationRequest (Ctx ctx, int AFGO_QuotationRequest_ID, String trxName)
	{
		super(ctx, AFGO_QuotationRequest_ID, trxName);

	}
	
	public MAFGOQuotationRequest (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private ArrayList<MAFGOQuotationRequestLine> lines = null;
	
	private String processMsg = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public ArrayList<MAFGOQuotationRequestLine> getLines()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOQuotationRequestLine>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_QuotationRequestLine"
				+ " WHERE AFGO_QuotationRequest_ID=?";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_QuotationRequest_ID());
				
				rs = pstmt.executeQuery();
				
				while(rs.next())
					this.lines.add(new MAFGOQuotationRequestLine(this.getCtx(), rs, this.get_TrxName()));
			}
			catch(Exception e)
			{
				log.severe(e.toString());
				e.printStackTrace();
			}
			finally
			{
				if (rs!= null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.lines;
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
		
		ArrayList<MAFGOQuotationRequestLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		// check that there are no existing quotation requests for the same domain/lot
		// with an overlapping period.
		String sql = "SELECT COUNT(*)"
			+ " FROM AFGO_QuotationRequest qro, AFGO_QuotationRequest qrn"
			+ " WHERE qrn.AFGO_QuotationRequest_ID=?"						// 1
			+ " AND qro.AFGO_PurchaseLot_ID=qrn.AFGO_PurchaseLot_ID"
			//+ " AND qro.AFGO_PurchaseDomain_ID=qrn.AFGO_PurchaseDomain_ID"
			+ " AND qro.DocStatus IN ('CO', 'CL')"
			+ " AND ((qro.StartDate>=qrn.StartDate AND qro.StartDate<=qrn.EndDate) OR (qro.EndDate>=qrn.StartDate AND qro.EndDate<=qrn.EndDate))";
		
		if (DB.getSQLValue(this.get_TrxName(), sql, this.getAFGO_QuotationRequest_ID()) != 0)
		{
			this.processMsg = "@QuotationRequestConflict@";
			return this.getDocStatus();
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
		this.setProcessed(true);
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{
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
