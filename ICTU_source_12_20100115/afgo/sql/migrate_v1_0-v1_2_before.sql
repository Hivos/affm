SET DEFINE OFF;

UPDATE AD_Element
SET EntityType='ICTU'
WHERE EntityType='AFGO'
AND ColumnName IN
(
	'IsBudgetMaintainerApproved',
	'IsBudgetOwnerApproved',
	'IsControlApproved',
	'IsExecutiveApproved',
	'IsFinancialApproved',
	'IsFinancialDirectorApproved',
	'IsHumanResourcesApproved',	--already ICTU
	'IsPurchaseDepartmentApproved',
	'IsSelectionIntakeApproved',
	'IsDeliveryAcknowledged'
);

UPDATE AD_Column
SET EntityType='ICTU'
WHERE EntityType='AFGO'
AND (AD_Table_ID, ColumnName) IN
(
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice'), 'IsDeliveryAcknowledged'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostDistr'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostDistr'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostDistr'), 'IsFinancialAdministrator'),		--already ICTU?
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostDistr'), 'IsFinancialApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostDistr'), 'IsFinancialDirectorApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund'), 'IsControlApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund'), 'IsExecutiveApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund'), 'IsSelectionIntakeApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_InternalCommitment'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_InternalCommitment'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment'), 'IsExecutiveApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment'), 'IsHumanResourcesApproved'),	--already ICTU
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment'), 'IsPurchaseDepartmentApproved'),
	--VIEWS
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget_Header_v'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget_Header_v'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Commitment_Header_v'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Commitment_Header_v'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Commitment_Header_v'), 'IsPurchaseDepartmentApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_CostAllocationHeader_v'), 'IsFinancialAdministrator'),		-- already ICTU
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund_Header_v'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund_Header_v'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund_Header_v'), 'IsExecutiveApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Fund_Header_v'), 'IsSelectionIntakeApproved'),
	--RV VIEWS
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_RV_AdditionalCommitment'), 'IsBudgetMaintainerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_RV_AdditionalCommitment'), 'IsBudgetOwnerApproved'),
	((SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_RV_AdditionalCommitment'), 'IsPurchaseDepartmentApproved')
);

UPDATE AD_ViewColumn
SET EntityType='ICTU'
WHERE AD_ViewColumn_ID IN
(
	SELECT vcol.AD_ViewColumn_ID
	FROM AD_Table tab
	LEFT OUTER JOIN AD_Table ttab ON (ttab.TableName=tab.TableName || 't')
	INNER JOIN AD_Column col ON (col.AD_Table_ID=tab.AD_Table_ID OR col.AD_Table_ID=ttab.AD_Table_ID)
	INNER JOIN AD_ViewComponent vcom ON (vcom.AD_Table_ID=tab.AD_Table_ID OR vcom.AD_TAble_ID=ttab.AD_Table_ID)
	INNER JOIN AD_ViewColumn vcol ON (vcol.AD_ViewComponent_ID=vcom.AD_ViewComponent_ID AND vcol.ColumnName=col.ColumnName)
	WHERE (tab.TableName LIKE '%_v' OR tab.TableName LIKE '%RV_%')
	AND col.EntityType='ICTU'
	AND vcol.EntityType='AFGO'
);


UPDATE AD_Ref_List
SET EntityType='AFGO'
WHERE EntityType='ICTU'
AND Value='SC'
AND AD_Reference_ID IN
(
	SELECT AD_Reference_ID
	FROM AD_Reference
	WHERE Name='AFGO_PurchaseCommitmentType'
);

UPDATE AD_Ref_List
SET EntityType='ICTU'
WHERE EntityType='AFGO'
AND Value!='--'
AND AD_Reference_ID IN
(
	SELECT AD_Reference_ID
	FROM AD_Reference
	WHERE Name='AFGO_WorkflowRoleType'
);

UPDATE AD_Field
SET EntityType='ICTU'
WHERE EntityType='AFGO'
AND AD_Column_ID IN
(
	SELECT AD_Column_ID
	FROM AD_Column
	WHERE EntityType='ICTU'
);


UPDATE AD_Workflow
SET Name='Process_Budget'
WHERE (Name='Process Budget' OR Name='Proces budget')
AND EntityType='AFGO';

UPDATE AD_WF_Node
SET EntityType='ICTU'
WHERE AD_WF_Node_ID IN
(
	SELECT n.AD_WF_Node_ID
	FROM AD_Workflow w
	INNER JOIN AD_WF_Node n ON(n.AD_Workflow_ID=w.AD_Workflow_ID)
	WHERE w.Name='Process_PurchaseCommitment'
	AND w.EntityType='AFGO'
	AND n.Value='(DocVoid)'
	AND n.EntityType='AFGO'
);

UPDATE AD_Val_Rule
SET EntityType='AFGO'
WHERE Name='C_DocType Purchase Commitment (Reset Purchase Commitment)'
AND EntityType!='AFGO';

UPDATE AD_Ref_Table
SET EntityType='AFGO'
WHERE EntityType!='AFGO'
AND AD_Reference_ID IN
(
	SELECT AD_Reference_ID
	FROM AD_Reference
	WHERE Name='AFGO_PurchaseCommitment (Invalid & Rejected)'
);

UPDATE AD_Reference
SET Name='AFGO_RV_PurchaseCommitmentLine'
WHERE Name='AFGO_PurchaseCommitmentLine_v'
AND EntityType='AFGO';


UPDATE AD_Ref_List
SET EntityType='U'
WHERE EntityType='AFGO'
AND AD_Reference_ID=(SELECT AD_Reference_ID FROM AD_Reference WHERE Name='AFGO_PurchaseCommitmentType');

UPDATE AD_Process
SET Name='Commitment Report'
WHERE Value='AFGO_RV_CommitmentReport'
AND Name='Commitment report'
AND EntityType='AFGO';


DELETE
FROM AD_ViewColumn
WHERE EntityType='AFGO'
AND AD_ViewComponent_ID IN
(
	SELECT AD_ViewComponent_ID
	FROM AD_ViewComponent
	WHERE EntityType='AFGO'
	AND Name IN ('Formalized Commitment', 'Planned Commitment')
	AND AD_Table_ID IN
	(
		SELECT AD_Table_ID
		FROM AD_Table
		WHERE TableName='AFGO_RV_DocLineAmt'
	)
);

DELETE
FROM AD_WF_NextCondition
WHERE AD_Column_ID IN
(
	SELECT c.AD_Column_ID
	FROM AD_Table t
	INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)
	WHERE t.TableName='AFGO_PurchaseCommitment'
	AND c.ColumnName='PurchaseCommitmentType'
);

DELETE
FROM AD_Column_Access
WHERE AD_Column_ID IN
(
	SELECT c.AD_Column_ID
	FROM AD_Column c
	INNER JOIN AD_Table t ON (t.AD_Table_ID=c.AD_Table_ID)
	WHERE c.ColumnName='IsMantel'
	AND t.TableName='AFGO_QuotationResponse'
);

COMMIT;

QUIT;