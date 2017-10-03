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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MasterPurchaseCommitment.java,v 1.6.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MasterPurchaseCommitment extends MAFGOPurchaseCommitment
{
	public MasterPurchaseCommitment(Ctx ctx, int AFGO_PurchaseCommitment_ID, String trxName)
	{
		super(ctx, AFGO_PurchaseCommitment_ID, trxName);
		
		if (!this.isMasterCommitment())
			throw new IllegalStateException("NoMasterCommitment");
	}
	
	private ArrayList<MAFGOPurchaseCommitment> additionalCommitments = null;
	
	private ArrayList<MAFGOPurchaseCommitment> transferCommitments = null;
	
	private ArrayList<MAFGOYear> orderedYears = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private synchronized ArrayList<MAFGOPurchaseCommitment> getLinkedCommitments()
	{
		ArrayList<MAFGOPurchaseCommitment> linkedCommitments = new ArrayList<MAFGOPurchaseCommitment>();

		String sql = "SELECT pc.*"
			+ " FROM AFGO_PurchaseCommitment pc"
			+ " INNER JOIN C_DocType dt ON (dt.C_DocType_ID=pc.C_DocType_ID)"
			+ " WHERE pc.MasterPurchaseCommitment_ID=?"
			+ " AND DocStatus IN ('CO', 'CL')"
			+ " AND PurchaseCommitmentStatus IN ('FR')";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
			
			rs = pstmt.executeQuery();

			while(rs.next())
			{
				MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), rs, this.get_TrxName());
				if (purchaseCommitment.getAFGO_PurchaseCommitment_ID() > 0 && purchaseCommitment.getMasterPurchaseCommitment_ID() == this.getAFGO_PurchaseCommitment_ID())
					linkedCommitments.add(purchaseCommitment);
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
			if (rs!= null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}

		return linkedCommitments;
	}
	
	public ArrayList<MAFGOPurchaseCommitment> getAdditionalCommitments()
	{
		return this.getAdditionalCommitments(false);
	}
	
	public ArrayList<MAFGOPurchaseCommitment> getAdditionalCommitments(boolean reload)
	{
		if (reload)
			this.additionalCommitments = null;
		
		if (this.additionalCommitments == null)
		{
			this.additionalCommitments = new ArrayList<MAFGOPurchaseCommitment>();
			for (Iterator<MAFGOPurchaseCommitment> linkedCommitments = this.getLinkedCommitments().iterator(); linkedCommitments.hasNext();)
			{
				MAFGOPurchaseCommitment linkedCommitment = linkedCommitments.next();
				if (linkedCommitment.isAdditionalCommitment())
					this.additionalCommitments.add(linkedCommitment);
			}
		}
		
		return this.additionalCommitments;
	}
	
	public ArrayList<MAFGOPurchaseCommitment> getTransferCommitments()
	{
		return this.getTransferCommitments(false);
	}
	
	public ArrayList<MAFGOPurchaseCommitment> getTransferCommitments(boolean reload)
	{
		if (reload)
			this.transferCommitments = null;
		
		if (this.transferCommitments == null && this.isMasterCommitment())
		{
			this.transferCommitments = new ArrayList<MAFGOPurchaseCommitment>();
			for (Iterator<MAFGOPurchaseCommitment> linkedCommitments = this.getLinkedCommitments().iterator(); linkedCommitments.hasNext();)
			{
				MAFGOPurchaseCommitment linkedCommitment = linkedCommitments.next();
				if (linkedCommitment.isTransferCommitment())
					this.transferCommitments.add(linkedCommitment);
			}
		}
		
		return this.transferCommitments;
	}
	
	
	public ArrayList<MAFGOPurchaseCommitmentLine> getLines()
	{
		ArrayList<MAFGOPurchaseCommitmentLine> lines = new ArrayList<MAFGOPurchaseCommitmentLine>();
		
		lines.addAll(super.getLines());
		
		ArrayList<MAFGOPurchaseCommitment> additionalCommitments = this.getAdditionalCommitments();
		if (additionalCommitments != null)
		{
			for (Iterator<MAFGOPurchaseCommitment> commitments = additionalCommitments.iterator(); commitments.hasNext();)
			{
				MAFGOPurchaseCommitment additionalCommitment = commitments.next();
				lines.addAll(additionalCommitment.getLines());
			}
		}
		
		return lines;
	}
	
	public ArrayList<MAFGOMonth> getMonths()
	{
		ArrayList<MAFGOMonth> months = new ArrayList<MAFGOMonth>();
		
		months.addAll(super.getMonths());
		
		for (Iterator<MAFGOPurchaseCommitment> additionalCommitments = this.getAdditionalCommitments().iterator(); additionalCommitments.hasNext();)
		{
			months.addAll(additionalCommitments.next().getMonths());
		}
		
		return months;
	}
	
	public ArrayList<MAFGOYear> getOrderedYears()
	{
		if (this.orderedYears == null)
		{
			this.orderedYears = new ArrayList<MAFGOYear>();
			
			String sql = "SELECT AFGO_Year_ID"
				+ " FROM AFGO_Year"
				+ " WHERE AFGO_Year_ID IN"
				+ " (" 
				+ " 	SELECT y.AFGO_Year_ID"
				+ " 	FROM AFGO_Year y"
				+ " 	INNER JOIN AFGO_Quarter q ON (q.AFGO_Year_ID=y.AFGO_Year_ID)"
				+ " 	INNER JOIN AFGO_Month m ON (m.AFGO_Quarter_ID=q.AFGO_Quarter_ID)"
				+ "	 	INNER JOIN AFGO_Calendar c ON (c.AFGO_Calendar_ID=y.AFGO_Calendar_ID)"
				+ " 	INNER JOIN AFGO_Program p ON (p.AFGO_Calendar_ID=c.AFGO_Calendar_ID)"
				+ " 	WHERE m.AFGO_Month_ID IN"
				+ " 	("
				+ " 		SELECT DISTINCT mpcl.AFGO_Month_ID"
				+ " 		FROM AFGO_PurchaseCommitment mc"
				+ " 		LEFT OUTER JOIN AFGO_PurchaseCommitmentLine mpcl ON (mpcl.AFGO_PurchaseCommitment_ID=mc.AFGO_PurchaseCommitment_ID)"
				+ " 		WHERE mc.AFGO_PurchaseCommitment_ID=?"
				+ " 		AND mc.AFGO_Program_ID=p.AFGO_Program_ID"
				+ " 		UNION "
				+ " 		SELECT DISTINCT apcl.AFGO_Month_ID"
				+ " 		FROM AFGO_PurchaseCommitment ac"
				+ " 		INNER JOIN AFGO_PurchaseCommitmentLine apcl ON (apcl.AFGO_PurchaseCommitment_ID=ac.AFGO_PurchaseCommitment_ID)"
				+ " 		INNER JOIN C_DocType dt ON (dt.C_DocType_ID=ac.C_DocType_ID)"
				+ " 		INNER JOIN AFGO_PurchaseCommitmentType pct ON (pct.AFGO_PurchaseCommitmentType_ID=dt.AFGO_PurchaseCommitmentType_ID)"
				+ " 		WHERE ac.MasterPurchaseCommitment_ID=?"
				+ " 		AND ac.DocStatus IN ('CO', 'CL')"
				+ " 		AND ac.PurchaseCommitmentStatus IN ('FR')"
				+ " 		AND pct.IsAdditionalCommitment='Y'"
				+ " 	)"
				+ " )"
				+ " ORDER BY StartDate";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
				pstmt.setInt(2, this.getAFGO_PurchaseCommitment_ID());
				
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					int AFGO_Year_ID = rs.getInt(1);
					MAFGOYear year = MAFGOYear.getYear(this.getCtx(), AFGO_Year_ID);
					this.orderedYears.add(year);
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
				if (rs!= null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}

		}
		
		return this.orderedYears;
	}
	
	public Timestamp getDateFrom()
	{
		Timestamp dateFrom = super.getDateFrom();
		
		for (Iterator<MAFGOPurchaseCommitment> additionalCommitments = this.getAdditionalCommitments().iterator(); additionalCommitments.hasNext();)
		{
			MAFGOPurchaseCommitment additionalCommitment = additionalCommitments.next();
			
			if (additionalCommitment.getDateFrom().before(dateFrom))
			{
				dateFrom = additionalCommitment.getDateFrom();
			}
		}
		
		return dateFrom;
	}
	
	public Timestamp getDateTo()
	{
		Timestamp dateTo = super.getDateTo();
		
		for (Iterator<MAFGOPurchaseCommitment> additionalCommitments = this.getAdditionalCommitments().iterator(); additionalCommitments.hasNext();)
		{
			MAFGOPurchaseCommitment additionalCommitment = additionalCommitments.next();
			
			if (additionalCommitment.getDateTo().after(dateTo))
			{
				dateTo = additionalCommitment.getDateTo();
			}
		}
		
		return dateTo;
	}
	
	public Date getMinimumCloseDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDateTo());
		cal.add(Calendar.DATE, this.getProgram().getCommitmentGraceDays());
		
		return cal.getTime();
	}
	
	public BigDecimal getOpenAmt(int AFGO_Year_ID)
	{
		BigDecimal openAmt = Env.ZERO;
		
		String sql = "SELECT COALESCE(SUM(OpenAmt), 0)"
			+ " FROM AFGO_RV_TransferableCommitment"
			+ " WHERE AFGO_PurchaseCommitment_ID=?"
			+ " AND AFGO_Year_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_PurchaseCommitment_ID());
			pstmt.setInt(2, AFGO_Year_ID);
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				openAmt = rs.getBigDecimal(1);
			}
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.severe(e.toString());
		}
		finally
		{
			if (rs!= null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		
		return openAmt;
	}
	
	
	public void closeDocument()
	{
		for (Iterator<MAFGOPurchaseCommitment> linkedCommitments = this.getLinkedCommitments().iterator(); linkedCommitments.hasNext();)
		{
			MAFGOPurchaseCommitment linkedCommitment = linkedCommitments.next();
			linkedCommitment.setIsClosed(true);
			linkedCommitment.save();
		}
		
		this.setIsClosed(true);
		this.save();
	}
}
