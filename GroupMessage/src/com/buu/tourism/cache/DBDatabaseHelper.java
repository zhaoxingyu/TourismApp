package com.buu.tourism.cache;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.buu.tourism.MessageModel;

public class DBDatabaseHelper extends SQLiteOpenHelper implements DBConstants{

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * 资源锁
     */
    private final static byte synLock[] = new byte[1];
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
     * 构造函数
     * @param context
     */
    public DBDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 创建数据库
     * 
     * 残酷的现实告诉我们，创建多个表时，要分开多次执行db.execSQL方法！！
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//Log.d("DB", "onCreate") ;
		db.execSQL(CREATE_MES_TABLE_SQL);
	}

	/**
	 * 数据库版本更新策略（直接放弃旧表）
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//Log.d("DB", "onUpgrade") ;
        if (oldVersion != newVersion) {
            // 其它情况，直接放弃旧表.
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_MES_TABLE_SQL + ";");
            db.setTransactionSuccessful();
            db.endTransaction();
            onCreate(db);
        }
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 添加一条数据
	 * @param model
	 * @return
	 */
	public synchronized void addMessageModel( MessageModel model ){
		synchronized (synLock) {
	        SQLiteDatabase db = getWritableDatabase();
	        ContentValues cv = new ContentValues();
	        cv.put(MES_COLUMN_UID, model.uid);
	        cv.put(MES_COLUMN_HID, model.hid);
	        cv.put(MES_COLUMN_NAME, model.name);
	        cv.put(MES_COLUMN_TIME, model.time);
	        cv.put(MES_COLUMN_MES, model.mes);
	        cv.put(MES_COLUMN_GID, model.gid);
	        cv.put(MES_COLUMN_READ, model.read);
	        db.insert(TABLE_NAME_MES, null, cv);
		}
	}
	
	/**
	 * 获取全部聊天记录
	 * @return
	 */
	public ArrayList<MessageModel> getMessageList(){
		synchronized (synLock) {
			ArrayList<MessageModel> list = new ArrayList<MessageModel>() ; 
			StringBuilder sql = new StringBuilder();
	        sql.append("SELECT * FROM ");
	        sql.append(TABLE_NAME_MES);
	        sql.append(" ; ");
	        SQLiteDatabase db = getReadableDatabase();
	        Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql.toString(), null);
				if (cursor != null && cursor.getCount() > 0) {
	                cursor.moveToFirst();
	                do {
	                	MessageModel mes = new MessageModel();
	                	mes.id = cursor.getString(cursor.getColumnIndex(MES_COLUMN_ID));
	                	mes.uid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_UID));
	                	mes.hid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_HID));
	                	mes.name = cursor.getString(cursor.getColumnIndex(MES_COLUMN_NAME));
	                	mes.time = cursor.getString(cursor.getColumnIndex(MES_COLUMN_TIME));
	                	mes.mes = cursor.getString(cursor.getColumnIndex(MES_COLUMN_MES));
	                	mes.gid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_GID));
	                	mes.read = cursor.getString(cursor.getColumnIndex(MES_COLUMN_READ));
	                    list.add(mes);
	                } while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list ; 
		}
	}
	
	
	/**
	 * 获取分组全部聊天记录
	 * @return
	 */
	public ArrayList<MessageModel> getMessageList(String gid ){
		synchronized (synLock) {
			ArrayList<MessageModel> list = new ArrayList<MessageModel>() ; 
			StringBuilder sql = new StringBuilder();
	        sql.append("SELECT * FROM ");
	        sql.append(TABLE_NAME_MES);
	        sql.append(" WHERE ");
	        sql.append(MES_COLUMN_GID);
	        sql.append(" =? ");
	        sql.append(" ; ");
	        SQLiteDatabase db = getReadableDatabase();
	        Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql.toString(), new String[]{ gid });
				if (cursor != null && cursor.getCount() > 0) {
	                cursor.moveToFirst();
	                do {
	                	MessageModel mes = new MessageModel();
	                	mes.id = cursor.getString(cursor.getColumnIndex(MES_COLUMN_ID));
	                	mes.uid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_UID));
	                	mes.hid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_HID));
	                	mes.name = cursor.getString(cursor.getColumnIndex(MES_COLUMN_NAME));
	                	mes.time = cursor.getString(cursor.getColumnIndex(MES_COLUMN_TIME));
	                	mes.mes = cursor.getString(cursor.getColumnIndex(MES_COLUMN_MES));
	                	mes.gid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_GID));
	                	mes.read = cursor.getString(cursor.getColumnIndex(MES_COLUMN_READ));
	                    list.add(mes);
	                } while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list ; 
		}
	}
	
	public ArrayList<MessageModel> getUnreadMessageList(String gid){
		synchronized (synLock) {
			ArrayList<MessageModel> list = new ArrayList<MessageModel>() ;
			StringBuilder sql = new StringBuilder();
	        sql.append("SELECT * FROM ");
	        sql.append(TABLE_NAME_MES);
	        sql.append(" WHERE ");
	        sql.append(MES_COLUMN_GID);
	        sql.append(" =? ");
	        sql.append(" AND ");
	        sql.append(MES_COLUMN_READ);
	        sql.append(" =? ;");
	        SQLiteDatabase db = getReadableDatabase();
	        Cursor cursor = null;
			
			try {
				cursor = db.rawQuery(sql.toString(), new String[] { gid, "0" });
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					do {
	                	MessageModel mes = new MessageModel();
	                	mes.id = cursor.getString(cursor.getColumnIndex(MES_COLUMN_ID));
	                	mes.uid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_UID));
	                	mes.hid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_HID));
	                	mes.name = cursor.getString(cursor.getColumnIndex(MES_COLUMN_NAME));
	                	mes.time = cursor.getString(cursor.getColumnIndex(MES_COLUMN_TIME));
	                	mes.mes = cursor.getString(cursor.getColumnIndex(MES_COLUMN_MES));
	                	mes.gid = cursor.getString(cursor.getColumnIndex(MES_COLUMN_GID));
	                	mes.read = cursor.getString(cursor.getColumnIndex(MES_COLUMN_READ));
	                    list.add(mes);
	                } while (cursor.moveToNext());
				}
	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				cursor.close();
				db.close() ;
	        }
			
			return list ; 
		}
	}
	
	
	
	
	/** 
	 * 更新是否已读状态
	 */
	public void updateMessageModel( MessageModel model ){
		synchronized (synLock) {
			
			ContentValues cv = new ContentValues();
			cv.put(MES_COLUMN_READ, model.read);
			
			StringBuilder where = new StringBuilder();
			where.append(MES_COLUMN_ID);
            where.append(" = ? ");
            
            String[] args = new String[] { String.valueOf(model.id) };
            
            SQLiteDatabase db = getWritableDatabase();
            db.update(TABLE_NAME_MES, cv, where.toString(), args);
		}
	}
	
    /**
     * 清除缓存数据
     */
	public void clear() {
	    synchronized (synLock) {
	        SQLiteDatabase db = getWritableDatabase();
	        try {
	            db.delete(TABLE_NAME_MES, null, null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            db.close();
	        }
        }
	}
	
}