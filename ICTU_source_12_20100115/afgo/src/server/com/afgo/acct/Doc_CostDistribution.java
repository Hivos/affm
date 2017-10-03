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
package com.afgo.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.compiere.acct.Doc;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.api.AccountingInterface;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;


import com.afgo.model.MAFGOCostDistr;
import com.afgo.model.MAFGOCostDistrAllocation;
import com.afgo.model.MAFGOCostDistrLine;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: Doc_CostDistribution.java,v 1.7.2.1 2010/01/06 11:45:34 tomassen Exp $
 *
 */
public class Doc_CostDistribution extends Doc
{
	public Doc_CostDistribution(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MAFGOCostDistr.class, rs, null, trxName);
	}
	
	private MAFGOCostDistr doc = null;
	
	private HashMap<DocLine_CostDistribution,MAFGOCostDistrLine> lines = null;
	
	
	public String getTrxName()
	{
		return super.getTrxName();
	}

	public String loadDocumentDetails()
	{
		this.doc = (MAFGOCostDistr)super.getPO();
		this.lines = new HashMap<DocLine_CostDistribution,MAFGOCostDistrLine>();
		for (Iterator<MAFGOCostDistrLine> lines = this.doc.getLines().iterator(); lines.hasNext();)
		{
			MAFGOCostDistrLine line = lines.next();
			this.lines.put(new DocLine_CostDistribution(line, this), line);
		}
		log.fine("Lines=" + this.lines.size());
		
		return null;
	}

	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}

	public ArrayList<Fact> createFacts(MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		Fact fact = new Fact(this, as, X_Fact_Acct.POSTINGTYPE_Actual);
		facts.add(fact);
		
		for (Iterator<DocLine_CostDistribution> docLines = this.lines.keySet().iterator(); docLines.hasNext();)
		{
			DocLine_CostDistribution docLine = docLines.next();
			MAFGOCostDistrLine line = this.lines.get(docLine);
			
			int From_Org_ID = line.getHeader().getProgram().getAD_Org_ID();
			int To_Org_ID = line.getHeader().getToProgram().getAD_Org_ID();
			
			MAccount ch_Expense_Acct = MCharge.getAccount(line.getC_Charge_ID(), as, line.getLineNetAmt());
			MAccount ch_Revenue_Acct = MCharge.getAccount(line.getC_Charge_ID(), as, line.getLineNetAmt().negate());
			
			MAccount ic_DueFrom_Acct = as.getDueFrom_Acct(MAcctSchemaElement.ELEMENTTYPE_Organization);
			MAccount ic_DueTo_Acct = as.getDueTo_Acct(MAcctSchemaElement.ELEMENTTYPE_Organization);
			
			FactLine icDueFrom = null;
			FactLine icDueTo = null;
			
			// AFGO_CostDistrLine
			FactLine factLine = fact.createLine(docLine, (From_Org_ID == To_Org_ID ? ch_Expense_Acct : ch_Revenue_Acct), docLine.getC_Currency_ID(), null, line.getLineNetAmt(), true);
			if (factLine == null)
				continue;
			factLine.setAD_Org_ID(From_Org_ID);
			factLine.setDateTrx(line.getHeader().getDateDoc());
			factLine.setDateAcct(line.getHeader().getDateAcct());
			
			// Intercompany Due From
			if (From_Org_ID != To_Org_ID)
			{
				factLine = fact.createLine(docLine, ic_DueFrom_Acct, docLine.getC_Currency_ID(), line.getLineNetAmt(), null, true);
				if (factLine == null)
					continue;
				factLine.setAD_Org_ID(From_Org_ID);
				factLine.setAD_OrgTrx_ID(To_Org_ID);
				factLine.setDateTrx(line.getHeader().getDateDoc());
				factLine.setDateAcct(line.getHeader().getDateAcct());
				
				icDueFrom = factLine;
			}
			
			// AFGO_CostDistrAllocation
			for (Iterator<MAFGOCostDistrAllocation> allocations = line.getAllocations().iterator(); allocations.hasNext();)
			{
				MAFGOCostDistrAllocation allocation = allocations.next();
				
				ch_Expense_Acct = MCharge.getAccount(allocation.getC_Charge_ID(), as, line.getLineNetAmt());
				
				factLine = fact.createLine(docLine, ch_Expense_Acct, docLine.getC_Currency_ID(), allocation.getLineNetAmt(), null, true);
				if (factLine == null)
					continue;
				factLine.setAD_Org_ID(line.getHeader().getToProgram().getAD_Org_ID());
				factLine.setDateTrx(line.getHeader().getDateDoc());
				factLine.setDateAcct(line.getHeader().getDateAcct());
				
				// Intercompany Due To
				if (icDueFrom != null && icDueTo == null)
				{
					factLine = fact.createLine(docLine, ic_DueTo_Acct, docLine.getC_Currency_ID(), null, line.getLineNetAmt(), true);
					if (factLine == null)
						continue;
					factLine.setAD_Org_ID(To_Org_ID);
					factLine.setAD_OrgTrx_ID(From_Org_ID);
					factLine.setDateTrx(line.getHeader().getDateDoc());
					factLine.setDateAcct(line.getHeader().getDateAcct());
					
					icDueTo = factLine;
				}
			}
			
		}
		
		return facts;
	}
	
	public static void main(String[] args)
	{
		int AD_Client_ID = 1000000;
		int AFGO_CostDistr_ID = 1000137;
		
		org.compiere.Compiere.startup(true);
		
		Ctx ctx = Env.getCtx();
		ctx.setAD_Client_ID(AD_Client_ID);
		
		MClient client = MClient.get(ctx);
		
		System.out.println("AD_Client_ID=" + client.getAD_Client_ID() + ", AFGO_CostDistr_ID=" + AFGO_CostDistr_ID);
		
		MAcctSchema[] ass = new MAcctSchema[]{MAcctSchema.get(ctx, client.getInfo().getC_AcctSchema1_ID())};
		
		String sql = "SELECT *"
			+ " FROM AFGO_CostDistr"
			+ " WHERE AFGO_CostDistr_ID=?"
			+ " AND AD_Client_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AFGO_CostDistr_ID);
			pstmt.setInt(2, client.getAD_Client_ID());
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{	
				Doc doc = new Doc_CostDistribution(ass, rs, null);
				doc.loadDocumentDetails();
				
				System.out.println("********************************");
				
				BigDecimal dr = Env.ZERO;
				BigDecimal cr = Env.ZERO;
				
				for (Iterator<Fact> facts = doc.createFacts(ass[0]).iterator(); facts.hasNext();)
				{
					System.out.println("FACT:");
					Fact fact = facts.next();
					
					FactLine[] lines = fact.getLines();
					
					for (int i = 0; i < lines.length; i++)
					{
						dr = dr.add(lines[i].getAmtAcctDr());
						cr = cr.add(lines[i].getAmtAcctCr());
						System.out.println(lines[i].getAD_Org_ID() + "	" + lines[i].getAccount().getDescription() + "	DR=" + lines[i].getAmtAcctDr() + ", CR=" + lines[i].getAmtAcctCr());
					}
					
				}
				
				System.out.println("********************************");
				System.out.println("dr=" + dr + ", cr=" + cr);
				System.out.println("********************************");
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

}
