package com.buu.tourism;

import java.util.ArrayList;

public class GroupModel {

	public GroupModel(String id , String hid , String name){
		this.id = id ; 
		this.hid = hid ; 
		this.name = name ; 
	}
	
	public String id = "" ; 
	
	public String hid = "" ; 
	
	public String name = "" ; 
	
	
	
	public String mes = "还未有新消息提醒~" ; 
	
	public String time = "00:00" ; 
	
	public String num = "0" ; 
	
	
	/**
	 * 返回一个测试数据
	 * @return
	 */
	public static ArrayList<GroupModel> getDemoModelList(){
		ArrayList<GroupModel> list = new ArrayList<GroupModel>() ; 
		list.add( new GroupModel( "0001" , "0010" , "北京郊区游" )  );
		list.add( new GroupModel( "0002" , "0004" , "丽江玩" )  );
		list.add( new GroupModel( "0003" , "0007" , "河北保定" )  );
		list.add( new GroupModel( "0004" , "0019" , "Jay-杰伦" )  );
		list.add( new GroupModel( "0005" , "0012" , "穷游" )  );
		list.add( new GroupModel( "0006" , "0003" , "华而不实的诺言" )  );
		return list ; 
	}
	
	
	public static GroupModel getDemoModel(String gid){
		ArrayList<GroupModel> list = getDemoModelList();
		for( GroupModel model : list ){
			if( model.id.equals(gid) ){
				return model ; 
			}
		}
		return null ; 
	}
	
}
