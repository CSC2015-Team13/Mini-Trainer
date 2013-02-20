package com.minitrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.minitrainer.R;

public class MainActivity extends Activity implements OnClickListener {

	
	TextView out;
	Button button_exAndDiets,button_profile,button_activities,button_prefs;
	Intent intent;
	
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
