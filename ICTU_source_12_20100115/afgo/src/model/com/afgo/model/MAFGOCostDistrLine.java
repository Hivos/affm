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
import java.util.ArrayList;

import org.compiere.framework.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFActivity;

import compiere.model.X_AFGO_CostDistrLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOCostDistrLine.java,v 1.11.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOCostDistrLine extends X_AFGO_CostDistrLine implements AllocatableDocumentLine
{

	public MAFGOCostDistrLine (Ctx ctx, int AFGO_CostDistr_ID, String trxName)
	{
		super(ctx, AFGO_CostDistr_ID, trxName);
	}
	
	public MAFGOCostDistrLine (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOCostDistr header = null;
	
	private ArrayList<MAFGOCostDistrAllocation> allocations = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public MAFGOCostDistr getHeader()
	{
		if (this.header == null)
			this.header = new MAFGOCostDistr(this.getCtx(), this.getAFGO_CostDistr_ID(), this.get_TrxName());
			
		return this.header;
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public ArrayList<MAFGOCostDistrAllocation> getAllocations()
	{
		//System.err.println("CDA: reload=" + (this.allocations == null));
		
		if (this.allocations == null)
		{
			this.allocations = new ArrayList<MAFGOCostDistrAllocation>();
			
			String sql = "SELECT *"
				+ " FROM AFGO_CostDistrAllocation"
				+ " WHERE AFGO_CostDistrLine_ID=?";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_CostDistrLine_ID());
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					this.allocations.add(new MAFGOCostDistrAllocation(this.getCtx(), rs, this.get_TrxName()));
				}
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		return this.allocations;
	}
	
	public boolean isAllocated()
	{
		return (this.getLineNetAmt().compareTo(this.getAllocatedAmt()) == 0);
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

	public int get_OldAFGO_Month_ID ()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}

	public MAFGOActivity getActivity()
	{
		return MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
	}
	
	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		// allocations exist: first allocation activity
		if (this.getAllocations().size() > 0)
			return this.getAllocations().get(0).getWorkflowActivity(workflowActivity);
		
		// no allocations yet, but we are in the target program: standard activity of target program
		if (this.getHeader().getToProgram() != this.getHeader().getProgram() 
				&& this.getActivity().getPhase().getProject().getProjectCluster().getProgram() != this.getHeader().getWorkflowProgram(workflowActivity))
		{
			return this.getHeader().getToProgram().getProjectCluster().getProject().getPhase().getActivity();
		}
		
		
		return this.getActivity();
	}
	
	public boolean beforeSave(boolean newRecord)
	{
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
		boolean result = true;
		
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE AFGO_CostDistr cd"
			+ " SET cd.GrandTotal=(SELECT SUM(LineNetAmt) FROM AFGO_CostDistrLine WHERE AFGO_CostDistr_ID=cd.AFGO_CostDistr_ID)"
			+ " WHERE cd.AFGO_CostDistr_ID=?";
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_CostDistr_ID());
			if (pstmt.executeUpdate() != 1)
				result = false;
			
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return result;
	}

}
