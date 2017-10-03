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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AllocatableInvoice.java,v 1.17.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class AllocatableInvoice implements AllocatableDocument
{
	
	public AllocatableInvoice(MInvoice invoice)
	{
		if (invoice == null)
			throw new IllegalStateException("No Invoice");
		
		this.invoice = invoice;
	}
	
	private MInvoice invoice = null;
	
	private ArrayList<AllocatableInvoiceLine> lines = null;
	
	private MAFGOPurchaseCommitment purchaseCommitment = null;
	
	private MBPartner bpartner = null;
	
	private MDocType docType = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public PO getPO ()
	{
		return this.invoice;
	}
	
	public int get_ID ()
	{
		return this.invoice.get_ID();
	}

	public int get_Table_ID ()
	{
		return this.invoice.get_Table_ID();
	}

	public String get_TrxName ()
	{
		return this.invoice.get_TrxName();
	}
	
	public Ctx getCtx ()
	{
		return this.invoice.getCtx();
	}
	
	public int getAD_Client_ID ()
	{
		return this.invoice.getAD_Client_ID();
	}

	public int getAD_Org_ID ()
	{
		return this.invoice.getAD_Org_ID();
	}
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null || this.invoice.getC_BPartner_ID() > 0)
			this.bpartner = new MBPartner(this.getCtx(), this.invoice.getC_BPartner_ID(), this.get_TrxName());
		return this.bpartner;
	}
	
	public MDocType getDocType()
	{
		return MDocType.get(this.getCtx(), this.invoice.getC_DocTypeTarget_ID());
	}

	public CLogger get_Logger ()
	{
		return this.invoice.get_Logger();
	}
	
	public void resetWorkflow()
	{
		if (DocActionConstants.STATUS_Completed.equals(this.getDocStatus()))
			return;
		if (DocActionConstants.STATUS_Closed.equals(this.getDocStatus()))
			return;
		
		this.invoice.setIsDeliveryAcknowledged(false);
		
		this.invoice.setIsBudgetOwnerApproved(false);
		
		this.setDocStatus(DocActionConstants.STATUS_Drafted);
		
		this.unlockIt();
		
		log.warning("DocStatus=" + this.invoice.getDocStatus() + ", DocAction=" + this.invoice.getDocAction() + ", C_DocType_ID=" + this.invoice.getC_DocType_ID());
	}
	
	public void routeWorkflowActivity(MWFActivity activity) throws CompiereUserException, CompiereSystemException, WorkflowRoleAssignmentException
	{
		/*
		 * Special routing:
		 * For invoices allocated to purchase commitments, the creator
		 * of the purchase commitment needs to acknowledge the delivery
		 * 
		 * 10002379
		 * This is no longer valid
		 * 
		if (MWFNode.ACTION_UserChoice.equals(activity.getNode().getAction()) && activity.getNode().getColumn() != null)
		{
			if ("IsDeliveryAcknowledged".equals(activity.getNode().getColumn().getColumnName()) && this.getPurchaseCommitment() != null)
			{
				activity.setAD_User_ID(this.getPurchaseCommitment().getDoc_User_ID());
				return;
			}
		}
		*/
		
		MAFGOWorkflowRoleAssignment.routeWorkflowActivity(activity, this);
	}
	
	public boolean save ()
	{
		return this.invoice.save();
	}
	
	public MAFGOPurchaseCommitment getPurchaseCommitment()
	{
		if (this.purchaseCommitment == null && this.invoice.getAFGO_PurchaseCommitment_ID() > 0)
			this.purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.invoice.getAFGO_PurchaseCommitment_ID(), this.get_TrxName());
		return this.purchaseCommitment;
	}
	
	public ArrayList<AllocatableInvoiceLine> getLines ()
	{
		if (this.lines == null)
		{
			this.lines = new ArrayList<AllocatableInvoiceLine>();
			MInvoiceLine[] invoiceLines = this.invoice.getLines();
			for (int i = 0; i < invoiceLines.length; i++)
				this.lines.add(new AllocatableInvoiceLine(this, invoiceLines[i]));
		}
		return this.lines;
	}

	public MAFGOProgram getProgram ()
	{
		return MAFGOProgram.getProgram(this.getCtx(), this.invoice.getAFGO_Program_ID());
	}
	
	public MAFGOProgram getWorkflowProgram(MWFActivity activity)
	{
		return this.getProgram();
	}
	
	public ArrayList<MAFGOMonth> getMonths ()
	{
		return null;
	}
	
	public BigDecimal getControlAmt()
	{
		BigDecimal controlAmt = (BigDecimal)invoice.get_Value("ControlAmt");
		
		if (controlAmt == null)
		{
			return Env.ZERO;
		}
		
		return controlAmt;
	}
	
	
	public int getAFGO_FundSchedule_ID()
	{
		Integer AFGO_PurchaseCommitment_ID = (Integer)invoice.get_Value("AFGO_FundSchedule_ID");
		
		if (AFGO_PurchaseCommitment_ID == null)
		{
			return 0;
		}
		
		return AFGO_PurchaseCommitment_ID;
	}
	
	public int getAFGO_PurchaseCommitment_ID()
	{
		Integer AFGO_PurchaseCommitment_ID = (Integer)invoice.get_Value("AFGO_PurchaseCommitment_ID");
		
		if (AFGO_PurchaseCommitment_ID == null)
		{
			return 0;
		}
		
		return AFGO_PurchaseCommitment_ID;
	}
	
	public boolean isReversal()
	{
		boolean result = false;
		
		String description = this.invoice.getDescription();
		if (description == null)
			description = "";
		
		Matcher matcher = docNoPattern.matcher(description);
		
		while(matcher.find())
		{
			result = true;
			break;
		}
		
		return result;
	}
	
	public String beforePrepare()
	{
		String msg = null;
		
		if (!invoice.isSOTrx())
		{	
			// verify control amount
			if (this.invoice.get_ColumnIndex("ControlAmt") > -1)
			{
    			if (this.isReversal())
    			{
    				this.invoice.set_ValueNoCheck("ControlAmt", Env.ZERO);
    			}
    			else if (this.invoice.getGrandTotal().compareTo(this.getControlAmt()) != 0)
    			{
    				return Msg.getMsg(getCtx(), "ControlAmtMismatch");
    			}
			}
			
			// check for open commitment
			if (this.getPurchaseCommitment() != null && this.getPurchaseCommitment().getMasterPurchaseCommitment() != null)
			{
				if (this.getPurchaseCommitment().getMasterPurchaseCommitment().isClosed())
				{
					return Msg.getMsg(getCtx(), "MasterCommitmentClosed");
				}
			}

			
			// reverse line allocation
			if (this.isReversal())
			{
				String documentNo = "";
				Matcher matcher = docNoPattern.matcher(this.invoice.getDescription());
				if (matcher.find())
					documentNo = matcher.group();
				
				if (documentNo.startsWith("(->") || documentNo.startsWith("{->"))
					documentNo = documentNo.substring(3);
	
				if (documentNo.endsWith(")"))
					documentNo = documentNo.substring(0, documentNo.length() - 1);
				
				int OriginalInvoiceLine_ID = -1;
				int ReversalInvoiceLine_ID = -1;
				
				
				String sql = "SELECT ol.C_InvoiceLine_ID AS OriginalLine_ID, rl.C_InvoiceLine_ID AS ReversalLine_ID, oa.AFGO_InvoiceLineAllocation_ID"
					+ " FROM C_Invoice r, C_InvoiceLine rl, C_Invoice o"
					+ " INNER JOIN C_InvoiceLine ol ON (ol.C_Invoice_ID=o.C_Invoice_ID)"
					+ " LEFT OUTER JOIN AFGO_InvoiceLineAllocation oa ON (oa.C_InvoiceLine_ID=ol.C_InvoiceLine_ID)"
					+ " WHERE r.C_Invoice_ID=?"
					+ " AND r.AD_Client_ID=o.AD_Client_ID"
					+ " AND r.AD_Org_ID=o.AD_Org_ID"
					+ " AND r.C_BPartner_ID=o.C_BPartner_ID"
					+ " AND r.GrandTotal + o.GrandTotal = 0"
					+ " AND r.TotalLines + o.TotalLines = 0"
					+ " AND o.DocumentNo=?"
					+ " AND rl.C_Invoice_ID=r.C_Invoice_ID"
					+ " AND rl.Line=ol.Line"
					+ " AND ((rl.M_Product_ID IS NULL AND ol.M_Product_ID IS NULL) OR rl.M_Product_ID=ol.M_Product_ID)"
					+ " AND ((rl.C_Charge_ID IS NULL AND ol.C_Charge_ID IS NULL) OR rl.C_Charge_ID=ol.C_Charge_ID)"
					+ " AND rl.LineNetAmt + ol.LineNetAmt = 0"
					+ " AND rl.AFGO_Quarter_ID=ol.AFGO_Quarter_ID"
					+ " AND rl.AFGO_Month_ID=ol.AFGO_Month_ID"
					+ " AND rl.AFGO_ProjectCluster_ID=ol.AFGO_ProjectCluster_ID"
					+ " AND rl.AFGO_Project_ID=ol.AFGO_Project_ID"
					+ " AND rl.AFGO_Phase_ID=ol.AFGO_Phase_ID"
					+ " AND rl.AFGO_Activity_ID=ol.AFGO_Activity_ID"
					+ " AND rl.AFGO_ServiceType_ID=ol.AFGO_ServiceType_ID";
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try
				{
					pstmt = DB.prepareStatement(sql, this.get_TrxName());
					pstmt.setInt(1, this.invoice.getC_Invoice_ID());
					pstmt.setString(2, documentNo);
					
					rs = pstmt.executeQuery();
					
					while (rs.next())
					{
						OriginalInvoiceLine_ID = rs.getInt(1);
						ReversalInvoiceLine_ID = rs.getInt(2);
						
						MAFGOInvoiceLineAllocation originalAllocation = new MAFGOInvoiceLineAllocation(this.getCtx(), rs.getInt(3), this.get_TrxName());
						
						MAFGOInvoiceLineAllocation reversalAllocation = new MAFGOInvoiceLineAllocation(originalAllocation, ReversalInvoiceLine_ID, true);
					
						if (!reversalAllocation.save())
						{
							msg = "ReverseLineAllocationError";
							log.warning("Cannot save Invoice Line Allocation: OriginalInvoiceLine_ID=" + OriginalInvoiceLine_ID + ", ReversalInvoiceLine_ID=" + ReversalInvoiceLine_ID);
							break;
						}
						
						log.fine(reversalAllocation.toString());
					}
					
					rs.close();
					rs = null;
					pstmt.close();
					pstmt = null;
				}
				catch(Exception e)
				{
					log.warning(e.toString());
					e.printStackTrace();
				}
				finally
				{
					if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
					if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
				}
					
			}
		}
		
		return msg;
	}
	
	public String afterComplete()
	{
		if (invoice.isSOTrx())
		{
			// recalculate AFGO_FundScheduleLine.InvoiceAmt
			Integer AFGO_FundSchedule_ID = this.getAFGO_FundSchedule_ID();
			
			if (AFGO_FundSchedule_ID != null && AFGO_FundSchedule_ID > 0)
			{
				invoice.save();
				//System.err.println("Processed=" + DB.getSQLValueString(invoice.get_TrxName(), "SELECT Processed FROM C_Invoice WHERE C_Invoice_ID=?",  po.get_ID()));
				MAFGOFundSchedule fundSchedule = new MAFGOFundSchedule(getCtx(), AFGO_FundSchedule_ID, get_TrxName());
				if (!fundSchedule.updateInvoicedAmount(invoice.getC_Invoice_ID()))
					return Msg.getMsg(getCtx(), "CannotUpdateFundScheduleInvoicedAmount");
			}
		}
		else
		{
			// recalculate AFGO_PurchaseCommitmentLine.InvoicedAmt
			Integer AFGO_PurchaseCommitment_ID = this.getAFGO_PurchaseCommitment_ID();
			
			if (AFGO_PurchaseCommitment_ID != null && AFGO_PurchaseCommitment_ID.intValue() > 0)
			{
				invoice.save();
				MInvoiceLine[] invoiceLines = invoice.getLines();
				for (int i = 0; i < invoiceLines.length; i++)
				{
					MInvoiceLine invoiceLine = invoiceLines[i];
					
					Integer AFGO_PurchaseCommitmentLine_ID = (Integer)invoiceLine.get_Value("AFGO_PurchaseCommitmentLine_ID");
					
					if (AFGO_PurchaseCommitmentLine_ID != null && AFGO_PurchaseCommitmentLine_ID.intValue() > 0)
					{
						MAFGOPurchaseCommitmentLine line = new MAFGOPurchaseCommitmentLine(invoice.getCtx(), AFGO_PurchaseCommitmentLine_ID, invoice.get_TrxName());
						if (!line.updateInvoicedAmount(invoice.getC_Invoice_ID()))
							return Msg.getMsg(line.getCtx(), "CannotUpdatePurchaseCommitmentInvoicedAmount");
						line.save();
					}
				}
			}
		}
		
		return null;
	}
	
	// wrapper methods

	public boolean approveIt ()
	{
		return this.invoice.approveIt();
	}

	public boolean closeIt ()
	{
		return this.invoice.closeIt();
	}

	public String completeIt ()
	{
		return this.invoice.completeIt();
	}

	public File createPDF ()
	{
		return this.invoice.createPDF();
	}


	public BigDecimal getApprovalAmt ()
	{
		return this.invoice.getApprovalAmt();
	}

	public int getC_Currency_ID ()
	{
		return this.invoice.getC_Currency_ID();
	}

	public String getDocAction ()
	{
		return this.invoice.getDocAction();
	}

	public String getDocStatus ()
	{
		return this.invoice.getDocStatus();
	}

	public int getDoc_User_ID ()
	{
		return this.invoice.getDoc_User_ID();
	}

	public String getDocumentInfo ()
	{
		return this.invoice.getDocumentInfo();
	}

	public String getDocumentNo ()
	{
		return this.invoice.getDocumentNo();
	}

	public String getProcessMsg ()
	{
		return this.invoice.getProcessMsg();
	}

	public String getSummary ()
	{
		return this.invoice.getSummary();
	}

	public boolean invalidateIt ()
	{
		return this.invoice.invalidateIt();
	}

	public String prepareIt ()
	{
		return this.invoice.prepareIt();
	}

	public boolean processIt (String processAction) throws Exception
	{
		return this.invoice.processIt(processAction);
	}

	public boolean reActivateIt ()
	{
		return this.invoice.reActivateIt();
	}

	public boolean rejectIt ()
	{
		return this.invoice.rejectIt();
	}

	public boolean reverseAccrualIt ()
	{
		return this.invoice.reverseAccrualIt();
	}

	public boolean reverseCorrectIt ()
	{
		return this.invoice.reverseCorrectIt();
	}

	public void setDocStatus (String DocStatus)
	{
		this.invoice.setDocStatus(DocStatus);
	}

	public boolean unlockIt ()
	{
		return this.invoice.unlockIt();
	}

	public boolean voidIt ()
	{
		return this.invoice.voidIt();
	}
	
	//
	
	public static Pattern docNoPattern = Pattern.compile("[\\{\\(].+\\)");

}
