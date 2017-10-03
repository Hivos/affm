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

import java.util.ArrayList;
import java.util.Iterator;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DocumentPeriodValidator.java,v 1.13.2.1 2010/01/06 11:45:27 tomassen Exp $
 * 
 * Prevent "moving" document (lines) from/to closed periods
 * (AFGO_Calendar)
 *
 */
public class DocumentPeriodValidator implements ModelValidator
{	
	public DocumentPeriodValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("DocumentPeriodValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		// AFGO_Budget
		engine.addDocValidate(MAFGOBudget.Table_Name, this);
		engine.addModelChange(MAFGOBudget.Table_Name, this);
		engine.addModelChange(MAFGOBudgetLine.Table_Name, this);
		
		// AFGO_PurchaseCommitment
		engine.addDocValidate(MAFGOPurchaseCommitment.Table_Name, this);
		engine.addModelChange(MAFGOPurchaseCommitment.Table_Name, this);
		engine.addModelChange(MAFGOPurchaseCommitmentLine.Table_Name, this);
		
		// AFGO_FundSchedule
		engine.addDocValidate(MAFGOFundSchedule.Table_Name, this);
		engine.addModelChange(MAFGOFundSchedule.Table_Name, this);
		engine.addModelChange(MAFGOFundScheduleLine.Table_Name, this);
		
		// AFGO_InternalCommitment
		engine.addDocValidate(MAFGOInternalCommitment.Table_Name, this);
		engine.addModelChange(MAFGOInternalCommitment.Table_Name, this);
		engine.addModelChange(MAFGOInternalCommitmentLine.Table_Name, this);
		
		// AFGO_CostDistr
		engine.addDocValidate(MAFGOCostDistr.Table_Name, this);
		engine.addModelChange(MAFGOCostDistr.Table_Name, this);
		engine.addModelChange(MAFGOCostDistrLine.Table_Name, this);
		engine.addModelChange(MAFGOCostDistrAllocation.Table_Name, this);
		
		// C_Invoice
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addModelChange(MInvoice.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String modelChange (PO po, int type) throws Exception
	{
		return this.validatePO(po);
	}
	
	public String docValidate (PO po, int docTiming)
	{
		return this.validatePO(po);
	}
	
	private String validatePO(PO po)
	{
		try
		{
			// Header
			AllocatableDocument doc = AFGOModelValidator.castAllocatableDocument(po);
			if (doc != null)
				return this.validateHeader(doc);
			
			// Line
			AllocatableDocumentLine line= AFGOModelValidator.castAllocatableDocumentLine(po);
			if (line != null)
				return this.validateLine(line);
			
			return(Msg.getMsg(po.getCtx(), "DocNoPeriodControl") + ": " + doc);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String validateHeader(AllocatableDocument doc)
	{
		StringBuffer sb = null;
		if (doc instanceof MAFGOFundSchedule)
		{
			
			MAFGOFundSchedule fundSchedule = (MAFGOFundSchedule)doc;
			return this.validatePeriod(fundSchedule.getOldMonth(), fundSchedule.getMonth());
		}
		for (Iterator<? extends AllocatableDocumentLine> lines = doc.getLines().iterator(); lines.hasNext();)
		{
			AllocatableDocumentLine line = lines.next();
			String periodInfo = this.validateLine(line);
			if (periodInfo == null)
				continue;
			if (sb == null)
				sb = new StringBuffer();
			else
				sb.append("\n");
			sb.append(periodInfo);
		}
		
		return (sb != null) ? sb.toString() : null;
	}
	
	private String validateLine(AllocatableDocumentLine line)
	{
		MAFGOMonth oldMonth = MAFGOMonth.getMonth(line.getHeader().getCtx(), line.get_OldAFGO_Month_ID());
		MAFGOMonth newMonth = MAFGOMonth.getMonth(line.getHeader().getCtx(), line.getAFGO_Month_ID());
		
		return this.validatePeriod(oldMonth, newMonth);
	}
	
	private String validatePeriod(MAFGOMonth oldMonth, MAFGOMonth newMonth)
	{
		if (oldMonth != null && !oldMonth.isPeriodOpen())
			return Msg.getMsg(oldMonth.getCtx(), "PeriodClosed") + ": " + oldMonth.getName();
		
		if (newMonth != null && !newMonth.isPeriodOpen())
			return Msg.getMsg(newMonth.getCtx(), "PeriodClosed") + ": " + newMonth.getName();
		
		return null;
	}


	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
