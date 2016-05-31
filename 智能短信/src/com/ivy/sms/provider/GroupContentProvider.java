package com.ivy.sms.provider;

import com.ivy.sms.constant.Constant;
import com.ivy.sms.constant.Constant.URI;
import com.ivy.sms.db.GroupOpenHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GroupContentProvider extends ContentProvider {
	public static final String authority = "com.ivy.sms.group";
	private static final int GROUP_INSERT = 0;
	private static final int GROUP_DELETE = 1;
	private static final int GROUP_QUERY = 2;
	private static final int GROUP_UPDATE = 3;
	private static final int GROUP_THREAD_INSERT = 4;
	private static final int GROUP_THREAD_DELETE = 5;
	private static final int GROUP_THREAD_QUERY = 6;
	private static final int GROUP_THREAD_UPDATE = 7;
	
	private static final Uri BASE_URI = Uri.parse("content://"+authority);
	
	private static UriMatcher matcher = new UriMatcher(0);
	private SQLiteDatabase db;
	static {
		matcher.addURI(authority, "group/insert", GROUP_INSERT);// ²»ÄÜ¼Ó  /group/insert
		matcher.addURI(authority, "group/delete", GROUP_DELETE);
		matcher.addURI(authority, "group/query", GROUP_QUERY);
		matcher.addURI(authority, "group/update", GROUP_UPDATE);
		
		matcher.addURI(authority, "group_thread/insert", GROUP_THREAD_INSERT);
		matcher.addURI(authority, "group_thread/delete", GROUP_THREAD_DELETE);
		matcher.addURI(authority, "group_thread/query", GROUP_THREAD_QUERY);
		matcher.addURI(authority, "group_thread/update", GROUP_THREAD_UPDATE);
		
	}

	private final String GROUP_TABLE =Constant.DB.TABLE_GROUP_NAME;
	private final String GROUP_THREAD_TABLE =Constant.DB.TABLE_GROUP_THREAD_NAME;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = matcher.match(uri);
		if(match == GROUP_DELETE){
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return db.delete(GROUP_TABLE, selection, selectionArgs);
		}else if(match == GROUP_THREAD_DELETE){
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return db.delete(GROUP_THREAD_TABLE, selection, selectionArgs);
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int match = matcher.match(uri);
		if(match == GROUP_INSERT){
			long insert = db.insert(GROUP_TABLE, null, values);
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return Uri.withAppendedPath(uri, insert+"");
		}else if(match == GROUP_THREAD_INSERT){
			long insert = db.insert(GROUP_THREAD_TABLE,null, values);
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return Uri.withAppendedPath(uri, insert+"");
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean onCreate() {
		GroupOpenHelper provider = GroupOpenHelper.getInsance(getContext());
		db = provider.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int match = matcher.match(uri);
		if(match == GROUP_QUERY){
			Cursor cursor = db.query(GROUP_TABLE, null, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		}else if(match == GROUP_THREAD_QUERY){
			Cursor cursor = db.query(GROUP_THREAD_TABLE, null, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int match = matcher.match(uri);
		if(match == GROUP_UPDATE){
			int update = db.update(GROUP_TABLE, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return update;
		}else if(match == GROUP_UPDATE){
			int update = db.update(GROUP_THREAD_TABLE, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(Uri.parse("content://com.ivy.sms.group"), null);
			return update;
		}else{
			throw new IllegalArgumentException();
		}
	}

}
