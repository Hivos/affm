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
package com.afgo.apps.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MClient;
import org.compiere.model.MForm;
import org.compiere.model.MInfoColumn;
import org.compiere.model.MInfoWindow;
import org.compiere.swing.CPanel;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.CLogger;

import com.afgo.model.MAFGOForm;
import com.afgo.model.MAFGOFormColumn;
import com.afgo.model.MAFGOFormTable;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: VTableBasedForm.java,v 1.2.2.1 2010/01/06 11:26:28 tomassen Exp $
 *
 */
public abstract class VTableBasedForm extends CPanel
{
	
	public VTableBasedForm()
	{
		
	}
	
	protected abstract Ctx getCtx();
	
	protected abstract int getWindowNo();
	
	private MForm form = null;
	
	private MAFGOForm tableBasedForm = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	protected MForm getForm()
	{
		if (this.form == null)
		{
    		String formName = this.getCtx().getContext(this.getWindowNo(), "WindowName");
    		log.fine("Form=" + formName);
    		
    		String sql = "SELECT *"
    			+ " FROM AD_Form";
    		
    		if (Language.isBaseLanguage(Env.getAD_Language(this.getCtx())))
    		{
    			sql += " WHERE Name=?";
    		}
    		else
    		{
    			sql += " WHERE AD_Form_ID IN"
    				+ " ("
    				+ " 	SELECT AD_Form_ID"
    				+ " 	FROM AD_Form_Trl"
    				+ " 	WHERE Name=?"
    				+ " 	AND AD_Language=?"
    				+ " )";
    		}
    		
    		PreparedStatement pstmt = null;
    		ResultSet rs = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement(sql, null);
    			pstmt.setString(1, formName);
    			if (!Language.isBaseLanguage(Env.getAD_Language(this.getCtx())))
    				pstmt.setString(2, Env.getAD_Language(this.getCtx()));
    			
    			rs = pstmt.executeQuery();
    			if (rs.next())
    			{
    				this.form = new MForm(this.getCtx(), rs, null);
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
    		
    		
    		if (this.form == null)
    			throw new IllegalStateException("NoForm");
		}
		
		return this.form;
	}
	
	protected MAFGOForm getTableBasedForm()
	{
		if (this.tableBasedForm == null)
		{
			this.tableBasedForm = MAFGOForm.getClientTableForm(MClient.get(this.getCtx()), this.getForm());
		}
		
		return this.tableBasedForm;
	}
	
	protected MAFGOFormTable getFormTable(String table)
	{
		MAFGOForm tableBasedForm = this.getTableBasedForm();
		if (tableBasedForm == null)
		{
			log.warning("NoTableBasedForm: AD_Form_ID=" + this.getForm().getAD_Form_ID());
			return null;
		}
		
		MAFGOFormTable formTable = tableBasedForm.getFormTable(table);
		if (formTable == null)
		{
			log.warning("NoFormTable: AD_Form_ID=" + tableBasedForm.getAD_Form_ID() + ", AFGO_Form_ID=" + tableBasedForm.getAFGO_Form_ID() + ", table=" + table);
			return null;
		}
		
		return formTable;
	}
	
}
