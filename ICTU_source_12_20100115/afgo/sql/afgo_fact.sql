
--AFGO_Fact_v
CREATE OR REPLACE VIEW AFGO_Fact_v AS
-- Funding Schedule 
SELECT fs.AD_Client_ID, fs.AD_Org_ID, fs.Created, fs.CreatedBy, fs.Updated, fs.UpdatedBy, fs.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_FundSchedule') AS AD_Table_ID, fs.AFGO_FundSchedule_ID AS Record_ID, 
fs.DocumentNo, fsl.Line, NULL AS M_Product_ID, fl.C_Charge_ID,
fsl.AFGO_Activity_ID, fsl.AFGO_ServiceType_ID, fs.AFGO_Month_ID,
-- Funding
fsl.Amount AS  FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM AFGO_FundSchedule fs
INNER JOIN AFGO_FundScheduleLine fsl ON (fsl.AFGO_FundSchedule_ID=fs.AFGO_FundSchedule_ID)
INNER JOIN AFGO_FundLine fl ON (fl.AFGO_FundLine_ID=fsl.AFGO_FundLine_ID)
WHERE fs.DocStatus IN ('CL', 'CO')
--
-- Invoiced Funding
UNION
SELECT fsi.AD_Client_ID, fsi.AD_Org_ID, fsi.Created, fsi.CreatedBy, fsi.Updated, fsi.UpdatedBy, fsi.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice') AS AD_Table_ID, fsi.C_Invoice_ID AS Record_ID,
fsi.DocumentNo, fsil.Line, fsil.M_Product_ID, fsil.C_Charge_ID,
fsil.AFGO_Activity_ID, fsil.AFGO_ServiceType_ID, fsil.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
fsil.LineNetAmt AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM C_Invoice fsi
INNER JOIN C_InvoiceLine fsil ON (fsil.C_Invoice_ID=fsi.C_Invoice_ID)
WHERE fsi.DocStatus IN ('CL', 'CO')
AND fsi.AFGO_FundSchedule_ID IS NOT NULL
--
-- Invoice (AR)
UNION
SELECT ari.AD_Client_ID,ari.AD_Org_ID, ari.Created, ari.CreatedBy, ari.Updated, ari.UpdatedBy, ari.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice') AS AD_Table_ID, ari.C_Invoice_ID AS Record_ID,
ari.DocumentNo, aril.Line, aril.M_Product_ID, aril.C_Charge_ID,
aril.AFGO_Activity_ID, aril.AFGO_ServiceType_ID, aril.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
aril.LineNetAmt AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM C_Invoice ari
INNER JOIN C_InvoiceLine aril ON (aril.C_Invoice_ID=ari.C_Invoice_ID)
WHERE ari.DocStatus IN ('CL', 'CO')
AND ari.IsSOTrx='Y'
AND ari.AFGO_FundSchedule_ID IS NULL
AND ari.AFGO_PurchaseCommitment_ID IS NULL
AND ari.AFGO_Program_ID IS NOT NULL
--
-- Budget
UNION
SELECT b.AD_Client_ID, b.AD_Org_ID, b.Created, b.CreatedBy, b.Updated, b.UpdatedBy, b.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget') AS AD_Table_ID, b.AFGO_Budget_ID AS Record_ID,
b.DocumentNo, bl.Line, NULL AS M_Product_ID, bl.C_Charge_ID,
bl.AFGO_Activity_ID, bl.AFGO_ServiceType_ID, bl.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
bl.Amount AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM AFGO_Budget b
INNER JOIN AFGO_BudgetLine bl ON (bl.AFGO_Budget_ID=b.AFGO_Budget_ID)
WHERE b.DocStatus IN ('CL', 'CO')
AND b.BudgetType IN ('B')
--
-- Prognosis
UNION
SELECT p.AD_Client_ID, p.AD_Org_ID, p.Created, p.CreatedBy, p.Updated, p.UpdatedBy, p.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_Budget') AS AD_Table_ID, p.AFGO_Budget_ID AS Record_ID, 
p.DocumentNo, pl.Line, NULL AS M_Product_ID, pl.C_Charge_ID,
pl.AFGO_Activity_ID, pl.AFGO_ServiceType_ID, pl.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
pl.Amount AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM AFGO_Budget p
INNER JOIN AFGO_BudgetLine pl ON (pl.AFGO_Budget_ID=p.AFGO_Budget_ID)
WHERE p.DocStatus IN ('CL', 'CO')
AND p.BudgetType IN ('P')
--
-- Commitment (Not Formalized)
UNION
SELECT nc.AD_Client_ID, nc.AD_Org_ID, nc.Created, nc.CreatedBy, nc.Updated, nc.UpdatedBy, nc.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment') AS AD_Table_ID, nc.AFGO_PurchaseCommitment_ID AS Record_ID,
nc.DocumentNo, ncl.Line, ncl.M_Product_ID, ncl.C_Charge_ID,
ncl.AFGO_Activity_ID, ncl.AFGO_ServiceType_ID, ncl.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
ncl.PlannedAmt AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitment
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM AFGO_PurchaseCommitment nc
INNER JOIN AFGO_PurchaseCommitmentLine ncl ON (ncl.AFGO_PurchaseCommitment_ID=nc.AFGO_PurchaseCommitment_ID)
WHERE nc.DocStatus IN ('DR', 'IP')
AND nc.PurchaseCommitmentStatus IN ('PR', 'QI', 'QS')
--
-- Commitment (Formalized)
UNION
SELECT fc.AD_Client_ID, fc.AD_Org_ID, fc.Created, fc.CreatedBy, fc.Updated, fc.UpdatedBy, fc.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='AFGO_PurchaseCommitment') AS AD_Table_ID, fc.AFGO_PurchaseCommitment_ID AS Record_ID,
fc.DocumentNo, fcl.Line, fcl.M_Product_ID, fcl.C_Charge_ID,
fcl.AFGO_Activity_ID, fcl.AFGO_ServiceType_ID, fcl.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivables,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
fcl.Amount AS FormalizedCommitmentAmt,
-- InvoicedCommitmentAmt
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM AFGO_PurchaseCommitment fc
INNER JOIN AFGO_PurchaseCommitmentLine fcl ON (fcl.AFGO_PurchaseCommitment_ID=fcl.AFGO_PurchaseCommitment_ID)
WHERE fc.DocStatus IN ('CL', 'CO')
AND fc.PurchaseCommitmentStatus IN ('FR')
--
-- InvoicedCommitment
UNION
SELECT ic.AD_Client_ID, ic.AD_Org_ID, ic.Created, ic.CreatedBy, ic.Updated, ic.UpdatedBy, ic.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice') AS AD_Table_ID, ic.C_Invoice_ID AS Record_ID,
ic.DocumentNo, icl.Line, icl.M_Product_ID, icl.C_Charge_ID,
icl.AFGO_Activity_ID, icl.AFGO_ServiceType_ID, icl.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitment
icl.LineNetAmt AS InvoicedCommitmentAmt,
-- Invoice (AP)
NULL AS InvoicedPayablesAmt
FROM C_Invoice ic
INNER JOIN C_InvoiceLine icl ON (icl.C_Invoice_ID=ic.C_Invoice_ID)
WHERE ic.DocStatus IN ('CL', 'CO')
AND ic.AFGO_PurchaseCommitment_ID IS NOT NULL
--
-- Invoice (AP)
UNION
SELECT api.AD_Client_ID, api.AD_Org_ID, api.Created, api.CreatedBy, api.Updated, api.UpdatedBy, api.IsActive,
(SELECT AD_Table_ID FROM AD_Table WHERE TableName='C_Invoice') AS AD_Table_ID, api.C_Invoice_ID AS Record_ID,
api.DocumentNo, apil.Line, apil.M_Product_ID, apil.C_Charge_ID,
apil.AFGO_Activity_ID, apil.AFGO_ServiceType_ID, apil.AFGO_Month_ID,
-- Funding
NULL AS FundAmt,
-- Invoiced Funding
NULL AS InvoicedFundAmt,
-- Invoiced (AR)
NULL AS InvoicedReceivablesAmt,
-- Budget
NULL AS BudgetAmt,
-- Prognosis
NULL AS PrognosisAmt,
-- Commitment (Not Formalized)
NULL AS CommitmentAmt,
-- Commitment (Formalized)
NULL AS FormalizedCommitmentAmt,
-- InvoicedCommitment
NULL AS InvoicedCommitmentAmt,
-- Invoice (AP)
apil.LineNetAmt AS InvoicedPayablesAmt
FROM C_Invoice api
INNER JOIN C_InvoiceLine apil ON (apil.C_Invoice_ID=api.C_Invoice_ID)
WHERE api.DocStatus IN ('CL', 'CO')
AND api.IsSOTrx='Y'
AND api.AFGO_PurchaseCommitment_ID IS NULL
AND api.AFGO_FundSchedule_ID IS NULL
AND api.AFGO_Program_ID IS NOT NULL
;
/
