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
/** Generated Model for AFGO_QuotationRequestLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_QuotationRequestLine.java,v 1.69 2010/01/04 13:30:18 tomassen Exp $ */
public class X_AFGO_QuotationRequestLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_QuotationRequestLine_ID id
    @param trxName transaction
    */
    public X_AFGO_QuotationRequestLine (Ctx ctx, int AFGO_QuotationRequestLine_ID, String trxName)
    {
        super (ctx, AFGO_QuotationRequestLine_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_QuotationRequestLine_ID == 0)
        {
            setAFGO_LotItem_ID (0);
            setAFGO_QuotationRequestLine_ID (0);
            setAFGO_QuotationRequest_ID (0);
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM AFGO_QuotationRequestLine WHERE AFGO_QuotationRequest_ID=@AFGO_QuotationRequest_ID@
            setLineNetAmt (Env.ZERO);	// 0
            setPrice (Env.ZERO);	// 0
            setQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_QuotationRequestLine (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536529152789L;
    /** Last Updated Timestamp 2009-10-01 15:30:36.0 */
    public static final long updatedMS = 1254403836000L;
    /** AD_Table_ID=1000017 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_QuotationRequestLine");
        
    }
    ;
    
    /** TableName=AFGO_QuotationRequestLine */
    public static final String Table_Name="AFGO_QuotationRequestLine";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_QuotationRequestLine");
    
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
    
    /** AFGO_LotItem_ID AD_Reference_ID=1000015 */
    public static final int AFGO_LOTITEM_ID_AD_Reference_ID=1000015;
    /** Set Lot Item.
    @param AFGO_LotItem_ID A lot item identifies a specific product or charge within a lot. */
    public void setAFGO_LotItem_ID (int AFGO_LotItem_ID)
    {
        if (AFGO_LotItem_ID < 1) throw new IllegalArgumentException ("AFGO_LotItem_ID is mandatory.");
        set_Value ("AFGO_LotItem_ID", Integer.valueOf(AFGO_LotItem_ID));
        
    }
    
    /** Get Lot Item.
    @return A lot item identifies a specific product or charge within a lot. */
    public int getAFGO_LotItem_ID() 
    {
        return get_ValueAsInt("AFGO_LotItem_ID");
        
    }
    
    /** Set Quotation Request Line.
    @param AFGO_QuotationRequestLine_ID On line level, you enter Lot item on which you want a quote response. */
    public void setAFGO_QuotationRequestLine_ID (int AFGO_QuotationRequestLine_ID)
    {
        if (AFGO_QuotationRequestLine_ID < 1) throw new IllegalArgumentException ("AFGO_QuotationRequestLine_ID is mandatory.");
        set_ValueNoCheck ("AFGO_QuotationRequestLine_ID", Integer.valueOf(AFGO_QuotationRequestLine_ID));
        
    }
    
    /** Get Quotation Request Line.
    @return On line level, you enter Lot item on which you want a quote response. */
    public int getAFGO_QuotationRequestLine_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationRequestLine_ID");
        
    }
    
    
    /** AFGO_QuotationRequest_ID AD_Reference_ID=1000014 */
    public static final int AFGO_QUOTATIONREQUEST_ID_AD_Reference_ID=1000014;
    /** Set Quotation Request.
    @param AFGO_QuotationRequest_ID You can define a quotation request to manage the selection procedure for the qualified suppliers.  */
    public void setAFGO_QuotationRequest_ID (int AFGO_QuotationRequest_ID)
    {
        if (AFGO_QuotationRequest_ID < 1) throw new IllegalArgumentException ("AFGO_QuotationRequest_ID is mandatory.");
        set_ValueNoCheck ("AFGO_QuotationRequest_ID", Integer.valueOf(AFGO_QuotationRequest_ID));
        
    }
    
    /** Get Quotation Request.
    @return You can define a quotation request to manage the selection procedure for the qualified suppliers.  */
    public int getAFGO_QuotationRequest_ID() 
    {
        return get_ValueAsInt("AFGO_QuotationRequest_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAFGO_QuotationRequest_ID()));
        
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
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_ValueNoCheck ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        if (LineNetAmt == null) throw new IllegalArgumentException ("LineNetAmt is mandatory.");
        set_ValueNoCheck ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
    }
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        if (Price == null) throw new IllegalArgumentException ("Price is mandatory.");
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        throw new IllegalArgumentException ("Processed is virtual column");
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
