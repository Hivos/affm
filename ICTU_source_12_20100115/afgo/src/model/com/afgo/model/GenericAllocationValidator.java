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

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.framework.POInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: GenericAllocationValidator.java,v 1.10.2.1 2010/01/06 11:45:28 tomassen Exp $
 * 
 * Verify valid combinations:
 * 
 * Program->ProjectCluster->Project->Phase->Activity
 * Calendar->Year->Quarter->Month
 * Program->Service Type
 *
 */
public class GenericAllocationValidator implements ModelValidator
{
	public GenericAllocationValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("GenericAllocationValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		// All tables with AFGO Allocation columns
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		String sql = "SELECT DISTINCT c.AD_Table_ID FROM AD_Column c WHERE ColumnName LIKE 'AFGO_%_ID'";
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				POInfo poinfo = POInfo.getPOInfo(Env.getCtx(), rs.getInt(1));
				log.fine("Adding table: " + poinfo.getTableName());
				engine.addModelChange(poinfo.getTableName(), this);
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
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	

	public String modelChange (PO po, int type) throws Exception
	{
		String result = null;
		
		POInfo poinfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID());
		
		// TODO: set default allocation values if not specified
		// (standard quarter when not set by callout, i.e. after document is processed)
		
		if ((MAFGOProgram.Table_Name.equals(poinfo.getTableName())
				|| MAFGOProjectCluster.Table_Name.equals(poinfo.getTableName())
				|| MAFGOProject.Table_Name.equals(poinfo.getTableName())
				|| MAFGOPhase.Table_Name.equals(poinfo.getTableName())
				|| MAFGOActivity.Table_Name.equals(poinfo.getTableName()))
				|| MAFGOWorkflowRoleAssignment.Table_Name.equals(poinfo.getTableName()))
		{
			log.fine("Skip allocation validation: " + poinfo.getTableName());
		}
		else
		{
			log.fine("Validate Allocation: " + po);
			// Validate AD_Org / AFGO_Program combination
			if (result == null && !poinfo.getTableName().equals(MAFGOProgram.Table_Name) && poinfo.getColumnIndex("AD_Org_ID") > -1 && poinfo.getColumnIndex("AFGO_Program_ID") > -1)
				result = MAFGOProgram.validateProgramOrganization(po);
				
			// Validate AFGO_Program->AFGO_ProjectCluster->AFGO_Program->AFGO_Phase->AFGO_Activity
			if (result == null && poinfo.getColumnIndex("AFGO_ProjectCluster_ID") > -1 && poinfo.getColumnIndex("AFGO_Project_ID") > -1 && poinfo.getColumnIndex("AFGO_Phase_ID") > -1 && poinfo.getColumnIndex("AFGO_Activity_ID") > -1)
				result = MAFGOProgram.validateAllocation(po);
			
			// Validate AFGO_ServiceType
			if (result == null && !poinfo.getTableName().equals(MAFGOServiceType.Table_Name) && poinfo.getColumnIndex("AFGO_ServiceType_ID") > -1)
				result = MAFGOServiceType.validateServiceType(po);
			
			// Validate AFGO_Year->AFGO_Quarter->AFGO_Month
			if (result == null && !poinfo.getTableName().equals(MAFGOQuarter.Table_Name) && !poinfo.getTableName().equals(MAFGOMonth.Table_Name) && !poinfo.getTableName().equals(MAFGOFundSchedule.Table_Name) && poinfo.getColumnIndex("AFGO_Quarter_ID") > -1 && poinfo.getColumnIndex("AFGO_Month_ID") > -1)
				result = MAFGOCalendar.validatePeriod(po);
			
			// Validate AFGO_LotItem->[M_Product | C_Charge]
			if (result == null && !poinfo.getTableName().equals(MAFGOLotItem.Table_Name) && poinfo.getColumnIndex("AFGO_LotItem_ID") > -1 && poinfo.getColumnIndex("M_Product_ID") > -1 && poinfo.getColumnIndex("C_Charge_ID") > -1)
				result = MAFGOLotItem.validateLotItemProductCharge(po);
		}
		
		if (result == null && (po instanceof AllocatableDocument || po instanceof AllocatableDocumentLine))
			;
		
		return result;
	}
	
	public String docValidate (PO doc, int timing)
	{
		return null;
	}


	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}


}
