package com.ivy.sms.log;

import android.util.Log;

public class L {
	private L(){};//˽�л�������
	
	private final static String TAG = "Ivy";
	private static final boolean flag = true;
	
	public static void v(String msg){
		if(flag)
			Log.v(TAG,msg);
	}

}
