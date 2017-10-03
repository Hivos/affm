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
/** Generated Model for AFGO_PurchaseCommitmentType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_PurchaseCommitmentType.java,v 1.4 2009/11/24 12:01:48 tomassen Exp $ */
public class X_AFGO_PurchaseCommitmentType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_PurchaseCommitmentType_ID id
    @param trxName transaction
    */
    public X_AFGO_PurchaseCommitmentType (Ctx ctx, int AFGO_PurchaseCommitmentType_ID, String trxName)
    {
        super (ctx, AFGO_PurchaseCommitmentType_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_PurchaseCommitmentType_ID == 0)
        {
            setAFGO_PurchaseCommitmentType_ID (0);
            setIsAdditionalCommitment (false);	// N
            setIsMasterCommitment (false);	// N
            setIsPurchaseDomain (false);	// N
            setIsQuotationRequired (false);	// N
            setIsSecondmentCommitment (false);	// N
            setIsTransferCommitment (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_PurchaseCommitmentType (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27540664729789L;
    /** Last Updated Timestamp 2009-11-18 11:16:53.0 */
    public static final long updatedMS = 1258539413000L;
    /** AD_Table_ID=1000079 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_PurchaseCommitmentType");
        
    }
    ;
    
    /** TableName=AFGO_PurchaseCommitmentType */
    public static final String Table_Name="AFGO_PurchaseCommitmentType";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_PurchaseCommitmentType");
    
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
    /** Set Purchase Commitment Type.
    @param AFGO_PurchaseCommitmentType_ID Purchase Commitment Type */
    public void setAFGO_PurchaseCommitmentType_ID (int AFGO_PurchaseCommitmentType_ID)
    {
        if (AFGO_PurchaseCommitmentType_ID < 1) throw new IllegalArgumentException ("AFGO_PurchaseCommitmentType_ID is mandatory.");
        set_ValueNoCheck ("AFGO_PurchaseCommitmentType_ID", Integer.valueOf(AFGO_PurchaseCommitmentType_ID));
        
    }
    
    /** Get Purchase Commitment Type.
    @return Purchase Commitment Type */
    public int getAFGO_PurchaseCommitmentType_ID() 
    {
        return get_ValueAsInt("AFGO_PurchaseCommitmentType_ID");
        
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
    
    /** Set Additional Commitment.
    @param IsAdditionalCommitment Additional Commitment */
    public void setIsAdditionalCommitment (boolean IsAdditionalCommitment)
    {
        set_Value ("IsAdditionalCommitment", Boolean.valueOf(IsAdditionalCommitment));
        
    }
    
    /** Get Additional Commitment.
    @return Additional Commitment */
    public boolean isAdditionalCommitment() 
    {
        return get_ValueAsBoolean("IsAdditionalCommitment");
        
    }
    
    /** Set Can Have Additional.
    @param IsCanHaveAdditional Can Have Additional */
    public void setIsCanHaveAdditional (boolean IsCanHaveAdditional)
    {
        set_Value ("IsCanHaveAdditional", Boolean.valueOf(IsCanHaveAdditional));
        
    }
    
    /** Get Can Have Additional.
    @return Can Have Additional */
    public boolean isCanHaveAdditional() 
    {
        return get_ValueAsBoolean("IsCanHaveAdditional");
        
    }
    
    /** Set Can Transfer.
    @param IsCanTransfer Can Transfer */
    public void setIsCanTransfer (boolean IsCanTransfer)
    {
        set_Value ("IsCanTransfer", Boolean.valueOf(IsCanTransfer));
        
    }
    
    /** Get Can Transfer.
    @return Can Transfer */
    public boolean isCanTransfer() 
    {
        return get_ValueAsBoolean("IsCanTransfer");
        
    }
    
    /** Set Disable Manual Creation.
    @param IsDisableManualCreation Disable Manual Creation */
    public void setIsDisableManualCreation (boolean IsDisableManualCreation)
    {
        set_Value ("IsDisableManualCreation", Boolean.valueOf(IsDisableManualCreation));
        
    }
    
    /** Get Disable Manual Creation.
    @return Disable Manual Creation */
    public boolean isDisableManualCreation() 
    {
        return get_ValueAsBoolean("IsDisableManualCreation");
        
    }
    
    /** Set Master Commitment.
    @param IsMasterCommitment Master Commitment */
    public void setIsMasterCommitment (boolean IsMasterCommitment)
    {
        set_Value ("IsMasterCommitment", Boolean.valueOf(IsMasterCommitment));
        
    }
    
    /** Get Master Commitment.
    @return Master Commitment */
    public boolean isMasterCommitment() 
    {
        return get_ValueAsBoolean("IsMasterCommitment");
        
    }
    
    /** Set Purchase Domain.
    @param IsPurchaseDomain Purchase Domain */
    public void setIsPurchaseDomain (boolean IsPurchaseDomain)
    {
        set_Value ("IsPurchaseDomain", Boolean.valueOf(IsPurchaseDomain));
        
    }
    
    /** Get Purchase Domain.
    @return Purchase Domain */
    public boolean isPurchaseDomain() 
    {
        return get_ValueAsBoolean("IsPurchaseDomain");
        
    }
    
    /** Set Quotation Required.
    @param IsQuotationRequired Quotation Required */
    public void setIsQuotationRequired (boolean IsQuotationRequired)
    {
        set_Value ("IsQuotationRequired", Boolean.valueOf(IsQuotationRequired));
        
    }
    
    /** Get Quotation Required.
    @return Quotation Required */
    public boolean isQuotationRequired() 
    {
        return get_ValueAsBoolean("IsQuotationRequired");
        
    }
    
    /** Set Secondment Commitment.
    @param IsSecondmentCommitment Secondment Commitment */
    public void setIsSecondmentCommitment (boolean IsSecondmentCommitment)
    {
        set_Value ("IsSecondmentCommitment", Boolean.valueOf(IsSecondmentCommitment));
        
    }
    
    /** Get Secondment Commitment.
    @return Secondment Commitment */
    public boolean isSecondmentCommitment() 
    {
        return get_ValueAsBoolean("IsSecondmentCommitment");
        
    }
    
    /** Set Transfer Commitment.
    @param IsTransferCommitment Transfer Commitment */
    public void setIsTransferCommitment (boolean IsTransferCommitment)
    {
        set_Value ("IsTransferCommitment", Boolean.valueOf(IsTransferCommitment));
        
    }
    
    /** Get Transfer Commitment.
    @return Transfer Commitment */
    public boolean isTransferCommitment() 
    {
        return get_ValueAsBoolean("IsTransferCommitment");
        
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
    
    
}
