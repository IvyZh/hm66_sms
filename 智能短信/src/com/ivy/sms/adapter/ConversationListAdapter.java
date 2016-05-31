package com.ivy.sms.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.dao.ContactDao;
import com.ivy.sms.domain.Conversation;
import com.ivy.sms.log.L;

public class ConversationListAdapter extends CursorAdapter {
	
	private boolean isSelectMode = false;//�ǲ���ѡ��ģʽ
	private ArrayList<Integer> selectedThreadIds = new ArrayList<Integer>();

	public ConversationListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.item_conversation, null);
//		return view;
		View view = View.inflate(context,R.layout.item_conversation, null);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getViewHolder(view);
		
		Conversation conversation = Conversation.createFromCursor(cursor);
		
		//���ö����������û���
		int msg_count = conversation.getMsg_count();
		String address = conversation.getAddress();
		address = ContactDao.getNameByAddress(context.getContentResolver(), address);
		String text = address+"("+msg_count+")";
		holder.tvName.setText(text);
		
		//�������һ����������
		holder.tvSnappet.setText(conversation.getSnippet());
		
		//����ͷ��
		
		Bitmap bitmap = ContactDao.getBitmapByAddress(context.getContentResolver(), address);
		if(bitmap!=null){
			holder.ivPortrait.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}else{
//			L.v(address+"--δ����ͷ��-");
		}
		
		//ʱ���ʽ��
		long date = conversation.getDate();
		if(DateUtils.isToday(date)){
			holder.tvDate.setText(DateFormat.getTimeFormat(context).format(date));
		}else{
			holder.tvDate.setText(DateFormat.getDateFormat(context).format(date));
		}
		
		if(isSelectMode){
			holder.ivCheck.setVisibility(View.VISIBLE);
			int thread_id = conversation.getThread_id();
			if(selectedThreadIds.contains(Integer.valueOf(thread_id))){
				holder.ivCheck.setBackgroundResource(R.drawable.common_checkbox_checked);
			}else{
				holder.ivCheck.setBackgroundResource(R.drawable.common_checkbox_normal);
			} 
			
		}else{
			holder.ivCheck.setVisibility(View.GONE);
		}
		
	}
	
	
	private ViewHolder getViewHolder(View view){
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder==null){
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		
		return holder;
	}
	
	class ViewHolder {
		private TextView tvName;
		private TextView tvDate;
		private ImageView ivPortrait;
		private TextView tvSnappet;
		private ImageView ivCheck;
		
		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			tvDate = (TextView) view.findViewById(R.id.tv_date);
			tvSnappet = (TextView) view.findViewById(R.id.tv_snappet);
			ivPortrait = (ImageView) view.findViewById(R.id.iv_portrait);
			ivCheck = (ImageView) view.findViewById(R.id.iv_check);
		}
	
	}

	public void setSelectMode(boolean isSelectMode) {
		this.isSelectMode = isSelectMode;
	}

	public boolean getIsSelectMode() {
		return isSelectMode;
	}
	
	/**
	 * ��ѡ�еĻỰ���뵽���ϵ���
	 * @param position
	 */
	public void setSingle(int position){
		Cursor cursor = (Cursor) getItem(position);
		Conversation conversation = Conversation.createFromCursor(cursor);
		int thread_id = conversation.getThread_id();
		if(selectedThreadIds.contains(Integer.valueOf(thread_id))){
			selectedThreadIds.remove(Integer.valueOf(thread_id));
		}else{
			selectedThreadIds.add(thread_id);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * ȫѡ
	 */
	public void setSelectAll(){
		selectedThreadIds.clear();
		int count = getCount();
		for (int i = 0; i < count; i++) {
			Cursor cursor = (Cursor) getItem(i);
			Conversation conversation = Conversation.createFromCursor(cursor);
			int thread_id = conversation.getThread_id();
			if(selectedThreadIds.contains(Integer.valueOf(thread_id))){
				selectedThreadIds.remove(Integer.valueOf(thread_id));
			}else{
				selectedThreadIds.add(thread_id);
			}
		}
		notifyDataSetChanged();
	}
	
	/**
	 * ȡ��ȫѡ
	 */
	public void setNoSelectAll(){
		selectedThreadIds.clear();
	}
	
	
	public void printSeletedTheadIds(){
		for (Integer id : selectedThreadIds) {
			L.v("---id---"+id);
		}
	}

	public ArrayList<Integer> getSelectedThreadIds() {
		return selectedThreadIds;
	}
	
	
	
	
}
