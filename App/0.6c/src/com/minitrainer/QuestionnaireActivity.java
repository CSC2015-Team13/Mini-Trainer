package com.minitrainer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuestionnaireActivity extends Activity implements OnClickListener {

	EditText e1,e2,e3,e4,e5,e6;
	CheckBox chb;
	Button back,next;
	LinearLayout st1,st2,layer;
	int currentStep = 0;
	Typeface tfGS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire);
		currentStep = 0;
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		st1 = (LinearLayout) findViewById(R.id.qStep1);
		st2 = (LinearLayout) findViewById(R.id.qStep2);
		layer = (LinearLayout) findViewById(R.id.topLayer);
		layer.setBackgroundColor(Color.parseColor("#dadada"));
		st1.setVisibility(View.VISIBLE);
		st2.setVisibility(View.GONE);
		e1 = (EditText) findViewById(R.id.editText1);
		e2 = (EditText) findViewById(R.id.editText2);
		e3 = (EditText) findViewById(R.id.editText3);
		e4 = (EditText) findViewById(R.id.editText4);
		e5 = (EditText) findViewById(R.id.editText5);
		e6 = (EditText) findViewById(R.id.editText6);
		chb = (CheckBox) findViewById(R.id.checkBox1);
		chb.setTypeface(tfGS);
		next = (Button) findViewById(R.id.button1);
		back = (Button) findViewById(R.id.button2);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
		changeFonts(layer);
	}
	
	public void changeFonts(LinearLayout ll)
	{
		int count = ll.getChildCount();
		
		for(int i=0;i< count;i++ )
		{
			LinearLayout llChild = (LinearLayout) ll.getChildAt(i);
			int childcount = llChild.getChildCount();
			for (int j=0; j < childcount; j++)
			{
				View v1 = llChild.getChildAt(j);
				if (v1 instanceof TextView)
				{
					TextView t = (TextView) v1;
					t.setTypeface(tfGS); 
				}
				else
				{
					if (v1 instanceof EditText)
					{
						EditText e = (EditText) v1;
						e.setTypeface(tfGS);
					}
					else
					{
						if (v1 instanceof Button)
						{
							Button b = (Button) v1;
							b.setTypeface(tfGS);
						}
					}
				}
			}	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questionnaire, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.button1:
			st1.setVisibility(View.GONE);
			st2.setVisibility(View.VISIBLE);
			currentStep = 1;
			break;
		case R.id.button2:
			st2.setVisibility(View.GONE);
			st1.setVisibility(View.VISIBLE);
			currentStep = 0;
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() 
	{
		if (currentStep == 1)
		{
			st2.setVisibility(View.GONE);
			st1.setVisibility(View.VISIBLE);
			currentStep = 0;
		}
		else
		{
			super.onBackPressed();
		}
	}

}
