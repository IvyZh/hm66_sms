package com.ivy.sms.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ivy.sms.R;
import com.ivy.sms.activity.GroupDetailActivity;
import com.ivy.sms.adapter.GroupAdapter;
import com.ivy.sms.constant.Constant;
import com.ivy.sms.dao.GroupDao;
import com.ivy.sms.dao.GroupThreadDao;
import com.ivy.sms.dao.SimpleQueryHandler;
import com.ivy.sms.dialog.CreateGroupDialog;
import com.ivy.sms.dialog.ListDialog;
import com.ivy.sms.dialog.ListDialog.OnListDialogLietener;
import com.ivy.sms.domain.Group;
import com.ivy.sms.interfaces.OnDialogClickLinstener;
import com.ivy.sms.utils.ToastUtils;

public class GroupFragment extends BaseFragment {

	private View view;
	private ListView lvGroup;
	private GroupAdapter adapter;

	@Override
	View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_group, null);
		lvGroup = (ListView) view.findViewById(R.id.lv_group_list);
		return view;
	}

	@Override
	void initData() {
		adapter = new GroupAdapter(getActivity(), null);
		lvGroup.setAdapter(adapter);
		
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		
		
		String[] projection = {
				"_id","createtime","count","name"
		};
		queryHandler.startQuery(0, adapter, Constant.URI.GROUP_QUERY, projection , null, null, "createtime desc");
		
//		ContentResolver resolver = getActivity().getContentResolver();
//		Cursor cursor = resolver.query(Constant.URI.GROUP_QUERY, null, null, null, null);
//		CursorUtils.print(cursor);
	}

	@Override
	void initListener() {
		view.findViewById(R.id.bt_group_newgroup).setOnClickListener(this);
		
		/***
		 * ����
		 */
		lvGroup.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				showListDialog(pos);
				return true;
			}
		});
		
		/**
		 * ���
		 */
		
		lvGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				//��תʱЯ��Ⱥ�����֡�Ⱥ��id
				Cursor cursor = (Cursor) adapter.getItem(position);
				Group group = Group.createFromCursor(cursor);
				if(group.getThread_count() > 0){
					//���Ⱥ���б����Ŀ����ת��Ⱥ����ϸActivity
					Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
					intent.putExtra("name", group.getName());
					intent.putExtra("group_id", group.get_id());
					startActivity(intent);
				}
				else{
					ToastUtils.ShowToast(getActivity(), "��ǰȺ��û���κλỰ");
				}
			}
		});
	}

	protected void showListDialog(final int pos) {
		ListDialog.showDialog(getActivity(), "ѡ�����", new String[]{"�޸�","ɾ��"}, new OnListDialogLietener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position==0){
					showEditDialog(pos);
				}else if(position==1){
					ContentResolver resolver = getActivity().getContentResolver();
					Cursor cursor = (Cursor) adapter.getItem(pos);
					Group group = Group.createFromCursor(cursor);
					GroupDao.delete(resolver, group.get_id());
					
					// ��Ҫ���� group.get_id() ɾ����Ӧthread_group������
					
					GroupThreadDao.clearByGroupId(getActivity().getContentResolver(), group.get_id());
					
				}
			}
		});
	}
	/**
	 * ��ʾ����Ի���
	 */
	protected void showEditDialog(final int pos) {
		CreateGroupDialog.showDialog(getActivity(),"�޸�Ⱥ��" ,new OnDialogClickLinstener() {
			@Override
			public void confirm(String text) {
				if(!TextUtils.isEmpty(text)){
					Cursor cursor = (Cursor) adapter.getItem(pos);
					Group group = Group.createFromCursor(cursor);
					ContentResolver resolver = getActivity().getContentResolver();
					
					ContentValues values = new ContentValues();
					values.put("name", text);
					GroupDao.update(resolver, values, group.get_id());
				}
			}
			
			@Override
			public void cacel() {
				
			}
		});
	}

	@Override
	void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_group_newgroup:
			showCreateDialog();
			break;
		}

	}
	
	private void showCreateDialog() {
		CreateGroupDialog.showDialog(getActivity(),new OnDialogClickLinstener() {
			
			@Override
			public void confirm(String text) {
				GroupDao.insert(getActivity().getContentResolver(), text);
			}
			
			@Override
			public void cacel() {
				
			}
		});
	}



}
