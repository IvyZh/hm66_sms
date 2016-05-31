package com.ivy.sms.activity;

import java.util.ArrayList;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.ivy.sms.R;
import com.ivy.sms.R.color;
import com.ivy.sms.adapter.MainAdapter;
import com.ivy.sms.base.BaseActivity;
import com.ivy.sms.fragment.ConversationFragment;
import com.ivy.sms.fragment.GroupFragment;
import com.ivy.sms.fragment.SearchFragment;
import com.ivy.sms.log.L;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends BaseActivity {

	private ViewPager vpMain;
	private ArrayList<Fragment> fragments;
	private TextView tvConversation;
	private TextView tvGroup;
	private TextView tvSearch;
	private MainAdapter adapter;
	private View viewTag;
	private int displayWidth;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		vpMain = (ViewPager) findViewById(R.id.vp_main);
		
		tvConversation = (TextView) findViewById(R.id.tv_conversation);
		tvGroup = (TextView) findViewById(R.id.tv_group);
		tvSearch = (TextView) findViewById(R.id.tv_search);
		
		viewTag = findViewById(R.id.view_tab);
		
		displayWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params = viewTag.getLayoutParams();
		params.width = displayWidth/3;
		viewTag.setLayoutParams(params);
	}

	@Override
	public void initListener() {
		
		findViewById(R.id.ll_tab_conversation).setOnClickListener(this);
		findViewById(R.id.ll_tab_group).setOnClickListener(this);
		findViewById(R.id.ll_tab_search).setOnClickListener(this);
		
		vpMain.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				textSizeAndScale(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
//				L.v("--positionOffsetPixels-"+positionOffsetPixels);
				ViewPropertyAnimator.animate(viewTag).translationX(position*(displayWidth/3)+positionOffsetPixels/3).setDuration(0);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	protected void textSizeAndScale(int position) {
		tvConversation.setTextColor(position==0?Color.WHITE:Color.parseColor("#ADAAAD"));
		tvGroup.setTextColor(position==1?Color.WHITE:Color.parseColor("#ADAAAD"));
		tvSearch.setTextColor(position==2?Color.WHITE:Color.parseColor("#ADAAAD"));
		
		ViewPropertyAnimator.animate(tvConversation).scaleX(position==0?1.2f:1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tvGroup).scaleX(position==1?1.2f:1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tvSearch).scaleX(position==2?1.2f:1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tvConversation).scaleY(position==0?1.2f:1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tvGroup).scaleY(position==1?1.2f:1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tvSearch).scaleY(position==2?1.2f:1.0f).setDuration(200);
		
	}

	@Override
	public void initData() {
		FragmentManager fm = getSupportFragmentManager();
		fragments = new ArrayList<Fragment>();
		
		ConversationFragment conversationFragment = new ConversationFragment();
		GroupFragment groupFragment = new GroupFragment();
		SearchFragment searchFragment = new SearchFragment();
		fragments.add(conversationFragment);
		fragments.add(groupFragment);
		fragments.add(searchFragment);
		adapter = new MainAdapter(fm, fragments);
		vpMain.setAdapter(adapter);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_tab_conversation:
			vpMain.setCurrentItem(0);
			break;
		case R.id.ll_tab_group:
			vpMain.setCurrentItem(1);
			break;
		case R.id.ll_tab_search:
			vpMain.setCurrentItem(2);
			break;

		}
	}



}
