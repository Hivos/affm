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

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MClient;
import org.compiere.model.MForm;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.X_AFGO_Form;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOForm.java,v 1.2.2.1 2010/01/06 11:45:25 tomassen Exp $
 *
 */
public class MAFGOForm extends X_AFGO_Form
{

	public MAFGOForm(Ctx ctx, int AFGO_Form_ID, String trxName)
	{
		super(ctx, AFGO_Form_ID, trxName);

	}
	
	public MAFGOForm(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private ArrayList<MAFGOFormTable> formTables = null;
	
	private HashMap<String,MAFGOFormTable> formTableValues = null;
	
	private HashMap<Integer,MAFGOFormTable> formTablesSequences = null;

	private CLogger log = CLogger.getCLogger(getClass());
	
	private void loadFormTables()
	{
		this.formTables = new ArrayList<MAFGOFormTable>();
		this.formTableValues = new HashMap<String,MAFGOFormTable>();
		this.formTablesSequences = new HashMap<Integer,MAFGOFormTable>();

		String sql = "SELECT ft.*"
			+ " FROM AFGO_FormTable ft"
			+ " WHERE AFGO_Form_ID=?"
			+ " AND IsActive='Y'"
			+ " ORDER BY SeqNo";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Form_ID());
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				MAFGOFormTable formTable = new MAFGOFormTable(this.getCtx(), rs, this.get_TrxName());
				this.formTables.add(formTable);
				this.formTablesSequences.put(formTable.getSeqNo(), formTable);
				this.formTableValues.put(formTable.getValue(), formTable);
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

	}
	
	public MAFGOFormTable getFormTable(String table)
	{
		if (this.formTableValues == null)
		{
			this.loadFormTables();
		}
		
		return this.formTableValues.get(table);
	}
	
	
	//
	
	private static HashMap<Integer,HashMap<Integer,Integer>> clientTableForms = new HashMap<Integer,HashMap<Integer,Integer>>();
	
	private static CCache<Integer,MAFGOForm> s_cache = new CCache<Integer,MAFGOForm>(MAFGOForm.Table_Name, 5);
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOForm.class);
	
	public static MAFGOForm getForm(Ctx ctx, int AFGO_Form_ID)
	{
		MAFGOForm form = (AFGO_Form_ID > 0) ? s_cache.get(ctx, AFGO_Form_ID) : null;
		
		if (AFGO_Form_ID > 0 && form == null)
		{
			form = new MAFGOForm(ctx, AFGO_Form_ID, null);
			
			if (AFGO_Form_ID == form.getAFGO_Form_ID())
				s_cache.put(form.getAFGO_Form_ID(), form);
			else
				form = null;
		}
		
		return form;
	}
	
	public static MAFGOForm getClientTableForm(MClient client, MForm form)
	{
		MAFGOForm tableForm = null;
		
		HashMap<Integer,Integer> tableForms = clientTableForms.get(client.getAD_Client_ID());
		if (tableForms == null)
		{
			tableForms = new HashMap<Integer,Integer>();
			clientTableForms.put(client.getAD_Client_ID(), tableForms);
		}
		
		Integer AFGO_Form_ID = null;
		
		if (tableForms.containsKey(form.getAD_Form_ID()))
		{
			AFGO_Form_ID = tableForms.get(form.getAD_Form_ID());
			if (AFGO_Form_ID != null && AFGO_Form_ID > 0)
			{
				tableForm = getForm(client.getCtx(), AFGO_Form_ID);
			}
		}
		else
		{
			tableForm = loadClientTableForm(client, form);
			if (tableForm != null)
			{
				tableForms.put(form.getAD_Form_ID(), tableForm.getAFGO_Form_ID());
			}
		}
		
		return tableForm;
	}
	
	private static MAFGOForm loadClientTableForm(MClient client, MForm form)
	{
		MAFGOForm tableForm = null;
		
		Integer AFGO_Form_ID = null;
		
		String sql = "SELECT f.AFGO_Form_ID"
			+ " FROM AFGO_Form f"
			+ " WHERE AD_Form_ID=?"
			+ " AND AD_Client_ID IN (0, ?)"
			+ " AND IsActive='Y'"
			+ " ORDER BY f.SeqNo";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, form.getAD_Form_ID());
			pstmt.setInt(2, client.getAD_Client_ID());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				AFGO_Form_ID = rs.getInt(1);
				s_log.fine("AFGO_Form_ID=" + AFGO_Form_ID);
			}
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			s_log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		if (AFGO_Form_ID != null && AFGO_Form_ID > 0)
			tableForm = getForm(client.getCtx(), AFGO_Form_ID);
		
		return tableForm;
	}
}
