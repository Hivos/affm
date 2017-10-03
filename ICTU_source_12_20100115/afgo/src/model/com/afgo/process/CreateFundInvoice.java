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
package com.afgo.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;

import com.afgo.model.MAFGOFundSchedule;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CreateFundInvoice.java,v 1.9.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CreateFundInvoice extends SvrProcess
{
	private int AD_Org_ID = -1;
	
	private int AFGO_Program_ID = -1;
	
	private int AFGO_FundProvider_ID = -1;
	
	private int AFGO_Fund_ID = -1;

	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_FundProvider_ID"))
				this.AFGO_FundProvider_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Fund_ID"))
				this.AFGO_Fund_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt () throws Exception
	{
		log.info("Create Funding Invoices: AFGO_Program_ID=" + this.AFGO_Program_ID + ", AFGO_FundProvider_ID=" + this.AFGO_FundProvider_ID + ", AFGO_Fund_ID=" + this.AFGO_Fund_ID);
		
		if (this.AFGO_FundProvider_ID > 0 && this.AFGO_Program_ID < 1)
			throw new CompiereUserException("No AFGO_Program_ID specified");
		
		if (this.AFGO_Fund_ID > 0 && this.AFGO_FundProvider_ID < 1)
			throw new CompiereUserException("No AFGO_FundProvider_ID specified");
		
		String sql = "SELECT isc.AFGO_FundSchedule_ID"
			+ "\nFROM AFGO_FundScheduleLine isl"
			+ "\nINNER JOIN AFGO_FundSchedule isc ON (isc.AFGO_FundSchedule_ID=isl.AFGO_FundSchedule_ID)"
			+ "\nINNER JOIN AFGO_Fund f ON (f.AFGO_Fund_ID=isc.AFGO_Fund_ID)"
			+ "\nWHERE isl.InvoicedAmt <> isl.LineNetAmt"
			+ "\nAND isc.Processed='Y'"
			+ "\nAND isc.DateInvoiced < ?"
			+ "\nAND isl.AD_Org_ID=?";
		

		if (this.AFGO_Program_ID > 0)
			sql = sql + "\nAND f.AFGO_Program_ID=?";
		if (this.AFGO_FundProvider_ID > 0)
			sql = sql + "\nAND f.AFGO_FundProvider_ID=?";
		if (this.AFGO_Fund_ID > 0)
			sql = sql + "\nAND f.AFGO_Fund_ID=?";
		
		log.fine(sql);
		
		sql = sql + 	" \nGROUP BY isc.AFGO_FundSchedule_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setTimestamp(1, new Timestamp(this.getCtx().getContextAsTime("#Date")));
			pstmt.setInt(2, this.AD_Org_ID);
			
			if (this.AFGO_Program_ID > 0)
				pstmt.setInt(3, this.AFGO_Program_ID);
			
			if (this.AFGO_FundProvider_ID > 0)
				pstmt.setInt(4, this.AFGO_FundProvider_ID);
			
			if (this.AFGO_Fund_ID > 0)
				pstmt.setInt(5, this.AFGO_Fund_ID);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				MAFGOFundSchedule schedule = new MAFGOFundSchedule(this.getCtx(), rs.getInt(1), this.get_TrxName());

				MInvoice invoice = schedule.createInvoice();
				
				if (invoice == null || invoice.getC_Invoice_ID() < 1)
					throw new CompiereSystemException("Invoice not Saved");
					
				this.addLog(invoice.getDocumentNo() + " - " + schedule.getFund().getFundProvider().getBPartner().getName() + " - " + invoice.getDescription());
			}
		}
		catch(Exception e)
		{
			String msg = "A problem occured while creating Funding invoices: " + e.getMessage();
			throw new IllegalStateException(msg);
			
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return "@OK@";
	}

}
