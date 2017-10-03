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
/** Generated Model for AFGO_WorkflowRoleAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_WorkflowRoleAssignment.java,v 1.24 2009/11/24 12:01:49 tomassen Exp $ */
public class X_AFGO_WorkflowRoleAssignment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_WorkflowRoleAssignment_ID id
    @param trxName transaction
    */
    public X_AFGO_WorkflowRoleAssignment (Ctx ctx, int AFGO_WorkflowRoleAssignment_ID, String trxName)
    {
        super (ctx, AFGO_WorkflowRoleAssignment_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_WorkflowRoleAssignment_ID == 0)
        {
            setAD_User_ID (0);
            setAFGO_WorkflowRoleAssignment_ID (0);
            setAFGO_WorkflowRole_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_WorkflowRoleAssignment (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554753789L;
    /** Last Updated Timestamp 2009-03-30 18:10:37.0 */
    public static final long updatedMS = 1238429437000L;
    /** AD_Table_ID=1000034 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_WorkflowRoleAssignment");
        
    }
    ;
    
    /** TableName=AFGO_WorkflowRoleAssignment */
    public static final String Table_Name="AFGO_WorkflowRoleAssignment";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_WorkflowRoleAssignment");
    
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
    
    /** AD_User_ID AD_Reference_ID=286 */
    public static final int AD_USER_ID_AD_Reference_ID=286;
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_Activity_ID()));
        
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
    
    
    /** AFGO_Program_ID AD_Reference_ID=1000000 */
    public static final int AFGO_PROGRAM_ID_AD_Reference_ID=1000000;
    /** Set Program.
    @param AFGO_Program_ID Program is the highest level of the specified set. You can have multiple programs in one organisation. */
    public void setAFGO_Program_ID (int AFGO_Program_ID)
    {
        if (AFGO_Program_ID <= 0) set_Value ("AFGO_Program_ID", null);
        else
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
    
    /** Set AFGO_WorkflowRoleAssignment.
    @param AFGO_WorkflowRoleAssignment_ID AFGO_WorkflowRoleAssignment */
    public void setAFGO_WorkflowRoleAssignment_ID (int AFGO_WorkflowRoleAssignment_ID)
    {
        if (AFGO_WorkflowRoleAssignment_ID < 1) throw new IllegalArgumentException ("AFGO_WorkflowRoleAssignment_ID is mandatory.");
        set_ValueNoCheck ("AFGO_WorkflowRoleAssignment_ID", Integer.valueOf(AFGO_WorkflowRoleAssignment_ID));
        
    }
    
    /** Get AFGO_WorkflowRoleAssignment.
    @return AFGO_WorkflowRoleAssignment */
    public int getAFGO_WorkflowRoleAssignment_ID() 
    {
        return get_ValueAsInt("AFGO_WorkflowRoleAssignment_ID");
        
    }
    
    
    /** AFGO_WorkflowRole_ID AD_Reference_ID=1000034 */
    public static final int AFGO_WORKFLOWROLE_ID_AD_Reference_ID=1000034;
    /** Set Workflow Role.
    @param AFGO_WorkflowRole_ID Workflow Role */
    public void setAFGO_WorkflowRole_ID (int AFGO_WorkflowRole_ID)
    {
        if (AFGO_WorkflowRole_ID < 1) throw new IllegalArgumentException ("AFGO_WorkflowRole_ID is mandatory.");
        set_ValueNoCheck ("AFGO_WorkflowRole_ID", Integer.valueOf(AFGO_WorkflowRole_ID));
        
    }
    
    /** Get Workflow Role.
    @return Workflow Role */
    public int getAFGO_WorkflowRole_ID() 
    {
        return get_ValueAsInt("AFGO_WorkflowRole_ID");
        
    }
    
    
}
