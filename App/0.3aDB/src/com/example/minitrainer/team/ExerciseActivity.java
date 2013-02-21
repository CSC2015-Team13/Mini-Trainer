package com.example.minitrainer.team;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

 

public class ExerciseActivity extends Activity implements OnClickListener
{

      Button button_1,button_2,button_3,compEx1,compEx2,compEx3;
      TextView step1;
      LinearLayout panel_1,panel_2,panel_3;
      
      int gained_exp;


    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        
        gained_exp = 0;

        button_1 = (Button)findViewById(R.id.button1);
        button_2 = (Button)findViewById(R.id.button2);       
        button_3 = (Button)findViewById(R.id.button3);
        compEx1 = (Button) findViewById(R.id.complete_ex1);
        compEx2 = (Button) findViewById(R.id.complete_ex2);
        compEx3 = (Button) findViewById(R.id.complete_ex3);
       
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);      
        button_3.setOnClickListener(this);
        compEx1.setOnClickListener(this);
        compEx2.setOnClickListener(this);
        compEx3.setOnClickListener(this);

        panel_1 = (LinearLayout)findViewById(R.id.panel1);
        panel_2 = (LinearLayout)findViewById(R.id.panel2);
        panel_3 = (LinearLayout)findViewById(R.id.panel3);

        /*We make both the panels, by default, invisible to user

        and make them appear on button click event in following manner*/

        panel_1.setVisibility(View.GONE);
        panel_2.setVisibility(View.GONE);     
        panel_3.setVisibility(View.GONE);
    }
    
    
    
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.button1:
            	panel_1.setVisibility(View.VISIBLE);
            	panel_2.setVisibility(View.GONE);
            	panel_3.setVisibility(View.GONE);
            break;
		case R.id.button2:
      	  		panel_2.setVisibility(View.VISIBLE);
        		panel_1.setVisibility(View.GONE);
        		panel_3.setVisibility(View.GONE);
        	break;
		case R.id.button3:
      	  		panel_3.setVisibility(View.VISIBLE);
      	  		panel_1.setVisibility(View.GONE);
      	  		panel_2.setVisibility(View.GONE);
			break;
		case R.id.complete_ex1:
				gained_exp = gained_exp + 15 ;
				compEx1.setEnabled(false);
			break;
		case R.id.complete_ex2:
				gained_exp = gained_exp + 12 ;
				compEx2.setEnabled(false);
		break;
		case R.id.complete_ex3:
				gained_exp = gained_exp + 10 ;
				compEx3.setEnabled(false);
		    break;
		}
	}
	
	@Override
	public void onBackPressed() {
	    Bundle bundle = new Bundle();
	    bundle.putString("expPoints", String.valueOf(gained_exp));
	    //bundle.putString("tabNo", "1");
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    setResult(RESULT_OK, intent);
	    super.onBackPressed();
	}
}
