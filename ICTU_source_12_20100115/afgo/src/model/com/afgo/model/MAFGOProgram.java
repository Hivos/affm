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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Ctx;
import org.compiere.util.Msg;

import org.compiere.framework.PO;
import org.compiere.framework.POInfo;
import org.compiere.model.MBPartner;
import org.compiere.model.MClientInfo;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MUser;
import org.compiere.util.DB;


import compiere.model.X_AFGO_Program;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: MAFGOProgram.java,v 1.16.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class MAFGOProgram extends X_AFGO_Program implements AllocationLevel
{
	public MAFGOProgram(Ctx ctx, String trxName)
	{
		this(ctx, 0, trxName);
	}

	public MAFGOProgram(Ctx ctx, int AFGO_Program_ID, String trxName) 
	{
		super(ctx, AFGO_Program_ID, trxName);
	}
	
	public MAFGOProgram(Ctx ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs, trxName);
		
	}
	
	private MAFGOProjectCluster projectCluster = null;
	
	private MUser programManager = null;
	
	private MBPartner bpartner;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public MUser getProgramManager()
	{
		if (this.programManager == null)
			this.programManager = new MUser(this.getCtx(), this.getProgramManager_ID(), this.get_TrxName());
		return this.programManager;
	}
	
	public MBPartner getBPartner()
	{
		if (this.bpartner == null)
		{
			if (this.getC_BPartner_ID() > 0)
				this.bpartner = new MBPartner(this.getCtx(), this.getC_BPartner_ID(), this.get_TrxName());
			else if (this.getProgramManager().getC_BPartner_ID() > 0)
				this.bpartner = new MBPartner(this.getCtx(), this.getProgramManager().getC_BPartner_ID(), this.get_TrxName());
		}
		return this.bpartner;
	}
	
	public int getLevel()
	{
		return LEVEL;
	}
	
	public MAFGOProgram getParent()
	{
		return null;
	}
	
	public MAFGOProjectCluster getDefaultChild()
	{
		return this.getProjectCluster();
	}
	
	public String getQualifiedName()
	{
		return "[" + this.getName() + "]";
	}
	
	public MAFGOProjectCluster getProjectCluster()
	{
		if (this.projectCluster == null)
			this.projectCluster = MAFGOProjectCluster.getProjectCluster(this.getCtx(), this.getAFGO_ProjectCluster_ID());
		return this.projectCluster;
	}
	
	public MAFGOCalendar getCalendar()
	{
		return MAFGOCalendar.getCalendar(this.getCtx(), this.getAFGO_Calendar_ID());
	}
	
	public MAFGOServiceType getServiceType()
	{
		return MAFGOServiceType.getServiceType(this.getCtx(), this.getAFGO_ServiceType_ID());
	}
	
	public MAFGOServiceType getDefaultServiceType()
	{
		return this.getServiceType();
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if (this.getAD_Org_ID() < 1)
		{
			this.log.saveError("Org0NotAllowed", "AD_Org_ID=0");
			return false;
		}
		
		MClientInfo cinfo = MClientInfo.get(this.getCtx());
		MOrgInfo oinfo = MOrgInfo.get(this.getCtx(), this.getAD_Org_ID(), this.get_TrxName());
		this.setAFGO_Calendar_ID(oinfo.get_ValueAsInt("AFGO_Calendar_ID") > 0 ? oinfo.get_ValueAsInt("AFGO_Calendar_ID") : cinfo.get_ValueAsInt("AFGO_Calendar_ID"));
		
		if (!newRecord)
		{
			Timestamp oldStartDate = (Timestamp)this.get_ValueOld("StartDate");
			if (this.getStartDate().after(oldStartDate))
			{
				this.log.saveError("CannotIncrementStartDate", Msg.getMsg(this.getCtx(), "From") + "=" + oldStartDate + ", " + Msg.getMsg(this.getCtx(), "To") + "=" + this.getEndDate());
				return false;
			}
			
			Timestamp oldEndDate = (Timestamp)this.get_ValueOld("EndDate");
			if (this.getEndDate().before(oldEndDate))
			{
				this.log.saveError("CannotDecrementEndDate", Msg.getMsg(this.getCtx(), "From") + "=" + oldEndDate + ", " + Msg.getMsg(this.getCtx(), "To") + "=" + this.getEndDate());
				return false;
			}
		}
		
		if (this.getStartDate().after(this.getEndDate()))
		{
			this.log.saveError("StartDateAfterEndDate", Msg.getMsg(this.getCtx(), "StartDate") + "=" + this.getStartDate() + ", " + Msg.getMsg(this.getCtx(), "EndDate") + this.getEndDate());
			return false;
		}
		
		return true;
	}
	
	public boolean afterSave(boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		if (newRecord)
		{
			// create the default ProjectCluster
			MAFGOProjectCluster projectCluster = new MAFGOProjectCluster(this);

			if (projectCluster.save())
			{
				this.setAFGO_ProjectCluster_ID(projectCluster.getAFGO_ProjectCluster_ID());
				return this.save();
			}
		}
		else
		{
			// reset cache
			MAFGOProgram.s_cache.remove(this.getAFGO_Program_ID());
			return success;
		}
		
		return success;
	}
	
	public ArrayList<ProgramPeriod> getPeriods()
	{
		try
		{
			return this.getPeriods(this.getStartDate(), this.getEndDate());
		}
		catch(CompiereUserException e)
		{
			
		}
		catch(CompiereSystemException e)
		{
			
		}
		
		return new ArrayList<ProgramPeriod>();
	}
	
	public ArrayList<ProgramPeriod> getPeriods(Timestamp startDate, Timestamp endDate) throws CompiereUserException, CompiereSystemException
	{
		if (startDate.before(this.getStartDate()))
			throw new CompiereUserException(Msg.getMsg(this.getCtx(), "StartDateBeforeProgramStartDate"));
		if (endDate.after(this.getEndDate()))
			throw new CompiereUserException(Msg.getMsg(this.getCtx(), "EndDateAfterProgramEndDate"));
		
		ArrayList<ProgramPeriod> periods = new ArrayList<ProgramPeriod>();
		
		String sql = null;
		
		if (MAFGOProgram.PROGRAMPERIODTYPE_Quarter.equals(this.getProgramPeriodType()))
		{
			sql = "SELECT q.AFGO_Quarter_ID"
				+ " FROM AFGO_Quarter q"
				+ " INNER JOIN AFGO_Year y ON (y.AFGO_Year_ID=q.AFGO_Year_ID AND y.AFGO_Quarter_ID!=q.AFGO_Quarter_ID)"
				+ " WHERE y.AFGO_Calendar_ID=?"
				+ " AND q.IsActive='Y'"
				+ " AND y.IsActive='Y'"
				+ " AND q.EndDate > ?"
				+ " AND q.StartDate < ?"
				+ " ORDER BY q.StartDate";
		}
		else if (MAFGOProgram.PROGRAMPERIODTYPE_Month.equals(this.getProgramPeriodType()))
		{
			sql = "SELECT m.AFGO_Month_ID"
				+ " FROM AFGO_Month m"
				+ " INNER JOIN AFGO_Quarter q ON (q.AFGO_Quarter_ID=m.AFGO_Quarter_ID AND q.AFGO_Month_ID!=m.AFGO_Month_ID)"
				+ " INNER JOIN AFGO_Year y ON (y.AFGO_Year_ID=q.AFGO_Year_ID)"
				+ " WHERE y.AFGO_Calendar_ID=?"
				+ " AND m.IsActive='Y'"
				+ " AND q.IsActive='Y'"
				+ " AND y.IsActive='Y'"
				+ " AND m.EndDate > ?"
				+ " AND m.StartDate < ?"
				+ " ORDER BY m.StartDate";
		}
		else
			throw new CompiereSystemException(Msg.getMsg(this.getCtx(), "UnknownPeriodType") + ": " + this.getProgramPeriodType());
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Calendar_ID());
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				if (MAFGOProgram.PROGRAMPERIODTYPE_Quarter.equals(this.getProgramPeriodType()))
				{
					MAFGOQuarter quarter = MAFGOQuarter.getQuarter(this.getCtx(), rs.getInt(1));
					periods.add(quarter);
				}
				else if (MAFGOProgram.PROGRAMPERIODTYPE_Month.equals(this.getProgramPeriodType()))
				{
					MAFGOMonth month = MAFGOMonth.getMonth(this.getCtx(), rs.getInt(1));
					periods.add(month);
				}
			}
			
			pstmt.close();
			pstmt = null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch (SQLException e) {e.printStackTrace();}
		}
		
		return periods;
	}
	
	public MAFGOMonth getMonth(java.sql.Date date, boolean onlyOpen)
	{
		MAFGOMonth month = this.getMonth(date);
		
		if (onlyOpen && !month.isPeriodOpen())
			month = null;
		
		return month;
	}
	
	public MAFGOMonth getMonth(java.sql.Date date)
	{
		MAFGOMonth month = null;
		
		String sql = "SELECT m.AFGO_Month_ID,"
			+ " CASE"
			+ "   WHEN m.AFGO_Month_ID=q.AFGO_Month_ID AND q.AFGO_Quarter_ID=y.AFGO_Quarter_ID"
			+ "   THEN 0"
			+ "   WHEN m.AFGO_Month_ID=q.AFGO_Month_ID" 
			+ "   THEN 1"
			+ "   ELSE 2"
			+ " END AS Priority"
			+ " FROM AFGO_Program p"
			+ " INNER JOIN AFGO_Calendar c ON (c.AFGO_Calendar_ID=p.AFGO_Calendar_ID)"
			+ " INNER JOIN AFGO_Year y ON (y.AFGO_Calendar_ID=c.AFGO_Calendar_ID)"
			+ " INNER JOIN AFGO_Quarter q ON (q.AFGO_Year_ID=y.AFGO_Year_ID)"
			+ " INNER JOIN AFGO_Month m ON (m.AFGO_Quarter_ID=q.AFGO_Quarter_ID)"
			+ " WHERE p.AFGO_Program_ID=?"
			+ " AND y.IsActive='Y'"
			+ " AND q.IsActive='Y'"
			//+ " AND q.IsClosed='N'"
			+ " AND m.IsActive='Y'"
			//+ " AND m.IsClosed='N'"
			+ " AND ? BETWEEN m.StartDate AND m.EndDate"
			+ " ORDER BY Priority";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			pstmt.setInt(1, this.getAFGO_Program_ID());
			pstmt.setDate(2, date);
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				int AFGO_Month_ID = rs.getInt(1);
				int priority = rs.getInt(2);
				month = MAFGOMonth.getMonth(this.getCtx(), AFGO_Month_ID);
				log.fine("priority=" + priority + ", month=" + month);
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
		
		return month;
	}
	
	
	//
	
	private static CLogger s_log = CLogger.getCLogger(MAFGOProgram.class);
	
	// Estimated 10 programs
	private static final transient int CACHE_SIZE = 10;
	
	public static int getCacheSize()
	{
		return CACHE_SIZE;
	}
	
	public static final transient int LEVEL			= 50;
	
	private static CCache<Integer,MAFGOProgram> s_cache = new CCache<Integer,MAFGOProgram>(MAFGOProgram.Table_Name, getCacheSize());
	
	public static MAFGOProgram getProgram(Ctx ctx, int AFGO_Program_ID)
	{
		MAFGOProgram program = (AFGO_Program_ID > 0) ? s_cache.get(ctx, AFGO_Program_ID) : null;
		
		if (AFGO_Program_ID > 0 && program == null)
		{
			program = new MAFGOProgram(ctx, AFGO_Program_ID, null);
			
			if (AFGO_Program_ID == program.getAFGO_Program_ID())
				s_cache.put(program.getAFGO_Program_ID(), program);
			else
				program = null;
		}
		
		return program;
	}
	
	public static String validateProgramOrganization(PO po)
	{
		s_log.fine("Validate Program / Organization: " + po);
		String result = null;
		
		int AD_Org_ID = (Integer)po.get_Value("AD_Org_ID");
		int AFGO_Program_ID = (po.get_Value("AFGO_Program_ID") != null ? (Integer)po.get_Value("AFGO_Program_ID") : -1);
		
		if (AFGO_Program_ID > 0)
		{
			if (DB.getSQLValue(po.get_TrxName(), "SELECT AD_Org_ID FROM AFGO_Program WHERE AFGO_Program_ID=?", AFGO_Program_ID) != AD_Org_ID)
				result =  Msg.getMsg(po.getCtx(), "InvalidProgramOrg");
		}
		
		if (result == null)
			s_log.fine("Valid AD_Org / AFGO_Program combination");
		
		return result;
	}
	
	public static String validateAllocation(PO po)
	{
		s_log.fine("Validate Allocation: " + po);
		
		Integer AFGO_ProjectCluster_ID = (Integer)po.get_Value("AFGO_ProjectCluster_ID");
		if (AFGO_ProjectCluster_ID == null || AFGO_ProjectCluster_ID < 1)
		{
			POInfo poinfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID());
			if (poinfo == null)
				return "No POInfo for table: " + po.get_TableName();
			if (!poinfo.isColumnMandatory(poinfo.getColumnIndex("AFGO_ProjectCluster")))
				return null;
			
			return Msg.getMsg(po.getCtx(), "NoProjectCluster");
		}
		
		Integer AFGO_Project_ID = (Integer)po.get_Value("AFGO_Project_ID");
		if (AFGO_Project_ID == null || AFGO_Project_ID < 1)
			return Msg.getMsg(po.getCtx(), "NoProject");
		
		Integer AFGO_Phase_ID = (Integer)po.get_Value("AFGO_Phase_ID");
		if (AFGO_Phase_ID == null || AFGO_Phase_ID < 1)
			return Msg.getMsg(po.getCtx(), "NoPhase");
		
		Integer AFGO_Activity_ID = (Integer)po.get_Value("AFGO_Activity_ID");
		if (AFGO_Activity_ID == null || AFGO_Activity_ID < 1)
			return Msg.getMsg(po.getCtx(), "NoActivity");
		
		String sql = "SELECT pr.AFGO_ProjectCluster_ID"
			+ " FROM AFGO_Project pr"
			+ " INNER JOIN AFGO_Phase ph ON (ph.AFGO_Project_ID=pr.AFGO_Project_ID)"
			+ " INNER JOIN AFGO_Activity ac ON (ac.AFGO_Phase_ID=ph.AFGO_Phase_ID)"
			+ " WHERE ac.AFGO_Activity_ID=?";
		
		if (DB.getSQLValue(po.get_TrxName(), sql, AFGO_Activity_ID) != AFGO_ProjectCluster_ID)
			return Msg.getMsg(po.getCtx(), "InvalidAllocation");
		
		
		s_log.fine("Valid Allocation: " + po);
		
		return null;
	}
	

}
