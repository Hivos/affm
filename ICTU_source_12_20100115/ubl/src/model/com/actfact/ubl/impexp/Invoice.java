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
package com.actfact.ubl.impexp;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPartner;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: Invoice.java,v 1.8 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class Invoice extends CommonAggregateComponent
{
	Invoice(Ctx ctx)
	{
		super(null);
		
		this.ctx = ctx;
	}
	
	private Ctx ctx = null;

	private File file = null;
	
	private Document doc = null;
	
	private String UBLVersionID = null;
	
	private String ID = null;
	
	private String UUID = null;
	
	private String IssueDate = null;
	
	private ArrayList<String> notes = new ArrayList<String>();
	
	private String taxPointDate = null;
	
	private String documentCurrencyCode = null;
	
	private OrderReference orderReference = null;
	
	private BillingReference billingReference = null;
	
	private AccountingSupplierParty accountingSupplierParty = null;
	
	private int C_BPartner_ID = 0;
	
	private MBPartner bp = null;
	
	private AccountingCustomerParty accountingCustomerParty = null;
	
	private PayeeParty payeeParty = null;
	
	private PaymentMeans paymentMeans = null;
	
	private PaymentTerms paymentTerms = null;
	
	private TaxTotal taxTotal = null;
	
	private int C_BP_BankAccount_ID = 0;
	
	private MBPBankAccount bpba = null;
	
	private LegalMonetaryTotal legalMonetaryTotal = null;
	
	private BigDecimal grandTotal = null;
	
	private String currencyISO = null;
	
	private ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
	
	private HashSet<String> errorList = new HashSet<String>();
	
	private HashSet<String> warningList = new HashSet<String>();
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void warning(String msg)
	{
		this.warningList.add(msg);
		log.warning(msg);
	}
	
	public void error(String msg)
	{
		this.errorList.add(msg);
		log.warning(msg);
	}
	
	public Ctx getCtx()
	{
		return this.ctx;
	}
	
	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonBasicComponent.PREFIX.equals(node.getPrefix()))
		{
			if (CommonBasicComponent.UBLVersionID.equals(nodeName))
			{
				this.UBLVersionID = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.ID.equals(nodeName))
			{
				this.ID = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.UUID.equals(nodeName))
			{
				this.UUID = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.IssueDate.equals(nodeName))
			{
				this.IssueDate = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.Note.equals(nodeName))
			{
				this.notes.add(node.getTextContent());
				return true;
			}
			else if (CommonBasicComponent.TaxPointDate.equals(nodeName))
			{
				this.taxPointDate = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.DocumentCurrencyCode.equals(nodeName))
			{
				this.documentCurrencyCode = node.getTextContent();
				return true;
			}
			return false;
				
		}
		else if (CommonAggregateComponent.PREFIX.equals(node.getPrefix()))
		{
			if (OrderReference.NAME.equals(nodeName))
			{
				this.orderReference = new OrderReference(this);
				this.orderReference.parse(node);
				return true;
			}
			else if (BillingReference.NAME.equals(nodeName))
			{
				this.billingReference = new BillingReference(this);
				this.billingReference.parse(node);
				return true;
			}
			else if (AccountingSupplierParty.NAME.equals(nodeName))
			{
				this.accountingSupplierParty = new AccountingSupplierParty(this);
				this.accountingSupplierParty.parse(node);
				return true;
			}
			else if (AccountingCustomerParty.NAME.equals(nodeName))
			{
				this.accountingCustomerParty = new AccountingCustomerParty(this);
				this.accountingCustomerParty.parse(node);
				return true;
			}
			else if (PayeeParty.NAME.equals(nodeName))
			{
				this.payeeParty = new PayeeParty(this);
				this.payeeParty.parse(node);
				return true;
			}
			else if (PaymentMeans.NAME.equals(nodeName))
			{
				this.paymentMeans = new PaymentMeans(this);
				this.paymentMeans.parse(node);
				return true;
			}
			else if (PaymentTerms.NAME.equals(nodeName))
			{
				this.paymentTerms = new PaymentTerms(this);
				this.paymentTerms.parse(node);
				return true;
			}
			else if (TaxTotal.NAME.equals(nodeName))
			{
				this.taxTotal = new TaxTotal(this);
				this.taxTotal.parse(node);
				return true;
			}
			else if (LegalMonetaryTotal.NAME.equals(nodeName))
			{
				this.legalMonetaryTotal = new LegalMonetaryTotal(this);
				this.legalMonetaryTotal.parse(node);
				return true;
			}
			else if (InvoiceLine.NAME.equals(nodeName))
			{
				InvoiceLine invoiceLine = new InvoiceLine(this);
				invoiceLine.parse(node);
				this.invoiceLines.add(invoiceLine);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean isValid()
	{
		/**
		 * mandatory fields
		 */
		if (this.getBPartner() == null)
			return false;
		
		if (this.getDateInvoiced() == null)
			return false;
					
		if (this.getGrandTotal() == null)
			return false;
		
		/**
		 * optional fields
		 */
		if (this.getBPBankAccount() != null)
		{
			if (this.getBPBankAccount().getC_BPartner_ID() != this.getBPartner().getC_BPartner_ID())
			{
				error("mismatch: C_BP_BankAccount_ID[" + this.getBPBankAccount().getC_BP_BankAccount_ID() + "] | C_BPartner_ID[" + this.getBPartner().getC_BPartner_ID());
			}
		}
		
		/**
		 * catch errors caused by previous steps
		 */
		if (this.isError())
		{
			return false;
		}
		
		
		return true;
	}
	
	public String getUBLVersionID()
	{
		return this.UBLVersionID;
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public String getUUID()
	{
		return this.UUID;
	}
	
	public String getIssueDate()
	{
		return this.IssueDate;
	}
	
	public Timestamp getDateInvoiced()
	{
		Timestamp dateInvoiced = null;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		
		try
		{
			dateInvoiced = new Timestamp(df.parse(this.getIssueDate()).getTime());
		}
		catch(Exception e)
		{
			error("Unable to parse IssueDate: " + this.getIssueDate());
		}
		
		return dateInvoiced;
	}
	
	public Timestamp getDateAcct()
	{
		return this.getDateInvoiced();
	}
	
	public ArrayList<String> getNotes()
	{
		return this.notes;
	}
	
	public String getTaxPointDate()
	{
		return this.taxPointDate;
	}
	
	public String getDocumentCurrencyCode()
	{
		return this.documentCurrencyCode;
	}
	
	public OrderReference getOrderReference()
	{
		return this.orderReference;
	}
	
	public BillingReference getBillingReference()
	{
		return this.billingReference;
	}

	public AccountingSupplierParty getAccountingSupplierParty()
	{
		return this.accountingSupplierParty;
	}

	public int getC_BPartner_ID()
	{
		if (this.C_BPartner_ID < 1)
		{
			if (this.getAccountingSupplierParty() != null)
			{
				if (this.getAccountingSupplierParty().getCustomerAssignedAccountID() != null)
				{
					try
					{
						this.C_BPartner_ID = Integer.parseInt(this.getAccountingSupplierParty().getCustomerAssignedAccountID());
					}
					catch(Exception e)
					{
						error("Unable to parse C_BPartner_ID: " + this.getAccountingSupplierParty().getCustomerAssignedAccountID());
					}
				}
			}
		}
		return this.C_BPartner_ID;
	}
	
	public MBPartner getBPartner()
	{
		if (this.bp == null)
		{
			if (this.getC_BPartner_ID() < 1)
			{
				error("No C_BPartner_ID");
				return this.bp;
			}
			
			this.bp = new MBPartner(this.getCtx(), this.getC_BPartner_ID(), null);

			if (this.bp.getC_BPartner_ID() != this.getC_BPartner_ID())
			{
				error("Invalid: C_BPartner_ID[" + this.getC_BPartner_ID() + "]");
				this.bp = null;
				return this.bp;
			}
			
			if (this.bp.getAD_Client_ID() != this.getCtx().getAD_Client_ID())
			{
				error("Mismatch: AD_Client_ID[" + this.getCtx().getAD_Client_ID() + "] | C_BPartner_ID[" + this.bp.getC_BPartner_ID() + "]");
			}
		}
		
		return this.bp;
	}
	
	public AccountingCustomerParty getAccountingCustomerParty()
	{
		return this.accountingCustomerParty;
	}

	public PayeeParty getPayeeParty()
	{
		return this.payeeParty;
	}

	public PaymentMeans getPaymentMeans()
	{
		return this.paymentMeans;
	}
	
	private PaymentTerms getPaymentTerms()
	{
		return this.paymentTerms;
	}

	public int getC_BP_BankAccount_ID()
	{
		if (this.C_BP_BankAccount_ID < 1)
		{
			if (this.getPaymentMeans() != null)
			{
				if (this.getPaymentMeans().getPayeeFinancialAccount() != null)
				{
					String value = this.getPaymentMeans().getPayeeFinancialAccount().getID();
					if (value != null && !"".equals(value))
					{
    					try
    					{
    						this.C_BP_BankAccount_ID = Integer.parseInt(value);
    					}
    					catch(Exception e)
    					{
    						error("Unable to parse C_BP_BankAccount_ID: " + this.getPaymentMeans().getPayeeFinancialAccount().getID());
    					}
					}
				}
			}
		}
		
		return this.C_BP_BankAccount_ID;
	}
	
	public MBPBankAccount getBPBankAccount()
	{
		if (this.bpba == null)
		{
			if (this.getC_BP_BankAccount_ID() < 1)
			{
				warning("No C_BP_BankAccount_ID");
				return this.bpba;
			}
			this.bpba = new MBPBankAccount(this.getCtx(), this.getC_BP_BankAccount_ID(), null);
			
			if (this.bpba.getC_BP_BankAccount_ID() != this.getC_BP_BankAccount_ID())
			{
				error("Invalid: C_BP_BankAccount[" + this.getC_BP_BankAccount_ID() + "]");
			}
			
			if (this.bpba.getAD_Client_ID() != this.getCtx().getAD_Client_ID())
			{
				error("mismatch: AD_Client_ID[" + this.getCtx().getAD_Client_ID() + "] | C_BP_BankAccount_ID[" + this.getC_BP_BankAccount_ID() + "]");
			}
		}
		
		return this.bpba;
	}
	
	public LegalMonetaryTotal getLegalMonetaryTotal()
	{
		return this.legalMonetaryTotal;
	}

	public BigDecimal getGrandTotal()
	{
		if (this.grandTotal == null)
		{
			if (this.legalMonetaryTotal != null)
			{
				if (this.getLegalMonetaryTotal().getPayableAmount() != null && !"".equals(this.getLegalMonetaryTotal().getPayableAmount()))
				{
					DecimalFormat df = new DecimalFormat();
					try
					{
						df.setParseBigDecimal(true);
						this.grandTotal = (BigDecimal)df.parse(this.getLegalMonetaryTotal().getPayableAmount());
					}
					catch(Exception e)
					{
						error("Unable to parse GrandTotal: " + this.getLegalMonetaryTotal().getPayableAmount());
					}
				}
			}
			
			if (this.grandTotal == null)
			{
				error("No GrandTotal");
			}
		}
		
		return this.grandTotal;
	}

	public String getCurrencyISO()
	{
		return this.currencyISO;
	}

	public ArrayList<InvoiceLine> getInvoiceLines()
	{
		return this.invoiceLines;
	}
	
	public HashSet<String> getErrors()
	{
		return this.errorList;
	}
	
	public HashSet<String> getWarnings()
	{
		return this.warningList;
	}
	
	public boolean isError()
	{
		return this.getErrors().size() > 0;
	}
	
	//
	
	public static final transient String NAME = "Invoice";
	
	public static Invoice get(Ctx ctx, Node node)
	{
		Invoice i =  new Invoice(ctx);
		i.parse(node);
		return i;
	}
	
	public static Invoice parse(Ctx ctx, File file)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			
			if (document == null)
				throw new IllegalStateException("NoDocument");
			
			Element eInvoice = document.getDocumentElement();
			if (!NAME.equals(eInvoice.getNodeName()))
				throw new IllegalStateException("NoInvoice");
			
			return get(ctx, eInvoice);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	
}
