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
      Button button_1,button_2,button_3,compEx1,compEx2,compEx3;
      TextView step1;
      LinearLayout panel_1,panel_2,panel_3;
      List<Button> exButtons,compExButtons = new ArrayList<Button>();
      List<LinearLayout> panels = new ArrayList<LinearLayout>();
      int gained_exp;
      int minutes,hours,day,month;
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
        compExButtons = new ArrayList<Button>();
        exButtons = new ArrayList<Button>();
        panels = new ArrayList<LinearLayout>();
        
        gained_exp = 0;
   //been thinking of creating an Exercise class..
        button_1 = (Button)findViewById(R.id.button1);
        button_2 = (Button)findViewById(R.id.button2);       
        button_3 = (Button)findViewById(R.id.button3);
        compEx1 = (Button) findViewById(R.id.complete_ex1);
        compEx2 = (Button) findViewById(R.id.complete_ex2);
        compEx3 = (Button) findViewById(R.id.complete_ex3);
        panel_1 = (LinearLayout)findViewById(R.id.panel1);
        panel_2 = (LinearLayout)findViewById(R.id.panel2);
        panel_3 = (LinearLayout)findViewById(R.id.panel3);     
      //order of adding to the array matters.
        exButtons.add(button_1);
        exButtons.add(button_2);
        exButtons.add(button_3); 
        compExButtons.add(compEx1);
        compExButtons.add(compEx2);
        compExButtons.add(compEx3);
        panels.add(panel_1);
        panels.add(panel_2);
        panels.add(panel_3);
        for (int i=0;i < panels.size();i++)
        {
        	exButtons.get(i).setOnClickListener(this);
        	compExButtons.get(i).setOnClickListener(this);
        	panels.get(i).setVisibility(View.GONE);
        }
        loadExProgress();
    }
    
	public void onClick(View v) 
	{
		int timeStamp;
		
		switch (v.getId()){
			case R.id.button1:		
					if (panel_1.getVisibility() != 0){enablePanelAt(0);}
					else {foldAt(0);}
				break;
			case R.id.button2:
					if (panel_2.getVisibility() != 0){enablePanelAt(1);}
					else {foldAt(1);}
				break;
			case R.id.button3:
					if (panel_3.getVisibility() != 0){enablePanelAt(2);}
					else {foldAt(2);}
			    break;
			case R.id.complete_ex1:
					gained_exp = gained_exp + 15 ;
					compEx1.setEnabled(false);
					foldAt(0);
					exButtons.get(0).setText("Simple pushups (Done)");
					saveExProgress("PUSHUPS","Simple pushups (Done)");
					saveExProgress("PUSHUPSBTN",false);
					recordTime();
					
				break;
			case R.id.complete_ex2:
					gained_exp = gained_exp + 12 ;
					compEx2.setEnabled(false);
					foldAt(1);
					exButtons.get(1).setText(exButtons.get(1).getText().toString() + " (Done)");
				break;
			case R.id.complete_ex3:
					gained_exp = gained_exp + 10 ;
					compEx3.setEnabled(false);
					foldAt(2);
					exButtons.get(2).setText(exButtons.get(2).getText().toString() + " (Done)");
				break;
		}
	}
	
    private void loadExProgress()
    {
    	String exTitle = sp.getString("PUSHUPS","Simple pushups");
    	System.out.println(exTitle);
    	boolean compButtonState = sp.getBoolean("PUSHUPSBTN",true);
    	exButtons.get(0).setText(exTitle);
    	compExButtons.get(0).setEnabled(compButtonState);
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
    					if(getCurrentTime() - t >= 1) // 3 is a test value
    					{
    						makeAvailable();
    					}
    				}
    				else
    				{
    					if(c.get(Calendar.DAY_OF_MONTH) - d == 1)
    					{
    						if (((dayTime() - t) + getCurrentTime()) >= 1) // 3 is a test value
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
						if (((dayTime() - t) + getCurrentTime()) >= 1) // 3 is a test value
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
				if (((dayTime() - t) + getCurrentTime()) >= 1) // 3 is a test value
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
		compEx1.setEnabled(true);
		exButtons.get(0).setText("Simple pushups");
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
    	return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
    }
    
    private int dayTime()
    {
    	return 60 * 24;
    }
	
    
  
    
    
	//inefficient, but it works.
	private void enablePanelAt(int pos)
	{
		for(int i=0;i < panels.size();i++)
		{
			if (pos == i)
			{
				panels.get(i).setVisibility(View.VISIBLE);
			}
			else
			{
				panels.get(i).setVisibility(View.GONE);
			}
		}
	}
	
	private void foldAt(int pos)
	{
			panels.get(pos).setVisibility(View.GONE);
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
