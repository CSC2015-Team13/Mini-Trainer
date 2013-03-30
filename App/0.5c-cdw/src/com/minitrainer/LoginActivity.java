package com.minitrainer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class LoginActivity extends Activity {
	
	EditText txtUsername, txtPassword;
	Button btnLogin;
	AlertDialogueManager alert = new AlertDialogueManager();
	SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Initialise the tabbedLayout
		TabHost tabHost = (TabHost) findViewById(R.id.tabHostLogin);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("Login");
		spec1.setContent(R.id.tabLogin);
		spec1.setIndicator("Login");
		
		TabSpec spec2 = tabHost.newTabSpec("Register");
		spec2.setContent(R.id.tabRegister);
		spec2.setIndicator("Register");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
		session = new SessionManager(getApplicationContext());
		
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
		
	}
	
	public void loginUser(View v)
	{
		String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
		String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
		if(username.trim().length()>0 && password.trim().length()>0)
		{
			DBHandlerUser db = new DBHandlerUser(this);
			User user = db.getUser(username);
			db.close();
			if(user.getName().equals(username) && user.getPassword().equals(password))
			{
				session.createLoginSession(user.getName(),user.getForename(),user.getSurname());
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}
			else
			{
				alert.showAlertDialogue(LoginActivity.this, "Login failed", "User/Pass is incorrect, please try again", false);
			}
		}
		else
		{
			alert.showAlertDialogue(LoginActivity.this, "Login failed", "Please enter a username and password", false);
		}

		
		
	}
	
	public void registerUser(View v)
	{
		
		String username, password, password2, forename, surname;
		username = ((EditText) findViewById(R.id.txtRegisterUsername)).getText().toString();
		password = ((EditText) findViewById(R.id.txtRegisterPass1)).getText().toString();
		password2 = ((EditText) findViewById(R.id.txtRegisterPass2)).getText().toString();
		forename = ((EditText) findViewById(R.id.txtRegisterForename)).getText().toString();
		surname = ((EditText) findViewById(R.id.txtRegisterSurname)).getText().toString();;
		
		if(username.trim().length()>0 && password.trim().length()>0 && forename.trim().length()>0 && surname.trim().length()>0 && password2.trim().length()>0)
		{
			if(password.equals(password2))
			{
				new registerUserServer(username.trim(),forename.trim(),surname.trim(),password.trim()).execute();

				//alert.showAlertDialogue(LoginActivity.this, "Registration complete", "Registration was successful, you may now login", false);
			}
			else
			{
				alert.showAlertDialogue(LoginActivity.this, "Registration error", "Please ensure the passwords you provided match", false);
			}
		}
		else
		{
			alert.showAlertDialogue(LoginActivity.this, "Registration error", "Please ensure you have filled all fields", false);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	class registerUserServer extends AsyncTask<String,String,String>
	{
		private String username;
		private String firstname;
		private String surname;
		private String password;
		private boolean complete;
		
		DBHandlerUser db = new DBHandlerUser(LoginActivity.this);
		
		private static final String KEY_USERNAME = "username";
		private static final String KEY_FIRSTNAME = "firstname";
		private static final String KEY_SURNAME = "surname";
		private static final String KEY_PASSWORD = "password";
		private static final String KEY_SUCCESS = "success";
		
		ProgressDialog pDialog;
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_push_register = "http://www.labs.callanwhite.co.uk/minitrainer/push_newuser.php";
		
		public registerUserServer(String u, String f, String s, String p)
		{
			this.username = u;
			this.firstname = f;
			this.surname = s;
			this.password = p;
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Registering with the server. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();			
		}
		
		protected String doInBackground(String... params)
		{
			List<NameValuePair> dataToPush = new ArrayList<NameValuePair>();
			dataToPush.add(new BasicNameValuePair(KEY_USERNAME,username));
			dataToPush.add(new BasicNameValuePair(KEY_FIRSTNAME,firstname));
			dataToPush.add(new BasicNameValuePair(KEY_SURNAME,surname));
			dataToPush.add(new BasicNameValuePair(KEY_PASSWORD,password));
			
			JSONObject json = jsonParser.makeHttpRequest(url_push_register, "POST", dataToPush);
			try {
				int success = json.getInt(KEY_SUCCESS);
				if(success==1)
				{
					complete = true;
				}
				else {
					complete = false;
				}
			}
			catch(JSONException e){e.printStackTrace();}
			return null;
		}
		
		protected void onPostExecute(String result)
		{
			pDialog.dismiss();
			if(complete)
			{
				//register the user locally
				//db.addUser(new User(username,password,forename,surname));
				//db.close();
				alert.showAlertDialogue(LoginActivity.this, "Registration complete", "Registration was successful, you may now login", false);
				Toast.makeText(LoginActivity.this, "Registration successful",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(LoginActivity.this, "Registration failed",Toast.LENGTH_SHORT).show();
			}
		}
	}

}
