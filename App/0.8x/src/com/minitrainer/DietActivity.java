package com.minitrainer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

public class DietActivity extends Activity implements OnClickListener
{
	  int NUMOFDTS = 9;
      Diet dt[] = new Diet[NUMOFDTS];
      final int NOT_VISIBLE_TEXT_AT = 3;
      ScrollView sv;
	  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_diets); 
	    sv = (ScrollView) findViewById(R.id.svDiets);
	    setupDiets();
	}
	
	private void setupDiets()
	{
    	//add in order you want them to appear // make sure it's in the xml!

		int[] d0Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_dinner,R.string.text_snack};
		int[] d0Txt = {R.string.text_d1_breakfast,R.string.text_d1_snack1,R.string.text_d1_lunch,R.string.text_d1_dinner,R.string.text_d1_snack3};
		
		int[] d1Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_preWO,R.string.text_postWO,R.string.text_dinner};
		int[] d1Txt = {R.string.text_d2_breakfast,R.string.text_d2_snack1,R.string.text_d2_lunch,R.string.text_d2_snack2,R.string.text_d2_snack3,R.string.text_d2_dinner};
		
		int[] d2Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d2Txt = {R.string.text_d3_breakfast,R.string.text_d3_snack1,R.string.text_d3_lunch,R.string.text_d3_snack2,R.string.text_d3_dinner,R.string.text_d3_snack3};
		
		int[] d3Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d3Txt = {R.string.text_d4_breakfast,R.string.text_d4_snack1,R.string.text_d4_lunch,R.string.text_d4_snack2,R.string.text_d4_dinner,R.string.text_d4_snack3};
		
		int[] d4Head = {R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d4Txt = {R.string.text_d5_breakfast,R.string.text_d5_snack1,R.string.text_d5_lunch,R.string.text_d5_snack2,R.string.text_d5_dinner,R.string.text_d5_snack3};
		
		int[] d5Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d5Txt = {R.string.text_d6_breakfast,R.string.text_d6_snack1,R.string.text_d6_lunch,R.string.text_d6_snack2,R.string.text_d6_dinner,R.string.text_d6_snack3};
		
		int[] d6Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d6Txt = {R.string.text_d7_breakfast,R.string.text_d7_snack1,R.string.text_d7_lunch,R.string.text_d7_snack2,R.string.text_d7_dinner,R.string.text_d7_snack3};
		
		int[] d7Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner,R.string.text_snack};
		int[] d7Txt = {R.string.text_d8_breakfast,R.string.text_d8_snack1,R.string.text_d8_lunch,R.string.text_d8_snack2,R.string.text_d8_dinner,R.string.text_d8_snack3};
		
		int[] d8Head ={R.string.text_breakfast,R.string.text_snack,R.string.text_lunch,R.string.text_snack,R.string.text_dinner};
		int[] d8Txt = {R.string.text_d9_breakfast,R.string.text_d9_snack1,R.string.text_d9_lunch,R.string.text_d9_snack2,R.string.text_d9_dinner};
		
		dt[0] = new Diet(this,R.id.dButton1,R.id.dPanel1,R.id.arrLeft1,R.id.stepProgress1,R.id.arrRight1,R.id.mTime1,R.id.currMeal1,d0Head,d0Txt);
    	dt[1] = new Diet(this,R.id.dButton2,R.id.dPanel2,R.id.arrLeft2,R.id.stepProgress2,R.id.arrRight2,R.id.mTime2,R.id.currMeal2,d1Head,d1Txt);
    	dt[2] = new Diet(this,R.id.dButton3,R.id.dPanel3,R.id.arrLeft3,R.id.stepProgress3,R.id.arrRight3,R.id.mTime3,R.id.currMeal3,d2Head,d2Txt);
    	dt[3] = new Diet(this,R.id.dButton4,R.id.dPanel4,R.id.arrLeft4,R.id.stepProgress4,R.id.arrRight4,R.id.mTime4,R.id.currMeal4,d3Head,d3Txt);
    	dt[4] = new Diet(this,R.id.dButton5,R.id.dPanel5,R.id.arrLeft5,R.id.stepProgress5,R.id.arrRight5,R.id.mTime5,R.id.currMeal5,d4Head,d4Txt);
    	dt[5] = new Diet(this,R.id.dButton6,R.id.dPanel6,R.id.arrLeft6,R.id.stepProgress6,R.id.arrRight6,R.id.mTime6,R.id.currMeal6,d5Head,d5Txt);
    	dt[6] = new Diet(this,R.id.dButton7,R.id.dPanel7,R.id.arrLeft7,R.id.stepProgress7,R.id.arrRight7,R.id.mTime7,R.id.currMeal7,d6Head,d6Txt);
    	dt[7] = new Diet(this,R.id.dButton8,R.id.dPanel8,R.id.arrLeft8,R.id.stepProgress8,R.id.arrRight8,R.id.mTime8,R.id.currMeal8,d7Head,d7Txt);
    	dt[8] = new Diet(this,R.id.dButton9,R.id.dPanel9,R.id.arrLeft9,R.id.stepProgress9,R.id.arrRight9,R.id.mTime9,R.id.currMeal9,d8Head,d8Txt);
    	
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
		int[] dButtons = {R.id.dButton1,R.id.dButton2,R.id.dButton3,R.id.dButton4,R.id.dButton5,R.id.dButton6,R.id.dButton7,R.id.dButton8,R.id.dButton9};
		int[] stepBack = {R.id.arrLeft1,R.id.arrLeft2,R.id.arrLeft3,R.id.arrLeft4,R.id.arrLeft5,R.id.arrLeft6,R.id.arrLeft7,R.id.arrLeft8,R.id.arrLeft9};
		int[] stepForth = {R.id.arrRight1,R.id.arrRight2,R.id.arrRight3,R.id.arrRight4,R.id.arrRight5,R.id.arrRight6,R.id.arrRight7,R.id.arrRight8,R.id.arrRight9};
		
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
						if (i >= NOT_VISIBLE_TEXT_AT)
						{
							focusOnView(dt[i].getPnlBtn());
						}
						break;
					}
				}
				for(int i=0;i<stepForth.length;i++)
				{
					if (v.getId() == stepForth[i])
					{
						dt[i].stepForth();
						if (i >= NOT_VISIBLE_TEXT_AT)
						{
							focusOnView(dt[i].getPnlBtn());
						}
						break;
					}
				}
			}
		}
	}
	
	private void openPanelAt(int pos)
	{
		for(int i=0;i < NUMOFDTS;i++)
		{
			if (pos == i)
			{
				dt[i].getPnl().setVisibility(View.VISIBLE);
				if (i >= NOT_VISIBLE_TEXT_AT)
				{
					focusOnView(dt[i].getPnlBtn());
				}
				dt[i].getPnlBtn().setBackgroundResource(R.drawable.exercise_open);
			}
			else
			{
				dt[i].getPnl().setVisibility(View.GONE);
				dt[i].getPnlBtn().setBackgroundResource(R.drawable.exercise_closed);
			}
		}
	}
	
	private final void focusOnView(final View v){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, v.getTop());
            }
        });
    }
	      
}
