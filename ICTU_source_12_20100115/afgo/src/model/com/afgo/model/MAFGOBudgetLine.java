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
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFActivity;

import compiere.model.X_AFGO_BudgetLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOBudgetLine.java,v 1.11.2.1 2010/01/06 11:45:27 tomassen Exp $
 *
 */
public class MAFGOBudgetLine extends X_AFGO_BudgetLine implements AllocatableDocumentLine
{

	public MAFGOBudgetLine(Ctx ctx, int AFGO_BudgetLine_ID, String trxName) 
	{
		super(ctx, AFGO_BudgetLine_ID, trxName);
	}
	
	public MAFGOBudgetLine(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
	}
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private MAFGOBudget budget = null;
	
	public MAFGOBudget getBudget()
	{
		if (this.budget == null)
			this.budget = new MAFGOBudget(this.getCtx(), this.getAFGO_Budget_ID(), this.get_TrxName());
		return this.budget;
	}
	
	public MAFGOBudget getHeader()
	{
		return this.getBudget();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}
	
	public int getM_Product_ID ()
	{
		return 0;
	}

	public BigDecimal getPrice ()
	{
		return this.getLineNetAmt();
	}

	public BigDecimal getQty ()
	{
		return Env.ONE;
	}

	public MAFGOActivity getActivity ()
	{
		return MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}
	
	public MAFGOMonth getMonth()
	{
		return MAFGOMonth.getMonth(this.getCtx(), this.getAFGO_Month_ID());
	}
	
	public boolean beforeSave(boolean isNew)
	{
		// always org of header
		this.setAD_Org_ID(this.getHeader().getAD_Org_ID());
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
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
			String sql = "UPDATE AFGO_Budget b"
				+ " SET b.GrandTotal=(SELECT COALESCE(SUM(LineNetAmt), 0) FROM AFGO_BudgetLine WHERE AFGO_Budget_ID=b.AFGO_Budget_ID)"
				+ " WHERE b.AFGO_Budget_ID=?";
			
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Budget_ID());
			if (pstmt.executeUpdate() != 1)
				success = false;

			
			pstmt.close();
			pstmt = null;
		}
		catch(SQLException e)
		{
			log.severe("Unable to update AFGO_Budget totals: AFGO_Budget_ID=" + this.getAFGO_Budget_ID() + ", " + e.getMessage());
			success = false;
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return success;
	}

}
