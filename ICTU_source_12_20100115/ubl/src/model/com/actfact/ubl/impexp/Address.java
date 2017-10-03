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

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: Address.java,v 1.5 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class Address extends CommonAggregateComponent
{
	
	Address(CommonAggregateComponent parent)
	{
		super(parent);
	}

	private String streetName = null;
	
	private String buildingNumber = null;
	
	private String cityName = null;
	
	private String postalZone = null;
	
	private Country country = null;
	
	private ArrayList<AddressLine> addressLines = new ArrayList<AddressLine>();

	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonBasicComponent.PREFIX.equals(node.getPrefix()))
		{
			if (CommonBasicComponent.StreetName.equals(nodeName))
			{
				this.streetName = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.BuildingNumber.equals(nodeName))
			{
				this.buildingNumber = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.CityName.equals(nodeName))
			{
				this.cityName = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.PostalZone.equals(nodeName))
			{
				this.postalZone = node.getTextContent();
				return true;
			}
			
			return false;
		}
		else if (CommonAggregateComponent.PREFIX.equals(node.getPrefix()))
		{
			if (Country.NAME.equals(nodeName))
			{
				this.country = new Country(this);
				this.country.parse(node);
				return true;
			}
			else if (AddressLine.NAME.equals(nodeName))
			{
				AddressLine addressLine = new AddressLine(this);
				addressLine.parse(node);
				this.addressLines.add(addressLine);
				return true;
			}

			return false;
		}
		
		return false;
	}
	
	public String getStreetName()
	{
		return this.streetName;
	}
	
	public String getBuildingNumber()
	{
		return this.buildingNumber;
	}
	
	public String getCityName()
	{
		return this.cityName;
	}
	
	public String getPostalZone()
	{
		return this.postalZone;
	}
	
	public Country getCountry()
	{
		return this.country;
	}
	
	//
	
	public static final transient String NAME = "Address";
	
}
