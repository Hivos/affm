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
/** Generated Model for AFGO_ProjectCluster
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_ProjectCluster.java,v 1.3 2009/11/24 14:17:54 tomassen Exp $ */
public class X_AFGO_ProjectCluster extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_ProjectCluster_ID id
    @param trxName transaction
    */
    public X_AFGO_ProjectCluster (Ctx ctx, int AFGO_ProjectCluster_ID, String trxName)
    {
        super (ctx, AFGO_ProjectCluster_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_ProjectCluster_ID == 0)
        {
            setAFGO_Program_ID (0);
            setAFGO_ProjectCluster_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_ProjectCluster (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520550191789L;
    /** Last Updated Timestamp 2009-03-30 16:54:35.0 */
    public static final long updatedMS = 1238424875000L;
    /** AD_Table_ID=1000008 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_ProjectCluster");
        
    }
    ;
    
    /** TableName=AFGO_ProjectCluster */
    public static final String Table_Name="AFGO_ProjectCluster";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_ProjectCluster");
    
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_Program_ID()));
        
    }
    
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
        if (AFGO_Project_ID <= 0) set_ValueNoCheck ("AFGO_Project_ID", null);
        else
        set_ValueNoCheck ("AFGO_Project_ID", Integer.valueOf(AFGO_Project_ID));
        
    }
    
    /** Get Project.
    @return Project is the third highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public int getAFGO_Project_ID() 
    {
        return get_ValueAsInt("AFGO_Project_ID");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    
}
