package com.ivy.sms.dao;

import android.database.Cursor;

import com.ivy.sms.domain.ConversationDetail;

public class ConversationInGroup {
//	05-31 09:18:23.606: V/Ivy(11110): 列名：snippet--值：high
//	05-31 09:18:23.606: V/Ivy(11110): 列名：_id--值：25
//	05-31 09:18:23.616: V/Ivy(11110): 列名：msg_count--值：1
//	05-31 09:18:23.616: V/Ivy(11110): 列名：address--值：187681131
//	05-31 09:18:23.616: V/Ivy(11110): 列名：date--值：1464623907343
	
	private String snippet;
	private int _id;
	private int msg_count;
	private String address;
	private long date;
	
	public static ConversationInGroup createFromCursor(Cursor cursor){
		ConversationInGroup conversationDetail = new ConversationInGroup();
		String snippet = cursor.getString(cursor.getColumnIndex("snippet"));
		int _id = cursor.getInt(cursor.getColumnIndex("_id"));
		String address = cursor.getString(cursor.getColumnIndex("address"));
		int msg_count = cursor.getInt(cursor.getColumnIndex("msg_count")); 
		Long date = cursor.getLong(cursor.getColumnIndex("date"));
		
		
		
		conversationDetail.set_id(_id);
		conversationDetail.setAddress(address);
		conversationDetail.setDate(date);
		conversationDetail.setSnippet(snippet);
		conversationDetail.setMsg_count(msg_count);
		
		return conversationDetail;
	}
	
	
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getMsg_count() {
		return msg_count;
	}
	public void setMsg_count(int msg_count) {
		this.msg_count = msg_count;
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
	
	
	
	

}
