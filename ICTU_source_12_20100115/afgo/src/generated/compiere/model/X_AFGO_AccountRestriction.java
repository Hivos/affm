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
/** Generated Model for AFGO_AccountRestriction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_AccountRestriction.java,v 1.26 2010/01/04 13:30:17 tomassen Exp $ */
public class X_AFGO_AccountRestriction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_AccountRestriction_ID id
    @param trxName transaction
    */
    public X_AFGO_AccountRestriction (Ctx ctx, int AFGO_AccountRestriction_ID, String trxName)
    {
        super (ctx, AFGO_AccountRestriction_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_AccountRestriction_ID == 0)
        {
            setAFGO_AccountRestriction_ID (0);
            setEntityType (null);	// U
            setName (null);
            setWhereClause (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_AccountRestriction (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27524176082789L;
    /** Last Updated Timestamp 2009-05-11 16:06:06.0 */
    public static final long updatedMS = 1242050766000L;
    /** AD_Table_ID=1000056 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_AccountRestriction");
        
    }
    ;
    
    /** TableName=AFGO_AccountRestriction */
    public static final String Table_Name="AFGO_AccountRestriction";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_AccountRestriction");
    
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
    /** Set Account Restriction.
    @param AFGO_AccountRestriction_ID Account Restriction */
    public void setAFGO_AccountRestriction_ID (int AFGO_AccountRestriction_ID)
    {
        if (AFGO_AccountRestriction_ID < 1) throw new IllegalArgumentException ("AFGO_AccountRestriction_ID is mandatory.");
        set_ValueNoCheck ("AFGO_AccountRestriction_ID", Integer.valueOf(AFGO_AccountRestriction_ID));
        
    }
    
    /** Get Account Restriction.
    @return Account Restriction */
    public int getAFGO_AccountRestriction_ID() 
    {
        return get_ValueAsInt("AFGO_AccountRestriction_ID");
        
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
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        if (WhereClause == null) throw new IllegalArgumentException ("WhereClause is mandatory.");
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
