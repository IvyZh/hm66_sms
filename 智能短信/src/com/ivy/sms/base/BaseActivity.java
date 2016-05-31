package com.ivy.sms.base;

import com.ivy.sms.log.L;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * BaseActivity �̳�v4.app.FragmentActivityΪ�˼��ݵͰ汾
 * 
 * ����Activity����Ҫ�̳��������
 * 
 * @author Ivy
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}

	/**
	 * ��ʼ�����ֺ�findViewById����
	 */
	public abstract void initView();

	/**
	 * ��ʼ���ؼ������¼�
	 */
	public abstract void initListener();

	/**
	 * ��ʼ������
	 */
	public abstract void initData();

	/**
	 * �������¼�
	 * 
	 * @param v
	 */
	public abstract void processClick(View v);

	@Override
	public void onClick(View v) {
		L.v("onClick--" + v.getId());
		processClick(v);
	}
}
