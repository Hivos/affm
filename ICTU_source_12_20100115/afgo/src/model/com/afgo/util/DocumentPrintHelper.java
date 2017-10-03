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
package com.afgo.util;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Language;

import com.afgo.model.AllocatableDocument;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DocumentPrintHelper.java,v 1.3.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class DocumentPrintHelper
{
	public DocumentPrintHelper(AllocatableDocument document)
	{
		this.document = document;
	}
	
	private AllocatableDocument document = null;
	
	private MPrintFormat printFormat = null;
	
	private Query query = null;
	
	private PrintInfo info = null;
	
	private ReportEngine reportEngine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public AllocatableDocument getDocument()
	{
		return this.document;
	}
	
	public MPrintFormat getPrintFormat()
	{
		if (this.printFormat == null)
		{

    		MDocType docType = this.getDocument().getDocType();
    		if (docType != null && docType.getAD_PrintFormat_ID() > 0)
    			this.printFormat = MPrintFormat.get(this.getDocument().getCtx(), docType.getAD_PrintFormat_ID(), false);
    		else
    			this.printFormat = MPrintFormat.get(this.getDocument().getCtx(), 0, this.getDocument().get_Table_ID());
		}
		
		if (this.printFormat == null)
			throw new IllegalStateException("NoPrintFormat");
		
		MClient client = MClient.get(this.getDocument().getCtx());
		Language language  = client.getLanguage();
		if (client.isMultiLingualDocument() && this.getDocument().getBPartner().getAD_Language() != null)
			language = Language.getLanguage(this.getDocument().getBPartner().getAD_Language());

		if (language != null)
		{
			this.printFormat.setLanguage(language);
			this.printFormat.setTranslationLanguage(language);
		}
		
		return this.printFormat;
	}
	
	public Query getQuery()
	{
		if (this.query == null)
		{
			this.query = new Query(this.getDocument().get_Table_ID());
			query.addRestriction(this.getDocument().getPO().get_TableName() + "_ID", Query.EQUAL, new Integer(this.getDocument().get_ID()));
		}
		return this.query;
	}
	
	public PrintInfo getPrintInfo()
	{
		if (this.info == null)
		{
			PrintInfo info = new PrintInfo(this.getDocument().getDocumentNo(), this.getDocument().get_Table_ID(), this.getDocument().get_ID(), this.getDocument().getBPartner().getC_BPartner_ID());
				info.setCopies(1);
				info.setDocumentCopy(false);
		}
		return this.info;
	}
	
	public ReportEngine getReportEngine()
	{
		if (this.reportEngine == null)
			this.reportEngine = new ReportEngine(this.getDocument().getCtx(), this.getPrintFormat(), this.getQuery(), this.getPrintInfo());
		return this.reportEngine;
	}
}
