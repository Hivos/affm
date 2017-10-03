UPDATE AFGO_BudgetLine
SET LineNetAmt=Amount;
ALTER TABLE AFGO_BudgetLine
DROP COLUMN Amount;

UPDATE AFGO_PurchaseCommitmentLine
SET LineNetAmt=Amount;
ALTER TABLE AFGO_BudgetLine
DROP COLUMN Amount;

UPDATE AFGO_FundLine
SET LineNetAmt=Amount;
ALTER TABLE AFGO_FundLine
DROP COLUMN Amount;

UPDATE AFGO_FundScheduleLine
SET LineNEtAmt=Amount;
ALTER TABLE AFGO_FundScheduleLine
DROP COLUMN AMOUNT;

UPDATE AFGO_QuotationResponseLine
SET LineNetAmt=Amount;
ALTER TABLE AFGO_QuotationResponseLine
DROP COLUMN Amount;

UPDATE AFGO_QuotationRequestLine
SET LineNetAmt=Amount;
ALTER TABLE AFGO_QuotationRequestLine
DROP COLUMN Amount;