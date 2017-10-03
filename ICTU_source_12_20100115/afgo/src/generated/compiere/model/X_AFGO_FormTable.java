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
/** Generated Model for AFGO_FormTable
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FormTable.java,v 1.5 2010/01/04 13:30:18 tomassen Exp $ */
public class X_AFGO_FormTable extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FormTable_ID id
    @param trxName transaction
    */
    public X_AFGO_FormTable (Ctx ctx, int AFGO_FormTable_ID, String trxName)
    {
        super (ctx, AFGO_FormTable_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FormTable_ID == 0)
        {
            setAFGO_FormTable_ID (0);
            setAFGO_Form_ID (0);
            setFromClause (null);
            setIsMultiSelection (false);	// N
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AFGO_FormTable WHERE AFGO_Form_ID=@AFGO_Form_ID@
            setTableName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FormTable (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27540506810789L;
    /** Last Updated Timestamp 2009-11-16 15:24:54.0 */
    public static final long updatedMS = 1258381494000L;
    /** AD_Table_ID=1000086 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FormTable");
        
    }
    ;
    
    /** TableName=AFGO_FormTable */
    public static final String Table_Name="AFGO_FormTable";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FormTable");
    
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
    
    
    /** AFGO_Form_ID AD_Reference_ID=1000056 */
    public static final int AFGO_FORM_ID_AD_Reference_ID=1000056;
    /** Set Table Based Form.
    @param AFGO_Form_ID Table Based Form */
    public void setAFGO_Form_ID (int AFGO_Form_ID)
    {
        if (AFGO_Form_ID < 1) throw new IllegalArgumentException ("AFGO_Form_ID is mandatory.");
        set_ValueNoCheck ("AFGO_Form_ID", Integer.valueOf(AFGO_Form_ID));
        
    }
    
    /** Get Table Based Form.
    @return Table Based Form */
    public int getAFGO_Form_ID() 
    {
        return get_ValueAsInt("AFGO_Form_ID");
        
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
    
    /** Set Sql FROM.
    @param FromClause SQL FROM clause */
    public void setFromClause (String FromClause)
    {
        if (FromClause == null) throw new IllegalArgumentException ("FromClause is mandatory.");
        set_Value ("FromClause", FromClause);
        
    }
    
    /** Get Sql FROM.
    @return SQL FROM clause */
    public String getFromClause() 
    {
        return (String)get_Value("FromClause");
        
    }
    
    /** Set Multi Selection.
    @param IsMultiSelection Multi Selection */
    public void setIsMultiSelection (boolean IsMultiSelection)
    {
        set_Value ("IsMultiSelection", Boolean.valueOf(IsMultiSelection));
        
    }
    
    /** Get Multi Selection.
    @return Multi Selection */
    public boolean isMultiSelection() 
    {
        return get_ValueAsBoolean("IsMultiSelection");
        
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
    
    /** Set DB Table Name.
    @param TableName Name of the table in the database */
    public void setTableName (String TableName)
    {
        if (TableName == null) throw new IllegalArgumentException ("TableName is mandatory.");
        set_Value ("TableName", TableName);
        
    }
    
    /** Get DB Table Name.
    @return Name of the table in the database */
    public String getTableName() 
    {
        return (String)get_Value("TableName");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
