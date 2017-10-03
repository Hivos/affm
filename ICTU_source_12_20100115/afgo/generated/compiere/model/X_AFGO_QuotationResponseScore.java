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
/** Generated Model for AFGO_QuotationResponseScore
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_QuotationResponseScore.java,v 1.1 2009/02/17 12:50:23 tomassen Exp $ */
public class X_AFGO_QuotationResponseScore extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_QuotationResponseScore_ID id
    @param trxName transaction
    */
    public X_AFGO_QuotationResponseScore (Ctx ctx, int AFGO_QuotationResponseScore_ID, String trxName)
    {
        super (ctx, AFGO_QuotationResponseScore_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_QuotationResponseScore_ID == 0)
        {
            setAFGO_QuotationResponseScore_ID (0);
            setAFGO_QuotationResponse_ID (0);
            setName (null);
            setValueNumber (0);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_QuotationResponseScore (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27515207025789L;
    /** Last Updated Timestamp 2009-01-27 19:41:49.0 */
    public static final long updatedMS = 1233081709000L;
    /** AD_Table_ID=1000028 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_QuotationResponseScore");
        
    }
    ;
    
    /** TableName=AFGO_QuotationResponseScore */
    public static final String Table_Name="AFGO_QuotationResponseScore";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_QuotationResponseScore");
    
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
    /** Set Quotation Response Score.
    @param AFGO_QuotationResponseScore_ID Not yet implemented */
    public void setAFGO_QuotationResponseScore_ID (int AFGO_QuotationResponseScore_ID)
    {
        if (AFGO_QuotationResponseScore_ID < 1) throw new IllegalArgumentException ("AFGO_QuotationResponseScore_ID is mandatory.");
        set_ValueNoCheck ("AFGO_QuotationResponseScore_ID", Integer.valueOf(AFGO_QuotationResponseScore_ID));
        
    }
    
    /** Get Quotation Response Score.
    @return Not yet implemented */
    public int getAFGO_QuotationResponseScore_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationResponseScore_ID");
        
    }
    
    
    /** AFGO_QuotationResponse_ID AD_Reference_ID=1000017 */
    public static final int AFGO_QUOTATIONRESPONSE_ID_AD_Reference_ID=1000017;
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
    
    /** Set Value.
    @param ValueNumber Numeric Value */
    public void setValueNumber (int ValueNumber)
    {
        set_Value ("ValueNumber", Integer.valueOf(ValueNumber));
        
    }
    
    /** Get Value.
    @return Numeric Value */
    public int getValueNumber() 
    {
        return get_ValueAsInt("ValueNumber");
        
    }
    
    
}
