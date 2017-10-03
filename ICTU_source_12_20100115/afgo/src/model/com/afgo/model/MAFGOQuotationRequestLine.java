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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.X_AFGO_QuotationRequestLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOQuotationRequestLine.java,v 1.5.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOQuotationRequestLine extends X_AFGO_QuotationRequestLine
{

	public MAFGOQuotationRequestLine (Ctx ctx, int AFGO_QuotationRequestLine_ID, String trxName)
	{
		super(ctx, AFGO_QuotationRequestLine_ID, trxName);

	}
	
	public MAFGOQuotationRequestLine (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private MAFGOQuotationRequest quotationRequest = null;
	
	public MAFGOQuotationRequest getQuotationRequest()
	{
		if (this.quotationRequest == null && this.getAFGO_QuotationRequest_ID() > 0)
			this.quotationRequest = new MAFGOQuotationRequest(this.getCtx(), this.getAFGO_QuotationRequest_ID(), this.get_TrxName());
		return this.quotationRequest;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		this.setAD_Org_ID(this.getQuotationRequest().getAD_Org_ID());
		
		return true;
	}
	
	public boolean afterSave(boolean isNew, boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	public boolean afterDelete(boolean success)
	{
		if (!success)
			return success;
		
		return this.updateHeader();
	}
	
	private boolean updateHeader()
	{
		boolean success = true;
		
		PreparedStatement pstmt = null;
		
		try
		{
			String sql = "UPDATE AFGO_QuotationRequest q"
				+ " SET q.GrandTotal=(SELECT SUM(LineNetAmt) FROM AFGO_QuotationRequestLine WHERE AFGO_QuotationRequest_ID=q.AFGO_QuotationRequest_ID)"
				+ " WHERE q.AFGO_QuotationRequest_ID=?";
			
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_QuotationRequest_ID());
			if (pstmt.executeUpdate() != 1)
				success = false;

			pstmt.close();
			pstmt = null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			success = false;
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return success;
	}

}
