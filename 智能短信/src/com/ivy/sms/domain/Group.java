package com.ivy.sms.domain;

import com.ivy.sms.log.L;
import com.ivy.sms.utils.CursorUtils;

import android.database.Cursor;

public class Group {

	private int _id;
	private String name;
	private long createtime;
	private int count;

	public static Group createFromCursor(Cursor cursor) {
//		L.v("----------Group------------");
//		CursorUtils.print(cursor);
		Group group = new Group();
		group.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
		group.setName(cursor.getString(cursor.getColumnIndex("name")));
		group.setCreate_date(cursor.getLong(cursor.getColumnIndex("createtime")));
		group.setThread_count(cursor.getInt(cursor.getColumnIndex("count")));

		return group;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreate_date() {
		return createtime;
	}

	public void setCreate_date(long create_date) {
		this.createtime = create_date;
	}

	public int getThread_count() {
		return count;
	}

	public void setThread_count(int thread_count) {
		this.count = thread_count;
	}

	@Override
	public String toString() {
		return "Group [_id=" + _id + ", name=" + name + ", createtime="
				+ createtime + ", count=" + count + "]";
	}
	
	

}
