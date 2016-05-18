package com.buu.tourism;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.buu.tourism.cache.DBCacheManager;

public class MessageActivity extends Activity {

	public static GroupModel indexGroup = null;
	public static ArrayList<MessageModel> messageList = null;

	public static ListView listView = null;
	public static MessageAdapter messageAdapter = null;

	public EditText edit_mes = null;
	public Button returnBut = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		Intent intent = getIntent();
		String gid = intent.getStringExtra("gid") ;
		if( gid != null && gid.equals("") == false ){
			GroupModel m = GroupModel.getDemoModel(gid) ; 
			if( m != null ) indexGroup = m ; 
		}
		
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		TextView groupName = (TextView) findViewById(R.id.groupname);
		if (indexGroup != null) {
			groupName.setText(indexGroup.name);
		}

		edit_mes = (EditText) findViewById(R.id.edit_mes);
		returnBut = (Button) findViewById(R.id.return_but);
		returnBut.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// 失去焦点 关闭键盘
				edit_mes.clearFocus();
				View view = getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}

				String str = edit_mes.getText().toString();
				if (str.equals("") || str.length() <= 0) {
					return;
				}

				MessageModel mesModel = new MessageModel();
				mesModel.mes = new String(str);
				mesModel.gid = indexGroup.id;
				MessageSend.getInstance().request(mesModel);
				edit_mes.setText("");

				messageList.add(mesModel);
			}
		});

		MessageActivity.messageList = DBCacheManager.getInstance().db
				.getMessageList(indexGroup.id);
		messageAdapter = new MessageAdapter(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(messageAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int index, long arg3) {
			}
		});

		listView.setSelection(messageAdapter.getCount() - 1);
		messageAdapter.notifyDataSetChanged();
		
		DBCacheManager.getInstance().upreadMessageRead( indexGroup.id );
	}

	public static void addMessage(MessageModel model) {
		if (messageAdapter == null)
			return;
		if (messageList == null)
			return;
		if (model.uid.equals(User.id))
			return;

		messageList.add(model);
		listView.setSelection(messageAdapter.getCount() - 1);
		messageAdapter.notifyDataSetChanged();
	}
	
	
	public void onDestroy(){
		super.onDestroy(); 
		indexGroup = null ;
	}
	
}
