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

/*
 * Class which changes the view of the tabs e.g. colours/spacing/text align/text size
 */
public class TabEssentials {

	TabHost th;
	TabWidget tw;
	
	public TabEssentials(TabHost tabhost)
	{
		th = tabhost;
		tw = th.getTabWidget();
	}
	
	/*
	 * Centers text in tabs
	 */
    public void alignTextInTabs()
    {
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
                //  get title of the tab as a textview
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                    // center and wrap text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    ((TextView) textView).setSingleLine(false);
                    // set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
    }
    
    /*
     * Sets up default colours and text size for tabs
     */
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
	
	/*
	 * Adds space between tabs
	 */
	public void addSpace(int tabCount)
	{
		View currentView;
		
		if (tabCount == 2) // for login
		{
    		currentView = tw.getChildAt(0);
    		LinearLayout.LayoutParams currentLayoutLogin = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutLogin.setMargins(0, 5, 5, 0); 
    		currentView = tw.getChildAt(1);
    		LinearLayout.LayoutParams currentLayoutReg = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutReg.setMargins(0, 5, 0, 0);	
    		tw.requestLayout();
		}
		if (tabCount == 3) // for main
		{
    		currentView = tw.getChildAt(0);
    		LinearLayout.LayoutParams currentLayoutExDiets = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutExDiets.setMargins(0, 3, 3, 0); 
    		currentView = tw.getChildAt(1);
    		LinearLayout.LayoutParams currentLayoutProf = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutProf.setMargins(0, 3, 3, 0);
    		currentView = tw.getChildAt(2);
    		LinearLayout.LayoutParams currentLayoutAct = (LinearLayout.LayoutParams) currentView.getLayoutParams();
    		currentLayoutAct.setMargins(0, 3, 0, 0);
    		tw.requestLayout();
		}
	}
}
