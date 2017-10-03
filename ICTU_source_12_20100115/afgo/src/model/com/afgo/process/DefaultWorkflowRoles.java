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

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOWorkflowRoleAssignment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DefaultWorkflowRoles.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class DefaultWorkflowRoles extends SvrProcess
{
	private int AD_Org_ID = 0;
	
	private int AFGO_Program_ID = 0;
	
	private int AD_User_ID = 0;
	
	protected void prepare()
	{		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;		
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_User_ID"))
				this.AD_User_ID= para[i].getParameterAsInt();
			else
				log.severe("Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception
	{
		MAFGOProgram program = MAFGOProgram.getProgram(this.getCtx(), this.AFGO_Program_ID);
		
		String sql = "SELECT wfr.AFGO_WorkflowRole_ID"
			+ " FROM AFGO_WorkflowRole wfr"
			+ " WHERE wfr.AFGO_WorkflowRole_ID NOT IN"
			+ " ("
			+ " 	SELECT AFGO_WorkflowRole_ID"
			+ " 	FROM AFGO_WorkflowRoleAssignment"
			+ " 	WHERE AFGO_Program_ID=?"
			+ " )";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, program.getAFGO_Program_ID());
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				int AFGO_WorkflowRole_ID = rs.getInt(1);
				MAFGOWorkflowRoleAssignment roleAssignment = new MAFGOWorkflowRoleAssignment(this.getCtx(), 0, this.get_TrxName());
				roleAssignment.setAFGO_Program_ID(this.AFGO_Program_ID);
				roleAssignment.setAFGO_WorkflowRole_ID(AFGO_WorkflowRole_ID);
				roleAssignment.setAD_User_ID(this.AD_User_ID);
				roleAssignment.save();
			}
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		return null;
	}

}
