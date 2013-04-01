package com.minitrainer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DietActivity extends Activity implements OnClickListener
{
	  int NUMOFDTS = 3;
      Diet dt[] = new Diet[NUMOFDTS];
      SharedPreferences sp;
      Editor edit;
	  
	  
	    /** Called when the activity is first created. */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_diet);
	        
	    setupDiets();
	}
	
	private void setupDiets()
	{
    	//add in order you want them to appear // make sure it's in the xml!
    	dt[0] = new Diet(this,R.id.dButton1,R.id.dPanel1);
    	dt[1] = new Diet(this,R.id.dButton2,R.id.dPanel2);
    	dt[2] = new Diet(this,R.id.dButton3,R.id.dPanel3);
    	//add listeners and etc
        for (int i=0;i < NUMOFDTS;i++)
        {
        	dt[i].getPnlBtn().setOnClickListener(this);
        }
	}
	
	public void onClick(View v) 
	{
		switch (v.getId()){
			case R.id.dButton1:		
					if (dt[0].panelPressed()){openPanelAt(0);}
					else {dt[0].foldPan();}
				break;
			case R.id.dButton2:
					if (dt[1].panelPressed()){openPanelAt(1);}
					else {dt[1].foldPan();}
				break;
			case R.id.dButton3:
					if (dt[2].panelPressed()){openPanelAt(2);}
					else {dt[2].foldPan();}
			    break;				
		}
	}
	
	private void openPanelAt(int pos)
	{
		for(int i=0;i < NUMOFDTS;i++)
		{
			if (pos == i)
			{
				dt[i].getPnl().setVisibility(View.VISIBLE);
			}
			else
			{
				dt[i].getPnl().setVisibility(View.GONE);
			}
		}
	}
	      
}
