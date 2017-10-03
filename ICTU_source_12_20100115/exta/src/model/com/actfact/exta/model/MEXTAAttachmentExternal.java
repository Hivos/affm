/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 ActFact B.V. 																			*
 * 																										*
 ********************************************************************************************************/
package com.actfact.exta.model;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.compiere.model.GridTab;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.X_EXTA_AttachmentExternal;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MEXTAAttachmentExternal.java,v 1.7 2010/01/06 11:54:24 tomassen Exp $
 *
 */
public class MEXTAAttachmentExternal extends X_EXTA_AttachmentExternal
{

	public MEXTAAttachmentExternal (Ctx ctx, int EXTA_AttachmentExternal_ID, String trxName)
	{
		super(ctx, EXTA_AttachmentExternal_ID, trxName);
	}
	
	public MEXTAAttachmentExternal (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private String errorMsg = null;
	
	public MMimeType getMimeType()
	{
		return MMimeType.get(this.getCtx(), this.getEXTA_MimeType_ID());
	}
	
	public String getErrorMsg()
	{
		return this.errorMsg;
	}
	
	public void setURL(String url)
	{
		super.setURL(url);
		log.fine("URL=" + url);
	}
	
	public boolean isAbsolutePath()
	{
		if (this.getURL() == null || "".equals(this.getURL()))
			return false;
		
		log.fine("URL=" + this.getURL());
		
		// UNIX root
		if (this.getURL().startsWith("/"))
			return true;
		
		// Protocol Windows drive letter 
		if (this.getURL().indexOf("://") > -1)
			return true;
		
		// Windows drive letter 
		if (this.getURL().indexOf(":/") > -1)
			return true;
		
		return false;
	}
	
	public String getPathOffset()
	{
		String offset = MClient.get(this.getCtx()).getDocumentDir();
		
		if (offset == null || "".equals(offset))
			offset = System.getProperty("user.dir");
		
		return offset;
	}
	
	public String getAbsolutePath()
	{
		String path = this.getURL();
		
		if (path == null || "".equals(path))
			return path;
		
		if (!this.isAbsolutePath())
		{
			String offset = this.getPathOffset();
			if (!offset.endsWith("/") && !path.startsWith("/"))
				offset = offset + "/";
			path = offset + path;
		}
		
		return path;
	}
	
	public void setAbsolutePath(String url)
	{
		String offset = MClient.get(this.getCtx()).getDocumentDir();
		
		url = url.replace("\\", "/");

		if (offset != null)
		{
			if (url.toUpperCase().startsWith(offset.toUpperCase()))
			{
				url = url.substring(offset.length());
				if (url.startsWith("/"))
					url = url.substring(1);
			}
		}

		super.setURL(url);
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (!this.isActive())
			return true;
		
		String urlString = this.getAbsolutePath();
		if (urlString.indexOf("://") < 0)
			urlString = "file:////" + urlString;
		
		log.fine("URL=" + urlString);
		try
		{
			URL url = new URL(urlString);
			url.getContent();
		}
		catch(Exception e)
		{
			this.errorMsg = e.getMessage();
			//return false;
		}
		
		return true;
	}
	
	// ************************************************************
	
	//private static final transient String DELETE_PROCESS = "EXTA_DeleteAtttachment";
	
	private static boolean tableExists = false;
	
	private static HashMap<Integer,Boolean> roleCanDelete = new HashMap<Integer,Boolean>();
	
	private static HashMap<Integer,Integer> baseTables = new HashMap<Integer,Integer>();
	
	private static CLogger s_log = CLogger.getCLogger(MEXTAAttachmentExternal.class);
	
	public static int getBaseAD_Table_ID(Ctx ctx, GridTab tab)
	{
		Integer AD_Table_ID = baseTables.get(tab.getAD_Table_ID());
		
		if (AD_Table_ID == null)
		{
    		MTable table = MTable.get(ctx, tab.getAD_Table_ID());
    		
    		// look for base table in case of view
    		if (table.isView())
    		{
        		// explicit AD_Table_ID as part of view
    			MColumn[] cols = table.getColumns(false);
        		for (int c = 0; c < cols.length; c++)
        		{
        			if ("AD_Table_ID".equalsIgnoreCase(cols[c].getColumnName()))
        			{
        				AD_Table_ID = (Integer)tab.getValue("AD_Table_ID");
        				break;
        			}
        		}
        		
        		// derive base table from PK
    			if (AD_Table_ID == null && tab.getKeyColumnName().endsWith("_ID"))
    			{
    				String baseTableName = tab.getKeyColumnName().substring(0, tab.getKeyColumnName().length()-3);
    				
    				if (!table.getTableName().equalsIgnoreCase(baseTableName))
    				{
    					AD_Table_ID  = MTable.get_Table_ID(baseTableName);
    				}
    			}

    		}
    		
    		if (AD_Table_ID == null)
    		{
    			AD_Table_ID = tab.getAD_Table_ID();
    		}
		
    		baseTables.put(tab.getAD_Table_ID(), AD_Table_ID);
		}
		
		s_log.fine("base table: " + tab.getAD_Table_ID() + " -> " + AD_Table_ID);
		
		return AD_Table_ID;
	}
	
	public static boolean hasAttachment(Ctx ctx, GridTab tab)
	{
		return hasAttachment(getBaseAD_Table_ID(ctx, tab), tab.getRecord_ID());
	}
	
	public static boolean hasAttachment(int AD_Table_ID, int Record_ID)
	{
		if (!tableExists)
			return false;
		
		String sql = "SELECT COUNT(*)"
			+ " FROM EXTA_AttachmentExternal"
			+ " WHERE AD_Table_ID=?"
			+ " AND Record_ID=?"
			+ " AND IsActive='Y'";
		
		int count = DB.getSQLValue(null, sql, AD_Table_ID, Record_ID);
		
		s_log.finer("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID + " => " + count);
		
		return (count > 0);
	}
	
	public static ArrayList<MEXTAAttachmentExternal> getAttachments(Ctx ctx, int AD_Table_ID, int Record_ID, boolean onlyActive, String trxName)
	{
		ArrayList<MEXTAAttachmentExternal> attachments = new ArrayList<MEXTAAttachmentExternal>();
		
		s_log.fine("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID);
		
		String sql  = "SELECT *"
			+ " FROM EXTA_AttachmentExternal ae"
			+ " WHERE ae.AD_Table_ID=?"
			+ " AND ae.Record_ID=?";
		
		if (onlyActive)
			sql = sql + " ae.AND IsActive='Y'";
			
		sql = sql + " ORDER BY ae.Created";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, Record_ID);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				attachments.add(new MEXTAAttachmentExternal(ctx, rs, trxName));
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
		
		return attachments;
	}
	
	static
	{
		MTable table = MTable.get(Env.getCtx(), "EXTA_AttachmentExternal");
		
		if (table != null)
		{
			s_log.info("ExternalAttachment: enabled");
			tableExists = true;
		}
		else
		{
			s_log.info("ExternalAttachment: disabled");
		}
	}

}
