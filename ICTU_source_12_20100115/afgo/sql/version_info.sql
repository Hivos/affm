
PROCEDURE listModification
(
	p_R_Request_ID IN NUMBER
)
IS
	CURSOR CUR_Request(pp_R_Request_ID NUMBER) IS
	SELECT r.R_Request_ID, r.DocumentNo, r.Summary
	FROM R_Request r
	WHERE r.R_Request_ID=pp_R_Request_ID;
BEGIN

	FOR r in CUR_Request(p_R_Request_ID) LOOP
		DBMS_OUTPUT.PUT_LINE('--**************************************************************************************');
		DBMS_OUTPUT.PUT_LINE('R_Request_ID=' || r.R_Request_ID);
		DBMS_OUTPUT.PUT_LINE('Summary=' || r.Summary);
	END LOOP; 
	
END listModification;
