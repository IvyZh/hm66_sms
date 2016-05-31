package com.ivy.sms.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.ivy.sms.R;
import com.ivy.sms.adapter.AutoSearchAdapter;
import com.ivy.sms.base.BaseActivity;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.dao.SmsDao;
import com.ivy.sms.log.L;
import com.ivy.sms.utils.CursorUtils;

public class NewSmsActivity extends BaseActivity {

	private static final int PICK_CONTACT = 0;
	private EditText etSmsBody;
	private AutoCompleteTextView etSmsAddress;
	private AutoSearchAdapter adapter;

	@Override
	public void initView() {
		setContentView(R.layout.activity_newmsg);
		
		etSmsBody = (EditText) findViewById(R.id.et_newmsg_body);
		etSmsAddress = (AutoCompleteTextView) findViewById(R.id.et_newmsg_address);
		
//		etSmsAddress.setDropDownBackgroundDrawable(new ColorDrawable(android.R.color.holo_orange_dark));
//		etSmsAddress.setDropDownVerticalOffset(20);
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("新建短信");
	}

	@Override
	public void initListener() {
		findViewById(R.id.bt_newmsg_send).setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.iv_newmsg_select_contact).setOnClickListener(this);
		
		
	}

	@Override
	public void initData() {
		
		adapter = new AutoSearchAdapter(this, null);
		etSmsAddress.setAdapter(adapter);
		
//		final SimpleQueryHandler queryHandler = new SimpleQueryHandler(getContentResolver());
		
		
		final String[] projection = {
                "data1",
                "display_name",
                "_id"
        };
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			
			@Override
			public Cursor runQuery(CharSequence constraint) {
				L.v(constraint.toString());
				String text = constraint.toString();
//				queryHandler.startQuery(0, adapter, Phone.CONTENT_URI, projection, "data1 like '%"+text+"%'", null, null);
				Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, projection, "data1 like '%"+text+"%'", null, null);
				return cursor;
			}
		});
		
	}

	@Override
	public void processClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_newmsg_send:
			String address = etSmsAddress.getText().toString().trim();
			String body = etSmsBody.getText().toString().trim();
			
			if(TextUtils.isEmpty(address)||TextUtils.isEmpty(body)){
				Toast.makeText(this, "号码或内容不能为空", 0).show();
			}else{
				SmsDao.sendSms(this, address, body);
			}
			
			break;
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.iv_newmsg_select_contact:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("vnd.android.cursor.dir/contact");
			startActivityForResult(intent, PICK_CONTACT);

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==PICK_CONTACT){
			Uri uri = data.getData();// content://com.android.contacts/contacts/lookup/0r2-574F/2
			if(uri!=null){
				String [] projection = {
						"_id",
						"has_phone_number"
				};
				Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//				CursorUtils.print(cursor);
				cursor.moveToFirst();
				String _id = cursor.getString(0);
				int has_phone_number = cursor.getInt(1);
				if(has_phone_number==1){
					String selection = "contact_id = " + _id;
					Cursor cursor2 = getContentResolver().query(Phone.CONTENT_URI, new String[]{"data1"}, selection, null, null);
					cursor2.moveToFirst();
					String data1 = cursor2.getString(0);
					
					etSmsAddress.setText(data1);
					//内容输入框获取焦点
					etSmsAddress.requestFocus();
				}else{
					Toast.makeText(this, "联系人没有号码", 0).show();
				}
				
			}
			

		}
	}

}
