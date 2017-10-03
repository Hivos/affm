UPDATE AFGO_ProjectCluster pc
SET pc.AD_Org_ID=(SELECT AD_Org_ID FROM AFGO_Program WHERE AFGO_Program_ID=pc.AFGO_Program_ID);

UPDATE AFGO_Project pr
SET pr.AD_Org_ID=(SELECT AD_Org_ID FROM AFGO_ProjectCluster WHERE AFGO_ProjectCluster_ID=pr.AFGO_ProjectCluster_ID);

UPDATE AFGO_Phase ph
SET ph.AD_Org_ID=(SELECT AD_Org_ID FROM AFGO_Project WHERE AFGO_Project_ID=ph.AFGO_Project_ID);


UPDATE AFGO_Activity ac
SET ac.AD_Org_ID=(SELECT AD_Org_ID FROM AFGO_Phase WHERE AFGO_Phase_ID=ac.AFGO_Phase_ID);

COMMIT;