package com.ivy.sms.domain;

import android.database.Cursor;

public class ConversationDetail {
	private int _id;
	private int thread_id;
	private String address;
	private long date;
	private int type;
	private String body;
	
	public static ConversationDetail createFromCursor(Cursor cursor){
		ConversationDetail conversationDetail = new ConversationDetail();
		
		int _id = cursor.getInt(cursor.getColumnIndex("_id"));
		String address = cursor.getString(cursor.getColumnIndex("address"));
		int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));//这里一定要用_id了
		String body = cursor.getString(cursor.getColumnIndex("body"));
		Long date = cursor.getLong(cursor.getColumnIndex("date"));
		int type = cursor.getInt(cursor.getColumnIndex("type"));
		
		conversationDetail.set_id(_id);
		conversationDetail.setAddress(address);
		conversationDetail.setBody(body);
		conversationDetail.setDate(date);
		conversationDetail.setThread_id(thread_id);
		conversationDetail.setType(type);
		
		return conversationDetail;
	}
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int thread_id) {
		this.thread_id = thread_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	
}
