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
/** Generated Model for AFGO_Year
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_Year.java,v 1.1 2009/02/17 12:50:22 tomassen Exp $ */
public class X_AFGO_Year extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_Year_ID id
    @param trxName transaction
    */
    public X_AFGO_Year (Ctx ctx, int AFGO_Year_ID, String trxName)
    {
        super (ctx, AFGO_Year_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_Year_ID == 0)
        {
            setAFGO_Calendar_ID (0);
            setAFGO_Year_ID (0);
            setEndDate (new Timestamp(System.currentTimeMillis()));
            setName (null);
            setStartDate (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_Year (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515207025789L;
    /** Last Updated Timestamp 2009-01-27 19:41:49.0 */
    public static final long updatedMS = 1233081709000L;
    /** AD_Table_ID=1000031 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_Year");
        
    }
    ;
    
    /** TableName=AFGO_Year */
    public static final String Table_Name="AFGO_Year";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_Year");
    
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
        set_Value ("AFGO_Calendar_ID", Integer.valueOf(AFGO_Calendar_ID));
        
    }
    
    /** Get Program Calendar.
    @return A program calender describes the various calender periods.  */
    public int getAFGO_Calendar_ID() 
    {
        return get_ValueAsInt("AFGO_Calendar_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_Calendar_ID()));
        
    }
    
    
    /** AFGO_Quarter_ID AD_Reference_ID=1000022 */
    public static final int AFGO_QUARTER_ID_AD_Reference_ID=1000022;
    /** Set Quarter.
    @param AFGO_Quarter_ID A valid program quarter within a program year. If not applicable choose standard. */
    public void setAFGO_Quarter_ID (int AFGO_Quarter_ID)
    {
        if (AFGO_Quarter_ID <= 0) set_ValueNoCheck ("AFGO_Quarter_ID", null);
        else
        set_ValueNoCheck ("AFGO_Quarter_ID", Integer.valueOf(AFGO_Quarter_ID));
        
    }
    
    /** Get Quarter.
    @return A valid program quarter within a program year. If not applicable choose standard. */
    public int getAFGO_Quarter_ID() 
    {
        return get_ValueAsInt("AFGO_Quarter_ID");
        
    }
    
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
        set_ValueNoCheck ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
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
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
        set_ValueNoCheck ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
