package com.minitrainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	TextView out;
	Button button_exAndDiets,button_profile,button_activities,button_prefs;
	Intent intent;
	SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		
		out = (TextView) findViewById(R.id.out);
        button_exAndDiets = (Button) findViewById(R.id.button_exDiets);
        button_profile = (Button) findViewById(R.id.button_prof);
        button_activities = (Button) findViewById(R.id.button_activities);
        button_prefs = (Button) findViewById(R.id.button_prefs);
        
        button_exAndDiets.setOnClickListener(this);
        button_profile.setOnClickListener(this);
        button_activities.setOnClickListener(this);
        button_prefs.setOnClickListener(this);
        
	}
	
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.button_exDiets:
			intent = new Intent(this, TabbedActivity.class);
			intent.putExtra("tabNo", "0");
			startActivity(intent);
			 //out.setText("Exercises and diets was pressed");
			 break;
		case R.id.button_prof:
			intent = new Intent(this, TabbedActivity.class);
			intent.putExtra("tabNo", "1");
			startActivity(intent);
			 break;
		case R.id.button_activities:
			intent = new Intent(this, TabbedActivity.class);
			intent.putExtra("tabNo", "2");
			startActivity(intent);
			 break;
		case R.id.button_prefs:
			 //out.setText("Preferences was pressed");
			new pushToServer().execute();
			//session.logoutUser();
			 break;
		}
	}
			 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	class pushToServer extends AsyncTask<String, String, String> {
		
		private ProgressDialog pDialog;
		private SharedPreferences userState;
		private SharedPreferences sp;
		boolean complete;
		private static final String KEY_ID = "id";
		private static final String KEY_USERNAME = "username";
		private static final String KEY_EXERCISESTOTAL = "exercisesTotal";
		private static final String KEY_EXPERIENCETOTAL = "experienceTotal";
		private static final String KEY_SUCCESS = "success";
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_push_user = "http://www.labs.callanwhite.co.uk/minitrainer/push_profile.php";
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Pushing userprofile to server. Please wait.");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}
		
		protected String doInBackground(String... params) {
			
			userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			sp = getApplicationContext().getSharedPreferences(userState.getString("username","username"),0);
			String username = userState.getString("username","username");
			String exercisesTotal = sp.getString("EXSCMPLT","0");
			//System.out.println(exercisesTotal);
			String experienceTotal = sp.getString("EXPERIENCE","0");
			
			List<NameValuePair> dataToPush = new ArrayList<NameValuePair>();
			dataToPush.add(new BasicNameValuePair(KEY_ID, "1"));
			dataToPush.add(new BasicNameValuePair(KEY_USERNAME,username));
			dataToPush.add(new BasicNameValuePair(KEY_EXERCISESTOTAL,exercisesTotal));
			dataToPush.add(new BasicNameValuePair(KEY_EXPERIENCETOTAL,experienceTotal));
			
			JSONObject json = jsonParser.makeHttpRequest(url_push_user, "POST", dataToPush);
			try {
				int success = json.getInt(KEY_SUCCESS);
				//System.out.println(success);
				if(success == 1) {
					//Intent i = getIntent();
					//finish();
					complete = true;
				}
				else {
					//failed to update
					complete = false;
				}
			}
			catch(JSONException e){e.printStackTrace();}
			return null;
			
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if(complete)
			{
				Toast.makeText(MainActivity.this, "Update successful",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(MainActivity.this, "Update failed...",Toast.LENGTH_SHORT).show();
			}
			session.logoutUser();
		}
		
	}

}
