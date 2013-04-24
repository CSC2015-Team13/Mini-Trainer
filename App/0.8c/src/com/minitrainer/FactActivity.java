package com.minitrainer;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

public class FactActivity extends Activity implements OnClickListener{

	final int NUMOFPANELS =  16;
	Fact[] facts = new Fact[NUMOFPANELS];
	Intent intent;
	int currLevel;
	boolean quickShow;
	ScrollView sv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fact);
		sv = (ScrollView) findViewById(R.id.svFacts);
		intent = getIntent();
		Bundle extras = intent.getExtras();
		currLevel = extras.getInt("LEVEL", 1);
		quickShow = extras.getBoolean("QUICKSHOW", false);
		setupFacts();
	}
	
	private void setupFacts()
	{
		final int panOpen = currLevel / 2;
		
		facts[0] = new Fact(this,R.id.fButton1,R.id.fPanel1,R.id.factView1,R.id.factView2);
		facts[1] = new Fact(this,R.id.fButton2,R.id.fPanel2,R.id.factView3,R.id.factView4);
		facts[2] = new Fact(this,R.id.fButton3,R.id.fPanel3,R.id.factView5,R.id.factView6);
		facts[3] = new Fact(this,R.id.fButton4,R.id.fPanel4,R.id.factView7,R.id.factView8);
		facts[4] = new Fact(this,R.id.fButton5,R.id.fPanel5,R.id.factView9,R.id.factView10);
		facts[5] = new Fact(this,R.id.fButton6,R.id.fPanel6,R.id.factView11,R.id.factView12);
		facts[6] = new Fact(this,R.id.fButton7,R.id.fPanel7,R.id.factView13,R.id.factView14);
		facts[7] = new Fact(this,R.id.fButton8,R.id.fPanel8,R.id.factView15,R.id.factView16);
		facts[8] = new Fact(this,R.id.fButton9,R.id.fPanel9,R.id.factView17,R.id.factView18);
		facts[9] = new Fact(this,R.id.fButton10,R.id.fPanel10,R.id.factView19,R.id.factView20);
		facts[10] = new Fact(this,R.id.fButton11,R.id.fPanel11,R.id.factView21,R.id.factView22);
		facts[11] = new Fact(this,R.id.fButton12,R.id.fPanel12,R.id.factView23,R.id.factView24);
		facts[12] = new Fact(this,R.id.fButton13,R.id.fPanel13,R.id.factView25,R.id.factView26);
		facts[13] = new Fact(this,R.id.fButton14,R.id.fPanel14,R.id.factView27,R.id.factView28);
		facts[14] = new Fact(this,R.id.fButton15,R.id.fPanel15,R.id.factView29,R.id.factView30);
		facts[15] = new Fact(this,R.id.fButton16,R.id.fPanel16,R.id.factView31,R.id.factView32);
		
		//open the last available panel
		Handler handler = new Handler();
		if (panOpen != 0) 
		{
			for(int i =0;i < panOpen+1;i++)//panOpen
			{
				facts[i].getPnlBtn().setOnClickListener(this);
				facts[i].getPnlBtn().setBackgroundResource(R.drawable.fact_closed);
			}
			if (panOpen <=3)
			{
				//openPanelAt(panOpen-1);
				openPanelAt(panOpen);
			}
			else
			{
				//openPanelAt(panOpen-1);
				openPanelAt(panOpen);
				if (quickShow)
				{
					handler.postDelayed(new Runnable() { 
						public void run() { 
							focusOnView(facts[panOpen].getPnlBtn());
						} 
					}, 10); 
					if (quickShow && currLevel == 30)
					{
						Toast.makeText(this, "Congratulations!\n You\'ve unlocked all facts!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		else
		{
			facts[0].getPnlBtn().setOnClickListener(this);
			openPanelAt(0);
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

	@Override
	public void onClick(View v) 
	{
		int[] fButtons = {R.id.fButton1,R.id.fButton2,R.id.fButton3,R.id.fButton4,R.id.fButton5,R.id.fButton6,
						  R.id.fButton7,R.id.fButton8,R.id.fButton9,R.id.fButton10,R.id.fButton11,R.id.fButton12,
						  R.id.fButton13,R.id.fButton14,R.id.fButton15,R.id.fButton16};

		if (v instanceof Button)
		{
			for(int i=0;i<fButtons.length;i++)
			{
				if (v.getId() == fButtons[i])
				{
					if (facts[i].panelPressed()){openPanelAt(i);}
					else {facts[i].foldPan();}
					break;
				}
			}
		}
	}

	private void openPanelAt(int pos)
	{
		for(int i=0;i < NUMOFPANELS;i++)
		{
			if (pos == i)
			{
				facts[i].getPnl().setVisibility(View.VISIBLE);
				facts[i].getPnlBtn().setBackgroundResource(R.drawable.fact_open);
			}
			//else
			//{
			//	facts[i].getPnl().setVisibility(View.GONE);
			//	facts[i].getPnlBtn().setBackgroundResource(R.drawable.fact_closed);
			//}
		}
	}

}
