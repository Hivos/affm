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
/** Generated Model for AFGO_FundScheduleLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FundScheduleLine.java,v 1.35 2009/11/24 12:01:47 tomassen Exp $ */
public class X_AFGO_FundScheduleLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FundScheduleLine_ID id
    @param trxName transaction
    */
    public X_AFGO_FundScheduleLine (Ctx ctx, int AFGO_FundScheduleLine_ID, String trxName)
    {
        super (ctx, AFGO_FundScheduleLine_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FundScheduleLine_ID == 0)
        {
            setAFGO_Activity_ID (0);
            setAFGO_FundLine_ID (0);
            setAFGO_FundScheduleLine_ID (0);
            setAFGO_FundSchedule_ID (0);
            setAFGO_Phase_ID (0);
            setAFGO_ProjectCluster_ID (0);
            setAFGO_Project_ID (0);
            setAFGO_ServiceType_ID (0);
            setInvoicedAmt (Env.ZERO);	// 0
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM AFGO_FundScheduleLine WHERE AFGO_FundSchedule_ID=@AFGO_FundSchedule_ID@
            setLineNetAmt (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FundScheduleLine (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536530876789L;
    /** Last Updated Timestamp 2009-10-01 15:59:20.0 */
    public static final long updatedMS = 1254405560000L;
    /** AD_Table_ID=1000020 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FundScheduleLine");
        
    }
    ;
    
    /** TableName=AFGO_FundScheduleLine */
    public static final String Table_Name="AFGO_FundScheduleLine";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FundScheduleLine");
    
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
    
    
    /** AFGO_FundLine_ID AD_Reference_ID=1000006 */
    public static final int AFGO_FUNDLINE_ID_AD_Reference_ID=1000006;
    /** Set Fund Line.
    @param AFGO_FundLine_ID A fund line contains a charge, amount and specified set. */
    public void setAFGO_FundLine_ID (int AFGO_FundLine_ID)
    {
        if (AFGO_FundLine_ID < 1) throw new IllegalArgumentException ("AFGO_FundLine_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FundLine_ID", Integer.valueOf(AFGO_FundLine_ID));
        
    }
    
    /** Get Fund Line.
    @return A fund line contains a charge, amount and specified set. */
    public int getAFGO_FundLine_ID() 
    {
        return get_ValueAsInt("AFGO_FundLine_ID");
        
    }
    
    /** Set Fund Schedule Line.
    @param AFGO_FundScheduleLine_ID A fund Schedule Line contains a charge, amount and specified set. */
    public void setAFGO_FundScheduleLine_ID (int AFGO_FundScheduleLine_ID)
    {
        if (AFGO_FundScheduleLine_ID < 1) throw new IllegalArgumentException ("AFGO_FundScheduleLine_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FundScheduleLine_ID", Integer.valueOf(AFGO_FundScheduleLine_ID));
        
    }
    
    /** Get Fund Schedule Line.
    @return A fund Schedule Line contains a charge, amount and specified set. */
    public int getAFGO_FundScheduleLine_ID() 
    {
        return get_ValueAsInt("AFGO_FundScheduleLine_ID");
        
    }
    
    
    /** AFGO_FundSchedule_ID AD_Reference_ID=1000019 */
    public static final int AFGO_FUNDSCHEDULE_ID_AD_Reference_ID=1000019;
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_FundSchedule_ID()));
        
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
    
    
}
