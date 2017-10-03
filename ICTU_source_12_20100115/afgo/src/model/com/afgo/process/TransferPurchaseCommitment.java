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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import com.afgo.model.MAFGOQuarter;
import com.afgo.model.MAFGOYear;
import com.afgo.model.MasterPurchaseCommitment;
import com.afgo.model.TransferredPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: TransferPurchaseCommitment.java,v 1.5.2.1 2010/01/06 11:45:33 tomassen Exp $
 *
 */
public class TransferPurchaseCommitment extends SvrProcess
{
	private int AFGO_Program_ID = -1;
	
	private int AFGO_ProjectCluster_ID	= -1;
	
	private int AFGO_Project_ID = -1;
	
	private int AFGO_Phase_ID = -1;
	
	private int AFGO_Activity_ID = -1;
	
	private int fromAFGO_Year_ID = -1;
	
	private int fromAFGO_Quarter_ID = -1;
	
	private int fromAFGO_Month_ID = -1;
	
	private int AFGO_PurchaseCommitment_ID = -1;
	
	private int C_DocType_ID = -1;
	
	private int toAFGO_Year_ID = -1;
	
	private int toAFGO_Quarter_ID = -1;
	
	private int toAFGO_Month_ID = -1;
	
	CLogger log = CLogger.getCLogger(getClass());
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_ProjectCluster_ID"))
				this.AFGO_ProjectCluster_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Project_ID"))
				this.AFGO_Project_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Phase_ID"))
				this.AFGO_Phase_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Activity_ID"))
				this.AFGO_Activity_ID = para[i].getParameterAsInt();
			else if (name.equals("FromYear_ID"))
				this.fromAFGO_Year_ID = para[i].getParameterAsInt();
			else if (name.equals("FromQuarter_ID"))
				this.fromAFGO_Quarter_ID = para[i].getParameterAsInt();
			else if (name.equals("FromMonth_ID"))
				this.fromAFGO_Month_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_PurchaseCommitment_ID"))
				this.AFGO_PurchaseCommitment_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				this.C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("ToYear_ID"))
				this.toAFGO_Year_ID = para[i].getParameterAsInt();
			else if (name.equals("ToQuarter_ID"))
				this.toAFGO_Quarter_ID = para[i].getParameterAsInt();
			else if (name.equals("ToMonth_ID"))
				this.toAFGO_Month_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		MasterPurchaseCommitment pc = null;
		TransferredPurchaseCommitment transfer = null;
		int lines = 0;
		
		int toAFGO_Month_ID = this.toAFGO_Month_ID;
		if (toAFGO_Month_ID < 1 && this.toAFGO_Quarter_ID > 1)
		{
			toAFGO_Month_ID = MAFGOQuarter.getQuarter(this.getCtx(), this.toAFGO_Quarter_ID).getAFGO_Month_ID();
		}
		if (toAFGO_Month_ID < 1 && this.toAFGO_Year_ID > 1)
		{
			toAFGO_Month_ID = MAFGOYear.getYear(this.getCtx(), this.toAFGO_Year_ID).getQuarter().getAFGO_Month_ID();
		}
		
		String sql = "SELECT AFGO_PurchaseCommitment_ID, "
			+ " AFGO_Activity_ID, AFGO_ServiceType_ID, M_Product_ID, C_Charge_ID, AFGO_Month_ID, OpenAmt"
			+ " FROM AFGO_RV_TransferableCommitment"
			+ " WHERE AD_Client_ID=?"
			+ " AND OpenAmt <> 0";
				
		if (this.AFGO_Program_ID > 0)
			sql = sql + " AND AFGO_Program_ID=?";
		
		sql = this.appendSQL(sql);
		
		sql = sql + " ORDER BY AFGO_PurchaseCommitment_ID, AFGO_Month_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAD_Client_ID());
			
			int idx = 2;
			
			if (this.AFGO_Program_ID > 0)
			{
				pstmt.setInt(idx, this.AFGO_Program_ID);
				idx++;
			}
			
			this.setParameters(pstmt, idx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				int AFGO_PurchaseCommitment_ID = rs.getInt(1);
				int AFGO_Activity_ID = rs.getInt(2);
				int AFGO_ServiceType_ID = rs.getInt(3);
				int M_Product_ID = rs.getInt(4);
				int C_Charge_ID = rs.getInt(5);
				int AFGO_Month_ID = rs.getInt(6);

				BigDecimal openAmt = rs.getBigDecimal(7);
				BigDecimal transferAmt = this.isSamePeriod() ? Env.ZERO : openAmt;
				BigDecimal closeAmt = this.isSamePeriod() ? openAmt : Env.ZERO;
				
				if (pc == null || pc.getAFGO_PurchaseCommitment_ID() != AFGO_PurchaseCommitment_ID)
				{
					if (transfer != null && lines > 0)
					{
						transfer.save();
						this.addLog(transfer.getDocumentNo());
					}
					
					lines = 0;
					pc = new MasterPurchaseCommitment(this.getCtx(), AFGO_PurchaseCommitment_ID, this.get_TrxName());
					transfer = new TransferredPurchaseCommitment(pc, this.C_DocType_ID);
					
				}
				
				lines = lines + transfer.transferCommitment(AFGO_Activity_ID, AFGO_ServiceType_ID, M_Product_ID, C_Charge_ID, AFGO_Month_ID, toAFGO_Month_ID, transferAmt, closeAmt);
			}
			
			if (transfer != null && lines > 0)
			{
				transfer.save();
				this.addLog(transfer.getDocumentNo());
			}
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return null;
	}
	
	private String appendSQL(String sql)
	{
		if (this.fromAFGO_Year_ID > 0)
			sql = sql + " AND AFGO_Year_ID=?";
		if (this.fromAFGO_Quarter_ID > 0)
			sql = sql + " AND AFGO_Quarter_ID=?";
		if (this.fromAFGO_Month_ID > 0)
			sql = sql + " AND AFGO_Month_ID=?";
		
		if (this.AFGO_ProjectCluster_ID > 0)
			sql = sql + " AND AFGO_ProjectCluster_ID=?";
		if (this.AFGO_Project_ID > 0)
			sql = sql + " AND AFGO_Project_ID=?";
		if (this.AFGO_Phase_ID > 0)
			sql = sql + " AND AFGO_Phase_ID=?";
		if (this.AFGO_Activity_ID > 0)
			sql = sql + " AND AFGO_Activity_ID=?";
		
		if (this.AFGO_PurchaseCommitment_ID > 0)
			sql = sql + " AND AFGO_PurchaseCommitment_ID=?";
		
		return sql;
	}
	
	private void setParameters(PreparedStatement pstmt, int idx) throws SQLException
	{
		if (this.fromAFGO_Year_ID > 0)
		{
			pstmt.setInt(idx, this.fromAFGO_Year_ID);
			idx++;
		}
		if (this.fromAFGO_Quarter_ID > 0)
		{
			pstmt.setInt(idx, this.fromAFGO_Quarter_ID);
			idx++;
		}
		if (this.fromAFGO_Month_ID > 0)
		{
			pstmt.setInt(idx, this.fromAFGO_Month_ID);
			idx++;
		}

		if (this.AFGO_ProjectCluster_ID > 0)
		{
			pstmt.setInt(idx, this.AFGO_ProjectCluster_ID);
			idx++;
		}
		if (this.AFGO_Project_ID > 0)
		{
			pstmt.setInt(idx, this.AFGO_Project_ID);
			idx++;
		}
		if (this.AFGO_Phase_ID > 0)
		{
			pstmt.setInt(idx, this.AFGO_Phase_ID);
			idx++;
		}
		if (this.AFGO_Activity_ID > 0)
		{
			pstmt.setInt(idx, this.AFGO_Activity_ID);
			idx++;
		}
		
		if (this.AFGO_PurchaseCommitment_ID > 0)
		{
			pstmt.setInt(idx, this.AFGO_PurchaseCommitment_ID);
			idx++;
		}
	}
	
	private boolean isSamePeriod()
	{
		if (this.toAFGO_Month_ID != this.fromAFGO_Month_ID)
			return false;
		if (this.toAFGO_Quarter_ID != this.fromAFGO_Quarter_ID)
			return false;
		if (this.toAFGO_Year_ID != this.fromAFGO_Year_ID)
			return false;
		return true;
	}
	
}
