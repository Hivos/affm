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
/** Generated Model for AFGO_FundScheduleType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_FundScheduleType.java,v 1.3 2009/11/24 14:17:54 tomassen Exp $ */
public class X_AFGO_FundScheduleType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_FundScheduleType_ID id
    @param trxName transaction
    */
    public X_AFGO_FundScheduleType (Ctx ctx, int AFGO_FundScheduleType_ID, String trxName)
    {
        super (ctx, AFGO_FundScheduleType_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_FundScheduleType_ID == 0)
        {
            setAFGO_FundScheduleType_ID (0);
            setC_DocTypeFundingSchedule_ID (0);
            setCorrectionPeriodType (null);	// S
            setFundSchedulePeriodType (null);	// S
            setName (null);
            setNetDays (0);	// 0
            setStd_Rounding (null);	// C
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_FundScheduleType (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520554644789L;
    /** Last Updated Timestamp 2009-03-30 18:08:48.0 */
    public static final long updatedMS = 1238429328000L;
    /** AD_Table_ID=1000026 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_FundScheduleType");
        
    }
    ;
    
    /** TableName=AFGO_FundScheduleType */
    public static final String Table_Name="AFGO_FundScheduleType";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_FundScheduleType");
    
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
    /** Set Invoice Schedule Type.
    @param AFGO_FundScheduleType_ID Determines the date, relative to funding dates, on which the invoice needs to be created. */
    public void setAFGO_FundScheduleType_ID (int AFGO_FundScheduleType_ID)
    {
        if (AFGO_FundScheduleType_ID < 1) throw new IllegalArgumentException ("AFGO_FundScheduleType_ID is mandatory.");
        set_ValueNoCheck ("AFGO_FundScheduleType_ID", Integer.valueOf(AFGO_FundScheduleType_ID));
        
    }
    
    /** Get Invoice Schedule Type.
    @return Determines the date, relative to funding dates, on which the invoice needs to be created. */
    public int getAFGO_FundScheduleType_ID() 
    {
        return get_ValueAsInt("AFGO_FundScheduleType_ID");
        
    }
    
    
    /** C_DocTypeFundingSchedule_ID AD_Reference_ID=170 */
    public static final int C_DOCTYPEFUNDINGSCHEDULE_ID_AD_Reference_ID=170;
    /** Set Document Type for Funding Schedule.
    @param C_DocTypeFundingSchedule_ID Document Type for Funding Schedule */
    public void setC_DocTypeFundingSchedule_ID (int C_DocTypeFundingSchedule_ID)
    {
        if (C_DocTypeFundingSchedule_ID < 1) throw new IllegalArgumentException ("C_DocTypeFundingSchedule_ID is mandatory.");
        set_Value ("C_DocTypeFundingSchedule_ID", Integer.valueOf(C_DocTypeFundingSchedule_ID));
        
    }
    
    /** Get Document Type for Funding Schedule.
    @return Document Type for Funding Schedule */
    public int getC_DocTypeFundingSchedule_ID() 
    {
        return get_ValueAsInt("C_DocTypeFundingSchedule_ID");
        
    }
    
    
    /** C_DocTypeInvoice_ID AD_Reference_ID=170 */
    public static final int C_DOCTYPEINVOICE_ID_AD_Reference_ID=170;
    /** Set Document Type for Invoice.
    @param C_DocTypeInvoice_ID Document type used for invoices generated from this sales document */
    public void setC_DocTypeInvoice_ID (int C_DocTypeInvoice_ID)
    {
        if (C_DocTypeInvoice_ID <= 0) set_Value ("C_DocTypeInvoice_ID", null);
        else
        set_Value ("C_DocTypeInvoice_ID", Integer.valueOf(C_DocTypeInvoice_ID));
        
    }
    
    /** Get Document Type for Invoice.
    @return Document type used for invoices generated from this sales document */
    public int getC_DocTypeInvoice_ID() 
    {
        return get_ValueAsInt("C_DocTypeInvoice_ID");
        
    }
    
    
    /** C_PaymentTerm_ID AD_Reference_ID=227 */
    public static final int C_PAYMENTTERM_ID_AD_Reference_ID=227;
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID <= 0) set_Value ("C_PaymentTerm_ID", null);
        else
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    
    /** CorrectionPeriodType AD_Reference_ID=1000029 */
    public static final int CORRECTIONPERIODTYPE_AD_Reference_ID=1000029;
    /** Period end = E */
    public static final String CORRECTIONPERIODTYPE_PeriodEnd = X_Ref_AFGO_FundSchedulePeriodType.PERIOD_END.getValue();
    /** Period start = S */
    public static final String CORRECTIONPERIODTYPE_PeriodStart = X_Ref_AFGO_FundSchedulePeriodType.PERIOD_START.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isCorrectionPeriodTypeValid(String test)
    {
         return X_Ref_AFGO_FundSchedulePeriodType.isValid(test);
         
    }
    /** Set Correction Period Type.
    @param CorrectionPeriodType Correction Period Type */
    public void setCorrectionPeriodType (String CorrectionPeriodType)
    {
        if (CorrectionPeriodType == null) throw new IllegalArgumentException ("CorrectionPeriodType is mandatory");
        if (!isCorrectionPeriodTypeValid(CorrectionPeriodType))
        throw new IllegalArgumentException ("CorrectionPeriodType Invalid value - " + CorrectionPeriodType + " - Reference_ID=1000029 - E - S");
        set_Value ("CorrectionPeriodType", CorrectionPeriodType);
        
    }
    
    /** Get Correction Period Type.
    @return Correction Period Type */
    public String getCorrectionPeriodType() 
    {
        return (String)get_Value("CorrectionPeriodType");
        
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
    
    
    /** FundSchedulePeriodType AD_Reference_ID=1000029 */
    public static final int FUNDSCHEDULEPERIODTYPE_AD_Reference_ID=1000029;
    /** Period end = E */
    public static final String FUNDSCHEDULEPERIODTYPE_PeriodEnd = X_Ref_AFGO_FundSchedulePeriodType.PERIOD_END.getValue();
    /** Period start = S */
    public static final String FUNDSCHEDULEPERIODTYPE_PeriodStart = X_Ref_AFGO_FundSchedulePeriodType.PERIOD_START.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isFundSchedulePeriodTypeValid(String test)
    {
         return X_Ref_AFGO_FundSchedulePeriodType.isValid(test);
         
    }
    /** Set Schedule Period Type.
    @param FundSchedulePeriodType Schedule Period Type */
    public void setFundSchedulePeriodType (String FundSchedulePeriodType)
    {
        if (FundSchedulePeriodType == null) throw new IllegalArgumentException ("FundSchedulePeriodType is mandatory");
        if (!isFundSchedulePeriodTypeValid(FundSchedulePeriodType))
        throw new IllegalArgumentException ("FundSchedulePeriodType Invalid value - " + FundSchedulePeriodType + " - Reference_ID=1000029 - E - S");
        set_Value ("FundSchedulePeriodType", FundSchedulePeriodType);
        
    }
    
    /** Get Schedule Period Type.
    @return Schedule Period Type */
    public String getFundSchedulePeriodType() 
    {
        return (String)get_Value("FundSchedulePeriodType");
        
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
    
    /** Set Net Days.
    @param NetDays Net Days in which payment is due */
    public void setNetDays (int NetDays)
    {
        set_Value ("NetDays", Integer.valueOf(NetDays));
        
    }
    
    /** Get Net Days.
    @return Net Days in which payment is due */
    public int getNetDays() 
    {
        return get_ValueAsInt("NetDays");
        
    }
    
    
    /** PaymentRule AD_Reference_ID=195 */
    public static final int PAYMENTRULE_AD_Reference_ID=195;
    /** Cash = B */
    public static final String PAYMENTRULE_Cash = X_Ref__Payment_Rule.CASH.getValue();
    /** Direct Debit = D */
    public static final String PAYMENTRULE_DirectDebit = X_Ref__Payment_Rule.DIRECT_DEBIT.getValue();
    /** Credit Card = K */
    public static final String PAYMENTRULE_CreditCard = X_Ref__Payment_Rule.CREDIT_CARD.getValue();
    /** On Credit = P */
    public static final String PAYMENTRULE_OnCredit = X_Ref__Payment_Rule.ON_CREDIT.getValue();
    /** Check = S */
    public static final String PAYMENTRULE_Check = X_Ref__Payment_Rule.CHECK.getValue();
    /** Direct Deposit = T */
    public static final String PAYMENTRULE_DirectDeposit = X_Ref__Payment_Rule.DIRECT_DEPOSIT.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isPaymentRuleValid(String test)
    {
         return X_Ref__Payment_Rule.isValid(test);
         
    }
    /** Set Payment Method.
    @param PaymentRule How you pay the invoice */
    public void setPaymentRule (String PaymentRule)
    {
        if (!isPaymentRuleValid(PaymentRule))
        throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRule", PaymentRule);
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public String getPaymentRule() 
    {
        return (String)get_Value("PaymentRule");
        
    }
    
    
    /** Std_Rounding AD_Reference_ID=155 */
    public static final int STD_ROUNDING_AD_Reference_ID=155;
    /** Whole Number .00 = 0 */
    public static final String STD_ROUNDING_WholeNumber00 = X_Ref_M_DiscountPriceList_RoundingRule.WHOLE_NUMBER00.getValue();
    /** Nickel .05, .10, .15, ... = 5 */
    public static final String STD_ROUNDING_Nickel051015 = X_Ref_M_DiscountPriceList_RoundingRule.NICKEL051015.getValue();
    /** Currency Precision = C */
    public static final String STD_ROUNDING_CurrencyPrecision = X_Ref_M_DiscountPriceList_RoundingRule.CURRENCY_PRECISION.getValue();
    /** Dime .10, .20, .30... = D */
    public static final String STD_ROUNDING_Dime102030 = X_Ref_M_DiscountPriceList_RoundingRule.DIME102030.getValue();
    /** No Rounding = N */
    public static final String STD_ROUNDING_NoRounding = X_Ref_M_DiscountPriceList_RoundingRule.NO_ROUNDING.getValue();
    /** Quarter .25 .50 .75 = Q */
    public static final String STD_ROUNDING_Quarter255075 = X_Ref_M_DiscountPriceList_RoundingRule.QUARTER255075.getValue();
    /** Ten 10.00, 20.00, .. = T */
    public static final String STD_ROUNDING_Ten10002000 = X_Ref_M_DiscountPriceList_RoundingRule.TEN10002000.getValue();
    /** Hundred = h */
    public static final String STD_ROUNDING_Hundred = X_Ref_M_DiscountPriceList_RoundingRule.HUNDRED.getValue();
    /** Thousand = t */
    public static final String STD_ROUNDING_Thousand = X_Ref_M_DiscountPriceList_RoundingRule.THOUSAND.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isStd_RoundingValid(String test)
    {
         return X_Ref_M_DiscountPriceList_RoundingRule.isValid(test);
         
    }
    /** Set Standard price Rounding.
    @param Std_Rounding Rounding rule for calculated price */
    public void setStd_Rounding (String Std_Rounding)
    {
        if (Std_Rounding == null) throw new IllegalArgumentException ("Std_Rounding is mandatory");
        if (!isStd_RoundingValid(Std_Rounding))
        throw new IllegalArgumentException ("Std_Rounding Invalid value - " + Std_Rounding + " - Reference_ID=155 - 0 - 5 - C - D - N - Q - T - h - t");
        set_Value ("Std_Rounding", Std_Rounding);
        
    }
    
    /** Get Standard price Rounding.
    @return Rounding rule for calculated price */
    public String getStd_Rounding() 
    {
        return (String)get_Value("Std_Rounding");
        
    }
    
    
}
