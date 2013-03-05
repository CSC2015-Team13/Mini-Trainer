package com.minitrainer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExerciseActivity extends Activity implements OnClickListener
{
	  int NUMOFEX = 3;
	  Exercise ex[] = new Exercise[NUMOFEX];
      int gained_exp;
      int seconds,minutes,hours,day,month;
      Calendar c;
      //Need an array of "exercise" objects
      
      SharedPreferences sp;
      Editor edit;
      
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
        c = Calendar.getInstance();
        
        gained_exp = 0;
        
        setupExercises();
        
        for (int i=0;i < NUMOFEX;i++)
        {
        	ex[i].getPnlBtn().setOnClickListener(this);
        	ex[i].getCmpltBtn().setOnClickListener(this);
        	ex[i].getPnl().setVisibility(View.GONE);
        }
        loadExProgress();
    }
    
    private void setupExercises()
    {
    	//add in order you want them to appear
    	ex[0] = new Exercise(this,15,R.id.button1,R.id.panel1,R.id.complete_ex1,2);
    	ex[1] = new Exercise(this,12,R.id.button2,R.id.panel2,R.id.complete_ex2,1);
    	ex[2] = new Exercise(this,10,R.id.button3,R.id.panel3,R.id.complete_ex3,1);

    }
    
	public void onClick(View v) 
	{
		int timeStamp;
		
		switch (v.getId()){
			case R.id.button1:		
					if (ex[0].panelPressed()){enablePanelAt(0);}
					else {ex[0].foldPan();}
				break;
			case R.id.button2:
					if (ex[1].panelPressed()){enablePanelAt(1);}
					else {ex[1].foldPan();}
				break;
			case R.id.button3:
					if (ex[2].panelPressed()){enablePanelAt(2);}
					else {ex[2].foldPan();}
			    break;
			case R.id.complete_ex1:
					gained_exp = gained_exp + ex[0].getExp() ;
					ex[0].setStateToDone();
					saveExProgress("PUSHUPS","Simple pushups (Done)");
					saveExProgress("PUSHUPSBTN",false);
					recordTime();
					
				break;
			case R.id.complete_ex2:
					gained_exp = gained_exp + ex[1].getExp() ;
					ex[1].setStateToDone();
				break;
			case R.id.complete_ex3:
					gained_exp = gained_exp + ex[2].getExp() ;
					ex[2].setStateToDone();
				break;
		}
	}
	
    private void loadExProgress()
    {
    	String exTitle = sp.getString("PUSHUPS","Simple pushups");
    	System.out.println(exTitle);
    	boolean compButtonState = sp.getBoolean("PUSHUPSBTN",true);
    	ex[0].getPnlBtn().setText(exTitle);
    	ex[0].getCmpltBtn().setEnabled(compButtonState);
    	int pTime = sp.getInt("TIME", 0);
    	int pDay = sp.getInt("DAY",0);
    	int pMonth = sp.getInt("MONTH",0);
    	int pYear = sp.getInt("YEAR", 0);
    	System.out.println("year: " + pYear + " month: " + pMonth + " day:" + pDay + " time: " + pTime);
    	cooldownTracker(pYear,pMonth,pDay,pTime);
    }
    	
    private void cooldownTracker(int y, int m, int d, int t) // int cd, objectname ex
    {
    	if (t != 0 && d != 0 && m != 0 && y != 0)
    	{
    		if (y == c.get(Calendar.YEAR))
    		{
    			System.out.println("year is the same");
    			if (m == c.get(Calendar.MONTH) + 1)
    			{	
    				System.out.println("month is the same");
    				if (d == c.get(Calendar.DAY_OF_MONTH))
    				{
    					System.out.println("day is the same");
    					System.out.println("current: " + getCurrentTime() + " past time:" + t);
    					if(getCurrentTime() - t >= 30) // 3 is a test value
    					{
    						makeAvailable();
    					}
    				}
    				else
    				{
    					if(c.get(Calendar.DAY_OF_MONTH) - d == 1)
    					{
    						if (((dayTime() - t) + getCurrentTime()) >= 30) // 3 is a test value
    						{
    							makeAvailable();
    						}
    					}
    					else
    					{
    						makeAvailable();
    					}
    				}
    			}
    			else
    			{
    				if ((c.get(Calendar.MONTH) + 1 - m) == 1)
    				{
						if (((dayTime() - t) + getCurrentTime()) >= 30) // 3 is a test value
						{
							makeAvailable();
						}
    				}
    				else
    				{
    					makeAvailable();
    				}
    			}
    		}
    	}
    	else
    	{
			if ((c.get(Calendar.YEAR)- m) == 1)
			{
				if (((dayTime() - t) + getCurrentTime()) >= 30) // 3 is a test value
				{
					makeAvailable();
				}
			}
			else
			{
				makeAvailable();
			}
    	}
    }
    
    private void makeAvailable()
    {
		ex[0].getCmpltBtn().setEnabled(true);
		ex[0].getPnlBtn().setText("Simple pushups");
		saveExProgress("PUSHUPS","Simple pushups");
		saveExProgress("PUSHUPSBTN",true);
    }
    
    
    private void recordTime()
    {
    	saveExProgress("TIME",getCurrentTime());
    	saveExProgress("DAY",c.get(Calendar.DAY_OF_MONTH));
    	saveExProgress("MONTH",c.get(Calendar.MONTH) + 1);
    	saveExProgress("YEAR",c.get(Calendar.YEAR));
    }
    
    private int getCurrentTime()
    {
    	return c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
    }
    
    private int dayTime()
    {
    	return 60 * 60 * 24 ;
    }
	
    
  
    
    
	//inefficient, but it works.
	private void enablePanelAt(int pos)
	{
		for(int i=0;i < NUMOFEX;i++)
		{
			if (pos == i)
			{
				ex[i].getPnl().setVisibility(View.VISIBLE);
			}
			else
			{
				ex[i].getPnl().setVisibility(View.GONE);
			}
		}
	}
    
    
    private void saveExProgress(String key, String val)
    {
    	edit.putString(key, val);
    	edit.commit();
    }
    
    private void saveExProgress(String key, boolean val)
    {
    	edit.putBoolean(key, val);
    	edit.commit();
    }
    private void saveExProgress(String key, int val)
    {
    	edit.putInt(key, val);
    	edit.commit();
    }
    
	@Override
	public void onBackPressed() 
	{
	    Bundle bundle = new Bundle();
	    bundle.putString("expPoints", String.valueOf(gained_exp));
	    //bundle.putString("tabNo", "1");
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    setResult(RESULT_OK, intent);
	    super.onBackPressed();
	}
}
