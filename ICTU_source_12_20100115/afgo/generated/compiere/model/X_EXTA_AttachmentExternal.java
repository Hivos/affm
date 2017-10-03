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
/** Generated Model for EXTA_AttachmentExternal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_EXTA_AttachmentExternal.java,v 1.1 2009/02/17 12:50:21 tomassen Exp $ */
public class X_EXTA_AttachmentExternal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param EXTA_AttachmentExternal_ID id
    @param trxName transaction
    */
    public X_EXTA_AttachmentExternal (Ctx ctx, int EXTA_AttachmentExternal_ID, String trxName)
    {
        super (ctx, EXTA_AttachmentExternal_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (EXTA_AttachmentExternal_ID == 0)
        {
            setEXTA_AttachmentExternal_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_EXTA_AttachmentExternal (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27514239100789L;
    /** Last Updated Timestamp 2009-01-16 14:49:44.0 */
    public static final long updatedMS = 1232113784000L;
    /** AD_Table_ID=1000072 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("EXTA_AttachmentExternal");
        
    }
    ;
    
    /** TableName=EXTA_AttachmentExternal */
    public static final String Table_Name="EXTA_AttachmentExternal";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"EXTA_AttachmentExternal");
    
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
    
    /** AD_Table_ID AD_Reference_ID=415 */
    public static final int AD_TABLE_ID_AD_Reference_ID=415;
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Attachment External.
    @param EXTA_AttachmentExternal_ID Attachment External */
    public void setEXTA_AttachmentExternal_ID (int EXTA_AttachmentExternal_ID)
    {
        if (EXTA_AttachmentExternal_ID < 1) throw new IllegalArgumentException ("EXTA_AttachmentExternal_ID is mandatory.");
        set_ValueNoCheck ("EXTA_AttachmentExternal_ID", Integer.valueOf(EXTA_AttachmentExternal_ID));
        
    }
    
    /** Get Attachment External.
    @return Attachment External */
    public int getEXTA_AttachmentExternal_ID() 
    {
        return get_ValueAsInt("EXTA_AttachmentExternal_ID");
        
    }
    
    
    /** EXTA_MimeType_ID AD_Reference_ID=1000054 */
    public static final int EXTA_MIMETYPE_ID_AD_Reference_ID=1000054;
    /** Set Mime Type.
    @param EXTA_MimeType_ID Mime Type */
    public void setEXTA_MimeType_ID (int EXTA_MimeType_ID)
    {
        if (EXTA_MimeType_ID <= 0) set_Value ("EXTA_MimeType_ID", null);
        else
        set_Value ("EXTA_MimeType_ID", Integer.valueOf(EXTA_MimeType_ID));
        
    }
    
    /** Get Mime Type.
    @return Mime Type */
    public int getEXTA_MimeType_ID() 
    {
        return get_ValueAsInt("EXTA_MimeType_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_Value ("Record_ID", null);
        else
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        set_Value ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
    }
    
    
}
