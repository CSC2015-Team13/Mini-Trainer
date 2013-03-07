package com.minitrainer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabbedActivity extends Activity implements OnClickListener {
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	Intent intent;
	SharedPreferences sp;
	Editor edit;
	
	TabHost tabHost;
	ScrollView scroll;
	TextView fback, experience,level;
	EditText height, weight;
	Button giveFeedback, exerButton, dietButton, plusExp;
	ProgressBar expBar;
	String[] vals = {"Quiz","Test","Questionnaire","Games","Facts","Jokes"};
	String resToString = "";
	double res = 0;
	double w;
	int h = 0;
	int stLevel = 1;
	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
        
        // textviews, progress bar, edittext ini
        expBar = (ProgressBar) findViewById(R.id.progress);//test
        level = (TextView) findViewById(R.id.currentLvl);
        experience = (TextView) findViewById(R.id.currentExp);
        loadProgressInfo();
        
        height = (EditText) findViewById(R.id.editHeight);
        weight = (EditText) findViewById(R.id.editWeight);
        fback = (TextView) findViewById(R.id.feedback);
        
        //layout ini
        scroll = (ScrollView) findViewById(R.id.scrollView1);
        
        //buttons ini
        giveFeedback = (Button) findViewById(R.id.button_giveFB);
        exerButton = (Button) findViewById(R.id.exButton);
        dietButton = (Button) findViewById(R.id.dButton);
        plusExp = (Button) findViewById(R.id.increase_test);//test
        
        //listview ini
        ListView lv = (ListView) findViewById(R.id.lvMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vals);
        lv.setAdapter(adapter);
        
        // listeners ini
        giveFeedback.setOnClickListener(this);
        exerButton.setOnClickListener(this);
        dietButton.setOnClickListener(this);
        plusExp.setOnClickListener(this);//test
        
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
    	System.out.println(expGained + "/" + expCap);
    	level.setText(lvl);    	
    	stLevel = Integer.parseInt(lvl);
    	expBar.setMax(Integer.parseInt(expCap));
    	expBar.setProgress(Integer.parseInt(expGained));
    	experience.setText(expGained + "/" + expCap);
    	//experience.setText(expBar.getProgress() + "/" + expBar.getMax());
    	//experience.setText(String.valueOf(expBar.getProgress()) + " / " + String.valueOf(expBar.getMax()));
    	
    }
   
    private void saveProgressInfo(String key, String val)
    {
    	edit.putString(key, val);
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
			case R.id.button_giveFB:
				String checkHeight = height.getText().toString();
				String checkWeight = weight.getText().toString();
				if ((checkWeight.trim().length()>0) && (checkHeight.trim().length()>0) && Integer.parseInt(checkHeight) != 0)
				{
					h = Integer.parseInt(height.getText().toString());
					w = Double.parseDouble(weight.getText().toString());
					res = w / (h * 0.01 * h *0.01);
					resToString = String.format("%.2f",res);    	
					if (res <= UWL)
					{
						fback.setText("You are underweight. Your BMI is " + String.format("%.2f",res));
					}
					if (res >UWL && res < NWL)
					{
						fback.setText("Your weight is normal. Your BMI is " + String.format("%.2f",res));
					}
					if (res > NWL && res <= OWL)
					{
						fback.setText("You are overweight. Your BMI is " + String.format("%.2f",res));
					}
					if (res > OWL)
					{
						fback.setText("You need to lose some weightaaaaaaaaaaaaaaaaaaaaaaaaaggg.\n Your BMI is gaaaaaaaaaaaaaaaaaaaaaww" + String.format("%.2f",res));
					}
					scroll.fullScroll(View.FOCUS_DOWN);
				}
				//reset progress test
				edit.clear();
				edit.commit();
				
				break;
					
			case R.id.increase_test:
				increaseProgressBy(7);
				break;
		}
	}
	
	private void increaseProgressBy(int val)
	{
		if (expBar.getProgress() + val >= expBar.getMax())
		{
			stLevel++;
			level.setText(String.valueOf(stLevel));
			
			if ((7 - (expBar.getMax() - expBar.getProgress())) > 0)
			{
				expBar.setProgress( val - (expBar.getMax() - expBar.getProgress()));
			}
			else
			{
				expBar.setProgress(0);
			}
			expBar.setMax(expBar.getMax() * 2);
			experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
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
				if (data.getStringExtra("expPoints") != null)
				{
					int got_exp = Integer.parseInt(data.getStringExtra("expPoints"));
					increaseProgressBy(got_exp);
					weight.setText(String.valueOf(got_exp));
				}	
				//int tabNumber = Integer.parseInt(data.getStringExtra("tabNo"));
				//tabHost.setCurrentTab(tabNumber);
			}
		}
	}
	
    public void alignTextInTabs()
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
	
}
