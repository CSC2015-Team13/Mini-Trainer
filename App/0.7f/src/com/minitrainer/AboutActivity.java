package com.minitrainer;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {

	Typeface tfGS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        LinearLayout ll = (LinearLayout) findViewById(R.id.settings);
        int childcount = ll.getChildCount();
        for (int i=0; i < childcount; i++){
              View v = ll.getChildAt(i);
              if (v instanceof TextView)
              {
            	  TextView t = (TextView) v;
            	  t.setTypeface(tfGS);   
              }
        }
	}
}
