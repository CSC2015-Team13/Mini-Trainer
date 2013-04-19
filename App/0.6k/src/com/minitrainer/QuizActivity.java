package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity implements OnClickListener{
	
	final int NUMOFQS = 9;
	final int MAX = 3;
	final int QWORTH = 4;
	final double LVL_SCALE = 1.25;
	double tempGain;
	Button a,b,c,d;
	TextView question,qNumber;
	List<Question> qsList = new ArrayList<Question>();
	List<Question> qs = new ArrayList<Question>();
	List<Integer> ind = new ArrayList<Integer>();
	List<Button> choices = new ArrayList<Button>();
	int qCount,currQ, gained_exp,gain,qAnswered;
	int currLevel,qTrack;
	AlertDialog.Builder alert;
	Typeface tfGS;
	
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		intent = getIntent();
		gained_exp = 0;
		Bundle extras = intent.getExtras();
		currLevel = extras.getInt("LEVEL");
		qTrack = extras.getInt("QANSWERED"); 
		System.out.println("WTF : it is " + qTrack);
		qCount = 0;
		//gain = (int)(QWORTH * currLevel * LVL_SCALE);
		qAnswered = qTrack;
		System.out.println("answered: " + qAnswered);
		
		tempGain = QWORTH;
		for(int i = 0; i < currLevel;i++)
		{
			tempGain = tempGain * LVL_SCALE;
		}
		gain = (int)tempGain;
		
		a = (Button) findViewById(R.id.answ_A); choices.add(a);
		b = (Button) findViewById(R.id.answ_B); choices.add(b);
		c = (Button) findViewById(R.id.answ_C); choices.add(c);
		d = (Button) findViewById(R.id.answ_D); choices.add(d);
		
		
		question = (TextView) findViewById(R.id.quest);
		question.setTypeface(tfGS);
		//qNumber = (TextView) findViewById(R.id.text_questionNumber);
		
		for (int i=0;i < choices.size();i++)
		{
			choices.get(i).setOnClickListener(this);
			choices.get(i).setTypeface(tfGS);
		}

		alert = new AlertDialog.Builder(this);
		createQuestions();
		pickQuestion();
	}
	
	//randomizes the questions every time.
	private void createQuestions()
	{
		int no, qdBound,quBound;
		qsList.add(new Question(this,R.string.q1,R.string.q1A,R.string.q1B,R.string.q1C,R.string.q1D,R.string.q1A));
		qsList.add(new Question(this,R.string.q2,R.string.q2A,R.string.q2B,R.string.q2C,R.string.q2D,R.string.q2D));
		qsList.add(new Question(this,R.string.q3,R.string.q3A,R.string.q3B,R.string.q3C,R.string.q3D,R.string.q3A));
		qsList.add(new Question(this,R.string.q4,R.string.q4A,R.string.q4B,R.string.q4C,R.string.q4D,R.string.q4C));
		qsList.add(new Question(this,R.string.q5,R.string.q5A,R.string.q5B,R.string.q5C,R.string.q5D,R.string.q5A));
		qsList.add(new Question(this,R.string.q6,R.string.q6A,R.string.q6B,R.string.q6C,R.string.q6D,R.string.q6D));
		qsList.add(new Question(this,R.string.q7,R.string.q7A,R.string.q7B,R.string.q7C,R.string.q7D,R.string.q7D));
		qsList.add(new Question(this,R.string.q8,R.string.q8A,R.string.q8B,R.string.q8C,R.string.q8D,R.string.q8A));
		qsList.add(new Question(this,R.string.q9,R.string.q9A,R.string.q9B,R.string.q9C,R.string.q9D,R.string.q9B));
		
		// level x / 2 = 2  2 * 3 - 1
		if (qAnswered == 0)
		{
			qdBound = ((currLevel /2) * 3) - 3;
			quBound = qdBound + 3;
			System.out.println("Lower: " + qdBound +"; Upper: " + quBound);
		}
		else
		{
			qdBound = ((currLevel /2) * 3) - 3 ;// + qAnswered
			quBound = qdBound + 3; //- qAnswered;
			System.out.println("Lower: " + qdBound +"; Upper: " + quBound);
		}
		
		
		for(int i = qdBound;i < quBound; i++) // always the same three questions on a certain level instead of random
		{
			qs.add(qsList.get(i));
		}
		//System.out.println("arraylength: " + qs.size());
		/*
		while (qsList.size() > 0)
		{
			no = (int) (Math.random() * qsList.size());
			System.out.println("no = " + ((int) (Math.random() * qsList.size()) + " which is " + Math.random() + " * " + qsList.size()));
			qs.add(qsList.get(no));
			qsList.remove(no);
		}*/
	}
	
	private void pickQuestion()
	{
		System.out.println("Questions answered count: " + qAnswered);
		if (qs.size()> 0 && qAnswered < MAX)
		{
			qCount++;
			System.out.println("qAnswered == " + qAnswered);
			//currQ = (int)( Math.random() * qs.size());
			//if (qAnswered == 3)
			//{
			//	currQ = qAnswered-1;
			//}
			//else
			//{
				for(int i = 0; i < qs.size();i++)
				{
					System.out.println(i+")" + qs.get(i).getQuestion());
				}
				currQ = qAnswered;
			//}
			question.setText("Question # " + (qAnswered+1) + "\n" + qs.get(currQ).getQuestion());
			//qNumber.setText("Question # " + (qAnswered + 1));
			a.setText(qs.get(currQ).getA());
			b.setText(qs.get(currQ).getB());
			c.setText(qs.get(currQ).getC());
			d.setText(qs.get(currQ).getD());
			for(int i=0;i<choices.size();i++)
			{
				choices.get(i).setBackgroundResource(R.drawable.button_bg_choice);
				choices.get(i).setClickable(true);
			}
			System.out.println("current question: " +currQ);
		}	
		else
		{
			setupAlert();
		}
	}
	
    private void setupAlert()
    {
		alert.setTitle("Quiz Results")
		.setMessage("Experience got: " + gained_exp )
		.setCancelable(false).setPositiveButton("Finish",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				onBackPressed();
			}
		  });
		alert.show();
    }
	
	public void onClick(final View v) 
	{
		if (v instanceof Button && qs.size() > 0)
		{
			System.out.println(((Button) v).getText().toString() + " == " + qs.get(currQ).getAnswer());
			qAnswered++;
			   
			    // SLEEP 4 SECONDS HERE ...
			    Handler handler = new Handler(); 
			    Button btn = (Button) v;
			    //v.setBackgroundColor(Color.parseColor("#f0d099"));
			    v.setBackgroundResource(R.drawable.button_bg_picked);
			    for(int i = 0;i < choices.size(); i++)
			    {
			    	choices.get(i).setClickable(false);
			    }

			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	
			 			if(((Button) v).getText().toString().equals(qs.get(currQ).getAnswer()) )
						{
							//v.setBackgroundColor(Color.parseColor("#44ca60"));
			 				v.setBackgroundResource(R.drawable.button_bg_correct);
							Toast.makeText(QuizActivity.this, "+" + gain + " experience points", Toast.LENGTH_SHORT).show();
							gained_exp += gain;
						}
						else
						{
							//v.setBackgroundColor(Color.parseColor("#d93636"));
							v.setBackgroundResource(R.drawable.button_bg_wrong);
							Handler handler2 = new Handler();
						    handler2.postDelayed(new Runnable() { 
						         public void run() { 
						        	 choices.get(findRightAnswer(qs.get(currQ).getAnswer())).setBackgroundResource(R.drawable.button_bg_correct);
						         } //.setBackgroundColor(Color.parseColor("#44ca60")
						    }, 1800); 
							
						}
			         } 
			    }, 2000); 
			    
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
						//qs.remove(currQ);
			        	pickQuestion();
			         } 
			    }, 5500); 
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
	}
	
	private int findRightAnswer(String answer)
	{
		int i = 0;
		while (i < choices.size())
		{
			if(answer.equals(choices.get(i).getText().toString()))
			{
				System.out.println(" FOUND: " +i);
				return i;
			}
			i++;
		}
		return -1;
	}
	
	@Override
	public void onBackPressed() 
	{
		Toast.makeText(getBaseContext(), "back button pressed", Toast.LENGTH_SHORT).show(); 
	    Bundle bundle = new Bundle();
	    bundle.putInt("expPoints", gained_exp);
	    bundle.putInt("QANSWERED", qAnswered);
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    setResult(RESULT_OK, intent);
	    super.onBackPressed();
	    //finish();
	}

}
