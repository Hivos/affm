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
import org.compiere.model.MEntityType;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import com.afgo.model.EntityTypeModule;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: ICTUModelValidator.java,v 1.19.2.1 2010/01/06 11:58:18 tomassen Exp $
 */
public class ICTUModelValidator implements ModelValidator
{
	
	public ICTUModelValidator()
	{
		
	}
	
	private EntityTypeModule module = null;
	
	private int AD_Client_ID = -1;
	
	private ModelValidationEngine engine = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public void initialize(int AD_Client_ID, ModelValidationEngine engine)
	{
		this.AD_Client_ID = AD_Client_ID;
		this.engine = engine;
		
		MEntityType entityType = MEntityType.getEntityType(Env.getCtx(), EntityType);
		if (entityType == null)
			throw new IllegalStateException("Module not installed: " + EntityType);
		this.module = new EntityTypeModule(entityType);
		
		log.info("****************************************************************************************");
		log.info("Application Module: " + this.module.getName() + " " + this.module.getVersionString());
		log.info("Application Code: " + EntityType + " " + getVersionString());
		log.info("****************************************************************************************");
		
		if (getMajor() != module.getMajor() || getMinor() != module.getMinor() || getRevision() != module.getRevision())
			throw new IllegalStateException("Module / Code version mismatch");
		
		//
		DateAcctPeriodValidator dateAcctPeriodValidator = new DateAcctPeriodValidator();
		dateAcctPeriodValidator.initialize(this.getAD_Client_ID(), engine);
		
		// 
		CostDistrAllocationValidator costDistrAllocationValidator = new CostDistrAllocationValidator();
		costDistrAllocationValidator.initialize(this.getAD_Client_ID(), engine);
		
		// 
		ICTUWorkflowValidator workflowValidator = new ICTUWorkflowValidator();
		workflowValidator.initialize(this.getAD_Client_ID(), engine);	
		
	}
	
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}
	
	public int getAD_Client_ID()
	{
		return this.AD_Client_ID;
	}

	public String modelChange(PO po, int type) throws Exception
	{
		return null;
	}
	
	public String docValidate(PO doc, int timing)
	{
		return null;
	}
	
	public boolean updateInfoColumns(ArrayList<Info_Column> arg0, StringBuffer arg1, StringBuffer arg2)
	{
		return false;
	}

/**********************************************************************************************/
	
	public static final transient String EntityType = "ICTU";
	
	public static final transient String COPYRIGHT = "(c) 2007-2010 Stichting ICTU";
	
	public static final transient int MAJOR_VERSION = 2;
	
	public static final transient int MINOR_VERSION = 2;
	
	public static final transient int REVISION_VERSION = 6;
	
	public static final transient int PATCH_VERSION = 0;
	
	public static int getMajor()
	{
		return MAJOR_VERSION;
	}
	
	public static int getMinor()
	{
		return MINOR_VERSION;
	}
	
	public static int getRevision()
	{
		return REVISION_VERSION;
	}
	
	public static int getPatch()
	{
		return PATCH_VERSION;
	}
	
	public static String getVersionString()
	{
		return "v" + getMajor() + "." + getMinor() + "." + getRevision() + "." + getPatch();
	}

}
