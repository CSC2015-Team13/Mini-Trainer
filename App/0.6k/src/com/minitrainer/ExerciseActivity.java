package com.minitrainer;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;


public class ExerciseActivity extends Activity implements OnClickListener
{
	final double EXP_SCALE = 1.06;
	final double LEVEL_SCALE = 1.3;
	int [] rewards = {45,9,7,9,10,11,9,8};//0 = 12
	int NUMOFEX = 8;
    int gained_exp,exercises_done,currLevel,lvlCap,currExp,lastLevel;
    Exercise ex[] = new Exercise[NUMOFEX];
    SharedPreferences userState;
    SharedPreferences sp;
    Editor edit;
    Date d = new Date();
    AlertDialog.Builder alert;
  	Intent intentGlob;
  	ScrollView sv;
      
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        userState = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences(userState.getString("username","username"),0);
        edit = sp.edit();
        gained_exp = 0;
        exercises_done = 0;
    	alert = new AlertDialog.Builder(this);
    	intentGlob = getIntent();
    	Bundle extras = intentGlob.getExtras();
    	currLevel = extras.getInt("LEVEL", 1);
    	lastLevel = currLevel;
    	currExp= extras.getInt("CURREXP",0);
    	lvlCap = extras.getInt("EXPCAP",50);
    	sv = (ScrollView) findViewById(R.id.svEx);
        setupExercises();
     
