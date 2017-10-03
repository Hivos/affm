SET SERVEROUTPUT ON SIZE 50000;
SET LINESIZE 160;

DECLARE
	v_FROM_Client_ID 	NUMBER := 1000000;
	
	v_TO_Client_ID 		NUMBER := 1000001;
	
	v_TO_TaxCategory_ID	NUMBER := 1000001;
	
	v_Commit		CHAR(1) := 'Y';
	
	--********************************************************
	
	v_FROM_AcctSchema_ID	NUMBER;
	
	v_FROM_Element_ID	NUMBER;
	
	v_TO_AcctSchema_ID	NUMBER;
	
	v_TO_Element_ID		NUMBER;
	
	v_C_Charge_ID		NUMBER;
	
	v_FROM_Expense_Acct_ID	NUMBER;
	
	v_FROM_Revenue_Acct_ID	NUMBER;
	
	v_TO_Expense_Acct_ID	NUMBER;
	
	v_TO_Revenue_Acct_ID	NUMBER;
	
	CURSOR CUR_Charges(p_AD_Client_ID IN NUMBER) IS
	SELECT c.*
	FROM C_Charge c
	WHERE c.AD_Client_ID=p_AD_Client_ID
	AND AD_Org_ID=0;
	
	PROCEDURE mapValidCombination
	(
		p_FROM_ValidCombination_ID IN NUMBER,
		p_TO_ValidCombination_ID OUT NUMBER
	) 
	IS
		v_ElementValue		NVARCHAR2(40);
		
		v_C_ElementValue_ID	 NUMBER;
			
		v_Alias 		NVARCHAR2(40);
			
		v_Combination 		NVARCHAR2(60);
		
		v_Description 		NVARCHAR2(255);
		
		v_IsFullyQualified	CHAR(1);
	BEGIN
		p_TO_ValidCombination_ID := NULL;
		
		SELECT vcTO.C_ValidCombination_ID, evFROM.Value, evTO.C_ElementValue_ID, vcFROM.Alias, vcFROM.Combination, vcFROM.Description, vcFROM.IsFullyQualified
		INTO p_TO_ValidCombination_ID, v_ElementValue, v_C_ElementValue_ID, v_Alias, v_Combination, v_Description, v_IsFullyQualified
		FROM C_ValidCombination vcFROM
		INNER JOIN C_ElementValue evFROM ON (evFROM.C_ElementValue_ID=vcFROM.Account_ID)
		LEFT OUTER JOIN C_ElementValue evTO ON (evTO.Value=evFROM.Value)
		LEFT OUTER JOIN C_ValidCombination vcTO ON (vcTO.Account_ID=evTO.C_ElementValue_ID)
		WHERE vcFROM.C_ValidCombination_ID=p_FROM_ValidCombination_ID
		AND evFROM.C_Element_ID=v_FROM_Element_ID
		AND evTO.C_Element_ID=v_TO_Element_ID;
		
		IF (p_TO_ValidCombination_ID IS NULL) THEN
			SELECT CurrentNext
			INTO p_TO_ValidCombination_ID
			FROM AD_Sequence
			WHERE Name='C_ValidCombination'
			FOR UPDATE OF CurrentNext;
			
			INSERT INTO C_ValidCombination
			(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
			C_ValidCombination_ID, C_AcctSchema_ID, Account_ID, Alias, Combination, Description, IsFullyQualified)
			VALUES
			(v_TO_Client_ID, 0, 'Y', Sysdate, 0, Sysdate, 0,
			p_TO_ValidCombination_ID, v_TO_AcctSchema_ID, v_C_ElementValue_ID, v_Alias, v_Combination, v_Description, v_IsFullyQualified);
			
			UPDATE AD_Sequence
			SET CurrentNext = CurrentNext + 1
			WHERE Name='C_ValidCombination';
		END IF;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				DBMS_OUTPUT.PUT_LINE('==>FROM_ValidCombination_ID=' || p_FROM_ValidCombination_ID || ': NoSuchElementValue: ' || v_ElementValue || ', FROM_Element_ID=' || v_FROM_Element_ID || ', TO_Element_ID=' || v_TO_Element_ID);

	END;
	
