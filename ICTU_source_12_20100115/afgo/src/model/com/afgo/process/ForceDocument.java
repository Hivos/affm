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
package com.afgo.process;

import java.util.ArrayList;

import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ForceDocument.java,v 1.6.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ForceDocument extends SvrProcess
{
	
	private int AD_Table_ID = -1;
	
	private int Record_ID = -1;
	
	private String DocStatus = null;
	
	private String DocAction = null;
	
	private String Processed = null;
	
	private String Processing = null;

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Table_ID"))
				this.AD_Table_ID = para[i].getParameterAsInt();
			else if (name.equals("Record_ID"))
				this.Record_ID= para[i].getParameterAsInt();
			else if (name.equals("DocStatus"))
				this.DocStatus = (String)para[i].getParameter();
			else if (name.equals("DocAction"))
				this.DocAction = (String)para[i].getParameter();
			else if (name.equals("Processed"))
				this.Processed = (String)para[i].getParameter();
			else if (name.equals("Processing"))
				this.Processing = (String)para[i].getParameter();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		MTable table = MTable.get(this.getCtx(), this.AD_Table_ID);
		
		String sql = "UPDATE " + table.getTableName();
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		if (this.DocStatus != null)
		{
			if (params.size() > 0)
				sql = sql + ",";
			else
				sql = sql + " SET ";
			sql = sql + "DocStatus=?";
			params.add(this.DocStatus);
		}
		
		if (this.DocAction != null)
		{
			if (params.size() > 0)
				sql = sql + ",";
			else
				sql = sql + " SET ";
			sql = sql + "DocAction=?";
			params.add(this.DocAction);
		}
		
		if (this.Processed != null)
		{
			if (params.size() > 0)
				sql = sql + ",";
			else
				sql = sql + " SET ";
			sql = sql + "Processed=?";
			params.add(this.Processed);
		}
		
		if (this.Processing != null)
		{
			if (params.size() > 0)
				sql = sql + ",";
			else
				sql = sql + " SET ";
			sql = sql + "Processing=?";
			params.add(this.Processing);
		}
		
		sql = sql + " WHERE " + table.getTableName() + "_ID=?"
			+ " AND AD_Client_ID=?";
		
		params.add(this.Record_ID);
		params.add(this.getAD_Client_ID());
		
		log.fine(sql);

		int result = 0;
		
		if (sql.toUpperCase().contains("SET"))
			result = DB.executeUpdate(sql, params, false, this.get_TrxName());
		
		return "@Updated@=" + result;
	}

}
