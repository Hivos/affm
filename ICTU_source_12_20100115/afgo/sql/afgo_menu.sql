SET SERVEROUTPUT ON SIZE 50000;


DECLARE
	
	v_EntityType	CHAR(4) := 'AFGO';
	
	--******************
	
	v_AD_Menu_ID	NUMBER(10) := 0;
	
	v_AD_Menu_FundsManagement_ID	NUMBER(10) := 0;
	
	v_AD_Menu_Reports_ID	NUMBER(10) := 0;
	
	v_AD_Menu_ReportRules_ID	NUMBER(10) := 0;
	
	v_AD_Menu_DevReports_ID	NUMBER(10) := 0;
	
	v_AD_Menu_WF_ID	NUMBER(10) := 0;
	
	v_AD_Menu_ProgramManagement_ID	NUMBER(10) := 0;
	
	v_AD_Menu_FA_ID	NUMBER(10) := 0;
	
	v_AD_Menu_Purchase_ID	NUMBER(10) := 0;
	
	CURSOR CUR_MenuTrees IS
	SELECT tr.Name, tr.AD_Tree_ID
	FROM AD_Tree tr
	INNER JOIN AD_Table tb ON (tb.AD_Table_ID=tr.AD_Table_ID)
	WHERE tr.AD_Client_ID=0
	AND tb.TableName='AD_Menu';
	
	CURSOR CUR_Menu(pAD_Tree_ID NUMBER) IS
	SELECT m.Name
	FROM AD_Menu m
	WHERE m.EntityType=v_EntityType
	AND m.AD_Menu_ID NOT IN
	(
		SELECT Node_ID
		FROM AD_TreeNodeMM
		WHERE AD_Tree_ID=pAD_Tree_ID
	);
	
	PROCEDURE insertNode
	(
		p_AD_Tree_ID IN INTEGER,
		p_Parent_ID IN INTEGER,
		p_MenuName In NVARCHAR2,
		p_Node_ID OUT INTEGER
	)
	IS
		v_SeqNo INTEGER(10) := 10;
	BEGIN
		BEGIN
			SELECT AD_Menu_ID
			INTO p_Node_ID
			FROM AD_Menu
			WHERE EntityType=V_EntityType
			AND LOWER(Name) LIKE LOWER(p_MenuName);
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				DBMS_OUTPUT.PUT_LINE('No such menu entry: ' || p_MenuName);
				RETURN;
		END;
		
		SELECT COALESCE
		(
			(
				SELECT MAX(SeqNo) 
				FROM AD_TreeNodeMM 
				WHERE AD_Tree_ID=p_AD_Tree_ID 
				AND Parent_ID=p_Parent_ID
			), 0
		) + 10 AS SeqNo
		INTO v_SeqNo
		FROM DUAL;
		
		DBMS_OUTPUT.PUT_LINE(p_MenuName || ' => AD_Menu_ID=' || p_Node_ID || ', SeqNo=' || v_SeqNo);
		
		INSERT INTO AD_TreeNodeMM
		(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
		AD_Tree_ID, Parent_ID, Node_ID, SeqNo)
		VALUES
		(0, 0, 'Y', sysdate, 0, sysdate, 0,
		p_AD_Tree_ID, p_Parent_ID, p_Node_ID, v_SeqNo);
		
	END insertNode;
	
BEGIN

	FOR t IN CUR_MenuTrees LOOP
	
		DBMS_OUTPUT.PUT_LINE('**');
		DBMS_OUTPUT.PUT_LINE('************* MenuTree: '|| t.Name || ', AD_Tree_ID=' || t.AD_Tree_ID || ' ************* ');

		DELETE
		FROM AD_TreeNodeMM
		WHERE AD_Tree_ID=t.AD_Tree_ID
		AND Node_ID IN
		(
			SELECT AD_Menu_ID
			FROM AD_Menu
			WHERE EntityType=v_EntityType
		);
		
		-- Funds Management
		insertNode(t.AD_Tree_ID, 0, 'Funds Management', v_AD_Menu_FundsManagement_ID);
		
		-- Reports
		insertNode(t.AD_Tree_ID, v_AD_Menu_FundsManagement_ID, 'Reports', v_AD_Menu_Reports_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Commitment Report', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Payables Pending', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Budget Report', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Spent Report', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Cost Report', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Transferable Commitment', v_AD_Menu_ID);
		
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Report Rules', v_AD_Menu_ReportRules_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ReportRules_ID, 'Delete Temporary Report Data', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ReportRules_ID, 'Account Type Restriction', v_AD_Menu_ID);
		
		insertNode(t.AD_Tree_ID, v_AD_Menu_Reports_ID, 'Development Reports', v_AD_Menu_DevReports_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_DevReports_ID, 'Cost Report (Lines)', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_DevReports_ID, 'Cost Report (Temp Table)', v_AD_Menu_ID);
		
		-- Control and Worklfow Management
		insertNode(t.AD_Tree_ID, v_AD_Menu_FundsManagement_ID, 'Control % Workflow Management', v_AD_Menu_WF_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Configure Default Workflow Roles', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Force Document Status', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Force Purchase Commitment Status', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Reset Purchase Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Complete Purchase Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Change Document Period', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Reopen Document Period', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Program Workflow Roles', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'All Document Lines', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Workflow Role', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Resolve Document Workflow Responsible', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_WF_ID, 'Pending Workflow Document', v_AD_Menu_ID);
		
		-- Program Management
		insertNode(t.AD_Tree_ID, v_AD_Menu_FundsManagement_ID, 'Program Management', v_AD_Menu_ProgramManagement_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Program (all)', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Internal Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Service Type', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Budget', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Program Maintainance', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Project Maintainance', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_ProgramManagement_ID, 'Funding', v_AD_Menu_ID);
		
		-- Financial Management
		insertNode(t.AD_Tree_ID, v_AD_Menu_FundsManagement_ID, 'Financial Management', v_AD_Menu_FA_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Transfer Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Transfer Commitment (Batch)', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Generate Funding Invoices', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Create Funding Schedule', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Cost Allocation', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Cost Distribution', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Fund Schedule Type', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Program Calendar', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Fund Schedule', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_FA_ID, 'Invoice (Customer)', v_AD_Menu_ID);
		
		-- Purchase Management
		insertNode(t.AD_Tree_ID, v_AD_Menu_FundsManagement_ID, 'Purchase Management', v_AD_Menu_Purchase_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Close Purchase Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Invoice Line Allocation', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Invoice (Vendor)', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Quotation Response', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Purchase Domain', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Purchase Commitment', v_AD_Menu_ID);
		insertNode(t.AD_Tree_ID, v_AD_Menu_Purchase_ID, 'Quotation Request', v_AD_Menu_ID);
		
		FOR m IN CUR_MENU(t.AD_Tree_ID) LOOP
			DBMS_OUTPUT.PUT_LINE('==> No TreeNode: ' || m.Name);
		END LOOP;
	
	END LOOP;
END;
/

COMMIT;

QUIT;