        loadExProgress();
        Toast.makeText(this, "Remember to stretch/warm up before doing any of the exercises!", Toast.LENGTH_SHORT).show();
    }
    
    
    //additional check (user confirmation of completed exercise)
    private void setupAlert(final int i)
    {
		alert.setTitle("Just checking..")
		.setMessage("Have you completed the exercise?")
		.setCancelable(false).setPositiveButton("Yes!",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				gained_exp = gained_exp + ex[i].getExp() ;
				exercises_done++;
				ex[i].setStateToLocked(ExerciseActivity.this);
				Toast.makeText(ExerciseActivity.this, "Experience points got: " + String.valueOf(ex[i].getExp()),Toast.LENGTH_SHORT).show();
				
				if (gained_exp + currExp >= lvlCap)
				{
					System.out.println("Gained: " + gained_exp +"; Current: " + currExp + " >= " + lvlCap);
					lastLevel = currLevel;
					currLevel++;
					setExp(currLevel);
					currExp = lvlCap - (gained_exp + currExp);
					lvlCap =(int)(lvlCap * LEVEL_SCALE);
					if (lvlCap % 10 > 5)
					{
						lvlCap -= ((lvlCap % 10) % 5);
					}
					else
					{
						if (lvlCap%10 != 5)
						{
							lvlCap -= (lvlCap % 10);
						}
					}
					Toast.makeText(ExerciseActivity.this, "Congratulations!\nYou've reached level " + currLevel, Toast.LENGTH_SHORT).show();
					if (currLevel % 3 == 0)
					{
						Toast.makeText(ExerciseActivity.this, "The Quiz\nhas become has become available!", Toast.LENGTH_SHORT).show();
					}
					if (currLevel % 2 == 0)
					{
						Toast.makeText(ExerciseActivity.this, "A new set of facts has been unlocked!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		  })
		.setNegativeButton("Not yet",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		
		alert.show();
    }
    
    private void setupExercises()
    {
    	//add in order you want them to appear // make sure it's in the xml!
    	int [] ex0Txt = {R.string.text_ex1_step1,R.string.text_ex1_step2,R.string.text_ex1_step3};
    	int [] ex1Txt = {R.string.text_ex2_step1,R.string.text_ex2_step2,R.string.text_ex2_step3};
    	int [] ex2Txt = {R.string.text_ex3_step1,R.string.text_ex3_step2,R.string.text_ex3_step3};
    	int [] ex3Txt = {R.string.text_ex4_step1,R.string.text_ex4_step2,R.string.text_ex4_step3};
    	int [] ex4Txt = {R.string.text_ex5_step1,R.string.text_ex5_step2,R.string.text_ex5_step3,R.string.text_ex5_step4};
    	int [] ex5Txt = {R.string.text_ex6_step1,R.string.text_ex6_step2,R.string.text_ex6_step3,R.string.text_ex6_step4,R.string.text_ex6_step5};
    	int [] ex6Txt = {R.string.text_ex7_step1,R.string.text_ex7_step2,R.string.text_ex7_step3,R.string.text_ex7_step4};
    	int [] ex7Txt = {R.string.text_ex8_step1,R.string.text_ex8_step2,R.string.text_ex8_step3};
    	
    	int [] ex0Img = {R.drawable.pushups,R.drawable.pushups,R.drawable.pushups};
    	int [] ex1Img = {R.drawable.crunches,R.drawable.crunches,R.drawable.crunches};
    	int [] ex2Img = {R.drawable.squats,R.drawable.squats,R.drawable.squats_up};
    	int [] ex3Img = {R.drawable.abs_step1,R.drawable.abs_step2,R.drawable.abs_step2};
    	int [] ex4Img = {R.drawable.climb_step1,R.drawable.climb_step2,R.drawable.climb_step2,R.drawable.climb_step3};
    	int [] ex5Img = {R.drawable.leg_step1,R.drawable.leg_step2,R.drawable.leg_step3,R.drawable.leg_step2,R.drawable.leg_step1};
    	int [] ex6Img = {R.drawable.frog_step1,R.drawable.frog_step2,R.drawable.frog_step3,R.drawable.frog_step4};
    	int [] ex7Img = {R.drawable.ball_step1,R.drawable.ball_step2,R.drawable.ball_step3};
    	
    	ex[0] = new Exercise(this,rewards[0],R.id.exButton1,R.id.exPanel1,R.id.arrLeft1,R.id.stepProgress1,R.id.arrRight1,R.id.currStep1,ex0Txt,R.id.exGuide1,ex0Img, R.id.complete_ex1,6000);//15
    	ex[1] = new Exercise(this,rewards[1],R.id.exButton2,R.id.exPanel2,R.id.arrLeft2,R.id.stepProgress2,R.id.arrRight2,R.id.currStep2,ex1Txt,R.id.exGuide2,ex1Img, R.id.complete_ex2,3000);//12
    	ex[2] = new Exercise(this,rewards[2],R.id.exButton3,R.id.exPanel3,R.id.arrLeft3,R.id.stepProgress3,R.id.arrRight3,R.id.currStep3,ex2Txt,R.id.exGuide3,ex2Img, R.id.complete_ex3,4500);//10
    	ex[3] = new Exercise(this,rewards[3],R.id.exButton4,R.id.exPanel4,R.id.arrLeft4,R.id.stepProgress4,R.id.arrRight4,R.id.currStep4,ex3Txt,R.id.exGuide4,ex3Img, R.id.complete_ex4,4500);
    	ex[4] = new Exercise(this,rewards[4],R.id.exButton5,R.id.exPanel5,R.id.arrLeft5,R.id.stepProgress5,R.id.arrRight5,R.id.currStep5,ex4Txt,R.id.exGuide5,ex4Img, R.id.complete_ex5,4500);
    	ex[5] = new Exercise(this,rewards[5],R.id.exButton6,R.id.exPanel6,R.id.arrLeft6,R.id.stepProgress6,R.id.arrRight6,R.id.currStep6,ex5Txt,R.id.exGuide6,ex5Img, R.id.complete_ex6,4500);
    	ex[6] = new Exercise(this,rewards[6],R.id.exButton7,R.id.exPanel7,R.id.arrLeft7,R.id.stepProgress7,R.id.arrRight7,R.id.currStep7,ex6Txt,R.id.exGuide7,ex6Img, R.id.complete_ex7,4500);
    	ex[7] = new Exercise(this,rewards[7],R.id.exButton8,R.id.exPanel8,R.id.arrLeft8,R.id.stepProgress8,R.id.arrRight8,R.id.currStep8,ex7Txt,R.id.exGuide8,ex7Img, R.id.complete_ex8,4500);
    	
    	setExp(currLevel);
    	//add listeners and etc
        for (int i=0;i < NUMOFEX;i++)
        {
        	ex[i].getPnlBtn().setOnClickListener(this);
        	ex[i].getCmpltBtn().setOnClickListener(this);
        	//new
        	ex[i].getBackView().setOnClickListener(this);
        	ex[i].getBackView().setEnabled(false);
        	ex[i].getForthView().setOnClickListener(this);
        }
    }
    
    // scales the experience rewards of exercises
    public void setExp(int level)
    {	
    	double[] tempRewards = {0,0,0,0,0,0,0,0};
		if (level != 1)
		{
			for(int i = 0;i < rewards.length;i++)
			{
				tempRewards[i] = ex[i].getExp();
				for(int j = 0; j < currLevel;j++)
				{
					tempRewards[i] = tempRewards[i] * EXP_SCALE;
        				
				}
				System.out.println("ex[" + i +"] rewards is " + tempRewards[i]);

				//rewards[i] = (int) tempRewards[i];
				ex[i].setReward((int) tempRewards[i]);
    		}
		}
    }	
    
	public void onClick(View v) 
	{
		int [] pButtons = {R.id.exButton1,R.id.exButton2,R.id.exButton3,R.id.exButton4,R.id.exButton5,R.id.exButton6,R.id.exButton7,R.id.exButton8};
		int [] exButtons = {R.id.complete_ex1,R.id.complete_ex2,R.id.complete_ex3,R.id.complete_ex4,R.id.complete_ex5,R.id.complete_ex6,R.id.complete_ex7,R.id.complete_ex8};
		int [] stepBack = {R.id.arrLeft1,R.id.arrLeft2,R.id.arrLeft3,R.id.arrLeft4,R.id.arrLeft5,R.id.arrLeft6,R.id.arrLeft7,R.id.arrLeft8};
		int [] stepForth = {R.id.arrRight1,R.id.arrRight2,R.id.arrRight3,R.id.arrRight4,R.id.arrRight5,R.id.arrRight6,R.id.arrRight7,R.id.arrRight8};
		//hate switch - gets too messy.
		if (v instanceof Button)
		{
			for(int i=0;i<pButtons.length;i++)
			{
				if (v.getId() == pButtons[i])
				{
					if (ex[i].panelPressed()){openPanelAt(i);}
					else {ex[i].foldPan();}
					break;
				}
			}
			for(int i=0;i<exButtons.length;i++)
			{
				if (v.getId() == exButtons[i])
				{
					setupAlert(i);
					break;
				}
			}
		}
		else
		{
			if (v instanceof ImageView)
			{
				for(int i=0;i<stepBack.length;i++)
				{
					if (v.getId() == stepBack[i])
					{
						ex[i].stepBack();
						//focusOnView(ex[i].getPnlBtn());
						break;
					}
				}
				for(int i=0;i<stepForth.length;i++)
				{
					if (v.getId() == stepForth[i])
					{
						ex[i].stepForth();
						break;
					}
				}
			}
		}
	}
	
    private void loadExProgress()
    {
    	String exTitle;
    	boolean btnState;
    	long timestamp;
    	
    	for(int i = 0;i<NUMOFEX;i++)
    	{
    		exTitle = sp.getString(ex[i].getNameTag(),ex[i].getName());
    		btnState = sp.getBoolean(ex[i].getBtnTag(),true);
    		timestamp = sp.getLong("TS" + ex[i].getNameTag(), ex[i].getTimestamp());
    		System.out.println("TS" + ex[i].getNameTag());
    		ex[i].loadState(this,exTitle,btnState,timestamp,d.getTime());
    	}
    }
	
    //inefficient, but it works.
	private void openPanelAt(int pos)
	{
		for(int i=0;i < NUMOFEX;i++)
		{
			if (pos == i)
			{
				ex[i].getPnl().setVisibility(View.VISIBLE);
				ex[i].getPnlBtn().setBackgroundResource(R.drawable.accordion_item_open);
			}
			else
			{
				ex[i].getPnl().setVisibility(View.GONE);
				ex[i].getPnlBtn().setBackgroundResource(R.drawable.accordion_item_closed);	
			}
		}
	}
	

	public void saveExProgress(String key, long val)
	{
		edit.putLong(key, val);
		edit.commit();
	}
    
    public void saveExProgress(String key, String val)
    {
    	edit.putString(key, val);
    	edit.commit();
    }
    
    public void saveExProgress(String key, boolean val)
    {
    	edit.putBoolean(key, val);
    	edit.commit();
    }
    public void saveExProgress(String key, int val)
    {
    	edit.putInt(key, val);
    	edit.putLong("lastUpdate", System.currentTimeMillis()/1000L);
    	edit.commit();
    }
    
	@Override
	public void onBackPressed() 
	{
	    Bundle bundle = new Bundle();
	    bundle.putInt("expPoints", gained_exp);
	    bundle.putInt("exercises_cmplt", exercises_done);
	    //bundle.putString("tabNo", "1");
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    setResult(RESULT_OK, intent);
	    super.onBackPressed();
	}
}
