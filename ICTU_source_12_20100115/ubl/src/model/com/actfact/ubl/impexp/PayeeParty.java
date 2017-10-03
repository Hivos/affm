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
 * @version $Id: PayeeParty.java,v 1.4 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class PayeeParty extends CommonAggregateComponent
{
	PayeeParty(CommonAggregateComponent parent)
	{
		super(parent);
	}

	private PartyIdentification partyIdentification = null;
	
	private PartyName partyName = null;

	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonAggregateComponent.PREFIX.equals(node.getPrefix()))
		{
			if (PartyIdentification.NAME.equals(nodeName))
			{
				this.partyIdentification = new PartyIdentification(this);
				this.partyIdentification.parse(node);
				return true;
			}
			else if (PartyName.NAME.equals(nodeName))
			{
				this.partyName = new PartyName(this);
				this.partyName.parse(node);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public PartyIdentification getPartyIdentification()
	{
		return this.partyIdentification;
	}
	
	public PartyName getPartyName()
	{
		return this.partyName;
	}
	
	//
	
	public static final transient String NAME = "PayeeParty";
	
}
