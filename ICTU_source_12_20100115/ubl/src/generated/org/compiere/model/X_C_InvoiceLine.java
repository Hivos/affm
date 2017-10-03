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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for C_InvoiceLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_C_InvoiceLine.java,v 1.3 2009/11/24 14:17:57 tomassen Exp $ */
public class X_C_InvoiceLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoiceLine_ID id
    @param trxName transaction
    */
    public X_C_InvoiceLine (Ctx ctx, int C_InvoiceLine_ID, String trxName)
    {
        super (ctx, C_InvoiceLine_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoiceLine_ID == 0)
        {
            setC_InvoiceLine_ID (0);
            setC_Invoice_ID (0);
            setIsDescription (false);	// N
            setIsPrinted (true);	// Y
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_InvoiceLine WHERE C_Invoice_ID=@C_Invoice_ID@
            setLineNetAmt (Env.ZERO);
            setPriceActual (Env.ZERO);
            setPriceEntered (Env.ZERO);
            setPriceLimit (Env.ZERO);
            setPriceList (Env.ZERO);
            setProcessed (false);	// N
            setQtyEntered (Env.ZERO);	// 1
            setQtyInvoiced (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_C_InvoiceLine (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27519331180789L;
    /** Last Updated Timestamp 2009-03-16 13:17:44.0 */
    public static final long updatedMS = 1237205864000L;
    /** AD_Table_ID=333 */
    public static final int Table_ID=333;
    
    /** TableName=C_InvoiceLine */
    public static final String Table_Name="C_InvoiceLine";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_InvoiceLine");
    
    /** AccessLevel
    @return 1 - Org 
    */
    @Override protected PO.AccessLevel get_AccessLevel()
    {
        return PO.AccessLevel.ORG;
        
    }
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    
    /** AD_OrgTrx_ID AD_Reference_ID=130 */
    public static final int AD_ORGTRX_ID_AD_Reference_ID=130;
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    
    /** AFGO_Activity_ID AD_Reference_ID=1000004 */
    public static final int AFGO_ACTIVITY_ID_AD_Reference_ID=1000004;
    /** Set Activity.
    @param AFGO_Activity_ID Activity is the lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the phase level. */
    public void setAFGO_Activity_ID (int AFGO_Activity_ID)
    {
        if (AFGO_Activity_ID <= 0) set_Value ("AFGO_Activity_ID", null);
        else
        set_Value ("AFGO_Activity_ID", Integer.valueOf(AFGO_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Activity is the lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the phase level. */
    public int getAFGO_Activity_ID() 
    {
        return get_ValueAsInt("AFGO_Activity_ID");
        
    }
    
    
    /** AFGO_FundScheduleLine_ID AD_Reference_ID=1000025 */
    public static final int AFGO_FUNDSCHEDULELINE_ID_AD_Reference_ID=1000025;
    /** Set Fund Schedule Line.
    @param AFGO_FundScheduleLine_ID A fund Schedule Line contains a charge, amount and specified set. */
    public void setAFGO_FundScheduleLine_ID (int AFGO_FundScheduleLine_ID)
    {
        if (AFGO_FundScheduleLine_ID <= 0) set_Value ("AFGO_FundScheduleLine_ID", null);
        else
        set_Value ("AFGO_FundScheduleLine_ID", Integer.valueOf(AFGO_FundScheduleLine_ID));
        
    }
    
    /** Get Fund Schedule Line.
    @return A fund Schedule Line contains a charge, amount and specified set. */
    public int getAFGO_FundScheduleLine_ID() 
    {
        return get_ValueAsInt("AFGO_FundScheduleLine_ID");
        
    }
    
    
    /** AFGO_Month_ID AD_Reference_ID=1000023 */
    public static final int AFGO_MONTH_ID_AD_Reference_ID=1000023;
    /** Set Month.
    @param AFGO_Month_ID A valid program month within a program quarter. If not applicable choose standard. */
    public void setAFGO_Month_ID (int AFGO_Month_ID)
    {
        if (AFGO_Month_ID <= 0) set_Value ("AFGO_Month_ID", null);
        else
        set_Value ("AFGO_Month_ID", Integer.valueOf(AFGO_Month_ID));
        
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
        if (AFGO_Phase_ID <= 0) set_Value ("AFGO_Phase_ID", null);
        else
        set_Value ("AFGO_Phase_ID", Integer.valueOf(AFGO_Phase_ID));
        
    }
    
    /** Get Phase.
    @return Phase is the second lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the project level. */
    public int getAFGO_Phase_ID() 
    {
        return get_ValueAsInt("AFGO_Phase_ID");
        
    }
    
    
    /** AFGO_ProjectCluster_ID AD_Reference_ID=1000007 */
    public static final int AFGO_PROJECTCLUSTER_ID_AD_Reference_ID=1000007;
    /** Set Project Cluster.
    @param AFGO_ProjectCluster_ID Project cluster is the second highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public void setAFGO_ProjectCluster_ID (int AFGO_ProjectCluster_ID)
    {
        if (AFGO_ProjectCluster_ID <= 0) set_Value ("AFGO_ProjectCluster_ID", null);
        else
        set_Value ("AFGO_ProjectCluster_ID", Integer.valueOf(AFGO_ProjectCluster_ID));
        
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
        if (AFGO_Project_ID <= 0) set_Value ("AFGO_Project_ID", null);
        else
        set_Value ("AFGO_Project_ID", Integer.valueOf(AFGO_Project_ID));
        
    }
    
    /** Get Project.
    @return Project is the third highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public int getAFGO_Project_ID() 
    {
        return get_ValueAsInt("AFGO_Project_ID");
        
    }
    
    
    /** AFGO_PurchaseCommitmentLine_ID AD_Reference_ID=1000052 */
    public static final int AFGO_PURCHASECOMMITMENTLINE_ID_AD_Reference_ID=1000052;
    /** Set Purchase Commitment Line.
    @param AFGO_PurchaseCommitmentLine_ID Purchase commitment lines describe in detail the desired product/charge or lot item, quantity, amount and specified set. */
    public void setAFGO_PurchaseCommitmentLine_ID (int AFGO_PurchaseCommitmentLine_ID)
    {
        if (AFGO_PurchaseCommitmentLine_ID <= 0) set_Value ("AFGO_PurchaseCommitmentLine_ID", null);
        else
        set_Value ("AFGO_PurchaseCommitmentLine_ID", Integer.valueOf(AFGO_PurchaseCommitmentLine_ID));
        
    }
    
    /** Get Purchase Commitment Line.
    @return Purchase commitment lines describe in detail the desired product/charge or lot item, quantity, amount and specified set. */
    public int getAFGO_PurchaseCommitmentLine_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseCommitmentLine_ID");
        
    }
    
    
    /** AFGO_Quarter_ID AD_Reference_ID=1000021 */
    public static final int AFGO_QUARTER_ID_AD_Reference_ID=1000021;
    /** Set Quarter.
    @param AFGO_Quarter_ID A valid program quarter within a program year. If not applicable choose standard. */
    public void setAFGO_Quarter_ID (int AFGO_Quarter_ID)
    {
        if (AFGO_Quarter_ID <= 0) set_Value ("AFGO_Quarter_ID", null);
        else
        set_Value ("AFGO_Quarter_ID", Integer.valueOf(AFGO_Quarter_ID));
        
    }
    
    /** Get Quarter.
    @return A valid program quarter within a program year. If not applicable choose standard. */
    public int getAFGO_Quarter_ID() 
    {
        return get_ValueAsInt("AFGO_Quarter_ID");
        
    }
    
    
    /** AFGO_ServiceType_ID AD_Reference_ID=1000031 */
    public static final int AFGO_SERVICETYPE_ID_AD_Reference_ID=1000031;
    /** Set Service Type.
    @param AFGO_ServiceType_ID Service type allows the users to keep track of financial data over the cost centres, programs and organisations. */
    public void setAFGO_ServiceType_ID (int AFGO_ServiceType_ID)
    {
        if (AFGO_ServiceType_ID <= 0) set_Value ("AFGO_ServiceType_ID", null);
        else
        set_Value ("AFGO_ServiceType_ID", Integer.valueOf(AFGO_ServiceType_ID));
        
    }
    
    /** Get Service Type.
    @return Service type allows the users to keep track of financial data over the cost centres, programs and organisations. */
    public int getAFGO_ServiceType_ID() 
    {
        return get_ValueAsInt("AFGO_ServiceType_ID");
        
    }
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_Value ("C_Charge_ID", null);
        else
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID < 1) throw new IllegalArgumentException ("C_InvoiceLine_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
        
    }
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_ValueNoCheck ("C_OrderLine_ID", null);
        else
        set_ValueNoCheck ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID <= 0) set_ValueNoCheck ("C_ProjectPhase_ID", null);
        else
        set_ValueNoCheck ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
    }
    
    /** Get Project Phase.
    @return Phase of a Project */
    public int getC_ProjectPhase_ID() 
    {
        return get_ValueAsInt("C_ProjectPhase_ID");
        
    }
    
    /** Set Project Task.
    @param C_ProjectTask_ID Actual Project Task in a Phase */
    public void setC_ProjectTask_ID (int C_ProjectTask_ID)
    {
        if (C_ProjectTask_ID <= 0) set_ValueNoCheck ("C_ProjectTask_ID", null);
        else
        set_ValueNoCheck ("C_ProjectTask_ID", Integer.valueOf(C_ProjectTask_ID));
        
    }
    
    /** Get Project Task.
    @return Actual Project Task in a Phase */
    public int getC_ProjectTask_ID() 
    {
        return get_ValueAsInt("C_ProjectTask_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_Value ("C_Tax_ID", null);
        else
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_ValueNoCheck ("C_UOM_ID", null);
        else
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
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
    
    /** Set Asset Addition.
    @param IsAssetAddition Is this an addition to an Asset or for a new Asset */
    public void setIsAssetAddition (boolean IsAssetAddition)
    {
        set_Value ("IsAssetAddition", Boolean.valueOf(IsAssetAddition));
        
    }
    
    /** Get Asset Addition.
    @return Is this an addition to an Asset or for a new Asset */
    public boolean isAssetAddition() 
    {
        return get_ValueAsBoolean("IsAssetAddition");
        
    }
    
    /** Set Description Only.
    @param IsDescription If true, the line is just description and no transaction */
    public void setIsDescription (boolean IsDescription)
    {
        set_Value ("IsDescription", Boolean.valueOf(IsDescription));
        
    }
    
    /** Get Description Only.
    @return If true, the line is just description and no transaction */
    public boolean isDescription() 
    {
        return get_ValueAsBoolean("IsDescription");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_Value ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    
    /** LineDocStatus AD_Reference_ID=131 */
    public static final int LINEDOCSTATUS_AD_Reference_ID=131;
    /** Unknown = ?? */
    public static final String LINEDOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String LINEDOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String LINEDOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String LINEDOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String LINEDOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String LINEDOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String LINEDOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String LINEDOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String LINEDOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String LINEDOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String LINEDOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String LINEDOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isLineDocStatusValid(String test)
    {
         return X_Ref__Document_Status.isValid(test);
         
    }
    /** Set Line Document Status.
    @param LineDocStatus The current status of the document line */
    public void setLineDocStatus (String LineDocStatus)
    {
        if (!isLineDocStatusValid(LineDocStatus))
        throw new IllegalArgumentException ("LineDocStatus Invalid value - " + LineDocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("LineDocStatus", LineDocStatus);
        
    }
    
    /** Get Line Document Status.
    @return The current status of the document line */
    public String getLineDocStatus() 
    {
        return (String)get_Value("LineDocStatus");
        
    }
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        if (LineNetAmt == null) throw new IllegalArgumentException ("LineNetAmt is mandatory.");
        set_ValueNoCheck ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
    }
    
    /** Set Line Total.
    @param LineTotalAmt Total line amount incl. Tax */
    public void setLineTotalAmt (java.math.BigDecimal LineTotalAmt)
    {
        set_Value ("LineTotalAmt", LineTotalAmt);
        
    }
    
    /** Get Line Total.
    @return Total line amount incl. Tax */
    public java.math.BigDecimal getLineTotalAmt() 
    {
        return get_ValueAsBigDecimal("LineTotalAmt");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_ValueNoCheck ("M_InOutLine_ID", null);
        else
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        if (PriceActual == null) throw new IllegalArgumentException ("PriceActual is mandatory.");
        set_ValueNoCheck ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Price.
    @param PriceEntered Price Entered - the price based on the selected/base UoM */
    public void setPriceEntered (java.math.BigDecimal PriceEntered)
    {
        if (PriceEntered == null) throw new IllegalArgumentException ("PriceEntered is mandatory.");
        set_Value ("PriceEntered", PriceEntered);
        
    }
    
    /** Get Price.
    @return Price Entered - the price based on the selected/base UoM */
    public java.math.BigDecimal getPriceEntered() 
    {
        return get_ValueAsBigDecimal("PriceEntered");
        
    }
    
    /** Set Limit Price.
    @param PriceLimit Lowest price for a product */
    public void setPriceLimit (java.math.BigDecimal PriceLimit)
    {
        if (PriceLimit == null) throw new IllegalArgumentException ("PriceLimit is mandatory.");
        set_Value ("PriceLimit", PriceLimit);
        
    }
    
    /** Get Limit Price.
    @return Lowest price for a product */
    public java.math.BigDecimal getPriceLimit() 
    {
        return get_ValueAsBigDecimal("PriceLimit");
        
    }
    
    /** Set List Price.
    @param PriceList List Price */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        if (PriceList == null) throw new IllegalArgumentException ("PriceList is mandatory.");
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List Price.
    @return List Price */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        if (QtyEntered == null) throw new IllegalArgumentException ("QtyEntered is mandatory.");
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
    }
    
    /** Set Quantity Invoiced.
    @param QtyInvoiced Invoiced Quantity */
    public void setQtyInvoiced (java.math.BigDecimal QtyInvoiced)
    {
        if (QtyInvoiced == null) throw new IllegalArgumentException ("QtyInvoiced is mandatory.");
        set_Value ("QtyInvoiced", QtyInvoiced);
        
    }
    
    /** Get Quantity Invoiced.
    @return Invoiced Quantity */
    public java.math.BigDecimal getQtyInvoiced() 
    {
        return get_ValueAsBigDecimal("QtyInvoiced");
        
    }
    
    /** Set Revenue Recognition Amt.
    @param RRAmt Revenue Recognition Amount */
    public void setRRAmt (java.math.BigDecimal RRAmt)
    {
        set_Value ("RRAmt", RRAmt);
        
    }
    
    /** Get Revenue Recognition Amt.
    @return Revenue Recognition Amount */
    public java.math.BigDecimal getRRAmt() 
    {
        return get_ValueAsBigDecimal("RRAmt");
        
    }
    
    /** Set Revenue Recognition Start.
    @param RRStartDate Revenue Recognition Start Date */
    public void setRRStartDate (Timestamp RRStartDate)
    {
        set_Value ("RRStartDate", RRStartDate);
        
    }
    
    /** Get Revenue Recognition Start.
    @return Revenue Recognition Start Date */
    public Timestamp getRRStartDate() 
    {
        return (Timestamp)get_Value("RRStartDate");
        
    }
    
    /** Set Referenced Invoice Line.
    @param Ref_InvoiceLine_ID Referenced Invoice Line */
    public void setRef_InvoiceLine_ID (int Ref_InvoiceLine_ID)
    {
        if (Ref_InvoiceLine_ID <= 0) set_Value ("Ref_InvoiceLine_ID", null);
        else
        set_Value ("Ref_InvoiceLine_ID", Integer.valueOf(Ref_InvoiceLine_ID));
        
    }
    
    /** Get Referenced Invoice Line.
    @return Referenced Invoice Line */
    public int getRef_InvoiceLine_ID() 
    {
        return get_ValueAsInt("Ref_InvoiceLine_ID");
        
    }
    
    /** Set Assigned Resource.
    @param S_ResourceAssignment_ID Assigned Resource */
    public void setS_ResourceAssignment_ID (int S_ResourceAssignment_ID)
    {
        if (S_ResourceAssignment_ID <= 0) set_ValueNoCheck ("S_ResourceAssignment_ID", null);
        else
        set_ValueNoCheck ("S_ResourceAssignment_ID", Integer.valueOf(S_ResourceAssignment_ID));
        
    }
    
    /** Get Assigned Resource.
    @return Assigned Resource */
    public int getS_ResourceAssignment_ID() 
    {
        return get_ValueAsInt("S_ResourceAssignment_ID");
        
    }
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        set_Value ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    
    /** User1_ID AD_Reference_ID=134 */
    public static final int USER1_ID_AD_Reference_ID=134;
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    
    /** User2_ID AD_Reference_ID=137 */
    public static final int USER2_ID_AD_Reference_ID=137;
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
