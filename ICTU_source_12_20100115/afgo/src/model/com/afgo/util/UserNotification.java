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
package com.afgo.util;

import org.compiere.framework.PO;
import org.compiere.model.MClient;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.EMail;

/**
 * 
 * @author Eldir Tomassen
 * @author ActFact Projects B.V.
 * 
 * @version $Id: UserNotification.java,v 1.10.2.1 2010/01/06 11:45:29 tomassen Exp $
 *
 */
public class UserNotification
{

	public UserNotification(Ctx ctx, int fromAD_User_ID, int toAD_User_ID, MMessage message, String reference, String msg, int AD_Table_ID, int Record_ID)
	{
		this(ctx, MUser.get(ctx, fromAD_User_ID), MUser.get(ctx, toAD_User_ID), message, reference, msg, null);
		
		this.AD_Table_ID = AD_Table_ID;
		this.Record_ID = Record_ID;
	}
	
	public UserNotification(Ctx ctx, int fromAD_User_ID, int toAD_User_ID, MMessage message, String reference, String msg, PO po)
	{
		this(ctx, MUser.get(ctx, fromAD_User_ID), MUser.get(ctx, toAD_User_ID), message, reference, msg, po);
	}
	
	public UserNotification(Ctx ctx, MUser fromUser, MUser toUser, MMessage message, String reference, String msg, PO po)
	{
		this.ctx = ctx;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.message = message;
		this.reference = reference;
		this.msg = msg;
		
		if (po != null)
		{
			this.AD_Table_ID = po.get_Table_ID();
			this.Record_ID = po.get_ID();
		}
	}
	
	private Ctx ctx = null;
	
	private MUser fromUser;
	
	private MUser toUser;
	
	private MMessage message;
	
	private String reference = null;
	
	private String msg = null;
	
	private int AD_Table_ID = 0;
	
	private int Record_ID = 0;
	
	//private PO po = null;
	
	private int AD_WF_Activity_ID = -1;
	
	private CLogger log = CLogger.getCLogger(getClass());
	
	public int getAD_WF_Activity_ID()
	{
		return this.AD_WF_Activity_ID;
	}
	
	public void setAD_WF_Activity_ID(int AD_WF_Activity_ID)
	{
		this.AD_WF_Activity_ID = AD_WF_Activity_ID;
	}
	
	public boolean isValid()
	{
		//if (this.fromUser == null)
		//	return false;
		if (this.toUser == null)
			return false;
	
		return true;
	}
	
	public boolean isEMail()
	{
		if (!this.isValid())
			return false;
		
		if (this.toUser.getEMail() == null || "".equals(this.toUser.getEMail()))
			return false;
		
		if (MUser.NOTIFICATIONTYPE_EMail.equals(this.toUser.getNotificationType()) || MUser.NOTIFICATIONTYPE_EMailPlusNotice.equals(this.toUser.getNotificationType()))
			return true;
		
		return false;
	}
	
	public boolean isNote()
	{
		if (!this.isValid())
			return false;
		
		if (MUser.NOTIFICATIONTYPE_Notice.equals(this.toUser.getNotificationType()) || MUser.NOTIFICATIONTYPE_EMailPlusNotice.equals(this.toUser.getNotificationType()))
			return true;
		
		return false;
	}
	
	private int getAD_Message_ID()
	{
		return message != null ? message.getAD_Message_ID() : 0;
	}
	
	
	/*
	 * 	public MNote (Ctx ctx, int AD_Message_ID, int AD_User_ID,
		int AD_Table_ID, int Record_ID, 
		String Reference, String TextMsg, String trxName)
	 */
	
	public boolean send()
	{
		if (!this.isValid())
			return false;
		
		boolean result = true;
		
		if (this.isEMail())
		{
			//System.err.println("BEFORE EMAIL: from=" + this.fromUser + ", to=" + this.toUser);
			String fromEmail = this.fromUser.getEMail();
			if (fromEmail == null || "".equals(fromEmail))
			{
				fromEmail = MClient.get(this.ctx).getRequestEMail();
			}
			EMail email = new EMail(MClient.get(this.ctx), fromEmail, this.fromUser.getName(), this.toUser.getEMail(), this.toUser.getName(), this.reference, this.msg);
			if (!"OK".equals(email.send()))
				result = false;
		}
		
		if (this.isNote())
		{
			MNote note = new MNote(this.ctx, this.getAD_Message_ID(), this.toUser.getAD_User_ID(), this.AD_Table_ID, this.Record_ID, this.reference, this.msg, null);
			if (this.AD_WF_Activity_ID > 0)
				note.setAD_WF_Activity_ID(this.AD_WF_Activity_ID);
			if (!note.save())
				result = false;
		}
		
		return result;
	}
}
