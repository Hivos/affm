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
 * @version $Id: FinancialInstitutionBranch.java,v 1.3 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public class FinancialInstitutionBranch extends CommonAggregateComponent
{
	
	public FinancialInstitutionBranch(CommonAggregateComponent parent)
	{
		super(parent);
	}
	
	private String ID = null;
	
	private String name = null;
	
	private String cityName = null;


	protected boolean parseField(String nodeName, Node node)
	{
		if (CommonBasicComponent.PREFIX.equals(node.getPrefix()))
		{
			if (CommonBasicComponent.ID.equals(nodeName))
			{
				this.ID = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.Name.equals(nodeName))
			{
				this.name = node.getTextContent();
				return true;
			}
			else if (CommonBasicComponent.CityName.equals(nodeName))
			{
				this.cityName = node.getTextContent();
				return true;
			}
			
			return false;
		}
		
		return false;
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCityName()
	{
		return this.cityName;
	}

	//
	
	public static final transient String NAME = "FinancialInstitutionBranch";
}
