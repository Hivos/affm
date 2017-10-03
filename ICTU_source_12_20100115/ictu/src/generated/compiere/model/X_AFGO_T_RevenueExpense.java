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
/** Generated Model for AFGO_T_RevenueExpense
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_T_RevenueExpense.java,v 1.20 2009/11/24 12:01:47 tomassen Exp $ */
public class X_AFGO_T_RevenueExpense extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_T_RevenueExpense_ID id
    @param trxName transaction
    */
    public X_AFGO_T_RevenueExpense (Ctx ctx, int AFGO_T_RevenueExpense_ID, String trxName)
    {
        super (ctx, AFGO_T_RevenueExpense_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_T_RevenueExpense_ID == 0)
        {
            setAD_PInstance_ID (0);
            setAD_Table_ID (0);
            setAFGO_Activity_ID (0);
            setAFGO_Phase_ID (0);
            setAFGO_Program_ID (0);
            setAFGO_ProjectCluster_ID (0);
            setAFGO_Project_ID (0);
            setAFGO_Quarter_ID (0);
            setAFGO_ServiceType_ID (0);
            setAFGO_Year_ID (0);
            setC_DocType_ID (0);
            setDocBaseType (null);
            setDocStatus (null);
            setDocumentNo (null);
            setLine (0);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_T_RevenueExpense (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27541109622789L;
    /** Last Updated Timestamp 2009-11-23 14:51:46.0 */
    public static final long updatedMS = 1258984306000L;
    /** AD_Table_ID=1000055 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_T_RevenueExpense");
        
    }
    ;
    
    /** TableName=AFGO_T_RevenueExpense */
    public static final String Table_Name="AFGO_T_RevenueExpense";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_T_RevenueExpense");
    
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
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    
    /** AD_Table_ID AD_Reference_ID=415 */
    public static final int AD_TABLE_ID_AD_Reference_ID=415;
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    
    /** AFGO_Activity_ID AD_Reference_ID=1000004 */
    public static final int AFGO_ACTIVITY_ID_AD_Reference_ID=1000004;
    /** Set Activity.
    @param AFGO_Activity_ID Activity is the lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the phase level. */
    public void setAFGO_Activity_ID (int AFGO_Activity_ID)
    {
        if (AFGO_Activity_ID < 1) throw new IllegalArgumentException ("AFGO_Activity_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Activity_ID", Integer.valueOf(AFGO_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Activity is the lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the phase level. */
    public int getAFGO_Activity_ID() 
    {
        return get_ValueAsInt("AFGO_Activity_ID");
        
    }
    
    
    /** AFGO_Month_ID AD_Reference_ID=1000023 */
    public static final int AFGO_MONTH_ID_AD_Reference_ID=1000023;
    /** Set Month.
    @param AFGO_Month_ID A valid program month within a program quarter. If not applicable choose standard. */
    public void setAFGO_Month_ID (int AFGO_Month_ID)
    {
        if (AFGO_Month_ID <= 0) set_ValueNoCheck ("AFGO_Month_ID", null);
        else
        set_ValueNoCheck ("AFGO_Month_ID", Integer.valueOf(AFGO_Month_ID));
        
    }
    
    /** Get Month.
    @return A valid program month within a program quarter. If not applicable choose standard. */
    public int getAFGO_Month_ID() 
    {
        return get_ValueAsInt("AFGO_Month_ID");
        
    }
    
    
    /** AFGO_Phase_ID AD_Reference_ID=1000008 */
    public static final int AFGO_PHASE_ID_AD_Reference_ID=1000008;
    /** Set Phase.
    @param AFGO_Phase_ID Phase is the second lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the project level. */
    public void setAFGO_Phase_ID (int AFGO_Phase_ID)
    {
        if (AFGO_Phase_ID < 1) throw new IllegalArgumentException ("AFGO_Phase_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Phase_ID", Integer.valueOf(AFGO_Phase_ID));
        
    }
    
    /** Get Phase.
    @return Phase is the second lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the project level. */
    public int getAFGO_Phase_ID() 
    {
        return get_ValueAsInt("AFGO_Phase_ID");
        
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
    
    
    /** AFGO_ProjectCluster_ID AD_Reference_ID=1000007 */
    public static final int AFGO_PROJECTCLUSTER_ID_AD_Reference_ID=1000007;
    /** Set Project Cluster.
    @param AFGO_ProjectCluster_ID Project cluster is the second highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public void setAFGO_ProjectCluster_ID (int AFGO_ProjectCluster_ID)
    {
        if (AFGO_ProjectCluster_ID < 1) throw new IllegalArgumentException ("AFGO_ProjectCluster_ID is mandatory.");
        set_ValueNoCheck ("AFGO_ProjectCluster_ID", Integer.valueOf(AFGO_ProjectCluster_ID));
        
    }
    
    /** Get Project Cluster.
    @return Project cluster is the second highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public int getAFGO_ProjectCluster_ID() 
    {
        return get_ValueAsInt("AFGO_ProjectCluster_ID");
        
    }
    
    
    /** AFGO_Project_ID AD_Reference_ID=1000002 */
    public static final int AFGO_PROJECT_ID_AD_Reference_ID=1000002;
    /** Set Project.
    @param AFGO_Project_ID Project is the third highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public void setAFGO_Project_ID (int AFGO_Project_ID)
    {
        if (AFGO_Project_ID < 1) throw new IllegalArgumentException ("AFGO_Project_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Project_ID", Integer.valueOf(AFGO_Project_ID));
        
    }
    
    /** Get Project.
    @return Project is the third highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public int getAFGO_Project_ID() 
    {
        return get_ValueAsInt("AFGO_Project_ID");
        
    }
    
    
    /** AFGO_Quarter_ID AD_Reference_ID=1000021 */
    public static final int AFGO_QUARTER_ID_AD_Reference_ID=1000021;
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
    
    
    /** AFGO_ServiceType_ID AD_Reference_ID=1000030 */
    public static final int AFGO_SERVICETYPE_ID_AD_Reference_ID=1000030;
    /** Set Service Type.
    @param AFGO_ServiceType_ID Service type allows the users to keep track of financial data over the cost centres, programs and organisations. */
    public void setAFGO_ServiceType_ID (int AFGO_ServiceType_ID)
    {
        if (AFGO_ServiceType_ID < 1) throw new IllegalArgumentException ("AFGO_ServiceType_ID is mandatory.");
        set_ValueNoCheck ("AFGO_ServiceType_ID", Integer.valueOf(AFGO_ServiceType_ID));
        
    }
    
    /** Get Service Type.
    @return Service type allows the users to keep track of financial data over the cost centres, programs and organisations. */
    public int getAFGO_ServiceType_ID() 
    {
        return get_ValueAsInt("AFGO_ServiceType_ID");
        
    }
    
    
    /** AFGO_Year_ID AD_Reference_ID=1000020 */
    public static final int AFGO_YEAR_ID_AD_Reference_ID=1000020;
    /** Set Year.
    @param AFGO_Year_ID A valid program year. Used for program reporting, doesn't need to correspond with accounting data. */
    public void setAFGO_Year_ID (int AFGO_Year_ID)
    {
        if (AFGO_Year_ID < 1) throw new IllegalArgumentException ("AFGO_Year_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Year_ID", Integer.valueOf(AFGO_Year_ID));
        
    }
    
    /** Get Year.
    @return A valid program year. Used for program reporting, doesn't need to correspond with accounting data. */
    public int getAFGO_Year_ID() 
    {
        return get_ValueAsInt("AFGO_Year_ID");
        
    }
    
    
    /** Account_ID AD_Reference_ID=132 */
    public static final int ACCOUNT_ID_AD_Reference_ID=132;
    /** Set Account.
    @param Account_ID Account used */
    public void setAccount_ID (int Account_ID)
    {
        if (Account_ID <= 0) set_Value ("Account_ID", null);
        else
        set_Value ("Account_ID", Integer.valueOf(Account_ID));
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Amount.
    @param Amt Amount */
    public void setAmt (java.math.BigDecimal Amt)
    {
        set_Value ("Amt", Amt);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getAmt() 
    {
        return get_ValueAsBigDecimal("Amt");
        
    }
    
    /** Set Budget Amt.
    @param BudgetAmt Budget Amt */
    public void setBudgetAmt (java.math.BigDecimal BudgetAmt)
    {
        set_ValueNoCheck ("BudgetAmt", BudgetAmt);
        
    }
    
    /** Get Budget Amt.
    @return Budget Amt */
    public java.math.BigDecimal getBudgetAmt() 
    {
        return get_ValueAsBigDecimal("BudgetAmt");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    
    /** C_Charge_ID AD_Reference_ID=200 */
    public static final int C_CHARGE_ID_AD_Reference_ID=200;
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_ValueNoCheck ("C_Charge_ID", null);
        else
        set_ValueNoCheck ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
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
    
    /** Set Cost Allocation Amt.
    @param CostAllocationAmt Cost Allocation Amt */
    public void setCostAllocationAmt (java.math.BigDecimal CostAllocationAmt)
    {
        set_ValueNoCheck ("CostAllocationAmt", CostAllocationAmt);
        
    }
    
    /** Get Cost Allocation Amt.
    @return Cost Allocation Amt */
    public java.math.BigDecimal getCostAllocationAmt() 
    {
        return get_ValueAsBigDecimal("CostAllocationAmt");
        
    }
    
    /** Set Cost Distribution Amt.
    @param CostDistributionAmt Cost Distribution Amt */
    public void setCostDistributionAmt (java.math.BigDecimal CostDistributionAmt)
    {
        set_ValueNoCheck ("CostDistributionAmt", CostDistributionAmt);
        
    }
    
    /** Get Cost Distribution Amt.
    @return Cost Distribution Amt */
    public java.math.BigDecimal getCostDistributionAmt() 
    {
        return get_ValueAsBigDecimal("CostDistributionAmt");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
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
    
    
    /** DocBaseType AD_Reference_ID=432 */
    public static final int DOCBASETYPE_AD_Reference_ID=432;
    /** Set Document BaseType.
    @param DocBaseType Logical type of document */
    public void setDocBaseType (String DocBaseType)
    {
        set_ValueNoCheck ("DocBaseType", DocBaseType);
        
    }
    
    /** Get Document BaseType.
    @return Logical type of document */
    public String getDocBaseType() 
    {
        return (String)get_Value("DocBaseType");
        
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
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_ValueNoCheck ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Fund Amt.
    @param FundAmt Fund Amt */
    public void setFundAmt (java.math.BigDecimal FundAmt)
    {
        set_ValueNoCheck ("FundAmt", FundAmt);
        
    }
    
    /** Get Fund Amt.
    @return Fund Amt */
    public java.math.BigDecimal getFundAmt() 
    {
        return get_ValueAsBigDecimal("FundAmt");
        
    }
    
    /** Set Internal Commitment Amt.
    @param InternalCommitmentAmt Internal Commitment Amt */
    public void setInternalCommitmentAmt (java.math.BigDecimal InternalCommitmentAmt)
    {
        set_ValueNoCheck ("InternalCommitmentAmt", InternalCommitmentAmt);
        
    }
    
    /** Get Internal Commitment Amt.
    @return Internal Commitment Amt */
    public java.math.BigDecimal getInternalCommitmentAmt() 
    {
        return get_ValueAsBigDecimal("InternalCommitmentAmt");
        
    }
    
    /** Set Invoiced Commitment Amt.
    @param InvoicedCommitmentAmt Invoiced Commitment Amt */
    public void setInvoicedCommitmentAmt (java.math.BigDecimal InvoicedCommitmentAmt)
    {
        set_ValueNoCheck ("InvoicedCommitmentAmt", InvoicedCommitmentAmt);
        
    }
    
    /** Get Invoiced Commitment Amt.
    @return Invoiced Commitment Amt */
    public java.math.BigDecimal getInvoicedCommitmentAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedCommitmentAmt");
        
    }
    
    /** Set Invoiced Fund Amt.
    @param InvoicedFundAmt Invoiced Fund Amt */
    public void setInvoicedFundAmt (java.math.BigDecimal InvoicedFundAmt)
    {
        set_ValueNoCheck ("InvoicedFundAmt", InvoicedFundAmt);
        
    }
    
    /** Get Invoiced Fund Amt.
    @return Invoiced Fund Amt */
    public java.math.BigDecimal getInvoicedFundAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedFundAmt");
        
    }
    
    /** Set Invoiced Payables Amt.
    @param InvoicedPayablesAmt Invoiced Payables Amt */
    public void setInvoicedPayablesAmt (java.math.BigDecimal InvoicedPayablesAmt)
    {
        set_ValueNoCheck ("InvoicedPayablesAmt", InvoicedPayablesAmt);
        
    }
    
    /** Get Invoiced Payables Amt.
    @return Invoiced Payables Amt */
    public java.math.BigDecimal getInvoicedPayablesAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedPayablesAmt");
        
    }
    
    /** Set Invoiced Receivables Amt.
    @param InvoicedReceivablesAmt Invoiced Receivables Amt */
    public void setInvoicedReceivablesAmt (java.math.BigDecimal InvoicedReceivablesAmt)
    {
        set_ValueNoCheck ("InvoicedReceivablesAmt", InvoicedReceivablesAmt);
        
    }
    
    /** Get Invoiced Receivables Amt.
    @return Invoiced Receivables Amt */
    public java.math.BigDecimal getInvoicedReceivablesAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedReceivablesAmt");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_ValueNoCheck ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Line ID.
    @param Line_ID Transaction line ID (internal) */
    public void setLine_ID (int Line_ID)
    {
        if (Line_ID <= 0) set_Value ("Line_ID", null);
        else
        set_Value ("Line_ID", Integer.valueOf(Line_ID));
        
    }
    
    /** Get Line ID.
    @return Transaction line ID (internal) */
    public int getLine_ID() 
    {
        return get_ValueAsInt("Line_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Prognosis Amt.
    @param PrognosisAmt Prognosis Amt */
    public void setPrognosisAmt (java.math.BigDecimal PrognosisAmt)
    {
        set_ValueNoCheck ("PrognosisAmt", PrognosisAmt);
        
    }
    
    /** Get Prognosis Amt.
    @return Prognosis Amt */
    public java.math.BigDecimal getPrognosisAmt() 
    {
        return get_ValueAsBigDecimal("PrognosisAmt");
        
    }
    
    /** Set Purchase Commitment Amt.
    @param PurchaseCommitmentAmt Purchase Commitment Amt */
    public void setPurchaseCommitmentAmt (java.math.BigDecimal PurchaseCommitmentAmt)
    {
        set_ValueNoCheck ("PurchaseCommitmentAmt", PurchaseCommitmentAmt);
        
    }
    
    /** Get Purchase Commitment Amt.
    @return Purchase Commitment Amt */
    public java.math.BigDecimal getPurchaseCommitmentAmt() 
    {
        return get_ValueAsBigDecimal("PurchaseCommitmentAmt");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    
}
