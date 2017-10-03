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

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.X_AFGO_FormTable;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOFormTable.java,v 1.2.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class MAFGOFormTable extends X_AFGO_FormTable
{

	public MAFGOFormTable(Ctx ctx, int AFGO_FormTable_ID, String trxName)
	{
		super(ctx, AFGO_FormTable_ID, trxName);

	}
	
	public MAFGOFormTable(Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);

	}
	
	private ArrayList<MAFGOFormColumn> columns = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public String getFromClause()
	{
		String fromClause = super.getFromClause();
		fromClause = fromClause.replace("\n", " ");
		if (fromClause.toUpperCase().startsWith("FROM "))
			fromClause = fromClause.substring(5);
		return fromClause;
	}
	
	public ArrayList<MAFGOFormColumn> getColumns()
	{
		if (this.columns == null)
		{
			this.columns = new ArrayList<MAFGOFormColumn>();
			
			String sql = "SELECT fc.*"
				+ " FROM AFGO_FormColumn fc"
				+ " WHERE fc.AFGO_FormTable_ID=?"
				+ " AND fc.IsActive='Y'"
				+ " ORDER BY  fc.SeqNo";
				
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sql, this.get_TrxName());
				pstmt.setInt(1, this.getAFGO_FormTable_ID());
				
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					MAFGOFormColumn column = new MAFGOFormColumn(this.getCtx(), rs, this.get_TrxName());
					log.info(column.getName() + " [" + column.getSeqNo() + "]");
					this.columns.add(column);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				log.severe(e.toString());
			}
			finally
			{
    			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
    			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
			}
		}
		
		return this.columns;
	}

}
