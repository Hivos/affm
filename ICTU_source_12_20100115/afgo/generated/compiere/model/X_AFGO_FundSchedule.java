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
/** Generated Model for AFGO_FundSchedule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FundSchedule.java,v 1.1 2009/02/17 12:50:21 tomassen Exp $ */
public class X_AFGO_FundSchedule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FundSchedule_ID id
    @param trxName transaction
    */
    public X_AFGO_FundSchedule (Ctx ctx, int AFGO_FundSchedule_ID, String trxName)
    {
        super (ctx, AFGO_FundSchedule_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FundSchedule_ID == 0)
        {
            setAFGO_FundSchedule_ID (0);
            setAFGO_Fund_ID (0);
            setAFGO_Month_ID (0);
            setAFGO_Quarter_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_DocType_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateDoc (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateInvoiced (new Timestamp(System.currentTimeMillis()));
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setGrandTotal (Env.ZERO);	// 0
            setInvoicedAmt (Env.ZERO);	// 0
            setProcessed (false);	// N
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FundSchedule (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515207025789L;
    /** Last Updated Timestamp 2009-01-27 19:41:49.0 */
    public static final long updatedMS = 1233081709000L;
    /** AD_Table_ID=1000030 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FundSchedule");
        
    }
    ;
    
    /** TableName=AFGO_FundSchedule */
    public static final String Table_Name="AFGO_FundSchedule";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FundSchedule");
    
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
    /** Set Fund Schedule.
    @param AFGO_FundSchedule_ID The fund schedule document is the invoice template for a certain program period based on the funding document. */
    public void setAFGO_FundSchedule_ID (int AFGO_FundSchedule_ID)
    {
        if (AFGO_FundSchedule_ID < 1) throw new IllegalArgumentException ("AFGO_FundSchedule_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FundSchedule_ID", Integer.valueOf(AFGO_FundSchedule_ID));
        
    }
    
    /** Get Fund Schedule.
    @return The fund schedule document is the invoice template for a certain program period based on the funding document. */
    public int getAFGO_FundSchedule_ID() 
    {
        return get_ValueAsInt("AFGO_FundSchedule_ID");
        
    }
    
    
    /** AFGO_Fund_ID AD_Reference_ID=1000002 */
    public static final int AFGO_FUND_ID_AD_Reference_ID=1000002;
    /** Set Fund.
    @param AFGO_Fund_ID A Fund document describes the agreement made with a fund provider for a certain period of time within the parameters of a program. */
    public void setAFGO_Fund_ID (int AFGO_Fund_ID)
    {
        if (AFGO_Fund_ID < 1) throw new IllegalArgumentException ("AFGO_Fund_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Fund_ID", Integer.valueOf(AFGO_Fund_ID));
        
    }
    
    /** Get Fund.
    @return A Fund document describes the agreement made with a fund provider for a certain period of time within the parameters of a program. */
    public int getAFGO_Fund_ID() 
    {
        return get_ValueAsInt("AFGO_Fund_ID");
        
    }
    
    
    /** AFGO_Month_ID AD_Reference_ID=1000024 */
    public static final int AFGO_MONTH_ID_AD_Reference_ID=1000024;
    /** Set Month.
    @param AFGO_Month_ID A valid program month within a program quarter. If not applicable choose standard. */
    public void setAFGO_Month_ID (int AFGO_Month_ID)
    {
        if (AFGO_Month_ID < 1) throw new IllegalArgumentException ("AFGO_Month_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Month_ID", Integer.valueOf(AFGO_Month_ID));
        
    }
    
    /** Get Month.
    @return A valid program month within a program quarter. If not applicable choose standard. */
    public int getAFGO_Month_ID() 
    {
        return get_ValueAsInt("AFGO_Month_ID");
        
    }
    
    
    /** AFGO_Program_ID AD_Reference_ID=1000001 */
    public static final int AFGO_PROGRAM_ID_AD_Reference_ID=1000001;
    /** Set Program.
    @param AFGO_Program_ID Program is the highest level of the specified set. You can have multiple programs in one organisation. */
    public void setAFGO_Program_ID (int AFGO_Program_ID)
    {
        throw new IllegalArgumentException ("AFGO_Program_ID is virtual column");
        
    }
    
    /** Get Program.
    @return Program is the highest level of the specified set. You can have multiple programs in one organisation. */
    public int getAFGO_Program_ID() 
    {
        return get_ValueAsInt("AFGO_Program_ID");
        
    }
    
    
    /** AFGO_Quarter_ID AD_Reference_ID=1000022 */
    public static final int AFGO_QUARTER_ID_AD_Reference_ID=1000022;
    /** Set Quarter.
    @param AFGO_Quarter_ID A valid program quarter within a program year. If not applicable choose standard. */
    public void setAFGO_Quarter_ID (int AFGO_Quarter_ID)
    {
        if (AFGO_Quarter_ID < 1) throw new IllegalArgumentException ("AFGO_Quarter_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Quarter_ID", Integer.valueOf(AFGO_Quarter_ID));
        
    }
    
    /** Get Quarter.
    @return A valid program quarter within a program year. If not applicable choose standard. */
    public int getAFGO_Quarter_ID() 
    {
        return get_ValueAsInt("AFGO_Quarter_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    
    /** C_DocType_ID AD_Reference_ID=1000048 */
    public static final int C_DOCTYPE_ID_AD_Reference_ID=1000048;
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
    
    /** Set Date Invoiced.
    @param DateInvoiced Date printed on Invoice */
    public void setDateInvoiced (Timestamp DateInvoiced)
    {
        if (DateInvoiced == null) throw new IllegalArgumentException ("DateInvoiced is mandatory.");
        set_Value ("DateInvoiced", DateInvoiced);
        
    }
    
    /** Get Date Invoiced.
    @return Date printed on Invoice */
    public Timestamp getDateInvoiced() 
    {
        return (Timestamp)get_Value("DateInvoiced");
        
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
        if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
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
    
    /** Set Invoiced Amount.
    @param InvoicedAmt The amount invoiced */
    public void setInvoicedAmt (java.math.BigDecimal InvoicedAmt)
    {
        if (InvoicedAmt == null) throw new IllegalArgumentException ("InvoicedAmt is mandatory.");
        set_Value ("InvoicedAmt", InvoicedAmt);
        
    }
    
    /** Get Invoiced Amount.
    @return The amount invoiced */
    public java.math.BigDecimal getInvoicedAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedAmt");
        
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
    
    
}
