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
import java.sql.SQLException;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.Env;

import org.compiere.util.DB;
import org.compiere.wf.MWFActivity;

import compiere.model.X_AFGO_FundLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFundLine.java,v 1.11.2.1 2010/01/06 11:45:24 tomassen Exp $
 *
 */
public class MAFGOFundLine extends X_AFGO_FundLine implements AllocatableDocumentLine
{

	public MAFGOFundLine(Ctx ctx, int AFGO_FundLine_ID, String trxName) 
	{
		super(ctx, AFGO_FundLine_ID, trxName);
	}
	
	public MAFGOFundLine(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOFund fund = null;
	
	private MAFGOActivity activity = null;
	
	public MAFGOFund getFund()
	{
		if (this.fund == null)
			this.fund = new MAFGOFund(this.getCtx(), this.getAFGO_Fund_ID(), this.get_TrxName());
		return this.fund;
	}
	
	public MAFGOFund getHeader()
	{
		return this.getFund();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return 0;
	}
	
	public MAFGOActivity getActivity() 
	{
		if (this.activity == null)
			this.activity = MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
		return this.activity;
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}
	
	public BigDecimal getScheduledAmt()
	{
		BigDecimal scheduledAmt = Env.ZERO;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT SUM(fsl.Amount)"
			+ " FROM AFGO_FundSchedule fsl"
			+ " WHERE fsl.AFGO_FundLine_ID=?";
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_FundLine_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				scheduledAmt = rs.getBigDecimal(1);
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return scheduledAmt;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		// always org of header
		this.setAD_Org_ID(this.getHeader().getAD_Org_ID());
		
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
			String sql = "UPDATE AFGO_Fund f"
				+ " SET f.GrandTotal=(SELECT SUM(LineNetAmt) FROM AFGO_FundLine WHERE AFGO_Fund_ID=f.AFGO_Fund_ID)"
				+ " WHERE f.AFGO_Fund_ID=?";
			
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Fund_ID());
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

	public int getAFGO_Month_ID() 
	{
		return 0;
	}

	public int getM_Product_ID()
	{
		return 0;
	}

	public BigDecimal getPrice() 
	{
		return this.getLineNetAmt();
	}

	public BigDecimal getQty() 
	{
		return BigDecimal.ONE;
	}

}
