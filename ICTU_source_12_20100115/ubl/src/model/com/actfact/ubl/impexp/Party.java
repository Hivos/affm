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
 * @version $Id: Party.java,v 1.4 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class Party extends CommonAggregateComponent
{
	Party(CommonAggregateComponent parent)
	{
		super(parent);
	}

	private PartyName partyName = null;
	
	private PostalAddress postalAddress = null;
	
	private PhysicalLocation physicalLocation = null;

	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonAggregateComponent.PREFIX.equals(node.getPrefix()))
		{
			if (PartyName.NAME.equals(nodeName))
			{
				this.partyName = new PartyName(this);
				this.partyName.parse(node);
				return true;
			}
			else if (PostalAddress.NAME.equals(nodeName))
			{
				this.postalAddress = new PostalAddress(this);
				this.postalAddress.parse(node);
				return true;
			}
			else if (PhysicalLocation.NAME.equals(nodeName))
			{
				this.physicalLocation = new PhysicalLocation(this);
				this.physicalLocation.parse(node);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public PartyName getPartyName()
	{
		return this.partyName;
	}
	
	public PostalAddress getPostalAddress()
	{
		return this.postalAddress;
	}
	
	public PhysicalLocation getPhysicalLocation()
	{
		return this.physicalLocation;
	}
	
	//
	
	public static final transient String NAME = "Party";
	
}
