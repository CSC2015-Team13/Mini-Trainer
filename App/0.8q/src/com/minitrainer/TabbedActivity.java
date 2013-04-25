package com.minitrainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TabbedActivity extends Activity implements OnClickListener,OnTabChangeListener {
	
	final double LEVEL_SCALE = 1.25;// maybe 1.3 as it was or leave 1.23?
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	final int MAX_LEVEL = 30;
	final int MIN_LEVEL = 1;
	
	final int EXERCISE_NORM = 8;
	final int MILLIS_DAY = 3600 * 24 *1000;
	final int QUIZ_UNLOCK_PARAM = 3;
	final int QUIZ_ANSWER_LIMIT = 3;
	final int FACT_PARAM = 2;
	
	final int RESCODE_EXERCISES = 5;
	final int RESCODE_QUIZ = 6;
	final int RESCODE_SETTINGS = 777;
	
	final int POSCODE_EXIT = 0;
	final int NEGCODE_EXIT = 0;
	final int POSCODE_QUIZ = 1;
	final int NEGCODE_QUIZ = 1;
	final int POSCODE_FACTS = 2;
	final int NEGCODE_FACTS = 2;
	
	Intent intent;
	SessionManager session;
	SharedPreferences userState;
	SharedPreferences sp;
	Editor edit;
	TabEssentials tess;
	Typeface tfGS,tfNV;
	Calendar c;
	
	TabHost tabHost;
	ScrollView scroll;
	TableLayout tLayout;
	TextView experience,level,BMI_fb,BMI,exsCmplt,progressOf,suggestion,exsFeedback;
	EditText height, weight;
	Button calculateBMI, exerButton, dietButton,qButton,factButton,questButton;
	ProgressBar expBar;
	RatingBar perfBar;
	String resToString = "";
	int stLevel;
	int qAnswered;
	boolean bonus, soundState;
	long lastTime;
	int lastDay;
	MediaPlayer mp;
	
	Button addExper;
	
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        stLevel = MIN_LEVEL;
        qAnswered = QUIZ_ANSWER_LIMIT;//0
        c = Calendar.getInstance();
        bonus = true;
        mp = MediaPlayer.create(getApplicationContext(), R.raw.levelup);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try {
			mp.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    	lastTime = 0;
    	lastDay = 0;
        
        session = new SessionManager(getApplicationContext());
        if(!session.checkLogin()){finish();return;}
        
        userState = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences(userState.getString("username","username"),0);
        edit = sp.edit();
        
		//ini typefaces
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        tfNV = Typeface.createFromAsset(getAssets(),"fonts/Nevis.otf");
  
        
        // textviews, progress bar, edittext ini
        expBar = (ProgressBar) findViewById(R.id.progress);//test
        perfBar = (RatingBar) findViewById(R.id.performanceBar);
        perfBar.setEnabled(false);
        
        exsFeedback = (TextView) findViewById(R.id.exs_feedback);
        level = (TextView) findViewById(R.id.currentLvl);
        experience = (TextView) findViewById(R.id.currentExp);

        exsCmplt = (TextView) findViewById(R.id.completedExs);
        progressOf = (TextView) findViewById(R.id.text_progressOf);
        progressOf.setText("Progress of " + userState.getString("username","username"));
        suggestion = (TextView) findViewById(R.id.text_suggestion);
        suggestion.setTypeface(tfGS);
      
        
        //layout ini
        scroll = (ScrollView) findViewById(R.id.sv_tab2);
        
        exerButton = (Button) findViewById(R.id.exButton);
        dietButton = (Button) findViewById(R.id.dButton);
        qButton = (Button) findViewById(R.id.qBtn);
        questButton = (Button) findViewById(R.id.questBtn);
        factButton = (Button) findViewById(R.id.factsBtn);
        
        addExper = (Button) findViewById(R.id.addExp);
        addExper.setOnClickListener(this);
        
        // listeners ini
        exerButton.setOnClickListener(this);
        dietButton.setOnClickListener(this);
        qButton.setOnClickListener(this); 
		questButton.setOnClickListener(this);
		factButton.setOnClickListener(this);
			 
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
        // forgot pullFrom?
        loadProgressInfo();
        changeFonts();
    }
    
    //modifies fonts of certain elements in all the activity
    public void changeFonts()
    {
        tLayout = (TableLayout) findViewById(R.id.tl_tab2);
  
        int childcountTbl = tLayout.getChildCount();
        
        for (int i=0; i < childcountTbl; i++){
              View v1 = tLayout.getChildAt(i);
              
              if (v1 instanceof TableRow)
              {
            	  TableRow tRow = (TableRow) v1;
            	  int childcountRow = tRow.getChildCount();
            	  
            	  for(int j=0; j< childcountRow;j++)
            	  {
            		  View v2 = tRow.getChildAt(j);
            		  
            		  if (v2 instanceof TextView)
            		  {
                		  TextView t = (TextView) v2;
                		  t.setTypeface(tfGS);  
            		  }
            	  }  
              }
              else
              {
            	  if (v1 instanceof TextView)
            	  {
               		  TextView t = (TextView) v1;
            		  t.setTypeface(tfGS); 
            	  }
              }
        }
    }
    
    public int levelToCap(int level)
    {
    	int cap = 50;
    	
    	for(int i=1;i < level;i++)
    	{
			cap =(int)(cap * LEVEL_SCALE);
			if (cap % 10 > 5)
			{
				cap -= ((cap % 10) % 5);
			}
			else
			{
				if (cap%10 != 5)
				{
					cap -= (cap % 10);
				}
			}
    	}
    	System.out.println("cap for level " + level + " is " + cap);
		return cap;
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
    	soundState = sp.getBoolean("SOUNDSTATE", true);
    	String lvl = sp.getString("LEVEL", String.valueOf(stLevel));
    	String expGained = sp.getString("EXPERIENCE","0");
    	int expCap = sp.getInt("EXPCAP",levelToCap(Integer.parseInt(lvl)));
    	System.out.println("the Cap is " + expCap );
    	String exsCompleted = sp.getString("EXSCMPLT", "0");
    	if (Integer.parseInt(exsCompleted) == EXERCISE_NORM)
    	{
    		bonus = false;
    	}
    	giveFbOnExercises(Integer.parseInt(exsCompleted));
    	//qAnswered = sp.getInt("QANSWERED", 0);
    	if (!(sp.getString("QANSWERED", "0").equals("")))
    	{
    		System.out.println("WHY THE HELL IS IT " + sp.getString("QANSWERED", "0"));
    		qAnswered = Integer.parseInt(sp.getString("QANSWERED", "0"));
    	}
    	else
    	{
    		qAnswered = 0;
    	}
    	//long lastTime = 0;
    	//int lastDay = 0;
    	System.out.println("value of lastcmplt is " + sp.getString("LASTCMPLT","0") );
    	if (!(sp.getString("LASTCMPLT", "0").equals("")) && (!(sp.getString("LASTCMPLT","0").equals("null"))))
    	{  		
        	lastTime = Long.parseLong(sp.getString("LASTCMPLT", "0"));
        	lastDay = (int) (lastTime % 100);
        	System.out.println("TIME WITH DAY AT THE END " + lastTime);
        	lastTime = lastTime / 100;
    	}
    	
    	System.out.println("LAST TIME RECEIVED: " + lastTime + "; LAST DAY: " + lastDay);
    	//int performance = sp.getInt("PERFRATING", 0);
    	
    	boolean quizButton = sp.getBoolean("QUIZBTN",false);
    	System.out.println(expGained + "/" + expCap + "numofex completed = " + exsCompleted);
    	level.setText(lvl);    	
    	//BMI.setText(BMIvalue);
    	stLevel = Integer.parseInt(lvl);
    	levelToCap(stLevel);
    	System.out.println("LEVEL IS " + stLevel);
    	if (stLevel == MAX_LEVEL)
    	{
    		level.setText(String.valueOf(stLevel));
    		expBar.setMax(expCap);
			expBar.setProgress(expBar.getMax());
			experience.setText(expBar.getMax() + "/" + expBar.getMax());
    	}
    	else
    	{
    		expBar.setMax(expCap);
    		expBar.setProgress(Integer.parseInt(expGained));
    		experience.setText(expGained + "/" + expCap);
    	}
    	exsCmplt.setText(exsCompleted);
    	perfBar.setProgress(Integer.parseInt(exsCompleted));
    	qButton.setEnabled(quizButton);
        //long currentTime = c.getTimeInMillis();// might use Date instead
      
        /*if (currentTime - lastTime >= 1000*60 *2 || lastDay != c.get(Calendar.DAY_OF_MONTH))//3600 *24 change
        {
        	System.out.println("Reset performance");
        	exsCmplt.setText("0");
        	exsFeedback.setText("You haven't started exercising yet");
        	perfBar.setProgress(0);
        	saveProgressInfo("EXSCMPLT","0");
        	saveProgressInfo("LASTCMPLT","0");
        	//saveProgressInfo("PERFRATING",0);
        }
    	*/
        resetIfNeeded();
    	if ((stLevel % QUIZ_UNLOCK_PARAM == 0 && qAnswered  < QUIZ_ANSWER_LIMIT) || stLevel == MIN_LEVEL || stLevel == MAX_LEVEL)
    	{
    		qButton.setEnabled(true);
    		saveProgressInfo("QUIZBTN",true);
    		//setupQuizAlert();
    	}
    	else
    	{
    		System.out.println("NO QUIZ FOR YOU" + "; qanswer is " + qAnswered);
    		qButton.setEnabled(false);
    		saveProgressInfo("QUIZBTN",false);
    	}
    }
    
    private void resetIfNeeded()
    {
    	long currentTime = c.getTimeInMillis();
    	 System.out.println("Current time "  + currentTime + "\n  past time: " + lastTime + "\n Difference: " + (currentTime - lastTime) + " compared to " + (MILLIS_DAY));
        if (currentTime - lastTime >= MILLIS_DAY || lastDay != c.get(Calendar.DAY_OF_MONTH))//3600 *24 change
        {
        	System.out.println("Reset performance");
        	exsCmplt.setText("0");
        	//exsFeedback.setText("You haven't started exercising yet");
        	perfBar.setProgress(0);
        	saveProgressInfo("EXSCMPLT","0");
        	saveProgressInfo("LASTCMPLT","0");
        	giveFbOnExercises(0);
        	//saveProgressInfo("PERFRATING",0);
        }
    }

	public void onClick(View v) 
	{
		Intent intent;
		switch (v.getId())
		{
		case R.id.addExp:increaseProgressBy(500);break;
			case R.id.exButton:
				intent = new Intent(this, ExerciseActivity.class);
				intent.putExtra("SOUNDSTATE", soundState);
				intent.putExtra("LEVEL", stLevel);
				intent.putExtra("CURREXP", expBar.getProgress());
				intent.putExtra("EXPCAP", expBar.getMax());
				startActivityForResult(intent, RESCODE_EXERCISES);
				//startActivity(intent);
				break;
			case R.id.dButton:
				intent = new Intent(this, DietActivity.class);
				startActivity(intent);
				break;
			case R.id.qBtn:
				intent = new Intent(this, QuizActivity.class);
				intent.putExtra("SOUNDSTATE", soundState);
				intent.putExtra("LEVEL", stLevel);
				intent.putExtra("CURREXP", expBar.getProgress());
				intent.putExtra("EXPCAP", expBar.getMax());
				intent.putExtra("QANSWERED", qAnswered);
				startActivityForResult(intent,RESCODE_QUIZ);
				break;
			case R.id.questBtn:
				intent = new Intent(this, LifestyleTestActivity.class);
				startActivity(intent);
				break;
			case R.id.factsBtn:
				intent = new Intent(this, FactActivity.class);
				intent.putExtra("LEVEL", stLevel);
				startActivity(intent);
				break;
			}
	}
	
	// need some help here with text.
	private void giveFbOnExercises(int val)
	{
		if (val <= EXERCISE_NORM)
		{
			perfBar.setProgress(val);
		}
		
		switch(val)
		{
			case 0: exsFeedback.setText("You haven't started exercising yet");break;
			case 1: exsFeedback.setText("Do not be lazy, you can do more than that!");break;
			case 2: exsFeedback.setText("Good start, keep exercising!");break;
			case 3: exsFeedback.setText("You can do better than that!");break;
			case 4: exsFeedback.setText("Half of today's exercise norm complete, well done!");break;
			case 5: exsFeedback.setText("Great! Keep exercising!");break;
			case 6: exsFeedback.setText("You did a very good job today! Complete two more exercises to finish the exercise norm and get bonus exp!");break;
			case 7: exsFeedback.setText("One more exercise to finish the exercise norm and get bonus exp!");break;
			case 8: exsFeedback.setText("Excellent workout today!");		
					if (bonus)
					 {	
						double bonusExp = 5;
						for(int i = 0; i < stLevel;i++)
						{
							bonusExp = bonusExp * LEVEL_SCALE;
						}
						System.out.println("Showed toast");
						Toast.makeText(this, "+" + (int)bonusExp + " experience points for completing today's exercises", Toast.LENGTH_SHORT).show();
						increaseProgressBy((int)bonusExp);
					 }
					break;
			default: exsFeedback.setText("Bravo! You've done more exercises than necessary, which is very good!");break;
		}
		/*
		switch (val)
		{
			case 0: exsFeedback.setText("You haven't started exercising yet");break;
			case 1: exsFeedback.setText("Do not be lazy!");break;
			case 2: exsFeedback.setText("Good start, keep exercising!");break;
			case 3: exsFeedback.setText("You can do better than that!");break;
			case 4: exsFeedback.setText("Good work! Keep exercising!");break;
			case 5: exsFeedback.setText("Good work! Keep exercising!");break;
			case 6: exsFeedback.setText("You did a very good job!");break;
			case 7: exsFeedback.setText("You did a very good job!");break;
			case 8: exsFeedback.setText("Great!");break;
			case 9: exsFeedback.setText("Very good!");break;
			case 10: exsFeedback.setText("Very good effort!");break;
			case 11: exsFeedback.setText("Great great great!!");break;
			case 12: exsFeedback.setText("Three more exercises to finish the exercise norm and get bonus exp!");break;
			case 13: exsFeedback.setText("Two more exercises to finish the exercise norm and get bonus exp!");break;
			case 14: exsFeedback.setText("One more exercise to finish the exercise norm and get bonus exp!");break;
			case 15: exsFeedback.setText("Excellent workout today!");
					 if (bonus)
					 {	
						double bonusExp = 5;
						for(int i = 0; i < stLevel;i++)
						{
							bonusExp = bonusExp * LEVEL_SCALE;
						}
						 Toast.makeText(this, "+" + (int)bonusExp + " experience points for completing today's exercises", Toast.LENGTH_SHORT).show();
						 increaseProgressBy((int)bonusExp);break;
					 }
			default: exsFeedback.setText("Bravo!");break;
		}*/
	}
	
	private void increaseProgressBy(int val)
	{
		int lastLevel = stLevel;
		int nextMax;
		
		if (!((stLevel == MAX_LEVEL -1) && (expBar.getProgress() + val) >= (expBar.getMax())))
		{
			while((expBar.getProgress() + val) >= (expBar.getMax()))
			{
				try {
					if (soundState)mp.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stLevel++;
				//saveProgressInfo("QANSWERED", 0);
				saveProgressInfo("QANSWERED","0");
				qAnswered = 0;//TBR??
				level.setText(String.valueOf(stLevel));
				val = val - expBar.getMax();

				expBar.setMax(levelToCap(stLevel));

				experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
				Toast.makeText(this, "Congratulations!\nYou've reached level " + level.getText().toString() ,Toast.LENGTH_SHORT).show();
				
				if (stLevel != lastLevel && stLevel % FACT_PARAM == 0)
				{
					//setupFactsAlert();
					setupAlert("New facts were unlocked!","Would you like to see them now?",
							   "Yes","Later",POSCODE_FACTS,NEGCODE_FACTS);
				}
				if ((stLevel % QUIZ_UNLOCK_PARAM == 0  && qAnswered < QUIZ_ANSWER_LIMIT) || stLevel == MIN_LEVEL)
				{
					qButton.setEnabled(true);
					saveProgressInfo("QANSWERED", String.valueOf(qAnswered));
					saveProgressInfo("QUIZBTN", true);
					if (stLevel != lastLevel)
					{
						//setupQuizAlert();
						setupAlert("The Quiz was unlocked!",
								   "Would you like to take it now\n" +
								   "to get extra experience points?",
								   "Yes","Later",POSCODE_QUIZ,NEGCODE_QUIZ);
					}
				}
				else
				{
					qButton.setEnabled(false);
					saveProgressInfo("QUIZBTN", false);
				}
			}
			
			if(!((expBar.getProgress() + val) >= (expBar.getMax())))
			{
				expBar.setProgress(expBar.getProgress() + val);
				experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
			}
		}
		else
		{
			try {
				if (soundState)mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stLevel++;
			Toast.makeText(this, "Congratulations!\nYou've reached level " + stLevel,Toast.LENGTH_SHORT).show();
			expBar.setProgress(expBar.getMax());
			experience.setText(expBar.getMax() + "/" + expBar.getMax());
			level.setText(String.valueOf(MAX_LEVEL));
			qButton.setEnabled(true);
			saveProgressInfo("QANSWERED", String.valueOf(qAnswered));
			saveProgressInfo("QUIZBTN", true);
			setupAlert("Random mode for Quiz was unlocked!",
					   "Would you like to try it now?",
					   "Yes","Later",POSCODE_QUIZ,NEGCODE_QUIZ);
			setupAlert("New facts were unlocked!","Would you like to see them now?",
					   "Yes","Later",POSCODE_FACTS,NEGCODE_FACTS);
		}
		
		saveProgressInfo("LEVEL",String.valueOf(level.getText().toString()));
		saveProgressInfo("EXPERIENCE",String.valueOf(expBar.getProgress()));
		saveProgressInfo("EXPCAP",expBar.getMax());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		System.out.println("Attempted to catch data...");
		//tabHost.setCurrentTab(1);
		if (requestCode == RESCODE_EXERCISES)
		{
			if (resultCode == RESULT_OK )
			{
				if ((data.getIntExtra("expPoints",0) != 0) && (data.getIntExtra("exercises_cmplt",0) != 0))
				{
					int got_exp = data.getIntExtra("expPoints",0);
					int got_exs = data.getIntExtra("exercises_cmplt",0);
					long tStampLastExDone = data.getLongExtra("LAST_EX_DONE_AT", 0);
					
					if (stLevel < MAX_LEVEL)
					{
						increaseProgressBy(got_exp);
					}
					
					exsCmplt.setText(String.valueOf(Integer.parseInt(exsCmplt.getText().toString()) + got_exs));
					int exsCompleted = Integer.parseInt(exsCmplt.getText().toString());
					//giveFbOnExercises(exsCompleted);
					if (exsCompleted == EXERCISE_NORM)
					{
						bonus = true;
					}
					perfBar.setProgress(exsCompleted);
					//tabHost.setCurrentTab(1);
					
					
					saveProgressInfo("EXSCMPLT",exsCmplt.getText().toString());
					
					
					saveProgressInfo("LASTCMPLT", String.valueOf(tStampLastExDone));
					
					
					
					//saveProgressInfo("DAYCMPLT",c.get(Calendar.DAY_OF_MONTH));
					//saveProgressInfo("PERFRATING", perfBar.getProgress());
				}	
			}
		}
		
		if (requestCode == RESCODE_SETTINGS)
		{
			System.out.println("result code : " + resultCode);
			if (resultCode == RESULT_OK)
			{
				System.out.println("BEFORE clear " + stLevel);
				edit.clear();
				edit.commit();
				stLevel = MIN_LEVEL;
				System.out.println("AFTER clear " + stLevel);
				loadProgressInfo();
			}
			if (resultCode == 99)
			{
				soundState = data.getBooleanExtra("SOUNDSTATE", true);
				System.out.println("received sound from sets: " + soundState);
				saveProgressInfo("SOUNDSTATE",soundState);
			}
		}
		
		if (requestCode == RESCODE_QUIZ)
		{
			if (resultCode == RESULT_OK )
			{
				if (data.getIntExtra("expPoints",0) != 0)
				{
					int got_exp = data.getIntExtra("expPoints",0);
					
					System.out.println("Got " + got_exp + " exp points");
					//qAnswered = ;
					if (stLevel < MAX_LEVEL)
					{
						increaseProgressBy(got_exp);
					}
				}
				if ((data.getIntExtra("QANSWERED", 0) != 0))
				{
					qAnswered = data.getIntExtra("QANSWERED",0);
					//qAnswered = Integer.parseInt(data.getStringExtra("QANSWERED"));
					saveProgressInfo("QANSWERED", String.valueOf(qAnswered));
					System.out.println(sp.getString("QANSWERED", "0"));
					System.out.println("Data got: " + qAnswered);
					if (qAnswered == QUIZ_ANSWER_LIMIT && stLevel < MAX_LEVEL)
					{
						tabHost.setCurrentTab(1);
						qButton.setEnabled(false);
						saveProgressInfo("QUIZBTN",false);
					}
				}
				
			}
		}
		giveFbOnExercises(Integer.parseInt(exsCmplt.getText().toString()));
		new pushToServer().execute();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    private void alignTextInTabs()
    {
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) 
        {
            final View v= tabHost.getTabWidget().getChildTabViewAt(i);
            if ( v != null ) 
            {
            	final View tv = v.findViewById(android.R.id.title);
                if ( tv instanceof TextView ) 
                {
                    ((TextView) tv).setGravity(Gravity.CENTER);
                    ((TextView) tv).setSingleLine(false);
                    tv.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                }
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.activity_tabbed, menu);
		  return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId())
    	{
    		case R.id.menu_sync: Toast.makeText(this, "Syncing..", Toast.LENGTH_SHORT).show();
    					new pushToServer().execute(); // should work ?
    				break;
    		case R.id.menu_settings: intent = new Intent(this, SettingsActivity.class);
    					intent.putExtra("SOUNDSTATE", soundState);
    					System.out.println("put before going to settings: " + soundState);
    					startActivityForResult(intent,RESCODE_SETTINGS);
					break;
    		case R.id.menu_about: intent = new Intent(this, AboutActivity.class);
						startActivity(intent);
    				break;
    	}
    	return true;
    }
    
	private void setupAlert(String title, String message, String pos, String neg, final int posCode, final int negCode)
	{
		AlertDialog.Builder alert;
	    alert = new AlertDialog.Builder(this);
		alert.setTitle(title)
		.setMessage(message)
		.setCancelable(false).setPositiveButton(pos,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				if (posCode == POSCODE_EXIT)
				{
					edit.commit();
					new pushToServer().execute();
					//Toast.makeText(TabbedActivity.this, "Saved everything", Toast.LENGTH_SHORT).show();
					finish();
				}
				else
				{
					if (posCode == POSCODE_FACTS)
					{
						intent = new Intent(TabbedActivity.this, FactActivity.class);
						intent.putExtra("LEVEL", stLevel);
						intent.putExtra("QUICKSHOW", true);
						startActivity(intent);
					}
					else
					{
						intent = new Intent(TabbedActivity.this, QuizActivity.class);
						intent.putExtra("SOUNDSTATE",soundState);
						intent.putExtra("LEVEL", stLevel);
						intent.putExtra("CURREXP", expBar.getProgress());
						intent.putExtra("EXPCAP", expBar.getMax());
						intent.putExtra("QANSWERED", qAnswered);
						startActivityForResult(intent,RESCODE_QUIZ);
					}
				}
				
			}
		  })
		.setNegativeButton(neg,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				
				if (negCode == NEGCODE_EXIT || negCode == NEGCODE_FACTS)
				{
					if (negCode == NEGCODE_QUIZ && stLevel < MAX_LEVEL)
					{
						Toast.makeText(TabbedActivity.this, "Make sure to complete the quiz\nbefore you reach next level !", Toast.LENGTH_SHORT).show();
					}
					dialog.cancel();
				}
			}
		});
		
		alert.show();
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
		private static final String KEY_LEVEL = "level";
		private static final String KEY_SUCCESS = "success";
		
		private static final String KEY_LASTTIMECOMPLETED = "lastExercise"; // time when the last exercise was completed
		private static final String KEY_QUESTIONSANSWERED = "questionsA";
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_push_user = "http://www.labs.callanwhite.co.uk/minitrainer/push_profile.php";
		
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected String doInBackground(String... params) {
			
			userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			sp = getApplicationContext().getSharedPreferences(userState.getString("username","username"),0);
			String username = userState.getString("username","username");
			String exercisesTotal = sp.getString("EXSCMPLT","0");
			//String lastCompleted = sp.getLong("LASTCMPLT", 0);// need to add this column to database 
			String level = sp.getString("LEVEL","1");
			String experienceTotal = sp.getString("EXPERIENCE","0");
			
			//String lastExercise = String.valueOf(sp.getLong("LASTCMPLT", 0));
			String questionsA = sp.getString("QANSWERED", "0");
			String lastExercise = sp.getString("LASTCMPLT", "0");
			
			System.out.println("ATTEMPTED TO PUSH " + questionsA + " q answered" + " + date : " + lastExercise);
			
			List<NameValuePair> dataToPush = new ArrayList<NameValuePair>();
			dataToPush.add(new BasicNameValuePair(KEY_ID, "1"));
			dataToPush.add(new BasicNameValuePair(KEY_USERNAME,username));
			dataToPush.add(new BasicNameValuePair(KEY_EXERCISESTOTAL,exercisesTotal));
			dataToPush.add(new BasicNameValuePair(KEY_EXPERIENCETOTAL,experienceTotal));
			// need to add last day an exercise was completed
			dataToPush.add(new BasicNameValuePair(KEY_LEVEL,level));
			dataToPush.add(new BasicNameValuePair(KEY_QUESTIONSANSWERED, questionsA));
			//dataToPush.add(new BasicNameValuePair(KEY_LASTTIMECOMPLETED,));
			dataToPush.add(new BasicNameValuePair(KEY_LASTTIMECOMPLETED, lastExercise));
			
			//dataToPush.add(new BasicNameValuePair(KEY_LASTTIMECOMPLETED,lastCompleted));// // need to push this as well
			
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
			//pDialog.dismiss();
			if(complete)
			{
				Toast.makeText(TabbedActivity.this, "Progress updated successfully",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(TabbedActivity.this, "Progress update failed...",Toast.LENGTH_SHORT).show();
			}
			//session.logoutUser();
		}
		
	}

	@Override
	public void onBackPressed() 
	{
		setupAlert("Confirm exit","Do you really want to exit?",
				   "Exit","No",POSCODE_EXIT,NEGCODE_EXIT);
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
    
    private void saveProgressInfo(String key, long val)
    {
    	edit.putLong(key, val);
    	edit.commit();
    }
    
    private void saveProgressInfo(String key, boolean val)
    {
    	edit.putBoolean(key, val);
    	edit.commit();
    }
	
}
