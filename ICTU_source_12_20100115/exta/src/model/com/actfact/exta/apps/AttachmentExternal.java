/********************************************************************************************************
 * 																										*
 * (c) 2007-2010 ActFact B.V. 																			*
 * 																										*
 ********************************************************************************************************/
package com.actfact.exta.apps;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.ConfirmPanel;
import org.compiere.model.GridTab;
import org.compiere.model.MRole;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CDialog;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextArea;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import com.actfact.exta.model.MEXTAAttachmentExternal;
import com.actfact.exta.model.MMimeType;


/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: AttachmentExternal.java,v 1.5 2010/01/06 11:54:24 tomassen Exp $
 *
 */
public class AttachmentExternal extends CDialog implements ActionListener 
{
	public AttachmentExternal(Frame frame, Ctx ctx, GridTab tab, String trxName)
	{
		super (frame, Msg.getMsg(Env.getCtx(), "AttachmentExternal"), true);
		
		this.parent = frame;
		this.ctx = ctx;
		this.trxName = trxName;
		this.WindowNo = tab.getWindowNo();
		
		this.AD_Table_ID = MEXTAAttachmentExternal.getBaseAD_Table_ID(ctx, tab);
		this.Record_ID = tab.getRecord_ID();
		
		this.staticInit();
		this.dynamicInit();
		
		try
		{
			AEnv.showCenterWindow(frame, this);
		}
		catch (Exception e)
		{
		}

	}
	
	private Container parent = null;
	private Ctx ctx = null;
	private String trxName = null;
	private int WindowNo = -1;
	
	private int AD_Table_ID = -1;
	private int Record_ID = -1;
	
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	
	private CPanel linePanel = new CPanel();
	private JScrollPane scrollPane = new JScrollPane(this.linePanel);
	
	private ConfirmPanel confirmPanel = new ConfirmPanel(false);
	
	private String title = null;
	
	private int totalCount = 0;
	
	private int activeCount = 0;
	
	private MRole role = null;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	private void staticInit()
	{
		this.mainPanel.setLayout(mainLayout);
		this.mainLayout.setHgap(5);
		this.mainLayout.setVgap(5);
		this.getContentPane().add(mainPanel);
		
		this.linePanel.setLayout(new BoxLayout(this.linePanel, BoxLayout.Y_AXIS));
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.add(this.confirmPanel, BorderLayout.SOUTH);
		this.confirmPanel.getOKButton().addActionListener(this);
		
		this.title = this.getTitle();
		
		this.role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
	}
	
	private void dynamicInit()
	{
		this.totalCount = 0;
		this.activeCount = 0;
		
		ArrayList<MEXTAAttachmentExternal> attachments = MEXTAAttachmentExternal.getAttachments(this.ctx, this.AD_Table_ID, this.Record_ID, false, this.trxName);
		
		for (Iterator<MEXTAAttachmentExternal> atts = attachments.iterator(); atts.hasNext();)
		{
			this.totalCount++;
			
			MEXTAAttachmentExternal attachment = atts.next();
			
			if (attachment.isActive())
			{
				this.activeCount++;
				
    			this.linePanel.add(new LinkPanel(attachment));
    			
    			if (atts.hasNext())
    				this.linePanel.add(new CPanel());
			}
		}
		
		this.addNew();
		
		this.refreshTitle();
	}
	
	private void refreshTitle()
	{
		this.setTitle(this.title + " [" + this.activeCount + " / " + this.totalCount + "]");
	}
	
	public void dispose()
	{
		super.dispose();
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(610, 400);
	}
	
	private void addNew()
	{
		MEXTAAttachmentExternal newAttachment = new MEXTAAttachmentExternal(this.ctx, 0, this.trxName);
		newAttachment.setAD_Table_ID(this.AD_Table_ID);
		newAttachment.setRecord_ID(this.Record_ID);
		this.linePanel.add(new CPanel());
		this.linePanel.add(new LinkPanel(newAttachment));
		this.refreshTitle();
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (this.confirmPanel.getOKButton().equals(e.getSource()))
			this.dispose();
	}
	
