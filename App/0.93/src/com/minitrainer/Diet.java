package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * @date 26/04/2013
 * @author Viktor Movcan
 * 
 * Description: Class which acts as a skeleton for the diet
 */
public class Diet {
	
	Button pb;
	View b,f;
	LinearLayout panel;
	TextView title,header,text;
	String name;
	List<String> txt = new ArrayList<String>();
	List<String> heads = new ArrayList<String>();
	int currStep;
    Typeface tfGS,tfNV;
	
	public Diet(Activity a, int pbId, int pId, int back, int stepNo, int forth, int headId, int textId, int[] headers, int[] texts)
	{
		pb = (Button) a.findViewById(pbId);
		panel = (LinearLayout) a.findViewById(pId);
		panel.setVisibility(View.GONE);
		name = pb.getText().toString();
		b = a.findViewById(back);
		f = a.findViewById(forth);
		title = (TextView) a.findViewById(stepNo);
		header = (TextView) a.findViewById(headId);
		text = (TextView) a.findViewById(textId);
		currStep = 0;
		
		for (int i=0; i < headers.length;i++)
		{
			heads.add(a.getString(headers[i]));
			txt.add(a.getString(texts[i]));
		}
		
		// ini fonts
		tfGS = Typeface.createFromAsset(a.getAssets(),"fonts/GillSans.ttc");
		tfNV = Typeface.createFromAsset(a.getAssets(),"fonts/Nevis.otf");
		
		title.setTypeface(tfNV);
		text.setTypeface(tfGS);
		header.setTypeface(tfGS);
		header.setText(heads.get(0));
		text.setText(txt.get(0));
		title.setText((currStep +1) + "/" + heads.size());
	}
	
	// go step forth
	public void stepForth()
	{
		if (!(currStep == heads.size() - 1))
		{
			currStep++;
			header.setText(heads.get(currStep));
			text.setText(txt.get(currStep));
			title.setText((currStep +1) + "/" + heads.size());
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
			header.setText(heads.get(currStep));
			text.setText(txt.get(currStep));
			title.setText((currStep +1) + "/" + heads.size());
			f.setEnabled(true);
		}
		else
		{
			b.setEnabled(false);
		}
	}
	
	
	//get methods
	public LinearLayout getPnl()
	{
		return panel;
	}
	
	// checks if the panel is open already
	public boolean panelPressed()
	{
		return (panel.getVisibility() != 0);
	}
	
	//fold panels at certain position
	public void foldPan()
	{
		panel.setVisibility(View.GONE);
		pb.setBackgroundResource(R.drawable.exercise_closed);
	}
	
	//get methods
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
}
