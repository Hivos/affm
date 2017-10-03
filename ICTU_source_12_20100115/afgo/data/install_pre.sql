SET SERVEROUTPUT ON SIZE 50000;

DECLARE

	v_AD_Element_ID	NUMBER := 0;

	v_AD_Column_ID 	NUMBER := 0;
	
	v_EntityType	VARCHAR2(4)	:= 'AFGO';

BEGIN

	-- AD_WF_Responsible.AFGO_WorkflowRole_ID

	SELECT COALESCE
	(
		(
			SELECT e.AD_Element_ID
			FROM AD_Element e
			WHERE ColumnName='AFGO_WorkflowRole_ID'
		), 0
	)
	INTO v_AD_Element_ID
	FROM DUAL;
	
	IF v_AD_Element_ID < 1 THEN
		SELECT CurrentNext
		INTO v_AD_Element_ID
		FROM AD_Sequence
		WHERE Name='AD_Element';
		
		INSERT INTO AD_Element
		(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
		AD_Element_ID, EntityType, ColumnName, Name, PrintName)
		VALUES
		(0, 0, 'Y', sysdate, 0, sysdate, 0,
		v_AD_Element_ID, v_EntityType, 'AFGO_WorkflowRole_ID', 'Workflow Role', 'Workflfow Role');
		
		UPDATE AD_Sequence
		SET CurrentNext=CurrentNext+IncrementNo
		WHERE Name='AD_Element';
	END IF;
	
	DBMS_OUTPUT.PUT_LINE('AFGO_WorlfowRole_ID: AD_Element_ID=' || v_AD_Element_ID);
	
	SELECT COALESCE
	(
		(
			SELECT c.AD_Column_ID
			FROM AD_Table t
			INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)
			WHERE t.TableName='AD_WF_Responsible'
			AND c.ColumnName='AFGO_WorkflowRole_ID'
		), 0
	)
	INTO v_AD_Column_ID
	FROM DUAL;
	
	IF v_AD_Column_ID < 1 THEN
		SELECT CurrentNext
		FROM AD_Sequence
		WHERE Name='AD_Column';
		
		UPDATE AD_Sequence
		
	END IF;
	
	DBMS_OUTPUT.PUT_LINE('AD_Column_ID=' || v_AD_Column_ID);
	


	
	ROLLBACK;
END;

/

QUIT;