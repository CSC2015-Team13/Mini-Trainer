package com.example.buttontest;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;
	
	int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME = "MiniTrainerPref";
	private static final String IS_LOGIN = "IsLoggedIn";
	private static final String KEY_NAME = "username";
	private static final String KEY_FORENAME = "forename";
	private static final String KEY_SURNAME = "surname";
	
	public SessionManager(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	//Create a login session within shared preferences
	public void createLoginSession(String name, String forename, String surname)
	{
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_FORENAME, forename);
		editor.putString(KEY_SURNAME, surname);
		editor.commit();
	}
	
	//Retrieve user details from the shared preferences
	public HashMap<String, String> retrieveUserDetails()
	{
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_NAME, pref.getString(KEY_NAME,null));
		user.put(KEY_FORENAME, pref.getString(KEY_FORENAME,  null));
		user.put(KEY_SURNAME, pref.getString(KEY_SURNAME, null));
		return user;
	}
	
	//Validate (check) if a user is logged in and return to x activity if not
	public void checkLogin()
	{
		if(!this.isLoggedIn())
		{
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}
	
	//Log a user out and clear the shared preferences data
	public void logoutUser()
	{
		editor.clear();
		editor.commit();
		//and redirect to x activity (to be created)
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}
	
	//login check
	public boolean isLoggedIn()
	{
		return pref.getBoolean(IS_LOGIN,  false);
	}
	
}
