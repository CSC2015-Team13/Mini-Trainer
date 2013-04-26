package com.minitrainer;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * @date 26/04/2013
 * @author Viktor Movcan
 * 
 * Description: Class which is used as a skeleton for fact
 */
public class Fact {

	Button pb;
	View b,f;
	LinearLayout panel;
	TextView fact1,fact2;
	Typeface tfGS;
	
	public Fact(FactActivity a, int pbId,int pId,int tvId1,int tvId2)
	{
		pb = (Button) a.findViewById(pbId);
		pb.setBackgroundResource(R.drawable.fact_locked);
		panel = (LinearLayout) a.findViewById(pId);
		
		fact1 = (TextView) a.findViewById(tvId1);
		fact2 = (TextView) a.findViewById(tvId2);
		
		panel.setVisibility(View.GONE);
		
		tfGS = Typeface.createFromAsset(a.getAssets(),"fonts/GillSans.ttc");
		
		pb.setTypeface(tfGS);
		fact1.setTypeface(tfGS);
		fact2.setTypeface(tfGS);
	}
	
	//checks if the panel is already open
	public boolean panelPressed()
	{
		return (panel.getVisibility() != 0);
	}
	
	//folds panel if it is already opens
	public void foldPan()
	{
		panel.setVisibility(View.GONE);
		pb.setBackgroundResource(R.drawable.fact_closed);
	}
	
	// get methods
	public Button getPnlBtn()
	{
		return pb;
	}
	
	public LinearLayout getPnl()
	{
		return panel;
	}
}
