package com.ivy.sms.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ivy.sms.R;
import com.ivy.sms.base.BaseDialog;
import com.ivy.sms.interfaces.OnDialogClickLinstener;

public class CreateGroupDialog extends BaseDialog {

	private EditText etGroupName;
	private OnDialogClickLinstener linstener;
	private TextView tv_dialog_title;
	private String title;

	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public static void showDialog(Context context,OnDialogClickLinstener linstener) {
		CreateGroupDialog dialog = new CreateGroupDialog(context);
		dialog.setView(new EditText(context));
		dialog.setLinstener(linstener);
		dialog.show();
	}
	public static void showDialog(Context context,String title,OnDialogClickLinstener linstener) {
		CreateGroupDialog dialog = new CreateGroupDialog(context);
		dialog.setView(new EditText(context));
		dialog.setLinstener(linstener);
		dialog.setTitle(title);
		dialog.show();
	}

	protected CreateGroupDialog(Context context) {
		super(context);
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_create_group);
		etGroupName = (EditText) findViewById(R.id.et_name);
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
	}

	@Override
	public void initLinstener() {
		findViewById(R.id.bt_dialog_cancel).setOnClickListener(this);
		findViewById(R.id.bt_dialog_confirm).setOnClickListener(this);
	}

	@Override
	public void initData() {
		tv_dialog_title.setText(title);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dialog_cancel:
			if(linstener!=null){
				linstener.cacel();
			}
			break;
		case R.id.bt_dialog_confirm:
			String text = etGroupName.getText().toString();
			if(TextUtils.isEmpty(text)){
				Toast.makeText(getContext(), "小组名不能为空", 0).show();
				return;
			}
			if(linstener!=null){
				linstener.confirm(text);
			}
			break;
		}
		dismiss();
	}


	public void setLinstener(OnDialogClickLinstener linstener) {
		this.linstener = linstener;
	}
	

}
