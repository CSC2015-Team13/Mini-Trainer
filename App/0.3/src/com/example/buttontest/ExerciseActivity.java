package com.example.buttontest;

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

      Button button_1,button_2,button_3,completeExercise;
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
        step1 = (TextView)findViewById(R.id.text_step1);

        button_1 = (Button)findViewById(R.id.button1);
        button_2 = (Button)findViewById(R.id.button2);       
        button_3 = (Button)findViewById(R.id.button3);
        completeExercise = (Button) findViewById(R.id.complete);
       
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);      
        button_3.setOnClickListener(this);
        completeExercise.setOnClickListener(this);

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
		case R.id.complete:
				gained_exp = gained_exp + 8 ;
				panel_1.setVisibility(View.GONE);
                //Intent intent = new Intent();
                //intent.putExtra("expPoints",String.valueOf(gained_exp));
               //setResult(RESULT_OK, intent);
               // finish();
			break;
		}
	}


/*
    public OnClickListener buttonClickListener = new OnClickListener()
    {
            @Override
            public void onClick(View v)
            {
                  Button ClickedButton = (Button)v;

                  if(ClickedButton.getId()== button1.getId())
                  {
                      panel1.setVisibility(View.VISIBLE);
                      panel2.setVisibility(View.GONE);
                      panel3.setVisibility(View.GONE);
                  }

                  if(ClickedButton.getId()== button2.getId())
                  {
                	  panel2.setVisibility(View.VISIBLE);
                      panel1.setVisibility(View.GONE);
                      panel3.setVisibility(View.GONE);	
                  }
                  
                  if(ClickedButton.getId() == button3.getId())
                  {
                	  panel3.setVisibility(View.VISIBLE);
                	  panel1.setVisibility(View.GONE);
                	  panel2.setVisibility(View.GONE);	  
                  }
                  
                  if(ClickedButton.getId() == completeExercise.getId())
                  {
                	  
                	  gained_exp = gained_exp + 8 ;
                	  panel1.setVisibility(View.GONE);
                      //Intent intent = new Intent();
                     // intent.putExtra("expPoints",String.valueOf(gained_exp));
                     // setResult(RESULT_OK, intent);
                     // finish();
                      
                  }
            }
      };
	*/
}
