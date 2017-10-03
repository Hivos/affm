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

import org.compiere.model.MEntityType;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: EntityTypeModule.java,v 1.3.2.1 2010/01/06 11:45:28 tomassen Exp $
 *
 */
public class EntityTypeModule
{
	public EntityTypeModule(MEntityType entityType)
	{
		this.entityType = entityType;
		
		this.versionString = entityType.getVersion().split("\\.");
	}
	
	private MEntityType entityType = null;
	
	private String[] versionString = null;
	
	public String getName()
	{
		return this.entityType.getName();
	}
	
	public int getMajor()
	{
		return Integer.parseInt(this.versionString[0]);
	}
	
	public int getMinor()
	{
		return Integer.parseInt(this.versionString[1]);
	}
	
	public int getRevision()
	{
		return Integer.parseInt(this.versionString[2]);
	}
	
	public String getVersionString()
	{
		return "v" + this.getMajor() + "." + this.getMinor() + "." + this.getRevision();
	}
	
}
