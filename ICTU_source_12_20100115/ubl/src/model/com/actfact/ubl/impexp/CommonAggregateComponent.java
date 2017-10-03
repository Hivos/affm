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
import org.w3c.dom.NodeList;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * @version $Id: CommonAggregateComponent.java,v 1.5 2010/01/06 12:05:27 tomassen Exp $
 * 
 */
public abstract class CommonAggregateComponent
{
	CommonAggregateComponent(CommonAggregateComponent parent)
	{
		this.parent = parent;
	}
	
	private CommonAggregateComponent parent = null;
	
	public void parse(Node node)
	{
		if (node == null)
			throw new IllegalArgumentException("NoNode");
		
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.contains(":"))
				nodeName = nodeName.substring(nodeName.lastIndexOf(":") + 1);
			
			if (!"#text".equals(nodeName))
			{
				if (!this.parseField(nodeName, child))
					warning(node, child);
			}
		}
	}
	
	public CommonAggregateComponent getParent()
	{
		return this.parent;
	}

	protected abstract boolean parseField(String nodeName, Node node);
	
	protected void warning(Node parent, Node child)
	{
		warning("not parsed: " + parent.getNodeName() + "->" + child.getNodeName());
		//System.err.println(parent.getNodeName() + "->" + child.getNodeName());
	}
	
	protected void warning(String msg)
	{
		this.getParent().warning(msg);
	}
	
	protected void error(String msg)
	{
		this.getParent().error(msg);
	}
	
	//
	
	public static String NAME = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
	
	public static String PREFIX = "cac";
	
	//
	
}

