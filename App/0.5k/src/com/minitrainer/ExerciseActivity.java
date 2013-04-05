package com.minitrainer;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class ExerciseActivity extends Activity implements OnClickListener
{
	  int NUMOFEX = 3;
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
		AlertDialog alertDialog = alert.create();

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
    	ex[0] = new Exercise(this,15,R.id.exButton1,R.id.exPanel1,R.id.complete_ex1,6000);
    	ex[1] = new Exercise(this,12,R.id.exButton2,R.id.exPanel2,R.id.complete_ex2,3000);
    	ex[2] = new Exercise(this,10,R.id.exButton3,R.id.exPanel3,R.id.complete_ex3,4500);
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
			case R.id.complete_ex1:
					setupAlert(0);
				break;
			case R.id.complete_ex2:
					setupAlert(1);

				break;
			case R.id.complete_ex3:
					setupAlert(2);
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
