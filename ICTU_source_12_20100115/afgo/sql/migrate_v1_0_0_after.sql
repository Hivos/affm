UPDATE AD_Reference
SET Name='AFGO_RV_Program'
WHERE Name='AFGO_Program_v';

UPDATE AD_Val_Rule
SET Name='AFGO_RV_Program (Provider Program)',
Code='AFGO_RV_Program.AFGO_Program_ID!=@AFGO_Program_ID@'
WHERE Name='AFGO_Program_v (Provider Program)';


COMMIT;

QUIT;