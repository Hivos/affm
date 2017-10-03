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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for T_Report
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_T_Report.java,v 1.1 2009/02/17 12:50:20 tomassen Exp $ */
public class X_T_Report extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_Report_ID id
    @param trxName transaction
    */
    public X_T_Report (Ctx ctx, int T_Report_ID, String trxName)
    {
        super (ctx, T_Report_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (T_Report_ID == 0)
        {
            setAD_PInstance_ID (0);
            setFact_Acct_ID (0);
            setPA_ReportLine_ID (0);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_T_Report (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27500142933789L;
    /** Last Updated Timestamp 2008-08-06 12:13:37.0 */
    public static final long updatedMS = 1218017617000L;
    /** AD_Table_ID=544 */
    public static final int Table_ID=544;
    
    /** TableName=T_Report */
    public static final String Table_Name="T_Report";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"T_Report");
    
    /** AccessLevel
    @return 4 - System 
    */
    @Override protected PO.AccessLevel get_AccessLevel()
    {
        return PO.AccessLevel.SYSTEM;
        
    }
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Set Col_0.
    @param Col_0 Col_0 */
    public void setCol_0 (java.math.BigDecimal Col_0)
    {
        set_ValueNoCheck ("Col_0", Col_0);
        
    }
    
    /** Get Col_0.
    @return Col_0 */
    public java.math.BigDecimal getCol_0() 
    {
        return get_ValueAsBigDecimal("Col_0");
        
    }
    
    /** Set Col_1.
    @param Col_1 Col_1 */
    public void setCol_1 (java.math.BigDecimal Col_1)
    {
        set_ValueNoCheck ("Col_1", Col_1);
        
    }
    
    /** Get Col_1.
    @return Col_1 */
    public java.math.BigDecimal getCol_1() 
    {
        return get_ValueAsBigDecimal("Col_1");
        
    }
    
    /** Set Col_10.
    @param Col_10 Col_10 */
    public void setCol_10 (java.math.BigDecimal Col_10)
    {
        set_ValueNoCheck ("Col_10", Col_10);
        
    }
    
    /** Get Col_10.
    @return Col_10 */
    public java.math.BigDecimal getCol_10() 
    {
        return get_ValueAsBigDecimal("Col_10");
        
    }
    
    /** Set Col_11.
    @param Col_11 Col_11 */
    public void setCol_11 (java.math.BigDecimal Col_11)
    {
        set_ValueNoCheck ("Col_11", Col_11);
        
    }
    
    /** Get Col_11.
    @return Col_11 */
    public java.math.BigDecimal getCol_11() 
    {
        return get_ValueAsBigDecimal("Col_11");
        
    }
    
    /** Set Col_12.
    @param Col_12 Col_12 */
    public void setCol_12 (java.math.BigDecimal Col_12)
    {
        set_ValueNoCheck ("Col_12", Col_12);
        
    }
    
    /** Get Col_12.
    @return Col_12 */
    public java.math.BigDecimal getCol_12() 
    {
        return get_ValueAsBigDecimal("Col_12");
        
    }
    
    /** Set Col_13.
    @param Col_13 Col_13 */
    public void setCol_13 (java.math.BigDecimal Col_13)
    {
        set_ValueNoCheck ("Col_13", Col_13);
        
    }
    
    /** Get Col_13.
    @return Col_13 */
    public java.math.BigDecimal getCol_13() 
    {
        return get_ValueAsBigDecimal("Col_13");
        
    }
    
    /** Set Col_14.
    @param Col_14 Col_14 */
    public void setCol_14 (java.math.BigDecimal Col_14)
    {
        set_ValueNoCheck ("Col_14", Col_14);
        
    }
    
    /** Get Col_14.
    @return Col_14 */
    public java.math.BigDecimal getCol_14() 
    {
        return get_ValueAsBigDecimal("Col_14");
        
    }
    
    /** Set Col_15.
    @param Col_15 Col_15 */
    public void setCol_15 (java.math.BigDecimal Col_15)
    {
        set_ValueNoCheck ("Col_15", Col_15);
        
    }
    
    /** Get Col_15.
    @return Col_15 */
    public java.math.BigDecimal getCol_15() 
    {
        return get_ValueAsBigDecimal("Col_15");
        
    }
    
    /** Set Col_16.
    @param Col_16 Col_16 */
    public void setCol_16 (java.math.BigDecimal Col_16)
    {
        set_ValueNoCheck ("Col_16", Col_16);
        
    }
    
    /** Get Col_16.
    @return Col_16 */
    public java.math.BigDecimal getCol_16() 
    {
        return get_ValueAsBigDecimal("Col_16");
        
    }
    
    /** Set Col_17.
    @param Col_17 Col_17 */
    public void setCol_17 (java.math.BigDecimal Col_17)
    {
        set_ValueNoCheck ("Col_17", Col_17);
        
    }
    
    /** Get Col_17.
    @return Col_17 */
    public java.math.BigDecimal getCol_17() 
    {
        return get_ValueAsBigDecimal("Col_17");
        
    }
    
    /** Set Col_18.
    @param Col_18 Col_18 */
    public void setCol_18 (java.math.BigDecimal Col_18)
    {
        set_ValueNoCheck ("Col_18", Col_18);
        
    }
    
    /** Get Col_18.
    @return Col_18 */
    public java.math.BigDecimal getCol_18() 
    {
        return get_ValueAsBigDecimal("Col_18");
        
    }
    
    /** Set Col_19.
    @param Col_19 Col_19 */
    public void setCol_19 (java.math.BigDecimal Col_19)
    {
        set_ValueNoCheck ("Col_19", Col_19);
        
    }
    
    /** Get Col_19.
    @return Col_19 */
    public java.math.BigDecimal getCol_19() 
    {
        return get_ValueAsBigDecimal("Col_19");
        
    }
    
    /** Set Col_2.
    @param Col_2 Col_2 */
    public void setCol_2 (java.math.BigDecimal Col_2)
    {
        set_ValueNoCheck ("Col_2", Col_2);
        
    }
    
    /** Get Col_2.
    @return Col_2 */
    public java.math.BigDecimal getCol_2() 
    {
        return get_ValueAsBigDecimal("Col_2");
        
    }
    
    /** Set Col_20.
    @param Col_20 Col_20 */
    public void setCol_20 (java.math.BigDecimal Col_20)
    {
        set_ValueNoCheck ("Col_20", Col_20);
        
    }
    
    /** Get Col_20.
    @return Col_20 */
    public java.math.BigDecimal getCol_20() 
    {
        return get_ValueAsBigDecimal("Col_20");
        
    }
    
    /** Set Col_21.
    @param Col_21 Col_21 */
    public void setCol_21 (java.math.BigDecimal Col_21)
    {
        set_ValueNoCheck ("Col_21", Col_21);
        
    }
    
    /** Get Col_21.
    @return Col_21 */
    public java.math.BigDecimal getCol_21() 
    {
        return get_ValueAsBigDecimal("Col_21");
        
    }
    
    /** Set Col_22.
    @param Col_22 Col_22 */
    public void setCol_22 (java.math.BigDecimal Col_22)
    {
        set_ValueNoCheck ("Col_22", Col_22);
        
    }
    
    /** Get Col_22.
    @return Col_22 */
    public java.math.BigDecimal getCol_22() 
    {
        return get_ValueAsBigDecimal("Col_22");
        
    }
    
    /** Set Col_23.
    @param Col_23 Col_23 */
    public void setCol_23 (java.math.BigDecimal Col_23)
    {
        set_ValueNoCheck ("Col_23", Col_23);
        
    }
    
    /** Get Col_23.
    @return Col_23 */
    public java.math.BigDecimal getCol_23() 
    {
        return get_ValueAsBigDecimal("Col_23");
        
    }
    
    /** Set Col_24.
    @param Col_24 Col_24 */
    public void setCol_24 (java.math.BigDecimal Col_24)
    {
        set_ValueNoCheck ("Col_24", Col_24);
        
    }
    
    /** Get Col_24.
    @return Col_24 */
    public java.math.BigDecimal getCol_24() 
    {
        return get_ValueAsBigDecimal("Col_24");
        
    }
    
    /** Set Col_25.
    @param Col_25 Col_25 */
    public void setCol_25 (java.math.BigDecimal Col_25)
    {
        set_ValueNoCheck ("Col_25", Col_25);
        
    }
    
    /** Get Col_25.
    @return Col_25 */
    public java.math.BigDecimal getCol_25() 
    {
        return get_ValueAsBigDecimal("Col_25");
        
    }
    
    /** Set Col_26.
    @param Col_26 Col_26 */
    public void setCol_26 (java.math.BigDecimal Col_26)
    {
        set_ValueNoCheck ("Col_26", Col_26);
        
    }
    
    /** Get Col_26.
    @return Col_26 */
    public java.math.BigDecimal getCol_26() 
    {
        return get_ValueAsBigDecimal("Col_26");
        
    }
    
    /** Set Col_27.
    @param Col_27 Col_27 */
    public void setCol_27 (java.math.BigDecimal Col_27)
    {
        set_ValueNoCheck ("Col_27", Col_27);
        
    }
    
    /** Get Col_27.
    @return Col_27 */
    public java.math.BigDecimal getCol_27() 
    {
        return get_ValueAsBigDecimal("Col_27");
        
    }
    
    /** Set Col_28.
    @param Col_28 Col_28 */
    public void setCol_28 (java.math.BigDecimal Col_28)
    {
        set_ValueNoCheck ("Col_28", Col_28);
        
    }
    
    /** Get Col_28.
    @return Col_28 */
    public java.math.BigDecimal getCol_28() 
    {
        return get_ValueAsBigDecimal("Col_28");
        
    }
    
    /** Set Col_29.
    @param Col_29 Col_29 */
    public void setCol_29 (java.math.BigDecimal Col_29)
    {
        set_ValueNoCheck ("Col_29", Col_29);
        
    }
    
    /** Get Col_29.
    @return Col_29 */
    public java.math.BigDecimal getCol_29() 
    {
        return get_ValueAsBigDecimal("Col_29");
        
    }
    
    /** Set Col_3.
    @param Col_3 Col_3 */
    public void setCol_3 (java.math.BigDecimal Col_3)
    {
        set_ValueNoCheck ("Col_3", Col_3);
        
    }
    
    /** Get Col_3.
    @return Col_3 */
    public java.math.BigDecimal getCol_3() 
    {
        return get_ValueAsBigDecimal("Col_3");
        
    }
    
    /** Set Col_30.
    @param Col_30 Col_30 */
    public void setCol_30 (java.math.BigDecimal Col_30)
    {
        set_ValueNoCheck ("Col_30", Col_30);
        
    }
    
    /** Get Col_30.
    @return Col_30 */
    public java.math.BigDecimal getCol_30() 
    {
        return get_ValueAsBigDecimal("Col_30");
        
    }
    
    /** Set Col_31.
    @param Col_31 Col_31 */
    public void setCol_31 (java.math.BigDecimal Col_31)
    {
        set_ValueNoCheck ("Col_31", Col_31);
        
    }
    
    /** Get Col_31.
    @return Col_31 */
    public java.math.BigDecimal getCol_31() 
    {
        return get_ValueAsBigDecimal("Col_31");
        
    }
    
    /** Set Col_32.
    @param Col_32 Col_32 */
    public void setCol_32 (java.math.BigDecimal Col_32)
    {
        set_ValueNoCheck ("Col_32", Col_32);
        
    }
    
    /** Get Col_32.
    @return Col_32 */
    public java.math.BigDecimal getCol_32() 
    {
        return get_ValueAsBigDecimal("Col_32");
        
    }
    
    /** Set Col_33.
    @param Col_33 Col_33 */
    public void setCol_33 (java.math.BigDecimal Col_33)
    {
        set_ValueNoCheck ("Col_33", Col_33);
        
    }
    
    /** Get Col_33.
    @return Col_33 */
    public java.math.BigDecimal getCol_33() 
    {
        return get_ValueAsBigDecimal("Col_33");
        
    }
    
    /** Set Col_34.
    @param Col_34 Col_34 */
    public void setCol_34 (java.math.BigDecimal Col_34)
    {
        set_ValueNoCheck ("Col_34", Col_34);
        
    }
    
    /** Get Col_34.
    @return Col_34 */
    public java.math.BigDecimal getCol_34() 
    {
        return get_ValueAsBigDecimal("Col_34");
        
    }
    
    /** Set Col_35.
    @param Col_35 Col_35 */
    public void setCol_35 (java.math.BigDecimal Col_35)
    {
        set_ValueNoCheck ("Col_35", Col_35);
        
    }
    
    /** Get Col_35.
    @return Col_35 */
    public java.math.BigDecimal getCol_35() 
    {
        return get_ValueAsBigDecimal("Col_35");
        
    }
    
    /** Set Col_36.
    @param Col_36 Col_36 */
    public void setCol_36 (java.math.BigDecimal Col_36)
    {
        set_ValueNoCheck ("Col_36", Col_36);
        
    }
    
    /** Get Col_36.
    @return Col_36 */
    public java.math.BigDecimal getCol_36() 
    {
        return get_ValueAsBigDecimal("Col_36");
        
    }
    
    /** Set Col_37.
    @param Col_37 Col_37 */
    public void setCol_37 (java.math.BigDecimal Col_37)
    {
        set_ValueNoCheck ("Col_37", Col_37);
        
    }
    
    /** Get Col_37.
    @return Col_37 */
    public java.math.BigDecimal getCol_37() 
    {
        return get_ValueAsBigDecimal("Col_37");
        
    }
    
    /** Set Col_38.
    @param Col_38 Col_38 */
    public void setCol_38 (java.math.BigDecimal Col_38)
    {
        set_ValueNoCheck ("Col_38", Col_38);
        
    }
    
    /** Get Col_38.
    @return Col_38 */
    public java.math.BigDecimal getCol_38() 
    {
        return get_ValueAsBigDecimal("Col_38");
        
    }
    
    /** Set Col_39.
    @param Col_39 Col_39 */
    public void setCol_39 (java.math.BigDecimal Col_39)
    {
        set_ValueNoCheck ("Col_39", Col_39);
        
    }
    
    /** Get Col_39.
    @return Col_39 */
    public java.math.BigDecimal getCol_39() 
    {
        return get_ValueAsBigDecimal("Col_39");
        
    }
    
    /** Set Col_4.
    @param Col_4 Col_4 */
    public void setCol_4 (java.math.BigDecimal Col_4)
    {
        set_ValueNoCheck ("Col_4", Col_4);
        
    }
    
    /** Get Col_4.
    @return Col_4 */
    public java.math.BigDecimal getCol_4() 
    {
        return get_ValueAsBigDecimal("Col_4");
        
    }
    
    /** Set Col_40.
    @param Col_40 Col_40 */
    public void setCol_40 (java.math.BigDecimal Col_40)
    {
        set_ValueNoCheck ("Col_40", Col_40);
        
    }
    
    /** Get Col_40.
    @return Col_40 */
    public java.math.BigDecimal getCol_40() 
    {
        return get_ValueAsBigDecimal("Col_40");
        
    }
    
    /** Set Col_41.
    @param Col_41 Col_41 */
    public void setCol_41 (java.math.BigDecimal Col_41)
    {
        set_ValueNoCheck ("Col_41", Col_41);
        
    }
    
    /** Get Col_41.
    @return Col_41 */
    public java.math.BigDecimal getCol_41() 
    {
        return get_ValueAsBigDecimal("Col_41");
        
    }
    
    /** Set Col_42.
    @param Col_42 Col_42 */
    public void setCol_42 (java.math.BigDecimal Col_42)
    {
        set_ValueNoCheck ("Col_42", Col_42);
        
    }
    
    /** Get Col_42.
    @return Col_42 */
    public java.math.BigDecimal getCol_42() 
    {
        return get_ValueAsBigDecimal("Col_42");
        
    }
    
    /** Set Col_43.
    @param Col_43 Col_43 */
    public void setCol_43 (java.math.BigDecimal Col_43)
    {
        set_ValueNoCheck ("Col_43", Col_43);
        
    }
    
    /** Get Col_43.
    @return Col_43 */
    public java.math.BigDecimal getCol_43() 
    {
        return get_ValueAsBigDecimal("Col_43");
        
    }
    
    /** Set Col_44.
    @param Col_44 Col_44 */
    public void setCol_44 (java.math.BigDecimal Col_44)
    {
        set_ValueNoCheck ("Col_44", Col_44);
        
    }
    
    /** Get Col_44.
    @return Col_44 */
    public java.math.BigDecimal getCol_44() 
    {
        return get_ValueAsBigDecimal("Col_44");
        
    }
    
    /** Set Col_45.
    @param Col_45 Col_45 */
    public void setCol_45 (java.math.BigDecimal Col_45)
    {
        set_ValueNoCheck ("Col_45", Col_45);
        
    }
    
    /** Get Col_45.
    @return Col_45 */
    public java.math.BigDecimal getCol_45() 
    {
        return get_ValueAsBigDecimal("Col_45");
        
    }
    
    /** Set Col_46.
    @param Col_46 Col_46 */
    public void setCol_46 (java.math.BigDecimal Col_46)
    {
        set_ValueNoCheck ("Col_46", Col_46);
        
    }
    
    /** Get Col_46.
    @return Col_46 */
    public java.math.BigDecimal getCol_46() 
    {
        return get_ValueAsBigDecimal("Col_46");
        
    }
    
    /** Set Col_47.
    @param Col_47 Col_47 */
    public void setCol_47 (java.math.BigDecimal Col_47)
    {
        set_ValueNoCheck ("Col_47", Col_47);
        
    }
    
    /** Get Col_47.
    @return Col_47 */
    public java.math.BigDecimal getCol_47() 
    {
        return get_ValueAsBigDecimal("Col_47");
        
    }
    
    /** Set Col_48.
    @param Col_48 Col_48 */
    public void setCol_48 (java.math.BigDecimal Col_48)
    {
        set_ValueNoCheck ("Col_48", Col_48);
        
    }
    
    /** Get Col_48.
    @return Col_48 */
    public java.math.BigDecimal getCol_48() 
    {
        return get_ValueAsBigDecimal("Col_48");
        
    }
    
    /** Set Col_49.
    @param Col_49 Col_49 */
    public void setCol_49 (java.math.BigDecimal Col_49)
    {
        set_ValueNoCheck ("Col_49", Col_49);
        
    }
    
    /** Get Col_49.
    @return Col_49 */
    public java.math.BigDecimal getCol_49() 
    {
        return get_ValueAsBigDecimal("Col_49");
        
    }
    
    /** Set Col_5.
    @param Col_5 Col_5 */
    public void setCol_5 (java.math.BigDecimal Col_5)
    {
        set_ValueNoCheck ("Col_5", Col_5);
        
    }
    
    /** Get Col_5.
    @return Col_5 */
    public java.math.BigDecimal getCol_5() 
    {
        return get_ValueAsBigDecimal("Col_5");
        
    }
    
    /** Set Col_50.
    @param Col_50 Col_50 */
    public void setCol_50 (java.math.BigDecimal Col_50)
    {
        set_ValueNoCheck ("Col_50", Col_50);
        
    }
    
    /** Get Col_50.
    @return Col_50 */
    public java.math.BigDecimal getCol_50() 
    {
        return get_ValueAsBigDecimal("Col_50");
        
    }
    
    /** Set Col_51.
    @param Col_51 Col_51 */
    public void setCol_51 (java.math.BigDecimal Col_51)
    {
        set_ValueNoCheck ("Col_51", Col_51);
        
    }
    
    /** Get Col_51.
    @return Col_51 */
    public java.math.BigDecimal getCol_51() 
    {
        return get_ValueAsBigDecimal("Col_51");
        
    }
    
    /** Set Col_52.
    @param Col_52 Col_52 */
    public void setCol_52 (java.math.BigDecimal Col_52)
    {
        set_ValueNoCheck ("Col_52", Col_52);
        
    }
    
    /** Get Col_52.
    @return Col_52 */
    public java.math.BigDecimal getCol_52() 
    {
        return get_ValueAsBigDecimal("Col_52");
        
    }
    
    /** Set Col_53.
    @param Col_53 Col_53 */
    public void setCol_53 (java.math.BigDecimal Col_53)
    {
        set_ValueNoCheck ("Col_53", Col_53);
        
    }
    
    /** Get Col_53.
    @return Col_53 */
    public java.math.BigDecimal getCol_53() 
    {
        return get_ValueAsBigDecimal("Col_53");
        
    }
    
    /** Set Col_54.
    @param Col_54 Col_54 */
    public void setCol_54 (java.math.BigDecimal Col_54)
    {
        set_ValueNoCheck ("Col_54", Col_54);
        
    }
    
    /** Get Col_54.
    @return Col_54 */
    public java.math.BigDecimal getCol_54() 
    {
        return get_ValueAsBigDecimal("Col_54");
        
    }
    
    /** Set Col_55.
    @param Col_55 Col_55 */
    public void setCol_55 (java.math.BigDecimal Col_55)
    {
        set_ValueNoCheck ("Col_55", Col_55);
        
    }
    
    /** Get Col_55.
    @return Col_55 */
    public java.math.BigDecimal getCol_55() 
    {
        return get_ValueAsBigDecimal("Col_55");
        
    }
    
    /** Set Col_56.
    @param Col_56 Col_56 */
    public void setCol_56 (java.math.BigDecimal Col_56)
    {
        set_ValueNoCheck ("Col_56", Col_56);
        
    }
    
    /** Get Col_56.
    @return Col_56 */
    public java.math.BigDecimal getCol_56() 
    {
        return get_ValueAsBigDecimal("Col_56");
        
    }
    
    /** Set Col_57.
    @param Col_57 Col_57 */
    public void setCol_57 (java.math.BigDecimal Col_57)
    {
        set_ValueNoCheck ("Col_57", Col_57);
        
    }
    
    /** Get Col_57.
    @return Col_57 */
    public java.math.BigDecimal getCol_57() 
    {
        return get_ValueAsBigDecimal("Col_57");
        
    }
    
    /** Set Col_58.
    @param Col_58 Col_58 */
    public void setCol_58 (java.math.BigDecimal Col_58)
    {
        set_ValueNoCheck ("Col_58", Col_58);
        
    }
    
    /** Get Col_58.
    @return Col_58 */
    public java.math.BigDecimal getCol_58() 
    {
        return get_ValueAsBigDecimal("Col_58");
        
    }
    
    /** Set Col_59.
    @param Col_59 Col_59 */
    public void setCol_59 (java.math.BigDecimal Col_59)
    {
        set_ValueNoCheck ("Col_59", Col_59);
        
    }
    
    /** Get Col_59.
    @return Col_59 */
    public java.math.BigDecimal getCol_59() 
    {
        return get_ValueAsBigDecimal("Col_59");
        
    }
    
    /** Set Col_6.
    @param Col_6 Col_6 */
    public void setCol_6 (java.math.BigDecimal Col_6)
    {
        set_ValueNoCheck ("Col_6", Col_6);
        
    }
    
    /** Get Col_6.
    @return Col_6 */
    public java.math.BigDecimal getCol_6() 
    {
        return get_ValueAsBigDecimal("Col_6");
        
    }
    
    /** Set Col_60.
    @param Col_60 Col_60 */
    public void setCol_60 (java.math.BigDecimal Col_60)
    {
        set_ValueNoCheck ("Col_60", Col_60);
        
    }
    
    /** Get Col_60.
    @return Col_60 */
    public java.math.BigDecimal getCol_60() 
    {
        return get_ValueAsBigDecimal("Col_60");
        
    }
    
    /** Set Col_7.
    @param Col_7 Col_7 */
    public void setCol_7 (java.math.BigDecimal Col_7)
    {
        set_ValueNoCheck ("Col_7", Col_7);
        
    }
    
    /** Get Col_7.
    @return Col_7 */
    public java.math.BigDecimal getCol_7() 
    {
        return get_ValueAsBigDecimal("Col_7");
        
    }
    
    /** Set Col_8.
    @param Col_8 Col_8 */
    public void setCol_8 (java.math.BigDecimal Col_8)
    {
        set_ValueNoCheck ("Col_8", Col_8);
        
    }
    
    /** Get Col_8.
    @return Col_8 */
    public java.math.BigDecimal getCol_8() 
    {
        return get_ValueAsBigDecimal("Col_8");
        
    }
    
    /** Set Col_9.
    @param Col_9 Col_9 */
    public void setCol_9 (java.math.BigDecimal Col_9)
    {
        set_ValueNoCheck ("Col_9", Col_9);
        
    }
    
    /** Get Col_9.
    @return Col_9 */
    public java.math.BigDecimal getCol_9() 
    {
        return get_ValueAsBigDecimal("Col_9");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_ValueNoCheck ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Accounting Fact.
    @param Fact_Acct_ID Accounting Fact */
    public void setFact_Acct_ID (int Fact_Acct_ID)
    {
        if (Fact_Acct_ID < 1) throw new IllegalArgumentException ("Fact_Acct_ID is mandatory.");
        set_ValueNoCheck ("Fact_Acct_ID", Integer.valueOf(Fact_Acct_ID));
        
    }
    
    /** Get Accounting Fact.
    @return Accounting Fact */
    public int getFact_Acct_ID() 
    {
        return get_ValueAsInt("Fact_Acct_ID");
        
    }
    
    /** Set Level no.
    @param LevelNo Level no */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level no */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_ValueNoCheck ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Report Line.
    @param PA_ReportLine_ID Report Line */
    public void setPA_ReportLine_ID (int PA_ReportLine_ID)
    {
        if (PA_ReportLine_ID < 1) throw new IllegalArgumentException ("PA_ReportLine_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportLine_ID", Integer.valueOf(PA_ReportLine_ID));
        
    }
    
    /** Get Report Line.
    @return Report Line */
    public int getPA_ReportLine_ID() 
    {
        return get_ValueAsInt("PA_ReportLine_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_ValueNoCheck ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
