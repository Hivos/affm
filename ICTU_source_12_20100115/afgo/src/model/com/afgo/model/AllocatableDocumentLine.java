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

import java.math.BigDecimal;

import org.compiere.framework.PO;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AllocatableDocumentLine.java,v 1.5.2.1 2010/01/06 11:45:24 tomassen Exp $
 *
 */
public interface AllocatableDocumentLine extends AllocatableItemLine
{
	public AllocatableDocument getHeader();
	
	public int getAFGO_Month_ID();
	
	public int get_OldAFGO_Month_ID();

	public int getAFGO_ServiceType_ID();

	public int getM_Product_ID();
	
	public int getC_Charge_ID();
	
	public BigDecimal getQty();
	
	public BigDecimal getPrice();
	
	public PO getPO();
	
	//public MAFGOWorkflowRoleAssignment getWorkflowResponsible();
}
