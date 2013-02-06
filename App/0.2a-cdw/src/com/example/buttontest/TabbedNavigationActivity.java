package com.example.buttontest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabbedNavigationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabbed_navigation);
		
		
		//Initialise the tabhost, this is used for containing the seperate tabs and views associated 
		//with them
		//Each tabspec associates with an id in the tabbednavigation xml file which contains a number of
		//LinearLayouts to handle each individual layout.
		TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("Exercises");
		spec1.setContent(R.id.tabExercises);
		spec1.setIndicator("Exercises!");
		
		TabSpec spec2 = tabHost.newTabSpec("Activities");
		spec2.setContent(R.id.tabActivities);
		spec2.setIndicator("Activities!");
		
		TabSpec spec3 = tabHost.newTabSpec("Profile");
		spec3.setContent(R.id.tabProfile);
		spec3.setIndicator("Profile!");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		
		//Set default values for the BMI, this would read from a local SQLite db eventually
		EditText defaultHeight = (EditText) findViewById(R.id.profileInputHeight);
		defaultHeight.setText("172");
		EditText defaultWeight = (EditText) findViewById(R.id.profileInputWeight);
		defaultWeight.setText("80");
		
	}
	
	public void calculateBMI(View v)
	{
		//Basic function to calculate a persons BMI based on their inputs
		//Note there is no exception handling at the moment, this needs to be 
		//implemented (Integer.parseInt doesn't play well with null values)
		
		TextView out = (TextView) findViewById(R.id.profileDisplayBMI);
		
		EditText heightText = (EditText) findViewById(R.id.profileInputHeight);
		double height = (Double.parseDouble(heightText.getText().toString()))/100;
		
		EditText weightText = (EditText) findViewById(R.id.profileInputWeight);
		int weight = Integer.parseInt(weightText.getText().toString());
		
		//Convert the double result of bmi to an integer (we don't really need all those trailing
		//decimals atm, a simple number will suffice for now (change to double with 2dp later on).
		int bmi =  (int) (weight/(Math.pow(height, 2)));
		
		
		String bmiClass = "";
		if(bmi<18.5)
		{
			bmiClass = "Underweight";
		}
		else if(bmi<25)
		{
			bmiClass = "Normal";
		}
		else if(bmi<=30)
		{
			bmiClass = "Overweight";
		}
		else 
		{
			bmiClass = "Obese";
		}
		
		out.setText("You have a BMI of : "+bmi+"\nClassification: "+bmiClass);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tabbed_navigation, menu);
		return true;
	}

}
