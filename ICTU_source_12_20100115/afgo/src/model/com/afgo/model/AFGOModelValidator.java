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
import org.compiere.model.MEntityType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AFGOModelValidator.java,v 1.87.2.2 2010/01/06 11:24:26 tomassen Exp $
 *
 */
public class AFGOModelValidator implements ModelValidator
{
	public AFGOModelValidator()
	{
		 
	}
	
	private EntityTypeModule module = null;
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;

		MEntityType entityType = MEntityType.getEntityType(Env.getCtx(), EntityType);
		if (entityType == null)
			throw new IllegalStateException("Module not installed: " + EntityType);
		this.module = new EntityTypeModule(entityType);
		
		log.info("****************************************************************************************");
		log.info("Application Module: " + this.module.getName() + " " + this.module.getVersionString());
		log.info("Application Code: " + EntityType + " " + getVersionString());
		log.info("****************************************************************************************");
		
		if (getMajor() != module.getMajor() || getMinor() != module.getMinor() || getRevision() != module.getRevision())
			throw new IllegalStateException("Module / Code version mismatch");
		
		//
		AllocatableInvoiceValidator allocatableInvoiceValidator = new AllocatableInvoiceValidator();
		allocatableInvoiceValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		GenericAllocationValidator genericAllocationValidator = new GenericAllocationValidator();
		genericAllocationValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		DocumentPeriodValidator documentPeriodValidator = new DocumentPeriodValidator();
		documentPeriodValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		WorkflowRoutingValidator workflowRoutingValidator = new WorkflowRoutingValidator();
		workflowRoutingValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		DocumentWorkflowValidator documentWorkflowValidator = new DocumentWorkflowValidator();
		documentWorkflowValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		DisplayIdentifierValidator displayIdentifierValidator = new DisplayIdentifierValidator();
		displayIdentifierValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		InvoiceLineAllocationValidator invoiceLineAllocationValidator = new InvoiceLineAllocationValidator();
		invoiceLineAllocationValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		DocumentWorkflowDeletionValidator documentDeletionValidator = new DocumentWorkflowDeletionValidator();
		documentDeletionValidator.initialize(this.getAD_Client_ID(), engine);
		
		//
		DocumentProcessedLineDeletionValidator documentProcessedLineDeletionValidator = new DocumentProcessedLineDeletionValidator();
		documentProcessedLineDeletionValidator.initialize(this.getAD_Client_ID(), engine);
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	
	public String docValidate (PO adoc, int timing)
	{
		return null;
	}

	public String modelChange (PO po, int type) throws Exception
	{
		return null;
	}

	/**********************************************************************************************/
	
	/**
	 * DO NOT CHANGE: Record_ID, EntityType, COPYRIGHT
	 */
	public static final transient int Record_ID = 4008122;
	
	public static final transient String EntityType = "AFGO";
	
	public static final transient String COPYRIGHT = "(c) 2007-2010 Stichting ICTU";
	
	/**
	 * Version info must match AD_EntityType / AD_Version data
	 */
	
	public static final transient int MAJOR_VERSION = 1;
	
	public static final transient int MINOR_VERSION = 2;
	
	public static final transient int REVISION_VERSION = 12;
	
	public static final transient int PATCH_VERSION = 3;
	
	public static int getMajor()
	{
		return MAJOR_VERSION;
	}
	
	public static int getMinor()
	{
		return MINOR_VERSION;
	}
	
	public static int getRevision()
	{
		return REVISION_VERSION;
	}
	
	public static int getPatch()
	{
		return PATCH_VERSION;
	}
	
	public static String getVersionString()
	{
		return "v" + getMajor() + "." + getMinor() + "." + getRevision() + "." + getPatch();
	}
	
	public static AllocatableDocument castAllocatableDocument(PO po)
	{
		AllocatableDocument doc = null;
		
		if (po == null)
			;
		
		else if (po instanceof AllocatableDocument)
			doc = (AllocatableDocument)po;
		
		else if (po instanceof MInvoice)
			doc = new AllocatableInvoice((MInvoice)po);

		return doc;
	}
	
	public static boolean isAllocatableDocument(PO po)
	{
		AllocatableDocument doc = castAllocatableDocument(po);
		
		if (doc != null)
			return true;

		return false;
	}
	
	public static AllocatableDocumentLine castAllocatableDocumentLine(PO po)
	{
		AllocatableDocumentLine line = null;
		
		if (po == null)
			;
		
		else if (po instanceof AllocatableDocumentLine)
			line = (AllocatableDocumentLine)po;
		
		else if (po instanceof MInvoiceLine)
			line = new AllocatableInvoiceLine((MInvoiceLine)po);
		
		return line;
	}


	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
