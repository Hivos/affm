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
/** Generated Model for AFGO_QuotationResponse
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_QuotationResponse.java,v 1.1 2009/02/17 12:50:22 tomassen Exp $ */
public class X_AFGO_QuotationResponse extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_QuotationResponse_ID id
    @param trxName transaction
    */
    public X_AFGO_QuotationResponse (Ctx ctx, int AFGO_QuotationResponse_ID, String trxName)
    {
        super (ctx, AFGO_QuotationResponse_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_QuotationResponse_ID == 0)
        {
            setAFGO_QuotationResponse_ID (0);
            setC_BPartner_ID (0);
            setDateDoc (new Timestamp(System.currentTimeMillis()));	// Sysdate
            setDocAction (null);	// CO
            setDocumentNo (null);
            setGrandTotal (Env.ZERO);	// 0
            setProcessed (false);	// N
            setProcessing (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_QuotationResponse (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515443044789L;
    /** Last Updated Timestamp 2009-01-30 13:15:28.0 */
    public static final long updatedMS = 1233317728000L;
    /** AD_Table_ID=1000025 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_QuotationResponse");
        
    }
    ;
    
    /** TableName=AFGO_QuotationResponse */
    public static final String Table_Name="AFGO_QuotationResponse";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_QuotationResponse");
    
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
    
    /** AD_User_ID AD_Reference_ID=110 */
    public static final int AD_USER_ID_AD_Reference_ID=110;
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    
    /** AFGO_PurchaseCommitment_ID AD_Reference_ID=1000010 */
    public static final int AFGO_PURCHASECOMMITMENT_ID_AD_Reference_ID=1000010;
    /** Set Purchase Commitment.
    @param AFGO_PurchaseCommitment_ID Document to process purchase requirement up to formalized commitment. */
    public void setAFGO_PurchaseCommitment_ID (int AFGO_PurchaseCommitment_ID)
    {
        if (AFGO_PurchaseCommitment_ID <= 0) set_ValueNoCheck ("AFGO_PurchaseCommitment_ID", null);
        else
        set_ValueNoCheck ("AFGO_PurchaseCommitment_ID", Integer.valueOf(AFGO_PurchaseCommitment_ID));
        
    }
    
    /** Get Purchase Commitment.
    @return Document to process purchase requirement up to formalized commitment. */
    public int getAFGO_PurchaseCommitment_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseCommitment_ID");
        
    }
    
    
    /** AFGO_PurchaseDomain_ID AD_Reference_ID=1000013 */
    public static final int AFGO_PURCHASEDOMAIN_ID_AD_Reference_ID=1000013;
    /** Set Purchase Domain.
    @param AFGO_PurchaseDomain_ID Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public void setAFGO_PurchaseDomain_ID (int AFGO_PurchaseDomain_ID)
    {
        if (AFGO_PurchaseDomain_ID <= 0) set_Value ("AFGO_PurchaseDomain_ID", null);
        else
        set_Value ("AFGO_PurchaseDomain_ID", Integer.valueOf(AFGO_PurchaseDomain_ID));
        
    }
    
    /** Get Purchase Domain.
    @return Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public int getAFGO_PurchaseDomain_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseDomain_ID");
        
    }
    
    
    /** AFGO_PurchaseLot_ID AD_Reference_ID=1000014 */
    public static final int AFGO_PURCHASELOT_ID_AD_Reference_ID=1000014;
    /** Set Purchase Lot.
    @param AFGO_PurchaseLot_ID A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public void setAFGO_PurchaseLot_ID (int AFGO_PurchaseLot_ID)
    {
        if (AFGO_PurchaseLot_ID <= 0) set_Value ("AFGO_PurchaseLot_ID", null);
        else
        set_Value ("AFGO_PurchaseLot_ID", Integer.valueOf(AFGO_PurchaseLot_ID));
        
    }
    
    /** Get Purchase Lot.
    @return A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public int getAFGO_PurchaseLot_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseLot_ID");
        
    }
    
    
    /** AFGO_QuotationRequest_ID AD_Reference_ID=1000015 */
    public static final int AFGO_QUOTATIONREQUEST_ID_AD_Reference_ID=1000015;
    /** Set Quotation Request.
    @param AFGO_QuotationRequest_ID You can define a quotation request to manage the selection procedure for the qualified suppliers.  */
    public void setAFGO_QuotationRequest_ID (int AFGO_QuotationRequest_ID)
    {
        if (AFGO_QuotationRequest_ID <= 0) set_ValueNoCheck ("AFGO_QuotationRequest_ID", null);
        else
        set_ValueNoCheck ("AFGO_QuotationRequest_ID", Integer.valueOf(AFGO_QuotationRequest_ID));
        
    }
    
    /** Get Quotation Request.
    @return You can define a quotation request to manage the selection procedure for the qualified suppliers.  */
    public int getAFGO_QuotationRequest_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationRequest_ID");
        
    }
    
    /** Set Quotation Response.
    @param AFGO_QuotationResponse_ID All responses will be entered into the system. You select the right quotation request or purchase commitment and enter the business partner you received the response from. */
    public void setAFGO_QuotationResponse_ID (int AFGO_QuotationResponse_ID)
    {
        if (AFGO_QuotationResponse_ID < 1) throw new IllegalArgumentException ("AFGO_QuotationResponse_ID is mandatory.");
        set_ValueNoCheck ("AFGO_QuotationResponse_ID", Integer.valueOf(AFGO_QuotationResponse_ID));
        
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
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Copy Lines.
    @param CopyLines Copy Lines */
    public void setCopyLines (String CopyLines)
    {
        set_Value ("CopyLines", CopyLines);
        
    }
    
    /** Get Copy Lines.
    @return Copy Lines */
    public String getCopyLines() 
    {
        return (String)get_Value("CopyLines");
        
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
        if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
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
        if (!isDocStatusValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("DocStatus", DocStatus);
        
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
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_ValueNoCheck ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Grand Total.
    @param GrandTotal Total amount of document */
    public void setGrandTotal (java.math.BigDecimal GrandTotal)
    {
        if (GrandTotal == null) throw new IllegalArgumentException ("GrandTotal is mandatory.");
        set_Value ("GrandTotal", GrandTotal);
        
    }
    
    /** Get Grand Total.
    @return Total amount of document */
    public java.math.BigDecimal getGrandTotal() 
    {
        return get_ValueAsBigDecimal("GrandTotal");
        
    }
    
    /** Set Mantel.
    @param IsMantel Is mantel identifies a quote response as valid for the appropriate quote request. */
    public void setIsMantel (boolean IsMantel)
    {
        set_Value ("IsMantel", Boolean.valueOf(IsMantel));
        
    }
    
    /** Get Mantel.
    @return Is mantel identifies a quote response as valid for the appropriate quote request. */
    public boolean isMantel() 
    {
        return get_ValueAsBoolean("IsMantel");
        
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
    
    
    /** ProgramPeriodType AD_Reference_ID=1000019 */
    public static final int PROGRAMPERIODTYPE_AD_Reference_ID=1000019;
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
        throw new IllegalArgumentException ("ProgramPeriodType Invalid value - " + ProgramPeriodType + " - Reference_ID=1000019 - M - Q");
        throw new IllegalArgumentException ("ProgramPeriodType is virtual column");
        
    }
    
    /** Get Program Period Type.
    @return Choose either monthly or quarterly to set the standard invoicing and reporting period type */
    public String getProgramPeriodType() 
    {
        return (String)get_Value("ProgramPeriodType");
        
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
    
    
}
