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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.framework.X;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.wf.MWFActivity;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * $Id: DocumentWorkflowDeletionValidator.java,v 1.4.2.1 2010/01/06 11:45:28 tomassen Exp $
 * 
 * Prevent deletion of document while WF is running
 * Required for Compiere 3.2.1
 * Issue is fixed in Compiere 3.4.x (see ticket 10020724)
 */
public class DocumentWorkflowDeletionValidator implements ModelValidator
{

	public DocumentWorkflowDeletionValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	/* line -> header */
	private HashMap<String,String> lineTables = new HashMap<String,String>();
	
	/* header -> lines */
	private HashMap<String,HashSet<String>> headerTables = new HashMap<String,HashSet<String>>();

	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("DocumentWorkflowDeletionValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		String sql = "SELECT ht.TableName AS HeaderTable, lt.TableName AS LineTable"
			+ " FROM AD_Table ht"
			+ " INNER JOIN AD_Column hkc ON (hkc.AD_Table_ID=ht.AD_Table_ID)"
			+ " INNER JOIN AD_Column llc ON (llc.ColumnName=hkc.ColumnName)"
			+ " INNER JOIN AD_Table lt ON (lt.AD_Table_ID=llc.AD_Table_ID)"
			+ " INNER JOIN AD_Column lkc ON (lkc.AD_Table_ID=lt.AD_Table_ID)"
			+ " INNER JOIN AD_Workflow wf ON (wf.AD_Table_ID=ht.AD_Table_ID)"
			+ " WHERE llc.IsParent='Y'"
			+ " AND hkc.ColumnName=ht.TableName || '_ID'"
			+ " AND lkc.ColumnName = lt.TableName || '_ID'"
			+ " ORDER BY ht.TableName, lt.TableName";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				String headerTable = rs.getString(1);
				String lineTable = rs.getString(2);
				
				log.fine("header=" + headerTable + ", line=" + lineTable);
				
				HashSet<String> headerLines = this.headerTables.get(headerTable);
				
				if (headerLines == null)
				{
					headerLines = new HashSet<String>();
					this.headerTables.put(headerTable, headerLines);
				}
				
				headerLines.add(lineTable);
				this.lineTables.put(lineTable, headerTable);
			}
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		for (Iterator<String> headers = this.headerTables.keySet().iterator(); headers.hasNext();)
		{
			String header = headers.next();
			log.fine("header=" + header);
			this.engine.addModelChange(header, this);
			
			for (Iterator<String> lines = this.headerTables.get(header).iterator(); lines.hasNext();)
			{
				String line = lines.next();
				log.fine("line=" + line);
				this.engine.addModelChange(line, this);
			}
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
		String msg = null;
		
		if (ModelValidator.CHANGETYPE_DELETE == type)
		{
    		PO header = this.getHeader(po);
    		if (header != null)
    		{
    			log.fine("po=" + po);
    			log.fine("header=" + header);
    		
    			MTable t = MTable.get(header.getCtx(), header.get_TableName());
    			
    			String wfStatus = MWFActivity.getActiveInfo(header.getCtx(), t.getAD_Table_ID(), header.get_ID());
    			
    			log.finer("AD_Table_ID=" + header.get_Table_ID() + ", Record_ID" + header.get_ID());
    			
    			if (wfStatus != null)
    			{
    				msg = Msg.getMsg(header.getCtx(), "WFActiveForRecord");
    			}
    			
    			log.fine("WFStatus=" + wfStatus);
    		}
		}
		
		return msg;
	}
	
	public String docValidate(PO doc, int timing)
	{
		return null;
	}
	
	private PO getHeader(PO po)
	{
		PO header = null;
		
		if (po == null)
		{
			;
		}
		else if (this.headerTables.containsKey(po.get_TableName()))
		{
			header = po;
		}
		else if (this.lineTables.containsKey(po.get_TableName()))
		{
			String headerTable = this.lineTables.get(po.get_TableName());
			String linkColumn = headerTable + "_ID";
			int parentID = po.get_ValueAsInt(linkColumn);
			MTable t = MTable.get(po.getCtx(), headerTable);
			header = new X(po.getCtx(), t, parentID, po.get_TrxName());
		}
		
		return header;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}
	
}
