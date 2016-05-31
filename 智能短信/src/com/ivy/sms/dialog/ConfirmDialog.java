package com.ivy.sms.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.base.BaseDialog;

public class ConfirmDialog extends BaseDialog {

	private String title;
	private String msg;
	private TextView tvTitle;
	private TextView tvDesc;
	private OnConfirmClickLintener mLintener;

	protected ConfirmDialog(Context context) {
		super(context);
	}

	public static void showDialog(Context context, String title, String msg,OnConfirmClickLintener mLintener) {
		ConfirmDialog dialog = new ConfirmDialog(context);
		
		dialog.setTitle(title);
		dialog.setMsg(msg);
		dialog.setMessage(msg);
		dialog.setLintener(mLintener);
		
		dialog.show();
	}

	public void setLintener(OnConfirmClickLintener mLintener) {
		this.mLintener = mLintener;
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_confirm);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvDesc = (TextView) findViewById(R.id.tv_dialog_des);
	}

	@Override
	public void initLinstener() {
		
		findViewById(R.id.bt_dialog_cancel).setOnClickListener(this);
		findViewById(R.id.bt_dialog_confirm).setOnClickListener(this);

	}

	@Override
	public void initData() {
		tvDesc.setText(msg);
		tvTitle.setText(title);
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dialog_cancel:
			if(mLintener!=null)
				mLintener.cancel();
			break;

		case R.id.bt_dialog_confirm:
			if(mLintener!=null)
				mLintener.confirm();
			break;
		}
		
		dismiss();
	}
	
	public interface OnConfirmClickLintener{
		void confirm();
		void cancel();
	}
}
