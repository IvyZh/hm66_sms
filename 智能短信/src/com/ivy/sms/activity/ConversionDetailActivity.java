package com.ivy.sms.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.adapter.ConversationDetailAdapter;
import com.ivy.sms.base.BaseActivity;
import com.ivy.sms.constant.Constant;
import com.ivy.sms.dao.ContactDao;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.dao.SmsDao;
import com.ivy.sms.log.L;

public class ConversionDetailActivity extends BaseActivity {
	
	private ListView lvConversationDetail;
	private int thread_id;
	private TextView tv_title;
	private String address;
	private EditText etConversationDetail;

	public static void startActivity(BaseActivity activity,int thread_id,String address){
		Intent intent = new Intent(activity,ConversionDetailActivity.class);
		intent.putExtra("thread_id", thread_id);
		intent.putExtra("address", address);
		activity.startActivity(intent);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_conversation_detail);
		findViewById(R.id.iv_back).setOnClickListener(this);
		lvConversationDetail = (ListView) findViewById(R.id.lv_conversation_detail);
		tv_title = (TextView) findViewById(R.id.tv_title);
		etConversationDetail = (EditText) findViewById(R.id.et_conversation_detail);
		
		//只要ListView刷新，就会滑动
		lvConversationDetail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	@Override
	public void initListener() {
		
		findViewById(R.id.bt_conversation_detail_send).setOnClickListener(this);

	}

	@Override
	public void initData() {
		
		thread_id = getIntent().getIntExtra("thread_id", -1);
		address = getIntent().getStringExtra("address");
		
		
		ConversationDetailAdapter adapter = new ConversationDetailAdapter(this, null,lvConversationDetail);
		
		lvConversationDetail.setAdapter(adapter);
		
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.SMS, null, "thread_id = "+thread_id, null, "date asc");
		
		
		String name = ContactDao.getNameByAddress(getContentResolver(), address);
		tv_title.setText(name);
		
	}

	@Override
	public void processClick(View v) {
		switch ( v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_conversation_detail_send:
			String sms = etConversationDetail.getText().toString().trim();
			L.v(sms);
			if(TextUtils.isEmpty(sms)){
				return;
			}
			sendSms(sms);
			break;
		}
	}

	/**
	 * 发送短信
	 * @param sms
	 */
	private void sendSms(String sms) {
		SmsDao.sendSms(this, address, sms);
	}

}
