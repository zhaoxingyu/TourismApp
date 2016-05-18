package com.buu.tourism;

import org.json.JSONObject;


/**
 * 单条消息数据模型
 * @author fenglei
 *
 */
public class MessageModel {

	/**
	 * 消息ID
	 */
	public String id = "-1" ; 
	
	/**
	 * 用户ID
	 */
	public String uid = "-1" ; 
	
	/**
	 * 头像ID
	 */
	public String hid = "-1" ;
	
	/**
	 * 用户名字
	 */
	public String name = "-1" ; 
	
	/**
	 * 消息时间
	 */
	public String time = "-1" ; 
	
	/**
	 * 消息正文
	 */
	public String mes = "-1" ; 
	
	/**
	 * 群组ID
	 */
	public String gid = "-1" ; 
	
	/**
	 * 是否已读标识位
	 */
	public String read = "-1" ;
	
	
	public MessageModel(){
		
		this.uid = User.id ;
		this.hid = User.hid ; 
		this.name = User.name ;
		
		this.read = String.valueOf(0) ; 
		this.time = String.valueOf( System.currentTimeMillis() );
	}
	
	
	public static MessageModel createModel(String jsonStr){
		MessageModel model = new MessageModel() ; 
		try {
			JSONObject jsonObj_data = new JSONObject(jsonStr) ;
			JSONObject jsonObj = new JSONObject(jsonObj_data.getString( "data" )) ;
			model.uid = jsonObj.getString("uid") ; 
			model.hid = jsonObj.getString("hid") ; 
			model.name = jsonObj.getString("name") ; 
			model.time = jsonObj.getString("time") ; 
			model.mes = jsonObj.getString("mes") ; 
			model.gid = jsonObj.getString("gid") ; 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); 
			model = null ; 
		}
		return model ; 
	}
	
	public String toString(){
		String str = "" ;
		str += "id=" + id + " - " ; 
		str += "uid=" + uid + " - " ; 
		str += "hid=" + hid + " - " ; 
		str += "name=" + name + " - " ; 
		str += "time=" + time + " - " ; 
		str += "mes=" + mes + " - " ; 
		str += "gid=" + gid + "\n" ; 
		return str ; 
	}
	
	
	/**
	 * 返回外面需要用到的 json str 数据
	 */
	public String toJsonStr(){
		JSONObject jsonObj = new JSONObject();  
		try {
			jsonObj.put("uid", uid) ; 
			jsonObj.put("hid", hid) ; 
			jsonObj.put("name", name) ; 
			jsonObj.put("time", time) ; 
			jsonObj.put("mes", mes) ; 
			jsonObj.put("gid", gid) ; 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); 
		}
		return jsonObj.toString() ; 
	}
	
}
