package com.minitrainer;

import java.util.Date;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Exercise {
	
	int expReward,pBtnId,panelId,completeId,cooldown;
	long timestamp;
	Button pb,cmplt;
	LinearLayout panel;
	String name;
	Date d;
	
	public Exercise(ExerciseActivity a, int exp, int pbId, int pId, int cmpltId, int cd)
	{
		pb = (Button) a.findViewById(pbId);
		name = pb.getText().toString();
		panel = (LinearLayout) a.findViewById(pId);
		panel.setVisibility(View.GONE);
		cmplt = (Button) a.findViewById(cmpltId);
		expReward = exp;
		pBtnId = pbId;
		panelId = pId;
		completeId = cmpltId;
		cooldown = cd;
		timestamp = 0;
	}
	public void loadState(String t, boolean btnSt, long ts, ExerciseActivity a, long time)
	{
		pb.setText(t);
		cmplt.setEnabled(btnSt);
		setTimestamp(ts);
		checkCd(a,time);
	}
	
	public void setTimestamp(long val)
	{
		timestamp = val;
	}
	
    public void recordTime(ExerciseActivity a, long val)
    {
    	//timestamp = val ???
    	a.saveExProgress("TS" + getNameTag(),val);
    }
	
    public void checkCd(ExerciseActivity a, long t)
    {
    	System.out.println(t -timestamp + " >= "+ cooldown);
    	if (t - timestamp >= cooldown)
    	{
    		setStateToUnlocked(a);
    	}
    	else
    	{
    		if (timestamp == 0)
    		{
    			setStateToUnlocked(a);
    		}
    	}
    }
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
    public void setStateToUnlocked(ExerciseActivity a)
    {
		cmplt.setEnabled(true);
		pb.setText(name);
		a.saveExProgress(getNameTag(),name);
		a.saveExProgress(getBtnTag(),true);
    }
	
	public void setStateToLocked(ExerciseActivity a)
	{
		d = new Date();
		cmplt.setEnabled(false);
		foldPan();
		pb.setText(pb.getText().toString() + " (Done)");
		
		a.saveExProgress(getNameTag(),getName() + " (Done)");
		a.saveExProgress(getBtnTag(),false);
		recordTime(a,d.getTime());
		System.out.println(getName() + d.getTime());
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
