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
import java.util.HashSet;
import java.util.Iterator;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DocumentProcessedLineDeletionValidator.java,v 1.5.2.1 2010/01/06 11:45:28 tomassen Exp $
 * 
 * Prevent document lines from being deleted after the header is set to Processed=Y
 * Most "core" Compiere documents have a Processed column on line level that prevents this.
 * The AFGO documents do not have this.
 *
 */
public class DocumentProcessedLineDeletionValidator implements ModelValidator
{
	public DocumentProcessedLineDeletionValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private HashSet<String> tableNames = new HashSet<String>();
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		
		this.log.info("DocumentProcessedLineDeletionValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		String sql = "SELECT ht.TableName, lt.TableName, at.TableName"
			+ " FROM AD_Table ht"
			+ " INNER JOIN AD_Column pc ON (pc.AD_Table_ID=ht.AD_Table_ID AND pc.ColumnName='Processed')"
			+ " INNER JOIN AD_Column hkc ON (hkc.AD_Table_ID=ht.AD_Table_ID AND hkc.ColumnName=ht.TableName||'_ID' AND hkc.IsKey='Y')"
			+ " INNER JOIN AD_Column llc ON (llc.ColumnName=hkc.ColumnName AND llc.IsParent='Y')"
			+ " INNER JOIN AD_Table lt ON (lt.AD_Table_ID=llc.AD_Table_ID)"
			+ " LEFT OUTER JOIN AD_Column lkc ON (lkc.AD_Table_ID=lt.AD_Table_ID AND lkc.ColumnName=lt.TableName||'_ID' AND lkc.IsKey='Y')"
			+ " LEFT OUTER JOIN AD_Column alc ON (alc.ColumnName=lkc.ColumnName AND alc.IsParent='Y')"
			+ " LEFT OUTER JOIN AD_Table at ON (at.AD_Table_ID=alc.AD_Table_ID AND at.AD_TAble_ID<>lt.AD_Table_ID AND at.AD_TAble_ID<>ht.AD_Table_ID)" 
			+ " WHERE (ht.IsView IS NULL OR ht.IsView='N')"
			+ " AND (ht.ImportTable IS NULL OR ht.ImportTable='N')"
			+ " AND ht.TableName NOT LIKE 'T_%'"
			+ " AND ht.TableName NOT LIKE 'AD_%'"
			+ " AND lt.EntityType=?"
			+ " AND (lt.IsView IS NULL OR lt.IsView='N')"
			+ " AND (lt.ImportTable IS NULL OR lt.ImportTable='N')"
			+ " AND lt.TableName NOT LIKE 'T_%'"
			+ " AND (at.IsView IS NULL OR at.IsView='N')"
			+ " AND (at.ImportTable IS NULL OR at.ImportTable='N')";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, AFGOModelValidator.EntityType);
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				String headerTableName = rs.getString(1);
				String lineTableName = rs.getString(2);
				String allocationTableName = rs.getString(3);
				log.fine("header: " + headerTableName + " line: " + lineTableName + "allocation:" + allocationTableName);
				
				this.tableNames.add(headerTableName);
				this.tableNames.add(lineTableName);
				this.tableNames.add(allocationTableName);
			}
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
		
		for (Iterator<String> tableNames = this.tableNames.iterator(); tableNames.hasNext();)
		{
			String tableName = tableNames.next();
			this.engine.addModelChange(tableName, this);
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
		if (po != null && ModelValidator.CHANGETYPE_DELETE == type)
		{
			PO parent = this.getParent(po);
			if (parent != null)
			{
				System.err.println("line=" + po + ", header=" + parent);
				boolean processed = parent.get_ValueAsBoolean("Processed");
				if (processed)
					return Msg.getMsg(po.getCtx(), "Processed");
			}
		}
		return null;
	}
	
	private PO getParent(PO po)
	{
		if (po == null)
			return po;
		
		MTable table = MTable.get(po.getCtx(), po.get_Table_ID());
		MColumn[] cols = table.getColumns(false);
		for (int i = 0; i < cols.length; i++)
		{
			if (cols[i].isParent())
			{
				MTable parentTable = MTable.get(po.getCtx(), cols[i].getColumnName().substring(0, cols[i].getColumnName().length() - 3));
				int Parent_ID = po.get_ValueAsInt(cols[i].getColumnName());
				PO parent = parentTable.getPO(po.getCtx(), Parent_ID, po.get_TrxName());
				return this.getParent(parent);
			}
		}
		
		return po;
	}
	
	public String docValidate(PO doc, int timing)
	{
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
