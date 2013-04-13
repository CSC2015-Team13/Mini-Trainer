package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}
	
	public void resetProgress(View v) {
		new resetOnServer().execute();
	}
	
	class resetOnServer extends AsyncTask<String,String,Boolean> {
		SharedPreferences userState;
		SharedPreferences usersPreferences;
		String username;
		
		private static final String KEY_USERNAME = "username";
		private static final String KEY_SUCCESS = "success";
		private static final String PREF_EXERCISES_COMPLETE = "EXSCMPLT";
		private static final String PREF_EXPERIENCE = "EXPERIENCE";
		private static final String PREF_LEVEL = "LEVEL";
		
		JSONParser jsonParser = new JSONParser();
		private static final String URL_RESET_PROGRESS = "http://www.labs.callanwhite.co.uk/minitrainer/reset_progress.php";
		
		public resetOnServer()
		{
			this.userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			this.usersPreferences = getApplicationContext().getSharedPreferences(userState.getString(KEY_USERNAME,KEY_USERNAME),0);
			this.username = userState.getString(KEY_USERNAME,KEY_USERNAME);
		
		}
		
		public Boolean doInBackground(String... params)
		{
			List<NameValuePair> dataPush = new ArrayList<NameValuePair>();
			dataPush.add(new BasicNameValuePair(KEY_USERNAME,username));
			
			JSONObject json = jsonParser.makeHttpRequest(URL_RESET_PROGRESS, "POST", dataPush);
			try {
				if(json==null)
				{
					return Boolean.valueOf(false);
				}
				else {
					if(json.getInt(KEY_SUCCESS)==1)
					{
						//need to change the level cap as well
						Editor edit = usersPreferences.edit();
						edit.putString(PREF_EXPERIENCE, "0");
						edit.putString(PREF_EXERCISES_COMPLETE, "0");
						edit.putString(PREF_LEVEL, "1");
						edit.commit();
						return Boolean.valueOf(true);
					}
					else
					{
						return Boolean.valueOf(false);
					}
				}
			}
			catch(JSONException e){e.printStackTrace();}
			return Boolean.valueOf(false);
		}
		
		public void onPostExecute(Boolean result)
		{
			if(result.booleanValue()) {
				Toast.makeText(SettingsActivity.this, "Cleared progress", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(SettingsActivity.this, "Clearing progress failed", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
