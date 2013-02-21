package com.example.minitrainer.team;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	Intent intent;
	
	TabHost tabHost;
	ScrollView scroll;
	TextView fback, experience,level;
	EditText height, weight;
	Button giveFeedback, exerButton, plusExp;
	ProgressBar expBar;
	String[] vals = {"Quiz","Test","Questionnaire","Games","Facts","Jokes"};
	String resToString;
	double res = 0;
	double w;
	int h = 0;
	int stLevel = 1;
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        
        expBar = (ProgressBar) findViewById(R.id.progress);//test
        level = (TextView) findViewById(R.id.currentLvl);
        intent = getIntent();
        scroll = (ScrollView) findViewById(R.id.scrollView1);
        
        int tabNo = Integer.parseInt(intent.getStringExtra("tabNo"));
        
        height = (EditText) findViewById(R.id.editHeight);
        weight = (EditText) findViewById(R.id.editWeight);
        experience = (TextView) findViewById(R.id.currentExp);
        fback = (TextView) findViewById(R.id.feedback);
        giveFeedback = (Button) findViewById(R.id.button_giveFB);
       
        
        exerButton = (Button) findViewById(R.id.exButton);
        
        plusExp = (Button) findViewById(R.id.increase_test);//test
        
        ListView lv = (ListView) findViewById(R.id.lvMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vals);
        lv.setAdapter(adapter);
        
        giveFeedback.setOnClickListener(this);
        exerButton.setOnClickListener(this);
        plusExp.setOnClickListener(this);//test
        
        
        tabHost= (TabHost)findViewById(R.id.tabHost);
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
        
        tabHost.setCurrentTab(tabNo);
        
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
    
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.exButton:
			Intent intent = new Intent(this, ExerciseActivity.class);
			startActivityForResult(intent, 5);
			//startActivity(intent);
			 break;
		case R.id.button_giveFB:
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
	    		fback.setText("You need to lose some weight bruh/sis. Your BMI is " + String.format("%.2f",res));
	    	}
	    	scroll.fullScroll(View.FOCUS_DOWN);
	    	
			 break;
		case R.id.increase_test:
			
			if (expBar.getProgress() + 7 >= expBar.getMax())
			{
				stLevel++;
				level.setText(String.valueOf(stLevel));
				
				if ((7 - (expBar.getMax() - expBar.getProgress())) > 0)
				{
					expBar.setProgress( 7 - (expBar.getMax() - expBar.getProgress()));
					
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
				expBar.setProgress(expBar.getProgress() + 7);
				experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
			}
			break;
			
		}
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
					
					expBar.setProgress(expBar.getProgress() + got_exp);	
					experience.setText(String.valueOf(expBar.getProgress()) + "/" + expBar.getMax());
					
					weight.setText(String.valueOf(got_exp));
				}	
				//int tabNumber = Integer.parseInt(data.getStringExtra("tabNo"));
				//tabHost.setCurrentTab(tabNumber);
			}
		}
	}
	
}
