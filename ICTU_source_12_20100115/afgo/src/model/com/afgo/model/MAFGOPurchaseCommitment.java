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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import compiere.model.X_AFGO_PurchaseCommitment;

import org.compiere.api.ModelValidator;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;

import com.afgo.util.DocumentPrintHelper;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOPurchaseCommitment.java,v 1.43.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class MAFGOPurchaseCommitment extends X_AFGO_PurchaseCommitment implements AllocatableDocument
{

	public MAFGOPurchaseCommitment (Ctx ctx, int AFGO_PurchaseCommitment_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseCommitment_ID, trxName);

	}
	
	public MAFGOPurchaseCommitment (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOPurchaseCommitmentType purchaseCommitmentType = null;
	
	private MAFGOPurchaseCommitment masterPurchaseCommitment = null;
	
	private ArrayList<MAFGOPurchaseCommitmentLine> lines = null;
	
	private MAFGOQuotationResponse quotationResponse = null;
	
	private MAFGOProgram program = null;
	
	private MBPartner bpartner = null;
	
	private String processMsg = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String toString()
	{
		return this.getDocumentInfo();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public MAFGOProgram getProgram()
	{
		if (this.program == null && this.getAFGO_Program_ID() > 0)
			this.program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
		return this.program;
	}
	
	public MAFGOProgram getWorkflowProgram(MWFActivity activity)
	{
		return this.getProgram();
	}
	
	public void resetWorkflow()
	{
		if (DOCSTATUS_Completed.equals(this.getDocStatus()))
			return;
		if (DOCSTATUS_Closed.equals(this.getDocStatus()))
			return;
		
		this.setPurchaseCommitmentStatus(MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Drafted);
		this.setDocStatus(DOCSTATUS_Drafted);
		
		this.unlockIt();
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
			this.bpartner = new MBPartner(this.getCtx(), this.getC_BPartner_ID(), this.get_TrxName());
		return this.bpartner;
	}
	
	public MDocType getDocType()
	{
		return MDocType.get(this.getCtx(), this.getC_DocType_ID());
	}
	
	public MAFGOPurchaseCommitmentType getPurchaseCommitmentType()
	{
		if (this.purchaseCommitmentType == null)
			this.purchaseCommitmentType = MAFGOPurchaseCommitmentType.getPurchaseCommitmentType(this.getDocType());
		return this.purchaseCommitmentType;
	}
	
	public MAFGOPurchaseCommitment getMasterPurchaseCommitment()
	{
		return this.getMasterPurchaseCommitment(false);
	}
	
	public MAFGOPurchaseCommitment getMasterPurchaseCommitment(boolean reload)
	{
		if (reload)
			this.masterPurchaseCommitment = null;
		
		if (this.masterPurchaseCommitment == null) 
		{
    		if (!this.isMasterCommitment())
    			this.masterPurchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.getMasterPurchaseCommitment_ID(), this.get_TrxName());
    		else
    			this.masterPurchaseCommitment = this;
		}
		
		return this.masterPurchaseCommitment;
	}
	
	public ArrayList<MAFGOPurchaseCommitmentLine> getLines()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<MAFGOPurchaseCommitmentLine>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_PurchaseCommitmentLine"
				+ " WHERE AFGO_PurchaseCommitment_ID=?"
				+ " AND IsActive='Y'"
				+ " ORDER BY Line";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
				rs = pstmt.executeQuery();
				
				while(rs.next())
					this.lines.add(new MAFGOPurchaseCommitmentLine(this.getCtx(), rs, this.get_TrxName()));
				
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
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
	
	public BigDecimal getAbsoluteTotal()
	{
		BigDecimal total = Env.ZERO;
		
		for(Iterator<MAFGOPurchaseCommitmentLine> lines = this.getLines().iterator(); lines.hasNext();)
		{
			MAFGOPurchaseCommitmentLine line = lines.next();
			total = total.add(line.getLineNetAmt().abs());
		}
		
		return total;
	}
	
	public ArrayList<MAFGOMonth> getMonths ()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		for (Iterator<MAFGOPurchaseCommitmentLine> lines = this.getLines().iterator(); lines.hasNext();)
			months.add(lines.next().getMonth());
		
		return months;
	}
	
	public MAFGOQuotationResponse getQuotationResponse()
	{
		if (this.quotationResponse == null && this.getAFGO_QuotationResponse_ID() > 0)
			this.quotationResponse = new MAFGOQuotationResponse(this.getCtx(), this.getAFGO_QuotationResponse_ID(), this.get_TrxName());
		return this.quotationResponse;
	}
	
	public boolean updateTotal()
	{
		boolean success = true;
				
		PreparedStatement pstmt = null;
		
		try
		{
			// PlannedAmt
			String sql = "UPDATE AFGO_PurchaseCommitment p"
				+ " SET p.PlannedAmt="
				+ " ("
				+ " 	SELECT COALESCE(SUM(PlannedAmt), 0)"
				+ " 	FROM AFGO_PurchaseCommitmentLine" 
				+ " 	WHERE AFGO_PurchaseCommitment_ID=p.AFGO_PurchaseCommitment_ID"
				+ " )"
				+ " WHERE p.AFGO_PurchaseCommitment_ID=?";
			
			log.fine("PlannedAmt: AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID() + ", sql=" + sql);
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
			success = (pstmt.executeUpdate() == 1);
			pstmt.close();
			pstmt = null;
			if (!success)
				return success;
			
			// TotalLines
			sql = "UPDATE AFGO_PurchaseCommitment p" 
				+ " SET p.TotalLines="
				+ " ("
				+ " 	SELECT COALESCE(SUM(LineNetAmt), 0)"
				+ " 	FROM AFGO_PurchaseCommitmentLine"
				+ " 	WHERE AFGO_PurchaseCommitment_ID=p.AFGO_PurchaseCommitment_ID"
				+ " )"
				+ " WHERE p.AFGO_PurchaseCommitment_ID=?";
			
			log.fine("TotalLines: AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID() + ", sql=" + sql);
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
			success = (pstmt.executeUpdate() == 1);
			pstmt.close();
			pstmt = null;
			if (!success)
				return success;
			
			// GrandTotal
			sql = "UPDATE AFGO_PurchaseCommitment p"
				+ " SET p.GrandTotal=p.TotalLines + "
				+ " ("
				+ " 	SELECT COALESCE(SUM(TotalLines), 0)"
				+ " 	FROM AFGO_PurchaseCommitment"
				+ " 	WHERE C_DocType_ID IN"
				+ "		("
				+ " 		SELECT dt.C_DocType_ID"
				+ " 		FROM C_DocType dt"
				+ " 		INNER JOIN AFGO_PurchaseCommitmentType pct ON (pct.AFGO_PurchaseCommitmentType_ID=dt.AFGO_PurchaseCommitmentType_ID)"
				+ " 		WHERE dt.DocBaseType='XPC'"
				+ " 		AND pct.IsMasterCommitment='N'"
				+ " 	)" 
				+ " 	AND MasterPurchaseCommitment_ID=p.AFGO_PurchaseCommitment_ID"
				+ " 	AND DocStatus IN ('CO', 'CL')"
				+ " 	AND PurchaseCommitmentStatus IN ('FR')"
				+ " )"
				+ " WHERE p.AFGO_PurchaseCommitment_ID=?";
			
			log.fine("GrandTotal: AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID() + ", sql=" + sql);
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
			success = (pstmt.executeUpdate() == 1);
			pstmt.close();
			pstmt = null;
			if (!success)
				return success;
			
			// Update master document
			if (!this.isMasterCommitment())
			{
				if (this.getMasterPurchaseCommitment() != this)
				{
					this.getMasterPurchaseCommitment().updateTotal();
				}
			}
			
			// InvoicedAmt
			else
			{
				sql = "UPDATE AFGO_PurchaseCommitment p"
					+ " SET p.InvoicedAmt="
					+ " ("
					+ " 	SELECT COALESCE(SUM(InvoicedAmt), 0)"
					+ " 	FROM AFGO_PurchaseCommitmentLine pcl"
					+ " 	WHERE pcl.AFGO_PurchaseCommitment_ID=p.AFGO_PurchaseCommitment_ID"
					+ " 	OR AFGO_PurchaseCommitment_ID IN"
					+ " 	("
					+ " 		SELECT AFGO_PurchaseCommitment_ID"
					+ " 		FROM AFGO_PurchaseCommitment"
					+ " 		WHERE C_DocType_ID IN "
					+ " 		(" 
					+ "				SELECT dt.C_DocType_ID"
					+ " 			FROM C_DocType dt"
					+ " 			INNER JOIN AFGO_PurchaseCommitmentType pct ON (pct.AFGO_PurchaseCommitmentType_ID=dt.AFGO_PurchaseCommitmentType_ID)"
					+ " 			WHERE dt.DocBaseType='XPC'"
					+ " 			AND pct.IsMasterCommitment='N'"
					+ "			)"
					+ " 		AND MasterPurchaseCommitment_ID=p.AFGO_PurchaseCommitment_ID"
					+ " 		AND Processed='Y'"
					+ " 	)" 
					+ "	)"
					+ " WHERE p.AFGO_PurchaseCommitment_ID=?";
				log.fine("InvoicedAmt: sql=" + sql);
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
				success = (pstmt.executeUpdate() == 1);
				pstmt.close();
				pstmt = null;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			success = false;
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return success;
	}
	
	public void setQuotationResponse(MAFGOQuotationResponse quotationResponse)
	{
		this.setQuotationResponse(quotationResponse, false);
	}
	
	public void setQuotationResponse(MAFGOQuotationResponse quotationResponse, boolean override)
	{
		if (this.getAFGO_QuotationResponse_ID() > 0 && !override)
			throw new IllegalStateException("Quotation response already selected");
		
		if (quotationResponse == null || !(DOCSTATUS_Closed.equals(quotationResponse.getDocStatus()) || DOCSTATUS_Completed.equals(quotationResponse.getDocStatus())))
			throw new IllegalStateException("Invalid Quotation Response: " + quotationResponse);
		
		HashMap<MAFGOPurchaseCommitmentLine,MAFGOQuotationResponseLine> lineMap = new HashMap<MAFGOPurchaseCommitmentLine,MAFGOQuotationResponseLine>();
		
		for (Iterator<MAFGOPurchaseCommitmentLine> lines = this.getLines().iterator(); lines.hasNext();)
		{
			MAFGOPurchaseCommitmentLine purchaseCommitmentLine = lines.next();
			MAFGOQuotationResponseLine quotationResponseLine = purchaseCommitmentLine.getQuotationResponseLine(quotationResponse);
		
			if (quotationResponseLine == null)
				throw new IllegalStateException("No QuotationResponseLine: AFGO_PurchaseCommitmentLine_ID=" + purchaseCommitmentLine.getAFGO_PurchaseCommitmentLine_ID());
		
			if (lineMap.containsValue(quotationResponseLine))
				throw new IllegalStateException("Duplicate QuotationResponseLine: AFGO_PurchaseCommitmentLine_ID=" + purchaseCommitmentLine.getAFGO_PurchaseCommitmentLine_ID() + ", AFGO_QuotationResponseLine_ID=" + quotationResponseLine.getAFGO_QuotationResponseLine_ID());
			
			lineMap.put(purchaseCommitmentLine, quotationResponseLine);
		}
		
		for (Iterator<MAFGOQuotationResponseLine> lines = quotationResponse.getLines().iterator(); lines.hasNext();)
		{
			MAFGOQuotationResponseLine quotationResponseLine = lines.next();
			if (!lineMap.containsValue(quotationResponseLine))
				throw new IllegalStateException("No PurchaseCommitmentLine: AFGO_QuotationResponseLine_ID" + quotationResponseLine.getAFGO_QuotationResponseLine_ID());
		}
		
		for (Iterator<MAFGOPurchaseCommitmentLine> lines = lineMap.keySet().iterator(); lines.hasNext();)
		{
			MAFGOPurchaseCommitmentLine purchaseCommitmentLine = lines.next();
			MAFGOQuotationResponseLine quotationResponseLine = lineMap.get(purchaseCommitmentLine);
			
			purchaseCommitmentLine.setQty(quotationResponseLine.getQty());
			purchaseCommitmentLine.setPrice(quotationResponseLine.getPrice());
			purchaseCommitmentLine.save();
		}
		
		this.setAFGO_QuotationResponse_ID(quotationResponse.getAFGO_QuotationResponse_ID());
		this.save();
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		//TODO: fix AD_Messages + AD_Element translate
		
		if (this.getDateFrom().after(this.getDateTo()))
		{
			this.log.saveError("StartDateAfterEndDate", Msg.getMsg(this.getCtx(), "DateFrom") + "=" + this.getDateFrom() + ", " + Msg.getMsg(this.getCtx(), "DateTo") + "=" + this.getDateTo());
			return false;
		}
		
		if (this.getProgram() != null && this.getProgram().getStartDate().after(this.getDateFrom()))
		{
			this.log.saveError("DateFromBeforeProgramStartDate", Msg.getMsg(this.getCtx(), "DateFrom")  + "=" + this.getDateFrom() + ", " + Msg.translate(this.getCtx(), "AFGO_Program_ID") + "." + Msg.getMsg(this.getCtx(), "StartDate") + "="+ this.getProgram().getStartDate());
			return false;
		}
		
		if (this.getProgram() != null && this.getProgram().getEndDate().before(this.getDateTo()))
		{
			this.log.saveError("DateToAfterProgramEndDate", Msg.translate(this.getCtx(), "DateTo") + "=" + this.getDateTo() + ", " + Msg.translate(this.getCtx(), "AFGO_Program_ID") + " " + Msg.translate(this.getCtx(), "EndDate") + "=" + this.getProgram().getEndDate());
			return false;
		}

		if (!this.isMasterCommitment())
		{
			MAFGOPurchaseCommitment masterPurchaseCommitment = this.getMasterPurchaseCommitment();
			if (this.getDateFrom().before(masterPurchaseCommitment.getDateFrom()))
			{
				this.log.saveError("DateFromBeforeMasterDateFrom", Msg.getMsg(this.getCtx(), "DateFrom") + "=" + this.getDateFrom() + ", " + Msg.translate(this.getCtx(), "MasterPurchaseCommitment_ID")  + "." + Msg.getMsg(this.getCtx(), "DateFrom") + "=" + masterPurchaseCommitment.getDateFrom());
				return false;
			}
			if (this.getC_BPartner_ID() != masterPurchaseCommitment.getC_BPartner_ID())
			{
				this.setC_BPartner_ID(masterPurchaseCommitment.getC_BPartner_ID());
			}
		}
		
		if (!newRecord && this.getAFGO_QuotationResponse_ID() > 0)
			this.setC_BPartner_ID(this.getQuotationResponse().getC_BPartner_ID());
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{	
		if (!success)
			return success;
		
		if (!this.isMasterCommitment())
		{
			MAFGOPurchaseCommitment masterPurchaseCommitment = this.getMasterPurchaseCommitment(true);
			masterPurchaseCommitment.updateTotal();
			//masterPurchaseCommitment.save(this.get_TrxName());
		}
		
		return success;
	}
	
	/*
	 * Override, as value from ColumnSQL is not available before save
	 */
	public boolean isMasterCommitment() 
	{
		return this.getPurchaseCommitmentType().isMasterCommitment();
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
		
		if (this.getMasterPurchaseCommitment().isClosed())
		{
			this.processMsg = "@MasterCommitmentClosed@";
			return DOCSTATUS_Invalid;
		}
		
		ArrayList<MAFGOPurchaseCommitmentLine> lines = this.getLines();
		if (lines.size() < 1)
		{
			processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		// AFP3272: Transfer document is never allowed to increase total commitment amount
		if (this.getPurchaseCommitmentType().isTransferCommitment())
		{
			if (Env.ZERO.compareTo(this.getTotalLines()) == -1)
			{
				processMsg = "InvalidTransfer";
				return DocActionConstants.STATUS_Invalid;
			}
		}
			
		if (DOCSTATUS_Approved.equals(this.getDocStatus()) && (PURCHASECOMMITMENTSTATUS_QuoteInvitation.equals(this.getPurchaseCommitmentStatus()) || PURCHASECOMMITMENTSTATUS_QuoteSelection.equals(this.getPurchaseCommitmentStatus())))
		{
				this.setPurchaseCommitmentStatus(PURCHASECOMMITMENTSTATUS_InProgress);
		}
		else if (DOCSTATUS_NotApproved.equals(this.getDocStatus()) || DOCSTATUS_Invalid.equals(this.getDocStatus()))
		{
			this.setPurchaseCommitmentStatus(PURCHASECOMMITMENTSTATUS_Drafted);
			this.setAFGO_QuotationResponse_ID(0);
			this.quotationResponse = null;
		}
		
		this.setDocAction(DOCACTION_Complete);
		
		return DOCSTATUS_InProgress;
	}

	public boolean approveIt ()
	{
		log.info(toString());
		return true;
	}
	
	public boolean rejectIt ()
	{
		log.info(toString());
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}

	public String completeIt ()
	{
		log.info(toString());
		
		// 3039
		// allow document completion without ICTU Workflow
		if (DocActionConstants.STATUS_InProgress.equals(this.getDocStatus()) && 
				MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Drafted.equals(this.getPurchaseCommitmentStatus()))
		{
			// force reload
			this.lines = null;
			
			for (Iterator<MAFGOPurchaseCommitmentLine> lines = this.getLines().iterator(); lines.hasNext();)
			{
				MAFGOPurchaseCommitmentLine line = lines.next();
				line.setQty(line.getQtyEntered());
				line.setPrice(line.getPriceEntered());
				line.save();
			}
			
			// force reload
			this.lines = null;
			
			this.setPurchaseCommitmentStatus(MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Formalized);
		}
		
		else if (!MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Formalized.equals(this.getPurchaseCommitmentStatus()))
		{
			this.processMsg = "@NotFormalized@";
			return DOCSTATUS_Invalid;
		}
		
		if (!(this.getAbsoluteTotal().compareTo(Env.ZERO) > 0))
		{
			this.processMsg = "@NoGrandTotal@";
			return DOCSTATUS_Invalid;
		}
		
		if (this.getMasterPurchaseCommitment().isClosed())
		{
			this.processMsg = "@MasterCommitmentClosed@";
			return DOCSTATUS_Invalid;
		}
				
		this.processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.DOCTIMING_AFTER_COMPLETE);
		if (this.processMsg != null)
			return DOCSTATUS_Invalid;
		
		this.setProcessed(true);
		//this.save(this.get_TrxName());		
		this.setDocAction(DOCACTION_Close);
		return DOCSTATUS_Completed;
	}
	
	public boolean closeIt ()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}
	
	public boolean reActivateIt ()
	{
		log.info(toString());
		return false;
	}

	public boolean voidIt ()
	{
		log.info(toString());
		
		if (DOCSTATUS_Completed.equals(this.getDocStatus()) || DOCSTATUS_Closed.equals(this.getDocStatus()))
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		
		return true;
	}
	
	public boolean invalidateIt()
	{
		log.info(toString());
		// 2980 do not allow quote selection when INVALID
		this.setPurchaseCommitmentStatus(MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Failed);
		this.setDocAction(DOCACTION_Prepare);
		return true;
	}
	
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}
	

	public boolean reverseCorrectIt()
	{
		log.info(toString());
		return false;
	}

	public boolean unlockIt ()
	{
		this.log.info("unlockIt: " + this.toString());
		this.setProcessing(false);
		return true;
	}

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}

		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		DocumentPrintHelper ph = new DocumentPrintHelper(this);
		ReportEngine re = ph.getReportEngine();
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

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
		MDocType dt = MDocType.get(this.getCtx(), this.getC_DocType_ID());
		return dt.getName() + " " + this.getDocumentNo();
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
