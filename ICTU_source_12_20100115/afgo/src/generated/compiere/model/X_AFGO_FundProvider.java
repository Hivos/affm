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
/** Generated Model for AFGO_FundProvider
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FundProvider.java,v 1.69 2010/01/04 13:30:18 tomassen Exp $ */
public class X_AFGO_FundProvider extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FundProvider_ID id
    @param trxName transaction
    */
    public X_AFGO_FundProvider (Ctx ctx, int AFGO_FundProvider_ID, String trxName)
    {
        super (ctx, AFGO_FundProvider_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FundProvider_ID == 0)
        {
            setAD_User_ID (0);
            setAFGO_FundProvider_ID (0);
            setAFGO_Program_ID (0);
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setM_PriceList_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FundProvider (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520548626789L;
    /** Last Updated Timestamp 2009-03-30 16:28:30.0 */
    public static final long updatedMS = 1238423310000L;
    /** AD_Table_ID=1000007 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FundProvider");
        
    }
    ;
    
    /** TableName=AFGO_FundProvider */
    public static final String Table_Name="AFGO_FundProvider";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FundProvider");
    
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
    
    /** AD_User_ID AD_Reference_ID=110 */
    public static final int AD_USER_ID_AD_Reference_ID=110;
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Fund Provider.
    @param AFGO_FundProvider_ID A fund provider is a business partner that qualifies as a fund provider for a certain program. */
    public void setAFGO_FundProvider_ID (int AFGO_FundProvider_ID)
    {
        if (AFGO_FundProvider_ID < 1) throw new IllegalArgumentException ("AFGO_FundProvider_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FundProvider_ID", Integer.valueOf(AFGO_FundProvider_ID));
        
    }
    
    /** Get Fund Provider.
    @return A fund provider is a business partner that qualifies as a fund provider for a certain program. */
    public int getAFGO_FundProvider_ID() 
    {
        return get_ValueAsInt("AFGO_FundProvider_ID");
        
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
    
    
    /** C_BPartner_ID AD_Reference_ID=138 */
    public static final int C_BPARTNER_ID_AD_Reference_ID=138;
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    
    /** C_BPartner_Location_ID AD_Reference_ID=159 */
    public static final int C_BPARTNER_LOCATION_ID_AD_Reference_ID=159;
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
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
    
    
    /** M_PriceList_ID AD_Reference_ID=166 */
    public static final int M_PRICELIST_ID_AD_Reference_ID=166;
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
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
