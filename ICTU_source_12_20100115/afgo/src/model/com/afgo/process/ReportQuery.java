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

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.compiere.model.MPInstancePara;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ReportQuery.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class ReportQuery 
{
	public ReportQuery(Ctx ctx, int AD_PInstance_ID, String trxName)
	{
		this.ctx = ctx;
		this.AD_PInstance_ID = AD_PInstance_ID;
		this.trxName = trxName;
	}
	
	private Ctx ctx = null;
	
	private int AD_PInstance_ID = -1;
	
	private String trxName = null;
	
	private LinkedList<QueryRestriction> restrictions = new LinkedList<QueryRestriction>();
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void addRestriction(String columnName, String operator, Class<?> type, Object value)
	{
		restrictions.add(new QueryRestriction(columnName, operator, type, value));
	}
	
	private class QueryRestriction 
	{
		private QueryRestriction(String columnName, String operator, Class<?> type, Object value)
		{
			this.columnName = columnName;
			this.operator = operator;
			this.type = type;
			this.value = value;
		}
		
		private String columnName = null;
		
		private String operator = null;
		
		private Class<?> type = null;
		
		private Object value = null;
		
		public String getColumnName()
		{
			return this.columnName;
		}
		
		public String getOperator()
		{
			return this.operator;
		}
		
		public Class<?> getType()
		{
			return this.type;
		}
		
		public Object getValue()
		{
			return this.value;
		}
		
		public String getSQL()
		{
			return this.getColumnName() + this.getOperator() + "?";
		}
		
	};
	
	public String getWhereClause()
	{
		StringBuffer sql = new StringBuffer();
		
		for (int i = 0; i < this.restrictions.size(); i++)
		{
			if (i > 0)
				sql.append(" " + AND_OPERATOR + " ");
			
			QueryRestriction restriction = this.restrictions.get(i);
			sql.append(restriction.getSQL());
		}
		
		return sql.toString();
	}
	
	public void fillParameters(PreparedStatement pstmt)
	{
		try
		{
			for (int i = 0; i < this.restrictions.size(); i++)
			{
				QueryRestriction restriction = this.restrictions.get(i);
				
				int p = i + 1;
				
				if (int.class.equals(restriction.type))
					pstmt.setInt(p, (Integer)restriction.getValue());
				else if (String.class.equals(restriction.type))
					pstmt.setString(p, (String)restriction.getValue());
				else if (Timestamp.class.equals(restriction.getType()))
					pstmt.setTimestamp(p, (Timestamp)restriction.getValue());
				else
					log.warning("Parameter type not implemented: " + restriction.getType());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.severe(e.getMessage());
		}
	}
	
	public void createProcessInstanceRestriction()
	{
		log.info("AD_PInstance_ID=" + this.AD_PInstance_ID);
		
		// We want to see all data created by the current process instance
		// The Query class add this restriction automatically, 
		// but only for tablenames that start with T_
		//
		// This will fail when the Query class excludes inactive process parameters
		//
		// The actual parameters are only used to initially construct the dataset.
		
		// Delete "selection only" parameters
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE ");
		sql.append(" FROM AD_PInstance_Para dip");
		sql.append(" WHERE dip.AD_PInstance_ID=?");
		sql.append(" AND dip.ParameterName IN");
		sql.append(" (");
		sql.append(" 	SELECT pp.ColumnName");
		sql.append(" 	FROM AD_PInstance_Para ip ");
		sql.append(" 	INNER JOIN AD_PInstance pi ON (pi.AD_PInstance_ID=ip.AD_PInstance_ID)");
		sql.append(" 	INNER JOIN AD_Process_Para pp ON (pp.AD_Process_ID=pi.AD_Process_ID)");
		sql.append(" 	WHERE pp.IsSelectionOnly='Y'");
		sql.append(" )");
		s_log.fine(sql.toString());
		int rows = DB.executeUpdate(sql.toString(), this.AD_PInstance_ID, this.trxName);
		s_log.info("Delete process only parameters: " + rows);
		
		// add AD_PInstance_ID restriction
		MPInstancePara pip = new MPInstancePara(this.ctx, this.AD_PInstance_ID, 99999);
		pip.setParameterName("AD_PInstance_ID");
		pip.setP_Number(this.AD_PInstance_ID);
		pip.save(this.trxName);
	}
	
	/************************************************************************/
	
	private static CLogger s_log = CLogger.getCLogger(ReportQuery.class);
	
	public static String EQUALS_OPERATOR = 	"=";
	
	public static String IN_OPERATOR = " IN ";
	
	public static String GREATER_EQUALS_OPERATOR = ">=";
	
	public static String LESSER_EQUALS_OPERATOR = "<=";
	
	public static String AND_OPERATOR = "AND";
	
}	
