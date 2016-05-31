package com.ivy.sms.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.base.BaseDialog;

public class DeleteSmsDialog extends BaseDialog {

	private String title;
	private TextView tvTitle;
	private OnClickLintener mLintener;
	private ProgressBar pb;
	private int max;
	private int process;

	protected DeleteSmsDialog(Context context) {
		super(context);
	}

	public static DeleteSmsDialog showDialog(Context context, int max, OnClickLintener mLintener) {
		DeleteSmsDialog dialog = new DeleteSmsDialog(context);
		dialog.setMax(max);
		dialog.setTitle("É¾³ýÖÐ...(0"+"/"+max+")");
		dialog.setLintener(mLintener);
		dialog.show();
		return dialog;
	}

	public void setLintener(OnClickLintener mLintener) {
		this.mLintener = mLintener;
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_delete_sms);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		pb = (ProgressBar) findViewById(R.id.pb_dialog_des);
	}

	@Override
	public void initLinstener() {
		
		findViewById(R.id.bt_dialog_cancel).setOnClickListener(this);

	}

	@Override
	public void initData() {
		tvTitle.setText(title);
		pb.setMax(max);
		pb.setProgress(0);
	}

	private void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dialog_cancel:
			if(mLintener!=null)
				mLintener.cancel();
			break;		
		}
		dismiss();
	}
	
	public interface OnClickLintener{
		void cancel();
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}
	
	public void updateProcess(int process){
		pb.setProgress(process);
		tvTitle.setText("É¾³ýÖÐ...("+process+"/"+max+")");
	}
	
	
	
}
