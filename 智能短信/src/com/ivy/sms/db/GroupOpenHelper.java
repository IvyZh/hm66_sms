package com.ivy.sms.db;

import com.ivy.sms.constant.Constant;
import com.ivy.sms.log.L;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupOpenHelper extends SQLiteOpenHelper {
	private static GroupOpenHelper helper;
	
	public GroupOpenHelper(Context context) {
		super(context, Constant.DB.DB_NAME, null, 1);
	}
	public static GroupOpenHelper getInsance(Context context){
		if(helper==null){
			helper = new GroupOpenHelper(context);
		}
		return helper;
	}

	private String createTableGroups = "create table groups (_id integer primary key autoincrement,createtime char(20),count char(20),name char(20));";
	private String createTableGroupsThread = "create table groups_thread (_id integer primary key autoincrement,group_id char(20),thread_id char(20));";
	@Override
	public void onCreate(SQLiteDatabase db) {
		L.v("---db¥¥Ω®¡À---");
		db.execSQL(createTableGroups);
		db.execSQL(createTableGroupsThread);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
