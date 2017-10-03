SELECT '1_2_11_4' AS Version
FROM DUAL;


UPDATE AFGO_PurchaseCommitment pc
SET pc.AFGO_PurchaseDomain_ID=(SELECT AFGO_PurchaseDomain_ID FROM AFGO_PurchaseCommitment mpc WHERE mpc.AFGO_PurchaseCommitment_ID=pc.MasterPurchaseCommitment_ID),
pc.AFGO_PurchaseLot_ID=(SELECT AFGO_PurchaseLot_ID FROM AFGO_PurchaseCommitment mpc WHERE mpc.AFGO_PurchaseCommitment_ID=pc.MasterPurchaseCommitment_ID)
WHERE pc.AFGO_PurchaseCommitment_ID IN
(
	SELECT pc.AFGO_PurchaseCommitment_ID
	FROM AFGO_PurchaseCommitment pc
	INNER JOIN AFGO_PurchaseCommitment mpc ON (mpc.AFGO_PurchaseCommitment_ID=pc.MasterPurchaseCommitment_ID)
	WHERE (pc.AFGO_PurchaseDomain_ID IS NULL AND mpc.AFGO_PurchaseDomain_ID IS NOT NULL)
	OR (pc.AFGO_PurchaseDomain_ID IS NOT NULL AND mpc.AFGO_PurchaseDomain_ID IS NULL)
	OR (pc.AFGO_PurchaseDomain_ID IS NOT NULL AND mpc.AFGO_PurchaseDomain_ID IS NOT NULL AND pc.AFGO_PurchaseDomain_ID!=mpc.AFGO_PurchaseDomain_ID)
	OR (pc.AFGO_PurchaseLot_ID IS NULL AND mpc.AFGO_PurchaseLot_ID IS NOT NULL)
	OR (pc.AFGO_PurchaseLot_ID IS NOT NULL AND mpc.AFGO_PurchaseLot_ID IS NULL)
	OR (pc.AFGO_PurchaseLot_ID IS NOT NULL AND mpc.AFGO_PurchaseLot_ID IS NOT NULL AND pc.AFGO_PurchaseLot_ID!=mpc.AFGO_PurchaseLot_ID)
);

UPDATE AD_Field
SET DisplayLogic='@IsPurchaseDomain@=Y & @IsMasterCommitment@=Y'
WHERE DisplayLogic != '@IsPurchaseDomain@=Y & @IsMasterCommitment@=Y'
AND (AD_Tab_ID, AD_Column_ID) IN
(
	SELECT tab.AD_Tab_ID, c.AD_Column_ID
	FROM AD_Tab tab
	INNER JOIN AD_Window w ON (w.AD_Window_ID=tab.AD_Window_ID)
	INNER JOIN AD_Table tbl ON (tbl.AD_Table_ID=tab.AD_Table_ID AND tbl.AD_Window_ID=w.AD_Window_ID)
	INNER JOIN AD_Column c ON (c.AD_Table_ID=tbl.AD_Table_ID)
	WHERE tbl.TableName IN ('AFGO_PurchaseCommitment')
	AND c.ColumnName IN ('AFGO_PurchaseDomain_ID', 'AFGO_PurchaseLot_ID')
);


