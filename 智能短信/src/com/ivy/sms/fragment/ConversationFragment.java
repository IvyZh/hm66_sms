package com.ivy.sms.fragment;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ivy.sms.R;
import com.ivy.sms.activity.ConversionDetailActivity;
import com.ivy.sms.activity.NewSmsActivity;
import com.ivy.sms.adapter.ConversationListAdapter;
import com.ivy.sms.base.BaseActivity;
import com.ivy.sms.constant.Constant;
import com.ivy.sms.dao.GroupDao;
import com.ivy.sms.dao.GroupThreadDao;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.dialog.ConfirmDialog;
import com.ivy.sms.dialog.ConfirmDialog.OnConfirmClickLintener;
import com.ivy.sms.dialog.DeleteSmsDialog;
import com.ivy.sms.dialog.DeleteSmsDialog.OnClickLintener;
import com.ivy.sms.dialog.ListDialog;
import com.ivy.sms.dialog.ListDialog.OnListDialogLietener;
import com.ivy.sms.domain.Conversation;
import com.ivy.sms.domain.Group;
import com.ivy.sms.log.L;
import com.ivy.sms.utils.CursorUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ConversationFragment extends BaseFragment {

	private View view;
	private View llEditMenu;
	private View llSelectMenu;
	private ListView lvConversation;
	private ConversationListAdapter adapter;
	private boolean stopDelete;
	protected final static int DELETE_SMS_DONE = 0;
	protected final static int DELETE_SMS_PROCESS = 1;
	
	private  Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DELETE_SMS_DONE:
				stopDelete = false;
				deleteSmsDialog.dismiss();
				adapter.setSelectMode(false);
				showEditMenu();
				adapter.notifyDataSetChanged();
				break;
			case DELETE_SMS_PROCESS :
				deleteSmsDialog.updateProcess(msg.arg1);
				break;

			}
			
		};
	};
	private DeleteSmsDialog deleteSmsDialog;

	@Override
	View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_conversation	, null);
		
		llEditMenu = view.findViewById(R.id.ll_menu_edit);
		llSelectMenu = view.findViewById(R.id.ll_menu_select);
		lvConversation = (ListView) view.findViewById(R.id.lv_conversation);
		return view;
	}

	@Override
	void initData() {
		
		adapter = new ConversationListAdapter(getActivity(), null);
		lvConversation.setAdapter(adapter);
		
		ContentResolver resolver = getActivity().getContentResolver();
//		Cursor cursor = resolver.query(Constant.URI.SMS_CONVERSATION, null, null, null, null);
//		CursorUtils.print(cursor);
		
		String[] projects  = {
				"sms.body AS snippet",
				"sms.thread_id AS _id",
				"groups.msg_count AS msg_count",
				"date AS date",
				"address AS address"
		};
		
		//�첽�����������
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(resolver);
		queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projects, null, null, "date desc");
	}

	@Override
	void initListener() {
		view.findViewById(R.id.bt_conversation_delete).setOnClickListener(this);
		view.findViewById(R.id.bt_conversation_edit).setOnClickListener(this);
		view.findViewById(R.id.bt_conversation_new).setOnClickListener(this);
		view.findViewById(R.id.bt_conversation_select_all).setOnClickListener(this);
		view.findViewById(R.id.bt_conversation_select_cancle).setOnClickListener(this);
		
		
		//��lvConversation������Ŀ����¼�
		
		lvConversation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//����Ǳ༭ģʽ
				if(adapter.getIsSelectMode()){
					adapter.setSingle(arg2);
				}else{
					//����Ự����ҳ��
					Cursor cursor = (Cursor) adapter.getItem(arg2);
					Conversation conversation = Conversation.createFromCursor(cursor);
					ConversionDetailActivity.startActivity((BaseActivity) getActivity(),conversation.getThread_id(),conversation.getAddress());
				}
			}
		});
		
		lvConversation.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				Cursor cursor = (Cursor) adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				//���ȵõ��û�������Ŀ��Ӧ��ϵ�˵�thread_id
				int thread_id = conversation.getThread_id();
				//ͨ��thread_id���ж��û��Ƿ��Ѿ����뵽С��
				boolean hasGroup = GroupThreadDao.hasGroup(getActivity().getContentResolver(), thread_id);
				
				if(hasGroup){
					
					//ͨ��thread_id�����groups_thread���ҵ����Ӧ��group_id
					int group_id = GroupThreadDao.getGroupIdByThreadId(getActivity().getContentResolver(), thread_id);
					
					L.v("----: hasGroup--group_id"+group_id);
					
					showExitGroupDialog(group_id,thread_id);
					
				}else{
					L.v("----hasGroup: "+hasGroup);
					showJoinGroupDialog(conversation.getThread_id());
					
					
				}
				
				
				return true;
			}
		});
	}

	/**
	 * ��ʾ�û��Ѿ������С��
	 * @param group_id С���id
	 * @param thread_id ��ϵ�˵�thread_id
	 */
	protected void showExitGroupDialog(final int group_id,final int thread_id) {
		//ͨ��group_id�ҵ�name
		String groupName = GroupDao.getGroupNameById(getActivity().getContentResolver(),group_id);
		ConfirmDialog.showDialog(getActivity(), "��ʾ", "�Ự�Ѿ�����["+groupName+"]���飬�Ƿ�Ҫ�˳��÷��飿", new OnConfirmClickLintener() {
			
			@Override
			public void confirm() {
				GroupThreadDao.delete(getActivity().getContentResolver(), thread_id,group_id);
			}
			@Override
			public void cancel() {
				
			}
		});
	}

	protected void showJoinGroupDialog(final int thread_id) {
		
		final Cursor cursor = GroupDao.query(getActivity().getContentResolver());
		
		int count = cursor.getCount();
		
		if(cursor.getCount() == 0){
			return;
		}
		
		String [] items = new String[count];
		//����cursor��ȡ������
		while(cursor.moveToNext()){
			Group group = Group.createFromCursor(cursor);
			//��ȡ����Ⱥ������֣�����Ⱥ����ȫ�����뵽һ��string���ϵ��С�
			items[cursor.getPosition()] = group.getName();
		}
		ListDialog.showDialog(getActivity(), "ѡ�����", items, new OnListDialogLietener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				cursor.moveToPosition(position);
//				CursorUtils.print(cursor);
				Group group = Group.createFromCursor(cursor);
				int get_id = group.get_id();
				L.v("Group---"+group);
				//0[_id=2, name=����, createtime=1464679083810, count=0]
				GroupThreadDao.insert(getActivity().getContentResolver(), get_id, thread_id);
				
			}
		});
	}

	@Override
	void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_conversation_edit:
			showSelectMenu();
			adapter.setSelectMode(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_select_cancle:
			showEditMenu();
			adapter.setSelectMode(false);
			adapter.setNoSelectAll();
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_new:
			Intent intent = new Intent(getActivity(),NewSmsActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.bt_conversation_delete:
			
			ArrayList<Integer> selectedThreadIds = adapter.getSelectedThreadIds();
			if(selectedThreadIds.size()==0){
				Toast.makeText(getActivity(), "û��ѡ�ж���", 0).show();
				return;
			}
			ConfirmDialog.showDialog(getActivity(),"��ʾ","ȷ��Ҫɾ��ô?",new OnConfirmClickLintener() {
				
				@Override
				public void confirm() {
					showDeleteProcessDialog();
				}
				
				@Override
				public void cancel() {
					
				}
			});
			
			break;
		case R.id.bt_conversation_select_all:
			
			adapter.setSelectAll();
			break;

		}
	}

	/**
	 * ��ʾɾ�����ŵĽ���
	 */
	protected void showDeleteProcessDialog() {
		deleteSmsDialog = DeleteSmsDialog.showDialog(getActivity(), adapter.getSelectedThreadIds().size(),new OnClickLintener() {
			
			@Override
			public void cancel() {
				stopDelete = true;
			}
		});
		deleteSms();
	}

	/***
	 * ɾ��ѡ�еĶ���
	 */
	private void deleteSms() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<Integer> selectedThreadIds = adapter.getSelectedThreadIds();
				
				for (int i = 0; i < selectedThreadIds.size(); i++) {
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(stopDelete)
						break;
					
					Message msg = Message.obtain();
					String where = "thread_id = "+selectedThreadIds.get(i);
					getActivity().getContentResolver().delete(Constant.URI.SMS, where, null);
					L.v("---------delete------"+i);
					msg.what = DELETE_SMS_PROCESS;
					msg.arg1 = i+1;
					handler.sendMessage(msg);
				}
				
				selectedThreadIds.clear();
				handler.sendEmptyMessage(DELETE_SMS_DONE);
				
			}
		}).start();
		
		
		
	}

	/**
	 * ��ʾѡ��Menu
	 */
	private void showSelectMenu() {
		L.v("---llSelectMenu.getHeight()--"+llSelectMenu.getHeight());
		ViewPropertyAnimator.animate(llEditMenu).translationY(llSelectMenu.getHeight()).setDuration(200);
		ViewPropertyAnimator.animate(llSelectMenu).translationY(0).setDuration(200);
	}

	/**
	 * ��ʾ�༭Menu
	 */
	private void showEditMenu() {
		ViewPropertyAnimator.animate(llSelectMenu).translationY(llSelectMenu.getHeight()).setDuration(200);
		ViewPropertyAnimator.animate(llEditMenu).translationY(0).setDuration(200);
	}

}
