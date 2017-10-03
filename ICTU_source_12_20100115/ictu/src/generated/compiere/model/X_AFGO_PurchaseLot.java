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
/** Generated Model for AFGO_PurchaseLot
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_PurchaseLot.java,v 1.35 2009/11/24 12:01:49 tomassen Exp $ */
public class X_AFGO_PurchaseLot extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_PurchaseLot_ID id
    @param trxName transaction
    */
    public X_AFGO_PurchaseLot (Ctx ctx, int AFGO_PurchaseLot_ID, String trxName)
    {
        super (ctx, AFGO_PurchaseLot_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_PurchaseLot_ID == 0)
        {
            setAFGO_PurchaseDomain_ID (0);
            setAFGO_PurchaseLot_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_PurchaseLot (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554705789L;
    /** Last Updated Timestamp 2009-03-30 18:09:49.0 */
    public static final long updatedMS = 1238429389000L;
    /** AD_Table_ID=1000013 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_PurchaseLot");
        
    }
    ;
    
    /** TableName=AFGO_PurchaseLot */
    public static final String Table_Name="AFGO_PurchaseLot";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_PurchaseLot");
    
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
    
    /** AFGO_PurchaseDomain_ID AD_Reference_ID=1000012 */
    public static final int AFGO_PURCHASEDOMAIN_ID_AD_Reference_ID=1000012;
    /** Set Purchase Domain.
    @param AFGO_PurchaseDomain_ID Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public void setAFGO_PurchaseDomain_ID (int AFGO_PurchaseDomain_ID)
    {
        if (AFGO_PurchaseDomain_ID < 1) throw new IllegalArgumentException ("AFGO_PurchaseDomain_ID is mandatory.");
        set_ValueNoCheck ("AFGO_PurchaseDomain_ID", Integer.valueOf(AFGO_PurchaseDomain_ID));
        
    }
    
    /** Get Purchase Domain.
    @return Purchasing is managed by identifying multiple domains, lots and items. A domain has a name (i.e. ICT). */
    public int getAFGO_PurchaseDomain_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseDomain_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_PurchaseDomain_ID()));
        
    }
    
    /** Set Purchase Lot.
    @param AFGO_PurchaseLot_ID A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public void setAFGO_PurchaseLot_ID (int AFGO_PurchaseLot_ID)
    {
        if (AFGO_PurchaseLot_ID < 1) throw new IllegalArgumentException ("AFGO_PurchaseLot_ID is mandatory.");
        set_ValueNoCheck ("AFGO_PurchaseLot_ID", Integer.valueOf(AFGO_PurchaseLot_ID));
        
    }
    
    /** Get Purchase Lot.
    @return A lot belongs to a domain and also has a name. There can be mutliple lots in 1 domain. */
    public int getAFGO_PurchaseLot_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseLot_ID");
        
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
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    
}
