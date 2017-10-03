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
/** Generated Model for AFGO_Calendar
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_Calendar.java,v 1.3 2009/11/24 14:17:56 tomassen Exp $ */
public class X_AFGO_Calendar extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_Calendar_ID id
    @param trxName transaction
    */
    public X_AFGO_Calendar (Ctx ctx, int AFGO_Calendar_ID, String trxName)
    {
        super (ctx, AFGO_Calendar_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_Calendar_ID == 0)
        {
            setAFGO_Calendar_ID (0);
            setIsAllowStandardMonth (false);	// N
            setIsAllowStandardQuarter (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_Calendar (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515207025789L;
    /** Last Updated Timestamp 2009-01-27 19:41:49.0 */
    public static final long updatedMS = 1233081709000L;
    /** AD_Table_ID=1000025 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_Calendar");
        
    }
    ;
    
    /** TableName=AFGO_Calendar */
    public static final String Table_Name="AFGO_Calendar";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_Calendar");
    
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
    
    /** Set Allow Standard Month.
    @param IsAllowStandardMonth Allow Standard Month */
    public void setIsAllowStandardMonth (boolean IsAllowStandardMonth)
    {
        set_Value ("IsAllowStandardMonth", Boolean.valueOf(IsAllowStandardMonth));
        
    }
    
    /** Get Allow Standard Month.
    @return Allow Standard Month */
    public boolean isAllowStandardMonth() 
    {
        return get_ValueAsBoolean("IsAllowStandardMonth");
        
    }
    
    /** Set Allow Standard Quarter.
    @param IsAllowStandardQuarter Allow Standard Quarter */
    public void setIsAllowStandardQuarter (boolean IsAllowStandardQuarter)
    {
        set_Value ("IsAllowStandardQuarter", Boolean.valueOf(IsAllowStandardQuarter));
        
    }
    
    /** Get Allow Standard Quarter.
    @return Allow Standard Quarter */
    public boolean isAllowStandardQuarter() 
    {
        return get_ValueAsBoolean("IsAllowStandardQuarter");
        
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
