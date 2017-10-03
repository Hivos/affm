SET SERVEROUTPUT ON SIZE 50000;
SET LINESIZE 160;


DECLARE
	@@version_info.sql
BEGIN
	DBMS_OUTPUT.PUT_LINE('-- VERSION_INFO: ' || '0_2_8');
	
	--1211
	listModification(1001329);
	
	--1209
	listModification(1001327);
	
	--1199
	listModification(1001317);
	
	--1197
	listModification(1001315);
	
	--1195
	listModification(1001313);
	
	--1192
	listModification(1001310);
	
	--1176
	listModification(1001294);
	
	--1129
	listModification(1001247);
	
	--1130
	listModification(1001248);
	
	--1131
	listModification(1001249);
	
	--1191
	listModification(1001309);
	
	--1166
	listModification(1001284);
	
	--1165
	listModification(1001283);
	
	--1159
	listModification(1001277);
	
	--1135
	listModification(1001253);
	
	--1133
	listModification(1001251);
	
	--1212
	listModification(1001330);
	
	--1170
	listModification(1001288);
END;
/
QUIT;