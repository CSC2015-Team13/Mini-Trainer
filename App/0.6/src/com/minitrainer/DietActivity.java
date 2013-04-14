package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DietActivity extends Activity implements OnClickListener
{
	  int NUMOFDTS = 3;
      Diet dt[] = new Diet[NUMOFDTS];
      SharedPreferences sp;
      Editor edit;
	  
	  
	    /** Called when the activity is first created. */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.copy_activity_diets);
	        
	    setupDiets();
	}
	
	private void setupDiets()
	{
    	//add in order you want them to appear // make sure it's in the xml!
		int [] d0Head = {R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int [] d0Txt = {R.string.text_d1_breakfast,R.string.text_d1_snack1,R.string.text_d1_lunch,R.string.text_d1_snack2,R.string.text_d1_dinner,R.string.text_d1_snack3};
    	int [] d1Head = {R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_preWO,R.string.text_postWO,R.string.text_dinner};
    	int [] d1Txt = {R.string.text_d2_breakfast,R.string.text_d2_snack1,R.string.text_d2_lunch,R.string.text_d2_snack2,R.string.text_d2_snack3,R.string.text_d2_dinner};
    	int [] d2Head = {R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int [] d2Txt = {R.string.text_d3_breakfast,R.string.text_d3_snack1,R.string.text_d3_lunch,R.string.text_d3_snack2,R.string.text_d3_dinner,R.string.text_d1_snack3};
    	
    	dt[0] = new Diet(this,R.id.dButton1,R.id.dPanel1,R.id.arrLeft1,R.id.stepProgress1,R.id.arrRight1,R.id.mTime1,R.id.currMeal1,d0Head,d0Txt);
    	dt[1] = new Diet(this,R.id.dButton2,R.id.dPanel2,R.id.arrLeft2,R.id.stepProgress2,R.id.arrRight2,R.id.mTime2,R.id.currMeal2,d1Head,d1Txt);
    	dt[2] = new Diet(this,R.id.dButton3,R.id.dPanel3,R.id.arrLeft3,R.id.stepProgress3,R.id.arrRight3,R.id.mTime3,R.id.currMeal3,d2Head,d2Txt);
    	
    	//add listeners and etc
        for (int i=0;i < NUMOFDTS;i++)
        {
        	dt[i].getPnlBtn().setOnClickListener(this);
        	dt[i].getBackView().setOnClickListener(this);
        	dt[i].getBackView().setEnabled(false);
        	dt[i].getForthView().setOnClickListener(this);
        }
	}
	
	public void onClick(View v) 
	{
		int[] dButtons = {R.id.dButton1,R.id.dButton2,R.id.dButton3};
		int[] stepBack = {R.id.arrLeft1,R.id.arrLeft2,R.id.arrLeft3};
		int[] stepForth = {R.id.arrRight1,R.id.arrRight2,R.id.arrRight3};
		
		if (v instanceof Button)
		{
			for(int i=0;i<dButtons.length;i++)
			{
				if (v.getId() == dButtons[i])
				{
					if (dt[i].panelPressed()){openPanelAt(i);}
					else {dt[i].foldPan();}
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
						dt[i].stepBack();
						break;
					}
				}
				for(int i=0;i<stepForth.length;i++)
				{
					if (v.getId() == stepForth[i])
					{
						dt[i].stepForth();
						break;
					}
				}
			}
		}
		/*
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
			case R.id.arrRight1:
				dt[0].stepForth();
			break;
		case R.id.arrLeft1:
				dt[0].stepBack();
			break;
		case R.id.arrRight2:
				dt[1].stepForth();
			break;
		case R.id.arrLeft2:
				dt[1].stepBack();
		    break;
		case R.id.arrRight3:
				dt[2].stepForth();
		    break;
		case R.id.arrLeft3:
				dt[2].stepBack();
		    break;
		}
		*/
	}
	
	private void openPanelAt(int pos)
	{
		for(int i=0;i < NUMOFDTS;i++)
		{
			if (pos == i)
			{
				dt[i].getPnl().setVisibility(View.VISIBLE);
				dt[i].getPnlBtn().setBackgroundResource(R.drawable.accordion_item_open);
			}
			else
			{
				dt[i].getPnl().setVisibility(View.GONE);
				dt[i].getPnlBtn().setBackgroundResource(R.drawable.accordion_item_closed);
			}
		}
	}
	      
}
