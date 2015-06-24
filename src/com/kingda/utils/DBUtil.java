package com.kingda.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtil extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;  //数据库版本号
    private static final String DATABASE_NAME= "Survey";  //数据库名称
    private  static final String HR_B_DEPT= "HR_B_DEPT";//部门

	public DBUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
