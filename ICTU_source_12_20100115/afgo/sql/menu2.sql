SET SERVEROUTPUT ON SIZE 50000;

DECLARE
	vEntityType		CHAR(4) := 'AFGO';
	
	vFromAD_Tree_ID		NUMBER	:= 10;
	
	vToAD_Tree_ID		NUMBER	:= 121;
BEGIN

	DELETE
	FROM AD_TreeNodeMM
	WHERE AD_Tree_ID=vToAD_Tree_ID
	AND Node_ID IN
	(
		SELECT AD_Menu_ID
		FROM AD_Menu
		WHERE EntityType=vEntityType
	);
	


	ROLLBACK;
END;
/