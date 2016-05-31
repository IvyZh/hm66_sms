package com.ivy.sms.utils;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class ToastUtils {

	public static void ShowToast(FragmentActivity activity, String msg) {
		Toast.makeText(activity, msg, 0).show();
	}

}
