package com.example.buttontest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabbedActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	TextView BMI;
	EditText height, weight;
	Button calculateBMI;
	String[] vals = {"Quiz","Test","Questionnaire","Games","Facts","Jokes"};
	String strRes;
	double res = 0;
	double w;
	int h = 0;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        
        height = (EditText) findViewById(R.id.editHeight);
        weight = (EditText) findViewById(R.id.editWeight);
        BMI = (TextView) findViewById(R.id.resBMI);
        calculateBMI = (Button) findViewById(R.id.calcBMI);
        
        ListView lv = (ListView) findViewById(R.id.lvMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vals);
        lv.setAdapter(adapter);
        
        calculateBMI.setOnClickListener(this);
        
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Exercises and Diets");

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Profile");
        spec2.setContent(R.id.tab2);

        TabSpec spec3=tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Activities");
        spec3.setContent(R.id.tab3);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        
        //center align text in tabs code
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
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
    
    public void onClick(View v)
    {
    	h = Integer.parseInt(height.getText().toString());
    	w = Double.parseDouble(weight.getText().toString());
    	
    	if (h > 0 && w > 0)
    	{
    		res = w / (h * 0.01 * h *0.01);
    		strRes = String.format("%.2f", res);
    		BMI.setText(strRes);		
    	}
    	else
    	{
    		if (h <= 0)
    		{
    			BMI.setText("Height is =< 0 ");
    		}
    		else
    		{
    			BMI.setText("Weight is =< 0 ");
    		}
    	}
    }
}
