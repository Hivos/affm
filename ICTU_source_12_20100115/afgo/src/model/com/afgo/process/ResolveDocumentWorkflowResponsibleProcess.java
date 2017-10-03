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

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import com.afgo.model.AllocatableDocument;
import com.afgo.model.MAFGOWorkflowRole;
import com.afgo.model.MAFGOWorkflowRoleAssignment;
import com.afgo.model.WorkflowRoleAssignmentException;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ResolveDocumentWorkflowResponsibleProcess.java,v 1.9.2.1 2010/01/06 11:45:33 tomassen Exp $
 *
 */
public class ResolveDocumentWorkflowResponsibleProcess extends SvrProcess
{
	private int AD_Table_ID = -1;
	
	private String documentNo = null;
	
	private int AFGO_WorkflowRole_ID = -1;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Table_ID"))
				this.AD_Table_ID = para[i].getParameterAsInt();
			else if (name.equals("DocumentNo"))
				this.documentNo = (String)para[i].getParameter();
			else if (name.equals("AFGO_WorkflowRole_ID"))
				this.AFGO_WorkflowRole_ID = para[i].getParameterAsInt();
		}
		
	}

	protected String doIt () throws Exception
	{
		log.info("AD_Table_ID=" + this.AD_Table_ID + ", DocumentNo=" + this.documentNo + ", AFGO_WorkflowRole_ID=" + this.AFGO_WorkflowRole_ID);
		
		AllocatableDocument doc = null;
		
		String sql = "SELECT *"
			+ " FROM " + MTable.getTableName(this.getCtx(), this.AD_Table_ID)
			+ " WHERE AD_Client_ID=?"
			+ " AND AD_Org_ID=?"
			+ " AND DocumentNo=?";
		
		log.fine(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			Class<?> poClass = MTable.getClass(MTable.getTableName(this.getCtx(), this.AD_Table_ID));
			if (poClass == null)
			{
				log.warning("NO PO Class: AD_Table_ID=" + this.AD_Table_ID);
			}
			Constructor<?> cnst = poClass.getConstructor(new Class<?>[]{Ctx.class, ResultSet.class, String.class});
			
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAD_Client_ID());
			pstmt.setInt(2, this.getCtx().getAD_Org_ID());
			pstmt.setString(3, this.documentNo);
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				doc = (AllocatableDocument)cnst.newInstance(new Object[]{this.getCtx(), rs, this.get_TrxName()});
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
			if (rs != null) try{rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try{pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		if (doc == null)
			return "@NoDocument@";
		
		try
		{
			MAFGOWorkflowRoleAssignment workflowRoleAssignment = MAFGOWorkflowRoleAssignment.getWorkflowRoleAssignment(doc, null, MAFGOWorkflowRole.getWorkflowRole(this.getCtx(), this.AFGO_WorkflowRole_ID));
			return workflowRoleAssignment.toString();
		}
		catch(WorkflowRoleAssignmentException e)
		{
			return e.getMessage();
		}
	}

}
