UPDATE AD_WF_Node
SET EMail='fa@ictu.nl'
WHERE Value='(MailAfterComplete)'
AND EMAil!='fa@ictu.nl'
AND AD_Workflow_ID IN
(
	SELECT AD_Workflow_ID
	FROM AD_Workflow
	WHERE EntityType='AFGO'
	AND Value='Process_PurchaseCommitment'
);

UPDATE C_DocType
SET AFGO_PurchaseCommitmentType_ID=
(
	SELECT AFGO_PurchaseCommitmentType_ID
	FROM AFGO_PurchaseCommitmentType
	WHERE Name='Transfered Commitment'
)
WHERE PurchaseCommitmentType='TR'
AND AFGO_PurchaseCommitmentType_ID IS NULL;

UPDATE C_DocType
SET AFGO_PurchaseCommitmentType_ID=
(
	SELECT AFGO_PurchaseCommitmentType_ID
	FROM AFGO_PurchaseCommitmentType
	WHERE Name='Specified Commitment'
)
WHERE PurchaseCommitmentType='SP'
AND AFGO_PurchaseCommitmentType_ID IS NULL;

UPDATE C_DocType
SET AFGO_PurchaseCommitmentType_ID=
(
	SELECT AFGO_PurchaseCommitmentType_ID
	FROM AFGO_PurchaseCommitmentType
	WHERE Name='Additional Commitment'
)
WHERE PurchaseCommitmentType='AC'
AND AFGO_PurchaseCommitmentType_ID IS NULL;

UPDATE C_DocType
SET AFGO_PurchaseCommitmentType_ID=
(
	SELECT AFGO_PurchaseCommitmentType_ID
	FROM AFGO_PurchaseCommitmentType
	WHERE Name='Service Commitment'
)
WHERE PurchaseCommitmentType='SE'
AND AFGO_PurchaseCommitmentType_ID IS NULL;


UPDATE C_DocType
SET AFGO_PurchaseCommitmentType_ID=
(
	SELECT AFGO_PurchaseCommitmentType_ID
	FROM AFGO_PurchaseCommitmentType
	WHERE Name='Secondment Commitment'
)
WHERE PurchaseCommitmentType='SC'
AND AFGO_PurchaseCommitmentType_ID IS NULL;


COMMIT;
QUIT;