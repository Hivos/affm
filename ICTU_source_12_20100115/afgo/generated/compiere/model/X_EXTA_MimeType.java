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
/** Generated Model for EXTA_MimeType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_EXTA_MimeType.java,v 1.1 2009/02/17 12:50:23 tomassen Exp $ */
public class X_EXTA_MimeType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param EXTA_MimeType_ID id
    @param trxName transaction
    */
    public X_EXTA_MimeType (Ctx ctx, int EXTA_MimeType_ID, String trxName)
    {
        super (ctx, EXTA_MimeType_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (EXTA_MimeType_ID == 0)
        {
            setEXTA_MimeType_ID (0);
            setEntityType (null);	// U
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_EXTA_MimeType (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27514239186789L;
    /** Last Updated Timestamp 2009-01-16 14:51:10.0 */
    public static final long updatedMS = 1232113870000L;
    /** AD_Table_ID=1000071 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("EXTA_MimeType");
        
    }
    ;
    
    /** TableName=EXTA_MimeType */
    public static final String Table_Name="EXTA_MimeType";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"EXTA_MimeType");
    
    /** AccessLevel
    @return 7 - System - Client - Org 
    */
    @Override protected PO.AccessLevel get_AccessLevel()
    {
        return PO.AccessLevel.ALL;
        
    }
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Mime Type.
    @param EXTA_MimeType_ID Mime Type */
    public void setEXTA_MimeType_ID (int EXTA_MimeType_ID)
    {
        if (EXTA_MimeType_ID < 1) throw new IllegalArgumentException ("EXTA_MimeType_ID is mandatory.");
        set_ValueNoCheck ("EXTA_MimeType_ID", Integer.valueOf(EXTA_MimeType_ID));
        
    }
    
    /** Get Mime Type.
    @return Mime Type */
    public int getEXTA_MimeType_ID() 
    {
        return get_ValueAsInt("EXTA_MimeType_ID");
        
    }
    
    
    /** EntityType AD_Reference_ID=389 */
    public static final int ENTITYTYPE_AD_Reference_ID=389;
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
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
