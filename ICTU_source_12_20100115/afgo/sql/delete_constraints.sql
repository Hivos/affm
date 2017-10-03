DELETE
FROM AD_IndexColumn
WHERE AD_TableIndex_ID IN
(
	SELECT AD_TableIndex_ID
	FROM AD_TableIndex
	WHERE EntityType='AFGO'
);

DELETE
FROM AD_TableIndex
WHERE EntityType='AFGO';

COMMIT;

QUIT;
