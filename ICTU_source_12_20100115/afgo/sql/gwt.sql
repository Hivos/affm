SET SERVEROUTPUT ON SIZE 50000;

DECLARE

	v_EntityType	CHAR(4) := 'AFGO';

BEGIN

	UPDATE AD_Column
	SET IsMandatoryUI=IsMandatory
	WHERE EntityType=v_EntityType;


	COMMIT;
END;
/