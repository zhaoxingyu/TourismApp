package com.buu.tourism;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buu.tourism.cache.DBCacheManager;

public class GroupsModelAdapter extends BaseAdapter{

	public Context context = null;
	
	public ArrayList<GroupModel> list = null ; 
	
	public GroupsModelAdapter(Context context){
		this.context = context;
		initData(); 
	}
	
	public void initData(){
		this.list = GroupModel.getDemoModelList() ; 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (list == null) ? 0 : list.size() ;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		GroupModel model = (GroupModel)getItem(position) ; 
		ArrayList<MessageModel> messageList = DBCacheManager.getInstance().db.getMessageList(model.id) ; 
		
		if( messageList != null && messageList.size() > 0 ) {
			MessageModel lastMessage =  messageList.get(messageList.size()-1) ; 
			model.mes = lastMessage.mes ; 
			model.time = Tools.getStringDateShort("HH:mm", Long.valueOf( lastMessage.time ) ) ; 
		}
		
		ArrayList<MessageModel> unreadMessageList = DBCacheManager.getInstance().db.getUnreadMessageList(model.id) ;
		model.num = String.valueOf( unreadMessageList.size() );

		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.group_list_view_item, null) ;
		}

		ImageView hid = (ImageView)convertView.findViewById(R.id.hid) ;
		hid.setImageResource( ResConfig.getInstance().getDrawableId( model.hid ) );
		
		TextView name = (TextView)convertView.findViewById(R.id.name) ;
		name.setText( model.name );
		
		TextView mes = (TextView)convertView.findViewById(R.id.mes) ;
		mes.setText( model.mes );
		
		TextView time = (TextView)convertView.findViewById(R.id.time) ;
		if(  messageList.size() == 0 ){
			time.setVisibility(View.INVISIBLE);
		} else {
			time.setVisibility(View.VISIBLE);
			time.setText( model.time );
		}
		
		
		TextView num = (TextView)convertView.findViewById(R.id.num) ;
		if( model.num.equals("0") ){
			num.setVisibility(View.INVISIBLE);
		}else{
			num.setVisibility(View.VISIBLE);
			num.setText( model.num );
		}
		
		return convertView;
	}

}
