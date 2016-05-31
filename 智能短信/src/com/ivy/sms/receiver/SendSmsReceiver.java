package com.ivy.sms.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SendSmsReceiver extends BroadcastReceiver {
	
	public static final String ACTION_SEND_SMS = "com.ivy.sms.sendsms";

	@Override
	public void onReceive(Context context, Intent intent) {
		int code = getResultCode();
		if(code == Activity.RESULT_OK){
			Toast.makeText(context, "发送成功", 0).show();
		}else{
			Toast.makeText(context, "发送失败", 0).show();
		}
	}
}
