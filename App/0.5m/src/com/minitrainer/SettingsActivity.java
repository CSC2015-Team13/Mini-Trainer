package com.minitrainer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

	Button res;
	SharedPreferences sp;
	Editor edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		// Callan please edit this to reset everything 
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		edit = sp.edit();
		res = (Button) findViewById(R.id.reset_all_progress);
		res.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.reset_all_progress:
				edit.clear();
				edit.commit();
				Toast.makeText(this, "Reset completed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
