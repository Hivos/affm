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
/** Generated Model for AFGO_InternalCommitment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_InternalCommitment.java,v 1.54 2010/01/04 13:30:17 tomassen Exp $ */
public class X_AFGO_InternalCommitment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_InternalCommitment_ID id
    @param trxName transaction
    */
    public X_AFGO_InternalCommitment (Ctx ctx, int AFGO_InternalCommitment_ID, String trxName)
    {
        super (ctx, AFGO_InternalCommitment_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_InternalCommitment_ID == 0)
        {
            setAFGO_InternalCommitment_ID (0);
            setAFGO_Program_ID (0);
            setC_DocType_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateContract (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDocumentNo (null);
            setGrandTotal (Env.ZERO);	// 0
            setIsBudgetMaintainerApproved (false);	// N
            setIsBudgetOwnerApproved (false);	// N
            setProcessed (false);	// N
            setProcessing (false);	// N
            setProviderProgram_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_InternalCommitment (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554648789L;
    /** Last Updated Timestamp 2009-03-30 18:08:52.0 */
    public static final long updatedMS = 1238429332000L;
    /** AD_Table_ID=1000031 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_InternalCommitment");
        
    }
    ;
    
    /** TableName=AFGO_InternalCommitment */
    public static final String Table_Name="AFGO_InternalCommitment";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_InternalCommitment");
    
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
    /** Set Internal Commitment.
    @param AFGO_InternalCommitment_ID Internal Commitment */
    public void setAFGO_InternalCommitment_ID (int AFGO_InternalCommitment_ID)
    {
        if (AFGO_InternalCommitment_ID < 1) throw new IllegalArgumentException ("AFGO_InternalCommitment_ID is mandatory.");
        set_ValueNoCheck ("AFGO_InternalCommitment_ID", Integer.valueOf(AFGO_InternalCommitment_ID));
        
    }
    
    /** Get Internal Commitment.
    @return Internal Commitment */
    public int getAFGO_InternalCommitment_ID() 
    {
        return get_ValueAsInt("AFGO_InternalCommitment_ID");
        
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
    
    /** Set Allocated Amount.
    @param AllocatedAmt Amount allocated to this document */
    public void setAllocatedAmt (java.math.BigDecimal AllocatedAmt)
    {
        set_Value ("AllocatedAmt", AllocatedAmt);
        
    }
    
    /** Get Allocated Amount.
    @return Amount allocated to this document */
    public java.math.BigDecimal getAllocatedAmt() 
    {
        return get_ValueAsBigDecimal("AllocatedAmt");
        
    }
    
    
    /** C_DocType_ID AD_Reference_ID=1000045 */
    public static final int C_DOCTYPE_ID_AD_Reference_ID=1000045;
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
    
    /** Set Contract Date.
    @param DateContract Indicates the (planned) effective date of this document. */
    public void setDateContract (Timestamp DateContract)
    {
        if (DateContract == null) throw new IllegalArgumentException ("DateContract is mandatory.");
        set_Value ("DateContract", DateContract);
        
    }
    
    /** Get Contract Date.
    @return Indicates the (planned) effective date of this document. */
    public Timestamp getDateContract() 
    {
        return (Timestamp)get_Value("DateContract");
        
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
        set_Value ("DocumentNo", DocumentNo);
        
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
    
    
    /** ProviderProgram_ID AD_Reference_ID=1000042 */
    public static final int PROVIDERPROGRAM_ID_AD_Reference_ID=1000042;
    /** Set Provider Program.
    @param ProviderProgram_ID Provider Program */
    public void setProviderProgram_ID (int ProviderProgram_ID)
    {
        if (ProviderProgram_ID < 1) throw new IllegalArgumentException ("ProviderProgram_ID is mandatory.");
        set_ValueNoCheck ("ProviderProgram_ID", Integer.valueOf(ProviderProgram_ID));
        
    }
    
    /** Get Provider Program.
    @return Provider Program */
    public int getProviderProgram_ID() 
    {
        return get_ValueAsInt("ProviderProgram_ID");
        
    }
    
    
}
