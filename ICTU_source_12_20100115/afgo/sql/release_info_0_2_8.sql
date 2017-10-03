SET SERVEROUTPUT ON SIZE 50000;
SET LINESIZE 160;

DECLARE
	v_EntityType CHAR(4) := 'AFGO';
	
	@@release_info.sql
	
BEGIN
	insertModification(v_EntityType, '0.2.8', 'name', 'comment');
	
	ROLLBACK;
END;
/
QUIT;