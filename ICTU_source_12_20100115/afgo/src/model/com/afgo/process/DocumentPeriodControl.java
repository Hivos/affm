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
import java.sql.ResultSet;
import java.util.Iterator;

import org.compiere.model.MMessage;
import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;


import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOQuarter;
import com.afgo.model.ProgramPeriod;
import com.afgo.util.UserNotification;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DocumentPeriodControl.java,v 1.8.2.1 2010/01/06 11:45:33 tomassen Exp $
 *
 */
public class DocumentPeriodControl extends SvrProcess
{
	
	protected void prepare ()
	{
		this.ctx = super.getCtx();
		this.AD_Table_ID = super.getTable_ID();
		this.Record_ID = super.getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("IsNoticeOnly"))
				this.noticeOnly = "Y".equals(para[i].getParameter()) ? true : false;
			else if (name.equals("IsIgnorePendingDocuments"))
				this.ignorePendingDocuments = "Y".equals(para[i].getParameter()) ? true : false;
		}
	}
	
	private Ctx ctx = null;
	
	private int AD_Table_ID = -1;
	
	private int Record_ID = -1;
	
	private boolean noticeOnly = true;
	
	private boolean ignorePendingDocuments = false;
	

	protected String doIt () throws Exception
	{
		String tableName = MTable.getTableName(this.ctx, AD_Table_ID);
		
		ProgramPeriod period = null;
		
		if (MAFGOQuarter.Table_Name.equals(tableName))
			period = new MAFGOQuarter(this.ctx, this.Record_ID, this.get_TrxName());
		else if (MAFGOMonth.Table_Name.equals(tableName))
			period = new MAFGOMonth(this.ctx, this.Record_ID, this.get_TrxName());
		else 
			throw new IllegalStateException("Invalid period type: " + tableName);
		
		// check for any open documents
		log.fine("Notice Only: " + this.noticeOnly);
		log.fine("Ignore Pending Documents: " + this.ignorePendingDocuments);
		
		boolean noticesSend = this.sendNotices(period);
		
		if (this.noticeOnly)
		{
			if (noticesSend)
				return "@NoticesSend@";
			else
				return "@NoPendingDocuments@";
		}
		
		if (noticesSend && !this.ignorePendingDocuments)
			return "@PendingDocuments@";
		
		if ("Y".equals(period.getIsClosed()))
			period.setIsClosed("N");
		else
			period.setIsClosed("Y");
		
		if (period.getPO().save())
			return "@OK@";
		else
			return "@Failed@";
	}
	
	private boolean sendNotices(ProgramPeriod period)
	{
		boolean pendingDocuments = false;
		
		MMessage message = MMessage.get(this.getCtx(), "PeriodWillBeClosed"); 
		
		StringBuffer monthSQL = new StringBuffer();
		for (Iterator<MAFGOMonth> months = period.getMonths().iterator(); months.hasNext();)
		{
			if (monthSQL.length() > 0)
				monthSQL.append(",");
			monthSQL.append(months.next().getAFGO_Month_ID());
		}
		
		String sql = "SELECT dwfa.AD_Table_ID, dwfa.Record_ID, dwfa.DocumentNo, dt.Name, dwfa.CreatedBy, dwfa.AD_User_ID "
			+ " FROM AFGO_RV_DocLine_WFActivity dwfa"
			+ " INNER JOIN C_DocType dt ON (dt.C_DocType_ID=dwfa.C_DocType_ID)"
			+ " WHERE dwfa.AFGO_Month_ID IN (" + monthSQL.toString() + ")"
			+ " GROUP BY dwfa.AD_Table_ID, dwfa.Record_ID, dwfa.DocumentNo, dt.Name, dwfa.CreatedBy, dwfa.AD_User_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		log.fine(sql);
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				pendingDocuments = true;
				
				int AD_Table_ID = rs.getInt(1);
				int Record_ID = rs.getInt(2);
				String DocumentNo = rs.getString(3);
				String DocType = rs.getString(4);
				int CreatedBy = rs.getInt(5);
				int AD_User_ID = rs.getInt(6);
				
				String reference = "Period will be closed: " + period.getName(); // TODO Use AD_Message
				StringBuffer msg = new StringBuffer();
				msg.append(reference);
				msg.append("\nPlease check document: " + DocType + " " + DocumentNo); // TODO: Use AD_Message
			
				// notify workflow activity responsible
				UserNotification notice = new UserNotification(this.getCtx(), this.getAD_User_ID(), AD_User_ID, message, reference, msg.toString(), AD_Table_ID, Record_ID);
				notice.send();
				
				// notify document created
				if (CreatedBy != AD_User_ID)
				{
					notice = new UserNotification(this.getCtx(), this.getAD_User_ID(), CreatedBy, message, reference, msg.toString(), AD_Table_ID, Record_ID);
					notice.send();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.severe(e.getMessage());
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		
		return pendingDocuments;
	}

}
