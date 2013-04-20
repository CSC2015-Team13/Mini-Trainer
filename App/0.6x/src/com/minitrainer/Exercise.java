package com.minitrainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Exercise {
	
	int expReward,pBtnId,panelId,completeId,cooldown;
	long timestamp;
	Button pb,cmplt;
	TextView title;
	View b,f;
	RelativeLayout panel;
	String name;
	Date d;
	List<String> steps = new ArrayList<String>();
	
	public Exercise(ExerciseActivity a, int exp, int pbId, int pId, int sBack, int stepNo, int sForth, int[] currStep, int cmpltId, int cd)
	{
		pb = (Button) a.findViewById(pbId);
		name = pb.getText().toString();
		panel = (RelativeLayout) a.findViewById(pId);
		panel.setVisibility(View.GONE);
		cmplt = (Button) a.findViewById(cmpltId);
		b = a.findViewById(sBack);
		f = a.findViewById(sForth);
		title = (TextView) a.findViewById(stepNo);
		for (int i=0; i < currStep.length;i++)
		{
			steps.add(a.getString(currStep[i]));
		}
		expReward = exp;
		pBtnId = pbId;
		panelId = pId;
		completeId = cmpltId;
		cooldown = cd;
		timestamp = 0;
		
	}
	public void loadState(ExerciseActivity a, String t, boolean btnSt, long ts, long time)
	{
		pb.setText(t);
		cmplt.setEnabled(btnSt);
		setTimestamp(ts);
		checkCd(a,time);
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
	
	public void setTimestamp(long val)
	{
		timestamp = val;
	}
	
	public boolean panelPressed()
	{
		return (panel.getVisibility() != 0);
	}
	
	public void foldPan()
	{
		panel.setVisibility(View.GONE);
		pb.setBackgroundResource(R.drawable.accordion_item_closed);
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public int getCd()
	{
		return cooldown;
	}
	
	public View getBackView()
	{
		return b;
	}
	
	public View getForthView()
	{
		return f;
	}
	
	public Button getPnlBtn()
	{
		return pb;
	}
	
	public Button getCmpltBtn()
	{
		return cmplt;
	}
	
	public RelativeLayout getPnl()
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
