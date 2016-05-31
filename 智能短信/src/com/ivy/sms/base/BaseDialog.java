package com.ivy.sms.base;

import com.ivy.sms.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener{

	protected BaseDialog(Context context, int theme) {
		super(context, R.style.BaseDialog);
	}

	protected BaseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	protected BaseDialog(Context context) {
		this(context,R.style.BaseDialog);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initLinstener();
		initData();
	}

	public abstract void initView();
	public abstract void initLinstener();
	public abstract void initData();
	public abstract void processClick(View v);
	
	
	@Override
	public void onClick(View v) {
		processClick(v);
	}
}
