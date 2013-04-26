package com.minitrainer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// Activity that acts as a lifestyle test for the user that gives feedback based on user inputs
public class LifestyleTestActivity extends Activity implements OnClickListener,OnTouchListener {
	// used to be QuestionnaireActivity
	EditText age,height,weight,mins,sets,hrs;
	CheckBox chb;
	Button back,next,results,finish;
	LinearLayout st1,st2,st3,layer;
	ScrollView  sv;
	TextView main,bmi,ex,sl,style;
	int currentStep = 0;
	Typeface tfGS;
	
	InputMethodManager imm;
	
	final double UWL = 18.5;
	final double NWL = 24.9;
	final double OWL = 29.9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lifestyle_test);
		currentStep = 0;
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		st1 = (LinearLayout) findViewById(R.id.qStep1);
		st2 = (LinearLayout) findViewById(R.id.qStep2);
		st3 = (LinearLayout) findViewById(R.id.qStep3);
		layer = (LinearLayout) findViewById(R.id.topLayer);
		sv = (ScrollView) findViewById(R.id.svQuest);
		
		layer.setOnTouchListener(this);
		
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
		style = (TextView) findViewById(R.id.lstyle);
		
		chb = (CheckBox) findViewById(R.id.checkBox5);
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
		chb.setTypeface(tfGS);
		
		changeFonts(layer);
		
        age.setError(null);
        height.setError(null);
        weight.setError(null);
        mins.setError(null);
        sets.setError(null);
        hrs.setError(null);
	}
	
	//changes fonts of textviews/buttons, and so on
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
	
	//builds a string with different text colour (text not showing without setting the colour)
	private SpannableStringBuilder getSpanString(String arg)
	{
		int color = Color.BLACK; 
		String string = arg;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(string);
		ssbuilder.setSpan(fgcspan, 0, string.length(), 0);
		return ssbuilder;

	}
	
	// checks whether the value is within the bounds ( either realistic or inputed at all)
	private boolean boundCheck(double low,double up,EditText e)
	{
		String data = e.getText().toString();
		
		if (TextUtils.isEmpty(data))
		{
			e.setError(getSpanString("empty field"));
			e.requestFocus();
			return false;
		}
		else
		{
				if (Double.parseDouble(data) < low || Double.parseDouble(data) > up) 
				{
					e.setError(getSpanString("be realistic!"));
					e.requestFocus();
					return false;
				}
		}
		return true;
	}
	
	//checks whether the first three edittexts are filled correctly
	private boolean stepOneValid()
	{
		boolean ageFlag = true;
		boolean heightFlag = true;
		boolean weightFlag = true;
		age.setError(null);
		height.setError(null);
		weight.setError(null);	
		
		weightFlag = boundCheck(20,300,weight);
		heightFlag = boundCheck(40,270,height);
		ageFlag = boundCheck(4,125,age);
		
		if (ageFlag && heightFlag && weightFlag)
		{
			return true;
		}
		return false;
	}
	//checks whether the last three edittexts are filled correctly( on page 2)
	private boolean stepTwoValid()
	{
		boolean minsFlag = true;
		boolean setsFlag = true;
		boolean hrsFlag = true;
		boolean legitFlag = true;
		
		mins.setError(null);
		sets.setError(null);
		hrs.setError(null);	
		
		hrsFlag = boundCheck(1,24,hrs);
		setsFlag = boundCheck(0,50,sets);
		minsFlag = boundCheck(0,40,mins);
		
		if (!(TextUtils.isEmpty(mins.getText().toString())) &&  (!(TextUtils.isEmpty(sets.getText().toString()))))
		{
			if ((Integer.parseInt(mins.getText().toString()) == 0 && Integer.parseInt(sets.getText().toString()) != 0) ||
				(Integer.parseInt(mins.getText().toString()) != 0 && Integer.parseInt(sets.getText().toString()) == 0))
			{
				legitFlag = false;
				if (Integer.parseInt(mins.getText().toString()) == 0) mins.setError(getSpanString("invalid value"));
				else sets.setError(getSpanString("invalid value"));
			}
		}
		if (minsFlag && setsFlag && hrsFlag && legitFlag)
		{
			return true;
		}
		return false;
	}
	// handles click events
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.nextPage:
			if (stepOneValid())
			{
				try{imm.hideSoftInputFromWindow(hrs.getWindowToken(), 0);}
				catch(Exception e){e.printStackTrace();};
				st1.setVisibility(View.GONE);
			    st2.setVisibility(View.VISIBLE);
				currentStep = 1;
			}
			break;
		case R.id.goBack:
			try{imm.hideSoftInputFromWindow(hrs.getWindowToken(), 0);}
			catch(Exception e){e.printStackTrace();};
			st2.setVisibility(View.GONE);
			st1.setVisibility(View.VISIBLE);
			currentStep = 0;
			break;
		case R.id.getResults:
			currentStep = 2;
			if (stepTwoValid())
			{
				try{imm.hideSoftInputFromWindow(hrs.getWindowToken(), 0);}
				catch(Exception e){e.printStackTrace();};
				st2.setVisibility(View.GONE);
				st3.setVisibility(View.VISIBLE);
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
	
	// provides feedback based on the data received from the user
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
		String commentCheckbox = "";
		boolean adult = false;
		boolean enoughMinutes = false;
		boolean enoughExercising = false;
		boolean enoughSleep = false;
		int good = 0;
		
		if(a > 18) adult = true; 
		if (!ch)
		{
			good++;
			commentCheckbox = "You noted that you think you have a sedentary lifestyle.\nYou should avoid sitting or lying for too long (usually that is the case when you watch TV, use the computer), particularly without any breaks or exercising inbetween. Such lifestyle can have a negative impact on your health.\nTherefore, we would advise you to become more active and keep track of how much you spend on activities that involve prolonged sitting or lying, take breaks and to do your daily exercise norm.";
		}
		else
		{
			commentCheckbox = "You did not note that you have a sedentary lifestyle.\nThat is very good, keep it like that, stay active!";
		}
		if(bmiOK(Double.parseDouble(calculateBMI(h,w)))) good++;

		
		if (adult)
		{
			m = m * 60;
			if(m >= 30 * 7 && s > 0)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm (around 30 mins a day). ";
			}	
			else
			{	
				commentMins += " (30 minutes per day is the norm for you).\n";
			}
			
			
			if(s >= 2 * 7 && s <= 3 * 7 && m >= 30 * 7)
			{
				enoughExercising = true;
				good++;
				if (m / (s * 1.0) > 15) 
				commentSets = "You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
				else commentSets = "You are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
			}
			else
			{
				if (s == 0 || s > 3 * 7 && m < 30*7)
				{
					if (!((m / (s * 1.0)) < 10)) commentSets = "";
				}

				if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m >= 30 * 7)
				{
					enoughExercising = true;
					good++;
					commentSets = " Also, you are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
				}
				else
				{
					if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m < 30 * 7)
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
				enoughSleep = true;
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
			m = m * 60;
			
			if (m >= 60 * 7 && s > 0)
			{
				enoughMinutes = true;
				good++;
				commentMins = "The amount of time you spend on exercises suits your norm ( around 1 hour a day). ";
			}
			else
			{
				commentMins += " (1 hour per day is the norm for you).\n";
			}
			
			if(s >= 4 * 7 && s <= 6 * 7 && m >= 60 * 7)
			{
				enoughExercising = true;
				good++;
				if (m / (s * 1.0) > 15) commentSets = "You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
				else commentSets = "You are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
			}
			else
			{
				if (s == 0 || s > 6 * 7 && m < 60 * 7)
				{
					if (!((m / (s * 1.0)) < 10)) commentSets = "";
				}

				if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m >= 60 * 7)
				{
					enoughExercising = true;
					good++;
					commentSets = ". Also, you are doing your exercises efficiently (10-15 minutes on a single set of exercise activites).\n";
				}
				else
				{
					if (m / (s * 1.0) <= 15 && m / (s * 1.0) >= 10 && m < 60 * 7)
					{
						commentSets = "Even though you do not devote that much time to exercises, you do them efficiently(10-15 minutes on a single set of exercise activities).\n";
					}
					if (m / (s * 1.0) > 15)
					{
						commentSets = "You should try to break your activities into smaller segments of 10-15 minutes instead of doing everything in one or a couple of goes.\nHowever, if you go to gym it is OK to workout for more than 15 minutes.";
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
		style.setText(commentCheckbox);
		
	}
	
	// calculates the BMI(Body mass index)
	private String calculateBMI(int h, double w)
	{
		double res = 0;

		res = w / (h * 0.01 * h *0.01);
		String resToString = String.format("%.2f",res);  
		return resToString;
	}
	
	// returns feedback based on the BMI calculated
	public String bmiFeedback(double res)
	{
		if (res <= UWL)
		{
			return "you are underweight, You need to eat more, check out some of the diets!";
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
	
	// checks whether the user is of normal weight
	public boolean bmiOK(double res)
	{
		if (res >UWL && res < NWL)
		{
			return true;
		}
		return false;
	}

	// allows the user to go back to previous pages/steps and to get back to parent(TabbedActivity) activity
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
	
	// hides soft keyboard on touch
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		try{
			if (v.getId() == R.id.topLayer)
			{
				InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
