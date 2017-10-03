SET SERVEROUTPUT ON SIZE 50000;

ALTER TABLE AD_IndexColumn
ADD EntityType CHAR(4);

DECLARE

	v_EntityType CHAR(4) := 'AFGO';
	
	v_ColClient NVARCHAR2(60) := 'AD_Client_ID';
	
	v_ColOrg NVARCHAR2(60) := 'AD_Org_ID';
	
	v_ColProgram NVARCHAR2(60) := 'AFGO_Program_ID';
	
	v_ColProjectCluster NVARCHAR2(60) := 'AFGO_ProjectCluster_ID';
	
	v_ColProject NVARCHAR2(60) := 'AFGO_Project_ID';
	
	v_ColPhase NVARCHAR2(60) := 'AFGO_Phase_ID';
	
	v_ColActivity NVARCHAR2(60) := 'AFGO_Activity_ID';
	
	v_ColName NVARCHAR2(60) := 'Name';
	
	v_ColDocumentNo NVARCHAR2(60) := 'DocumentNo';
	
	v_ColDocNo NVARCHAR2(60) := 'DocNo';
	
	--***************************************************
	
	v_AD_TableIndex_ID NUMBER;
	
	v_AD_Table_ID NUMBER;
	
	CURSOR CUR_DocTables IS
	SELECT t.TableName, t.TableName || '_' || v_ColDocNo AS IndexName
	FROM AD_Table t
	INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID)
	WHERE t.EntityType=v_EntityType
	AND c.ColumnName=v_ColDocumentNo
	AND IsView='N'
	AND t.TableName NOT LIKE '%T_%';
	
	
	PROCEDURE createIndex
	(
		p_TableName IN NVARCHAR2,
		p_IndexName IN NVARCHAR2,
		p_AD_TableIndex_ID OUT NUMBER
	)
	IS
		v_AD_Table_ID NUMBER;
	BEGIN
		SELECT AD_Table_ID
		INTO v_AD_Table_ID
		FROM AD_Table
		WHERE TableName=p_TableName;
		 
		SELECT COALESCE((SELECT AD_TableIndex_ID FROM AD_TableIndex WHERE AD_Table_ID=v_AD_Table_ID AND Name=p_IndexName), -1)
		INTO p_AD_TableIndex_ID
		FROM DUAL;
		
		IF p_AD_TableIndex_ID > 0 THEN
			DBMS_OUTPUT.PUT_LINE('EXISTING INDEX: ' || p_IndexName);
			RETURN;
		END IF;
		
			SELECT CurrentNext
			INTO p_AD_TableIndex_ID
			FROM AD_Sequence
			WHERE Name='AD_TableIndex'
			FOR UPDATE OF CurrentNext;
			
			INSERT INTO AD_TableIndex
			(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
			AD_TableIndex_ID, AD_Table_ID, EntityType, Name, IsCreateConstraint, IsUnique)
			VALUES 
			(0, 0, 'Y', sysdate, 0, sysdate, 0,
			p_AD_TableIndex_ID, v_AD_Table_ID, v_EntityType, p_IndexName, 'Y', 'Y');
			
			UPDATE AD_Sequence
			SET CurrentNext=CurrentNext+1
			WHERE Name='AD_TableIndex';
			
			DBMS_OUTPUT.PUT_LINE('INDEX CREATED: ' || p_IndexName);
	END createIndex;
	
	PROCEDURE addIndexColumn
	(
		p_AD_TableIndex_ID IN NUMBER,
		p_ColumnName IN NVARCHAR2
	)
	IS
		v_AD_Table_ID NUMBER;
		v_AD_Column_ID NUMBER;
		v_AD_IndexColumn_ID NUMBER;
		v_SeqNo NUMBER;
	BEGIN
		SELECT ti.AD_Table_ID
		INTO v_AD_Table_ID
		FROM AD_TableIndex ti
		WHERE ti.AD_TableIndex_ID=p_AD_TableIndex_ID;
		
		SELECT c.AD_Column_ID
		INTO v_AD_Column_ID
		FROM AD_Column c
		WHERE c.AD_Table_ID=v_AD_Table_ID
		AND c.ColumnName=p_ColumnName;
		
		SELECT COALESCE
		(
			(
				SELECT ic.AD_Column_ID
				FROM AD_IndexColumn ic
				WHERE ic.AD_TableIndex_ID=p_AD_TableIndex_ID
				AND ic.AD_Column_ID=v_AD_Column_ID
			),
			-1
		)
		INTO v_AD_IndexColumn_ID
		FROM DUAL;
		
		IF v_AD_IndexColumn_ID > 0 THEN
			DBMS_OUTPUT.PUT_LINE('EXISTING INDEX COLUMN=' || p_ColumnName);
			RETURN;
		END IF;
		
		SELECT CurrentNext
		INTO v_AD_IndexColumn_ID
		FROM AD_Sequence
		WHERE Name='AD_IndexColumn'
		FOR UPDATE OF CurrentNext;
		
		SELECT COALESCE
		(
			(
				SELECT MAX(SeqNo)
				FROM AD_IndexColumn
				WHERE AD_TableIndex_ID=p_AD_TableIndex_ID
			),
			0
		) + 10
		INTO v_SeqNo
		FROM DUAL;

		INSERT INTO AD_IndexColumn
		(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
		AD_TableIndex_ID, AD_IndexColumn_ID, SeqNo, AD_Column_ID)
		VALUES
		(0, 0, 'Y', sysdate, 0, sysdate, 0,
		p_AD_TableIndex_ID, v_AD_IndexColumn_ID, v_SeqNo, v_AD_Column_ID);
		
		UPDATE AD_Sequence
		SET CurrentNext=CurrentNext+1
		WHERE Name='AD_IndexColumn';
		
		DBMS_OUTPUT.PUT_LINE('NEW INDEX COLUMN=' || p_ColumnName);
		
	END addIndexColumn;
	
