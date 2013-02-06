package com.example.buttontest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class AlertDialogueManager {

	public void showAlertDialogue(Context context, String title, String message, Boolean status)
	{
		AlertDialog alertDialogue = new AlertDialog.Builder(context).create();
		alertDialogue.setTitle(title);
		alertDialogue.setMessage(message);
		
		alertDialogue.setButton(-1,"OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		
		alertDialogue.show();
	}
	
}
