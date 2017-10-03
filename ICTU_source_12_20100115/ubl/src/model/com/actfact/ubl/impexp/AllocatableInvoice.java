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
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.compiere.model.MBPartner;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: AllocatableInvoice.java,v 1.10 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class AllocatableInvoice extends Invoice
{
	
	AllocatableInvoice(Ctx ctx)
	{
		super(ctx);
	}

	private String documentNo = null;
	
	private int AFGO_Program_ID = 0;
	
	private MAFGOProgram program = null;
	
	private int AFGO_PurchaseCommitment_ID = 0;
	
	private MAFGOPurchaseCommitment pc = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void parse(Node node)
	{
		super.parse(node);
		
		this.parseAFGO_PurchaseCommitment_ID(super.getOrderReference().getID());
		
		for (Iterator<String> notes = super.getNotes().iterator(); notes.hasNext();)
		{
			String note = notes.next();
			if (note == null || "".equals(note))
				continue;
			
			if (note.toLowerCase().startsWith(DOCUMENTNO.toLowerCase()))
			{
				this.parseDocumentNo(note);
			}
			else if (note.toLowerCase().startsWith(AFGO_PROGRAM_ID.toLowerCase()))
			{
				this.parseAFGO_Program_ID(note);
			}
			/*
			else if (note.toLowerCase().startsWith(AFGO_PURCHASECOMMITMENT_ID.toLowerCase()))
			{
				this.parseAFGO_PurchaseCommitment_ID(note);
			}
			*/
		}
		
	}
	
	private void parseDocumentNo(String value)
	{
		if (value == null || "".equals(value))
		{
			error("No DocumentNo");
			return;
		}
		value = value.substring(value.lastIndexOf(":") + 1);
		if (value.startsWith(" "))
			value = value.substring(1);
		this.documentNo = value;
		log.fine("DocumentNo=" + this.documentNo);
	}
	
	private void parseAFGO_Program_ID(String value)
	{
		if (value == null || "".equals(value))
		{
			error("No AFGO_Program_ID");
			return;
		}
		
		this.AFGO_Program_ID = StringToInt(value);
		
		if (this.AFGO_Program_ID < 1)
			error("Invalid AFGO_Program_ID[" + value + "]");
		
		log.fine("AFGO_Program_ID=" + this.AFGO_Program_ID);
	}
	
	private void parseAFGO_PurchaseCommitment_ID(String value)
	{
		if (value == null || "".equals(value))
		{
			warning("No AFGO_PurchaseCommitment_ID");
			return;
		}
		
		this.AFGO_PurchaseCommitment_ID = StringToInt(value);
		
		if (this.AFGO_PurchaseCommitment_ID < 1)
			warning("Invalid AFGO_PurchaseCommitment_ID[" + value + "]");
		
		log.fine("AFGO_PurchaseCommitment_ID=" + this.AFGO_PurchaseCommitment_ID);
	}
	
	public boolean isValid()
	{
		if (!super.isValid())
		{
			return false;
		}
		
		/**
		 * mandatory fields
		 */
		
		if (this.getDocumentNo() == null)
			return false;
		
		if (this.getProgram() == null)
			return false;
		
		/**
		 * optional fields
		 */
		if (this.getPurchaseCommitment() != null)
		{
			
			if (!this.getPurchaseCommitment().isMasterCommitment())
			{
				error("No MasterCommitment: AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID());
			}
			
			if (this.getPurchaseCommitment().getAFGO_Program_ID() != this.getProgram().getAFGO_Program_ID())
			{
				error("mismatch: AFGO_PurchaseCommitment_ID[" + this.getPurchaseCommitment().getAFGO_PurchaseCommitment_ID() + "] | AFGO_Program_ID[" + this.getProgram().getAFGO_Program_ID() + "]");
			}
			
			if (this.getPurchaseCommitment().getC_BPartner_ID() != this.getBPartner().getC_BPartner_ID())
			{
				error("mismatch: AFGO_PurchaseCommitment_ID[" + this.getPurchaseCommitment().getAFGO_PurchaseCommitment_ID() + "] | C_BPartner_ID[" + this.getBPartner().getC_BPartner_ID() + "]");
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
	
	public boolean isDuplicate(int AD_Client_ID, boolean ignoreError)
	{
		String sql = "SELECT COUNT(*)"
			+ " FROM C_Invoice"
			+ " WHERE AD_Client_ID=?"
			+ " AND UPPER(DocumentNo)=?";
		
		if (DB.getSQLValue(null, sql, AD_Client_ID, this.getDocumentNo()) > 0)
		{
			if (ignoreError)
				warning("duplicate DocumentNo: " + this.getDocumentNo());
			else
				error("duplicate DocumentNo: " + this.getDocumentNo());
			
			return true;
		}
			
		return false;
	}
	
	public String toString()
	{
		StringBuffer info = new StringBuffer();
		
		info.append("DocumentNo=" + this.getDocumentNo());
		info.append(", C_BPartner_ID=" + this.getC_BPartner_ID());
		info.append(", AFGO_Program_ID=" + this.getAFGO_Program_ID());
		info.append(", AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID());
		info.append(", GrandTotal=" + this.getGrandTotal());
		
		return info.toString();
	}
	
	public String getDocumentNo()
	{
		if (this.documentNo == null)
		{
			error("No DocumentNo");
		}
		
		return this.documentNo;
	}
	
	public int getAFGO_Program_ID()
	{
		return this.AFGO_Program_ID;
	}
	
	public MAFGOProgram getProgram()
	{
		if (this.program == null)
		{
    		if (this.getAFGO_Program_ID() < 1)
    		{
    			error("No AFGO_Program_ID");
    			return this.program;
    		}
    		else
    		{
    			this.program = MAFGOProgram.getProgram(this.getCtx(), this.getAFGO_Program_ID());
    		}

    		if (this.program == null || this.program.getAFGO_Program_ID() != this.getAFGO_Program_ID())
    		{
    			error("Invalid: AFGO_Program_ID[" + this.getAFGO_Program_ID() + "]");
    		}
    		
    		else if (this.program.getAD_Client_ID() != this.getCtx().getAD_Client_ID())
    		{
    			error("mismatch: AD_Client_ID[" + this.getCtx().getAD_Client_ID() + "] | AFGO_Program_ID[" + this.program.getAFGO_Program_ID() + "]");
    		}
		}
		
		return this.program;
	}
	
	public int getAFGO_PurchaseCommitment_ID()
	{
		return this.AFGO_PurchaseCommitment_ID;
	}
	
	public MAFGOPurchaseCommitment getPurchaseCommitment()
	{
		if (this.pc == null)
		{
			if (this.getAFGO_PurchaseCommitment_ID() < 1)
			{
				warning("No AFGO_PurchaseCommitment_ID");
				return this.pc;
			}
			
			this.pc = new MAFGOPurchaseCommitment(this.getCtx(), this.getAFGO_PurchaseCommitment_ID(), null);
			
			if (this.pc.getAFGO_PurchaseCommitment_ID() != this.getAFGO_PurchaseCommitment_ID())
			{
				error("Invalid: AFGO_PurchaseCommitment_ID[" + this.getAFGO_PurchaseCommitment_ID() + "]");
			}
			
			if (this.pc.getAD_Client_ID() != this.getCtx().getAD_Client_ID())
			{
				error("mismatch: AD_Client_ID[" + this.getCtx().getAD_Client_ID() + "] | AFGO_PurchaseCommitment_ID[" + this.getAFGO_PurchaseCommitment_ID() + "]");
			}
		}
		
		return this.pc;
	}
	
	public int StringToInt(String value)
	{
		int r = -1;
		String s = "";
		for (int i = 0; i < value.length(); i++)
		{
			if (value.charAt(i) > 47 && value.charAt(i) < 58)
				s = s + value.charAt(i);
		}
		try
		{
			r = Integer.parseInt(s);
		}
		catch(Exception e)
		{
			error("Unable to parse integer value: " + value);
		}
		
		return r;
	}
	
	//
	
	public static final transient String DOCUMENTNO = "Streepjescode";
	
	public static final transient String AFGO_PROGRAM_ID = "Programma";
	
	public static final transient String AFGO_PURCHASECOMMITMENT_ID = "Verplichtingnummer";
	
	public static AllocatableInvoice get(Ctx ctx, Node node)
	{
		AllocatableInvoice i =  new AllocatableInvoice(ctx);
		i.parse(node);
		return i;
	}

	public static AllocatableInvoice parse(Ctx ctx, File file)
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
