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
/** Generated Model for AFGO_PurchaseDomain
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_PurchaseDomain.java,v 1.69 2010/01/04 13:30:17 tomassen Exp $ */
public class X_AFGO_PurchaseDomain extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_PurchaseDomain_ID id
    @param trxName transaction
    */
    public X_AFGO_PurchaseDomain (Ctx ctx, int AFGO_PurchaseDomain_ID, String trxName)
    {
        super (ctx, AFGO_PurchaseDomain_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_PurchaseDomain_ID == 0)
        {
            setAFGO_PurchaseDomain_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_PurchaseDomain (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554702789L;
    /** Last Updated Timestamp 2009-03-30 18:09:46.0 */
    public static final long updatedMS = 1238429386000L;
    /** AD_Table_ID=1000012 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_PurchaseDomain");
        
    }
    ;
    
    /** TableName=AFGO_PurchaseDomain */
    public static final String Table_Name="AFGO_PurchaseDomain";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_PurchaseDomain");
    
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
