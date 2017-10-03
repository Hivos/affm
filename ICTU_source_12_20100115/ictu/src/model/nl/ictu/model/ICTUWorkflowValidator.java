/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 Stichting ICTU 																		*
 * 																										*
 ********************************************************************************************************/
package nl.ictu.model;

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
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ICTUWorkflowValidator.java,v 1.4.2.1 2010/01/06 11:58:18 tomassen Exp $
 * 
 * Clear all "Approvals" during prepareIt WF action
 *
 */
public class ICTUWorkflowValidator implements ModelValidator
{
	public ICTUWorkflowValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private HashMap<String,HashSet<String>> approvalColumns = new HashMap<String,HashSet<String>>();
	
	private CLogger log = CLogger.getCLogger(getClass());

	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("ICTUWorkflowValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		String sql = "SELECT t.TableName, c.ColumnName"
			 + " FROM AD_Table t"
			 + " INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)"
			 + " WHERE c.ColumnName LIKE '%Approved%'" 
			 + " AND ColumnName NOT IN ('IsApproved', 'IsCreditApproved')"
			 + " AND c.EntityType=?"
			 + " ORDER BY t.TableName, c.ColumnName";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, ICTUModelValidator.EntityType);
			rs = pstmt.executeQuery();
			
			String tableName = null;
			HashSet<String> cols = null;
			
			while(rs.next())
			{
				if (tableName == null || !tableName.equals(rs.getString(1)))
				{
					if (cols != null && tableName != null)
					{
						this.approvalColumns.put(tableName, cols);
						
						String info = tableName + "{";
						int i = 0;
						for (Iterator<String> iCols = cols.iterator(); iCols.hasNext();)
						{
							if (i > 0)
								info = info + ",";
							info = info + iCols.next();
							i++;
						}
						info = info + "}";
						
						log.fine(info);
					}
					
					tableName = rs.getString(1);
					this.engine.addDocValidate(tableName, this);
					cols = new HashSet<String>();
				}
				
				cols.add(rs.getString(2));
			}
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
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String modelChange(PO po, int typ) throws Exception
	{
		return null;
	}
	
	public String docValidate(PO doc, int timing)
	{
		if (doc != null && ModelValidator.DOCTIMING_BEFORE_PREPARE == timing)
		{
			log.info("clear document approvals: " + doc.toString());
			HashSet<String> cols = this.approvalColumns.get(doc.get_TableName());
			if (cols == null)
				return null;
			for (Iterator<String> iCols = cols.iterator(); iCols.hasNext();)
			{
				String columnName = iCols.next();
				log.fine("clear approval: " + doc.toString() + " " + columnName);
				doc.set_Value(columnName, false);
			}
		}
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
