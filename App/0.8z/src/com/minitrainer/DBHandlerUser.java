package com.minitrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandlerUser extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "usersManager";
	private static final String TABLE_USERS = "users";
	
	private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	//private static final String KEY_FORENAME = "forename";
	//private static final String KEY_SURNAME = "surname";
	private static final String KEY_EMAIL = "email";
	
	public DBHandlerUser(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT," + "UNIQUE(" + KEY_USERNAME + ") ON CONFLICT ROLLBACK" + ")";
		db.execSQL(CREATE_USERS_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}
	
	public void addUser(User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME,  user.getName());
		values.put(KEY_PASSWORD, user.getPassword());
		//values.put(KEY_FORENAME, user.getForename());
		//values.put(KEY_SURNAME, user.getSurname());
		values.put(KEY_EMAIL, user.getEmail());
		
		db.insert(TABLE_USERS, null, values);
		db.close();		
	}
	
	public User getUser(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null,null,null, null);
		if(cursor!=null)
		{
			cursor.moveToFirst();
		}
		else{return null;}
		User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		return user;
	}
	
	public boolean userExists(String username)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL}, KEY_USERNAME + "=?", new String[] {String.valueOf(username)},null,null,null,null);
		if(cursor.moveToFirst()){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public User getUser(String username)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL}, KEY_USERNAME + "=?", new String[] {String.valueOf(username)}, null,null,null,null);
		if(cursor.moveToFirst())
		{
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			return user;
		}
		return new User(0,"","","");

	}
	
	public int updateUser(User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, user.getName());
		values.put(KEY_PASSWORD, user.getPassword());
		//values.put(KEY_FORENAME, user.getForename());
		//values.put(KEY_SURNAME, user.getSurname());
		values.put(KEY_EMAIL, user.getEmail());
		return db.update(TABLE_USERS,  values,  KEY_ID + " = ?", new String[] {String.valueOf(user.getId())});
		
	}
	
	public void deleteUser(User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] { String.valueOf(user.getId())});
		db.close();
	}
}
