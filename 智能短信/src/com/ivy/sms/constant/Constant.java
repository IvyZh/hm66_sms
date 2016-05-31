package com.ivy.sms.constant;

import android.net.Uri;

public class Constant {
	
	public interface URI{
		Uri SMS= Uri.parse("content://sms");
		Uri SMS_CONVERSATION = Uri.parse("content://sms/conversations");
		
		
		Uri GROUP_INSERT = Uri.parse("content://com.ivy.sms.group/group/insert");
		Uri GROUP_DELETE = Uri.parse("content://com.ivy.sms.group/group/delete");
		Uri GROUP_UPDATE = Uri.parse("content://com.ivy.sms.group/group/update");
		Uri GROUP_QUERY = Uri.parse("content://com.ivy.sms.group/group/query");
		
		Uri GROUP_THREAD_INSERT = Uri.parse("content://com.ivy.sms.group/group_thread/insert");
		Uri GROUP_THREAD_DELETE = Uri.parse("content://com.ivy.sms.group/group_thread/delete");
		Uri GROUP_THREAD_UPDATE = Uri.parse("content://com.ivy.sms.group/group_thread/update");
		Uri GROUP_THREAD_QUERY = Uri.parse("content://com.ivy.sms.group/group_thread/query");
		
	}

	public interface SMS{
		int TYPE_RECEIVE = 1;
		int TYPE_SEND = 2;
	}
	
	public interface DB{
		String DB_NAME = "group_db.db";
		String TABLE_GROUP_NAME = "groups";
		String TABLE_GROUP_THREAD_NAME = "groups_thread";
	}
}
