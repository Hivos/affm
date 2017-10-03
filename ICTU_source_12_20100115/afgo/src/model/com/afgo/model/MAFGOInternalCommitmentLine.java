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
import java.sql.ResultSet;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFActivity;

import compiere.model.X_AFGO_InternalCommitmentLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOInternalCommitmentLine.java,v 1.11.2.1 2010/01/06 11:45:24 tomassen Exp $
 *
 */
public class MAFGOInternalCommitmentLine extends X_AFGO_InternalCommitmentLine implements AllocatableDocumentLine
{

	public MAFGOInternalCommitmentLine (Ctx ctx, int AFGO_InternalCommitmentLine_ID, String trxName)
	{
		super(ctx, AFGO_InternalCommitmentLine_ID, trxName);
	}
	
	public MAFGOInternalCommitmentLine (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private MAFGOInternalCommitment header = null;
	
	public MAFGOInternalCommitment getHeader()
	{
		if (this.header == null)
			this.header = new MAFGOInternalCommitment(this.getCtx(), this.getAFGO_InternalCommitment_ID(), this.get_TrxName());
		return this.header;
	}
	
	public PO getPO()
	{
		return this;
	}

	public int getM_Product_ID()
	{
		return 0;
	}

	public int get_OldAFGO_Month_ID()
	{
		return super.get_ValueOldAsInt("AFGO_Month_ID");
	}

	public MAFGOActivity getActivity()
	{
		return MAFGOActivity.getActivity(this.getCtx(), this.getAFGO_Activity_ID());
	}

	public MAFGOActivity getWorkflowActivity(MWFActivity workflowActivity)
	{
		return this.getActivity();
	}
	
	public boolean beforeSave(boolean newRecord)
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
	
	public void updateAllocatedAmt()
	{
		String sql = "SELECT SUM(cda.LineNetAmt)"
			+ " FROM AFGO_CostDistr cd"
			+ " INNER JOIN AFGO_CostDistrLine cdl ON (cdl.AFGO_CostDistr_ID=cd.AFGO_CostDistr_ID)"
			+ " INNER JOIN AFGO_CostDistrAllocation cda ON (cda.AFGO_CostDistrLine_ID=cdl.AFGO_CostDistrLine_ID)"
			+ " WHERE cda.AFGO_InternalCommitmentLine_ID=?"
			+ " AND cd.DocStatus IN ('CL', 'CO')";
		
		BigDecimal allocatedAmt = DB.getSQLValueBD(this.get_TrxName(), sql, this.getAFGO_InternalCommitmentLine_ID());

		this.setAllocatedAmt(allocatedAmt != null ? allocatedAmt : Env.ZERO);
	}
	
	
	private boolean updateHeader()
	{
		String sql = "UPDATE AFGO_InternalCommitment ic"
			+ " SET GrandTotal=(SELECT SUM(LineNetAmt) FROM AFGO_InternalCommitmentLine WHERE AFGO_InternalCommitment_ID=ic.AFGO_InternalCommitment_ID), "
			+ " AllocatedAmt=(SELECT SUM(AllocatedAmt) FROM AFGO_InternalCommitmentLine WHERE AFGO_InternalCommitment_ID=ic.AFGO_InternalCommitment_ID)"
			+ " WHERE ic.AFGO_InternalCommitment_ID=?";
		
		if (DB.executeUpdate(sql, this.getAFGO_InternalCommitment_ID(), this.get_TrxName()) != 1)
			return false;
		
		return true;
	}

}
