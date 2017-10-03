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
/** Generated Model for AFGO_LotItem
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_LotItem.java,v 1.3 2009/11/24 14:17:55 tomassen Exp $ */
public class X_AFGO_LotItem extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_LotItem_ID id
    @param trxName transaction
    */
    public X_AFGO_LotItem (Ctx ctx, int AFGO_LotItem_ID, String trxName)
    {
        super (ctx, AFGO_LotItem_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_LotItem_ID == 0)
        {
            setAFGO_LotItem_ID (0);
            setAFGO_PurchaseLot_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_LotItem (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27522700810789L;
    /** Last Updated Timestamp 2009-04-24 14:18:14.0 */
    public static final long updatedMS = 1240575494000L;
    /** AD_Table_ID=1000015 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_LotItem");
        
    }
    ;
    
    /** TableName=AFGO_LotItem */
    public static final String Table_Name="AFGO_LotItem";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_LotItem");
    
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
    /** Set Lot Item.
    @param AFGO_LotItem_ID A lot item identifies a specific product or charge within a lot. */
    public void setAFGO_LotItem_ID (int AFGO_LotItem_ID)
    {
        if (AFGO_LotItem_ID < 1) throw new IllegalArgumentException ("AFGO_LotItem_ID is mandatory.");
        set_ValueNoCheck ("AFGO_LotItem_ID", Integer.valueOf(AFGO_LotItem_ID));
        
    }
    
    /** Get Lot Item.
    @return A lot item identifies a specific product or charge within a lot. */
    public int getAFGO_LotItem_ID() 
    {
        return get_ValueAsInt("AFGO_LotItem_ID");
        
    }
    
    
    /** AFGO_PurchaseLot_ID AD_Reference_ID=1000013 */
    public static final int AFGO_PURCHASELOT_ID_AD_Reference_ID=1000013;
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_PurchaseLot_ID()));
        
    }
    
    
    /** C_Charge_ID AD_Reference_ID=200 */
    public static final int C_CHARGE_ID_AD_Reference_ID=200;
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_ValueNoCheck ("C_Charge_ID", null);
        else
        set_ValueNoCheck ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
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
    
    
    /** M_Product_ID AD_Reference_ID=162 */
    public static final int M_PRODUCT_ID_AD_Reference_ID=162;
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
