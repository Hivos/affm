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
/** Generated Model for AFGO_CostDistrAllocation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_CostDistrAllocation.java,v 1.56 2010/01/04 13:30:18 tomassen Exp $ */
public class X_AFGO_CostDistrAllocation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_CostDistrAllocation_ID id
    @param trxName transaction
    */
    public X_AFGO_CostDistrAllocation (Ctx ctx, int AFGO_CostDistrAllocation_ID, String trxName)
    {
        super (ctx, AFGO_CostDistrAllocation_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_CostDistrAllocation_ID == 0)
        {
            setAFGO_Activity_ID (0);
            setAFGO_CostDistrAllocation_ID (0);
            setAFGO_CostDistrLine_ID (0);
            setAFGO_Month_ID (0);
            setAFGO_Phase_ID (0);
            setAFGO_Program_ID (0);
            setAFGO_ProjectCluster_ID (0);
            setAFGO_Project_ID (0);
            setAFGO_Quarter_ID (0);
            setAFGO_ServiceType_ID (0);
            setC_Charge_ID (0);
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM AFGO_CostDistrAllocation WHERE AFGO_CostDistrLine_ID=@AFGO_CostDistrLine_ID@
            setLineNetAmt (Env.ZERO);	// 0
            setPrice (Env.ZERO);	// 0
            setProcessing (false);	// N
            setQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_CostDistrAllocation (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27525383321789L;
    /** Last Updated Timestamp 2009-05-25 15:26:45.0 */
    public static final long updatedMS = 1243258005000L;
    /** AD_Table_ID=1000030 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_CostDistrAllocation");
        
    }
    ;
    
    /** TableName=AFGO_CostDistrAllocation */
    public static final String Table_Name="AFGO_CostDistrAllocation";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_CostDistrAllocation");
    
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
        if (AFGO_Activity_ID < 1) throw new IllegalArgumentException ("AFGO_Activity_ID is mandatory.");
        set_Value ("AFGO_Activity_ID", Integer.valueOf(AFGO_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Activity is the lowest level of the specified set. If no value is required, leave it to the standard or default value defined on the phase level. */
    public int getAFGO_Activity_ID() 
    {
        return get_ValueAsInt("AFGO_Activity_ID");
        
    }
    
    /** Set AFGO_CostDistrAllocation.
    @param AFGO_CostDistrAllocation_ID AFGO_CostDistrAllocation */
    public void setAFGO_CostDistrAllocation_ID (int AFGO_CostDistrAllocation_ID)
    {
        if (AFGO_CostDistrAllocation_ID < 1) throw new IllegalArgumentException ("AFGO_CostDistrAllocation_ID is mandatory.");
        set_ValueNoCheck ("AFGO_CostDistrAllocation_ID", Integer.valueOf(AFGO_CostDistrAllocation_ID));
        
    }
    
    /** Get AFGO_CostDistrAllocation.
    @return AFGO_CostDistrAllocation */
    public int getAFGO_CostDistrAllocation_ID() 
    {
        return get_ValueAsInt("AFGO_CostDistrAllocation_ID");
        
    }
    
    
    /** AFGO_CostDistrLine_ID AD_Reference_ID=1000032 */
    public static final int AFGO_COSTDISTRLINE_ID_AD_Reference_ID=1000032;
    /** Set Cost Distribution Line.
    @param AFGO_CostDistrLine_ID Cost Distribution Line */
    public void setAFGO_CostDistrLine_ID (int AFGO_CostDistrLine_ID)
    {
        if (AFGO_CostDistrLine_ID < 1) throw new IllegalArgumentException ("AFGO_CostDistrLine_ID is mandatory.");
        set_ValueNoCheck ("AFGO_CostDistrLine_ID", Integer.valueOf(AFGO_CostDistrLine_ID));
        
    }
    
    /** Get Cost Distribution Line.
    @return Cost Distribution Line */
    public int getAFGO_CostDistrLine_ID() 
    {
        return get_ValueAsInt("AFGO_CostDistrLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_CostDistrLine_ID()));
        
    }
    
    
    /** AFGO_CostDistr_ID AD_Reference_ID=1000031 */
    public static final int AFGO_COSTDISTR_ID_AD_Reference_ID=1000031;
    /** Set Cost Distribution.
    @param AFGO_CostDistr_ID Cost Distribution */
    public void setAFGO_CostDistr_ID (int AFGO_CostDistr_ID)
    {
        throw new IllegalArgumentException ("AFGO_CostDistr_ID is virtual column");
        
    }
    
    /** Get Cost Distribution.
    @return Cost Distribution */
    public int getAFGO_CostDistr_ID() 
    {
        return get_ValueAsInt("AFGO_CostDistr_ID");
        
    }
    
    
    /** AFGO_InternalCommitmentLine_ID AD_Reference_ID=1000043 */
    public static final int AFGO_INTERNALCOMMITMENTLINE_ID_AD_Reference_ID=1000043;
    /** Set Internal Commitment Line.
    @param AFGO_InternalCommitmentLine_ID Internal Commitment Line */
    public void setAFGO_InternalCommitmentLine_ID (int AFGO_InternalCommitmentLine_ID)
    {
        if (AFGO_InternalCommitmentLine_ID <= 0) set_ValueNoCheck ("AFGO_InternalCommitmentLine_ID", null);
        else
        set_ValueNoCheck ("AFGO_InternalCommitmentLine_ID", Integer.valueOf(AFGO_InternalCommitmentLine_ID));
        
    }
    
    /** Get Internal Commitment Line.
    @return Internal Commitment Line */
    public int getAFGO_InternalCommitmentLine_ID() 
    {
        return get_ValueAsInt("AFGO_InternalCommitmentLine_ID");
        
    }
    
    
    /** AFGO_InternalCommitment_ID AD_Reference_ID=1000033 */
    public static final int AFGO_INTERNALCOMMITMENT_ID_AD_Reference_ID=1000033;
    /** Set Internal Commitment.
    @param AFGO_InternalCommitment_ID Internal Commitment */
    public void setAFGO_InternalCommitment_ID (int AFGO_InternalCommitment_ID)
    {
        if (AFGO_InternalCommitment_ID <= 0) set_ValueNoCheck ("AFGO_InternalCommitment_ID", null);
        else
        set_ValueNoCheck ("AFGO_InternalCommitment_ID", Integer.valueOf(AFGO_InternalCommitment_ID));
        
    }
    
    /** Get Internal Commitment.
    @return Internal Commitment */
    public int getAFGO_InternalCommitment_ID() 
    {
        return get_ValueAsInt("AFGO_InternalCommitment_ID");
        
    }
    
    
    /** AFGO_Month_ID AD_Reference_ID=1000023 */
    public static final int AFGO_MONTH_ID_AD_Reference_ID=1000023;
    /** Set Month.
    @param AFGO_Month_ID A valid program month within a program quarter. If not applicable choose standard. */
    public void setAFGO_Month_ID (int AFGO_Month_ID)
    {
        if (AFGO_Month_ID < 1) throw new IllegalArgumentException ("AFGO_Month_ID is mandatory.");
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
        if (AFGO_Phase_ID < 1) throw new IllegalArgumentException ("AFGO_Phase_ID is mandatory.");
        set_Value ("AFGO_Phase_ID", Integer.valueOf(AFGO_Phase_ID));
        
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
        set_Value ("AFGO_Program_ID", Integer.valueOf(AFGO_Program_ID));
        
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
        if (AFGO_Project_ID < 1) throw new IllegalArgumentException ("AFGO_Project_ID is mandatory.");
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
        if (AFGO_Quarter_ID < 1) throw new IllegalArgumentException ("AFGO_Quarter_ID is mandatory.");
        set_Value ("AFGO_Quarter_ID", Integer.valueOf(AFGO_Quarter_ID));
        
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
        if (C_Charge_ID < 1) throw new IllegalArgumentException ("C_Charge_ID is mandatory.");
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
