package com.buu.tourism;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 聊天程序主页面 入口
 * 
 * @author fenglei
 */
public class GroupListActivity extends Activity {

	public static GroupListActivity instance = null ; 
	
	
	
	public ListView listView = null;
	public GroupsModelAdapter groupsAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouplist);
		instance = this ; 
		
		groupsAdapter = new GroupsModelAdapter(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(groupsAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long arg3) {
				GroupModel model = groupsAdapter.list.get(index);
				MessageActivity.indexGroup = model ; 
				Intent intent = new Intent();
				intent.setClass(GroupListActivity.this, MessageActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 进入页面重新初始化数据
	 */
	public void onResume(){
		super.onResume(); 
		initData(); 
	}
	
	public void initData(){
		groupsAdapter.initData(); 
		groupsAdapter.notifyDataSetChanged(); 
	}
	
	
	public void onDestroy(){
		super.onDestroy(); 
		instance = null ; 
	}
}
