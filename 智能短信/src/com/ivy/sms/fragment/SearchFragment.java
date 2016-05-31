package com.ivy.sms.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.ivy.sms.R;
import com.ivy.sms.activity.ConversionDetailActivity;
import com.ivy.sms.adapter.ConversationListAdapter;
import com.ivy.sms.constant.Constant;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.domain.Conversation;

public class SearchFragment extends BaseFragment {

	private EditText et_search_list;
	private ListView lv_search_list;
	private ConversationListAdapter adapter;
	private SimpleQueryHandler queryHandler;
	String[] projection = {
			"sms.body AS snippet",
			"sms.thread_id AS _id",
			"groups.msg_count AS msg_count",
			"address AS address",
			"date AS date"
	};
	
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		et_search_list = (EditText) view.findViewById(R.id.et_search_list);
		lv_search_list = (ListView) view.findViewById(R.id.lv_search_list);
		return view;
	}

	@Override
	void initData() {
		adapter = new ConversationListAdapter(getActivity(), null);
		lv_search_list.setAdapter(adapter);
		
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
	}

	@Override
	void initListener() {
		//添加文本改变侦听
		et_search_list.addTextChangedListener(new TextWatcher() {
			
			//文本只要一改变，此方法调用
			//s:当前文本框的文本
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, 
						projection, "body like '%" + s + "%'", null, "date desc");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		lv_search_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//直接跳转至ConversationDetailActivity
				//进入会话详细
				Intent intent = new Intent(getActivity(), ConversionDetailActivity.class);
				//携带数据：address和thread_id
				Cursor cursor = (Cursor) adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				intent.putExtra("address", conversation.getAddress());
				intent.putExtra("thread_id", conversation.getThread_id());
				startActivity(intent);
			}
		});
		
		lv_search_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//输入法管理器
				//隐藏输入法软键盘
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});

	}

	@Override
	void processClick(View view) {
		// TODO Auto-generated method stub

	}

}
