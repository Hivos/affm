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

import org.compiere.framework.POInfo;
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MClientInfo;
import org.compiere.model.MOrgInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import com.afgo.model.MAFGOAccountRestriction;
import com.afgo.model.MAFGOActivity;
import com.afgo.model.MAFGOCalendar;
import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOPhase;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOProject;
import com.afgo.model.MAFGOProjectCluster;
import com.afgo.model.MAFGOQuarter;
import com.afgo.model.MAFGORevenueExpense;
import com.afgo.model.MAFGOServiceType;
import com.afgo.model.MAFGOYear;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: RevenueExpenseReport.java,v 1.7.2.1 2010/01/06 11:45:33 tomassen Exp $
 *
 */
public class RevenueExpenseReport extends SvrProcess
{
	private int AFGO_AccountRestriction_ID = -1;
	
	private int AFGO_Calendar_ID = -1;
	
	private int AD_Org_ID = -1;
	
	private int AFGO_Program_ID = -1;
	
	private int AFGO_ProjectCluster_ID = -1;
	
	private int AFGO_Project_ID = -1;
	
	private int AFGO_Phase_ID = -1;
	
	private int AFGO_Activity_ID = -1;
	
	private int AFGO_ServiceType_ID = -1;
	
	private int AFGO_Year_ID = -1;
	
	private int AFGO_Quarter_ID = -1;
	
	private int AFGO_Month_ID = -1;
	
	private int C_Charge_ID = -1;
	
	private int C_BPartner_ID = -1;
	
	private Timestamp from = null;
	
	private Timestamp to = null;
	
