/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AFGO_PurchaseCommitment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_PurchaseCommitment.java,v 1.3 2009/11/24 14:17:55 tomassen Exp $ */
public class X_AFGO_PurchaseCommitment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_PurchaseCommitment_ID id
    @param trxName transaction
    */
    public X_AFGO_PurchaseCommitment (Ctx ctx, int AFGO_PurchaseCommitment_ID, String trxName)
    {
        super (ctx, AFGO_PurchaseCommitment_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_PurchaseCommitment_ID == 0)
        {
            setAFGO_Program_ID (0);
            setAFGO_PurchaseCommitment_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_DocType_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateDoc (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateFrom (new Timestamp(System.currentTimeMillis()));
            setDateTo (new Timestamp(System.currentTimeMillis()));
            setDocStatus (null);	// DR
            setGrandTotal (Env.ZERO);	// 0
            setIsBudgetMaintainerApproved (false);	// N
            setIsBudgetOwnerApproved (false);	// N
            setIsClosed (false);	// N
            setIsExecutiveApproved (false);	// N
            setIsPurchaseDepartmentApproved (false);	// N
            setIsSpecific (false);	// N
            setPlannedAmt (Env.ZERO);	// 0
            setPosted (false);	// N
            setPurchaseCommitmentStatus (null);	// DR
            setTotalLines (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_PurchaseCommitment (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27539287748789L;
    /** Last Updated Timestamp 2009-11-02 12:47:12.0 */
    public static final long updatedMS = 1257162432000L;
    /** AD_Table_ID=1000010 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_PurchaseCommitment");
        
    }
    ;
    
    /** TableName=AFGO_PurchaseCommitment */
    public static final String Table_Name="AFGO_PurchaseCommitment";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_PurchaseCommitment");
    
    /** AccessLevel
    @return 3 - Client - Org 
    */
    @Override protected PO.AccessLevel get_AccessLevel()
    {
        return PO.AccessLevel.CLIENTORG;
        
    }
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    
    /** AFGO_Program_ID AD_Reference_ID=1000000 */
    public static final int AFGO_PROGRAM_ID_AD_Reference_ID=1000000;
    /** Set Program.
    @param AFGO_Program_ID Program is the highest level of the specified set. You can have multiple programs in one organisation. */
    public void setAFGO_Program_ID (int AFGO_Program_ID)
    {
        if (AFGO_Program_ID < 1) throw new IllegalArgumentException ("AFGO_Program_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Program_ID", Integer.valueOf(AFGO_Program_ID));
        
    }
    
    /** Get Program.
    @return Program is the highest level of the specified set. You can have multiple programs in one organisation. */
    public int getAFGO_Program_ID() 
    {
        return get_ValueAsInt("AFGO_Program_ID");
        
    }
    
    
    /** AFGO_PurchaseCommitmentType_ID AD_Reference_ID=1000010 */
    public static final int AFGO_PURCHASECOMMITMENTTYPE_ID_AD_Reference_ID=1000010;
    /** Set Purchase Commitment Type.
    @param AFGO_PurchaseCommitmentType_ID Purchase Commitment Type */
    public void setAFGO_PurchaseCommitmentType_ID (int AFGO_PurchaseCommitmentType_ID)
    {
        throw new IllegalArgumentException ("AFGO_PurchaseCommitmentType_ID is virtual column");
        
    }
    
    /** Get Purchase Commitment Type.
    @return Purchase Commitment Type */
    public int getAFGO_PurchaseCommitmentType_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseCommitmentType_ID");
        
    }
    
    /** Set Purchase Commitment.
    @param AFGO_PurchaseCommitment_ID Document to process purchase requirement up to formalized commitment. */
    public void setAFGO_PurchaseCommitment_ID (int AFGO_PurchaseCommitment_ID)
    {
        if (AFGO_PurchaseCommitment_ID < 1) throw new IllegalArgumentException ("AFGO_PurchaseCommitment_ID is mandatory.");
        set_ValueNoCheck ("AFGO_PurchaseCommitment_ID", Integer.valueOf(AFGO_PurchaseCommitment_ID));
        
    }
    
    /** Get Purchase Commitment.
    @return Document to process purchase requirement up to formalized commitment. */
    public int getAFGO_PurchaseCommitment_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseCommitment_ID");
        
    }
    
    
    /** AFGO_PurchaseDomain_ID AD_Reference_ID=1000012 */
    public static final int AFGO_PURCHASEDOMAIN_ID_AD_Reference_ID=1000012;
    /** Set Purchase Domain.
    @param AFGO_PurchaseDomain_ID Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public void setAFGO_PurchaseDomain_ID (int AFGO_PurchaseDomain_ID)
    {
        if (AFGO_PurchaseDomain_ID <= 0) set_ValueNoCheck ("AFGO_PurchaseDomain_ID", null);
        else
        set_ValueNoCheck ("AFGO_PurchaseDomain_ID", Integer.valueOf(AFGO_PurchaseDomain_ID));
        
    }
    
    /** Get Purchase Domain.
    @return Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public int getAFGO_PurchaseDomain_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseDomain_ID");
        
    }
    
    
    /** AFGO_PurchaseLot_ID AD_Reference_ID=1000013 */
    public static final int AFGO_PURCHASELOT_ID_AD_Reference_ID=1000013;
    /** Set Purchase Lot.
    @param AFGO_PurchaseLot_ID A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public void setAFGO_PurchaseLot_ID (int AFGO_PurchaseLot_ID)
    {
        if (AFGO_PurchaseLot_ID <= 0) set_ValueNoCheck ("AFGO_PurchaseLot_ID", null);
        else
        set_ValueNoCheck ("AFGO_PurchaseLot_ID", Integer.valueOf(AFGO_PurchaseLot_ID));
        
    }
    
    /** Get Purchase Lot.
    @return A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public int getAFGO_PurchaseLot_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseLot_ID");
        
    }
    
    
    /** AFGO_QuotationResponse_ID AD_Reference_ID=1000016 */
    public static final int AFGO_QUOTATIONRESPONSE_ID_AD_Reference_ID=1000016;
    /** Set Quotation Response.
    @param AFGO_QuotationResponse_ID All responses will be entered into the system. You select the right quotation request or purchase commitment and enter the business partner you received the response from. */
    public void setAFGO_QuotationResponse_ID (int AFGO_QuotationResponse_ID)
    {
        if (AFGO_QuotationResponse_ID <= 0) set_Value ("AFGO_QuotationResponse_ID", null);
        else
        set_Value ("AFGO_QuotationResponse_ID", Integer.valueOf(AFGO_QuotationResponse_ID));
        
    }
    
    /** Get Quotation Response.
    @return All responses will be entered into the system. You select the right quotation request or purchase commitment and enter the business partner you received the response from. */
    public int getAFGO_QuotationResponse_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationResponse_ID");
        
    }
    
    
    /** C_BPartner_ID AD_Reference_ID=138 */
    public static final int C_BPARTNER_ID_AD_Reference_ID=138;
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    
    /** C_DocType_ID AD_Reference_ID=1000040 */
    public static final int C_DOCTYPE_ID_AD_Reference_ID=1000040;
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_ValueNoCheck ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Close Date.
    @param CloseDate Close Date */
    public void setCloseDate (Timestamp CloseDate)
    {
        set_Value ("CloseDate", CloseDate);
        
    }
    
    /** Get Close Date.
    @return Close Date */
    public Timestamp getCloseDate() 
    {
        return (Timestamp)get_Value("CloseDate");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory.");
        set_Value ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
    }
    
    /** Set Document Date.
    @param DateDoc Date of the Document */
    public void setDateDoc (Timestamp DateDoc)
    {
        if (DateDoc == null) throw new IllegalArgumentException ("DateDoc is mandatory.");
        set_Value ("DateDoc", DateDoc);
        
    }
    
    /** Get Document Date.
    @return Date of the Document */
    public Timestamp getDateDoc() 
    {
        return (Timestamp)get_Value("DateDoc");
        
    }
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        if (DateFrom == null) throw new IllegalArgumentException ("DateFrom is mandatory.");
        set_ValueNoCheck ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        if (DateTo == null) throw new IllegalArgumentException ("DateTo is mandatory.");
        set_ValueNoCheck ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    
    /** DocAction AD_Reference_ID=135 */
    public static final int DOCACTION_AD_Reference_ID=135;
    /** <None> = -- */
    public static final String DOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String DOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String DOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String DOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String DOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String DOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String DOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String DOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String DOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String DOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String DOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String DOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String DOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String DOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isDocActionValid(String test)
    {
         return X_Ref__Document_Action.isValid(test);
         
    }
    /** Set Document Action.
    @param DocAction The targeted status of the document */
    public void setDocAction (String DocAction)
    {
        if (!isDocActionValid(DocAction))
        throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("DocAction", DocAction);
        
    }
    
    /** Get Document Action.
    @return The targeted status of the document */
    public String getDocAction() 
    {
        return (String)get_Value("DocAction");
        
    }
    
    
    /** DocStatus AD_Reference_ID=131 */
    public static final int DOCSTATUS_AD_Reference_ID=131;
    /** Unknown = ?? */
    public static final String DOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String DOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String DOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String DOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String DOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String DOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String DOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String DOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String DOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String DOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String DOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String DOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isDocStatusValid(String test)
    {
         return X_Ref__Document_Status.isValid(test);
         
    }
    /** Set Document Status.
    @param DocStatus The current status of the document */
    public void setDocStatus (String DocStatus)
    {
        if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
        if (!isDocStatusValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_ValueNoCheck ("DocStatus", DocStatus);
        
    }
    
    /** Get Document Status.
    @return The current status of the document */
    public String getDocStatus() 
    {
        return (String)get_Value("DocStatus");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_ValueNoCheck ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Set Document Note.
    @param DocumentNote Additional information for a Document */
    public void setDocumentNote (String DocumentNote)
    {
        set_Value ("DocumentNote", DocumentNote);
        
    }
    
    /** Get Document Note.
    @return Additional information for a Document */
    public String getDocumentNote() 
    {
        return (String)get_Value("DocumentNote");
        
    }
    
    /** Set Grand Total.
    @param GrandTotal Total amount of document */
    public void setGrandTotal (java.math.BigDecimal GrandTotal)
    {
        if (GrandTotal == null) throw new IllegalArgumentException ("GrandTotal is mandatory.");
        set_ValueNoCheck ("GrandTotal", GrandTotal);
        
    }
    
    /** Get Grand Total.
    @return Total amount of document */
    public java.math.BigDecimal getGrandTotal() 
    {
        return get_ValueAsBigDecimal("GrandTotal");
        
    }
    
    /** Set Invoiced Amount.
    @param InvoicedAmt The amount invoiced */
    public void setInvoicedAmt (java.math.BigDecimal InvoicedAmt)
    {
        set_ValueNoCheck ("InvoicedAmt", InvoicedAmt);
        
    }
    
    /** Get Invoiced Amount.
    @return The amount invoiced */
    public java.math.BigDecimal getInvoicedAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedAmt");
        
    }
    
    /** Set Additional Commitment.
    @param IsAdditionalCommitment Additional Commitment */
    public void setIsAdditionalCommitment (boolean IsAdditionalCommitment)
    {
        set_ValueNoCheck ("IsAdditionalCommitment", Boolean.valueOf(IsAdditionalCommitment));
        
    }
    
    /** Get Additional Commitment.
    @return Additional Commitment */
    public boolean isAdditionalCommitment() 
    {
        return get_ValueAsBoolean("IsAdditionalCommitment");
        
    }
    
    /** Set Approved by Budget Maintainer.
    @param IsBudgetMaintainerApproved Approved by Budget Maintainer */
    public void setIsBudgetMaintainerApproved (boolean IsBudgetMaintainerApproved)
    {
        set_Value ("IsBudgetMaintainerApproved", Boolean.valueOf(IsBudgetMaintainerApproved));
        
    }
    
    /** Get Approved by Budget Maintainer.
    @return Approved by Budget Maintainer */
    public boolean isBudgetMaintainerApproved() 
    {
        return get_ValueAsBoolean("IsBudgetMaintainerApproved");
        
    }
    
    /** Set Approved by Budget Owner.
    @param IsBudgetOwnerApproved Approved by Budget Owner */
    public void setIsBudgetOwnerApproved (boolean IsBudgetOwnerApproved)
    {
        set_Value ("IsBudgetOwnerApproved", Boolean.valueOf(IsBudgetOwnerApproved));
        
    }
    
    /** Get Approved by Budget Owner.
    @return Approved by Budget Owner */
    public boolean isBudgetOwnerApproved() 
    {
        return get_ValueAsBoolean("IsBudgetOwnerApproved");
        
    }
    
    /** Set Can Have Additional.
    @param IsCanHaveAdditional Can Have Additional */
    public void setIsCanHaveAdditional (boolean IsCanHaveAdditional)
    {
        set_ValueNoCheck ("IsCanHaveAdditional", Boolean.valueOf(IsCanHaveAdditional));
        
    }
    
    /** Get Can Have Additional.
    @return Can Have Additional */
    public boolean isCanHaveAdditional() 
    {
        return get_ValueAsBoolean("IsCanHaveAdditional");
        
    }
    
    /** Set Can Transfer.
    @param IsCanTransfer Can Transfer */
    public void setIsCanTransfer (boolean IsCanTransfer)
    {
        set_ValueNoCheck ("IsCanTransfer", Boolean.valueOf(IsCanTransfer));
        
    }
    
    /** Get Can Transfer.
    @return Can Transfer */
    public boolean isCanTransfer() 
    {
        return get_ValueAsBoolean("IsCanTransfer");
        
    }
    
    /** Set Closed Status.
    @param IsClosed The status is closed */
    public void setIsClosed (boolean IsClosed)
    {
        set_Value ("IsClosed", Boolean.valueOf(IsClosed));
        
    }
    
    /** Get Closed Status.
    @return The status is closed */
    public boolean isClosed() 
    {
        return get_ValueAsBoolean("IsClosed");
        
    }
    
    /** Set Approved by Executive.
    @param IsExecutiveApproved Approved by Executive */
    public void setIsExecutiveApproved (boolean IsExecutiveApproved)
    {
        set_Value ("IsExecutiveApproved", Boolean.valueOf(IsExecutiveApproved));
        
    }
    
    /** Get Approved by Executive.
    @return Approved by Executive */
    public boolean isExecutiveApproved() 
    {
        return get_ValueAsBoolean("IsExecutiveApproved");
        
    }
    
    /** Set Approved by Human Resources.
    @param IsHumanResourcesApproved Approved by Human Resources */
    public void setIsHumanResourcesApproved (boolean IsHumanResourcesApproved)
    {
        set_Value ("IsHumanResourcesApproved", Boolean.valueOf(IsHumanResourcesApproved));
        
    }
    
    /** Get Approved by Human Resources.
    @return Approved by Human Resources */
    public boolean isHumanResourcesApproved() 
    {
        return get_ValueAsBoolean("IsHumanResourcesApproved");
        
    }
    
    /** Set Mantel.
    @param IsMantel Is mantel identifies a quote response as valid for the appropriate quote request. */
    public void setIsMantel (boolean IsMantel)
    {
        throw new IllegalArgumentException ("IsMantel is virtual column");
        
    }
    
    /** Get Mantel.
    @return Is mantel identifies a quote response as valid for the appropriate quote request. */
    public boolean isMantel() 
    {
        return get_ValueAsBoolean("IsMantel");
        
    }
    
    /** Set Master Commitment.
    @param IsMasterCommitment Master Commitment */
    public void setIsMasterCommitment (boolean IsMasterCommitment)
    {
        set_ValueNoCheck ("IsMasterCommitment", Boolean.valueOf(IsMasterCommitment));
        
    }
    
    /** Get Master Commitment.
    @return Master Commitment */
    public boolean isMasterCommitment() 
    {
        return get_ValueAsBoolean("IsMasterCommitment");
        
    }
    
    /** Set Approved by Purchase Department.
    @param IsPurchaseDepartmentApproved Approved by Purchase Department */
    public void setIsPurchaseDepartmentApproved (boolean IsPurchaseDepartmentApproved)
    {
        set_Value ("IsPurchaseDepartmentApproved", Boolean.valueOf(IsPurchaseDepartmentApproved));
        
    }
    
    /** Get Approved by Purchase Department.
    @return Approved by Purchase Department */
    public boolean isPurchaseDepartmentApproved() 
    {
        return get_ValueAsBoolean("IsPurchaseDepartmentApproved");
        
    }
    
    /** Set Purchase Domain.
    @param IsPurchaseDomain Purchase Domain */
    public void setIsPurchaseDomain (boolean IsPurchaseDomain)
    {
        set_ValueNoCheck ("IsPurchaseDomain", Boolean.valueOf(IsPurchaseDomain));
        
    }
    
    /** Get Purchase Domain.
    @return Purchase Domain */
    public boolean isPurchaseDomain() 
    {
        return get_ValueAsBoolean("IsPurchaseDomain");
        
    }
    
    /** Set Quotation Required.
    @param IsQuotationRequired Quotation Required */
    public void setIsQuotationRequired (boolean IsQuotationRequired)
    {
        set_ValueNoCheck ("IsQuotationRequired", Boolean.valueOf(IsQuotationRequired));
        
    }
    
    /** Get Quotation Required.
    @return Quotation Required */
    public boolean isQuotationRequired() 
    {
        return get_ValueAsBoolean("IsQuotationRequired");
        
    }
    
    /** Set Secondment Commitment.
    @param IsSecondmentCommitment Secondment Commitment */
    public void setIsSecondmentCommitment (boolean IsSecondmentCommitment)
    {
        set_ValueNoCheck ("IsSecondmentCommitment", Boolean.valueOf(IsSecondmentCommitment));
        
    }
    
    /** Get Secondment Commitment.
    @return Secondment Commitment */
    public boolean isSecondmentCommitment() 
    {
        return get_ValueAsBoolean("IsSecondmentCommitment");
        
    }
    
    /** Set Specific.
    @param IsSpecific Is specific identities a purchase commitment as a specific type of commitment. */
    public void setIsSpecific (boolean IsSpecific)
    {
        set_Value ("IsSpecific", Boolean.valueOf(IsSpecific));
        
    }
    
    /** Get Specific.
    @return Is specific identities a purchase commitment as a specific type of commitment. */
    public boolean isSpecific() 
    {
        return get_ValueAsBoolean("IsSpecific");
        
    }
    
    /** Set Transfer Commitment.
    @param IsTransferCommitment Transfer Commitment */
    public void setIsTransferCommitment (boolean IsTransferCommitment)
    {
        set_ValueNoCheck ("IsTransferCommitment", Boolean.valueOf(IsTransferCommitment));
        
    }
    
    /** Get Transfer Commitment.
    @return Transfer Commitment */
    public boolean isTransferCommitment() 
    {
        return get_ValueAsBoolean("IsTransferCommitment");
        
    }
    
    
    /** MasterPurchaseCommitment_ID AD_Reference_ID=1000011 */
    public static final int MASTERPURCHASECOMMITMENT_ID_AD_Reference_ID=1000011;
    /** Set Master Commitment.
    @param MasterPurchaseCommitment_ID Master purchase commitment this document is referenced to. */
    public void setMasterPurchaseCommitment_ID (int MasterPurchaseCommitment_ID)
    {
        if (MasterPurchaseCommitment_ID <= 0) set_ValueNoCheck ("MasterPurchaseCommitment_ID", null);
        else
        set_ValueNoCheck ("MasterPurchaseCommitment_ID", Integer.valueOf(MasterPurchaseCommitment_ID));
        
    }
    
    /** Get Master Commitment.
    @return Master purchase commitment this document is referenced to. */
    public int getMasterPurchaseCommitment_ID() 
    {
        return get_ValueAsInt("MasterPurchaseCommitment_ID");
        
    }
    
    /** Set Planned Amount.
    @param PlannedAmt Planned amount for this project */
    public void setPlannedAmt (java.math.BigDecimal PlannedAmt)
    {
        if (PlannedAmt == null) throw new IllegalArgumentException ("PlannedAmt is mandatory.");
        set_Value ("PlannedAmt", PlannedAmt);
        
    }
    
    /** Get Planned Amount.
    @return Planned amount for this project */
    public java.math.BigDecimal getPlannedAmt() 
    {
        return get_ValueAsBigDecimal("PlannedAmt");
        
    }
    
    /** Set Posted.
    @param Posted Posting status */
    public void setPosted (boolean Posted)
    {
        set_Value ("Posted", Boolean.valueOf(Posted));
        
    }
    
    /** Get Posted.
    @return Posting status */
    public boolean isPosted() 
    {
        return get_ValueAsBoolean("Posted");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    
    /** ProgramPeriodType AD_Reference_ID=1000018 */
    public static final int PROGRAMPERIODTYPE_AD_Reference_ID=1000018;
    /** Month = M */
    public static final String PROGRAMPERIODTYPE_Month = X_Ref_AFGO_ProgramPeriodType.MONTH.getValue();
    /** Quarter = Q */
    public static final String PROGRAMPERIODTYPE_Quarter = X_Ref_AFGO_ProgramPeriodType.QUARTER.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isProgramPeriodTypeValid(String test)
    {
         return X_Ref_AFGO_ProgramPeriodType.isValid(test);
         
    }
    /** Set Program Period Type.
    @param ProgramPeriodType Choose either monthly or quarterly to set the standard invoicing and reporting period type */
    public void setProgramPeriodType (String ProgramPeriodType)
    {
        if (!isProgramPeriodTypeValid(ProgramPeriodType))
        throw new IllegalArgumentException ("ProgramPeriodType Invalid value - " + ProgramPeriodType + " - Reference_ID=1000018 - M - Q");
        throw new IllegalArgumentException ("ProgramPeriodType is virtual column");
        
    }
    
    /** Get Program Period Type.
    @return Choose either monthly or quarterly to set the standard invoicing and reporting period type */
    public String getProgramPeriodType() 
    {
        return (String)get_Value("ProgramPeriodType");
        
    }
    
    
    /** PurchaseCommitmentStatus AD_Reference_ID=1000017 */
    public static final int PURCHASECOMMITMENTSTATUS_AD_Reference_ID=1000017;
    /** Drafted = DR */
    public static final String PURCHASECOMMITMENTSTATUS_Drafted = X_Ref_AFGO_PurchaseCommitmentStatus.DRAFTED.getValue();
    /** Failed = FL */
    public static final String PURCHASECOMMITMENTSTATUS_Failed = X_Ref_AFGO_PurchaseCommitmentStatus.FAILED.getValue();
    /** Formalized = FR */
    public static final String PURCHASECOMMITMENTSTATUS_Formalized = X_Ref_AFGO_PurchaseCommitmentStatus.FORMALIZED.getValue();
    /** In Progress = IP */
    public static final String PURCHASECOMMITMENTSTATUS_InProgress = X_Ref_AFGO_PurchaseCommitmentStatus.IN_PROGRESS.getValue();
    /** Purchase Request = PR */
    public static final String PURCHASECOMMITMENTSTATUS_PurchaseRequest = X_Ref_AFGO_PurchaseCommitmentStatus.PURCHASE_REQUEST.getValue();
    /** Quote Invitation = QI */
    public static final String PURCHASECOMMITMENTSTATUS_QuoteInvitation = X_Ref_AFGO_PurchaseCommitmentStatus.QUOTE_INVITATION.getValue();
    /** Quote Selection = QS */
    public static final String PURCHASECOMMITMENTSTATUS_QuoteSelection = X_Ref_AFGO_PurchaseCommitmentStatus.QUOTE_SELECTION.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isPurchaseCommitmentStatusValid(String test)
    {
         return X_Ref_AFGO_PurchaseCommitmentStatus.isValid(test);
         
    }
    /** Set Commitment Status.
    @param PurchaseCommitmentStatus The commitment status determines the current options for the document and work flow. */
    public void setPurchaseCommitmentStatus (String PurchaseCommitmentStatus)
    {
        if (PurchaseCommitmentStatus == null) throw new IllegalArgumentException ("PurchaseCommitmentStatus is mandatory");
        if (!isPurchaseCommitmentStatusValid(PurchaseCommitmentStatus))
        throw new IllegalArgumentException ("PurchaseCommitmentStatus Invalid value - " + PurchaseCommitmentStatus + " - Reference_ID=1000017 - DR - FL - FR - IP - PR - QI - QS");
        set_Value ("PurchaseCommitmentStatus", PurchaseCommitmentStatus);
        
    }
    
    /** Get Commitment Status.
    @return The commitment status determines the current options for the document and work flow. */
    public String getPurchaseCommitmentStatus() 
    {
        return (String)get_Value("PurchaseCommitmentStatus");
        
    }
    
    /** Set Reference No.
    @param ReferenceNo Your customer or vendor number at the Business Partner's site */
    public void setReferenceNo (String ReferenceNo)
    {
        set_Value ("ReferenceNo", ReferenceNo);
        
    }
    
    /** Get Reference No.
    @return Your customer or vendor number at the Business Partner's site */
    public String getReferenceNo() 
    {
        return (String)get_Value("ReferenceNo");
        
    }
    
    /** Set Reset Purchase Commitment.
    @param ResetPurchaseCommitment Reset Purchase Commitment */
    public void setResetPurchaseCommitment (String ResetPurchaseCommitment)
    {
        set_Value ("ResetPurchaseCommitment", ResetPurchaseCommitment);
        
    }
    
    /** Get Reset Purchase Commitment.
    @return Reset Purchase Commitment */
    public String getResetPurchaseCommitment() 
    {
        return (String)get_Value("ResetPurchaseCommitment");
        
    }
    
    /** Set Select Quotation.
    @param SelectQuotation Start this process to select the winning quote */
    public void setSelectQuotation (String SelectQuotation)
    {
        set_Value ("SelectQuotation", SelectQuotation);
        
    }
    
    /** Get Select Quotation.
    @return Start this process to select the winning quote */
    public String getSelectQuotation() 
    {
        return (String)get_Value("SelectQuotation");
        
    }
    
    /** Set Specific Statement.
    @param SpecificStatementURL External location for the Specific Statement. */
    public void setSpecificStatementURL (String SpecificStatementURL)
    {
        set_Value ("SpecificStatementURL", SpecificStatementURL);
        
    }
    
    /** Get Specific Statement.
    @return External location for the Specific Statement. */
    public String getSpecificStatementURL() 
    {
        return (String)get_Value("SpecificStatementURL");
        
    }
    
    /** Set SubTotal.
    @param TotalLines Total of all document lines (excluding Tax) */
    public void setTotalLines (java.math.BigDecimal TotalLines)
    {
        if (TotalLines == null) throw new IllegalArgumentException ("TotalLines is mandatory.");
        set_ValueNoCheck ("TotalLines", TotalLines);
        
    }
    
    /** Get SubTotal.
    @return Total of all document lines (excluding Tax) */
    public java.math.BigDecimal getTotalLines() 
    {
        return get_ValueAsBigDecimal("TotalLines");
        
    }
    
    
}
