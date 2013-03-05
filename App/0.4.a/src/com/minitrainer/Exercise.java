package com.minitrainer;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Exercise {
	
	int expReward,pBtnId,panelId,completeId,cooldown;
	Button pb,cmplt;
	LinearLayout panel;
	String name;
	
	public Exercise(Activity a, int exp, int pbId, int pId, int cmpltId, int cd)
	{
		pb = (Button) a.findViewById(pbId);
		name = pb.getText().toString();
		panel = (LinearLayout) a.findViewById(pId);
		cmplt = (Button) a.findViewById(cmpltId);
		expReward = exp;
		pBtnId = pbId;
		panelId = pId;
		completeId = cmpltId;
		cooldown = cd;
	}
	
	public void setStateToLocked()
	{
		cmplt.setEnabled(false);
		foldPan();
		pb.setText(pb.getText().toString() + " (Done)");
	}
	public boolean panelPressed()
	{
		return (panel.getVisibility() != 0);
	}
	
	public void foldPan()
	{
		panel.setVisibility(View.GONE);
	}
	
	public int getCd()
	{
		return cooldown;
	}
	
	public Button getPnlBtn()
	{
		return pb;
	}
	
	public Button getCmpltBtn()
	{
		return cmplt;
	}
	
	public LinearLayout getPnl()
	{
		return panel;
	}
	
	public int getExp()
	{
		return expReward;
	}
	
	public int getpBtnId()
	{
		return pBtnId;
	}
	
	public int getPnlId()
	{
		return panelId;
	}
	
	public int getCmpltId()
	{
		return completeId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNameTag()
	{
		return name.toUpperCase();
	}
	
	public String getBtnTag()
	{
		return (name + "btn").toUpperCase();
	}

}
