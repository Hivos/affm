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
/** Generated Model for AFGO_FormColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FormColumn.java,v 1.1 2009/11/24 12:01:47 tomassen Exp $ */
public class X_AFGO_FormColumn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FormColumn_ID id
    @param trxName transaction
    */
    public X_AFGO_FormColumn (Ctx ctx, int AFGO_FormColumn_ID, String trxName)
    {
        super (ctx, AFGO_FormColumn_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FormColumn_ID == 0)
        {
            setAD_Reference_ID (0);
            setAFGO_FormColumn_ID (0);
            setAFGO_FormTable_ID (0);
            setColumnSQL (null);
            setIsKey (false);	// N
            setIsReadOnly (true);	// Y
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AFGO_FormColumn WHERE AFGO_FormTable_ID=@AFGO_FormTable_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FormColumn (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27540584517789L;
    /** Last Updated Timestamp 2009-11-17 13:00:01.0 */
    public static final long updatedMS = 1258459201000L;
    /** AD_Table_ID=1000087 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FormColumn");
        
    }
    ;
    
    /** TableName=AFGO_FormColumn */
    public static final String Table_Name="AFGO_FormColumn";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FormColumn");
    
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
    /** Set System Element.
    @param AD_Element_ID System Element enables the central maintenance of column description and help. */
    public void setAD_Element_ID (int AD_Element_ID)
    {
        if (AD_Element_ID <= 0) set_Value ("AD_Element_ID", null);
        else
        set_Value ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
        
    }
    
    /** Get System Element.
    @return System Element enables the central maintenance of column description and help. */
    public int getAD_Element_ID() 
    {
        return get_ValueAsInt("AD_Element_ID");
        
    }
    
    
    /** AD_Reference_ID AD_Reference_ID=1 */
    public static final int AD_REFERENCE_ID_AD_Reference_ID=1;
    /** Set Reference.
    @param AD_Reference_ID System Reference and Validation */
    public void setAD_Reference_ID (int AD_Reference_ID)
    {
        if (AD_Reference_ID < 1) throw new IllegalArgumentException ("AD_Reference_ID is mandatory.");
        set_Value ("AD_Reference_ID", Integer.valueOf(AD_Reference_ID));
        
    }
    
    /** Get Reference.
    @return System Reference and Validation */
    public int getAD_Reference_ID() 
    {
        return get_ValueAsInt("AD_Reference_ID");
        
    }
    
    /** Set Form Column.
    @param AFGO_FormColumn_ID Form Column */
    public void setAFGO_FormColumn_ID (int AFGO_FormColumn_ID)
    {
        if (AFGO_FormColumn_ID < 1) throw new IllegalArgumentException ("AFGO_FormColumn_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FormColumn_ID", Integer.valueOf(AFGO_FormColumn_ID));
        
    }
    
    /** Get Form Column.
    @return Form Column */
    public int getAFGO_FormColumn_ID() 
    {
        return get_ValueAsInt("AFGO_FormColumn_ID");
        
    }
    
    
    /** AFGO_FormTable_ID AD_Reference_ID=1000057 */
    public static final int AFGO_FORMTABLE_ID_AD_Reference_ID=1000057;
    /** Set Form Table.
    @param AFGO_FormTable_ID Form Table */
    public void setAFGO_FormTable_ID (int AFGO_FormTable_ID)
    {
        if (AFGO_FormTable_ID < 1) throw new IllegalArgumentException ("AFGO_FormTable_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FormTable_ID", Integer.valueOf(AFGO_FormTable_ID));
        
    }
    
    /** Get Form Table.
    @return Form Table */
    public int getAFGO_FormTable_ID() 
    {
        return get_ValueAsInt("AFGO_FormTable_ID");
        
    }
    
    /** Set DB Column Name.
    @param ColumnName Name of the column in the database */
    public void setColumnName (String ColumnName)
    {
        set_Value ("ColumnName", ColumnName);
        
    }
    
    /** Get DB Column Name.
    @return Name of the column in the database */
    public String getColumnName() 
    {
        return (String)get_Value("ColumnName");
        
    }
    
    /** Set Column SQL.
    @param ColumnSQL Virtual Column (r/o) */
    public void setColumnSQL (String ColumnSQL)
    {
        if (ColumnSQL == null) throw new IllegalArgumentException ("ColumnSQL is mandatory.");
        set_Value ("ColumnSQL", ColumnSQL);
        
    }
    
    /** Get Column SQL.
    @return Virtual Column (r/o) */
    public String getColumnSQL() 
    {
        return (String)get_Value("ColumnSQL");
        
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
    
    /** Set Key column.
    @param IsKey This column is the key in this table */
    public void setIsKey (boolean IsKey)
    {
        set_Value ("IsKey", Boolean.valueOf(IsKey));
        
    }
    
    /** Get Key column.
    @return This column is the key in this table */
    public boolean isKey() 
    {
        return get_ValueAsBoolean("IsKey");
        
    }
    
    /** Set Read Only.
    @param IsReadOnly Field is read only */
    public void setIsReadOnly (boolean IsReadOnly)
    {
        set_Value ("IsReadOnly", Boolean.valueOf(IsReadOnly));
        
    }
    
    /** Get Read Only.
    @return Field is read only */
    public boolean isReadOnly() 
    {
        return get_ValueAsBoolean("IsReadOnly");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
