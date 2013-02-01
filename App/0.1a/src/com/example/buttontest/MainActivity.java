package com.example.buttontest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	
	TextView out;
	Button button_exAndDiets,button_profile,button_activities,button_prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
			 out.setText("Exercises and diets was pressed");
			 break;
		case R.id.button_prof:
			 out.setText("Profile was pressed");
			 break;
		case R.id.button_activities:
			 out.setText("Activities was pressed");
			 break;
		case R.id.button_prefs:
			 out.setText("Preferences was pressed");
			 break;
		}
	}
			 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
