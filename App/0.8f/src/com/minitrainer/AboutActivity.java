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

	LinearLayout ll1,ll2,ll3;
	Button ref,disc;
	Typeface tfGS;
	int currStep,childcount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        ref = (Button) findViewById(R.id.btn_resources_used);
        disc = (Button) findViewById(R.id.btn_disc);
        ref.setOnClickListener(this);
        disc.setOnClickListener(this);
        ll1 = (LinearLayout) findViewById(R.id.aboutSection);
        ll2 = (LinearLayout) findViewById(R.id.references);
        ll3 = (LinearLayout) findViewById(R.id.disclaimer);
        
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        childcount = 0;
        currStep = 1;
        
        childcount = ll1.getChildCount();
        for (int i=0; i < childcount; i++){
              View v = ll1.getChildAt(i);
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
		if (v.getId() == R.id.btn_resources_used)
		{
	        childcount = ll2.getChildCount();
	        for (int i=0; i < childcount; i++){
	            View view = ll2.getChildAt(i);
	            if (view instanceof TextView)
	            {
	          	  TextView t = (TextView) view;
	          	  t.setTypeface(tfGS);   
	            }
	        }
			ll1.setVisibility(View.GONE);
			ll2.setVisibility(View.VISIBLE);
			currStep++;
		}
		if(v.getId() == R.id.btn_disc)
		{
			
	        childcount = ll3.getChildCount();
	        for (int i=0; i < childcount; i++){
	            View view = ll3.getChildAt(i);
	            if (view instanceof TextView)
	            {
	          	  TextView t = (TextView) view;
	          	  t.setTypeface(tfGS);   
	            }
	        }
			ll1.setVisibility(View.GONE);
			ll2.setVisibility(View.GONE);
			ll3.setVisibility(View.VISIBLE);
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
			ll1.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.GONE);
			ll3.setVisibility(View.GONE);
		}
	}
}
