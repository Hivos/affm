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
package com.afgo.callout;

import java.math.BigDecimal;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutLineAmt.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CalloutLineAmt extends CalloutEngine
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String price(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("Price=" + value);
		
		return this.calculateLineNetAmt(ctx, WindowNo, mTab, mField, value);
	}
	
	public String qty(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("Qty=" + value);
		
		return this.calculateLineNetAmt(ctx, WindowNo, mTab, mField, value);
	}
	
	private String calculateLineNetAmt(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (mTab.getValue("Price") != null && mTab.getValue("Qty") != null)
		{
			BigDecimal price = (BigDecimal)mTab.getValue("Price");
			BigDecimal qty = (BigDecimal)mTab.getValue("Qty");
			
			mTab.setValue("LineNetAmt", price.multiply(qty));
		}
		
		return "";
	}
}
