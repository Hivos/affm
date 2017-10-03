
DELETE
FROM AD_ViewColumn
WHERE EntityType='AFGO';

DELETE
FROM AD_ViewComponent
WHERE EntityType='AFGO'
AND AD_ViewComponent_ID NOT IN
(
	SELECT AD_ViewComponent_ID
	FROM AD_ViewColumn
);

DELETE
FROM AFGO_T_RevenueExpense;

DELETE
FROM T_TrialBalance;

DELETE
FROM AD_PInstance_Para;

DELETE
FROM AD_PInstance;

DELETE
FROM AD_WF_EventAudit;


-- tables

UPDATE AD_Table
SET TableName='AFGO_RV_AdditionalCommitment'
WHERE TableName='AFGO_AdditionalCommitment_v';

UPDATE AD_Table
SET TableName='AFGO_RV_CostReportLine'
WHERE TableName='AFGO_CostReportLine_v';

UPDATE AD_Table
SET TableName='AFGO_RV_DocLineAmt'
WHERE TableName='AFGO_DocLineAmt_v';

UPDATE AD_Table
SET TableName='AFGO_RV_DocLineFact'
WHERE TableName='AFGO_DocLineFact_v';

UPDATE AD_Table
SET TableName='AFGO_RV_DocLine_WFActivity'
WHERE TableName='AFGO_DocLine_WFActivity_v';

UPDATE AD_Table
SET TableName='AFGO_RV_DocLine'
WHERE TableName='AFGO_DocLine_v';

UPDATE AD_Table
SET TableName='AFGO_RV_InvoiceLineAllocation'
WHERE TableName='AFGO_InvoiceLineAllocation_v';

UPDATE AD_Table
SET TableName='AFGO_RV_Program'
WHERE TableName='AFGO_Program_v';

UPDATE AD_Table
SET TableName='AFGO_RV_TransferableCommitment'
WHERE TableName='AFGO_TransferableCommitment_v';

UPDATE AD_Table
SET TableName='AFGO_RV_WorkflowRoleAssignment'
WHERE TableName='AFGO_WorkflowRoleAssignment_v';

UPDATE AD_Table
SET TableName='AFGO_RV_CommitmentReportBase'
WHERE TableName='AFGO_CommitmentReportBase_v';

UPDATE AD_Table
SET TableName='AFGO_RV_BudgetReport'
WHERE TableName='AFGO_BudgetReport_v';

UPDATE AD_Table
SET TableName='AFGO_RV_CommitmentReport'
WHERE TableName='AFGO_CommitmentReport_v';

UPDATE AD_Table
SET TableName='AFGO_RV_CostReport'
WHERE TableName='AFGO_CostReport_v';

UPDATE AD_Table
SET TableName='AFGO_RV_CostReportBase'
WHERE TableName='AFGO_CostReportBase_v';

UPDATE AD_Table
SET TableName='AFGO_RV_PayablesPendingReport'
WHERE TableName='AFGO_PayablesPendingReport_v';

UPDATE AD_Table
SET TableName='AFGO_RV_SpentReport'
WHERE TableName='AFGO_SpentReport_v';

UPDATE AD_Table
SET TableName='AFGO_RV_PurchaseCommitmentLine'
WHERE TableName='AFGO_PurchaseCommitmentLine_v';


--report views

UPDATE AD_ReportView
SET Name='AFGO_RV_CostReportLine'
WHERE Name='AFGO_CostReportLine_v';

UPDATE AD_ReportView
SET Name='AFGO_RV_DocLine_WFActivity'
WHERE Name='AFGO_DocLine_WFActivity_v';

UPDATE AD_ReportView
SET Name='AFGO_RV_DocLineAmt'
WHERE Name='AFGO_DocLineAmt_v';

UPDATE AD_ReportView
SET Name='AFGO_RV_DocLineFact'
WHERE Name='AFGO_Fact_v';

UPDATE AD_ReportView
SET Name='AFGO_RV_SpentReport'
WHERE Name='AFGO_SpentReport_v';

UPDATE AD_ReportView
SET Name='AFGO_RV_TransferableCommitment'
WHERE Name='AFGO_TransferableCommitment_v';


--processes

UPDATE AD_Process
SET Value='AFGO_RV_BudgetReport'
WHERE Value='AFGO_BudgetReport_v';

UPDATE AD_Process
SET Value='AFGO_RV_CommitmentReport'
WHERE Value='AFGO_CommitmentReport_v';

UPDATE AD_Process
SET Value='AFGO_RV_CostReportLines'
WHERE Value='AFGO_CostReportLines_v';

UPDATE AD_Process
SET Value='AFGO_RV_CostReport'
WHERE Value='AFGO_CostReport_v';

UPDATE AD_Process
SET Value='AFGO_RV_DocLine'
WHERE Value='AFGO_DocLine_v';

UPDATE AD_Process
SET Value='AFGO_RV_PayablesPendingReport'
WHERE Value='AFGO_PayablesPendingReport_v';

UPDATE AD_Process
SET Value='AFGO_RV_SpentReport'
WHERE Value='AFGO_SpentReport_v';

UPDATE AD_Process
SET Value='AFGO_RV_TransferableCommitment'
WHERE Value='AFGO_TransferableCommitment_v';


--references

UPDATE AD_Reference
SET Name='AFGO_RV_Program'
WHERE Name='AFGO_Program_v';


-- validation rules

UPDATE AD_Val_Rule
SET Name='AFGO_RV_Program (Provider Program)',
Code='AFGO_RV_Program.AFGO_Program_ID!=@AFGO_Program_ID@'
WHERE Name='AFGO_Program_v (Provider Program)';

COMMIT;

QUIT;