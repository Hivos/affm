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

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrg;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.vos.DocActionConstants;

import com.afgo.model.MAFGOProgram;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ResetInvoice.java,v 1.4.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ResetInvoice extends SvrProcess
{
	private int AD_Org_ID = 0;
	
	private int AFGO_Program_ID = 0;
	
	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}

	protected String doIt() throws Exception
	{
		int C_Invoice_ID = this.getRecord_ID();
		MInvoice invoice = new MInvoice(this.getCtx(), C_Invoice_ID, this.get_TrxName());
		if (invoice.getC_Invoice_ID() < 1 || invoice.getC_Invoice_ID() != C_Invoice_ID)
			throw new IllegalStateException("Invalid C_Invoice_ID: " + C_Invoice_ID);
		
		if (!(DocActionConstants.STATUS_Invalid.equals(invoice.getDocStatus()) || DocActionConstants.STATUS_NotApproved.equals(invoice.getDocStatus())))
			throw new IllegalStateException("InvalidStatus");
		
		if (invoice.isProcessing())
			throw new IllegalStateException("DocumentProcessing");
		
		String sql = "DELETE"
			+ " FROM AFGO_InvoiceLineAllocation"
			+ " WHERE C_InvoiceLine_ID IN"
			+ " ("
			+ " 	SELECT C_InvoiceLine_ID"
			+ " 	FROM C_InvoiceLine"
			+ " 	WHERE C_Invoice_ID=?"
			+ " )";
		
		int allocationLinesDeleted = DB.executeUpdate(sql, C_Invoice_ID, this.get_TrxName());
		
		MInvoiceLine[] lines = invoice.getLines(true);
		int linesDeleted = 0;
		for (int i = 0; i < lines.length; i++)
		{
			if (lines[i].delete(true, this.get_TrxName()))
				linesDeleted++;
		}
		
		invoice.setDocStatus(DocActionConstants.STATUS_Drafted);
		invoice.setDocAction(DocActionConstants.ACTION_Complete);
		invoice.setProcessed(false);
		invoice.setC_DocType_ID(0);
		invoice.set_ValueNoCheck("AFGO_PurchaseCommitment_ID", null);
		
		
		// 2834: optionally overwrite Organization & Program
		MOrg org = MOrg.get(this.getCtx(), (this.AD_Org_ID > 0 ? this.AD_Org_ID : invoice.getAD_Org_ID()));
		MAFGOProgram program = MAFGOProgram.getProgram(this.getCtx(), (this.AFGO_Program_ID > 0 ? this.AFGO_Program_ID : invoice.getAFGO_Program_ID()));
		
		log.info("AD_Org_ID: " + invoice.getAD_Org_ID() + "=>" + org.getAD_Org_ID() +", AFGO_Program_ID: " + invoice.getAFGO_Program_ID() + "=>" + program.getAFGO_Program_ID());
		
		if (program.getAD_Org_ID() != org.getAD_Org_ID())
			throw new IllegalStateException("Program/Organization mismatch: AD_Org_ID=" + org.getAD_Org_ID() + ", AFGO_Program_ID=" + program.getAFGO_Program_ID());
		
		if (org.getAD_Org_ID() != invoice.getAD_Org_ID())
			invoice.set_ValueNoCheck("AD_Org_ID", org.getAD_Org_ID());
		if (program.getAFGO_Program_ID() != invoice.getAFGO_Program_ID())
			invoice.setAFGO_Program_ID(program.getAFGO_Program_ID());
		
		boolean result = invoice.save();
		
		if (!result)
			throw new IllegalStateException("SaveFailed");
		
		return "@Updated@=" + result + ", @LinesDeleted@=" + linesDeleted;
	}

}