	private String loadFile()
	{
		log.info("");
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "AttachmentNew"));
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return null;
		//
		String fileName = chooser.getSelectedFile().getAbsolutePath();
		log.info("URL=" + fileName);

		return fileName;
	}
	
	class LinkPanel extends CPanel implements ActionListener
	{
		LinkPanel(MEXTAAttachmentExternal attachment)
		{
			this.attachment = attachment;
			
			this.staticInit();
			this.dynamicInit();
		}
		
		private CPanel infoPanel = null;
		
		private CTextArea infoArea = null;
		
		private CPanel filePanel = null;
		
		private CTextField textURL = null;
		
		private CComboBox comboMimeType = null;
		
		private CTextField textCreated = null;
		
		private CButton buttonOpen = null;
		
		private CButton buttonDelete = null;
		
		private CButton buttonLoad = null;
		
		private CButton buttonSave = null;
		
		private MEXTAAttachmentExternal attachment;
		
		private void staticInit()
		{
			this.setLayout(new BorderLayout());
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setVgap(1);
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			this.infoPanel = new CPanel();
			//this.infoPanel.setBackground(Color.YELLOW);
			this.infoPanel.setLayout(flowLayout);
			this.add(this.infoPanel);
			
			this.infoArea = new CTextArea(3,51);
			this.infoPanel.add(this.infoArea, BorderLayout.CENTER);
			
			this.filePanel = new CPanel();
			//this.filePanel.setBackground(Color.GREEN);
			this.add(filePanel, BorderLayout.SOUTH);
			
			this.filePanel.setLayout(flowLayout);
			
			this.textURL = new CTextField(20);
			this.filePanel.add(this.textURL);
			
			this.comboMimeType = new CComboBox();
			this.comboMimeType.setPreferredSize(new Dimension(80, (int)this.comboMimeType.getPreferredSize().getHeight()));
			this.filePanel.add(comboMimeType);
			
			this.textCreated = new CTextField();
			this.filePanel.add(textCreated);
			
			this.buttonLoad = new CButton("Load");
			this.filePanel.add(this.buttonLoad);
			this.buttonLoad.addActionListener(this);
			
			this.buttonSave = new CButton("Save");
			this.filePanel.add(this.buttonSave);
			this.buttonSave.addActionListener(this);
			
			this.buttonDelete = new CButton("Delete");
			this.filePanel.add(this.buttonDelete);
			this.buttonDelete.addActionListener(this);
			
			this.buttonOpen = new CButton("Open");
			this.filePanel.add(this.buttonOpen);
			this.buttonOpen.addActionListener(this);
			
		}
		
		private void dynamicInit()
		{
			this.infoArea.setText(this.attachment.getDescription());
			this.textURL.setText(this.attachment.getAbsolutePath());
			
			// add new
			if (this.attachment.get_ID() < 1)
			{
				this.textCreated.setVisible(false);
				this.buttonLoad.setVisible(true);
				this.buttonSave.setVisible(true);
				this.buttonDelete.setVisible(false);
				this.buttonOpen.setVisible(false);
				
				this.comboMimeType.setVisible(true);
				this.comboMimeType.addItem(null);
				for (Iterator<MMimeType> mimeTypes = MMimeType.getMimeTypes(ctx).iterator(); mimeTypes.hasNext();)
					this.comboMimeType.addItem(mimeTypes.next());
			}
			// deleted
			else if (!this.attachment.isActive())
			{
				this.setVisible(false);
			}
			// open / delete existing
			else
			{
				this.infoArea.setEditable(false);
				this.infoArea.setBackground(CompierePLAF.getFieldBackground_Inactive());
				this.textURL.setEditable(false);
				this.textURL.setBackground(CompierePLAF.getFieldBackground_Inactive());
				
				this.buttonLoad.setVisible(false);
				this.buttonSave.setVisible(false);
				this.buttonDelete.setVisible(true);
				this.buttonDelete.setEnabled(role.isCanDeleteExternalAttachment());
				this.buttonOpen.setVisible(true);

				this.comboMimeType.setVisible(false);
				//this.comboMimeType.addItem(this.attachment.getMimeType());
				//this.comboMimeType.setReadWrite(false);
				
				this.textCreated.setVisible(true);
				this.textCreated.setEditable(false);
				this.textCreated.setBackground(CompierePLAF.getFieldBackground_Inactive());
				DateFormat df = DisplayType.getDateFormat(16); // DisplayType.DateTime
				this.textCreated.setText(df.format(this.attachment.getCreated()));
			}
			
			this.setMaximumSize(new Dimension((int)super.getMaximumSize().getWidth(), 76));
		}
		

		public void actionPerformed(ActionEvent e) 
		{
			if (e == null)
				;
			// load file
			else if (this.buttonLoad.equals(e.getSource()))
			{
				this.textURL.setText(loadFile());
			}
			// save new
			else if (this.buttonSave.equals(e.getSource()))
			{
				if (this.comboMimeType.getSelectedItem() != null)
					this.attachment.setEXTA_MimeType_ID(((MMimeType)this.comboMimeType.getSelectedItem()).getEXTA_MimeType_ID());
				this.attachment.setAbsolutePath(this.textURL.getText());
				this.attachment.setDescription(this.infoArea.getText());
				if (this.attachment.save())
				{
					this.dynamicInit();
					addNew();
					totalCount++;
					activeCount++;
					refreshTitle();
				}
				else
				{
					ADialog.error(WindowNo, parent, "SaveError", this.attachment.getErrorMsg());
				}
			}
			// delete existing
			else if (this.buttonDelete.equals(e.getSource()))
			{
				this.attachment.setIsActive(false);
				if (this.attachment.save())
				{
					this.dynamicInit();
					activeCount--;
					refreshTitle();
				}
			}
			// open existing
			else if (this.buttonOpen.equals(e.getSource()))
			{
				Env.startBrowser(this.textURL.getText());
			}
			else
			{
				;
			}

		}
		
	}

}
