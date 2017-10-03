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

import java.sql.Timestamp;
import java.util.Iterator;

import org.compiere.model.MDocType;
import org.compiere.model.MOrg;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereUserException;
import org.compiere.util.ValueNamePair;
import org.compiere.vos.DocActionConstants;

import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOPurchaseCommitment;
import com.afgo.model.MAFGOPurchaseCommitmentLine;
import com.afgo.model.MAFGOPurchaseCommitmentType;
import com.afgo.model.MAFGOPurchaseDomain;
import com.afgo.model.MAFGOPurchaseLot;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ResetPurchaseCommitment.java,v 1.10.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ResetPurchaseCommitment extends SvrProcess
{
	private int AFGO_PurchaseCommitment_ID = 0;
	
	private int AD_Org_ID = 0;
	
	private int AFGO_Program_ID = 0;
	
	private int C_DocType_ID = 0;
	
	private int MasterPurchaseCommitment_ID = 0;
	
	private Boolean specific = null;
	
	private int AFGO_PurchaseDomain_ID = 0;
	
	private int AFGO_PurchaseLot_ID = 0;
	
	private Timestamp DateForm = null;
	
	private Timestamp DateTo = null;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AFGO_PurchaseCommitment_ID"))
				this.AFGO_PurchaseCommitment_ID= para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				this.C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("MasterPurchaseCommitment_ID"))
				this.MasterPurchaseCommitment_ID = para[i].getParameterAsInt();
			else if (name.equals("IsSpecific"))
			{
				if ("Y".equals(para[i].getParameter()))
					this.specific = true;
				else if ("N".equals(para[i].getParameter()))
					this.specific = false;
			}
			else if (name.equals("AFGO_PurchaseDomain_ID"))
				this.AFGO_PurchaseDomain_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_PurchaseLot_ID"))
				this.AFGO_PurchaseLot_ID = para[i].getParameterAsInt();
			else if (name.equals("DateFrom"))
				this.DateForm = (Timestamp)para[i].getParameter();
			else if (name.equals("DateTo"))
				this.DateTo = (Timestamp)para[i].getParameter();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		int AFGO_PurchaseCommitment_ID = (this.AFGO_PurchaseCommitment_ID > 0 ? this.AFGO_PurchaseCommitment_ID : this.getRecord_ID());
		MAFGOPurchaseCommitment purchaseCommitment = new MAFGOPurchaseCommitment(this.getCtx(), AFGO_PurchaseCommitment_ID, this.get_TrxName());
		log.info("AFGO_PurchaseCommitment_ID=" + AFGO_PurchaseCommitment_ID);
		if (purchaseCommitment.getAFGO_PurchaseCommitment_ID() < 1 || purchaseCommitment.getAFGO_PurchaseCommitment_ID() != AFGO_PurchaseCommitment_ID)
			throw new IllegalStateException("Invalid AFGO_PurchaseCommitment_ID: " + AFGO_PurchaseCommitment_ID);
		
		if (!(DocActionConstants.STATUS_Invalid.equals(purchaseCommitment.getDocStatus()) || DocActionConstants.STATUS_NotApproved.equals(purchaseCommitment.getDocStatus())))
			throw new IllegalStateException("InvalidStatus");
		
		if (purchaseCommitment.isProcessing())
			throw new IllegalStateException("DocumentProcessing");
		
		MOrg org = MOrg.get(this.getCtx(), (this.AD_Org_ID > 0 ? this.AD_Org_ID : purchaseCommitment.getAD_Org_ID()));
		log.info("AD_Org_ID: " + purchaseCommitment.getAD_Org_ID() + "->" + org.getAD_Org_ID());
		
		MAFGOProgram program = MAFGOProgram.getProgram(this.getCtx(), (this.AFGO_Program_ID > 0 ? this.AFGO_Program_ID : purchaseCommitment.getAFGO_Program_ID()));
		if (program.getAD_Org_ID() != org.getAD_Org_ID())
			throw new IllegalStateException("Program/Organization mismatch: AD_Org_ID=" + org.getAD_Org_ID() + ", AFGO_Program_ID=" + program.getAFGO_Program_ID());
		log.info("AFGO_Program_ID: " + purchaseCommitment.getAFGO_Program_ID() + "->" + program.getAFGO_Program_ID());
		
		MDocType docType = (MDocType.get(this.getCtx(), (this.C_DocType_ID > 0 ? this.C_DocType_ID : purchaseCommitment.getC_DocType_ID())));
		if (!"XPC".equals(docType.getDocBaseType()))
			throw new IllegalStateException("Invalid C_DocType_ID: " + docType.getC_DocType_ID());
		log.info("C_DocType_ID: " + purchaseCommitment.getC_DocType_ID() + "->" + docType.getC_DocType_ID());
		
		MAFGOPurchaseCommitment masterCommitment = null;
		if (this.MasterPurchaseCommitment_ID > 0)
			 masterCommitment = new MAFGOPurchaseCommitment(this.getCtx(), this.MasterPurchaseCommitment_ID, this.get_TrxName());
		else if (purchaseCommitment.getMasterPurchaseCommitment_ID() > 0)
			 masterCommitment = new MAFGOPurchaseCommitment(this.getCtx(), purchaseCommitment.getMasterPurchaseCommitment_ID(), this.get_TrxName());
		if (masterCommitment != null && masterCommitment.getAFGO_Program_ID() != program.getAFGO_Program_ID())
			throw new IllegalStateException("Invalid MasterCommitment_ID: " + masterCommitment.getAFGO_PurchaseCommitment_ID());
		
		boolean specific = (this.specific != null ? this.specific : purchaseCommitment.isSpecific());
		log.info("IsSpecific: " + purchaseCommitment.isSpecific() + "->" + specific);
		
		int AFGO_PurchaseDomain_ID = (this.AFGO_PurchaseDomain_ID > 0 ? this.AFGO_PurchaseDomain_ID : purchaseCommitment.getAFGO_PurchaseDomain_ID());
		if (masterCommitment != null)
		{
			AFGO_PurchaseDomain_ID = masterCommitment.getAFGO_PurchaseDomain_ID();
		}
		MAFGOPurchaseLot purchaseLot = null;
		if (AFGO_PurchaseDomain_ID > 0)
		{
			MAFGOPurchaseDomain purchaseDomain = MAFGOPurchaseDomain.getPurchaseDomain(this.getCtx(), AFGO_PurchaseDomain_ID);
    		log.info("AFGO_PurchaseDomain_ID: " + purchaseCommitment.getAFGO_PurchaseDomain_ID() + "->" + purchaseDomain.getAFGO_PurchaseDomain_ID());
    		
    		int AFGO_PurchaseLot_ID = (this.AFGO_PurchaseLot_ID > 0 ? this.AFGO_PurchaseLot_ID : purchaseCommitment.getAFGO_PurchaseLot_ID());
    		if (masterCommitment != null)
    		{
    			AFGO_PurchaseLot_ID = masterCommitment.getAFGO_PurchaseLot_ID();
    		}
    		purchaseLot = MAFGOPurchaseLot.getPurchaseLot(this.getCtx(), AFGO_PurchaseLot_ID);
    		if (purchaseLot.getAFGO_PurchaseDomain_ID() != purchaseDomain.getAFGO_PurchaseDomain_ID())
    			throw new IllegalStateException("PurchaseDomain/PurchaseLot mismatch: AFGO_PurchaseDomain_ID=" + purchaseDomain.getAFGO_PurchaseDomain_ID() + ", AFGO_PurchaseLot_ID=" + purchaseLot.getAFGO_PurchaseLot_ID());
    		log.info("AFGO_PurchaseLot_ID: " + purchaseCommitment.getAFGO_PurchaseLot_ID() + "->" + purchaseLot.getAFGO_PurchaseLot_ID());
		}
		
		Timestamp DateFrom = (this.DateForm != null ? this.DateForm : purchaseCommitment.getDateFrom());
		log.info("DateFrom: " + purchaseCommitment.getDateFrom() + "->" + DateFrom);
		
		Timestamp DateTo = (this.DateTo != null ? this.DateTo : purchaseCommitment.getDateTo());
		log.info("DateTo: " + purchaseCommitment.getDateTo() + "->" + DateTo);
		
		purchaseCommitment.setDocStatus(DocActionConstants.STATUS_Drafted);
		purchaseCommitment.setDocAction(DocActionConstants.ACTION_Complete);
		purchaseCommitment.setProcessed(false);
		purchaseCommitment.setPurchaseCommitmentStatus(MAFGOPurchaseCommitment.PURCHASECOMMITMENTSTATUS_Drafted);
		purchaseCommitment.setC_BPartner_ID(0);
		purchaseCommitment.setAFGO_QuotationResponse_ID(0);
		
		if (purchaseCommitment.getAD_Org_ID() != org.getAD_Org_ID())
			purchaseCommitment.setAD_Org_ID(org.getAD_Org_ID());
		if (purchaseCommitment.getAFGO_Program_ID() != program.getAFGO_Program_ID())
			purchaseCommitment.setAFGO_Program_ID(program.getAFGO_Program_ID());
		if (purchaseCommitment.isSpecific() != specific)
			purchaseCommitment.setIsSpecific(specific);
		if (!purchaseCommitment.getDateFrom().equals(DateFrom))
			purchaseCommitment.setDateFrom(DateFrom);
		if (!purchaseCommitment.getDateTo().equals(DateTo))
			purchaseCommitment.setDateTo(DateTo);
		
		if (purchaseCommitment.getC_DocType_ID() != docType.getC_DocType_ID())
		{
			purchaseCommitment.setC_DocType_ID(docType.getC_DocType_ID());
		}
		
		if (masterCommitment != null)
		{
    		if (purchaseCommitment.getMasterPurchaseCommitment_ID() != masterCommitment.getAFGO_PurchaseCommitment_ID())
    		{
    			purchaseCommitment.setMasterPurchaseCommitment_ID(masterCommitment.getAFGO_PurchaseCommitment_ID());
    			purchaseCommitment.setC_BPartner_ID(masterCommitment.getC_BPartner_ID());
    		}
		}
		
		MAFGOPurchaseCommitmentType purchaseCommitmentType = MAFGOPurchaseCommitmentType.getPurchaseCommitmentType(docType);
		
		if (purchaseCommitmentType.isMasterCommitment() && !purchaseCommitmentType.isPurchaseDomain())
		{
			purchaseCommitment.setAFGO_PurchaseDomain_ID(0);
			purchaseCommitment.setAFGO_PurchaseLot_ID(0);
		}
		
		else if (!purchaseCommitmentType.isMasterCommitment() && masterCommitment != null)
		{
			if (!masterCommitment.getPurchaseCommitmentType().isPurchaseDomain())
			{
				purchaseCommitment.setAFGO_PurchaseDomain_ID(0);
				purchaseCommitment.setAFGO_PurchaseLot_ID(0);
			}
		}

		else if (purchaseLot != null)
		{
			if (purchaseCommitment.getAFGO_PurchaseDomain_ID() != purchaseLot.getAFGO_PurchaseDomain_ID())
				purchaseCommitment.setAFGO_PurchaseDomain_ID(purchaseLot.getAFGO_PurchaseDomain_ID());
			if (purchaseCommitment.getAFGO_PurchaseLot_ID() != purchaseLot.getAFGO_PurchaseLot_ID())
				purchaseCommitment.setAFGO_PurchaseLot_ID(purchaseLot.getAFGO_PurchaseLot_ID());
		}
		else
			throw new IllegalStateException("No PurchaseDomainLot");


		boolean result = purchaseCommitment.save(); 
		
		//purchaseCommitment.get
		
		//
		int linesDeleted = 0;
		if (result)
		{
    		for (Iterator<MAFGOPurchaseCommitmentLine> lines = purchaseCommitment.getLines().iterator(); lines.hasNext();)
    		{
    			MAFGOPurchaseCommitmentLine line = lines.next();
    			if (line.delete(true, this.get_TrxName()))
    				linesDeleted++;
    		}
		}
		else
		{
			String msg = "@Error@ ";
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				msg = pp.getName() + " - ";
			throw new CompiereUserException (msg);
		}
		
		return "@Udpated@=" + result + ", @LinesDeleted@=" + linesDeleted;
	}

}
