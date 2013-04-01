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
			new loginUserServer(username.trim(),password.trim()).execute();
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
	
	class loginUserServer extends AsyncTask<String,String,String>
	{
		private String username;
		private String password;
		private boolean complete = false;
		private String resultFirstname;
		private String resultSurname;
		private int resultCode;
		
		ProgressDialog pDialog;
		
		private static final String KEY_USERNAME = "username";
		private static final String KEY_PASSWORD = "password";
		private static final String KEY_SUCCESS = "success";
		private static final String KEY_RESULT_FIRSTNAME = "firstname";
		private static final String KEY_RESULT_SURNAME = "surname";
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_pull_login = "http://www.labs.callanwhite.co.uk/minitrainer/pull_loginuser.php";
		
		public loginUserServer(String u, String p)
		{
			this.username = u;
			this.password = p;
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Logging in");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();				
		}
		
		protected String doInBackground(String... params)
		{
			List<NameValuePair> dataToCheck = new ArrayList<NameValuePair>();
			dataToCheck.add(new BasicNameValuePair(KEY_USERNAME,username));
			dataToCheck.add(new BasicNameValuePair(KEY_PASSWORD,password));
			
			JSONObject json = jsonParser.makeHttpRequest(url_pull_login, "POST", dataToCheck);
			try {
				int success;
				if(json==null)
				{
					success = 0;
				}
				else{
					success = json.getInt(KEY_SUCCESS);
				}
				resultCode = success;
				System.out.println(success);
				if(success==1)
				{
					complete = true;
					resultFirstname = json.getString(KEY_RESULT_FIRSTNAME);
					resultSurname = json.getString(KEY_RESULT_SURNAME);
				}
				else{
					complete = false;
				}
			}
			catch(JSONException e){e.printStackTrace();}
			return null;
		}
		
		protected void onPostExecute(String result)
		{
			pDialog.dismiss();
			DBHandlerUser db = new DBHandlerUser(getApplicationContext());
			if(complete)
			{
				//we know they authenticate with the online database at this point
				//lets ensure they have a local account on the device
				if(db.userExists(username))
				{
					//they have an account - fastforward
					User user = db.getUser(username);
					db.close();
					session.createLoginSession(user.getName(),user.getForename(),user.getSurname());
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
					Toast.makeText(LoginActivity.this, "Local account found", Toast.LENGTH_LONG).show();
				}
				else
				{
					//they dont have a local account, create one from query result and supplied params
					db.addUser(new User(username,password,resultFirstname,resultSurname));
					User user = db.getUser(username);
					db.close();
					session.createLoginSession(user.getName(),user.getForename(),user.getSurname());
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
					Toast.makeText(LoginActivity.this, "Local account added",Toast.LENGTH_SHORT).show();
				}
			}
			else if(resultCode==2)
			{
				Toast.makeText(LoginActivity.this, "Invalid username and password or not found", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//at this point they have failed the online check
				//might be a network issue though, lets check if they have a local account
				Toast.makeText(LoginActivity.this, "Possible connection issues, checking for local account",Toast.LENGTH_SHORT).show();
				if(db.userExists(username))
				{
					//they have a local account, lets try and log them in with that
					User user = db.getUser(username);
					db.close();
					if(user.getName().equals(username) && user.getPassword().equals(password))
					{
						//they have a valid local account, log them in
						session.createLoginSession(user.getName(),user.getForename(),user.getSurname());
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(i);
						finish();
						Toast.makeText(LoginActivity.this, "Local account valid - no net", Toast.LENGTH_SHORT).show();
					}
					else
					{
						//They have a local account but have the wrong password!
						Toast.makeText(LoginActivity.this, "Local account invalid - passuser issue", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(LoginActivity.this, "No local account found...",Toast.LENGTH_SHORT).show();
					//no local accounts - turn them away
				}
			}
		}
		
	}
	
	class registerUserServer extends AsyncTask<String,String,String>
	{
		private String username;
		private String firstname;
		private String surname;
		private String password;
		private boolean complete;
		private int resultCode;
		
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
				int success;
				if(json==null)
				{
					success = 0;
				}
				else {
					success = json.getInt(KEY_SUCCESS);
				}
				resultCode = success;
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
				db.addUser(new User(username,password,firstname,surname));
				db.close();
				alert.showAlertDialogue(LoginActivity.this, "Registration complete", "Registration was successful, you may now login", false);
				Toast.makeText(LoginActivity.this, "Registration successful",Toast.LENGTH_SHORT).show();
			}
			else if(resultCode==3){
				alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful due to server error", false);
			}
			else
			{
				alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful, that username already exists", false);
				Toast.makeText(LoginActivity.this, "Registration failure - username in use",Toast.LENGTH_SHORT).show();
			}
		}
	}

}
