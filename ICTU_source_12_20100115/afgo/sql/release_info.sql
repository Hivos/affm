
v_ModificationType_BugFix NUMBER := 9;

PROCEDURE insertModification
(
	p_EntityType IN VARCHAR2,
	p_Version NVARCHAR2,
	p_ModificationType 
	p_Name IN NVARCHAR2,
	p_Comment IN NVARCHAR2
)
IS
	v_AD_Version_ID NUMBER;
	
	v_AD_Modification_ID NUMBER;
	
BEGIN

	SELECT COALESCE
	(
		(
			SELECT AD_Version_ID
			FROM AD_Version
			WHERE EntityType=p_EntityType
			AND Value=p_Version
		),
		-1
	)
	INTO v_AD_Version_ID
	FROM DUAL;
	
	IF (v_AD_Version_ID < 1) THEN
		DBMS_OUTPUT.PUT_LINE('No such version: ' || p_Version);
		RETURN;
	END IF;
	
	SELECT COALESCE
	(
		(
			SELECT AD_Modification_ID
			FROM AD_Modification
			WHERE AD_Version_ID=v_AD_Version_ID
			AND Name=p_Name
		),
		0
	)
	INTO v_AD_Modification_ID
	FROM DUAl;
	
	IF (v_AD_Modification_ID > 0) THEN
		DBMS_OUTPUT.PUT_LINE('Existing modification: ' || p_Name);
		RETURN;
	END IF;
	
	SELECT CurrentNext
	INTO v_AD_Modification_ID
	FROM AD_Sequence
	WHERE Name='AD_Modification'
	FOR UPDATE OF CurrentNext;
	
	DBMS_OUTPUT.PUT_LINE('EntityType=' || p_EntityType);
	
	INSERT INTO AD_Modification
	(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
	AD_Version_ID, ModificationType, Name, Comment);
	VALUES
	(0, 0, 'Y', sysdate, 0, sysdate, 0,
	v_AD_Version_ID, );
	
	UPDATE AD_Sequence
	SET CurrentNext=CurrentNext+1
	WHERE Name='AD_Modification';
	
END insertModification;