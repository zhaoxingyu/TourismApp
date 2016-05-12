package com.buu.tourism.cache;

/**
 * 数据库字段定义
 * @author fenglei
 *
 */
public interface DBConstants {

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * db 数据库名字 
	 */
	public static final String DATABASE_NAME = "message_info.db" ; 
	
	/**
	 * db 版本
	 */
	public static final int DATABASE_VERSION = 1 ; 
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * domain表名称、列名定义
	 */
	public static final String TABLE_NAME_MES = "mes" ; 
	
	/**
	 * MES 自增id
	 */
	public static final String MES_COLUMN_ID = "id";
	
	/**
	 * MES 用户id
	 */
	public static final String MES_COLUMN_UID = "uid";
	
	/**
	 * MES 头像id
	 */
	public static final String MES_COLUMN_HID = "hid";
	
	/**
	 * MES 用户名
	 */
	public static final String MES_COLUMN_NAME = "name";
	
	/**
	 * MES 消息事件
	 */
	public static final String MES_COLUMN_TIME = "time";
	
	/**
	 * MES 消息正文
	 */
	public static final String MES_COLUMN_MES = "mes";
	
	/**
	 * MES 群组ID
	 */
	public static final String MES_COLUMN_GID = "gid";
	
	/**
	 * MES 是否已读标识位 0未读 1已读
	 */
	public static final String MES_COLUMN_READ = "read";
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * 创建 mes 表 sql 语句
	 */
	public static  final String CREATE_MES_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME_MES + " (" + 
					MES_COLUMN_ID + " INTEGER PRIMARY KEY," +
					MES_COLUMN_UID + " TEXT," +
					MES_COLUMN_HID + " TEXT," +
					MES_COLUMN_NAME + " TEXT," +
					MES_COLUMN_TIME + " TEXT," +
					MES_COLUMN_MES + " TEXT," +
					MES_COLUMN_GID + " TEXT," +
					MES_COLUMN_READ + " TEXT" +
			");";
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}
