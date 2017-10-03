/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 Stichting ICTU 																		*
 * 																										*
 ********************************************************************************************************/
package nl.ictu.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

import com.afgo.model.AFGOModelValidator;
import com.afgo.model.AllocatableDocument;
import com.afgo.model.AllocatableDocumentLine;
import com.afgo.model.MAFGOCostDistr;
import com.afgo.model.MAFGOCostDistrAllocation;
import com.afgo.model.MAFGOCostDistrLine;
import com.afgo.model.MAFGOMonth;
import com.afgo.model.MAFGOProgram;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: DateAcctPeriodValidator.java,v 1.6.2.1 2010/01/06 11:58:18 tomassen Exp $
 *
 * Period on document line should match DateAcct on header
 */

public class DateAcctPeriodValidator implements ModelValidator
{

	public DateAcctPeriodValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("DateAcctPeriodValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		// header level
		this.engine.addModelChange(MInvoice.Table_Name, this);
		this.engine.addModelChange(MAFGOCostDistr.Table_Name, this);
		
		// line level
		this.engine.addModelChange(MInvoiceLine.Table_Name, this);
		this.engine.addModelChange(MAFGOCostDistrLine.Table_Name, this);
		this.engine.addModelChange(MAFGOCostDistrAllocation.Table_Name, this);
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}

	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}

	public String modelChange(PO po, int type) throws Exception
	{
		if (po.get_Info().getColumnIndex("DateAcct") > 1)
		{	
			log.fine("header:" + po);
			return this.headerUpdated(po);
		}
		
		AllocatableDocumentLine line = AFGOModelValidator.castAllocatableDocumentLine(po);
		if (line != null)
		{
			log.fine("line: " + po);
			return this.lineUpdated(line);
		}

		return null;
	}
	
	public String docValidate(PO doc, int timing)
	{
		return null;
	}
	
	private String headerUpdated(PO po)
	{
		try
		{
    		AllocatableDocument header = AFGOModelValidator.castAllocatableDocument(po);
    		Timestamp newDateAcct = (Timestamp)po.get_Value("DateAcct");
    		Timestamp oldDateAcct = (Timestamp)po.get_ValueOld("DateAcct");
    		if (oldDateAcct == null)
    			oldDateAcct = newDateAcct;
    		
    		if (header != null && (newDateAcct.before(oldDateAcct) || newDateAcct.after(oldDateAcct)))
    		{
    			MAFGOProgram program = header.getProgram();
    			MAFGOMonth month = program.getMonth(new java.sql.Date(newDateAcct.getTime()), false);
    			
    			if (month == null)
    			{
    				return "NoPeriod: " + newDateAcct;
    			}
    			else if (!month.isPeriodOpen())
    			{
    				return "PeriodClosed: " + newDateAcct;
    			}
    			
    			String sql = null;
    			ArrayList<Object> params = new ArrayList<Object>();
    			params.add(month.getAFGO_Quarter_ID());
    			params.add(month.getAFGO_Month_ID());
    			params.add(header.get_ID());
    			
    			// C_InvoiceLine
    			if (MInvoice.Table_Name.equals(po.get_TableName()))
    			{
    				sql = "UPDATE C_InvoiceLine"
    					+ " SET AFGO_Quarter_ID=?, AFGO_Month_ID=?"
    					+ " WHERE C_Invoice_ID=?";
    
    				DB.executeUpdate(sql, params, false, po.get_TrxName());
    			}
    			// AFGO_CostDistrLine / AFGO_CostDistrAllocation
    			else if (MAFGOCostDistr.Table_Name.equals(po.get_TableName()))
    			{
    				sql = "UPDATE AFGO_CostDistrLine"
    					+ " SET AFGO_Quarter_ID=?, AFGO_Month_ID=?"
    					+ " WHERE AFGO_CostDistr_ID=?";
    				
    				DB.executeUpdate(sql, params, false, po.get_TrxName());
    				
    				sql = "UPDATE AFGO_CostDistrAllocation"
    					+ " SET AFGO_Quarter_ID=?, AFGO_Month_ID=?"
    					+ " WHERE AFGO_CostDistrLine_ID IN"
    					+ " ("
    					+ " 	SELECT AFGO_CostDistrLine_ID"
    					+ " 	FROM AFGO_CostDistrLine"
    					+ " 	WHERE AFGO_CostDistr_ID=?"
    					+ " )";
    				
    				DB.executeUpdate(sql, params, false, po.get_TrxName());
    			} 
    		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private String lineUpdated(AllocatableDocumentLine line)
	{
		AllocatableDocument header = line.getHeader();
		
		if (header.getPO().get_Info().getColumnIndex("DateAcct") < 0)
			return null;
		
		Timestamp dateAcct = (Timestamp)header.getPO().get_Value("DateAcct");
		if (dateAcct == null)
			return null;
		
		MAFGOMonth month = MAFGOMonth.getMonth(header.getCtx(), line.getAFGO_Month_ID());
		
		if (dateAcct.before(month.getStartDate()) || dateAcct.after(month.getEndDate()))
		{
			MAFGOProgram program = header.getProgram();
			month = program.getMonth(new java.sql.Date(dateAcct.getTime()), false);
			
			if (month == null)
			{
				return "NoPeriod: " + dateAcct;
			}
			else if (!month.isPeriodOpen())
			{
				return "PeriodClosed: " + dateAcct;
			}

			line.getPO().set_ValueNoCheck("AFGO_Quarter_ID", month.getAFGO_Quarter_ID());
			line.getPO().set_ValueNoCheck("AFGO_Month_ID", month.getAFGO_Month_ID());

		}
		
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
