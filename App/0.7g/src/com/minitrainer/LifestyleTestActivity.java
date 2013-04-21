package com.minitrainer;

import com.minitrainer.ExerciseActivity.pushExerciseCooldowns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LifestyleTestActivity extends Activity implements OnClickListener {
	// used to be QuestionnaireActivity
	EditText age,height,weight,mins,sets,hrs;
	CheckBox chb;
	Button back,next,results,finish;
	LinearLayout st1,st2,st3,layer;
	ScrollView  sv;
	TextView main,bmi,ex,sl;
	int currentStep = 0;
	Typeface tfGS;
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lifestyle_test);
		currentStep = 0;
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		st1 = (LinearLayout) findViewById(R.id.qStep1);
		st2 = (LinearLayout) findViewById(R.id.qStep2);
		st3 = (LinearLayout) findViewById(R.id.qStep3);
		layer = (LinearLayout) findViewById(R.id.topLayer);
		sv = (ScrollView) findViewById(R.id.svQuest);
		//sv.setBackgroundColor(Color.parseColor("#c0c0c0")); // e7e7e9
		sv.setBackgroundResource(R.drawable.main_bg);
		st1.setVisibility(View.VISIBLE);
		st2.setVisibility(View.GONE);
		st3.setVisibility(View.GONE);
		
		age = (EditText) findViewById(R.id.editText1);
		height = (EditText) findViewById(R.id.editText2);
		weight = (EditText) findViewById(R.id.editText3);
		mins = (EditText) findViewById(R.id.editText4);
		sets = (EditText) findViewById(R.id.editText5);
		hrs = (EditText) findViewById(R.id.editText6);
		
		main = (TextView) findViewById(R.id.fbMain);
		bmi = (TextView) findViewById(R.id.fbBMI);
		ex = (TextView) findViewById(R.id.fbExercising);
		sl = (TextView) findViewById(R.id.fbSleep);
		
		chb = (CheckBox) findViewById(R.id.checkBox5);
		chb.setTypeface(tfGS);
		next = (Button) findViewById(R.id.nextPage);
		back = (Button) findViewById(R.id.goBack);
		results = (Button) findViewById(R.id.getResults);
		finish = (Button) findViewById(R.id.finishTest);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
		results.setOnClickListener(this);
		finish.setOnClickListener(this);
		next.setTypeface(tfGS);
		back.setTypeface(tfGS);
		results.setTypeface(tfGS);
		finish.setTypeface(tfGS);
		
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
				}
			}	
		}
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
				if (Double.parseDouble(data) < low || Double.parseDouble(data) > up) 
				{
					e.setError("be realistic!");
					return false;
				}
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
		boolean legitFlag = true;
		
		mins.setError(null);
		sets.setError(null);
		hrs.setError(null);	
		
		minsFlag = boundCheck(0,60*24,mins);
		setsFlag = boundCheck(0,50,sets);
		hrsFlag = boundCheck(1,24,hrs);
		
		if (!(TextUtils.isEmpty(mins.getText().toString()) &&  TextUtils.isEmpty(sets.getText().toString())))
		{
			if ((Integer.parseInt(mins.getText().toString()) == 0 && Integer.parseInt(sets.getText().toString()) != 0) ||
				(Integer.parseInt(mins.getText().toString()) != 0 && Integer.parseInt(sets.getText().toString()) == 0))
			{
				legitFlag = false;
				if (Integer.parseInt(mins.getText().toString()) == 0) mins.setError("invalid value");
				else sets.setError("invalid value");
			}
		}
		if (minsFlag && setsFlag && hrsFlag && legitFlag)
		{
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.nextPage:
			if (stepOneValid())
			{
				st1.setVisibility(View.GONE);
			    st2.setVisibility(View.VISIBLE);
				currentStep = 1;
			}
			break;
		case R.id.goBack:
			st2.setVisibility(View.GONE);
			st1.setVisibility(View.VISIBLE);
			currentStep = 0;
			break;
		case R.id.getResults:
			if (stepTwoValid())
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(hrs.getWindowToken(), 0);
				st2.setVisibility(View.GONE);
				st3.setVisibility(View.VISIBLE);
				currentStep = 2;
				provideFeedback(Integer.parseInt(age.getText().toString()),
								Integer.parseInt(height.getText().toString()),
								Double.parseDouble(weight.getText().toString()),
								Integer.parseInt(mins.getText().toString()),
								Integer.parseInt(sets.getText().toString()),
								Integer.parseInt(hrs.getText().toString()),
								chb.isChecked());
			}
			else
			{
				Toast.makeText(this, "Fill in all fields correctly first!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.finishTest: this.finish();
			break;
		}
	}
	
	private void provideFeedback(int a, int h, double w, int m, int s, int hrs, boolean ch)
	{
		String temp = "";
		String commentMins = "You should devote more time to doing exercises";
		String commentSets = "You should try to break your activities into bigger segments of 10-15 minutes.\n";
		//String commentSleep = "You might be suffering from sleep deprivation and should consider changing your sleeping patterns UNLESS you are an 'early bird' or a 'night owl' which likes to sleep either 5-6 or 9-10 hours respectively.";
		String commentSleep = "";
		String commentSleepFew = " hours of sleep a day is not enough, you need more sleep to avoid sleep deprivation that might have unhealthy consequences!";
		String commentSleepMuch =" hours of sleep a day is too much!\nAre you a cat by chance? Sleeping too much is not good!";
		String commentSleepEB = "You are undersleeping!\nAre you an 'early bird' by chance? If not, you might be suffering from sleep deprivation and should consider changing your sleeping patterns.";
		String commentSleepNO = "You are oversleeping!\nAre you a 'night owl' by chance? Even though oversleeping is not as bad as undersleeping you should sleep less than you do now,as it is unhealthy.";
		boolean adult = false;
		boolean enoughMinutes = false;
		boolean enoughExercising = false;
		boolean enoughSleep = false;
		int good = 0;
		
		if(a > 18) adult = true; 
		if (!ch) good++;
		if(bmiOK(Double.parseDouble(calculateBMI(h,w)))) good++;

		
		if (adult)
		{
			if(m >= 30 && s > 0)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm (30 mins). ";
			}	
			else
			{	
				commentMins += " (30 minutes per day is the norm for you).\n";
			}
			
			
			if(s >= 2 && s <= 3 && m >= 30)
			{
				enoughExercising = true;
				good++;
				if (m / (s * 1.0) > 15) commentSets = "You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
				else commentSets = "You are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
			}
			else
			{
				if (s == 0 || s > 3 && m < 30)
				{
					if (!((m / (s * 1.0)) < 10)) commentSets = "";
				}
				////
				if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m >= 30)
				{
					enoughExercising = true;
					good++;
					commentSets = " Also, you are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
				}
				//-----
				else
				{
					if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m < 30)
					{
						commentSets = "Even though you do not devote that much time to exercises, you do them efficiently(10-15 minutes on a single set of exercise activities).\n";
					}
					if (m / (s * 1.0) > 15)
					{
						commentSets = " You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
					}
				}
			}
			
			
			if (hrs  == 7 || hrs == 8)
			{
				enoughSleep = true;//make note of early birds/ owl
				good++;
				commentSleep = "The number of hours you sleep is fine (7-8 hours).";
			}
			else
			{
				if (hrs < 7 && hrs > 4)
				{
					commentSleep = commentSleepEB + "(you should sleep on average 7-8 hours)";
				}
				else
				{
					if (hrs > 8 && hrs < 11)
					{
						commentSleep = commentSleepNO + "(you should sleep on average 7-8 hours)";
					}
					else
					{
						if (hrs <= 4)
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
			if (m >= 60 && s > 0)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm (60mins). ";
			}
			else
			{
				commentMins += " (60 minutes per day is the norm for you).\n";
			}
			
			//
			if(s >= 4 && s <=6  && m >= 60)
			{
				enoughExercising = true;
				good++;
				if (m / (s * 1.0) > 15) commentSets = "You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
				else commentSets = "You are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
			}
			else
			{
				if (s == 0 || s > 6 && m < 60)
				{
					if (!((m / (s * 1.0)) < 10)) commentSets = "";
				}
				////
				if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m >= 60)
				{
					enoughExercising = true;
					good++;
					commentSets = ". Also, you are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
				}
				else
				{
					if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m < 60)
					{
						commentSets = "Even though you do not devote that much time to exercises, you do them efficiently(10-15 minutes on a single set of exercise activities).\n";
					}
					if (m / (s * 1.0) > 15)
					{
						commentSets = " You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
					}
				}
				
			}
			if (hrs == 8 || hrs == 9)
			{
				enoughSleep = true;
				good++;
				commentSleep = "The number of hours you sleep is fine (7-8 hours).";
			}
			else
			{
				if (hrs < 7 && hrs > 4)
				{
					commentSleep = commentSleepEB + "(you should sleep on average 9 hours)";
				}
				else
				{
					if (hrs > 8 && hrs < 11)
					{
						commentSleep = commentSleepNO + "(you should sleep on average 7-8 hours)";
					}
					else
					{
						if (hrs <= 4)
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
			case 0: main.setText("Woah, your current lifestyle is completely uhealthy, you have to make drastic changes in your lifestyle!!\n");
					break;
			case 1: main.setText("Hmm..Your current lifestyle needs improvements to become more or less healthy!");
					break;
			case 2: main.setText("Hmm..Your current lifestyle is not healthy enough!\n");
					break;
			case 3: main.setText("Your current lifestyle is moderately good, although it's better for you to make a few changes to make it better\n");
					break;
			case 4: main.setText("Your current lifestyle is very good! You handle your lifestyle pretty well, however you can improve it further! \n");
					break;
			case 5: main.setText("Excellent, you have a very active and healthy lifestyle!\n");
					break;
		}
		bmi.setText("Your BMI (Body Mass Index) is " + calculateBMI(h,w) + ", which means that " + bmiFeedback(Double.parseDouble(calculateBMI(h,w))) + "\n");
		ex.setText(commentMins + commentSets);
		sl.setText(commentSleep);
		//buildString += "Your BMI (Biomass Index) is " + calculateBMI(h,w) + ", which means that " 
					//	+ bmiFeedback(Double.parseDouble(calculateBMI(h,w))) + "\n" + commentMins + commentSets + commentSleep;
		
		//return buildString;
		
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
			return "you are underweight, You need to eat more, broad your ration!";
		}
		if (res >UWL && res < NWL)
		{
			return "your weight is normal, keep it like that by exercising and occasionally sticking to diets.";
		}
		if (res > NWL && res <= OWL)
		{
			return "you are overweight, consider sticking to a diet and doing more exercises.";
		}
		if (res > OWL)
		{
			return "you really need to lose some weight! You should really consider going on a diet!";
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
