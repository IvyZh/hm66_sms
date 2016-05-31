package com.ivy.sms.base;

import com.ivy.sms.log.L;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * BaseActivity 继承v4.app.FragmentActivity为了兼容低版本
 * 
 * 所有Activity都需要继承这个基类
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
	 * 初始化布局和findViewById操作
	 */
	public abstract void initView();

	/**
	 * 初始化控件监听事件
	 */
	public abstract void initListener();

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 处理点击事件
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
