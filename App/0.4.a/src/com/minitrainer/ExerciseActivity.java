package com.minitrainer;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;


public class ExerciseActivity extends Activity implements OnClickListener
{
	  int NUMOFEX = 3;
      int gained_exp;
      Exercise ex[] = new Exercise[NUMOFEX];
      Calendar c;
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
     
        loadExProgress();
    }
    
    private void setupExercises()
    {
    	//add in order you want them to appear // make sure it's in the xml!
    	ex[0] = new Exercise(this,15,R.id.button1,R.id.panel1,R.id.complete_ex1,60);
    	ex[1] = new Exercise(this,12,R.id.button2,R.id.panel2,R.id.complete_ex2,30);
    	ex[2] = new Exercise(this,10,R.id.button3,R.id.panel3,R.id.complete_ex3,45);
    	//add listeners and etc
        for (int i=0;i < NUMOFEX;i++)
        {
        	ex[i].getPnlBtn().setOnClickListener(this);
        	ex[i].getCmpltBtn().setOnClickListener(this);
        	ex[i].getPnl().setVisibility(View.GONE);
        }

    }
    
	public void onClick(View v) 
	{
		switch (v.getId()){
			case R.id.button1:		
					if (ex[0].panelPressed()){openPanelAt(0);}
					else {ex[0].foldPan();}
				break;
			case R.id.button2:
					if (ex[1].panelPressed()){openPanelAt(1);}
					else {ex[1].foldPan();}
				break;
			case R.id.button3:
					if (ex[2].panelPressed()){openPanelAt(2);}
					else {ex[2].foldPan();}
			    break;
			case R.id.complete_ex1:
					gained_exp = gained_exp + ex[0].getExp() ;
					ex[0].setStateToLocked();
					saveExProgress(ex[0].getNameTag(),"Simple pushups (Done)");
					saveExProgress(ex[0].getBtnTag(),false);
					recordTime();
				break;
			case R.id.complete_ex2:
					gained_exp = gained_exp + ex[1].getExp() ;
					ex[1].setStateToLocked();
					saveExProgress(ex[1].getNameTag(),"Crunches (Done)");
					saveExProgress(ex[1].getBtnTag(),false);
				break;
			case R.id.complete_ex3:
					gained_exp = gained_exp + ex[2].getExp() ;
					ex[2].setStateToLocked();
					saveExProgress(ex[2].getNameTag(),"Squats (Done)");
					saveExProgress(ex[2].getBtnTag(),false);
				break;
		}
	}
	
    private void loadExProgress()
    {
    	String exTitle;
    	boolean btnState;
    	
    	for(int i = 0;i<NUMOFEX;i++)
    	{
    		exTitle = sp.getString(ex[i].getNameTag(),ex[i].getName());
    		btnState = sp.getBoolean(ex[i].getBtnTag(),true);
    		ex[i].getPnlBtn().setText(exTitle);
    		ex[i].getCmpltBtn().setEnabled(btnState);
    	}
    	/////////////////////
    	int pTime = sp.getInt("TIME", 0);
    	int pDay = sp.getInt("DAY",0);
    	int pMonth = sp.getInt("MONTH",0);
    	int pYear = sp.getInt("YEAR", 0);
    	System.out.println("year: " + pYear + " month: " + pMonth + " day:" + pDay + " time: " + pTime);
    	/////////////////////
    	checkCooldowns(ex,pYear,pMonth,pDay,pTime);
    }
    	
    private void checkCooldowns(Exercise e[],int y, int m, int d, int t) // int cd, objectname ex
    {
    	if (t != 0 && d != 0 && m != 0 && y != 0)
    	{
    		for(int i = 0;i < NUMOFEX;i++)
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
    						if(getCurrentTime() - t >= e[i].getCd()) // 3 is a test value
    						{
    							setStateToUnlocked(i);
    						}
    					}
    					else
    					{
    						if(c.get(Calendar.DAY_OF_MONTH) - d == 1)
    						{
    							if (((dayTime() - t) + getCurrentTime()) >= e[i].getCd()) // 3 is a test value
    							{
    								setStateToUnlocked(i);
    							}
    						}
    						else
    						{
    							setStateToUnlocked(i);
    						}
    					}
    				}
    				else
    				{
    					if ((c.get(Calendar.MONTH) + 1 - m) == 1)
    					{
    						if (((dayTime() - t) + getCurrentTime()) >= e[i].getCd()) // 3 is a test value
    						{
    							setStateToUnlocked(i);
    						}
    					}
    					else
    					{
    						setStateToUnlocked(i);
    					}
    				}
    			}
    			else
    			{
    				if ((c.get(Calendar.MONTH) == c.get(Calendar.JANUARY)) && m == c.get(Calendar.DECEMBER) 
    						&& (d == 31) && (c.get(Calendar.DAY_OF_MONTH) == 1))
    				{
    					if (((dayTime() - t) + getCurrentTime()) >= e[i].getCd()) // 3 is a test value
    					{
    						setStateToUnlocked(i);
    					}
    				}
    				else
    				{
    					setStateToUnlocked(i);
    				}
    			}
    		}
    	}
    }
    
    private void setStateToUnlocked(int exNo)
    {
		ex[exNo].getCmpltBtn().setEnabled(true);
		ex[exNo].getPnlBtn().setText(ex[exNo].getName());
		saveExProgress(ex[exNo].getNameTag(),ex[exNo].getName());
		saveExProgress(ex[exNo].getBtnTag(),true);
    }
    
    private void recordTime()
    {
    	saveExProgress("TIME",getCurrentTime()); // time in seconds
    	saveExProgress("DAY",c.get(Calendar.DAY_OF_MONTH)); // day
    	saveExProgress("MONTH",c.get(Calendar.MONTH) + 1); // month (+1 because January starts at zero)
    	saveExProgress("YEAR",c.get(Calendar.YEAR)); // year
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
	private void openPanelAt(int pos)
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
