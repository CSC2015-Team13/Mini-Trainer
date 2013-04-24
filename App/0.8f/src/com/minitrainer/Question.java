package com.minitrainer;

import android.app.Activity;

public class Question {
	
	String qst,A,B,C,D,answer;
	
	public Question(QuizActivity a,int q,int As,int Bs,int Cs, int Ds, int ans)
	{
		qst = a.getString(q);
		A = a.getString(As);
		B = a.getString(Bs);
		C = a.getString(Cs);
		D = a.getString(Ds);
		answer = a.getString(ans);
	}
	
	public String getAnswer()
	{
		return answer;
	}
	
	public String getQuestion()
	{
		return qst;
	}
	
	public String getA()
	{
		return A;
	}
	
	public String getB()
	{
		return B;
	}
	
	public String getC()
	{
		return C;
	}
	
	public String getD()
	{
		return D;
	}

}
