package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity implements OnClickListener{
	
	final int NUMOFQS = 9;
	final int MAX = 3;
	final int GAIN_SCALE = 8;
	final double LVL_SCALE = 1.3;
	Button a,b,c,d;
	TextView question,qNumber;
	List<Question> qsList = new ArrayList<Question>();
	List<Question> qs = new ArrayList<Question>();
	List<Integer> ind = new ArrayList<Integer>();
	int qCount,currQ, gained_exp,gain,qAnswered;
	int currLevel,qTrack;
	AlertDialog.Builder alert;
	
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		intent = getIntent();
		gained_exp = 0;
		Bundle extras = intent.getExtras();
		currLevel = extras.getInt("level");
		qTrack = extras.getInt("qAnswered");
		qCount = 0;
		gain = (int)(GAIN_SCALE * currLevel * LVL_SCALE);
		qAnswered = qTrack;
		
		a = (Button) findViewById(R.id.answ_A);
		b = (Button) findViewById(R.id.answ_B);
		c = (Button) findViewById(R.id.answ_C);
		d = (Button) findViewById(R.id.answ_D);
		
		question = (TextView) findViewById(R.id.quest);
		qNumber = (TextView) findViewById(R.id.text_questionNumber);
		
		a.setOnClickListener(this);
		b.setOnClickListener(this);
		c.setOnClickListener(this);
		d.setOnClickListener(this);
		alert = new AlertDialog.Builder(this);
		createQuestions();
		pickQuestion();
	}
	
	//randomizes the questions every time.
	private void createQuestions()
	{
		int no;
		Integer i;
		qsList.add(new Question(this,R.string.q1,R.string.q1A,R.string.q1B,R.string.q1C,R.string.q1D,R.string.q1A));
		qsList.add(new Question(this,R.string.q2,R.string.q2A,R.string.q2B,R.string.q2C,R.string.q2D,R.string.q2B));
		qsList.add(new Question(this,R.string.q3,R.string.q3A,R.string.q3B,R.string.q3C,R.string.q3D,R.string.q3A));
		qsList.add(new Question(this,R.string.q4,R.string.q4A,R.string.q4B,R.string.q4C,R.string.q4D,R.string.q4C));
		qsList.add(new Question(this,R.string.q5,R.string.q5A,R.string.q5B,R.string.q5C,R.string.q5D,R.string.q5A));
		qsList.add(new Question(this,R.string.q6,R.string.q6A,R.string.q6B,R.string.q6C,R.string.q6D,R.string.q6D));
		qsList.add(new Question(this,R.string.q7,R.string.q7A,R.string.q7B,R.string.q7C,R.string.q7D,R.string.q7D));
		qsList.add(new Question(this,R.string.q8,R.string.q8A,R.string.q8B,R.string.q8C,R.string.q8D,R.string.q8A));
		qsList.add(new Question(this,R.string.q9,R.string.q9A,R.string.q9B,R.string.q9C,R.string.q9D,R.string.q9B));
		
		while (qsList.size() > 0)
		{
			no = (int) (Math.random() * qsList.size());
			System.out.println("no = " + ((int) (Math.random() * qsList.size()) + " which is " + Math.random() + " * " + qsList.size()));
			qs.add(qsList.get(no));
			qsList.remove(no);
		}
	}
	
	private void pickQuestion()
	{
		if (qs.size()> 0 && qAnswered < MAX)
		{
			qCount++;
			System.out.println("Quiz asked me " + qAnswered + " questions");
			currQ = (int)( Math.random() * qs.size());
			question.setText(qs.get(currQ).getQuestion());
			qNumber.setText("Question # " + qCount);
			a.setText(qs.get(currQ).getA());
			b.setText(qs.get(currQ).getB());
			c.setText(qs.get(currQ).getC());
			d.setText(qs.get(currQ).getD());
			System.out.println(currQ);
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
	
	public void onClick(View v) 
	{
		if (v instanceof Button && qs.size() > 0)
		{
			System.out.println(((Button) v).getText().toString() + " == " + qs.get(currQ).getAnswer());
			if(((Button) v).getText().toString().equals(qs.get(currQ).getAnswer()) )
			{
				Toast.makeText(this, "You got it right!", Toast.LENGTH_SHORT).show();
				gained_exp += gain;
			}
			else
			{
				Toast.makeText(this, "WRONG.", Toast.LENGTH_SHORT).show();
			}
			qs.remove(currQ);
			qAnswered++;
			pickQuestion();
			
		}
		
		//switch (v.getId()){
		//	case R.id.answ_A:
		//			Toast.makeText(this, "You got it right!", Toast.LENGTH_SHORT);
		//		break;
				
		//}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    Bundle bundle = new Bundle();
	    bundle.putInt("expPoints", gained_exp);
	    bundle.putInt("qAnswered", qAnswered);
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    setResult(RESULT_OK, intent);
	    super.onBackPressed();
	    //finish();
	}

}
