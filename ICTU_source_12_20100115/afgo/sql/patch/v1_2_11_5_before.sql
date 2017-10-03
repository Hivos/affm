SELECT '1_2_11_5' AS Version
FROM DUAL;

UPDATE AD_Column
SET MandatoryLogic='@IsSecondmentCommitment@=Y', ReadOnlyLogic='@IsSecondmentCommitment@!Y'
WHERE (MandatoryLogic!='@IsSecondmentCommitment@=Y' OR ReadOnlyLogic!='@IsSecondmentCommitment@!Y')
AND AD_Column_ID IN
(
	SELECT c.AD_Column_ID
	FROM AD_Table t
	INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)
	WHERE t.TableName='AFGO_PurchaseCommitment'
	AND c.ColumnName='C_BPartner_ID'
);