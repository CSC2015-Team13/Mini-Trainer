package com.minitrainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/* 
 * @date 26/04/2013
 * @author Viktor Movcan
 * 
 * Description: Class which acts as a skeleton for exercise
 */
public class Exercise {
	
	int expReward,pBtnId,panelId,completeId,cooldown,currStep;
	long timestamp;
	Button pb,cmplt;
	TextView title,text;
	View b,f;
	ImageView guide;
	LinearLayout panel;
	String name;
	Date d;
	List<String> steps = new ArrayList<String>();
	List<Integer> imgs = new ArrayList<Integer>();
	Typeface tfGS,tfNV;
	
	public Exercise(ExerciseActivity a, int exp, int pbId, int pId, int sBack, int stepNo, int sForth,int textId, int[] allSteps, int imgId, int [] images, int cmpltId, int cd)
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
		guide = (ImageView) a.findViewById(imgId);
		currStep = 0;
		
		tfGS = Typeface.createFromAsset(a.getAssets(),"fonts/GillSans.ttc");
		tfNV = Typeface.createFromAsset(a.getAssets(),"fonts/Nevis.otf");
		
		title.setTypeface(tfNV);
		text.setTypeface(tfGS);
		cmplt.setTypeface(tfGS);
		pb.setTypeface(tfGS);
		
		
		for (int i=0; i < allSteps.length;i++)
		{
			steps.add(a.getString(allSteps[i]));
		}
		for(int i=0;i < images.length;i++)
		{
			imgs.add(images[i]);
		}
		guide.setImageResource(imgs.get(0));

		text.setText(steps.get(0));
		title.setText((currStep +1) + "/" + steps.size());
		expReward = exp;
		pBtnId = pbId;
		panelId = pId;
		completeId = cmpltId;
		cooldown = cd;
		timestamp = 0;
	}
	
	// locks/unlocks the state of the exercise depending on whether the cooldown has passed
	public void loadState(ExerciseActivity a, boolean btnSt, long ts, long time)// String t
	{
		cmplt.setEnabled(btnSt);
		setTimestamp(ts);
		checkCd(a,time);
	}
	
	// resets the number of steps
	public void resetSteps()
	{
		currStep = 0;
	}
	
	// records the time when the exercise was completed
    public void recordTime(ExerciseActivity a, long val)
    {
    	a.saveExProgress("TS" + getNameTag(),val);
    }
	
    /*
     * checks the cooldown. unlocks the exercise, or keeps it locked 
     * depending on the time passed since the exercise was compelted
     */
    public void checkCd(ExerciseActivity a, long t)
    {
    	if (t - timestamp >= cooldown)
    	{
    		setStateToUnlocked(a);
    		pb.setBackgroundResource(R.drawable.exercise_closed);
    	}
    	else
    	{
    		if (timestamp == 0)
    		{
    			setStateToUnlocked(a);
    		}
    		pb.setBackgroundResource(R.drawable.exercise_done_closed);
    	}
    }
    
    // displays the remaining time for the exercise to unlock
    public boolean getRemTime(ExerciseActivity a, long t)
    {
    	boolean reload = false;
    	long seconds = ((cooldown/1000) -((t/1000) -(timestamp /1000)));
    	long minutes = seconds / 60;   
    	long hours = minutes / 60;
    	if (hours > 0)
    	{
    		if (minutes != 0)
    		{
    			Toast.makeText(a, pb.getText().toString() + " will unlock in\n" + hours + " hour(s) and " + minutes % 60 + " minute(s)", Toast.LENGTH_LONG).show();
    		}													
    		else
    		{
    			Toast.makeText(a, pb.getText().toString() + " will unlock in\n" + hours + " hour(s)", Toast.LENGTH_LONG).show();
    		}
    	}
    	else
    	{
        	if (minutes > 0)
        	{
        		if (seconds != 0)
        		{
        			Toast.makeText(a, pb.getText().toString() + " will unlock in\n" + minutes + " minute(s) and " + seconds % 60 + " second(s)", Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			Toast.makeText(a, pb.getText().toString() + " will unlock in\n" + minutes + " minute(s)", Toast.LENGTH_LONG).show();
        		}
        	}
        	else
        	{
        		if (seconds > 0)
        		{
        			Toast.makeText(a, pb.getText().toString() + " will unlock in\n" + seconds + " second(s)", Toast.LENGTH_SHORT).show();
        		}
        		else
        		{
        			return true;
        		}
        	}
    	}
    	return reload;

    }
	
    // unlock the exercise
    public void setStateToUnlocked(ExerciseActivity a)
    {
		cmplt.setEnabled(true);
		cmplt.setBackgroundResource(R.drawable.complete_ex);
		a.saveExProgress(getBtnTag(),true);
    }
	
    // lock the exercise
	public void setStateToLocked(ExerciseActivity a)
	{
		d = new Date();
		cmplt.setEnabled(false);
		cmplt.setBackgroundResource(R.drawable.complete_ex_done);
		foldPan();
		pb.setBackgroundResource(R.drawable.exercise_done_closed);
		a.saveExProgress(getBtnTag(),false);
		recordTime(a,d.getTime());
	}
	
	// shows the "Complete exercise" button when the user reaches the maximum step
	public void showCompleteBtn()
	{
		if (currStep < steps.size()-1) 
		{
			cmplt.setVisibility(View.GONE);
		}
		else
		{
			if (!(name.equals("Warm-up"))) // warm-up cannot be completed on purpose.
			{
				cmplt.setVisibility(View.VISIBLE);
			}
		}
	}
	
	// go step forth
	public void stepForth()
	{
		if (!(currStep == steps.size() - 1))
		{
			currStep++;
			showCompleteBtn();
			guide.setImageResource(imgs.get(currStep));
			text.setText(steps.get(currStep));
			title.setText((currStep +1) + "/" + steps.size());
			b.setEnabled(true);
		}
		else
		{
			f.setEnabled(false);
		}
	}
	
	// go step back
	public void stepBack()
	{
		if (!(currStep == 0))
		{
			currStep--;
			showCompleteBtn();
			guide.setImageResource(imgs.get(currStep));
			text.setText(steps.get(currStep));
			title.setText((currStep +1) + "/" + steps.size());
			f.setEnabled(true);
		}
		else
		{
			b.setEnabled(false);
		}
	}
	
	//sets timestamp for the exercise
	public void setTimestamp(long val)
	{
		timestamp = val;
	}
	
	// checks if the panel is already open
	public boolean panelPressed()
	{
		return (panel.getVisibility() != 0);
	}
	
	// folds panel of this exercise
	public void foldPan()
	{
		panel.setVisibility(View.GONE);
		if (cmplt.isEnabled())
		{
			pb.setBackgroundResource(R.drawable.exercise_closed);
		}
		else
		{
			pb.setBackgroundResource(R.drawable.exercise_done_closed);
		}
	}
	
	
	//get methods
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
	
	public void setReward(int val)
	{
		expReward = val;
	}

}
