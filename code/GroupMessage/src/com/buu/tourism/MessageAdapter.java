package com.buu.tourism;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	public Context context = null;

	public MessageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (MessageActivity.messageList == null) ? 0
				: MessageActivity.messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MessageActivity.messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		MessageModel model = (MessageModel) getItem(position);

		if (model.uid.equals(User.id) == false) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.message_list_view_item, null);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.message_list_view_item_my, null);
		}

		ImageView hid = (ImageView)convertView.findViewById(R.id.hid) ;
		hid.setImageResource( ResConfig.getInstance().getDrawableId( model.hid ) );
		
		TextView name = (TextView)convertView.findViewById(R.id.name) ;
		name.setText( model.name );
		
		TextView mes = (TextView)convertView.findViewById(R.id.mes) ;
		mes.setText( model.mes );

		return convertView;
	}

}
