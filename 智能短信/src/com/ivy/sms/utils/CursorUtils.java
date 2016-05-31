package com.ivy.sms.utils;

import android.database.Cursor;

import com.ivy.sms.log.L;

public class CursorUtils {
	
	public static void print(Cursor cursor){
		if(cursor==null){
			L.v("CursorUtils---cursor==null");
			return;
		}
		int count = cursor.getCount();
		L.v("һ�� "+count+" ������");
		while(cursor.moveToNext()){
			
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String columnName = cursor.getColumnName(i);
				String cursorValue = cursor.getString(i);
				L.v("������"+columnName+"--ֵ��"+cursorValue);
				
			}
			L.v("============================");
//			String cursorName = cursor.getColumnName(cursor.getPosition());
//			String cursorValue = cursor.getString(cursor.getColumnIndex(cursorName));
			
		}
	}
}
