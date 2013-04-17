package com.minitrainer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionnaireActivity extends Activity implements OnClickListener {

	EditText age,height,weight,mins,sets,hrs;
	CheckBox chb;
	Button back,next,results;
	LinearLayout st1,st2,st3,layer;
	ScrollView  sv;
	int currentStep = 0;
	Typeface tfGS;
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire);
		currentStep = 0;
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		st1 = (LinearLayout) findViewById(R.id.qStep1);
		st2 = (LinearLayout) findViewById(R.id.qStep2);
		st3 = (LinearLayout) findViewById(R.id.qStep3);
		layer = (LinearLayout) findViewById(R.id.topLayer);
		sv = (ScrollView) findViewById(R.id.svQuest);
		sv.setBackgroundColor(Color.parseColor("#dadada"));
		st1.setVisibility(View.VISIBLE);
		st2.setVisibility(View.GONE);
		st3.setVisibility(View.GONE);
		age = (EditText) findViewById(R.id.editText1);
		height = (EditText) findViewById(R.id.editText2);
		weight = (EditText) findViewById(R.id.editText3);
		mins = (EditText) findViewById(R.id.editText4);
		sets = (EditText) findViewById(R.id.editText5);
		hrs = (EditText) findViewById(R.id.editText6);
		chb = (CheckBox) findViewById(R.id.checkBox1);
		chb.setTypeface(tfGS);
		next = (Button) findViewById(R.id.button1);
		back = (Button) findViewById(R.id.button2);
		results = (Button) findViewById(R.id.button3);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
		results.setOnClickListener(this);
		
		changeFonts(layer);
		
        age.setError(null);
        height.setError(null);
        weight.setError(null);
        mins.setError(null);
        sets.setError(null);
        hrs.setError(null);
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
	
	private boolean boundCheck(double low,double up,EditText e)
	{
		String data = e.getText().toString();
		
		if (TextUtils.isEmpty(data))
		{
			e.setError("empty field");
			return false;
		}
		else
		{
			//if (Integer.parseInt(data) == 0)
		//	{
			//	e.setError("invalid value");
		//		return false;
			//}
			//else
			//{
				if (Integer.parseInt(data) < low || Integer.parseInt(data) > up) 
				{
					e.setError("be realistic!");
					return false;
				}
			//}
		}
		return true;
	}
	
	private boolean stepOneValid()
	{
		boolean ageFlag = true;
		boolean heightFlag = true;
		boolean weightFlag = true;
		
		age.setError(null);
		height.setError(null);
		weight.setError(null);	
		
		ageFlag = boundCheck(4,125,age);
		heightFlag = boundCheck(40,270,height);
		weightFlag = boundCheck(20,300,weight);
		
		
		if (ageFlag && heightFlag && weightFlag)
		{
			return true;
		}
		return false;
	}
	
	private boolean stepTwoValid()
	{
		boolean minsFlag = true;
		boolean setsFlag = true;
		boolean hrsFlag = true;
		
		mins.setError(null);
		sets.setError(null);
		hrs.setError(null);	
		
		minsFlag = boundCheck(0,60*24,mins);
		setsFlag = boundCheck(0,50,sets);
		hrsFlag = boundCheck(1,24,hrs);

		if (minsFlag && setsFlag && hrsFlag)
		{
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.button1:
			if (stepOneValid())
			{
				st1.setVisibility(View.GONE);
				st2.setVisibility(View.VISIBLE);
				currentStep = 1;
			}
			break;
		case R.id.button2:
			st2.setVisibility(View.GONE);
			st1.setVisibility(View.VISIBLE);
			currentStep = 0;
			break;
		case R.id.button3:
			if (stepTwoValid())
			{
				TextView results = (TextView) findViewById(R.id.text_results);
				st2.setVisibility(View.GONE);
				st3.setVisibility(View.VISIBLE);
				currentStep = 2;
				results.setText(provideFeedback(Integer.parseInt(age.getText().toString()),
												Integer.parseInt(height.getText().toString()),
												Integer.parseInt(weight.getText().toString()),
												Integer.parseInt(mins.getText().toString()),
												Integer.parseInt(sets.getText().toString()),
												Integer.parseInt(hrs.getText().toString()),
												chb.isChecked()));
			}
			else
			{
				Toast.makeText(this, "Finish all fields first!", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private String provideFeedback(int a, int h, double w, int m, int s, int hrs, boolean ch)
	{
		String buildString = "";
		String commentMins = "You should devote more time to doing exercises";
		String commentSets = "You should also try to break your activities into smaller segments of, say, 10-15 minutes instead of doing everything in one or a couple of goes.\n";
		//String commentSleep = "You might be suffering from sleep deprivation and should consider changing your sleeping patterns UNLESS you are an 'early bird' or a 'night owl' which likes to sleep either 5-6 or 9-10 hours respectively.";
		String commentSleep = "";
		String commentSleepFew = " hours a day is not enough, you need more sleep to avoid sleep deprivation tht might have unhealthy consequences!";
		String commentSleepMuch =" hours a day is too much, are you a cat by chance? Sleeping too much is not good!";
		String commentSleepEB = "You might be suffering from sleep deprivation and should consider changing your sleeping patterns UNLESS you are an 'early bird' which likes to sleep for 5-6 hours.";
		String commentSleepNO = "You might be suffering from sleep deprivation and should consider changing your sleeping patterns UNLESS you are a 'night owl' which likes to sleep for 9-10 hours.";
		boolean adult = false;
		boolean enoughMinutes = false;
		boolean enoughExercising = false;
		boolean enoughSleep = false;
		int good = 0;
		
		if(a > 18) adult = true; 
		if (!ch) good++;
		
		if (adult)
		{
			if(m >= 30)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm ";
			}	
			else
			{
				commentMins += " (30 minutes per day is the norm).\n";
			}
			if (s >= 2 && s <= 3 && m >= 20)
			{
				enoughExercising = true;
				good++;
				commentSets = "and by the looks of it, you know how to efficiently do exercises!\n";
			}
			else
			{
				if(!((m / s == 2) || (m/s == 3)) && s >= 2 && m >= 20)
				{
					commentSets = "";
				}
			}
			if (hrs  == 7 || hrs == 8)
			{
				enoughSleep = true;//make note of early birds/ owl
				good++;
				commentSleep = "The number of hours you sleep is fine.";
			}
			else
			{
				if (hrs < 7 && hrs > 4)
				{
					commentSleep = commentSleepEB;
				}
				else
				{
					if (hrs > 8 && hrs < 11)
					{
						commentSleep = commentSleepNO;
					}
					else
					{
						if (hrs < 4)
						{
							commentSleep = String.valueOf(hrs) + commentSleepFew;
						}
						else
						{
							if (hrs > 10)
							{
								commentSleep = String.valueOf(hrs) + commentSleepMuch;
							}
						}
					}
				}
			}
			
		}
		else
		{
			if (m > 50)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm ";
			}
			else
			{
				commentMins += " (60 minutes per day is the norm).\n";
			}
			if (s >= 3 && m >= 30)
			{
				enoughExercising = true;
				good++;
				commentSets = "and by the looks of it, you know how to efficiently do exercises!\n";
			}
			if (hrs == 8 || hrs == 9)
			{
				enoughSleep = true;
				good++;
				commentSleep = "The number of hours you sleep is fine.";
			}
			else
			{
				if (hrs < 7 && hrs > 4)
				{
					commentSleep = commentSleepEB;
				}
				else
				{
					if (hrs > 8 && hrs < 11)
					{
						commentSleep = commentSleepNO;
					}
					else
					{
						if (hrs < 4)
						{
							commentSleep = String.valueOf(hrs) + commentSleepFew;
						}
						else
						{
							if (hrs > 10)
							{
								commentSleep = String.valueOf(hrs) + commentSleepMuch;
							}
						}
					}
				}
			}
		}
		 
		
		switch (good)
		{
			case 0: buildString += "Woah, the your current lifestyle is bad, you have to make certain improvements!\n";
					break;
			case 1: buildString += "Hmm..Your current lifestyle is not heathy!\n";
					break;
			case 2: buildString += "Good, although we recommend You to change your lifestyle a bit though.\n";
					break;
			case 3: buildString += "Very good! You handle your lifestyle pretty well! \n";
					break;
			case 4: buildString += "Excellent, you have a very active and healthy lifestyle!\n";
					break;
		}

		buildString += "Your BMI (Biomass Index) is " + calculateBMI(h,w) + ", which means that " 
						+ bmiFeedback(Double.parseDouble(calculateBMI(h,w))) + "\n" + commentMins + commentSets + commentSleep;
		
		return buildString;
		
	}
	
	private String calculateBMI(int h, double w)
	{
		double res = 0;

		res = w / (h * 0.01 * h *0.01);
		String resToString = String.format("%.2f",res);  
		return resToString;
	}
	
	public String bmiFeedback(double res)
	{
		if (res <= UWL)
		{
			return "You are underweight, You need to eat more, broad your ration!";
		}
		if (res >UWL && res < NWL)
		{
			return "Your weight is normal, keep it like that!";
		}
		if (res > NWL && res <= OWL)
		{
			return "You are overweight, consider sticking to a diet and doing more exercises.";
		}
		if (res > OWL)
		{
			return "You really need to lose some weight! You should really consider going on a diet!";
		}
		
		return "";
		
	}
	
	public boolean bmiOK(double res)
	{
		if (res >UWL && res < NWL)
		{
			return true;
		}
		return false;
	}

	
	@Override
	public void onBackPressed() 
	{
		if (currentStep == 2)
		{
			st3.setVisibility(View.GONE);
			st2.setVisibility(View.VISIBLE);
			st1.setVisibility(View.GONE);
			currentStep--;
			//finish();
		}
		else
		{
			if (currentStep == 1)
			{
				st3.setVisibility(View.GONE);
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

}
