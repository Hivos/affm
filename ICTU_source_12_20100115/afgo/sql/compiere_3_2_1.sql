--DDL

-- AD_Process_Para.IsSelectionOnly
ALTER TABLE AD_Process_Para
ADD IsSelectionOnly CHAR(1) DEFAULT 'N' NOT NULL;


--DML

-- AD_Note.Processed
UPDATE AD_Column
SET IsUpdateable='Y'
WHERE AD_Column_ID=5949
AND IsUpdateable='N';

-- Payment Batch: beta functionality
UPDATE AD_Window
SET IsBetaFunctionality='N'
WHERE AD_Window_ID=303
AND IsBEtaFunctionality='Y';

-- C_BankStatementLine.C_Invoice_ID (only completed/closed)
UPDATE AD_Column
SET AD_Val_Rule_ID=220
WHERE AD_Column_ID=10779
AND AD_Val_Rule_ID!=220;

-- AD_Process_Para
UPDATE AD_Process_Para
SET IsSelectionOnly='Y'
WHERE AD_Process_Para_ID IN
(
	SELECT pp.AD_Process_Para_ID
	FROM AD_Process p
	INNER JOIN AD_Process_Para pp ON (pp.AD_Process_ID=p.AD_Process_ID)
	WHERE p.EntityType='AFGO'
	AND pp.IsSelectionOnly='N'
	AND pp.EntityType='AFGO'
	AND (p.Value, pp.ColumnName) IN
	(
		('AFGO_RV_BudgetReport', 'DateAcct'),
		('AFGO_RV_BudgetReport', 'AFGO_AccountRestriction_ID'),
		('AFGO_RV_CostReportLines', 'DateAcct'),
		('AFGO_RV_CostReportLines', 'AFGO_AccountRestriction_ID'),
		('AFGO_CostReport_t', 'DateAcct'),
		('AFGO_CostReport_t', 'AFGO_AccountRestriction_ID'),
		('AFGO_RV_CostReport', 'DateAcct'),
		('AFGO_RV_CostReport', 'AFGO_AccountRestriction_ID'),
		('AFGO_RV_PayablesPendingReport', 'DateAcct'),
		('AFGO_RV_PayablesPendingReport', 'AFGO_AccountRestriction_ID'),
		('AFGO_RV_SpentReport', 'DateAcct'),
		('AFGO_RV_SpentReport', 'AFGO_AccountRestriction_ID')
	)
);


COMMIT;

QUIT;

