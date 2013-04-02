package com.minitrainer;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Diet {
	
	Button pb;
	LinearLayout panel;
	String name;
	
	public Diet(Activity a, int pbId, int pId)
	{
		pb = (Button) a.findViewById(pbId);
		panel = (LinearLayout) a.findViewById(pId);
		panel.setVisibility(View.GONE);
		name = pb.getText().toString();
	}
	
	public Button getPnlBtn()
	{
		return pb;
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
	}
}
