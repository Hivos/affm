/******************************************************************************
 * Product: AFGO                                                              *
 * Copyright (C) 2007-2010 Stichting ICTU. All Rights Reserved.               *
 *                                                                            *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 3 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * Stichting ICTU                                                             *
 * Postbus 84011                                                              *
 * 2508 AA Den Haag                                                           *
 * The Netherlands                                                            *
 * +31(0)70 888 77 77                                                         *
 * www.ictu.nl / info@ictu.nl                                                 *
 ******************************************************************************/
package com.afgo.test;

import org.compiere.framework.AccessSqlParser;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: TestAccessSqlParser.java,v 1.2.2.1 2010/01/06 11:45:35 tomassen Exp $
 *
 */
public class TestAccessSqlParser
{
	public static void main(String[] argts)
	{
		org.compiere.Compiere.startup(true);
		
		String sql = "SELECT AD_Client_ID,AD_Org_ID,C_Order_ID,DateOrdered,DocumentNo,POReference,Description," 
			+ " C_DocTypeTarget_ID,IsSelfService,DateInvoiced,DateAcct,C_BPartner_ID,C_BPartner_Location_ID," 
			+ " AD_User_ID,M_PriceList_ID,C_Currency_ID,C_ConversionType_ID,SalesRep_ID,IsDiscountPrinted,"
			+ " C_Charge_ID,ChargeAmt,PaymentRule,C_PaymentTerm_ID,C_Project_ID,C_Activity_ID,C_Campaign_ID,"
			+ " AD_OrgTrx_ID,User1_ID,User2_ID,TotalLines,GrandTotal,DocStatus,C_DocType_ID,IsPayScheduleValid,"
			+ " IsInDispute,CopyFrom,DocAction,Posted,IsPaid,InvoiceCollectionType,AFGO_Program_ID,AFGO_FundSchedule_ID,"
			+ " IsReturnTrx,Ref_Invoice_ID,SendEMail,GenerateTo,CreateFrom,C_Payment_ID,C_CashLine_ID,IsTaxIncluded,Processing,"
			+ " Processed,IsTransferred,IsApproved,IsActive,C_Invoice_ID,IsSOTrx,DatePrinted,IsPrinted,"
			+ " (  "
			+ " 	SELECT p.ProgramPeriodType   " 
			+ "		FROM AFGO_FundSchedule s  "
			+ "		INNER JOIN AFGO_Fund f ON (f.AFGO_Fund_ID=s.AFGO_Fund_ID)  "
			+ " 	INNER JOIN AFGO_Program p ON (p.AFGO_Program_ID=f.AFGO_Program_ID) "
			+ "		WHERE s.AFGO_FundSchedule_ID=C_Invoice.AFGO_FundSchedule_ID "
			+ "	) AS ProgramPeriodType,"
			+ "	Created,CreatedBy,Updated,UpdatedBy "
			+ " FROM C_Invoice "
			+ " WHERE IsSOTrx='Y' "
			+ " AND (Processed='N' OR Created>=addDays(SysDate, -1))";
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
		
		AccessSqlParser parser = new AccessSqlParser(sql);
		
		boolean result = parser.parse();
		
		System.out.println("parse=" + result);
	}
}
