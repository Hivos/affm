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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.compiere.framework.PO;
import org.compiere.util.CompiereUserException;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ProgramPeriod.java,v 1.6.2.1 2010/01/06 11:45:24 tomassen Exp $
 *
 */
public interface ProgramPeriod
{
	public PO getPO();
	
	public Timestamp getStartDate();
	
	public Timestamp getEndDate();
	
	public String getName();
	
	public int getDays() throws CompiereUserException; 
	
	public int getDays(Date startDate, Date endDate) throws CompiereUserException;
	
	public int getAFGO_Year_ID();
	
	public int getAFGO_Quarter_ID();
	
	public int getAFGO_Month_ID();
	
	public boolean isDefault();
	
	public String getIsClosed();
	
	public void setIsClosed(String isClosed);
	
	public boolean isPeriodOpen();
	
	public ArrayList<MAFGOMonth> getMonths();
}
