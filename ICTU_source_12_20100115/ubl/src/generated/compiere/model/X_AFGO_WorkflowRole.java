/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AFGO_WorkflowRole
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: X_AFGO_WorkflowRole.java,v 1.3 2009/11/24 14:17:54 tomassen Exp $ */
public class X_AFGO_WorkflowRole extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AFGO_WorkflowRole_ID id
    @param trxName transaction
    */
    public X_AFGO_WorkflowRole (Ctx ctx, int AFGO_WorkflowRole_ID, String trxName)
    {
        super (ctx, AFGO_WorkflowRole_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (AFGO_WorkflowRole_ID == 0)
        {
            setAFGO_WorkflowRole_ID (0);
            setEntityType (null);
            setName (null);
            setWorkflowRoleType (null);	// --
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_AFGO_WorkflowRole (Ctx ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536610845789L;
    /** Last Updated Timestamp 2009-10-02 14:12:09.0 */
    public static final long updatedMS = 1254485529000L;
    /** AD_Table_ID=1000033 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("AFGO_WorkflowRole");
        
    }
    ;
    
    /** TableName=AFGO_WorkflowRole */
    public static final String Table_Name="AFGO_WorkflowRole";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AFGO_WorkflowRole");
    
    /** AccessLevel
    @return 6 - System - Client 
    */
    @Override protected PO.AccessLevel get_AccessLevel()
    {
        return PO.AccessLevel.SYSTEMCLIENT;
        
    }
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Workflow Role.
    @param AFGO_WorkflowRole_ID Workflow Role */
    public void setAFGO_WorkflowRole_ID (int AFGO_WorkflowRole_ID)
    {
        if (AFGO_WorkflowRole_ID < 1) throw new IllegalArgumentException ("AFGO_WorkflowRole_ID is mandatory.");
        set_ValueNoCheck ("AFGO_WorkflowRole_ID", Integer.valueOf(AFGO_WorkflowRole_ID));
        
    }
    
    /** Get Workflow Role.
    @return Workflow Role */
    public int getAFGO_WorkflowRole_ID() 
    {
        return get_ValueAsInt("AFGO_WorkflowRole_ID");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    
    /** EntityType AD_Reference_ID=389 */
    public static final int ENTITYTYPE_AD_Reference_ID=389;
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_ValueNoCheck ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    
    /** WorkflowRoleType AD_Reference_ID=1000056 */
    public static final int WORKFLOWROLETYPE_AD_Reference_ID=1000056;
    /** N/A = -- */
    public static final String WORKFLOWROLETYPE_NA = X_Ref_AFGO_WorkflowRoleType.NA.getValue();
    /** Budget Maintainer = BM */
    public static final String WORKFLOWROLETYPE_BudgetMaintainer = X_Ref_AFGO_WorkflowRoleType.BUDGET_MAINTAINER.getValue();
    /** Budget Owner = BO */
    public static final String WORKFLOWROLETYPE_BudgetOwner = X_Ref_AFGO_WorkflowRoleType.BUDGET_OWNER.getValue();
    /** Control = CO */
    public static final String WORKFLOWROLETYPE_Control = X_Ref_AFGO_WorkflowRoleType.CONTROL.getValue();
    /** Executive = EX */
    public static final String WORKFLOWROLETYPE_Executive = X_Ref_AFGO_WorkflowRoleType.EXECUTIVE.getValue();
    /** Financial Administrator = FA */
    public static final String WORKFLOWROLETYPE_FinancialAdministrator = X_Ref_AFGO_WorkflowRoleType.FINANCIAL_ADMINISTRATOR.getValue();
    /** Financial Director = FD */
    public static final String WORKFLOWROLETYPE_FinancialDirector = X_Ref_AFGO_WorkflowRoleType.FINANCIAL_DIRECTOR.getValue();
    /** Human Resources = HR */
    public static final String WORKFLOWROLETYPE_HumanResources = X_Ref_AFGO_WorkflowRoleType.HUMAN_RESOURCES.getValue();
    /** Legal Department = LD */
    public static final String WORKFLOWROLETYPE_LegalDepartment = X_Ref_AFGO_WorkflowRoleType.LEGAL_DEPARTMENT.getValue();
    /** Purchase Department = PD */
    public static final String WORKFLOWROLETYPE_PurchaseDepartment = X_Ref_AFGO_WorkflowRoleType.PURCHASE_DEPARTMENT.getValue();
    /** Quotation Selector = QS */
    public static final String WORKFLOWROLETYPE_QuotationSelector = X_Ref_AFGO_WorkflowRoleType.QUOTATION_SELECTOR.getValue();
    /** Selection / Intake = SI */
    public static final String WORKFLOWROLETYPE_SelectionIntake = X_Ref_AFGO_WorkflowRoleType.SELECTION_INTAKE.getValue();
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isWorkflowRoleTypeValid(String test)
    {
         return X_Ref_AFGO_WorkflowRoleType.isValid(test);
         
    }
    /** Set Workflow Role Type.
    @param WorkflowRoleType Workflow Role Type */
    public void setWorkflowRoleType (String WorkflowRoleType)
    {
        if (WorkflowRoleType == null) throw new IllegalArgumentException ("WorkflowRoleType is mandatory");
        if (!isWorkflowRoleTypeValid(WorkflowRoleType))
        throw new IllegalArgumentException ("WorkflowRoleType Invalid value - " + WorkflowRoleType + " - Reference_ID=1000056 - -- - BM - BO - CO - EX - FA - FD - HR - LD - PD - QS - SI");
        set_Value ("WorkflowRoleType", WorkflowRoleType);
        
    }
    
    /** Get Workflow Role Type.
    @return Workflow Role Type */
    public String getWorkflowRoleType() 
    {
        return (String)get_Value("WorkflowRoleType");
        
    }
    
    
}
