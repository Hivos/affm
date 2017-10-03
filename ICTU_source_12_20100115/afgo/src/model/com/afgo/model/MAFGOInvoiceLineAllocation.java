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
package com.afgo.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.framework.PO;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.X_AFGO_InvoiceLineAllocation;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOInvoiceLineAllocation.java,v 1.5.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOInvoiceLineAllocation extends X_AFGO_InvoiceLineAllocation
{

	public MAFGOInvoiceLineAllocation(Ctx ctx, int AFGO_InvoiceLineAllocation_ID, String trxName)
	{
		super(ctx, AFGO_InvoiceLineAllocation_ID, trxName);
	}
	
	public MAFGOInvoiceLineAllocation(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	public MAFGOInvoiceLineAllocation(MAFGOInvoiceLineAllocation original, int C_InvoiceLine_ID, boolean reverse)
	{
		super(original.getCtx(), 0, original.get_TrxName());
		
		PO.copyValues(original, this, original.getAD_Client_ID(), original.getAD_Org_ID());
		
		this.setC_InvoiceLine_ID(C_InvoiceLine_ID);
		this.set_ValueNoCheck("AFGO_InvoiceLineAllocation_ID", I_ZERO);
		
		if (reverse)
			this.setLineNetAmt(this.getLineNetAmt().negate());
	}
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private MInvoiceLine invoiceLine = null;
	
	public MInvoiceLine getInvoiceLine()
	{
		if (this.invoiceLine == null)
			this.invoiceLine = new MInvoiceLine(this.getCtx(), this.getC_InvoiceLine_ID(), this.get_TrxName());
		return this.invoiceLine;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		boolean result = true;
		
		if (!checkAllocation(this.getInvoiceLine(), this))
		{
			log.saveError("AmtExceeded", "" + this.getInvoiceLine().getLineNetAmt());
			result = false;
		}
		
		return result;
	}
	
	//***************
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOInvoiceLineAllocation.class);
	
	public static boolean checkAllocation(MInvoiceLine invoiceLine, MAFGOInvoiceLineAllocation allocation)
	{
		String trxName = (allocation != null ? allocation.get_TrxName() : invoiceLine.get_TrxName());
		
		BigDecimal allocatedAmt = Env.ZERO;
		
		String sql = "SELECT SUM(LineNetAmt)"
			+ " FROM AFGO_InvoiceLineAllocation"
			+ " WHERE C_InvoiceLine_ID=?";
		
		if (allocation != null && allocation.getAFGO_InvoiceLineAllocation_ID() > 0)
			sql = sql + " AND AFGO_InvoiceLineAllocation_ID!=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, invoiceLine.getC_InvoiceLine_ID());
			if (allocation != null && allocation.getAFGO_InvoiceLineAllocation_ID() > 0)
				pstmt.setInt(2, allocation.getAFGO_InvoiceLineAllocation_ID());
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
				allocatedAmt = rs.getBigDecimal(1);
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			s_log.severe(e.getStackTrace().toString());
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		if (allocatedAmt == null)
			allocatedAmt = Env.ZERO;
		
		if (allocation != null)
			allocatedAmt = allocatedAmt.add(allocation.getLineNetAmt());

		if (invoiceLine.getLineNetAmt().compareTo(Env.ZERO) < 0)
		{
			if (allocatedAmt.compareTo(Env.ZERO) > 0 || allocatedAmt.compareTo(invoiceLine.getLineNetAmt()) < 0)
			{
				s_log.warning("LineAmt=" + invoiceLine.getLineNetAmt() + ", AllocatedAmt=" + allocatedAmt);
				return false;
			}
		}
		else if (invoiceLine.getLineNetAmt().compareTo(Env.ZERO) == 0)
		{
			if (allocatedAmt.compareTo(Env.ZERO) != 0)
			{
				s_log.warning("LineAmt=" + invoiceLine.getLineNetAmt() + ", AllocatedAmt=" + allocatedAmt);
				return false;
			}
		}
		else if (invoiceLine.getLineNetAmt().compareTo(Env.ZERO) > 0)
		{
			if (allocatedAmt.compareTo(Env.ZERO) < 0 || allocatedAmt.compareTo(invoiceLine.getLineNetAmt()) > 0)
			{
				s_log.warning("LineAmt=" + invoiceLine.getLineNetAmt() + ", AllocatedAmt=" + allocatedAmt);
				return false;
			}
		}
		
		return true;
	}

}
