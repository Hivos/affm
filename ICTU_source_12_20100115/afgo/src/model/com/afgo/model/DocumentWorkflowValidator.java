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

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MInvoice;
import org.compiere.model.MMessage;
import org.compiere.model.MOrg;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;

import com.afgo.util.UserNotification;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DocumentWorkflowValidator.java,v 1.11.2.1 2010/01/06 11:45:28 tomassen Exp $
 * 
 * Send Notification / EMail to document creator,
 * when a document is rejected or invalidated during WF execution
 *
 */
public class DocumentWorkflowValidator implements ModelValidator
{
	
	public DocumentWorkflowValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("DocumentWorkflowValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		
		// C_Invoice
		this.engine.addModelChange(MInvoice.Table_Name, this);
		
		// AFGO_Budget
		this.engine.addModelChange(MAFGOBudget.Table_Name, this);
		
		// AFGO_CostDistr
		this.engine.addModelChange(MAFGOCostDistr.Table_Name, this);
		
		// AFGO_Fund
		this.engine.addModelChange(MAFGOFund.Table_Name, this);
		
		// AFGO_FundSchedule
		this.engine.addModelChange(MAFGOFundSchedule.Table_Name, this);
		
		// AFGO_InternalCommitment
		this.engine.addModelChange(MAFGOInternalCommitment.Table_Name, this);
		
		// AFGO_PurchaseCommitment
		this.engine.addModelChange(MAFGOPurchaseCommitment.Table_Name, this);
		
		// AFGO_QuotationRequest
		this.engine.addModelChange(MAFGOQuotationRequest.Table_Name, this);
		
		// AFGO_QuotationResponse
		this.engine.addModelChange(MAFGOQuotationResponse.Table_Name, this);
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	

	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String modelChange (PO po, int type) throws Exception
	{
		DocAction doc = (DocAction)po;
		
		String oldDocStatus = (String)po.get_ValueOld("DocStatus");
		String newDocStatus = (String)po.get_Value("DocStatus");
		
		if (newDocStatus != null && !newDocStatus.equals(oldDocStatus))
		{
			UserNotification notification = null;

			String subject = null;
			
			MOrg docOrg = MOrg.get(doc.getCtx(), doc.getAD_Org_ID());
			
			if (DocActionConstants.STATUS_NotApproved.equals(newDocStatus))
			{
				subject = Msg.getMsg(po.getCtx(), "DocRejected") + ": " + doc.getDocumentInfo();
				notification = new UserNotification(po.getCtx(), docOrg.getInfo().getSupervisor_ID(), doc.getDoc_User_ID(), MMessage.get(doc.getCtx(), "DocRejected"), subject, subject, po);
			}
			else if (DocActionConstants.STATUS_Invalid.equals(newDocStatus))
			{
				subject = Msg.getMsg(po.getCtx(), "DocInvalidated") + ": " + doc.getDocumentInfo();
				notification = new UserNotification(po.getCtx(), docOrg.getInfo().getSupervisor_ID(), doc.getDoc_User_ID(), MMessage.get(doc.getCtx(), "DocInvalidated"), subject, subject, po);
			}
			if (notification != null)
			{
				notification.send();
			}
		}
		
		return null;
	}
	
	public String docValidate (PO doc, int timing)
	{
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
