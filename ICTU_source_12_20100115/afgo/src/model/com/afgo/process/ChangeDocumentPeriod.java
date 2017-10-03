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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.compiere.model.MClientInfo;
import org.compiere.model.MDocBaseType;
import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ChangeDocumentPeriod.java,v 1.2.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ChangeDocumentPeriod extends SvrProcess
{
	
	private int C_Period_ID = -1;
	
	private String DocBaseType = null;
	
	private int C_DocType_ID = -1;
	
	private Timestamp dateAcct = null;

	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("C_Period_ID"))
				this.C_Period_ID = para[i].getParameterAsInt();
			else if (name.equals("DocBaseType"))
				this.DocBaseType = (String)para[i].getParameter();
			else if (name.equals("C_DocType_ID"))
				this.C_DocType_ID= para[i].getParameterAsInt();
			else if (name.equals("DateAcct"))
				this.dateAcct = (Timestamp)para[i].getParameter();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		if (this.C_Period_ID < 1)
			throw new CompiereUserException("NoPeriod");
		log.fine("C_Period_ID=" + this.C_Period_ID);
		
		if (this.dateAcct == null)
			throw new CompiereUserException("NoDateAcct");
		log.fine("DateAcct=" + this.dateAcct);
		
		MClientInfo clientInfo = MClientInfo.get(this.getCtx(), this.getAD_Client_ID());
		MPeriod targetPeriod = MPeriod.getOfCalendar(this.getCtx(), clientInfo.getC_Calendar_ID(), this.dateAcct);
		
		if (targetPeriod == null)
			throw new CompiereUserException("NoTargetPeriod");
		
		log.info("TargetPeriod=" + targetPeriod);
		
		MDocType docType = null;
		
		if (this.C_DocType_ID > 0)
		{
			docType = new MDocType(this.getCtx(), this.C_DocType_ID, this.get_TrxName());
		}
		
		HashSet<String> docBaseTypeStrings = new HashSet<String>();
		
		MDocType[] docTypes = MDocType.getOfClient(this.getCtx());
		for (int i = 0; i < docTypes.length; i++)
		{
			if (this.C_DocType_ID > 0 && this.C_DocType_ID != docTypes[i].getC_DocType_ID())
			{
				continue;
			}
			
			docBaseTypeStrings.add(docTypes[i].getDocBaseType());
		}
		
		HashSet<String> tableNames = new HashSet<String>(); 
		
		MDocBaseType[] docBaseTypes = MDocBaseType.getAll(this.getCtx());
		for (int i = 0; i < docBaseTypes.length; i++)
		{
			if (!docBaseTypeStrings.contains(docBaseTypes[i].getDocBaseType()))
			{
				continue;
			}
		
			String periodOpen = null;
			periodOpen = targetPeriod.isOpen(docBaseTypes[i].getDocBaseType(), this.dateAcct);
			
			if (periodOpen != null)
			{
				throw new CompiereUserException(periodOpen);
			}
			
			if (docType != null && !docType.getDocBaseType().equals(docBaseTypes[i].getDocBaseType()))
			{
				continue;
			}
			
			if (this.DocBaseType != null && !this.DocBaseType.equals(docBaseTypes[i].getDocBaseType()))
			{
				continue;
			}
			
			tableNames.add(docBaseTypes[i].getTableName());
		}
		
		for (Iterator<String> tables = tableNames.iterator(); tables.hasNext();)
		{
			String tableName = tables.next();
			
			String sql = "SELECT COUNT(*)"
				+ " FROM AD_Table t"
				+ " INNER JOIN AD_Column cdt ON (cdt.AD_Table_ID=t.AD_Table_ID AND cdt.ColumnName='C_DocType_ID')"
				+ " INNER JOIN AD_Column cda ON (cda.AD_Table_ID=t.AD_Table_ID AND cda.ColumnName='DateAcct')"
				+ " INNER JOIN AD_Column cdk ON (cdk.AD_Table_ID=t.AD_Table_ID AND cdk.ColumnName=t.TableName || '_ID')"
				+ " WHERE t.TableName=?";
			
			log.fine(sql);
			
			if (DB.getSQLValue(this.get_TrxName(), sql, tableName) != 1)
			{
				continue;
			}
			
			int updated = updateTable(tableName);
			if (updated != 0)
			{
				this.addLog(tableName + ": @Updated@ " + updated);
			}
		}
		
		return "OK";
	}

	private int updateTable(String tableName)
	{
		int result = -1;
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.dateAcct);
		params.add(this.C_Period_ID);
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE " + tableName);
		sql.append(" SET DateAcct=?");
		sql.append(" WHERE " + tableName + "_ID IN");
		sql.append(" (");
		sql.append(" 	SELECT " + tableName + "_ID");
		sql.append(" 	FROM " + tableName + " t");
		sql.append(" 	INNER JOIN C_Period p ON (p.AD_Client_ID=t.AD_Client_ID AND p.StartDate <= t.DateAcct AND p.EndDate >= t.DateAcct)");
		sql.append(" 	INNER JOIN C_DocType dt ON (dt.C_DocType_ID=t.C_DocType_ID)");
		sql.append(" 	WHERE t.DocStatus IN ('DR', 'IP', 'AP')");
		sql.append(" 	AND p.C_Period_ID=?");
		if (this.DocBaseType != null)
		{
				sql.append(" AND dt.DocBaseType=?");
				params.add(this.DocBaseType);
		}
		if (this.C_DocType_ID > 0)
		{
			sql.append(" AND dt.C_DocType_ID=?");
			params.add(this.C_DocType_ID);
		}
		sql.append(" )");
		
		log.fine(sql.toString());
		
		result = DB.executeUpdate(sql.toString(), params, false, this.get_TrxName());
		
		return result;
	}

}
