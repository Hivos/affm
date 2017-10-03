SET SERVEROUTPUT ON SIZE 50000;
SET LINESIZE 160;

DECLARE
	v_FROM_Client_ID		NUMBER 	:= 1000000;
	
	v_TO_Client_ID			NUMBER 	:= 1000001;
	
	v_Commit			CHAR(1) := 'Y';
	
	--********************************************************
	
	v_AFGO_PurchaseDomain_ID 	NUMBER;
	
	v_AFGO_PurchaseLot_ID		NUMBER;
	
	v_AFGO_LotItem_ID		NUMBER;
	
	v_M_Product_ID			NUMBER;
	
	v_C_Charge_ID			NUMBER;
	
	CURSOR CUR_Domain(p_AD_Client_ID IN NUMBER) IS
	SELECT *
	FROM AFGO_PurchaseDomain
	WHERE AD_Client_ID=p_AD_Client_ID;
	
	CURSOR CUR_Lot(p_AFGO_PurchaseDomain_ID IN NUMBER) IS
	SELECT *
	FROM AFGO_PurchaseLot
	WHERE AFGO_PurchaseDomain_ID=p_AFGO_PurchaseDomain_ID;
	
	CURSOR CUR_LotItem(p_AFGO_PurchaseLot_ID IN NUMBER) IS
	SELECT *
	FROM AFGO_LotItem
	WHERE AFGO_PurchaseLot_ID=p_AFGO_PurchaseLot_ID;
BEGIN
	FOR d IN CUR_Domain(v_FROM_Client_ID) LOOP
		DBMS_OUTPUT.PUT_LINE(d.Name);
		
		SELECT CurrentNext
		INTO v_AFGO_PurchaseDomain_ID
		FROM AD_Sequence
		WHERE Name='AFGO_PurchaseDomain'
		FOR UPDATE OF CurrentNext;
		
		INSERT INTO AFGO_PurchaseDomain
		(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
		AFGO_PurchaseDomain_ID, Name, Description)
		VALUES
		(v_TO_Client_ID, 0, 'Y', Sysdate, 0, Sysdate, 0,
		v_AFGO_PurchaseDomain_ID, d.Name, d.Description);
		
		UPDATE AD_Sequence
		SET CurrentNext = CurrentNext + 1
		WHERE Name='AFGO_PurchaseDomain';
		
		FOR l IN CUR_Lot(d.AFGO_PurchaseDomain_ID) LOOP
			DBMS_OUTPUT.PUT_LINE('->' || l.Name);
			
			SELECT CurrentNext
			INTO v_AFGO_PurchaseLot_ID
			FROM AD_Sequence
			WHERE Name='AFGO_PurchaseLot'
			FOR UPDATE OF CurrentNext;
			
			INSERT INTO AFGO_PurchaseLot
			(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
			AFGO_PurchaseDomain_ID, AFGO_PurchaseLot_ID, Name, Description)
			VALUES
			(v_TO_Client_ID, 0, 'Y', Sysdate, 0, Sysdate, 0,
			v_AFGO_PurchaseDomain_ID, v_AFGO_PurchaseLot_ID, l.Name, l.Description);
			
			UPDATE AD_Sequence
			SET CurrentNext = CurrentNext + 1
			WHERE Name='AFGO_PurchaseLot';
			
			FOR i IN CUR_LotItem(l.AFGO_PurchaseLot_ID) LOOP
				DBMS_OUTPUT.PUT_LINE('-->' || i.Name);
				
				IF i.M_Product_ID IS NOT NULL THEN
					SELECT pTO.M_Product_ID
					INTO v_M_Product_ID
					FROM M_Product pFROM
					LEFT OUTER JOIN M_Product pTO ON (pTO.Value=pFROM.Value)
					WHERE pFROM.M_Product_ID=i.M_Product_ID
					AND pTO.AD_Client_ID=v_TO_Client_ID
					AND pTO.AD_Org_ID=0;
				ELSE
					v_M_Product_ID := NULL;
				END IF;
				
				IF i.C_Charge_ID IS NOT NULL THEN
					SELECT cTO.C_Charge_ID
					INTO v_C_CHarge_ID
					FROM C_Charge cFROM
					LEFT OUTER JOIN C_Charge cTO ON (cTO.Name=cFROM.Name)
					WHERE cFROM.C_Charge_ID=i.C_Charge_ID
					AND cTO.AD_Client_ID=v_TO_Client_ID
					AND cTO.AD_Org_ID=0;
				ELSE
					v_C_Charge_ID := NULL;
				END IF;
				
				SELECT CurrentNext
				INTO v_AFGO_LotItem_ID
				FROM AD_Sequence
				WHERE Name='AFGO_LotItem'
				FOR UPDATE OF CurrentNext;
				
				INSERT INTO AFGO_LotItem
				(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
				AFGO_PurchaseLot_ID, AFGO_LotItem_ID, Name, Description,
				M_Product_ID, C_Charge_ID)
				VALUES
				(v_TO_Client_ID, 0, 'Y', Sysdate, 0, Sysdate, 0,
				v_AFGO_PurchaseLot_ID, v_AFGO_LotItem_ID, i.Name, i.Description,
				v_M_Product_ID, v_C_Charge_ID);
				
				UPDATE AD_Sequence
				SET CurrentNext = CurrentNext + 1
				WHERE Name='AFGO_LotItem';
				
			END LOOP;
		END LOOP;
	END LOOP;
	
	IF v_Commit = 'Y' THEN 
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;
	ROLLBACK;
END;
/