package com.minitrainer;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class ExerciseActivity extends Activity implements OnClickListener
{
	  int NUMOFEX = 7;
      int gained_exp,exercises_done;
      Exercise ex[] = new Exercise[NUMOFEX];
      SharedPreferences userState;
      SharedPreferences sp;
      Editor edit;
      Date d = new Date();
  	  AlertDialog.Builder alert;
      
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        userState = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences(userState.getString("username","username"),0);
        edit = sp.edit();
        gained_exp = 0;
        exercises_done = 0;
    	alert = new AlertDialog.Builder(this);

		// create alert dialog
		//AlertDialog alertDialog = alert.create();

		// show it
		
	
        setupExercises();
     
        loadExProgress();
    }
    
    private void setupAlert(final int i)
    {
		alert.setTitle("Just checking..")
		.setMessage("Have you completed the exercise?")
		.setCancelable(false).setPositiveButton("Yes!",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				gained_exp = gained_exp + ex[i].getExp() ;
				exercises_done++;
				ex[i].setStateToLocked(ExerciseActivity.this);
				Toast.makeText(ExerciseActivity.this, "Experience points got: " + String.valueOf(ex[i].getExp()),Toast.LENGTH_SHORT).show();
			}
		  })
		.setNegativeButton("Emm..No",new DialogInterface.OnClickListener() {
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
    	int [] ex6Txt = {R.string.text_ex7_step1,R.string.text_ex7_step2,R.string.text_ex7_step3};
    	
    	int [] ex0Img = {R.drawable.pushups,R.drawable.pushups,R.drawable.pushups};
    	int [] ex1Img = {R.drawable.crunches,R.drawable.crunches,R.drawable.crunches};
    	int [] ex2Img = {R.drawable.squats,R.drawable.squats,R.drawable.squats_up};
    	int [] ex3Img = {R.drawable.abs_step1,R.drawable.abs_step2,R.drawable.abs_step2};
    	int [] ex4Img = {R.drawable.climb_step1,R.drawable.climb_step2,R.drawable.climb_step2,R.drawable.climb_step3};
    	int [] ex5Img = {R.drawable.leg_step1,R.drawable.leg_step2,R.drawable.leg_step3,R.drawable.leg_step2,R.drawable.leg_step1};
    	int [] ex6Img = {R.drawable.ball_step1,R.drawable.ball_step2,R.drawable.ball_step3};

    	ex[0] = new Exercise(this,20,R.id.exButton1,R.id.exPanel1,R.id.arrLeft1,R.id.stepProgress1,R.id.arrRight1,R.id.currStep1,ex0Txt,R.id.exGuide1,ex0Img, R.id.complete_ex1,6000);//15
    	ex[1] = new Exercise(this,15,R.id.exButton2,R.id.exPanel2,R.id.arrLeft2,R.id.stepProgress2,R.id.arrRight2,R.id.currStep2,ex1Txt,R.id.exGuide2,ex1Img, R.id.complete_ex2,3000);//12
    	ex[2] = new Exercise(this,30,R.id.exButton3,R.id.exPanel3,R.id.arrLeft3,R.id.stepProgress3,R.id.arrRight3,R.id.currStep3,ex2Txt,R.id.exGuide3,ex2Img, R.id.complete_ex3,4500);//10
    	ex[3] = new Exercise(this,25,R.id.exButton4,R.id.exPanel4,R.id.arrLeft4,R.id.stepProgress4,R.id.arrRight4,R.id.currStep4,ex3Txt,R.id.exGuide4,ex3Img, R.id.complete_ex4,4500);
    	ex[4] = new Exercise(this,22,R.id.exButton5,R.id.exPanel5,R.id.arrLeft5,R.id.stepProgress5,R.id.arrRight5,R.id.currStep5,ex4Txt,R.id.exGuide5,ex4Img, R.id.complete_ex5,4500);
    	ex[5] = new Exercise(this,12,R.id.exButton6,R.id.exPanel6,R.id.arrLeft6,R.id.stepProgress6,R.id.arrRight6,R.id.currStep6,ex5Txt,R.id.exGuide6,ex5Img, R.id.complete_ex6,4500);
    	ex[6] = new Exercise(this,19,R.id.exButton7,R.id.exPanel7,R.id.arrLeft7,R.id.stepProgress7,R.id.arrRight7,R.id.currStep7,ex6Txt,R.id.exGuide7,ex6Img, R.id.complete_ex7,4500);
    	
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
    
	public void onClick(View v) 
	{
		
		int [] pButtons = {R.id.exButton1,R.id.exButton2,R.id.exButton3,R.id.exButton4,R.id.exButton5,R.id.exButton6,R.id.exButton7};
		int [] exButtons = {R.id.complete_ex1,R.id.complete_ex2,R.id.complete_ex3,R.id.complete_ex4,R.id.complete_ex5,R.id.complete_ex6,R.id.complete_ex7};
		int [] stepBack = {R.id.arrLeft1,R.id.arrLeft2,R.id.arrLeft3,R.id.arrLeft4,R.id.arrLeft5,R.id.arrLeft6,R.id.arrLeft7};
		int [] stepForth = {R.id.arrRight1,R.id.arrRight2,R.id.arrRight3,R.id.arrRight4,R.id.arrRight5,R.id.arrRight6,R.id.arrRight7};
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
		
		/*
		switch (v.getId()){
			case R.id.exButton1:		
					if (ex[0].panelPressed()){openPanelAt(0);}
					else {ex[0].foldPan();}
				break;
			case R.id.exButton2:
					if (ex[1].panelPressed()){openPanelAt(1);}
					else {ex[1].foldPan();}
				break;
			case R.id.exButton3:
					if (ex[2].panelPressed()){openPanelAt(2);}
					else {ex[2].foldPan();}
			    break;
			case R.id.exButton4:
					if (ex[3].panelPressed()){openPanelAt(3);}
					else {ex[3].foldPan();}
				break;
			case R.id.exButton5:
					if (ex[4].panelPressed()){openPanelAt(4);}
					else {ex[4].foldPan();}
				break;
			case R.id.exButton6:
					if (ex[5].panelPressed()){openPanelAt(5);}
					else {ex[5].foldPan();}
				break;
			case R.id.exButton7:
					if (ex[6].panelPressed()){openPanelAt(6);}
					else {ex[6].foldPan();}
				break;
			case R.id.complete_ex1:
					setupAlert(0);
				break;
			case R.id.complete_ex2:
					setupAlert(1);
				break;
			case R.id.complete_ex3:
					setupAlert(2);
				break;
			case R.id.complete_ex4:
					setupAlert(3);
				break;
			case R.id.complete_ex5:
					setupAlert(4);
				break;	
			case R.id.complete_ex6:
					setupAlert(5);
				break;
			case R.id.complete_ex7:
				setupAlert(6);
			break;
			case R.id.arrRight1:
					ex[0].stepForth();
				break;
			case R.id.arrLeft1:
					ex[0].stepBack();
				break;
			case R.id.arrRight2:
					ex[1].stepForth();
				break;
			case R.id.arrLeft2:
					ex[1].stepBack();
			    break;
			case R.id.arrRight3:
					ex[2].stepForth();
			    break;
			case R.id.arrLeft3:
					ex[2].stepBack();
			    break;
			case R.id.arrRight4:
					ex[3].stepForth();
				break;
			case R.id.arrLeft4:
					ex[3].stepBack();
				break;
			case R.id.arrRight5:
					ex[4].stepForth();
				break;
			case R.id.arrLeft5:
					ex[4].stepBack();
				break;
			case R.id.arrRight6:
					ex[5].stepForth();
				break;
			case R.id.arrLeft6:
					ex[5].stepBack();
				break;
			case R.id.arrRight7:
					ex[6].stepForth();
				break;
		case R.id.arrLeft7:
					ex[6].stepBack();
				break;
		}
		*/
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
