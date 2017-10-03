SET LINESIZE ON 160;

SELECT vcTO.C_ValidCombination_ID, evFROM.Value, evTO.C_ElementValue_ID
FROM C_ValidCombination vcFROM
INNER JOIN C_ElementValue evFROM ON (evFROM.C_ElementValue_ID=vcFROM.Account_ID)
LEFT OUTER JOIN C_ElementValue evTO ON (evTO.Value=evFROM.Value)
LEFT OUTER JOIN C_ValidCombination vcTO ON (vcTO.Account_ID=evTO.C_ElementValue_ID)
WHERE vcFROM.C_ValidCombination_ID=1000217
AND evFROM.C_Element_ID=1000000
AND evTO.C_Element_ID=1000001;