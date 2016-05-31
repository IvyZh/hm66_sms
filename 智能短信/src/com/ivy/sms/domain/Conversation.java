package com.ivy.sms.domain;

import android.database.Cursor;

public class Conversation {

	private long date;
	private String address;
	private int thread_id;
	private int msg_count;
	private String snippet;
	
	public static Conversation createFromCursor(Cursor cursor){
		String address = cursor.getString(cursor.getColumnIndex("address"));
		int thread_id = cursor.getInt(cursor.getColumnIndex("_id"));//这里一定要用_id了
		int msg_count = cursor.getInt(cursor.getColumnIndex("msg_count"));
		String snippet = cursor.getString(cursor.getColumnIndex("snippet"));
		Long date = cursor.getLong(cursor.getColumnIndex("date"));
		
		Conversation conversation = new Conversation();
		
		conversation.setAddress(address);
		conversation.setDate(date);
		conversation.setMsg_count(msg_count);
		conversation.setSnippet(snippet);
		conversation.setThread_id(thread_id);
		
		return conversation;
		
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int thread_id) {
		this.thread_id = thread_id;
	}
	public int getMsg_count() {
		return msg_count;
	}
	public void setMsg_count(int msg_count) {
		this.msg_count = msg_count;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}