	private String VerifyPeriod = null;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AFGO_AccountRestriction_ID"))
				this.AFGO_AccountRestriction_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Calendar_ID"))
				this.AFGO_Calendar_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				this.AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Program_ID"))
				this.AFGO_Program_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_ProjectCluster_ID"))
				this.AFGO_ProjectCluster_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Project_ID"))
				this.AFGO_Project_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Phase_ID"))
				this.AFGO_Phase_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Activity_ID"))
				this.AFGO_Activity_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_ServiceType_ID"))
				this.AFGO_ServiceType_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Year_ID"))
				this.AFGO_Year_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Quarter_ID"))
				this.AFGO_Quarter_ID = para[i].getParameterAsInt();
			else if (name.equals("AFGO_Month_ID"))
				this.AFGO_Month_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Charge_ID"))
				this.C_Charge_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				this.C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("DateAcct"))
			{
				if (para[i].getParameter() != null)
					from = (Timestamp)para[i].getParameter();
				if (para[i].getParameter_To() != null)
					to = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("VerifyPeriod"))
				this.VerifyPeriod = (String)para[i].getParameter();
		}
		
	}
	
	protected String doIt()
	{
		// explicit calendar from parameter
		int AFGO_Calendar_ID = this.AFGO_Calendar_ID;
		
		// calendar from  program
		MAFGOProgram program = null;
		if (this.AFGO_Program_ID > 0)
		{
			program = MAFGOProgram.getProgram(this.getCtx(), this.AFGO_Program_ID);
			AFGO_Calendar_ID = program.getAFGO_Calendar_ID();
		}
		
		// calendar from org
		if (AFGO_Calendar_ID < 1 && this.AD_Org_ID > 0)
		{
			MOrgInfo orgInfo = MOrgInfo.get(this.getCtx(), this.AD_Org_ID, this.get_TrxName());
			AFGO_Calendar_ID = orgInfo.get_ValueAsInt("AFGO_Calendar_ID");
		}
		
		// calendar from client
		if (AFGO_Calendar_ID < 1)
		{
			MClientInfo clientInfo = MClientInfo.get(this.getCtx());
			AFGO_Calendar_ID = clientInfo.get_ValueAsInt("AFGO_Calendar_ID");
		}
		if (AFGO_Calendar_ID < 1)
		{
			throw new IllegalStateException("No AFGO_Calendar_ID");
		}
		
		MAFGOCalendar calendar = MAFGOCalendar.getCalendar(this.getCtx(), AFGO_Calendar_ID);

		
		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO " + MAFGORevenueExpense.Table_Name + " (AD_PInstance_ID");
		
		StringBuffer select = new StringBuffer();
		select.append("(SELECT " + this.getAD_PInstance_ID() + " AS AD_PInstance_ID");
		
		StringBuffer where = new StringBuffer();
		where.append(" WHERE 1=1 ");
		
		// Copy all columns from AFGO_RV_DocLineFact to AFGO_T_RevenueExpense
		POInfo poi = POInfo.getPOInfo(this.getCtx(), MAFGORevenueExpense.Table_ID);
		for (int i = 0; i < poi.getColumnCount(); i++)
		{
			if ("AD_PInstance_ID".equals(poi.getColumnName(i)))
				continue;
			insert.append("," + poi.getColumnName(i));
			select.append(",f." + poi.getColumnName(i));
		}
		
		insert.append(")");
		
		ReportQuery query = new ReportQuery(this.getCtx(), this.getAD_PInstance_ID(), this.get_TrxName());
		
		select.append(" FROM AFGO_RV_DocLineFact f");
		
		// Only specified AccountTypes
		if (this.AFGO_AccountRestriction_ID > 0 && MAFGOAccountRestriction.getSQL(this.getCtx(), AFGO_AccountRestriction_ID) != null)
		{
			select.append(" INNER JOIN C_ElementValue ev ON (ev.C_ElementValue_ID=f.Account_ID)");
		}
		
		query.addRestriction("f.AD_Client_ID", ReportQuery.EQUALS_OPERATOR, int.class, this.getAD_Client_ID());
		
		if (this.AD_Org_ID > 0)
			query.addRestriction("f.AD_Org_ID", ReportQuery.EQUALS_OPERATOR, int.class, this.AD_Org_ID);
		
		if (program != null)
		{
			if (program.getAFGO_Calendar_ID() != AFGO_Calendar_ID)
				throw new IllegalStateException("Invalid: AFGO_Calendar_ID=" + AFGO_Calendar_ID + ", AFGO_Program_ID=" + program.getAFGO_Program_ID());
			if (program.getAFGO_Program_ID() != this.AFGO_Program_ID || program.getAD_Org_ID() != this.AD_Org_ID)
				throw new IllegalStateException("Invalid AFGO_Program_ID: " + this.AFGO_Program_ID);
			query.addRestriction("f.AFGO_Program_ID", ReportQuery.EQUALS_OPERATOR, int.class, program.getAFGO_Program_ID());
		}
		//else
		//	throw new IllegalStateException("No AFGO_Program_ID");
		
		// Only DateAcct outside of document period
		if (this.VerifyPeriod != null)
		{
			String PeriodType = VerifyPeriod;
			
			if ("P".equals(PeriodType))
				PeriodType = program.getProgramPeriodType();
			
			if ("Y".equals(PeriodType))
				select.append(" INNER JOIN AFGO_Year p ON (p.AFGO_Year_ID=f.AFGO_Year_ID)");
			else if ("Q".equals(PeriodType))
				select.append(" INNER JOIN AFGO_Quarter p ON (p.AFGO_Quarter_ID=f.AFGO_Quarter_ID)");
			else if ("M".equals(PeriodType))
				select.append(" INNER JOIN AFGO_Month p ON (p.AFGO_Month_ID=f.AFGO_Month_ID)");
			
			where.append(" AND (f.DateAcct < p.StartDate OR f.DateAcct > p.EndDate)");
		}
		
		MAFGOProjectCluster projectCluster = null;
		if (this.AFGO_ProjectCluster_ID > 0 && program != null)
		{
			projectCluster = MAFGOProjectCluster.getProjectCluster(this.getCtx(), AFGO_ProjectCluster_ID);
			if (projectCluster.getAFGO_ProjectCluster_ID() != this.AFGO_ProjectCluster_ID || program.getAFGO_Program_ID() != projectCluster.getAFGO_Program_ID())
				throw new IllegalStateException("Invalid AFGO_ProjectCluster_ID: " + this.AFGO_ProjectCluster_ID);
			query.addRestriction("f.AFGO_ProjectCluster_ID", ReportQuery.EQUALS_OPERATOR, int.class, projectCluster.getAFGO_ProjectCluster_ID());
		}
		
		MAFGOProject project = null;
		if (this.AFGO_Project_ID > 0 && projectCluster != null)
		{
			project = MAFGOProject.getProject(this.getCtx(), this.AFGO_Project_ID);
			if (project.getAFGO_Project_ID() != this.AFGO_Project_ID || projectCluster.getAFGO_ProjectCluster_ID() != project.getAFGO_ProjectCluster_ID())
				throw new IllegalStateException("Invalid AFGO_Project_ID: " + this.AFGO_Project_ID);
			query.addRestriction("f.AFGO_Project_ID", ReportQuery.EQUALS_OPERATOR, int.class, project.getAFGO_Project_ID());
		}
		
		MAFGOPhase phase = null;
		if (this.AFGO_Phase_ID > 0 && project != null)
		{
			phase = MAFGOPhase.getPhase(this.getCtx(), this.AFGO_Phase_ID);
			if (phase.getAFGO_Phase_ID() != this.AFGO_Phase_ID || project.getAFGO_Project_ID() != phase.getAFGO_Project_ID())
				throw new IllegalStateException("Invalid AFGO_Phase_ID: " + this.AFGO_Phase_ID);
			query.addRestriction("f.AFGO_Phase_ID", ReportQuery.EQUALS_OPERATOR, int.class, phase.getAFGO_Phase_ID());
		}
		
		MAFGOActivity activity = null;
		if (this.AFGO_Activity_ID > 0)
		{
			activity = MAFGOActivity.getActivity(this.getCtx(), this.AFGO_Activity_ID);
			if (activity.getAFGO_Activity_ID() != this.AFGO_Activity_ID || phase == null || phase.getAFGO_Phase_ID() != activity.getAFGO_Activity_ID())
				throw new IllegalStateException("Invalid AFGO_Activity_ID: " + this.AFGO_Activity_ID);
			query.addRestriction("f.AFGO_Activity_ID", ReportQuery.EQUALS_OPERATOR, int.class, activity.getAFGO_Activity_ID());
		}
		
		MAFGOServiceType serviceType = null;
		if (this.AFGO_ServiceType_ID > 0)
		{
			serviceType = MAFGOServiceType.getServiceType(this.getCtx(), this.AFGO_ServiceType_ID);
			if (serviceType.getAFGO_ServiceType_ID() != this.AFGO_ServiceType_ID)
				throw new IllegalStateException("Invalid AFGO_ServiceType_ID: " + this.AFGO_ServiceType_ID);
			query.addRestriction("f.AFGO_ServiceType_ID", ReportQuery.EQUALS_OPERATOR, int.class, serviceType.getAFGO_ServiceType_ID());
		}
		
		MAFGOYear year = null;
		if (this.AFGO_Year_ID > 0)
		{
			year = MAFGOYear.getYear(this.getCtx(), this.AFGO_Year_ID);
			if (year.getAFGO_Year_ID() != this.AFGO_Year_ID || year.getAFGO_Calendar_ID() != calendar.getAFGO_Calendar_ID())
				throw new IllegalStateException("Invalid AFGO_Year_ID: " + this.AFGO_Year_ID);
			query.addRestriction("f.AFGO_Year_ID", ReportQuery.EQUALS_OPERATOR, int.class, year.getAFGO_Year_ID());
		}
		
		MAFGOQuarter quarter = null;
		if (this.AFGO_Quarter_ID > 0 && year != null)
		{
			quarter = MAFGOQuarter.getQuarter(this.getCtx(), this.AFGO_Quarter_ID);
			if (quarter.getAFGO_Quarter_ID() != this.AFGO_Quarter_ID || year.getAFGO_Year_ID() != quarter.getAFGO_Year_ID())
				throw new IllegalStateException("Invalid AFGO_Quarter_ID: " + this.AFGO_Quarter_ID);
			query.addRestriction("f.AFGO_Quarter_ID", ReportQuery.EQUALS_OPERATOR, int.class,quarter.getAFGO_Quarter_ID());
		}
		
		MAFGOMonth month = null;
		if (this.AFGO_Month_ID > 0 && quarter != null)
		{
			month = MAFGOMonth.getMonth(this.getCtx(), this.AFGO_Month_ID);
			if (month.getAFGO_Month_ID() != this.AFGO_Month_ID || quarter.getAFGO_Quarter_ID() != month.getAFGO_Quarter_ID())
				throw new IllegalStateException("Invalid AFGO_Month_ID: " + this.AFGO_Month_ID);
			query.addRestriction("f.AFGO_Month_ID", ReportQuery.EQUALS_OPERATOR, int.class, month.getAFGO_Month_ID());
		}
		
		MCharge charge = null;
		if (this.C_Charge_ID > 0)
		{
			charge = new MCharge(this.getCtx(), this.C_Charge_ID, this.get_TrxName());
			if (charge.getC_Charge_ID() != this.C_Charge_ID)
				throw new IllegalStateException("Invalid C_Charge_ID: " + this.C_Charge_ID);
			query.addRestriction("f.C_Charge_ID", ReportQuery.EQUALS_OPERATOR, int.class, charge.getC_Charge_ID());
		}
		
		MBPartner bp = null;
		if (this.C_BPartner_ID > 0)
		{
			bp = new MBPartner(this.getCtx(), this.C_BPartner_ID, this.get_TrxName());
			if (bp.getC_BPartner_ID() != this.C_BPartner_ID)
				throw new IllegalStateException("Invalid C_BPartner_ID: " + this.C_BPartner_ID);
			query.addRestriction("f.C_BPartner_ID", ReportQuery.EQUALS_OPERATOR, int.class, bp.getC_BPartner_ID());
		}
		
		if (this.from != null)
			query.addRestriction("f.DateAcct", ReportQuery.GREATER_EQUALS_OPERATOR, Timestamp.class, this.from);
		
		if (this.to != null)
			query.addRestriction("f.DateAcct", ReportQuery.LESSER_EQUALS_OPERATOR, Timestamp.class, this.to);
	
		
		StringBuffer sql = new StringBuffer();
		sql.append(insert);
		sql.append(select);
		sql.append(where.toString());
		sql.append(" AND " + query.getWhereClause());
		if (this.AFGO_AccountRestriction_ID > 0)
			sql.append(" AND ev.AccountType IN " + MAFGOAccountRestriction.getSQL(this.getCtx(), this.AFGO_AccountRestriction_ID));
		sql.append(")");
		
		log.info(sql.toString());
		
		PreparedStatement pstmt = null;
		int rows = -1;
		
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), this.get_TrxName());
			query.fillParameters(pstmt);
			rows = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		query.createProcessInstanceRestriction();

		
		return "@Count@=" + rows;
	}
}
