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

import compiere.model.X_AFGO_FundScheduleLine;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.wf.MWFActivity;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFundScheduleLine.java,v 1.9.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class MAFGOFundScheduleLine extends X_AFGO_FundScheduleLine implements AllocatableDocumentLine
{

	public MAFGOFundScheduleLine (Ctx ctx, int AFGO_FundScheduleLine_ID, String trxName)
	{
		super(ctx, AFGO_FundScheduleLine_ID, trxName);

	}
	
	public MAFGOFundScheduleLine (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	public MAFGOFundScheduleLine (MAFGOFundSchedule schedule, MAFGOFundLine line)
	{
		super(schedule.getCtx(), 0, schedule.get_TrxName());

		this.setAFGO_FundSchedule_ID(schedule.getAFGO_FundSchedule_ID());
		this.setAFGO_FundLine_ID(line.getAFGO_FundLine_ID());
		this.setLine(line.getLine());
		this.setAFGO_ProjectCluster_ID(line.getAFGO_ProjectCluster_ID());
		this.setAFGO_Project_ID(line.getAFGO_Project_ID());
		this.setAFGO_Phase_ID(line.getAFGO_Phase_ID());
		this.setAFGO_Activity_ID(line.getAFGO_Activity_ID());
		this.setAFGO_ServiceType_ID(line.getAFGO_ServiceType_ID());
	}
	
	private MAFGOFundLine fundLine = null;
	
	private MAFGOFundSchedule schedule = null;
	
	private MAFGOActivity activity = null;
	
	private CLogger log = CLogger.getCLogger(this.getClass());
	
	public MAFGOFundSchedule getFundSchedule()
	{
		if (this.schedule == null)
			this.schedule = new MAFGOFundSchedule(this.getCtx(), this.getAFGO_FundSchedule_ID(), this.get_TrxName());
		return this.schedule;
	}
	
	public MAFGOFundSchedule getHeader()
	{
		return this.getFundSchedule();
	}
	
	public PO getPO()
	{
		return this;
	}
	
	public MAFGOFundLine getFundLine()
	{
		if (this.fundLine == null)
			this.fundLine = new MAFGOFundLine(this.getCtx(), this.getAFGO_FundLine_ID(), this.get_TrxName());
		return this.fundLine;
	}
	
	public int get_OldAFGO_Month_ID()
	{
		return this.getHeader().get_ValueOld("AFGO_Month_ID") != null ? (Integer)this.getHeader().get_ValueOld("AFGO_Month_ID") : this.getAFGO_Month_ID();
	}
	
	public boolean updateInvoicedAmount(int C_Invoice_ID)
	{
		String sql = "UPDATE AFGO_FundScheduleLine sl"
			+ " SET sl.InvoicedAmt=(SELECT COALESCE((SELECT SUM(LineNetAmt) FROM C_InvoiceLine il INNER JOIN C_Invoice i ON (i.C_Invoice_ID=il.C_Invoice_ID) WHERE il.AFGO_FundScheduleLine_ID=sl.AFGO_FundScheduleLine_ID AND (i.Processed='Y' OR i.DocStatus IN ('CO', 'CL') OR i.C_Invoice_ID=" + C_Invoice_ID + ")), 0) FROM DUAL)"
			+ " WHERE sl.AFGO_FundScheduleLine_ID=?";
		
		if (DB.executeUpdate(sql, this.getAFGO_FundScheduleLine_ID(), this.get_TrxName()) != 1)
			return false;
		
		//System.err.println("TRX=" + this.get_TrxName() + " LINE INVOICED AMT=" + DB.getSQLValue(this.get_TrxName(), "SELECT InvoicedAmt FROM AFGO_FundScheduleLine WHERE AFGO_FundScheduleLine_ID=" + this.getAFGO_FundScheduleLine_ID()));
		return this.updateHeader();
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
	
	public boolean beforeSave(boolean isNew)
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
			String sql = "UPDATE AFGO_FundSchedule s"
				+ " SET s.GrandTotal=(SELECT SUM(LineNetAmt) FROM AFGO_FundScheduleLine WHERE AFGO_FundSchedule_ID=s.AFGO_FundSchedule_ID)"
				+ " , s.InvoicedAmt=(SELECT SUM(InvoicedAmt) FROM AFGO_FundScheduleLine WHERE AFGO_FundSchedule_ID=s.AFGO_FundSchedule_ID)"
				+ " WHERE s.AFGO_FundSchedule_ID=?";
			
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_FundSchedule_ID());
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

	public int getAFGO_Month_ID ()
	{
		return this.getFundSchedule().getAFGO_Month_ID();
	}
	
	public int getC_Currentcy_ID ()
	{
		return this.getFundSchedule().getC_Currency_ID();
	}

	public int getM_Product_ID ()
	{
		return 0;
	}

	public int getC_Charge_ID ()
	{
		return this.getFundLine().getC_Charge_ID();
	}

	public BigDecimal getPrice ()
	{
		return this.getLineNetAmt();
	}

	public BigDecimal getQty ()
	{
		return BigDecimal.ONE;
	}

}
