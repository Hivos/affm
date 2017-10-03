/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 Stichting ICTU 																		*
 * 																										*
 ********************************************************************************************************/
package nl.ictu.model;

import java.util.ArrayList;

import org.compiere.api.ModelValidator;
import org.compiere.framework.Info_Column;
import org.compiere.framework.ModelValidationEngine;
import org.compiere.framework.PO;
import org.compiere.util.CLogger;

import com.afgo.model.MAFGOCostDistr;
import com.afgo.model.MAFGOCostDistrAllocation;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: CostDistrAllocationValidator.java,v 1.4.2.1 2010/01/06 11:58:18 tomassen Exp $
 * 
 * Prevent Cost Distribution from being allocated when
 * not allowed.
 *
 */
public class CostDistrAllocationValidator implements ModelValidator
{
	public CostDistrAllocationValidator()
	{
		
	}
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		this.log.info("CostDistrAllocationValidator started: AD_Client_ID=" + this.getAD_Client_ID());
		
		this.engine.addModelChange(MAFGOCostDistrAllocation.Table_Name, this);
		
		this.engine.addDocValidate(MAFGOCostDistr.Table_Name, this);
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
		/*
		 * Allocation is performed as part of the WF,
		 * by the BudgetMaintainer of the program that is receiving the costs
		 * 
		 * except when:
		 * 
		 * -distribution created by FA
		 * -distribution within one program
		 * 
		 */
		if (po instanceof MAFGOCostDistrAllocation)
		{
			MAFGOCostDistrAllocation allocation = (MAFGOCostDistrAllocation)po;
			MAFGOCostDistr distribution = allocation.getCostDistributionLine().getHeader();
			
			if (distribution.isBudgetMaintainerApproved() && distribution.isBudgetOwnerApproved()) 
				return null;
			else if (distribution.isSingleProgram())
				return null;
			else if (distribution.isFinancialAdministrator())
				return null;
			
			return "AllocationNotAllowed";

		}
		return null;
	}
	
	public String docValidate(PO doc, int timing)
	{
		/*
		 * Doc created by FA should be allocated before
		 * entering the WF
		 * AFP2927: Same applies to docs within one program 
		 */
		if (doc instanceof MAFGOCostDistr)
		{
			MAFGOCostDistr distribution = (MAFGOCostDistr)doc;
			
			if ((distribution.isFinancialAdministrator() || distribution.isSingleProgram()) && ModelValidator.DOCTIMING_BEFORE_PREPARE == timing)
			{
				if (!distribution.isAllocated())
					return "NotAllocated";
			}
		}
		return null;
	}

	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

}
