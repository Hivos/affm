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
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: InvoiceLineAllocationValidator.java,v 1.4.2.1 2010/01/06 11:45:26 tomassen Exp $
 * 
 * Verify that Invoice Lines are not over/under allocated
 * (AFGO_InvoiceLineAllocation)
 *
 */
public class InvoiceLineAllocationValidator implements ModelValidator
{
	
	public InvoiceLineAllocationValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("InvoiceLineAllocationValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		this.engine.addModelChange(MInvoiceLine.Table_Name, this);
	}
	

	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	
	public String modelChange(PO po, int type) throws Exception
	{
		if (MInvoiceLine.Table_Name.equals(po.get_TableName()))
		{
			MInvoiceLine invoiceLine = (MInvoiceLine)po;
			if (!MAFGOInvoiceLineAllocation.checkAllocation(invoiceLine, null))
				return "OverAllocated";
		}
		
		return null;
	}

	public String docValidate(PO doc, int timing)
	{
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
