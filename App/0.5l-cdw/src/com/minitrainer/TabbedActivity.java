package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class TabbedActivity extends Activity implements OnClickListener,OnTabChangeListener {
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	final double LEVEL_SCALE = 1.3;
	
	Intent intent;
	SessionManager session;
	SharedPreferences userState;
	SharedPreferences sp;
	Editor edit;
	TabEssentials tess;
	Typeface tfGS,tfNV;
	
	AlertDialog.Builder alertBMI,alertQuiz;
	TabHost tabHost;
	ScrollView scroll;
	TextView experience,level,BMI_fb,BMI,exsCmplt,progressOf,suggestion;
	EditText height, weight;
	Button calculateBMI, exerButton, dietButton,qButton;
	ProgressBar expBar;
	String resToString = "";
	int stLevel = 1;
	int qAnswered;
	
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        
        session = new SessionManager(getApplicationContext());
        if(!session.checkLogin()){finish();return;}
        
        userState = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences(userState.getString("username","username"),0);
        edit = sp.edit();
        alertBMI = new AlertDialog.Builder(this);
        alertQuiz = new AlertDialog.Builder(this);
        
		//ini typefaces
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        tfNV = Typeface.createFromAsset(getAssets(),"fonts/Nevis.otf");
        
        // textviews, progress bar, edittext ini
        expBar = (ProgressBar) findViewById(R.id.progress);//test
        level = (TextView) findViewById(R.id.currentLvl);
        experience = (TextView) findViewById(R.id.currentExp);
        BMI_fb = (TextView) findViewById(R.id.BMI_feedback);
        BMI = (TextView) findViewById(R.id.BMI_value);
        exsCmplt = (TextView) findViewById(R.id.completedExs);
        progressOf = (TextView) findViewById(R.id.text_progressOf);
        progressOf.setText("Progress of " + userState.getString("username","username"));
        suggestion = (TextView) findViewById(R.id.text_suggestion);
        suggestion.setTypeface(tfGS);
        
        
        height = (EditText) findViewById(R.id.editHeight);
        weight = (EditText) findViewById(R.id.editWeight);
        //fback = (TextView) findViewById(R.id.feedback);--
        
        //layout ini
        scroll = (ScrollView) findViewById(R.id.sv_tab2);
        
        //buttons ini
        calculateBMI = (Button) findViewById(R.id.button_calcBMI);
        exerButton = (Button) findViewById(R.id.exButton);
        dietButton = (Button) findViewById(R.id.dButton);
        qButton = (Button) findViewById(R.id.qBtn);
        
        // listeners ini
        calculateBMI.setOnClickListener(this);
        exerButton.setOnClickListener(this);
        dietButton.setOnClickListener(this);
        qButton.setOnClickListener(this); // test
		BMI.setOnClickListener(this);
			 
		
        
        // tab ini
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("");
        TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("");
        spec2.setContent(R.id.tab2);
        TabSpec spec3 = tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("");
        spec3.setContent(R.id.tab3);
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        intent = getIntent();
        tabHost.setCurrentTab(1);
        tabHost.setOnTabChangedListener(this);
        
        tess = new TabEssentials(tabHost);
		tess.alignTextInTabs();
		tess.setupTabs("#000000","#dadada","#ffffff",14,tabHost.getTabWidget().getChildCount(),tfNV);

        alignTextInTabs();
        loadProgressInfo();
    }
    
	@Override
	public void onTabChanged(String tabId) {
		setTabColor(tabHost);
	}
	
	public static void setTabColor(TabHost tabhost) {
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	    {
	        tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dadada")); //unselected
	    }
	    tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffff")); // selected
	}
    
    private void loadProgressInfo()
    {
    	String lvl = sp.getString("LEVEL", String.valueOf(stLevel));
    	String expGained = sp.getString("EXPERIENCE","0");
    	String expCap = sp.getString("EXPCAP","50");
    	String exsCompleted = sp.getString("EXSCMPLT", "0");
    	qAnswered = sp.getInt("QANSWERED", 0);
    	System.out.println(expGained + "/" + expCap + "numofex completed = " + exsCompleted);
    	level.setText(lvl);    	
    	stLevel = Integer.parseInt(lvl);
    	expBar.setMax(Integer.parseInt(expCap));
    	expBar.setProgress(Integer.parseInt(expGained));
    	experience.setText(expGained + "/" + expCap);
    	exsCmplt.setText(exsCompleted);
    	
    	if (stLevel % 2 == 0 && qAnswered  < 3)
    	{
    		qButton.setEnabled(true);
    		setupAlert();
    	}
    	else
    	{
    		qButton.setEnabled(false);
    	}
    }
   
    private void saveProgressInfo(String key, String val)
    {
    	edit.putString(key, val);
    	edit.putLong("lastUpdate", System.currentTimeMillis()/1000L);
    	edit.commit();
    }
    
    private void saveProgressInfo(String key, int val)
    {
    	edit.putInt(key, val);
    	edit.commit();
    }

	public void onClick(View v) 
	{
		Intent intent;
		switch (v.getId())
		{
			case R.id.exButton:
				intent = new Intent(this, ExerciseActivity.class);
				startActivityForResult(intent, 5);
				//startActivity(intent);
				break;
			case R.id.dButton:
				intent = new Intent(this, DietActivity.class);
				startActivity(intent);
				break;
			case R.id.button_calcBMI:
				calculateBMI(height.getText().toString(),weight.getText().toString());
				break;
				
			case R.id.qBtn:
				intent = new Intent(this, QuizActivity.class);
				intent.putExtra("level", stLevel);
				intent.putExtra("qAnswered", qAnswered);
				startActivityForResult(intent,6);
				break;
			case R.id.BMI_value :
				// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(TabbedActivity.this);
				View promptsView = li.inflate(R.layout.bmi_prompt, null);
 
				// set prompts.xml to alertdialog builder
				alertBMI.setView(promptsView);
 
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editHeight1);
				
				final EditText userInput2 = (EditText) promptsView
						.findViewById(R.id.editWeight1);
 
				// set dialog message
				alertBMI
					.setCancelable(false)
					.setPositiveButton("Get BMI",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
					    	calculateBMI(userInput.getText().toString(),userInput2.getText().toString());
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
				alertBMI.show();
				break;
			}
	}
	private void giveFbOnExercises()
	{
		//TBD
	}
	
	private void calculateBMI(String h1, String w1)
	{
		double res = 0;
		double w;
		int h = 0;
		if ((w1.trim().length()>0) && (h1.trim().length()>0) && Integer.parseInt(h1) != 0)
		{
			h = Integer.parseInt(h1);
			w = Double.parseDouble(w1);
			res = w / (h * 0.01 * h *0.01);
			resToString = String.format("%.2f",res);    	
			if (res <= UWL)
			{
				BMI.setText(resToString);
				BMI_fb.setText("You are underweight.");
			}
			if (res >UWL && res < NWL)
			{
				BMI.setText(resToString);
				BMI_fb.setText("Your weight is normal");
			}
			if (res > NWL && res <= OWL)
			{
				BMI.setText(resToString);
				BMI_fb.setText("You are overweight");
			}
			if (res > OWL)
			{
				BMI.setText(resToString);
				BMI_fb.setText("You need to lose some weight");
			}
			scroll.fullScroll(View.FOCUS_DOWN);
		}
		//reset progress test
		//edit.clear();
		//edit.commit();
	}
	
	private void increaseProgressBy(int val)
	{
		if (expBar.getProgress() + val >= expBar.getMax())
		{
			stLevel++;
			qAnswered = 0;
			level.setText(String.valueOf(stLevel));
			
			if ((val - (expBar.getMax() - expBar.getProgress())) > 0)
			{
				expBar.setProgress( val - (expBar.getMax() - expBar.getProgress()));
			}
			else
			{
				expBar.setProgress(0);
			}
			expBar.setMax((int)(expBar.getMax() * LEVEL_SCALE));
			experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
			Toast.makeText(this, "Congratulations!\nYou've reached level " + level.getText().toString() ,Toast.LENGTH_SHORT).show();
		}
		else
		{
			expBar.setProgress(expBar.getProgress() + val);
			experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
		}
		
		if (stLevel % 2 == 0  && qAnswered < 3)
		{
			qButton.setEnabled(true);
			saveProgressInfo("QANSWERED", qAnswered);
			setupAlert();
			
		}
		else
		{
			qButton.setEnabled(false);
		}
		
		saveProgressInfo("LEVEL",level.getText().toString());
		saveProgressInfo("EXPERIENCE",String.valueOf(expBar.getProgress()));
		saveProgressInfo("EXPCAP",String.valueOf(expBar.getMax()));
	}
	
    private void setupAlert()
    {
		alertQuiz.setTitle("Quiz available")
		.setMessage("Would you like to do the quiz now ?\nNote: the quiz is only available until you gain a new level!")
		.setCancelable(false).setPositiveButton("Go to Quiz!",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				intent = new Intent(TabbedActivity.this, QuizActivity.class);
				intent.putExtra("level", stLevel);
				intent.putExtra("qAnswered", qAnswered);
				startActivityForResult(intent,6);
			
			}
		  })
		.setNegativeButton("Later..",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		
		alertQuiz.show();
    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 5)
		{
			if (resultCode == RESULT_OK )
			{
				if ((data.getIntExtra("expPoints",0) != 0) && (data.getIntExtra("exercises_cmplt",0) != 0))
				{
					int got_exp = data.getIntExtra("expPoints",0);
					int got_exs = data.getIntExtra("exercises_cmplt",0);
					increaseProgressBy(got_exp);
					exsCmplt.setText(String.valueOf(Integer.parseInt(exsCmplt.getText().toString()) + got_exs));
					saveProgressInfo("EXSCMPLT",exsCmplt.getText().toString());
					//weight.setText(String.valueOf(got_exp));
				}	
				//int tabNumber = Integer.parseInt(data.getStringExtra("tabNo"));
				//tabHost.setCurrentTab(tabNumber);
			}
		}
		
		if (requestCode == 6)
		{
			if (resultCode == RESULT_OK )
			{
				if ((data.getIntExtra("expPoints",0) != 0) && (data.getIntExtra("qAnswered", 0) != 0))
				{
					int got_exp = data.getIntExtra("expPoints",0);
					int answered = data.getIntExtra("qAnswered",0);
					qAnswered = answered;
					increaseProgressBy(got_exp);
					saveProgressInfo("QANSWERED", answered);
				}	
			}
		}
		new pushToServer().execute();
	}
	
    private void alignTextInTabs()
    {
        //center align text in tabs code
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                //  get title text view
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {

                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tabbed, menu);
		return true;
	}

