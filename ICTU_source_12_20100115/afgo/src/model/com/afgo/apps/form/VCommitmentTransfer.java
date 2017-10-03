/******************************************************************************
 * Product: AFGO                                                              *
 * Copyright (C) 2007-2010 Stichting ICTU. All Rights Reserved.               *
 *                                                                            *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 3 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * Stichting ICTU                                                             *
 * Postbus 84011                                                              *
 * 2508 AA Den Haag                                                           *
 * The Netherlands                                                            *
 * +31(0)70 888 77 77                                                         *
 * www.ictu.nl / info@ictu.nl                                                 *
 ******************************************************************************/
package com.afgo.apps.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import com.afgo.model.MAFGOFormColumn;
import com.afgo.model.MAFGOFormTable;
import com.afgo.model.MAFGOProgram;
import com.afgo.model.MAFGOYear;
import com.afgo.model.MasterPurchaseCommitment;
import com.afgo.model.TransferredPurchaseCommitment;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: VCommitmentTransfer.java,v 1.17.2.1 2010/01/06 11:25:17 tomassen Exp $
 *
 */
public class VCommitmentTransfer extends VTableBasedForm implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
	public VCommitmentTransfer()
	{
		
	}
	
	public void init(int WindowNo, FormFrame frame)
	{
		this.WindowNo = WindowNo;
		this.frame = frame;
		this.ctx = Env.getCtx();
		
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
	}
	
	
	private Ctx ctx = null;
	
	private int WindowNo = -1;
	
	private FormFrame frame = null;
	
	private MasterPurchaseCommitment purchaseCommitment;
	
	private int commitmentIndex = -1;
	private int monthIndex = -1;
	private int activityIndex = -1;
	private int serviceTypeIndex = -1;
	private int productIndex = -1;
	private int chargeIndex = -1;
	
	private int closeAmtIndex = -1;
	private HashMap<Integer,BigDecimal> closeAmtMap = null;
	private int transferAmtIndex = -1;
	private HashMap<Integer,BigDecimal> transferAmtMap = null;
	
	private String sql = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	
	private CPanel parameterPanel = new CPanel();
	private CLabel labelProgram = new CLabel();
	private VComboBox fieldProgram = new VComboBox();
	private CLabel labelFromYear = new CLabel();
	private VComboBox fieldFromYear = new VComboBox();
	private CLabel labelPurchaseCommitment = new CLabel();
	private VComboBox fieldPurchaseCommitment = new VComboBox();
	private CLabel labelToYear = new CLabel();
	private VComboBox fieldToYear = new VComboBox();
	private CLabel labelDocType= new CLabel();
	private VComboBox fieldDocType = new VComboBox();
	private GridBagLayout parameterLayout = new GridBagLayout();

	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	
	
	protected Ctx getCtx()
	{
		return this.ctx;
	}
	
	protected int getWindowNo()
	{
		return this.WindowNo;
	}
	
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		
		labelProgram.setText(Msg.translate(Env.getCtx(), "AFGO_Program_ID"));
		parameterPanel.add(labelProgram,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		fieldProgram.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		fieldProgram.addActionListener(this);
		parameterPanel.add(fieldProgram,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//
		labelPurchaseCommitment.setText(Msg.translate(Env.getCtx(), "AFGO_PurchaseCommitment_ID"));
		parameterPanel.add(labelPurchaseCommitment, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		fieldPurchaseCommitment.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		fieldPurchaseCommitment.addActionListener(this);
		parameterPanel.add(fieldPurchaseCommitment, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//
		labelFromYear.setText(Msg.translate(Env.getCtx(), "fromYear_ID"));
		parameterPanel.add(labelFromYear,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		fieldFromYear.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		fieldFromYear.addActionListener(this);
		parameterPanel.add(fieldFromYear, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//
		labelToYear.setText(Msg.translate(Env.getCtx(), "toYear_ID"));
		parameterPanel.add(labelToYear,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		fieldToYear.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		fieldToYear.addActionListener(this);
		parameterPanel.add(fieldToYear,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		labelDocType.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
		parameterPanel.add(labelDocType,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		fieldDocType.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		fieldDocType.addActionListener(this);
		parameterPanel.add(fieldDocType,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);
		//
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);

	}
	
	private void dynInit()
	{
		String sql = "SELECT p.AFGO_Program_ID"
			+ " FROM AFGO_Program p"
			+ " WHERE p.IsActive='Y'"
			+ " AND p.AD_client_ID=?";
		sql = MRole.getDefault().addAccessSQL(sql, "p", false, false);
		
		log.fine(sql);
		
		this.fieldProgram.addItem(null);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				MAFGOProgram program = MAFGOProgram.getProgram(ctx, rs.getInt(1));
				this.fieldProgram.addItem(new ProgramInfo(program));
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
		MAFGOFormTable formTable = this.getFormTable("AFGO_RV_TransferableCommitment");
		
		if (formTable != null)
		{
    		ArrayList<ColumnInfo> infoColumns = new ArrayList<ColumnInfo>();
    		
    		for (Iterator<MAFGOFormColumn> columns = formTable.getColumns().iterator(); columns.hasNext();)
    		{
    			MAFGOFormColumn column = columns.next();
    			if (column.getElement() != null && "AFGO_PurchaseCommitment_ID".equals(column.getElement().getColumnName()))
    				commitmentIndex = infoColumns.size();
    			else if (column.getElement() != null && "AFGO_Month_ID".equals(column.getElement().getColumnName()))
    				monthIndex = infoColumns.size();
    			else if (column.getElement() != null && "AFGO_Activity_ID".equals(column.getElement().getColumnName()))
    				activityIndex = infoColumns.size();
    			else if (column.getElement() != null && "AFGO_ServiceType_ID".equals(column.getElement().getColumnName()))
    				serviceTypeIndex = infoColumns.size();
    			else if (column.getElement() != null && "M_Product_ID".equals(column.getElement().getColumnName()))
    				productIndex = infoColumns.size();
    			else if (column.getElement() != null && "C_Charge_ID".equals(column.getElement().getColumnName()))
    				chargeIndex = infoColumns.size();
    			else if (column.getElement() != null && "WriteOffAmt".equals(column.getElement().getColumnName()))
    				closeAmtIndex = infoColumns.size();
    			else if (column.getElement() != null && "TransferAmt".equals(column.getElement().getColumnName()))
    				transferAmtIndex = infoColumns.size();
    			ColumnInfo columnInfo = new ColumnInfo(column.getHeader(Env.getAD_Language(this.getCtx())), column.getColumnSQL(), column.getJavaClass(), column.isReadOnly(), column.isKey(), column.getColumnName());
    			infoColumns.add(columnInfo);
    		}
    		
    		this.sql = miniTable.prepareTable(infoColumns.toArray(new ColumnInfo[]{}), formTable.getFromClause(), formTable.getWhereClause(), formTable.isMultiSelection(), "tc");
		}

		if (commitmentIndex < 0 || (closeAmtIndex < 0 && transferAmtIndex < 0) || (chargeIndex < 0 && productIndex < 0))
		{
    		this.sql = miniTable.prepareTable(new ColumnInfo[] 
    		{
    				new ColumnInfo(Msg.translate(ctx, "AFGO_PurchaseCommitment_ID"), "pc.DocumentNo", KeyNamePair.class, true, true, "tc.AFGO_PurchaseCommitment_ID"),	// 0
    				new ColumnInfo(Msg.translate(ctx, "Description"), "SUBSTR(pc.Description, 1, 60)", String.class),													// 1
    				new ColumnInfo(Msg.translate(ctx, "AFGO_Month_ID"), "m.Name", KeyNamePair.class, true, true, "tc.AFGO_Month_ID"),									// 2
    				new ColumnInfo(Msg.translate(ctx, "AFGO_Activity_ID"), "a.Name", KeyNamePair.class, true, true, "tc.AFGO_Activity_ID"),								// 3
    				new ColumnInfo(Msg.translate(ctx, "AFGO_ServiceType_ID"), "s.Name", KeyNamePair.class, true, true, "tc.AFGO_ServiceType_ID"),						// 4
    				new ColumnInfo(Msg.translate(ctx, "M_Product_ID"), "p.Name", KeyNamePair.class, true, true, "tc.M_Product_ID"),										// 5
    				new ColumnInfo(Msg.translate(ctx, "C_Charge_ID"), "c.Name", KeyNamePair.class, true, true, "tc.C_Charge_ID"),										// 6		
    				new ColumnInfo(Msg.translate(ctx, "TotalAmt"), "tc.LineNetAmt", BigDecimal.class),																	// 7	
    				new ColumnInfo(Msg.translate(ctx, "OpenAmt"), "tc.OpenAmt", BigDecimal.class),																		// 8
    				new ColumnInfo(Msg.translate(ctx, "WriteOffAmt"), "0", BigDecimal.class, false, false, null),														// 9	
    				new ColumnInfo(Msg.translate(ctx, "TransferAmt"), "0", BigDecimal.class, false, false, null)														// 10
    		},
    		"AFGO_RV_TransferableCommitment tc"
    		+ " INNER JOIN AFGO_PurchaseCommitment pc ON (pc.AFGO_PurchaseCommitment_ID=tc.AFGO_PurchaseCommitment_ID)"
    		+ " INNER JOIN AFGO_Month m ON (m.AFGO_Month_ID=tc.AFGO_Month_ID)"
    		+ " INNER JOIN AFGO_Activity a ON (a.AFGO_Activity_ID=tc.AFGO_Activity_ID)"
    		+ " INNER JOIN AFGO_ServiceType s ON (s.AFGO_ServiceType_ID=tc.AFGO_ServiceType_ID)"
    		+ " LEFT OUTER JOIN M_Product p ON (p.M_Product_ID=tc.M_Product_ID)"
    		+ " LEFT OUTER JOIN C_Charge c ON (c.C_Charge_ID=tc.C_Charge_ID)",
    		"tc.OpenAmt <> 0", 
    		true, "tc");
    		
    		commitmentIndex = 0;
    		monthIndex = 2;
    		activityIndex = 3;
    		serviceTypeIndex = 4;
    		productIndex = 5;
    		chargeIndex = 6;
    		closeAmtIndex = 9;
    		transferAmtIndex = 10;
		}

		miniTable.getModel().addTableModelListener(this);
		miniTable.autoSize();

		this.frame.setPreferredSize(new Dimension(1024,768));
		this.frame.pack();
	}
	
	
	
	private int getAFGO_Program_ID()
	{
		int AFGO_Program_ID = -1;
		
		if (this.fieldProgram.getSelectedItem() != null)
			AFGO_Program_ID = ((ProgramInfo)this.fieldProgram.getSelectedItem()).getAFGO_Program_ID();
		
		return AFGO_Program_ID;
	}
	
	private int getAFGO_PurchaseCommitment_ID()
	{
		int AFGO_PurchaseCommitment_ID = -1;
		
		if (this.fieldPurchaseCommitment.getSelectedItem() != null)
			AFGO_PurchaseCommitment_ID = ((PurchaseCommitmentInfo)this.fieldPurchaseCommitment.getSelectedItem()).getAFGO_PurchaseCommitment_ID();
				
		return AFGO_PurchaseCommitment_ID;
	}
	
	private MasterPurchaseCommitment getPurchaseCommitment()
	{
		if (this.purchaseCommitment == null || this.getAFGO_PurchaseCommitment_ID() != this.purchaseCommitment.getAFGO_PurchaseCommitment_ID())
		{
			this.purchaseCommitment = null;
			
			if (this.getAFGO_PurchaseCommitment_ID() > 0)
			{
				this.purchaseCommitment = new MasterPurchaseCommitment(this.ctx, this.getAFGO_PurchaseCommitment_ID(), null);
			}
		}
		return this.purchaseCommitment;
	}
	
	private int getFromYear_ID()
	{
		int AFGO_Year_ID = -1;
		
		if (this.fieldFromYear.getSelectedItem() != null)
			AFGO_Year_ID = ((YearInfo)this.fieldFromYear.getSelectedItem()).getAFGO_Year_ID();
		
		return AFGO_Year_ID;
	}
	
	private int getToYear_ID()
	{
		int AFGO_Year_ID = -1;
		
		if (this.fieldToYear.getSelectedItem() != null)
			return ((YearInfo)this.fieldToYear.getSelectedItem()).getAFGO_Year_ID();
		
		return AFGO_Year_ID;
	}
	
	private int getC_DocType_ID()
	{
		int C_DocType_ID = -1;
		
		if (this.fieldDocType.getSelectedItem() != null)
			return ((DocTypeInfo)this.fieldDocType.getSelectedItem()).getC_DocType_ID();
		
		return C_DocType_ID;
	}
	
	private void loadPurchaseCommitmentInfo()
	{
		log.fine("");
		
		this.fieldPurchaseCommitment.removeAllItems();
		this.fieldPurchaseCommitment.addItem(null);
		
		if (this.getAFGO_Program_ID() < 1)
			return;

		String sql = "SELECT pc.AFGO_PurchaseCommitment_ID, pc.DocumentNo"
			+ " FROM AFGO_PurchaseCommitment pc"
			+ " INNER JOIN C_DocType dt ON (dt.C_DocType_ID=pc.C_DocType_ID)"
			+ " INNER JOIN AFGO_PurchaseCommitmentType pct ON (pct.AFGO_PurchaseCommitmentType_ID=dt.AFGO_PurchaseCommitmentType_ID)"
			+ " WHERE (pct.IsMasterCommitment='Y' AND pct.IsCanTransfer='Y')"
			+ " AND EXISTS"
			+ " ("
			+ " 	SELECT tc.AFGO_PurchaseCommitment_ID"
			+ " 	FROM AFGO_RV_TransferableCommitment tc"
			+ " 	WHERE tc.AFGO_PurchaseCommitment_ID=pc.AFGO_PurchaseCommitment_ID"
			+ " 	AND tc.OpenAmt <> 0"
			+ " )"
			+ " AND pc.DocStatus IN ('CO', 'CL')"
			+ " AND pc.AFGO_Program_ID=?";
		
		sql = MRole.getDefault().addAccessSQL(sql, "pc", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		
		log.fine(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getAFGO_Program_ID());

			rs = pstmt.executeQuery();
			while(rs.next())
			{
				this.fieldPurchaseCommitment.addItem(new PurchaseCommitmentInfo(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
	}
	
	private void loadFromYearInfo()
	{
		this.fieldFromYear.removeAllItems();
		this.fieldFromYear.addItem(null);
		
		if (this.getPurchaseCommitment() == null)
			return;
		
		for (Iterator<MAFGOYear> years = this.getPurchaseCommitment().getOrderedYears().iterator(); years.hasNext();)
		{
			this.fieldFromYear.addItem(new YearInfo(years.next()));
		}

	}
	
	private void loadToYearInfo()
	{
		this.fieldToYear.removeAllItems();
		this.fieldToYear.addItem(null);
		
		if (this.getPurchaseCommitment() == null)
			return;
		
		for (Iterator<MAFGOYear> years = this.getPurchaseCommitment().getOrderedYears().iterator(); years.hasNext();)
		{
			MAFGOYear year = years.next();
			if (year.getQuarter().getMonth().isPeriodOpen())
			{
				if (this.getFromYear_ID() > 0 && this.getFromYear_ID() == year.getAFGO_Year_ID())
				{
					//	continue;
					//this.miniTable.setColumnReadOnly(8, true);
					// allow transfer within same year, 10002382
				}
				
				
				this.fieldToYear.addItem(new YearInfo(year));
			}
		}
	}
	
	private void loadDocTypeInfo()
	{
		log.fine("");
		
		this.fieldDocType.removeAllItems();
		this.fieldDocType.addItem(null);
		
		if (this.getAFGO_Program_ID() < 1)
			return;
		
		String sql = "SELECT dt.C_DocType_ID, COALESCE(dtt.Name, dt.Name)"
			+ " FROM C_DocType dt"
			+ " INNER JOIN AFGO_PurchaseCommitmentType pct ON (pct.AFGO_PurchaseCommitmentType_ID=dt.AFGO_PurchaseCommitmentType_ID)"
			+ " LEFT OUTER JOIN C_DocType_Trl dtt ON (dtt.C_DocType_ID=dt.C_DocType_ID AND dtt.AD_Language=?)"
			+ " WHERE dt.AD_Client_ID=?"
			+ " AND dt.IsActive='Y'"
			+ " AND dt.DocBaseType='XPC'"
			+ " AND pct.IsTransferCommitment='Y'";
		
		sql = MRole.getDefault().addAccessSQL(sql, "dt", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		
		log.fine(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, this.ctx.getContext("#AD_Language"));
			pstmt.setInt(2, this.ctx.getAD_Client_ID());
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				this.fieldDocType.addItem(new DocTypeInfo(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
		
	}
	
	private void loadTableInfo()
	{
		log.fine("");
		
		//this.miniTable.
		this.miniTable.setRowCount(0);
		
		if (this.sql == null)
			return;
		
		String sql = this.sql;
		
		if (this.getAFGO_PurchaseCommitment_ID() < 1)
			return;
		else
			sql = sql + " AND tc.AFGO_PurchaseCommitment_ID=?"; // 1
		
		if (this.getFromYear_ID() > 0)
			sql = sql + " AND tc.AFGO_Year_ID=?";
		
		if (this.getToYear_ID() > 0)
		{
			this.miniTable.setColumnReadOnly(transferAmtIndex, (this.getToYear_ID() == this.getFromYear_ID()));
			//sql = sql + " AND tc.AFGO_Year_ID!=?";
		}
		
		sql = sql + " ORDER BY tc.AFGO_PurchaseCommitment_ID";
		
		log.fine("AFGO_Program_ID=" + this.getAFGO_Program_ID() + ", AFGO_Year_ID=" + this.getFromYear_ID() + ", AFGO_PurchaseCommitment_ID=" + this.getAFGO_PurchaseCommitment_ID());

		log.fine(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			int idx = 1;
			pstmt.setInt(idx, this.getAFGO_PurchaseCommitment_ID());
			
			if (this.getFromYear_ID() > 0)
			{
				idx++;
				pstmt.setInt(idx, this.getFromYear_ID());
			}
			
			/*
			if (this.getToYear_ID() > 0)
			{
				idx++;
				pstmt.setInt(idx, this.getToYear_ID());
			}
			*/
			
			rs = pstmt.executeQuery();
			
			this.miniTable.loadTable(rs);
			this.closeAmtMap = new HashMap<Integer,BigDecimal>();
			this.transferAmtMap = new HashMap<Integer,BigDecimal>();
			
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			log.severe(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rs != null) try {rs.close(); rs = null;} catch(Exception e) {e.printStackTrace();}
			if (pstmt != null) try {pstmt.close(); pstmt = null;} catch(Exception e) {e.printStackTrace();}
		}
	}
	
	private void generate()
	{
		int C_DocType_ID = this.getC_DocType_ID();
		if (C_DocType_ID < 1)
		{
			ADialog.error(WindowNo, this.frame, "NoTransferDocType");
			return;
		}
		
		if (this.getToYear_ID() < 1)
		{
			ADialog.error(WindowNo, this.frame, "NoYearTo");
			return;
		}
		int toAFGO_Month_ID = MAFGOYear.getYear(ctx, this.getToYear_ID()).getQuarter().getMonth().getAFGO_Month_ID();
		
		TableModel model = this.miniTable.getModel();
		
		MasterPurchaseCommitment master = null;
		TransferredPurchaseCommitment transfer = null;
		int lines = 0;
		StringBuffer result = new StringBuffer();
		
		try
		{
    		for (int row = 0; row < model.getRowCount(); row++)
    		{
    			if (master == null || master.getAFGO_PurchaseCommitment_ID() != ((KeyNamePair)model.getValueAt(row, commitmentIndex)).getKey())
    			{
    				if (((KeyNamePair)model.getValueAt(row, commitmentIndex)).getKey() == this.getAFGO_PurchaseCommitment_ID())
    					master = this.getPurchaseCommitment();
    				else master = new MasterPurchaseCommitment(this.ctx, ((KeyNamePair)model.getValueAt(row, commitmentIndex)).getKey(), null);
    				
    				//System.err.println("LINES=" + lines);
    				if (transfer != null && lines > 0)
    				{
    					transfer.save();
    					result.append("\n" + transfer.getDocumentNo());
    				}
    				
    				transfer = new TransferredPurchaseCommitment(master, C_DocType_ID);
    				lines = 0;
    			}
    			
    			int fromAFGO_Month_ID = ((KeyNamePair)model.getValueAt(row, monthIndex)).getKey();
    			int AFGO_Activity_ID = ((KeyNamePair)model.getValueAt(row, activityIndex)).getKey();
    			int AFGO_ServiceType_ID = master.getProgram().getAFGO_ServiceType_ID();
    			if (serviceTypeIndex > -1)
    				AFGO_ServiceType_ID = ((KeyNamePair)model.getValueAt(row, serviceTypeIndex)).getKey();
    			int M_Product_ID = 0;
    			if (productIndex > -1)
    				M_Product_ID = ((KeyNamePair)model.getValueAt(row, productIndex)).getKey();
    			
    			int C_Charge_ID = ((KeyNamePair)model.getValueAt(row, chargeIndex)).getKey();
    			BigDecimal closeAmt = this.closeAmtMap.get(row);
    			if (closeAmt == null)
    				closeAmt = Env.ZERO;
    			
    			BigDecimal transferAmt = this.transferAmtMap.get(row);
    			if (transferAmt == null)
    				transferAmt = Env.ZERO;
    			
    			//System.err.println("SELECTED VALUE=" + this.miniTable.getSelectedValue());
    			
    			log.info("close=" + closeAmt + ", transfer=" + transferAmt);
    			
    			lines = lines + transfer.transferCommitment(AFGO_Activity_ID, AFGO_ServiceType_ID, M_Product_ID, C_Charge_ID, fromAFGO_Month_ID, toAFGO_Month_ID, transferAmt, closeAmt);
    		}
    		
			//System.err.println("LINES=" + lines);
			if (transfer != null && lines > 0)
			{
				transfer.save();
				result.append("\n" + transfer.getDocumentNo());
			}
		}
		catch(Exception e)
		{
			ADialog.error(WindowNo, this.frame, "ProcessError", e.getMessage());
			e.printStackTrace();
		}
		
		
		if (transfer != null)
		{
			log.info(transfer.getDocumentNo());
			ADialog.info(WindowNo, this.frame, "DocumentNo", result.toString());
		}
		
		this.loadPurchaseCommitmentInfo();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(this.fieldProgram))
		{
			this.loadDocTypeInfo();
			this.loadPurchaseCommitmentInfo();
		}
		
		else if (e.getSource().equals(this.fieldPurchaseCommitment))
		{
			this.loadFromYearInfo();
			this.loadToYearInfo();
			this.loadTableInfo();
		}
		
		else if (e.getSource().equals(this.fieldFromYear))
		{
			this.loadToYearInfo();
			this.loadTableInfo();
		}
		
		else if (e.getSource().equals(this.fieldToYear))
		{
			this.loadTableInfo();
		}
		
		else if (e.getSource().equals(this.bGenerate))
		{
			this.bGenerate.requestFocusInWindow();
			this.generate();
		}

		else if (e.getSource().equals(this.bCancel))
		{
			this.dispose();
		}
		
	}

	public void dispose()
	{
		if (this.frame != null)
			this.frame.dispose();
		this.frame = null;
	}

	public void tableChanged(TableModelEvent e)
	{
		// CloseAmt
		if (e.getColumn() == closeAmtIndex)
		{
			if (this.closeAmtMap == null)
				return;
			
			BigDecimal closeAmt = (BigDecimal)this.miniTable.getValueAt(e.getFirstRow(), e.getColumn());
			if (closeAmt.compareTo(Env.ZERO) < 0)
			{
				closeAmt = closeAmt.negate();
				this.miniTable.setValueAt(closeAmt, e.getFirstRow(), e.getColumn());
				return;
			}
			
			this.closeAmtMap.put(e.getFirstRow(), (BigDecimal)this.miniTable.getValueAt(e.getFirstRow(), e.getColumn()));
		}
		// TransferAmt
		else if (e.getColumn() == transferAmtIndex)
		{
			if (this.transferAmtMap == null)
				return;
			
			BigDecimal transferAmt = (BigDecimal)this.miniTable.getValueAt(e.getFirstRow(), e.getColumn());
			if (transferAmt.compareTo(Env.ZERO) < 0)
			{
				transferAmt = transferAmt.negate();
				this.miniTable.setValueAt(transferAmt, e.getFirstRow(), e.getColumn());
				return;
			}
			
			this.transferAmtMap.put(e.getFirstRow(), (BigDecimal)this.miniTable.getValueAt(e.getFirstRow(), e.getColumn()));
		}
	}


	public void executeASync(ProcessInfo pinfo)
	{

	}

	public void lockUI(ProcessInfo pinfo)
	{

	}
	
	public boolean isUILocked()
	{
		return false;
	}


	public void unlockUI(ProcessInfo pinfo)
	{
		
	}
	
	class ProgramInfo
	{
		public ProgramInfo(MAFGOProgram program)
		{
			this.program = program;
		}
		
		private MAFGOProgram program = null;
		
		public String toString()
		{
			return this.program.getName();
		}
		
		public int getAFGO_Program_ID()
		{
			return this.program.getAFGO_Program_ID();
		}
		
		public MAFGOProgram getProgram()
		{
			return this.program;
		}
	
	}
	
	class PurchaseCommitmentInfo
	{
		public PurchaseCommitmentInfo(int AFGO_PurchaseCommitment_ID, String name)
		{
			this.AFGO_PurchaseCommitment_ID = AFGO_PurchaseCommitment_ID;
			this.name = name;
		}
		
		private int AFGO_PurchaseCommitment_ID = -1;
		
		private String name = null;
		
		public String toString()
		{
			return this.name;
		}
		
		public int getAFGO_PurchaseCommitment_ID()
		{
			return this.AFGO_PurchaseCommitment_ID;
		}
	}
	
	class DocTypeInfo
	{
		public DocTypeInfo(int C_DocType_ID, String name)
		{
			this.C_DocType_ID = C_DocType_ID;
			this.name = name;
		}
		
		private int C_DocType_ID = -1;
		
		private String name = null;
		
		public String toString()
		{
			return this.name;
		}
		
		public int getC_DocType_ID()
		{
			return this.C_DocType_ID;
		}
	}
	
	class YearInfo
	{
		public YearInfo(MAFGOYear year)
		{
			this.year = year;
		}
		
		MAFGOYear year;
		
		public String toString()
		{
			return this.year.getName();
		}
		
		public int getAFGO_Year_ID()
		{
			return this.year.getAFGO_Year_ID();
		}
		
	}
}
