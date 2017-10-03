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
package com.afgo.callout;

import java.util.Date;

import org.compiere.model.*;
import org.compiere.util.*;

import org.compiere.util.Ctx;

import com.afgo.model.MAFGOActivity;
import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOPhase;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOProject;
import com.afgo.model.MAFGOProjectCluster;
import com.afgo.model.MAFGOQuarter;
import com.afgo.model.MAFGOYear;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CalloutAllocation.java,v 1.17.2.1 2010/01/06 11:45:30 tomassen Exp $
 *
 */
public class CalloutAllocation extends CalloutEngine 
{
	private CLogger log = CLogger.getCLogger(getClass());
	
	public String program (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Program_ID=" + value);
		
		Integer AFGO_ProjectCluster_ID = null;
		
		super.setCalloutActive(true);
		
		if (value != null)
		{
			int AFGO_Program_ID = (Integer)value;
			MAFGOProgram program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
			if (program == null || program.getAFGO_Program_ID() != AFGO_Program_ID)
				return "NoProgram";
			mTab.setValue("ProgramPeriodType", program.getProgramPeriodType());
			mTab.setValue("C_Currency_ID", program.getC_Currency_ID());
			
			if (program.isDefaultAllocation())
			{
				AFGO_ProjectCluster_ID = program.getAFGO_ProjectCluster_ID();
			}
				
		}
		
		mTab.setValue("AFGO_ProjectCluster_ID", AFGO_ProjectCluster_ID);
		
		super.setCalloutActive(false);
		
		return "";
	}
	
	public String projectCluster (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_ProjectCluster_ID=" + value);
		
		Integer AFGO_Project_ID = null;
		
		super.setCalloutActive(true);

		if (value != null)
		{
			int AFGO_ProjectCluster_ID = (Integer)value;
			MAFGOProjectCluster projectCluster = MAFGOProjectCluster.getProjectCluster(ctx, AFGO_ProjectCluster_ID);
			if (projectCluster == null || projectCluster.getAFGO_ProjectCluster_ID() != AFGO_ProjectCluster_ID)
				return "NoProjectCluster";
			
			if (projectCluster.getProgram().isDefaultAllocation())
			{
				AFGO_Project_ID = projectCluster.getAFGO_Project_ID();
			}
		}

		mTab.setValue("AFGO_Project_ID", AFGO_Project_ID);
		
		super.setCalloutActive(false);

		return "";
	}
	
	public String project (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Project_ID=" + value);
		
		Integer AFGO_Phase_ID = null;
		
		super.setCalloutActive(true);
	
		if (value != null)
		{
			int AFGO_Project_ID = (Integer)value;
			MAFGOProject project = MAFGOProject.getProject(ctx, AFGO_Project_ID);
			if (project == null || project.getAFGO_Project_ID() != AFGO_Project_ID)
				return "NoProject";
			
			if (project.getProjectCluster().getProgram().isDefaultAllocation())
			{
				AFGO_Phase_ID = project.getAFGO_Phase_ID();
			}
		}
		
		mTab.setValue("AFGO_Phase_ID", AFGO_Phase_ID);
		
		super.setCalloutActive(false);
		
		return "";
	}
	
	public String phase (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Phase_ID=" + value);
		
		Integer AFGO_Activity_ID = null;
		
		super.setCalloutActive(true);
		
		if (value != null)
		{
			int AFGO_Phase_ID = (Integer)value;
			MAFGOPhase phase = MAFGOPhase.getPhase(ctx, AFGO_Phase_ID);
			if (phase == null || phase.getAFGO_Phase_ID() != AFGO_Phase_ID)
				return "NoPhase";
			
			if (phase.getProject().getProjectCluster().getProgram().isDefaultAllocation())
			{
				AFGO_Activity_ID = phase.getAFGO_Activity_ID();
			}
		}
		
		mTab.setValue("AFGO_Activity_ID", AFGO_Activity_ID);
		
		super.setCalloutActive(false);
		
		return "";
	}
	
