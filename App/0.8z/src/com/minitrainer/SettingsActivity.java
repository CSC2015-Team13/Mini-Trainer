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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minitrainer.ExerciseActivity.pushExerciseCooldowns;

public class SettingsActivity extends Activity implements OnClickListener{

	Button reset,tglSound,nameChange;
	boolean resetRequest,soundPar;
	TextView set1,set2,set3;
	static int yes;
	Typeface tfGS;
	Intent intent;
	String newName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		setContentView(R.layout.activity_settings);
		reset = (Button) findViewById(R.id.reset_all_progress);
		tglSound = (Button) findViewById(R.id.btn_music_state);
		nameChange = (Button) findViewById(R.id.changeName);
		intent = getIntent();
		Bundle extras = intent.getExtras();
		soundPar =  extras.getBoolean("SOUNDSTATE", true);
		System.out.println("received " + soundPar);
		if (soundPar)tglSound.setText("ON");
		else tglSound.setText("OFF");
		
		set1 = (TextView) findViewById(R.id.text_reset_prog);
		set2 = (TextView) findViewById(R.id.text_music);
		set3 = (TextView) findViewById(R.id.text_changeName);
		
		reset.setTypeface(tfGS);
		tglSound.setTypeface(tfGS);
		nameChange.setTypeface(tfGS);
		set1.setTypeface(tfGS);
		set2.setTypeface(tfGS);
		set3.setTypeface(tfGS);
		
		resetRequest = false;
		
		reset.setOnClickListener(this);
		tglSound.setOnClickListener(this);
		nameChange.setOnClickListener(this);
		yes = 0;
		
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.reset_all_progress)
		{
			setupResetAlert();
		}
		else
		{
			if (v.getId() == R.id.btn_music_state)
			{
				soundPar = !(soundPar);
				System.out.println("TURNED SOUND " + soundPar);
				if (soundPar)tglSound.setText("ON");
				else tglSound.setText("OFF");
			}
			else
			{
				setupNameChangeAlert();
			}
		}
	}
	
	public void resetProgress() {
		new resetOnServer().execute();
		
	}
	
	@Override
	public void onBackPressed() 
	{
		if (!resetRequest)
		{
			Toast.makeText(getBaseContext(), "back button pressed", Toast.LENGTH_SHORT).show();
			Bundle bundle = new Bundle();
			System.out.println("put " + soundPar);
			bundle.putBoolean("SOUNDSTATE", soundPar);
			System.out.println("put " + newName);
			bundle.putString("COSMETIC_USERNAME", newName);
			//bundle.putString("tabNo", "1");
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(99, intent);
		}
	    super.onBackPressed();
	    //finish();
	}
	
	private void setupNameChangeAlert()
	{
		final AlertDialog.Builder alert;
		alert = new AlertDialog.Builder(this);

		LayoutInflater li = LayoutInflater.from(SettingsActivity.this);
		View prompt = li.inflate(R.layout.name_prompt, null);
 
		alert.setView(prompt);
 
		final EditText userInput = (EditText) prompt.findViewById(R.id.editName);
		alert
		.setCancelable(false)
		.setPositiveButton("Change name",
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog,int id) {
			String nameTemp = userInput.getText().toString();
		    if (!(TextUtils.isEmpty(nameTemp) || nameTemp.length() < 3))
		    {
		    	newName = userInput.getText().toString();
		    	System.out.println("changed to " + newName);
		    	Toast.makeText(SettingsActivity.this, "Username has been changed to:\n" + newName, Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	Toast.makeText(SettingsActivity.this, "Username is too short", Toast.LENGTH_SHORT).show();
		    	setupNameChangeAlert();
		    }
		   }
		})
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
			dialog.cancel();
		    }
		  });
	alert.show();
	}
	
	private void setupResetAlert()
	{
		AlertDialog.Builder alert;
		alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Are you sure?")
		.setMessage("Do you really wish to\nRESET ALL PROGRESS?\nClick Yes three times" + " (" +yes + ")")
		.setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				yes++;
				if (yes >= 3)
				{
					resetProgress();
				}
				else
				{
					setupResetAlert();
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
				resetRequest = true;
				onBackPressed();
			}
			else
			{
				Toast.makeText(SettingsActivity.this, "Clearing progress failed\n Connection required", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
