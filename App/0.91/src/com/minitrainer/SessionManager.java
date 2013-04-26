package com.minitrainer;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;

	
	private static final String IS_LOGIN = "IsLoggedIn";
	private static final String KEY_NAME = "username";
	
	public SessionManager(Context context)
	{
		this._context = context;
		pref = PreferenceManager.getDefaultSharedPreferences(_context);
		editor = pref.edit();
	}
	
	//Create a login session within shared preferences
	public void createLoginSession(String name)
	{
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_NAME, name);
		editor.commit();
	}
	
	//Retrieve user details from the shared preferences
	public HashMap<String, String> retrieveUserDetails()
	{
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_NAME, pref.getString(KEY_NAME,null));
		return user;
	}
	
	//Validate (check) if a user is logged in and return to x activity if not
	public boolean checkLogin()
	{
		if(!this.isLoggedIn())
		{
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
			return false;
		}
		return true;
	}
	
	//Log a user out and clear the shared preferences data
	public void logoutUser()
	{
		editor.clear();
		editor.commit();
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