	public String activity (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.fine("AFGO_Activity_ID=" + value);

		Integer AFGO_ServiceType_ID = null;
		
		super.setCalloutActive(true);
		
		if (value != null)
		{
			int AFGO_Activity_ID = (Integer)value;
			MAFGOActivity activity = MAFGOActivity.getActivity(ctx, AFGO_Activity_ID);
			if (activity == null || activity.getAFGO_Activity_ID() != AFGO_Activity_ID)
				return "NoActivity";
			
			if (activity.getPhase().getProject().getProjectCluster().getProgram().isDefaultAllocation())
			{
				if (activity.getDefaultServiceType() != null)
					AFGO_ServiceType_ID = activity.getDefaultServiceType().getAFGO_ServiceType_ID();
			}
		}
		
		mTab.setValue("AFGO_ServiceType_ID", AFGO_ServiceType_ID);
		
		super.setCalloutActive(false);
		
		return "";
	}
	
	/*
	 * Will set quarter and month.
	 * Quarter and month fields may be hidden.
	 */
	public String year (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer AFGO_Year_ID = (Integer)value;
		
		super.setCalloutActive(true);
		
		if (AFGO_Year_ID != null && AFGO_Year_ID > 0)
		{
			MAFGOYear year = MAFGOYear.getYear(ctx, AFGO_Year_ID);
			if (year == null || year.getAFGO_Year_ID() != AFGO_Year_ID)
			{
				super.setCalloutActive(false);
				return "Invalid AFGO_Year_ID: " + AFGO_Year_ID;
			}
			
			if (year.getCalendar().isAllowStandardQuarter())
			{
				MAFGOQuarter quarter = MAFGOQuarter.getQuarter(ctx, year.getAFGO_Quarter_ID());
				mTab.setValue("AFGO_Quarter_ID", quarter.getAFGO_Quarter_ID());
				
				if (year.getCalendar().isAllowStandardMonth())
				{
					MAFGOMonth month = MAFGOMonth.getMonth(ctx, quarter.getAFGO_Month_ID());
					mTab.setValue("AFGO_Month_ID", month.getAFGO_Month_ID());
				}
			}
		
		}
		
		super.setCalloutActive(false);
		return "";
	}
	
	public String quarter (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		int AFGO_Program_ID = ctx.getContextAsInt(WindowNo, "AFGO_Program_ID");
		MAFGOProgram program = MAFGOProgram.getProgram(ctx, AFGO_Program_ID);
		
		String programPeriodType = ctx.getContext(WindowNo, "ProgramPeriodType");
		
		if (program != null && !program.getProgramPeriodType().equals(programPeriodType))
			log.warning("ProgramPeriodTypeMismatch: AFGO_Program_ID=" + program.getAFGO_Program_ID() + ", Program=" + program.getProgramPeriodType() + ", Context=" + programPeriodType);
		
		log.fine("AFGO_Quarter_ID=" + value + ", ProgramPeriodType=" + programPeriodType);
		
		if (super.isCalloutActive())
			return "";
				
		super.setCalloutActive(true);
		
		MAFGOQuarter quarter = null;
		MAFGOMonth month = null;
		
		// derived from DateAcct
		if (value == null)
		{
			java.sql.Date dateAcct = null;
			if (ctx.getContextAsTime(WindowNo, "DateAcct") > 0)
				dateAcct = new java.sql.Date(ctx.getContextAsTime(WindowNo, "DateAcct"));
			
			if (dateAcct != null && program != null)
			{
				month = program.getMonth(dateAcct, false);
				if (month == null)
				{
					super.setCalloutActive(false);
					return "NoPeriod: " + dateAcct;
				}
				else if (!month.isPeriodOpen())
				{
					super.setCalloutActive(false);
					return "PeriodClosed: " + dateAcct;
				}
				mTab.setValue("AFGO_Quarter_ID", month.getAFGO_Quarter_ID());
				mTab.setValue("AFGO_Month_ID", month.getAFGO_Month_ID());
			}
			
		}
		// value selected by user
		else	
		{
			quarter = MAFGOQuarter.getQuarter(ctx, (Integer)value);
			
			if (MAFGOProgram.PROGRAMPERIODTYPE_Quarter.equals(programPeriodType) || quarter.getYear().getCalendar().isAllowStandardMonth())
			{
				month = quarter.getMonth();
				mTab.setValue("AFGO_Month_ID", month != null ? month.getAFGO_Month_ID() : null);
			}
		}
		
		super.setCalloutActive(false);
		
		return "";
	}
	
}