BEGIN 
	-- FROM
	SELECT a.C_AcctSchema_ID, e.C_Element_ID
	INTO v_FROM_AcctSchema_ID, v_FROM_Element_ID
	FROM AD_ClientInfo ci
	INNER JOIN C_AcctSchema a ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)
	INNER JOIN C_AcctSchema_Element ase ON (ase.C_AcctSchema_ID=a.C_AcctSchema_ID)
	INNER JOIN C_Element e ON (e.C_Element_ID=ase.C_Element_ID)
	WHERE ci.AD_Client_ID=v_FROM_Client_ID
	AND e.ElementType='A';
	
	-- TO
	SELECT a.C_AcctSchema_ID, e.C_Element_ID
	INTO v_TO_AcctSchema_ID, v_TO_Element_ID
	FROM AD_ClientInfo ci
	INNER JOIN C_AcctSchema a ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)
	INNER JOIN C_AcctSchema_Element ase ON (ase.C_AcctSchema_ID=a.C_AcctSchema_ID)
	INNER JOIN C_Element e ON (e.C_Element_ID=ase.C_Element_ID)
	WHERE ci.AD_Client_ID=v_TO_Client_ID
	AND e.ElementType='A';
	

	FOR c IN CUR_Charges(v_FROM_Client_ID) LOOP
		DBMS_OUTPUT.PUT_LINE(c.NAME);
		
		SELECT CurrentNext
		INTO v_C_Charge_ID
		FROM AD_Sequence
		WHERE Name='C_Charge'
		FOR UPDATE OF CurrentNext;
		
		INSERT INTO C_Charge
		(AD_Client_ID, AD_Org_ID, C_Charge_ID, C_TaxCategory_ID, ChargeAmt, 
		Created, CreatedBy, Updated, UpdatedBy, IsActive,
		Name, Description, IsSameCurrency, IsSameTax, IsTaxIncluded)
		VALUES
		(v_TO_Client_ID, 0, v_C_Charge_ID, v_TO_TaxCategory_ID, c.ChargeAmt,
		Sysdate, 0, Sysdate, 0, c.IsActive, 
		c.Name, c.Description, c.IsSameCurrency, c.IsSameTax, c.IsTaxIncluded);
		
		UPDATE AD_Sequence
		SET CurrentNext = CurrentNext + 1
		WHERE Name='C_Charge';
		
		SELECT ca.CH_Expense_Acct, ca.CH_Revenue_Acct
		INTO v_FROM_Expense_Acct_ID, v_FROM_Revenue_Acct_ID
		FROM C_Charge_Acct ca
		WHERE ca.C_Charge_ID=c.C_Charge_ID
		AND ca.C_AcctSchema_ID=v_FROM_AcctSchema_ID;
		
		mapValidCombination(v_FROM_Expense_Acct_ID, v_TO_Expense_Acct_ID);
		mapValidCombination(v_FROM_Revenue_Acct_ID, v_TO_Revenue_Acct_ID);
		
		if (v_TO_Expense_Acct_ID > 0 AND v_TO_Revenue_Acct_ID > 0) THEN
			INSERT INTO C_Charge_Acct
			(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
			C_Charge_ID, C_AcctSchema_ID, CH_Expense_Acct, CH_Revenue_Acct)
			VALUES
			(v_TO_Client_ID, 0, 'Y', Sysdate, 0, Sysdate, 0,
			v_C_Charge_ID, v_TO_AcctSchema_ID, v_TO_Expense_Acct_ID, v_TO_Revenue_Acct_ID);
		END IF;
	
	END LOOP;

	IF (v_Commit = 'Y') THEN
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;
END;
/