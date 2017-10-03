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
/** Generated Model for AFGO_QuotationResponseLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_QuotationResponseLine.java,v 1.3 2009/11/24 14:17:51 tomassen Exp $ */
public class X_AFGO_QuotationResponseLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_QuotationResponseLine_ID id
    @param trxName transaction
    */
    public X_AFGO_QuotationResponseLine (Ctx ctx, int AFGO_QuotationResponseLine_ID, String trxName)
    {
        super (ctx, AFGO_QuotationResponseLine_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_QuotationResponseLine_ID == 0)
        {
            setAFGO_QuotationResponseLine_ID (0);
            setAFGO_QuotationResponse_ID (0);
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM AFGO_QuotationResponseLine WHERE AFGO_QuotationResponse_ID=@AFGO_QuotationResponse_ID@
            setLineNetAmt (Env.ZERO);	// 0
            setPrice (Env.ZERO);	// 0
            setQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_QuotationResponseLine (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536530496789L;
    /** Last Updated Timestamp 2009-10-01 15:53:00.0 */
    public static final long updatedMS = 1254405180000L;
    /** AD_Table_ID=1000018 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_QuotationResponseLine");
        
    }
    ;
    
    /** TableName=AFGO_QuotationResponseLine */
    public static final String Table_Name="AFGO_QuotationResponseLine";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_QuotationResponseLine");
    
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
    
    
    /** AFGO_LotItem_ID AD_Reference_ID=1000015 */
    public static final int AFGO_LOTITEM_ID_AD_Reference_ID=1000015;
    /** Set Lot Item.
    @param AFGO_LotItem_ID A lot item identifies a specific product or charge within a lot. */
    public void setAFGO_LotItem_ID (int AFGO_LotItem_ID)
    {
        if (AFGO_LotItem_ID <= 0) set_Value ("AFGO_LotItem_ID", null);
        else
        set_Value ("AFGO_LotItem_ID", Integer.valueOf(AFGO_LotItem_ID));
        
    }
    
    /** Get Lot Item.
    @return A lot item identifies a specific product or charge within a lot. */
    public int getAFGO_LotItem_ID() 
    {
        return get_ValueAsInt("AFGO_LotItem_ID");
        
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
    
    /** Set Quotation Response Line.
    @param AFGO_QuotationResponseLine_ID On line level you will select the right purchase item and enter the given price. The tab score doesn't do anything and is there for future functionality.  */
    public void setAFGO_QuotationResponseLine_ID (int AFGO_QuotationResponseLine_ID)
    {
        if (AFGO_QuotationResponseLine_ID < 1) throw new IllegalArgumentException ("AFGO_QuotationResponseLine_ID is mandatory.");
        set_ValueNoCheck ("AFGO_QuotationResponseLine_ID", Integer.valueOf(AFGO_QuotationResponseLine_ID));
        
    }
    
    /** Get Quotation Response Line.
    @return On line level you will select the right purchase item and enter the given price. The tab score doesn't do anything and is there for future functionality.  */
    public int getAFGO_QuotationResponseLine_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationResponseLine_ID");
        
    }
    
    
    /** AFGO_QuotationResponse_ID AD_Reference_ID=1000016 */
    public static final int AFGO_QUOTATIONRESPONSE_ID_AD_Reference_ID=1000016;
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_QuotationResponse_ID()));
        
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
    
    
    /** C_Charge_ID AD_Reference_ID=200 */
    public static final int C_CHARGE_ID_AD_Reference_ID=200;
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
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        if (LineNetAmt == null) throw new IllegalArgumentException ("LineNetAmt is mandatory.");
        set_Value ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
    }
    
    
    /** M_Product_ID AD_Reference_ID=162 */
    public static final int M_PRODUCT_ID_AD_Reference_ID=162;
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        if (Price == null) throw new IllegalArgumentException ("Price is mandatory.");
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        throw new IllegalArgumentException ("Processed is virtual column");
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
