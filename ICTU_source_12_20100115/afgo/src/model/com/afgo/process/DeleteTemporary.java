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

import org.compiere.framework.POInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DeleteTemporary.java,v 1.3.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class DeleteTemporary extends SvrProcess
{

	private int AD_Table_ID = -1;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("AD_Table_ID"))
				this.AD_Table_ID = para[i].getParameterAsInt();
		}
		
	}
	
	protected String doIt() throws Exception
	{
		if (this.AD_Table_ID < 1)
			throw new IllegalStateException("No Table");
		
		POInfo poinfo = POInfo.getPOInfo(this.getCtx(), this.AD_Table_ID);
		
		if (poinfo == null)
			throw new IllegalStateException("No such table: AD_Table_ID=" + this.AD_Table_ID);
		
		if (poinfo.getTableName().indexOf("_T_") < 0)
			throw new IllegalStateException("Not a temporary table: AD_Table_ID=" + this.AD_Table_ID);
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(poinfo.getTableName());
		if (this.getAD_Client_ID() > 0)
			sql.append(" WHERE AD_Client_ID=" + this.getAD_Client_ID());
		
		int deleted = DB.executeUpdate(sql.toString(), this.get_TrxName());
		
		
		return "@Deleted@=" + deleted;
	}

}
