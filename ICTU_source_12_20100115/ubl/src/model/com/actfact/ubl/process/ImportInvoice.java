/******************************************************************************
 * Product: IUBL                                                              *
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
package com.actfact.ubl.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;

import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import com.actfact.exta.model.MEXTAAttachmentExternal;
import com.actfact.ubl.impexp.AllocatableInvoice;
import com.actfact.ubl.impexp.Item;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: ImportInvoice.java,v 1.13 2010/01/06 12:05:28 tomassen Exp $
 * 
 */
public class ImportInvoice extends SvrProcess
{
	private int C_DocType_ID = -1;
	
	private String importDirectory = null;
	
	private boolean ignoreDuplicates = false;
	
	//
	
	private StringBuffer errorBuffer = new StringBuffer();
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				this.C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("ImportDirectory"))
				this.importDirectory = (String)para[i].getParameter();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		try
		{
    		if (this.C_DocType_ID < 1)
    			return "@NoDocType@";
    		
    		if (this.importDirectory == null)
    			return "@NoImportDirectory@";
    		
    		File path = new File(this.importDirectory);
    		
    		if (!path.exists() || !path.isDirectory())
    			return "@NoSuchDirectory@: " + this.importDirectory;
    		
    		int imported = 0;
    		int errors = 0;
    		
    		File[] files = path.listFiles();
    		for (int i = 0; i < files.length; i++)
    		{
    			File file = files[i];
    			
    			if (file == null || !file.exists() || file.isDirectory())
    				continue;
    			
    			if (!file.getName().toLowerCase().endsWith(XML))
    				continue;
    			
    			log.fine(file.getAbsolutePath());
    			
    			AllocatableInvoice importInvoice = AllocatableInvoice.parse(this.getCtx(), file);
    			
    			// error: can the data be parsed
    			if (importInvoice == null || importInvoice.isError())
    			{
    				if (importInvoice == null)
    				{
    					log.warning("Error: " + file.getAbsolutePath());
    				}
    				else
    				{
    					//importInvoice.error(Msg.getMsg(this.getCtx(), "Error"));
    					this.moveFile(file, ERR, importInvoice.getErrors());
    				}
    				errors++;
    			}
    			
    			// invalid: verify identifiers & object relations
    			else if (!importInvoice.isValid())
    			{
    				//importInvoice.error(Msg.getMsg(this.getCtx(), "Invalid"));
    				log.warning(importInvoice.toString());
    				this.moveFile(file, ERR, importInvoice.getErrors());
    				errors++;
    			}
    			
    			// duplicate
    			else if (importInvoice.isDuplicate(this.getAD_Client_ID(), ignoreDuplicates) && !ignoreDuplicates)
    			{
    				importInvoice.error(Msg.getMsg(this.getCtx(), "Duplicate"));
    				this.moveFile(file, ERR, importInvoice.getErrors());
    				errors++;
    			}
    			
    			// imported
    			else
    			{
    				MInvoice invoice = this.importInvoice(importInvoice);
    				if (invoice.save())
    				{
    					this.moveFile(file, IMP, importInvoice.getErrors());
    					log.info(invoice.getDocumentNo() + " {" + importInvoice.toString() + "}");
    					//super.addLog(invoice.getDocumentNo());
    					imported++;
    				}
    				else
    				{
    					importInvoice.error("saveError: " + invoice.getSummary());
    					this.moveFile(file, ERR, importInvoice.getErrors());
    					errors++;
    				}
    			}
    		}
    		
    		addLog(0, null, new BigDecimal(errors), "@Errors@");
    		addLog(0, null, new BigDecimal(imported), "@C_Invoice_ID@: @Inserted@");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if (this.errorBuffer.length() > 0)
			this.saveErrorBuffer();
		
		return "";
	}
	
	private MInvoice importInvoice(AllocatableInvoice importInvoice)
	{
		MInvoice invoice = new MInvoice(this.getCtx(), 0, this.get_TrxName());
		
		try
		{
    		invoice.setIsSOTrx(false);
    		invoice.setC_DocTypeTarget_ID(this.C_DocType_ID);
    		invoice.setSalesRep_ID(this.getCtx().getAD_User_ID());
    		
    		if (importInvoice.getInvoiceLines().size() > 0)
    		{
    			Item item = importInvoice.getInvoiceLines().get(0).getItem();
    			if (item != null)
    			{
    				invoice.setDescription(item.getDescription());
    			}
    		}
    		
    		invoice.set_ValueNoCheck("BPartnerDocumentNo", importInvoice.getBillingReference().getInvoiceDocumentReference().getID());
    		invoice.set_ValueNoCheck("BPartnerPaymentReference", importInvoice.getBillingReference().getInvoiceDocumentReference().getID());
    				
    		if (importInvoice.isDuplicate(this.getCtx().getAD_Client_ID(), true))
    			invoice.setDocumentNo(importInvoice.getDocumentNo() + " - " + System.currentTimeMillis());
    		else
    			invoice.setDocumentNo(importInvoice.getDocumentNo());
    		
    		Timestamp dateInvoiced = importInvoice.getDateInvoiced();
    		if (dateInvoiced != null)
    			invoice.setDateInvoiced(dateInvoiced);
    		Timestamp dateAcct = importInvoice.getDateAcct();
    		if (dateAcct != null)
    			invoice.setDateAcct(dateAcct);
    		
    		MBPartner bpartner = new MBPartner(this.getCtx(), importInvoice.getC_BPartner_ID(), this.get_TrxName());
    		invoice.setC_BPartner_ID(importInvoice.getC_BPartner_ID());
    		if (bpartner.getPO_PriceList_ID() > 0)
    			invoice.setM_PriceList_ID(bpartner.getPO_PriceList_ID());
    		if (bpartner.getPO_PaymentTerm_ID() > 0)
    			invoice.setC_PaymentTerm_ID(bpartner.getPO_PaymentTerm_ID());
    		if (bpartner.getPaymentRule() != null)
    			invoice.setPaymentRule(bpartner.getPaymentRulePO());
    		
    		if (MInvoice.PAYMENTRULE_DirectDeposit.equals(invoice.getPaymentRule()) || MInvoice.PAYMENTRULE_OnCredit.equals(invoice.getPaymentRule()))
    		{
    			if (importInvoice.getC_BP_BankAccount_ID() > 0)
    				invoice.set_ValueNoCheck("C_BP_BankAccount_ID", importInvoice.getC_BP_BankAccount_ID());
    		}
    		
    		invoice.setAD_Org_ID(DB.getSQLValue(this.get_TrxName(), "SELECT AD_Org_ID FROM AFGO_Program WHERE AFGO_Program_ID=?", importInvoice.getAFGO_Program_ID()));
    		invoice.set_ValueNoCheck("AFGO_Program_ID", importInvoice.getProgram().getAFGO_Program_ID());
    		if (importInvoice.getPurchaseCommitment()!= null)
    			invoice.set_ValueNoCheck("AFGO_PurchaseCommitment_ID", importInvoice.getAFGO_PurchaseCommitment_ID());
    		invoice.set_ValueNoCheck("ControlAmt", importInvoice.getGrandTotal());
    		
    		if (!invoice.save())
    			return invoice;
    		
    		MEXTAAttachmentExternal attachment = this.importAttachment(importInvoice, invoice.getC_Invoice_ID());
    		if (!attachment.save())
    			return invoice;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return invoice;
	}
	
	private MEXTAAttachmentExternal importAttachment(AllocatableInvoice importInvoice, int C_Invoice_ID)
	{
		MEXTAAttachmentExternal attachment = null;
		
		try
		{
		
    		if (importInvoice.getBillingReference() == null)
    			return attachment;
    		if (importInvoice.getBillingReference().getInvoiceDocumentReference() == null)
    			return attachment;
    		if (importInvoice.getBillingReference().getInvoiceDocumentReference().getAttachment() == null)
    			return attachment;
    		if (importInvoice.getBillingReference().getInvoiceDocumentReference().getAttachment().getExternalReference() == null)
    			return attachment;
    		
    		String url = importInvoice.getBillingReference().getInvoiceDocumentReference().getAttachment().getExternalReference().getURI();
    		if (url == null && "".equals(url))
    			return attachment;
    		
    		attachment = new MEXTAAttachmentExternal(this.getCtx(), 0, this.get_TrxName());
    		attachment.setAD_Table_ID(MInvoice.Table_ID);
    		attachment.setRecord_ID(C_Invoice_ID);
    		attachment.setAbsolutePath(url);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return attachment;
	}
	
	private void moveFile(File file, String extension, HashSet<String> errorList)
	{
		if (ERR.equals(extension))
		{
			
			if (errorBuffer.length() > 0)
				errorBuffer.append(NL + NL);
			errorBuffer.append(file.getName());
			
			try
			{
				FileWriter fw = new FileWriter(file.getAbsolutePath().replace("." + XML, "." + extension));
				errorBuffer.append(NL + "<!--" + NL);
				fw.write("<!--" + NL);
				for (Iterator<String> errors = errorList.iterator(); errors.hasNext();)
				{
					String error = errors.next();
					fw.write("	" + error + NL);
					errorBuffer.append(error + NL);
				}
				fw.write("-->" + NL + NL);
				errorBuffer.append("-->");
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null)
				{
					fw.write(line + NL);
				}
				br.close();
				fw.flush();
				fw.close();
				file.delete();
			}
			catch(Exception e)
			{
				this.renameFile(file, XML, extension);
			}
		}
		else
			this.renameFile(file, XML, extension);
	}
	
	private void renameFile(File file, String oldExt, String newExt)
	{
		String source = file.getAbsolutePath();
		String target = file.getAbsolutePath().replace("." + oldExt, "." + newExt);
		
		log.fine(source + " => " + target + " : " + file.renameTo(new File(target)));
	}
	
	private void saveErrorBuffer()
	{
		try
		{
			Date date = new Date(System.currentTimeMillis());
			DateFormat df = new SimpleDateFormat("yyyyMMdd_kkmm");
			String fileName = df.format(date) + ".log";
			
			File file = new File(this.importDirectory + "/" + fileName);
			file.createNewFile();
			
			FileWriter wf = new FileWriter(file);
			
			wf.write(this.errorBuffer.toString());
			
			wf.flush();
			wf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.severe(e.toString());
		}
	}
	
	//
	
	public static final transient String XML = "xml";
	
	public static final transient String IMP = "imp";
	
	public static final transient String ERR = "err";
	
	public static final transient String NL = System.getProperty("line.separator");

}
