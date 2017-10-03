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
import java.sql.ResultSet;
import java.util.HashMap;

import org.compiere.model.M_Element;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueNamePair;

import compiere.model.X_AFGO_FormColumn;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFormColumn.java,v 1.2.2.1 2010/01/06 11:45:26 tomassen Exp $
 *
 */
public class MAFGOFormColumn extends X_AFGO_FormColumn
{

	public MAFGOFormColumn(Ctx ctx, int AFGO_FormColumn_ID, String trxName)
	{
		super(ctx, AFGO_FormColumn_ID, trxName);

	}
	
	public MAFGOFormColumn(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private HashMap<String,String> headerNames = new HashMap <String,String>();
	
	private M_Element element = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public M_Element getElement()
	{
		if (this.element == null && this.getAD_Element_ID() > 0)
		{
			this.element = new M_Element(this.getCtx(), this.getAD_Element_ID(), this.get_TrxName());
		}
		return this.element;
	}
	
	public String getHeader(String AD_Language)
	{
		return this.getName();
	}
	
	public Class<?> getJavaClass()
	{
		Class<?> clazz = Object.class;
		
		if (DisplayType.isLookup(this.getAD_Reference_ID()))
				clazz = KeyNamePair.class;
		else if (DisplayType.isID(this.getAD_Reference_ID()))
			clazz =  KeyNamePair.class;
		else if (DisplayType.isNumeric(this.getAD_Reference_ID()))
			clazz =  BigDecimal.class;
		else if (DisplayType.isText(this.getAD_Reference_ID()))
			clazz =  String.class;
		
		return clazz;
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getElement() != null)
		{
			log.fine("AD_Element_ID=" + this.getElement().getName() + ", Name=" + this.getElement().getName());
			this.setName(this.getElement().getName());
		}
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (this.getElement() != null && success)
		{
			String sql = "UPDATE AFGO_FormColumn_Trl fct"
				+ " SET fct.Name="
				+ " ("
				+ " 	SELECT et.Name"
				+ " 	FROM AFGO_FormColumn fc"
				+ " 	INNER JOIN AD_Element_Trl et ON (et.AD_Element_ID=fc.AD_Element_ID)"
				+ " 	WHERE fc.AFGO_FormColumn_ID=fct.AFGO_FormColumn_ID"
				+ " 	AND et.AD_Language=fct.AD_Language"
				+ " )"
				+ " WHERE fct.AFGO_FormColumn_ID=?";
			
			DB.executeUpdate(sql, this.getAFGO_FormColumn_ID(), this.get_TrxName());
		}
		
		return success;
	}

}
