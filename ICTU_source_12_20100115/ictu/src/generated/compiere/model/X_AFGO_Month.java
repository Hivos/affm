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
/** Generated Model for AFGO_Month
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_Month.java,v 1.35 2009/11/24 12:01:48 tomassen Exp $ */
public class X_AFGO_Month extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_Month_ID id
    @param trxName transaction
    */
    public X_AFGO_Month (Ctx ctx, int AFGO_Month_ID, String trxName)
    {
        super (ctx, AFGO_Month_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_Month_ID == 0)
        {
            setAFGO_Month_ID (0);
            setAFGO_Quarter_ID (0);
            setIsClosed (null);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_Month (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554665789L;
    /** Last Updated Timestamp 2009-03-30 18:09:09.0 */
    public static final long updatedMS = 1238429349000L;
    /** AD_Table_ID=1000024 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_Month");
        
    }
    ;
    
    /** TableName=AFGO_Month */
    public static final String Table_Name="AFGO_Month";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_Month");
    
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
    /** Set Month.
    @param AFGO_Month_ID A valid program month within a program quarter. If not applicable choose standard. */
    public void setAFGO_Month_ID (int AFGO_Month_ID)
    {
        if (AFGO_Month_ID < 1) throw new IllegalArgumentException ("AFGO_Month_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Month_ID", Integer.valueOf(AFGO_Month_ID));
        
    }
    
    /** Get Month.
    @return A valid program month within a program quarter. If not applicable choose standard. */
    public int getAFGO_Month_ID() 
    {
        return get_ValueAsInt("AFGO_Month_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_Quarter_ID()));
        
    }
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_ValueNoCheck ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    
    /** IsClosed AD_Reference_ID=1000040 */
    public static final int ISCLOSED_AD_Reference_ID=1000040;
    /** Close Period = N */
    public static final String ISCLOSED_ClosePeriod = X_Ref_AFGO_DocumentPeriodOpenClosed.CLOSE_PERIOD.getValue();
    /** Open Period = Y */
    public static final String ISCLOSED_OpenPeriod = X_Ref_AFGO_DocumentPeriodOpenClosed.OPEN_PERIOD.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isIsClosedValid(String test)
    {
         return X_Ref_AFGO_DocumentPeriodOpenClosed.isValid(test);
         
    }
    /** Set Closed Status.
    @param IsClosed The status is closed */
    public void setIsClosed (String IsClosed)
    {
        if (IsClosed == null) throw new IllegalArgumentException ("IsClosed is mandatory");
        if (!isIsClosedValid(IsClosed))
        throw new IllegalArgumentException ("IsClosed Invalid value - " + IsClosed + " - Reference_ID=1000040 - N - Y");
        set_Value ("IsClosed", IsClosed);
        
    }
    
    /** Get Closed Status.
    @return The status is closed */
    public String getIsClosed() 
    {
        return (String)get_Value("IsClosed");
        
    }
    
    /** Set Period Open.
    @param IsPeriodOpen Period Open */
    public void setIsPeriodOpen (boolean IsPeriodOpen)
    {
        throw new IllegalArgumentException ("IsPeriodOpen is virtual column");
        
    }
    
    /** Get Period Open.
    @return Period Open */
    public boolean isPeriodOpen() 
    {
        return get_ValueAsBoolean("IsPeriodOpen");
        
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
        set_ValueNoCheck ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
