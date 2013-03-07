package com.minitrainer;

import java.util.Calendar;
import java.util.Date;

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
      Date d = new Date();
      
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
    	ex[0] = new Exercise(this,15,R.id.button1,R.id.panel1,R.id.complete_ex1,60000);
    	ex[1] = new Exercise(this,12,R.id.button2,R.id.panel2,R.id.complete_ex2,30000);
    	ex[2] = new Exercise(this,10,R.id.button3,R.id.panel3,R.id.complete_ex3,45000);
    	//add listeners and etc
        for (int i=0;i < NUMOFEX;i++)
        {
        	ex[i].getPnlBtn().setOnClickListener(this);
        	ex[i].getCmpltBtn().setOnClickListener(this);
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
					ex[0].setStateToLocked(this);
				break;
			case R.id.complete_ex2:
					gained_exp = gained_exp + ex[1].getExp() ;
					ex[1].setStateToLocked(this);

				break;
			case R.id.complete_ex3:
					gained_exp = gained_exp + ex[2].getExp() ;
					ex[2].setStateToLocked(this);

				break;
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
    		ex[i].loadState(exTitle,btnState,timestamp,this,d.getTime());
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
			}
			else
			{
				ex[i].getPnl().setVisibility(View.GONE);
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
