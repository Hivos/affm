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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.framework.POInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DisplayIdentifierValidator.java,v 1.3.2.1 2010/01/06 11:45:24 tomassen Exp $
 * 
 * Prevent records from being saved with typical "identifier" 
 * values (name, value) with only/trailing/leading spaces
 *
 */
public class DisplayIdentifierValidator implements ModelValidator
{

	public DisplayIdentifierValidator()
	{
		this.identifierColumns.add("Name");
		this.identifierColumns.add("Value");
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private ArrayList<String> identifierColumns = new ArrayList<String>();
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		log.info("DisplayIdentifierModelValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		String sql = "SELECT t.TableName"
			+ " FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)"
			+ " WHERE t.IsActive='Y'"
			+ " AND t.AccessLevel!=4"
			+ " AND t.IsView='N'"
			+ " AND c.ColumnName IN ('Name', 'Value')"
			+ " GROUP BY t.TableName";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					log.fine(rs.getString(1));
					engine.addModelChange(rs.getString(1), this);
				}
				
				rs.close();
				rs = null;
				pstmt.close();
				pstmt = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
				if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	
	public String modelChange(PO po, int type) throws Exception
	{
		POInfo poinfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID());
		
		if (po.getAD_Client_ID() < 11)
			return null;
		
		for (Iterator<String> cols = this.identifierColumns.iterator(); cols.hasNext();)
		{
			String colName = cols.next();
			
			int colIdx = poinfo.getColumnIndex(colName);
			
			if (colIdx < 0)
				continue;
			
			if (!String.class.equals(poinfo.getColumnClass(colIdx)))
				continue;
			
			String value = (String)po.get_Value(colName);
			if (value == null)
				continue;
			String oldValue = (String)po.get_ValueOld(colName);
			if (oldValue == null)
				continue;
			
			if (value.equals(oldValue))
				continue;

			String newValue = this.cleanIdentifierString(value);
			
			if (newValue.equals(value))
				continue;
			
			log.fine(this.hashCode() + " clean: " + poinfo.getTableName() + "." + colName + " [" + value + "]=[" + newValue + "]");
			
			po.set_Value(colName, newValue);
		}
		
		return null;
	}
	
	private String cleanIdentifierString(String value)
	{
		value = value.trim();
		
		return value;
	}

	public String docValidate(PO doc, int timing)
	{
		return null;
	}

	public static void main(String[] args)
	{
		String name = "  vdfvdf fdvdfvdfv  ";
		
		name = name.trim();
		
		if (name == null || name.length() < 1)
			System.out.println("invalid name");
		else
			System.out.println("name=" + name);
			
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