BEGIN
	DBMS_OUTPUT.PUT_LINE('--');
		
	-- AFGO_Program_Name
	DBMS_OUTPUT.PUT_LINE('--');
	createIndex('AFGO_Program', 'AFGO_Program_Name', v_AD_TableIndex_ID);
	addIndexColumn(v_AD_TableIndex_ID, v_ColClient);
	addIndexColumn(v_AD_TableIndex_ID, v_ColName);
	
	-- AFGO_ProjectCluster_Name
	DBMS_OUTPUT.PUT_LINE('--');
	createIndex('AFGO_ProjectCluster', 'AFGO_ProjectCluster_Name', v_AD_TableIndex_ID);
	addIndexColumn(v_AD_TableIndex_ID, v_ColProgram);
	addIndexColumn(v_AD_TableIndex_ID, v_ColName);
	
	-- AFGO_Project_Name
	DBMS_OUTPUT.PUT_LINE('--');
	createIndex('AFGO_Project', 'AFGO_Project_Name', v_AD_TableIndex_ID);
	addIndexColumn(v_AD_TableIndex_ID, v_ColProjectCluster);
	addIndexColumn(v_AD_TableIndex_ID, v_ColName);
	
	-- AFGO_Phase_Name
	DBMS_OUTPUT.PUT_LINE('--');
	createIndex('AFGO_Phase', 'AFGO_Phase_Name', v_AD_TableIndex_ID);
	addIndexColumn(v_AD_TableIndex_ID, v_ColProject);
	addIndexColumn(v_AD_TableIndex_ID, v_ColName);
	
	-- AFGO_Activity_Name
	DBMS_OUTPUT.PUT_LINE('--');
	createIndex('AFGO_Activity', 'AFGO_Activity_Name', v_AD_TableIndex_ID);
	addIndexColumn(v_AD_TableIndex_ID, v_ColPhase);
	addIndexColumn(v_AD_TableIndex_ID, v_ColName);
	
	FOR dt IN CUR_DocTables LOOP
		DBMS_OUTPUT.PUT_LINE('--');
		createIndex(dt.TableName, dt.IndexName, v_AD_TableIndex_ID);
		addIndexColumn(v_AD_TableIndex_ID, v_ColClient);
		addIndexColumn(v_AD_TableIndex_ID, v_ColDocumentNo);
	END LOOP;

	COMMIT;
END;
/
QUIT;
