package com.minitrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class TabbedActivity extends Activity implements OnClickListener {
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	Intent intent;
	SessionManager session;
	SharedPreferences userState;
	SharedPreferences sp;
	Editor edit;
	
	AlertDialog.Builder alert;
	TabHost tabHost;
	ScrollView scroll;
	TextView experience,level,BMI_fb,BMI,exsCmplt;
	EditText height, weight;
	Button calculateBMI, exerButton, dietButton,qButton;
	ProgressBar expBar;
	String[] vals = {"Quiz","Test","Questionnaire","Games","Facts","Jokes"};
	String resToString = "";
	int stLevel = 1;
	
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        
        session = new SessionManager(getApplicationContext());
        if(!session.checkLogin()){finish();return;}
        
        userState = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences(userState.getString("username","username"),0);
        edit = sp.edit();
        alert = new AlertDialog.Builder(this);
        
        // textviews, progress bar, edittext ini
        expBar = (ProgressBar) findViewById(R.id.progress);//test
        level = (TextView) findViewById(R.id.currentLvl);
        experience = (TextView) findViewById(R.id.currentExp);
        BMI_fb = (TextView) findViewById(R.id.BMI_feedback);
        BMI = (TextView) findViewById(R.id.BMI_value);
        exsCmplt = (TextView) findViewById(R.id.completedExs);
        loadProgressInfo();
        
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
        //plusExp = (Button) findViewById(R.id.increase_test);//test--
        
        //listview ini
        //ListView lv = (ListView) findViewById(R.id.lvMain);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vals);
        //lv.setAdapter(adapter);
        
        // listeners ini
        calculateBMI.setOnClickListener(this);
        exerButton.setOnClickListener(this);
        dietButton.setOnClickListener(this);
        qButton.setOnClickListener(this); // test
        //plusExp.setOnClickListener(this);//test
		BMI.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
 
				// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(TabbedActivity.this);
				View promptsView = li.inflate(R.layout.bmi_prompt, null);
 
				// set prompts.xml to alertdialog builder
				alert.setView(promptsView);
 
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editHeight1);
				
				final EditText userInput2 = (EditText) promptsView
						.findViewById(R.id.editWeight1);
 
				// set dialog message
				alert
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
 
				// create alert dialog
				//AlertDialog alert = alertDialogBuilder.create();
 
				// show it
				alert.show();
 
			}
		});
        
        // tab ini
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Exercises and Diets");
        TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Profile");
        spec2.setContent(R.id.tab2);
        TabSpec spec3 = tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Activities");
        spec3.setContent(R.id.tab3);
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        intent = getIntent();
        int tabNo = Integer.parseInt(intent.getStringExtra("tabNo"));
        tabHost.setCurrentTab(tabNo);
        
        alignTextInTabs();
    }
    
    private void loadProgressInfo()
    {
    	String lvl = sp.getString("LEVEL", String.valueOf(stLevel));
    	String expGained = sp.getString("EXPERIENCE","0");
    	String expCap = sp.getString("EXPCAP","50");
    	String exsCompleted = sp.getString("EXSCMPLT", "0");
    	System.out.println(expGained + "/" + expCap + "numofex completed = " + exsCompleted);
    	level.setText(lvl);    	
    	stLevel = Integer.parseInt(lvl);
    	expBar.setMax(Integer.parseInt(expCap));
    	expBar.setProgress(Integer.parseInt(expGained));
    	experience.setText(expGained + "/" + expCap);
    	exsCmplt.setText(exsCompleted);
    	
    }
   
    private void saveProgressInfo(String key, String val)
    {
    	edit.putString(key, val);
    	edit.putLong("lastUpdate", System.currentTimeMillis()/1000L);
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
				startActivity(intent);
				break;
			/*	
			case R.id.increase_test:
				int x = 7;
				increaseProgressBy(x);
				break;
			*/
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
		edit.clear();
		edit.commit();
	}
	
	private void increaseProgressBy(int val)
	{
		if (expBar.getProgress() + val >= expBar.getMax())
		{
			stLevel++;
			level.setText(String.valueOf(stLevel));
			
			if ((val - (expBar.getMax() - expBar.getProgress())) > 0)
			{
				expBar.setProgress( val - (expBar.getMax() - expBar.getProgress()));
			}
			else
			{
				expBar.setProgress(0);
			}
			expBar.setMax((int)(expBar.getMax() * 1.3));
			experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
			Toast.makeText(this, "Congratulations!\nYou've reached level " + level.getText().toString() ,Toast.LENGTH_SHORT).show();
		}
		else
		{
			expBar.setProgress(expBar.getProgress() + val);
			experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
		}
		saveProgressInfo("LEVEL",level.getText().toString());
		saveProgressInfo("EXPERIENCE",String.valueOf(expBar.getProgress()));
		saveProgressInfo("EXPCAP",String.valueOf(expBar.getMax()));
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
	
}
