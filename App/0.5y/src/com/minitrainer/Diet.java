package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Diet {
	
	Button pb;
	View b,f;
	LinearLayout panel;
	TextView title,header,text;
	String name;
	List<String> txt = new ArrayList<String>();
	List<String> heads = new ArrayList<String>();
	int currStep;
	
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
		
		header.setText(heads.get(0));
		text.setText(txt.get(0));
		title.setText((currStep +1) + "/" + heads.size());
	}
	
	public Button getPnlBtn()
	{
		return pb;
	}
	
	public void stepForth()
	{
		System.out.println("CURRENT STEP: " + (currStep + 1));
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
	
	public void stepBack()
	{
		System.out.println("CURRENT STEP: " + (currStep +1));
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
	
	public LinearLayout getPnl()
	{
		return panel;
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
	
	public View getBackView()
	{
		return b;
	}
	
	public View getForthView()
	{
		return f;
	}
}
