SELECT '1_2_11_1' AS Version
FROM DUAL;

UPDATE AD_ViewColumn
SET ColumnSQL='CASE
WHEN pct.IsAdditionalCommitment = ''Y'' AND mpct.IsPurchaseDomain IS NOT NULL
THEN mpct.IsPurchaseDomain
ELSE pct.IsPurchaseDomain
END'
WHERE ColumnSQL LIKE '%COALESCE%'
AND AD_ViewComponent_ID IN
(
	SELECT vc.AD_ViewComponent_ID
	FROM AD_ViewComponent vc
	INNER JOIN AD_Table t ON (t.AD_Table_ID=vc.AD_Table_ID)
	WHERE vc.Name='AFGO_PurchaseCommitment'
	AND t.TableName='AFGO_RV_PurchaseCommitmentType'
);