//Moved to LoginActivity.java as we can use the login checks there to conditionally call it
//based on whether the previous server connection worked.
/*	class pullFromServer extends AsyncTask<String,String,Boolean> {
		private ProgressDialog pDialog;
		private SharedPreferences userState;
		private SharedPreferences sp;
		private static final String KEY_USERNAME = "username";
		private static final String KEY_EXERCISESTOTAL = "exercisesTotal";
		private static final String KEY_EXPERIENCETOTAL = "experienceTotal";
		private static final String KEY_SUCCESS = "success";
		private static final String KEY_PREF_EXERCISESTOTAL = "EXSCMPLT";
		private static final String KEY_PREF_EXPERIENCETOTAL = "EXPERIENCE";
		private static final String KEY_LAST_UPDATE = "lastUpdate";
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_pull_user = "http://www.labs.callanwhite.co.uk/minitrainer/pull_profile.php";
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TabbedActivity.this);
			pDialog.setMessage("Updating user profile from server. Please wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected Boolean doInBackground(String... params) {
			userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			sp = getApplicationContext().getSharedPreferences(userState.getString("username","username"),0);
			String username = userState.getString("username","username");
			List<NameValuePair> dataToPull = new ArrayList<NameValuePair>();
			dataToPull.add(new BasicNameValuePair(KEY_USERNAME,username));
			
			JSONObject json = jsonParser.makeHttpRequest(url_pull_user, "POST", dataToPull);
			try{
				if(json==null) {
					return Boolean.valueOf(false);
				}
				else {
					if(json.getInt(KEY_SUCCESS)==1)
					{
						long deviceUpdate = sp.getLong(KEY_LAST_UPDATE, 0L);
						long serverUpdate = json.getLong(KEY_LAST_UPDATE);
						if(deviceUpdate<serverUpdate)
						{
							Editor edit = sp.edit();
							edit.putString(KEY_PREF_EXERCISESTOTAL,json.getString(KEY_EXERCISESTOTAL));
							edit.putString(KEY_PREF_EXPERIENCETOTAL, json.getString(KEY_EXPERIENCETOTAL));
							edit.putLong(KEY_LAST_UPDATE, serverUpdate);
							edit.commit();		
						}
						//no need to push the device timestamp if server is outdated, it will happen next time the device syncs
						return Boolean.valueOf(true);
					}
					else{
						return Boolean.valueOf(false);
					}
				}
				
			}
			catch(JSONException e){e.printStackTrace();}
			return Boolean.valueOf(false);
		}
		
		protected void onPostExecute(Boolean result) {
			pDialog.dismiss();
			if(result.booleanValue()) {
				Toast.makeText(TabbedActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(TabbedActivity.this, "Profile update was unsuccessful", Toast.LENGTH_SHORT).show();
			}
		}
	}*/
	
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
			pDialog = new ProgressDialog(TabbedActivity.this);
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
				int success;
				if(json==null)
				{
					success = 0;
				}
				else
				{
					success = json.getInt(KEY_SUCCESS);
				}

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
				Toast.makeText(TabbedActivity.this, "Update successful",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(TabbedActivity.this, "Update failed...",Toast.LENGTH_SHORT).show();
			}
			//session.logoutUser();
		}
		
	}

	
}
