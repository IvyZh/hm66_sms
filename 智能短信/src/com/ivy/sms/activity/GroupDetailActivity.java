package com.ivy.sms.activity;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.adapter.ConversationListAdapter;
import com.ivy.sms.base.BaseActivity;
import com.ivy.sms.constant.Constant;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.domain.Conversation;
import com.ivy.sms.log.L;

public class GroupDetailActivity extends BaseActivity {

	private TextView tv_title;
	private ListView lv_group_detail;
	private int groupId;
	private ConversationListAdapter adapter;

	@Override
	public void initView() {
		setContentView(R.layout.activity_group_detail);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_group_detail = (ListView) findViewById(R.id.lv_group_detail);
	}

	@Override
	public void initListener() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		
		lv_group_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//进入会话详情页面
				Cursor cursor = (Cursor) adapter.getItem(arg2);
				Conversation conversation = Conversation.createFromCursor(cursor);
				ConversionDetailActivity.startActivity(GroupDetailActivity.this,conversation.getThread_id(),conversation.getAddress());
			}
		});
	}

	@Override
	public void initData() {
		String name = getIntent().getStringExtra("name");
		groupId = getIntent().getIntExtra("group_id", 0);
		tv_title.setText(name);
		
		adapter = new ConversationListAdapter(this, null);
		lv_group_detail.setAdapter(adapter);
		
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getContentResolver());
		
		String[] projection = {
				"sms.body AS snippet",
				"sms.thread_id AS _id",
				"groups.msg_count AS msg_count",
				"address AS address",
				"date AS date"
		};
		
		
		L.v("--groupId--"+groupId);
		queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projection, buildQuery(), null, null);
		
		
	}

	/***
	 * 自定义查询条件
	 * @return
	 */
	private String buildQuery() {
		//查询当前群组包含的所有会话的thread_id，这些会话都会被显示在GroupDetailActivity的界面中
		Cursor cursor = getContentResolver().query(Constant.URI.GROUP_THREAD_QUERY, new String[]{"thread_id"}, "group_id = " + groupId, null, null);
		String selection = "thread_id in (";
		while (cursor.moveToNext()) {
			if(cursor.isLast())
				//最后一个会话id后面不要逗号
				selection += cursor.getString(cursor.getColumnIndex("thread_id"));
			else
				selection += cursor.getString(0) + ", ";
		}
		selection += ")";
		L.v("---buildQuery---"+selection);
		return selection;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		}

	}

}
