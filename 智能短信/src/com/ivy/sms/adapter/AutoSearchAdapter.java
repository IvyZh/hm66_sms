package com.ivy.sms.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivy.sms.R;

public class AutoSearchAdapter extends CursorAdapter {

	public AutoSearchAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_auto_search_tv, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getHolder(view);
		
		String name = cursor.getString(1);
		String address = cursor.getString(0);
		holder.tv_autosearch_name.setText(name);
		holder.tv_autosearch_address.setText(address);
	}
	
	
	private ViewHolder getHolder(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;

	}

	class ViewHolder {
		private TextView tv_autosearch_name;
		private TextView tv_autosearch_address;

		public ViewHolder(View view) {
			tv_autosearch_name = (TextView) view
					.findViewById(R.id.tv_autosearch_name);
			tv_autosearch_address = (TextView) view
					.findViewById(R.id.tv_autosearch_address);
		}
	}

	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("data1"));
	}
	

}
