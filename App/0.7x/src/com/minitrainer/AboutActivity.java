package com.minitrainer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {

	LinearLayout ll,ll2;
	Button b;
	Typeface tfGS;
	int currStep;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        b = (Button) findViewById(R.id.resources_used);
        b.setOnClickListener(this);
        ll = (LinearLayout) findViewById(R.id.aboutSection);
        currStep = 1;
        ll2 = (LinearLayout) findViewById(R.id.references);
        ll2.setVisibility(View.GONE);
        int childcount = ll.getChildCount();
        for (int i=0; i < childcount; i++){
              View v = ll.getChildAt(i);
              if (v instanceof TextView)
              {
            	  TextView t = (TextView) v;
            	  t.setTypeface(tfGS);   
              }
        }
        childcount = ll2.getChildCount();
        for (int i=0; i < childcount; i++){
            View v = ll2.getChildAt(i);
            if (v instanceof TextView)
            {
          	  TextView t = (TextView) v;
          	  t.setTypeface(tfGS);   
            }
      }
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.resources_used)
		{
			ll.setVisibility(View.GONE);
			ll2.setVisibility(View.VISIBLE);
			currStep++;
		}
		
	}
	
	@Override
	public void onBackPressed() 
	{
		if (currStep == 1)
		{
			super.onBackPressed();
		}
		else
		{
			currStep--;
			ll2.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		}
	}
}
