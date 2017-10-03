SET DEFINE OFF;

UPDATE AD_Ref_List_Trl rlt
SET rlt.Name=
(
	SELECT CASE
	WHEN rl.Value='--' THEN 'Ongedefinieerd'
	WHEN rl.Value='BM' THEN 'Budget Beheerder'
	WHEN rl.Value='BO' THEN 'Budget Eigenaar'
	WHEN rl.Value='CO' THEN 'Control'
	WHEN rl.Value='EX' THEN 'Directie'
	WHEN rl.Value='FA' THEN 'GD-FIN'
	WHEN rl.Value='FD' THEN 'Hoofd GD-FIN'
	WHEN rl.Value='HR' THEN 'GD-P&O'
	WHEN rl.Value='LD' THEN 'Juridisch'
	WHEN rl.Value='PD' THEN 'Inkoopbureau'
	WHEN rl.Value='SI' THEN 'Selectie / Intake'
	ELSE TO_CHAR(rltt.name)
	END AS Name
	FROM AD_Ref_List rl
	INNER JOIN AD_Ref_List_trl rltt ON (rltt.AD_Ref_List_ID=rl.AD_Ref_List_ID)
	WHERE rl.AD_Ref_List_ID=rlt.AD_Ref_List_ID
	AND rltt.AD_Language=rlt.AD_Language
)
WHERE rlt.Name!=
(
	SELECT CASE
	WHEN rl.Value='--' THEN 'Ongedefinieerd'
	WHEN rl.Value='BM' THEN 'Budget Beheerder'
	WHEN rl.Value='BO' THEN 'Budget Eigenaar'
	WHEN rl.Value='CO' THEN 'Control'
	WHEN rl.Value='EX' THEN 'Directie'
	WHEN rl.Value='FA' THEN 'GD-FIN'
	WHEN rl.Value='FD' THEN 'Hoofd GD-FIN'
	WHEN rl.Value='HR' THEN 'GD-P&O'
	WHEN rl.Value='LD' THEN 'Juridisch'
	WHEN rl.Value='PD' THEN 'Inkoopbureau'
	WHEN rl.Value='SI' THEN 'Selectie / Intake'
	ELSE TO_CHAR(rltt.name)
	END AS Name
	FROM AD_Ref_List rl
	INNER JOIN AD_Ref_List_trl rltt ON (rltt.AD_Ref_List_ID=rl.AD_Ref_List_ID)
	WHERE rl.AD_Ref_List_ID=rlt.AD_Ref_List_ID
	AND rltt.AD_Language=rlt.AD_Language
)
AND AD_Ref_List_ID IN
(
	SELECT rl.AD_Ref_List_ID
	FROM AD_Reference r
	INNER JOIN AD_Ref_List rl ON (rl.AD_Reference_ID=r.AD_Reference_ID)
	WHERE r.Name='AFGO_WorkflowRoleType'
	AND rl.EntityType IN ('AFGO', 'ICTU')
)
AND AD_Language='nl_NL';

UPDATE AFGO_WorkflowRole
SET Name=
(
	SELECT l.Name
	FROM AD_Reference r
	INNER JOIN AD_Ref_List l ON (l.AD_Reference_ID=r.AD_Reference_ID)
	WHERE r.Name='AFGO_WorkflowRoleType'
	AND l.Value=AFGO_WorkflowRole.WorkflowRoleType
)
WHERE EntityType='ICTU'
AND Name NOT IN
(
	SELECT l.Name
	FROM AD_Reference r
	INNER JOIN AD_Ref_List l ON (l.AD_Reference_ID=r.AD_Reference_ID)
	WHERE r.Name='AFGO_WorkflowRoleType'
	AND l.Value=AFGO_WorkflowRole.WorkflowRoleType
);

UPDATE AD_WF_Responsible wrr
SET wrr.Name=
(
	SELECT rlt.Name
	FROM AFGO_WorkflowRole wfr
	INNER JOIN AD_Ref_List rl ON (rl.Value=wfr.WorkflowRoleType)
	INNER JOIN AD_Ref_List_Trl rlt ON (rlt.AD_Ref_List_ID=rl.AD_Ref_List_ID AND rlt.AD_Language='nl_NL')
	INNER JOIN AD_Reference r ON (r.AD_Reference_ID=rl.AD_Reference_ID)
	WHERE r.Name='AFGO_WorkflowRoleType'
	AND wfr.AFGO_WorkflowRole_ID=wrr.AFGO_WorkflowRole_ID
)
WHERE wrr.Name!=
(
	SELECT rlt.Name
	FROM AFGO_WorkflowRole wfr
	INNER JOIN AD_Ref_List rl ON (rl.Value=wfr.WorkflowRoleType)
	INNER JOIN AD_Ref_List_Trl rlt ON (rlt.AD_Ref_List_ID=rl.AD_Ref_List_ID AND rlt.AD_Language='nl_NL')
	INNER JOIN AD_Reference r ON (r.AD_Reference_ID=rl.AD_Reference_ID)
	WHERE r.Name='AFGO_WorkflowRoleType'
	AND wfr.AFGO_WorkflowRole_ID=wrr.AFGO_WorkflowRole_ID
)
AND wrr.AFGO_WorkflowRole_ID > 0
AND wrr.EntityType='ICTU'
AND wrr.ResponsibleType='C';

UPDATE AD_WF_Responsible
SET Name='Invoker'
WHERE Name='Opsteller';

UPDATE AD_WF_Node
SET Value='(ApproveBudgetMaintainer)'
WHERE Value='(ApprBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveBudgetOwner)'
WHERE Value='(ApprBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAllocation)'
WHERE Value='(AppAllocation)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAllocationBudgetOwner)'
WHERE Value='(AppAllocationBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAllocationFinancial)'
WHERE Value='(AppAllocationFinancial)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAllocationFA)'
WHERE Value='(AppAllocationFinancialAdministrator)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAllocationFinancialDirector)'
WHERE Value='(AppAllocationFinancialDirector)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveDistributionBudgetMaintainer)'
WHERE Value='(AppDistributionBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveDistributionBudgetOwner)'
WHERE Value='(AppDistributionBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAdditionalBudgetMaintainer)'
WHERE Value='(AppAdditionalBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveAdditionalBudgetOwner)'
WHERE Value='(AppAdditionalBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveCommitmentBudgetOwner)'
WHERE Value='(AppCommitmentBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveCommitmentExecutive)'
WHERE Value='(AppCommitmentExecutive)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveRequestBudgetMaintainer)'
WHERE Value='(AppRequestBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveRequestBudgetOwner)'
WHERE Value='(AppRequestBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveRequestPurchaseDepartment)'
WHERE Value='(AppRequestPurchaseDepartment)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveSecondmentBudgetMaintainer)'
WHERE Value='(AppSecondmentBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveSecondmentBudgetOwner)'
WHERE Value='(AppSecondmentBudgetOwner)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveSecondmentHumanResources)'
WHERE Value='(AppSecondmentHumanResources)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveTransferBudgetMaintainer)'
WHERE Value='(AppTransferBudgetMaintainer)'
AND EntityType='ICTU';

UPDATE AD_WF_Node
SET Value='(ApproveCommitmentPurchaseDepartment)'
WHERE Value='(AppCommitmentPurchaseDepartment)'
AND EntityType='ICTU';


COMMIT;

QUIT;