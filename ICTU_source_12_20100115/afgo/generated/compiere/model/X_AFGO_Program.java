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
/** Generated Model for AFGO_Program
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_Program.java,v 1.1 2009/02/17 12:50:22 tomassen Exp $ */
public class X_AFGO_Program extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_Program_ID id
    @param trxName transaction
    */
    public X_AFGO_Program (Ctx ctx, int AFGO_Program_ID, String trxName)
    {
        super (ctx, AFGO_Program_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_Program_ID == 0)
        {
            setAFGO_Calendar_ID (0);
            setAFGO_Program_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setEndDate (new Timestamp(System.currentTimeMillis()));
            setIsDefaultAllocation (true);	// Y
            setName (null);
            setProgramManager_ID (0);
            setProgramPeriodType (null);	// M
            setProgramSecretary_ID (0);
            setStartDate (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_Program (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515207025789L;
    /** Last Updated Timestamp 2009-01-27 19:41:49.0 */
    public static final long updatedMS = 1233081709000L;
    /** AD_Table_ID=1000009 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_Program");
        
    }
    ;
    
    /** TableName=AFGO_Program */
    public static final String Table_Name="AFGO_Program";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_Program");
    
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
    
    /** AFGO_Calendar_ID AD_Reference_ID=1000023 */
    public static final int AFGO_CALENDAR_ID_AD_Reference_ID=1000023;
    /** Set Program Calendar.
    @param AFGO_Calendar_ID A program calender describes the various calender periods.  */
    public void setAFGO_Calendar_ID (int AFGO_Calendar_ID)
    {
        if (AFGO_Calendar_ID < 1) throw new IllegalArgumentException ("AFGO_Calendar_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Calendar_ID", Integer.valueOf(AFGO_Calendar_ID));
        
    }
    
    /** Get Program Calendar.
    @return A program calender describes the various calender periods.  */
    public int getAFGO_Calendar_ID() 
    {
        return get_ValueAsInt("AFGO_Calendar_ID");
        
    }
    
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
    
    
    /** AFGO_ProjectCluster_ID AD_Reference_ID=1000008 */
    public static final int AFGO_PROJECTCLUSTER_ID_AD_Reference_ID=1000008;
    /** Set Project Cluster.
    @param AFGO_ProjectCluster_ID Project cluster is the second highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public void setAFGO_ProjectCluster_ID (int AFGO_ProjectCluster_ID)
    {
        if (AFGO_ProjectCluster_ID <= 0) set_ValueNoCheck ("AFGO_ProjectCluster_ID", null);
        else
        set_ValueNoCheck ("AFGO_ProjectCluster_ID", Integer.valueOf(AFGO_ProjectCluster_ID));
        
    }
    
    /** Get Project Cluster.
    @return Project cluster is the second highest level of the specified set. If no value is required, leave it to the standard or default value defined on the program  level. */
    public int getAFGO_ProjectCluster_ID() 
    {
        return get_ValueAsInt("AFGO_ProjectCluster_ID");
        
    }
    
    
    /** AFGO_ServiceType_ID AD_Reference_ID=1000033 */
    public static final int AFGO_SERVICETYPE_ID_AD_Reference_ID=1000033;
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
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** Set Default Allocation.
    @param IsDefaultAllocation Default Allocation */
    public void setIsDefaultAllocation (boolean IsDefaultAllocation)
    {
        set_Value ("IsDefaultAllocation", Boolean.valueOf(IsDefaultAllocation));
        
    }
    
    /** Get Default Allocation.
    @return Default Allocation */
    public boolean isDefaultAllocation() 
    {
        return get_ValueAsBoolean("IsDefaultAllocation");
        
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
    
    
    /** ProgramManager_ID AD_Reference_ID=316 */
    public static final int PROGRAMMANAGER_ID_AD_Reference_ID=316;
    /** Set Program Manager.
    @param ProgramManager_ID Program Manager */
    public void setProgramManager_ID (int ProgramManager_ID)
    {
        if (ProgramManager_ID < 1) throw new IllegalArgumentException ("ProgramManager_ID is mandatory.");
        set_Value ("ProgramManager_ID", Integer.valueOf(ProgramManager_ID));
        
    }
    
    /** Get Program Manager.
    @return Program Manager */
    public int getProgramManager_ID() 
    {
        return get_ValueAsInt("ProgramManager_ID");
        
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
        if (ProgramPeriodType == null) throw new IllegalArgumentException ("ProgramPeriodType is mandatory");
        if (!isProgramPeriodTypeValid(ProgramPeriodType))
        throw new IllegalArgumentException ("ProgramPeriodType Invalid value - " + ProgramPeriodType + " - Reference_ID=1000019 - M - Q");
        set_ValueNoCheck ("ProgramPeriodType", ProgramPeriodType);
        
    }
    
    /** Get Program Period Type.
    @return Choose either monthly or quarterly to set the standard invoicing and reporting period type */
    public String getProgramPeriodType() 
    {
        return (String)get_Value("ProgramPeriodType");
        
    }
    
    
    /** ProgramSecretary_ID AD_Reference_ID=316 */
    public static final int PROGRAMSECRETARY_ID_AD_Reference_ID=316;
    /** Set Program Secretary.
    @param ProgramSecretary_ID Program Secretary */
    public void setProgramSecretary_ID (int ProgramSecretary_ID)
    {
        if (ProgramSecretary_ID < 1) throw new IllegalArgumentException ("ProgramSecretary_ID is mandatory.");
        set_Value ("ProgramSecretary_ID", Integer.valueOf(ProgramSecretary_ID));
        
    }
    
    /** Get Program Secretary.
    @return Program Secretary */
    public int getProgramSecretary_ID() 
    {
        return get_ValueAsInt("ProgramSecretary_ID");
        
    }
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
