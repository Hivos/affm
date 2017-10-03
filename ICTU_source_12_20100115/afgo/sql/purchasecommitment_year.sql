UPDATE AFGO_PurchaseCommitmentLine
SET AFGO_Year_ID=
(
	SELECT AFGO_Year_ID
	FROM AFGO_Quarter
	WHERE AFGO_Quarter_ID=AFGO_PurchaseCommitmentLine.AFGO_Quarter_ID
)
WHERE AFGO_Year_ID IS NULL;

COMMIT;

QUIT;