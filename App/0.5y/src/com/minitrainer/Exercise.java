package com.minitrainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Exercise {
	
	int expReward,pBtnId,panelId,completeId,cooldown,currStep;
	long timestamp;
	Button pb,cmplt;
	TextView title,text;
	View b,f;
	LinearLayout panel;
	String name;
	Date d;
	List<String> steps = new ArrayList<String>();
	
	public Exercise(ExerciseActivity a, int exp, int pbId, int pId, int sBack, int stepNo, int sForth,int textId, int[] allSteps, int cmpltId, int cd)
	{
		pb = (Button) a.findViewById(pbId);
		name = pb.getText().toString();
		panel = (LinearLayout) a.findViewById(pId);
		panel.setVisibility(View.GONE);
		cmplt = (Button) a.findViewById(cmpltId);
		cmplt.setVisibility(View.GONE);
		b = a.findViewById(sBack);
		f = a.findViewById(sForth);
		title = (TextView) a.findViewById(stepNo);
		text = (TextView) a.findViewById(textId);
		currStep = 0;
		for (int i=0; i < allSteps.length;i++)
		{
			steps.add(a.getString(allSteps[i]));
		}
		text.setText(steps.get(0));
		title.setText((currStep +1) + "/" + steps.size());
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
	// might be useful
	public void resetSteps()
	{
		currStep = 0;
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
	
	public void showCompleteBtn()
	{
		if (currStep < steps.size()-1) 
		{
			cmplt.setVisibility(View.GONE);
		}
		else
		{
			cmplt.setVisibility(View.VISIBLE);
		}
	}
	
	public void stepForth()
	{
		System.out.println("CURRENT STEP: " + (currStep + 1));
		if (!(currStep == steps.size() - 1))
		{
			currStep++;
			showCompleteBtn();
			text.setText(steps.get(currStep));
			title.setText((currStep +1) + "/" + steps.size());
			b.setEnabled(true);
		}
		else
		{
			f.setEnabled(false);
		}
	}
	
	public void stepBack()
	{
		System.out.println("CURRENT STEP: " + (currStep +1));
		if (!(currStep == 0))
		{
			currStep--;
			showCompleteBtn();
			text.setText(steps.get(currStep));
			title.setText((currStep +1) + "/" + steps.size());
			f.setEnabled(true);
		}
		else
		{
			b.setEnabled(false);
		}
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
