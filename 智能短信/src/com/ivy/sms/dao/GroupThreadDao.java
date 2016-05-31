package com.ivy.sms.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ivy.sms.constant.Constant;
import com.ivy.sms.log.L;

public class GroupThreadDao {

	public static void insert(ContentResolver resolver, int group_id,
			int thread_id) {
		ContentValues values = new ContentValues();
		values.put("group_id", group_id);
		values.put("thread_id", thread_id);
		Uri insert = resolver.insert(Constant.URI.GROUP_THREAD_INSERT, values);
		if (insert != null) {
			// ����Ự�󣬸ı�Ⱥ��ĻỰ����
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			L.v("------����֮ǰthread_count--------"+thread_count+",group_id "+group_id);
			
			GroupDao.updateThreadCount(resolver, group_id, thread_count + 1);
		}
	}

	public static void update(ContentResolver resolver, ContentValues values,
			int _id) {
		int update = resolver.update(Constant.URI.GROUP_THREAD_UPDATE, values,
				"_id = " + _id, null);
	}

	public static void delete(ContentResolver resolver, int thread_id,int group_id) {
		int delete = resolver.delete(Constant.URI.GROUP_THREAD_DELETE, "thread_id = " + thread_id, null);
		if(delete>0){
			// �ı�Ⱥ��ĻỰ����
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			L.v("------����֮ǰthread_count--------"+thread_count+",group_id "+group_id);
			if(thread_count>0){
				GroupDao.updateThreadCount(resolver, group_id, thread_count - 1);
			}
		}
	}

	
	public static void clearByGroupId(ContentResolver resolver, int group_id) {
		int delete = resolver.delete(Constant.URI.GROUP_THREAD_DELETE, "group_id = " + group_id, null);
		if(delete>0){
			L.v("------clearByGroupId-------"+group_id+","+delete);
		}
	}
	/**
	 * ���ж���û�м���Ự
	 * 
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean hasGroup(ContentResolver resolver, int thread_id) {
		// ֻҪ�ܲ鵽������˵������Ự�Ѿ�������thread_group����
		Cursor cursor = resolver.query(Constant.URI.GROUP_THREAD_QUERY, null,
				"thread_id = " + thread_id, null, null);
		return cursor.moveToNext();
	}

	/**
	 * ͨ���Ựthread_idȥ��ѯȺ��group_id
	 * 
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static int getGroupIdByThreadId(ContentResolver resolver,
			int thread_id) {
		Cursor cursor = resolver.query(Constant.URI.GROUP_THREAD_QUERY,
				new String[] { "group_id" }, "thread_id = " + thread_id, null,
				null);
		cursor.moveToFirst();
		int group_id = cursor.getInt(cursor.getColumnIndex("group_id"));//���ﲻ��
		return group_id;
	}

}
