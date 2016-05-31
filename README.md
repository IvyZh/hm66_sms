# hm66_sms
黑马66期智能短信

## 问题汇总

1. 很奇怪：就是继承`BaseFragment`和`BaseActivity`的时候IDE会报错，把子类和父类放到一个包内，就可以了。
2. 给TextView设置字体颜色时,不能这样使用`tvConversation.setTextColor(position==0?Color.WHITE:R.color.text_color);`
3. ViewPager 由第一个Pager直接切到第三个的时候会在第二个Pager的地方卡一下
4. `android:translationY="60dp"`属性可以在布局文件中改变控件位置
5. 异步查询`public class SimpleQueryHandler extends AsyncQueryHandler`
6. 看Android短信的源码
7. 查询短信会话的条件和注意事项：（需要查看短信源码对project的处理）

		String[] projects  = {
					"sms.body AS snippet",
					"sms.thread_id AS _id",
					"groups.msg_count AS msg_count",
					"date AS date",
					"address AS address"
			};
		
		//异步请求短信内容
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(resolver);
		queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projects, null, null, null);
8. SimpleQueryHandler里面的处理

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			L.v("------SimpleQueryHandler-----查询完毕");
			CursorUtils.print(cursor);
			if(cookie!=null && cookie instanceof CursorAdapter){
				L.v("---notifyDataSetChanged---");
				((CursorAdapter)cookie).changeCursor(cursor);
			}
		}

9. 在JavaBean中createFromCursor()方法中取id

		String thread_id = cursor.getString(cursor.getColumnIndex("_id"))
10. 根据address查找用户名

		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = cr.query(uri, new String []{PhoneLookup.DISPLAY_NAME}, null, null, null);
11. 根据address查找用户名的头像
	
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = cr.query(uri, new String []{PhoneLookup._ID}, null, null, null);
		if(cursor.moveToFirst()){
			String _id = cursor.getString(0);
			
			InputStream is = Contacts.openContactPhotoInputStream(cr, Uri.withAppendedPath(Contacts.CONTENT_URI, _id));
			
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		}

12. 全局设置对话框BaseDialog，可以用style来实现

		public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener{
		
			protected BaseDialog(Context context, int theme) {
				super(context, R.style.BaseDialog);
			}


	    <style name="BaseDialog" parent="@android:style/Theme.Dialog">
	        <item name="android:windowBackground">@drawable/base_dialog_bg</item>
	    </style>

13. 设置LsitView滑动模式

		//只要ListView刷新，就会滑动
		lvConversationDetail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);	

14. 进入ConversationDetail界面后自动滑动到底部：
	1. 需要将ListView传到ConversationDetailAdapter界面，
	2. 然后重写adapter的`changeCursor`方法

			@Override
			public void changeCursor(Cursor cursor) {
				int count = cursor.getCount();
				lv.setSelection(count);
				super.changeCursor(cursor);
			}

15. 用广播接受者来提示用户短信发送情况

		Intent intent = new Intent(SendSmsReceiver.ACTION_SEND_SMS);
		//短信发出去后，系统会发送一条广播，告知我们短信发送是成功还是失败
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for (String text : smss) {
			//这个api只负责发短信，不会把短信写入数据库
			manager.sendTextMessage(address, null, text, sentIntent, null);
		
			//把短信插入短信数据库
			insertSms(context, address, text);
		}


		public class SendSmsReceiver extends BroadcastReceiver {
			
			public static final String ACTION_SEND_SMS = "com.ivy.sms.sendsms";
		
			@Override
			public void onReceive(Context context, Intent intent) {
				int code = getResultCode();
				if(code == Activity.RESULT_OK){
					Toast.makeText(context, "发送成功", 0).show();
				}else{
					Toast.makeText(context, "发送失败", 0).show();
				}
			}
		}


16. AutoCompleteTextView的使用
	1. `android:completionThreshold` 自动匹配的字数限制

			AutoSearchAdapter adapter = new AutoSearchAdapter(this, null);
			etSmsAddress.setAdapter(adapter);
			
			
			adapter.setFilterQueryProvider(new FilterQueryProvider() {
				
				@Override
				public Cursor runQuery(CharSequence constraint) {
					L.v(constraint.toString());
					return null;
				}
			});
	2. AutoSearchAdapter 是继承`CursorAdapter`

17. 不让软键盘自动弹出`android:windowSoftInputMode="adjustPan|adjustUnspecified"`


18. 根据数字，进行模糊查询
		
		queryHandler.startQuery(0, adapter, Phone.CONTENT_URI, projection, "data1 like '%"+text+"%'", null, null);

19. 如果不是异步查询

				adapter.setFilterQueryProvider(new FilterQueryProvider() {
					
					@Override
					public Cursor runQuery(CharSequence constraint) {
						L.v(constraint.toString());
						String text = constraint.toString();
					//	queryHandler.startQuery(0, adapter, Phone.CONTENT_URI, projection, "data1 like '%"+text+"%'", null, null);
						Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, projection, "data1 like '%"+text+"%'", null, null);
						return cursor;
					}
				});

20. 点击下拉列表条目时的返回值

		@Override
		public CharSequence convertToString(Cursor cursor) {
			return cursor.getString(cursor.getColumnIndex("data1"));
		}

21. 设置下拉列表的背景和距离等

		etSmsAddress.setDropDownBackgroundDrawable(new ColorDrawable(android.R.color.holo_orange_dark));
		etSmsAddress.setDropDownVerticalOffset(20);

22. 调用系统的选择联系人界面

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("vnd.android.cursor.dir/contact");
		startActivityForResult(intent, PICK_CONTACT);

23. 让输入对话框可以弹出对话框

		public static void showDialog(Context context) {
				CreateGroupDialog dialog = new CreateGroupDialog(context);
				dialog.setView(new EditText(context));
				dialog.show();
			}

24. 自己创建的数据库内容改变了之后需要通知一下


		//这个是自己创建的数据库只能调用notifychange方法，让其帮忙刷新。
		getContext().getContentResolver().notifyChange(BASE_URI, null);

	同时还要配合查询时的内容观察者使用：

		if(match == GROUP_QUERY){
			Cursor cursor = db.query(GROUP_TABLE, null, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		}

25. cursor.moveToPosition(position);
26. 千万不要删文件 想三遍
27. cursor.getString(2)  不要这么用
28. 隐藏键盘

		//隐藏输入法软键盘
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
