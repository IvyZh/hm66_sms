package com.ivy.sms.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ivy.sms.constant.Constant;
import com.ivy.sms.log.L;

public class GroupDao {

	public static void insert(ContentResolver resolver, String groupName){
		ContentValues values = new ContentValues();
		values.put("createtime", System.currentTimeMillis());
		values.put("name", groupName);
		values.put("count", 0);
		Uri insert = resolver.insert(Constant.URI.GROUP_INSERT, values);
		L.v(insert.toString());
	}
	
	public static void update(ContentResolver resolver, ContentValues values,int _id){
		int update = resolver.update(Constant.URI.GROUP_UPDATE, values, "_id = "+_id, null);
	}
	
	public static void delete(ContentResolver resolver,int _id){
		ContentValues values = new ContentValues();
		resolver.delete(Constant.URI.GROUP_DELETE, "_id = "+_id, null);
	}
	
	public static Cursor query(ContentResolver resolver){
		Cursor query = resolver.query(Constant.URI.GROUP_QUERY, null, null, null, null);
		return query;
	}

	/**
	 * 获取指定群组存放的会话的数量
	 * @param resolver
	 * @param _id
	 * @return
	 */
	public static int getThreadCount(ContentResolver resolver, int _id){
		int threadCount = -1;
		Cursor cursor = resolver.query(Constant.URI.GROUP_QUERY, new String[]{"count"}, "_id = " + _id, null, null);
		if(cursor.moveToNext()){
			threadCount = cursor.getInt(cursor.getColumnIndex("count"));
		}
		return threadCount;
	}
	
	/**
	 * 更新指定群组的会话数量
	 * @param resolver
	 * @param _id
	 * @param threadCount
	 */
	public static void updateThreadCount(ContentResolver resolver, int _id, int threadCount){
		ContentValues values = new ContentValues();
		values.put("count", threadCount);
		resolver.update(Constant.URI.GROUP_UPDATE, values, "_id = " + _id, null);
	}

	public static String getGroupNameById(ContentResolver resolver,int group_id) {
		L.v("---vgetGroupNameById--"+group_id);
		String groupName = "";
		Cursor cursor = resolver.query(Constant.URI.GROUP_QUERY, new String[]{"name"}, "_id = " + group_id, null, null);
		if(cursor.moveToFirst()){
			groupName = cursor.getString(cursor.getColumnIndex("name"));
			//
			L.v("---从groups表中找的name---"+groupName);
		}
		return groupName;
	}
}
