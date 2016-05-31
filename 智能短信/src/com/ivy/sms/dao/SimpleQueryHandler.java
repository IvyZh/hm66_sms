package com.ivy.sms.dao;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.ivy.sms.log.L;
import com.ivy.sms.utils.CursorUtils;

/**
 * 异步查询
 */
public class SimpleQueryHandler extends AsyncQueryHandler {

	public SimpleQueryHandler(ContentResolver cr) {
		super(cr);
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);
		L.v("------SimpleQueryHandler-----查询完毕");
		if(cursor!=null){
			CursorUtils.print(cursor);
		}else{
			L.v("cursor==null");
		}

		if(cookie!=null && cookie instanceof CursorAdapter){
			L.v("---notifyDataSetChanged---");
			((CursorAdapter)cookie).changeCursor(cursor);
		}
	}

}
