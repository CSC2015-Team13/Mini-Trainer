package com.minitrainer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabEssentials {

	TabHost th;
	TabWidget tw;
	
	public TabEssentials(TabHost tabhost)
	{
		th = tabhost;
		tw = th.getTabWidget();
	}
	
    public void alignTextInTabs()
    {
        //center align text in tabs code
        int tabCount = th.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = th.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
            	if (tabCount == 2)
            	{
            		view.getLayoutParams().height *= 0.8;
            	}
            	if (tabCount == 3)
            	{
            		view.getLayoutParams().height *= 0.9;
            	}
                //  get title text view
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                	
                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
    }
    
	public void setupTabs(String textColor, String unselected, String selected, int textSize, int tabCount, Typeface font)
	{
		for(int i=0;i<th.getTabWidget().getChildCount();i++) 
		{
		    TextView tab = (TextView) th.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
		    tab.setTextColor(Color.parseColor(textColor));
		    th.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor(unselected)); //unselected
	        tab.setTextSize(textSize);
	        tab.setTypeface(font,Typeface.BOLD);
		} 
	    th.getTabWidget().getChildAt(th.getCurrentTab()).setBackgroundColor(Color.parseColor(selected)); // selected
	    addSpace(tabCount);
	}
	
	public void addSpace(int tabCount)
	{
		View currentView;
		
		if (tabCount == 2)
		{
    		currentView = tw.getChildAt(0);
    		LinearLayout.LayoutParams currentLayoutLogin = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutLogin.setMargins(0, 5, 5, 0); // left top right bottom
    		currentView = tw.getChildAt(1);
    		LinearLayout.LayoutParams currentLayoutReg = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutReg.setMargins(0, 5, 0, 0);	// left top right bottom
    		tw.requestLayout();
		}
		if (tabCount == 3)
		{
    		currentView = tw.getChildAt(0);
    		LinearLayout.LayoutParams currentLayoutExDiets = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutExDiets.setMargins(0, 3, 3, 0); // left top right bottom
    		currentView = tw.getChildAt(1);
    		LinearLayout.LayoutParams currentLayoutProf = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutProf.setMargins(0, 3, 3, 0);	// left top right bottom
    		currentView = tw.getChildAt(2);
    		LinearLayout.LayoutParams currentLayoutAct = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutAct.setMargins(0, 3, 0, 0);	// left top right bottom
    		tw.requestLayout();
		}
	}
}
