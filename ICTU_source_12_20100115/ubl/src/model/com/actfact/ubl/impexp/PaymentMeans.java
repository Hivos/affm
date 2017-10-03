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

import org.w3c.dom.Node;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: PaymentMeans.java,v 1.5 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class PaymentMeans extends CommonAggregateComponent
{
	PaymentMeans(CommonAggregateComponent parent)
	{
		super(parent);
	}

	private String paymentMeansCode = null;
	
	private String paymentDueDate = null;
	
	private PayeeFinancialAccount payeeFinancialAccount = null;

	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonBasicComponent.PREFIX.equals(node.getPrefix()))
		{
			if (CommonBasicComponent.PaymentMeansCode.equals(nodeName))
			{
				this.paymentMeansCode = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.PaymentDueDate.equals(nodeName))
			{
				this.paymentDueDate = node.getTextContent();
				return true;
			}
			return false;
		}
		else if (CommonAggregateComponent.PREFIX.equals(node.getPrefix()))
		{
			if (PayeeFinancialAccount.NAME.equals(nodeName))
			{
				this.payeeFinancialAccount = new PayeeFinancialAccount(this);
				this.payeeFinancialAccount.parse(node);
				return true;
			}
		}
		return false;
	}
	
	public String getPaymentMeans()
	{
		return this.paymentMeansCode;
	}
	
	public String getPaymentDueDate()
	{
		return this.paymentDueDate;
	}
	
	public PayeeFinancialAccount getPayeeFinancialAccount()
	{
		return this.payeeFinancialAccount;
	}
	
	//
	
	public static final transient String NAME = "PaymentMeans";
	
}
