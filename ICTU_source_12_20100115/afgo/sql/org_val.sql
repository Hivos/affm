-- AD_Val_Rule for AD_Org_ID => AD_Org Security Validation
UPDATE AD_Column
SET AD_Val_Rule_ID=104
WHERE EntityType='AFGO'
AND AD_Val_Rule_ID=117;

-- AD_Val_Rule for AD_Org_ID (transaction tables) => AD_Org Trx Security Validation
UPDATE AD_Column
SET AD_Val_Rule_ID=130
WHERE EntityType='AFGO'
AND AD_Val_Rule_ID=104
AND AD_Table_ID IN
(
	SELECT AD_Table_ID
	FROM AD_Table
	WHERE EntityType='AFGO'
	AND TableName IN
	(
		'AFGO_FundScheduleLine',
		'AFGO_FundSchedule',
		'AFGO_Fund',
		'AFGO_FundLine',
		'AFGO_Budget',
		'AFGO_BudgetLine',
		'AFGO_PurchaseCommitment',
		'AFGO_PurchaseCommitmentLine',
		'AFGO_QuotationRequest',
		'AFGO_QuotationRequestLine',
		'AFGO_QuotationResponse',
		'AFGO_QuotationResponseLine',
		'AFGO_QuotationResponseScore'
	)
);

COMMIT;
