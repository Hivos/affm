/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 ActFact B.V. 																			*
 * 																										*
 ********************************************************************************************************/
package com.actfact.exta.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.X_EXTA_MimeType;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MMimeType.java,v 1.5 2010/01/06 11:54:24 tomassen Exp $
 *
 */
public class MMimeType extends X_EXTA_MimeType
{

	public MMimeType (Ctx ctx, int EXTA_MimeType_ID, String trxName)
	{
		super(ctx, EXTA_MimeType_ID, trxName);
	}
	
	public MMimeType (Ctx ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String toString()
	{
		return this.getName();
	}
	
	private static CCache<Integer,MMimeType> s_cache = new CCache<Integer,MMimeType> ("AD_MimeType", 3);
	
	private static ArrayList<MMimeType> mimeTypes = null;
	
	private static CLogger s_log = CLogger.getCLogger(MMimeType.class);
	
	static
	{
		//s_cache.addVetoableChangeListener(new CacheManager());
	}
	
	public static MMimeType get(Ctx ctx, int AD_MimeType_ID)
	{
		MMimeType mimeType = s_cache.get(ctx, AD_MimeType_ID);
		
		if (mimeType == null && AD_MimeType_ID > 0)
		{
			mimeType = new MMimeType(ctx, AD_MimeType_ID, null);
			if (mimeType.getEXTA_MimeType_ID() == AD_MimeType_ID)
				s_cache.put(AD_MimeType_ID, mimeType);
		}
		
		return mimeType;
	}
	
	public static ArrayList<MMimeType> getMimeTypes(Ctx ctx)
	{
		if (MMimeType.mimeTypes == null)
		{
			MMimeType.mimeTypes = new ArrayList<MMimeType>();
			
			String sql = "SELECT *"
				+ " FROM EXTA_MimeType"
				+ " WHERE IsActive='Y'";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			MRole role = MRole.get(ctx, ctx.getAD_Role_ID());
			sql = role.addAccessSQL(sql, MMimeType.Table_Name, true, false);
			
			s_log.fine(sql);
			
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while(rs.next())
				{	
					MMimeType mimeType = new MMimeType(ctx, rs, null);
					mimeTypes.add(mimeType);
					s_cache.put(mimeType.getEXTA_MimeType_ID(), mimeType);
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
		
		}
		return MMimeType.mimeTypes;
	}
	
	static class CacheManager implements VetoableChangeListener
	{
		public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException 
		{
			MMimeType.mimeTypes = null;
		}
		
	}

}
