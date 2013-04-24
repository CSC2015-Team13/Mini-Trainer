package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{

	AlertDialog.Builder alert;
	Button reset;
	TextView tv;
	static int yes;
	Typeface tfGS;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		setContentView(R.layout.activity_settings);
		reset = (Button) findViewById(R.id.reset_all_progress);
		tv = (TextView) findViewById(R.id.text_reset_prog);
		reset.setTypeface(tfGS);
		tv.setTypeface(tfGS);
		
		reset.setOnClickListener(this);
		alert = new AlertDialog.Builder(this);
		yes = 0;
		
	}
	
	public void resetProgress() {
		new resetOnServer().execute();
		
	}
	
	@Override
	public void onBackPressed() 
	{
		Toast.makeText(getBaseContext(), "back button pressed", Toast.LENGTH_SHORT).show(); 
	    super.onBackPressed();
	    //finish();
	}
	
	private void setupAlert()
	{
		alert.setTitle("Are you sure?")
		.setMessage("Do you really want to\n RESET ALL PROGRESS?\nClick Yes three times" + " (" +yes + ")")
		.setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				yes++;
				if (yes >= 3)
				{
					resetProgress();
				}
				else
				{
					setupAlert();
				}
			}
		  })
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		
		alert.show();
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
		private static final String PREF_LAST_EX_CMPLT = "LASTCMPLT";
		private static final String PREF_QANSWERED = "QANSWERED";
		
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
						edit.putString(PREF_LAST_EX_CMPLT, "0");
						edit.putString(PREF_QANSWERED, "0");
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
				setResult(RESULT_OK);
				onBackPressed();
				
				// how to go back to login and close all activities so I would not be able to go back to SettingsActivity???
				//Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//|FLAG_ACTIVITY_SINGLE_TOP);FLAG_ACTIVITY_REORDER_TO_FRONT
				//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				//startActivity(intent);
				//finish();
			}
			else
			{
				Toast.makeText(SettingsActivity.this, "Clearing progress failed\n Connection required", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.reset_all_progress)
		{
			setupAlert();
		}
		
	}
}
