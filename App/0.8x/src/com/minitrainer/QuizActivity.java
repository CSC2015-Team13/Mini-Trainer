package com.minitrainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity implements OnClickListener{
	
	final int NUMOFQS = 30;
	final int MAX = 3;
	final int QWORTH = 4;
	final int MAX_LEVEL = 30;
	final int MIN_LEVEL = 1;
	final double LVL_SCALE = 1.25;
	final double ATTEMPT_RATIO = 3.0;
	
	final int WAIT_PICKED = 2000;
	final int WAIT_LASTQ_CORRECT = 1600;
	final int WAIT_LASTQ_WRONG = 3800;
	final int WAIT_WRONG = 1800;
	
	double tempGain;
	Button a,b,c,d;
	TextView question,qNumber;
	List<Question> qsList = new ArrayList<Question>();
	List<Question> qs = new ArrayList<Question>();
	List<Integer> ind = new ArrayList<Integer>();
	List<Button> choices = new ArrayList<Button>();
	int qCount,currQ, gained_exp,gain,qAnswered,currExp,levelCap;
	int currLevel,qTrack,lastLevel,wrong,correct,backTaps;
	
	boolean answerFlag,soundState;
	
	MediaPlayer mpCorrect,mpWrong,mpPicked;

	Typeface tfGS;
	
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
		intent = getIntent();
		gained_exp = 0;
		backTaps = 0;
		answerFlag = true;
		Bundle extras = intent.getExtras();
		soundState = extras.getBoolean("SOUNDSTATE",true);
		currLevel = extras.getInt("LEVEL");
		lastLevel = currLevel;
		if (currLevel < 30)
		{
			qAnswered = extras.getInt("QANSWERED");
		}
		else
		{
			qAnswered = 0;
		}
		currExp = extras.getInt("CURREXP",0);
		levelCap = extras.getInt("EXPCAP",50);
		
		mpCorrect = MediaPlayer.create(getApplicationContext(), R.raw.correct);
		mpWrong = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
		mpPicked = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		try {
			mpCorrect.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mpCorrect.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			mpWrong.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		wrong = 0;
		correct = 0;
		
		System.out.println("WTF : it is " + qTrack);
		qCount = 0;
		//gain = (int)(QWORTH * currLevel * LVL_SCALE);
		//qAnswered = qTrack;
		System.out.println("answered: " + qAnswered);
		
		a = (Button) findViewById(R.id.answ_A); choices.add(a);
		b = (Button) findViewById(R.id.answ_B); choices.add(b);
		c = (Button) findViewById(R.id.answ_C); choices.add(c);
		d = (Button) findViewById(R.id.answ_D); choices.add(d);
		
		setExp(currLevel);
		
		question = (TextView) findViewById(R.id.quest);
		question.setTypeface(tfGS);
		//qNumber = (TextView) findViewById(R.id.text_questionNumber);
		
		for (int i=0;i < choices.size();i++)
		{
			choices.get(i).setOnClickListener(this);
			choices.get(i).setTypeface(tfGS);
		}

		createQuestions();
		pickQuestion();
	}
	
    public void setExp(int level)
    {	
    	tempGain = QWORTH;
		for(int i = 0; i < level;i++)
		{
			tempGain = tempGain * LVL_SCALE;
		}
		System.out.println("Each q is worth" + tempGain);
		gain = (int)tempGain;
    }
	
	// sets up questions depending on the level.
	private void createQuestions()
	{
		int no, qdBound,quBound;
		qsList.add(new Question(this,R.string.q1,R.string.q1A,R.string.q1B,R.string.q1C,R.string.q1D,R.string.q1A));
		qsList.add(new Question(this,R.string.q2,R.string.q2A,R.string.q2B,R.string.q2C,R.string.q2D,R.string.q2C));
		qsList.add(new Question(this,R.string.q3,R.string.q3A,R.string.q3B,R.string.q3C,R.string.q3D,R.string.q3C));
		qsList.add(new Question(this,R.string.q4,R.string.q4A,R.string.q4B,R.string.q4C,R.string.q4D,R.string.q4D));
		qsList.add(new Question(this,R.string.q5,R.string.q5A,R.string.q5B,R.string.q5C,R.string.q5D,R.string.q5A));
		qsList.add(new Question(this,R.string.q6,R.string.q6A,R.string.q6B,R.string.q6C,R.string.q6D,R.string.q6D));
		qsList.add(new Question(this,R.string.q7,R.string.q7A,R.string.q7B,R.string.q7C,R.string.q7D,R.string.q7A));
		qsList.add(new Question(this,R.string.q8,R.string.q8A,R.string.q8B,R.string.q8C,R.string.q8D,R.string.q8B));
		qsList.add(new Question(this,R.string.q9,R.string.q9A,R.string.q9B,R.string.q9C,R.string.q9D,R.string.q9C));
		qsList.add(new Question(this,R.string.q10,R.string.q10A,R.string.q10B,R.string.q10C,R.string.q10D,R.string.q10D));
		qsList.add(new Question(this,R.string.q11,R.string.q11A,R.string.q11B,R.string.q11C,R.string.q11D,R.string.q11D));
		qsList.add(new Question(this,R.string.q12,R.string.q12A,R.string.q12B,R.string.q12C,R.string.q12D,R.string.q12B));
		qsList.add(new Question(this,R.string.q13,R.string.q13A,R.string.q13B,R.string.q13C,R.string.q13D,R.string.q13B));
		qsList.add(new Question(this,R.string.q14,R.string.q14A,R.string.q14B,R.string.q14C,R.string.q14D,R.string.q14B));
		qsList.add(new Question(this,R.string.q15,R.string.q15A,R.string.q15B,R.string.q15C,R.string.q15D,R.string.q15C));
		qsList.add(new Question(this,R.string.q16,R.string.q16A,R.string.q16B,R.string.q16C,R.string.q16D,R.string.q16D));
		qsList.add(new Question(this,R.string.q17,R.string.q17A,R.string.q17B,R.string.q17C,R.string.q17D,R.string.q17A));
		qsList.add(new Question(this,R.string.q18,R.string.q18A,R.string.q18B,R.string.q18C,R.string.q18D,R.string.q18A));
		qsList.add(new Question(this,R.string.q19,R.string.q19A,R.string.q19B,R.string.q19C,R.string.q19D,R.string.q19B));
		qsList.add(new Question(this,R.string.q20,R.string.q20A,R.string.q20B,R.string.q20C,R.string.q20D,R.string.q20C));
		qsList.add(new Question(this,R.string.q21,R.string.q21A,R.string.q21B,R.string.q21C,R.string.q21D,R.string.q21C));
		qsList.add(new Question(this,R.string.q22,R.string.q22A,R.string.q22B,R.string.q22C,R.string.q22D,R.string.q22D));
		qsList.add(new Question(this,R.string.q23,R.string.q23A,R.string.q23B,R.string.q23C,R.string.q23D,R.string.q23A));
		qsList.add(new Question(this,R.string.q24,R.string.q24A,R.string.q24B,R.string.q24C,R.string.q24D,R.string.q24A));
		qsList.add(new Question(this,R.string.q25,R.string.q25A,R.string.q25B,R.string.q25C,R.string.q25D,R.string.q25B));
		qsList.add(new Question(this,R.string.q26,R.string.q26A,R.string.q26B,R.string.q26C,R.string.q26D,R.string.q26C));
		qsList.add(new Question(this,R.string.q27,R.string.q27A,R.string.q27B,R.string.q27C,R.string.q27D,R.string.q27B));
		qsList.add(new Question(this,R.string.q28,R.string.q28A,R.string.q28B,R.string.q28C,R.string.q28D,R.string.q28C));
		qsList.add(new Question(this,R.string.q29,R.string.q29A,R.string.q29B,R.string.q29C,R.string.q29D,R.string.q29D));
		qsList.add(new Question(this,R.string.q30,R.string.q30A,R.string.q30B,R.string.q30C,R.string.q30D,R.string.q30D));
		
		System.out.println("CurrLevel: " + currLevel);

		if (currLevel == MIN_LEVEL)
		{
			qdBound = 0;
			quBound = 3;
		}
		else
		{
			qdBound = currLevel;
			quBound = qdBound + 3;
			if (currLevel == MAX_LEVEL)
			{
				qdBound = currLevel;
				quBound = qdBound + 3;
			}
		}
		
		if (currLevel < MAX_LEVEL)
		{
			for(int i = qdBound;i < quBound; i++) // always the same three questions on a certain level instead of random
			{
				qs.add(qsList.get(i));
			}	
		}
		else
		{
			while (qsList.size() > 0)
			{
				no = (int) (Math.random() * qsList.size());
				//System.out.println("no = " + ((int) (Math.random() * qsList.size()) + " which is " + Math.random() + " * " + qsList.size()));
				qs.add(qsList.get(no));
				qsList.remove(no);
			}
		}
	}
	
	private void pickQuestion()
	{
		System.out.println("Questions answered count: " + qAnswered);
		System.out.println((qs.size() > 0) + " && "  + (qAnswered < MAX) +  " || " + (currLevel == MAX_LEVEL) + " && " + (qs.size() > 0));
		if (((qs.size()> 0 && qAnswered < MAX)) || (currLevel == MAX_LEVEL && qs.size()> 0))
		{
			qCount++;
			System.out.println("qAnswered == " + qAnswered);
			if (currLevel < MAX_LEVEL)
			{
				currQ = qAnswered;
			}
			else
			{
				currQ = (int)( Math.random() * qs.size());
			}

			//if (currLevel < 30)
			//{
				question.setText("Question # " + (qAnswered+1) + "\n" + qs.get(currQ).getQuestion());
			//}
			//else
			//{
			//	question.setText(qs.get(currQ).getQuestion());
			//}
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
    	String result = "";
    	double calc;
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	if (currLevel < 30)
    	{
    		result = "Experience gained: " + gained_exp;
    	}
    	else
    	{
    		calc = (correct/(1.0 *(correct+wrong))) * 100;
    		result = String.format("%.2f",calc) + "%";
    		result = "Correct answers: " + correct + "\nWrong answers: " + wrong + "\nAccuracy: " + result; 
    	}
    	alert.setTitle("Quiz Results")
		.setMessage(result)
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
			try {
				if(soundState)mpPicked.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(((Button) v).getText().toString() + " == " + qs.get(currQ).getAnswer());
			qAnswered++;
			   
		    Handler handler = new Handler(); 
		    Button btn = (Button) v;

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
		 				answerFlag = true;
						try {
							if(soundState)mpCorrect.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
		 				v.setBackgroundResource(R.drawable.button_bg_correct);
		 				if (currLevel < MAX_LEVEL)
		 				{
		 					Toast.makeText(QuizActivity.this, "+" + gain + " experience points", Toast.LENGTH_SHORT).show();
		 					gained_exp += gain;
		 				}
		 				else
		 				{
		 					correct++;
		 				}
		 				Handler handler3 = new Handler();
		 			    handler3.postDelayed(new Runnable() { 
		 			         public void run() { 
		 						//qs.remove(currQ);
		 			        	
		 						if ((currLevel < MAX_LEVEL) && (gained_exp + currExp >= levelCap))
		 						{
		 							currLevel++;
		 							setExp(currLevel);
		 						}
		 						System.out.println("gained: " + gained_exp + "curr: " + currExp + ">=" + levelCap);
		 			        	if (currLevel == MAX_LEVEL)
		 			        	{
		 			        		qs.remove(currQ);
		 			        	}
		 						pickQuestion();
		 			         } 
		 			    }, WAIT_LASTQ_CORRECT); 
					}
					else
					{
						answerFlag = false;
						if (currLevel < MAX_LEVEL)
						{
							gained_exp += (int)(gain / ATTEMPT_RATIO);
							Toast.makeText(QuizActivity.this, "+" + (int)(gain / ATTEMPT_RATIO) + " experience points\n(Could've earned " + gain + ")", Toast.LENGTH_SHORT).show();
						}
						else
						{
							wrong++;
						}
							//v.setBackgroundColor(Color.parseColor("#d93636"));
						try {
							if(soundState)mpWrong.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
						v.setBackgroundResource(R.drawable.button_bg_wrong);
						Handler handler2 = new Handler();
					    handler2.postDelayed(new Runnable() { 
					         public void run() { 
									try {
										if(soundState)mpCorrect.start();
									} catch (Exception e) {
										e.printStackTrace();
									}
					        	 choices.get(findRightAnswer(qs.get(currQ).getAnswer())).setBackgroundResource(R.drawable.button_bg_correct);
					         } //.setBackgroundColor(Color.parseColor("#44ca60")
					    }, WAIT_WRONG); 
					    
					    handler2.postDelayed(new Runnable() { 
					         public void run() { 
								//qs.remove(currQ);
					        	
								if ((currLevel < MAX_LEVEL) && (gained_exp + currExp >= levelCap))
								{
									currLevel++;
									setExp(currLevel);
								}
								System.out.println("gained: " + gained_exp + "curr: " + currExp + ">=" + levelCap);
					        	if (currLevel == MAX_LEVEL)
					        	{
					        		qs.remove(currQ);
					        	}
								pickQuestion();
					         } 
					    }, WAIT_LASTQ_WRONG); 
						
					}
		         } 
		    }, WAIT_PICKED); 
		}
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
		Bundle bundle = new Bundle();
		backTaps++;
		if (lastLevel != currLevel && qAnswered < 3 && currLevel < 30)
		{
			if (backTaps == 1)
			{
				Toast.makeText(this, "If you leave now, you will not be able to finish the quiz later!", Toast.LENGTH_LONG).show();
			}
			if (backTaps == 2)
			{
				bundle.putInt("expPoints", gained_exp);
				bundle.putInt("QANSWERED", qAnswered);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				super.onBackPressed();
			}
		}
		else
		{
			bundle.putInt("expPoints", gained_exp);
			bundle.putInt("QANSWERED", qAnswered);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			if (currLevel == MAX_LEVEL)
			{
				if (backTaps == 1)
				{
					if (correct + wrong !=0) setupAlert();
					else super.onBackPressed();
				}
				else
				{
					super.onBackPressed();
				}
			}
			else
			{
				super.onBackPressed();
			}
		}
	}

}
