package com.minitrainer;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,OnTabChangeListener,OnFocusChangeListener,OnTouchListener {
	
	
	AlertDialogueManager alert = new AlertDialogueManager();
	AlertDialog.Builder alertRecovery;
	Typeface tfGS,tfNV;
	TabEssentials tess;
	SessionManager session;
	SharedPreferences sp;
	Editor edit;
	ImageView iv;
	
	TabHost tabHost;
	Button btnLogin,btnReg; 
	EditText logUsername,logPassword,regUsername,regPassword,regEmail;
	TextView logTrouble;
	CheckBox chb;
	
	TableLayout tl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_new);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		edit = sp.edit();
		alertRecovery = new AlertDialog.Builder(this);
		iv = (ImageView) findViewById(R.id.logo);
		iv.setOnClickListener(this);
		
		tl = (TableLayout) findViewById(R.id.loginLayout);
		tl.setOnTouchListener(this);

		//ini typefaces
        tfGS = Typeface.createFromAsset(getAssets(),"fonts/GillSans.ttc");
        tfNV = Typeface.createFromAsset(getAssets(),"fonts/Nevis.otf");
        
        logTrouble = (TextView) findViewById(R.id.text_AccRecovery);
        logTrouble.setTypeface(tfGS);
		
		//Initialise the tabbedLayout
		tabHost = (TabHost) findViewById(R.id.tabHostLogin);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("Login");
		spec1.setContent(R.id.tabLogin);
		spec1.setIndicator("LOGIN");
		
		TabSpec spec2 = tabHost.newTabSpec("Register");
		spec2.setContent(R.id.tabRegister);
		spec2.setIndicator("REGISTER");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.setOnTabChangedListener(this);
		
		tess = new TabEssentials(tabHost);
		
		session = new SessionManager(getApplicationContext());
		
		logUsername = (EditText) findViewById(R.id.txtUsername);
		logUsername.setTypeface(tfGS);
		logUsername.setOnFocusChangeListener(this);
		logPassword = (EditText) findViewById(R.id.txtPassword);
		logPassword.setTypeface(tfGS);
		
		chb = (CheckBox) findViewById(R.id.checkBox1);
		chb.setChecked(sp.getBoolean("CHECKBOX",false));
		chb.setOnClickListener(this);
		chb.setTypeface(tfGS);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setTypeface(tfGS);
		btnReg = (Button) findViewById(R.id.btnRegister);
		btnReg.setTypeface(tfGS);
		btnLogin.setOnClickListener(this);
		btnReg.setOnClickListener(this);
		
		
		if (chb.isChecked() == true)
		{
			logUsername.setText(sp.getString("USERNAME", ""));
			logPassword.setText(sp.getString("PASSWORD", ""));
			int pos1 = logUsername.length();
			int pos2 = logPassword.length();
			Editable unCursor = logUsername.getText();
			Editable pwCursor = logPassword.getText();
			Selection.setSelection(unCursor, pos1);
			Selection.setSelection(pwCursor, pos2);
		}
		
		regUsername = (EditText) findViewById(R.id.txtRegisterUsername);
		regUsername.setTypeface(tfGS);
		regPassword = (EditText) findViewById(R.id.txtRegisterPass1);
		regPassword.setTypeface(tfGS);
		regEmail = (EditText) findViewById(R.id.txtRegEmailAddress);
		regEmail.setTypeface(tfGS);
		
       // regUsername.setError(null);
       // regPassword.setError(null);
       // regEmail.setError(null);
		
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
		
		logTrouble.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				LayoutInflater li = LayoutInflater.from(LoginActivity.this);
				View promptsView = li.inflate(R.layout.restore_prompt, null);
				alertRecovery.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.txtEmailRecovery);
				
				alertRecovery
					.setCancelable(false)
					.setPositiveButton("Recover details",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
					    	String email = userInput.getText().toString();
					    	new resetPasswordEmail().execute(email);
					    	//Toast bread = Toast.makeText(getApplicationContext(), "Account details were sent to email:\n" + email, Toast.LENGTH_LONG);
					    	//bread.show();	    	
					    }
					  })
					.setNegativeButton("Back",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
				alertRecovery.show();
			}
		});
		
		tess.alignTextInTabs();
		tess.setupTabs("#000000","#ecebeb","#ffffff",17,tabHost.getTabWidget().getChildCount(),tfNV);
	}
	
	@Override
	public void onTabChanged(String tabId) {
		setTabColor(tabHost);
	}
	
	public static void setTabColor(TabHost tabhost) {
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	    {
	        tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ecebeb")); //unselected
	    }
	    tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffff")); // selected
	}
	
	@Override
	public void onClick(View v) 
	{
		boolean emailFlag = true;
		boolean usernameFlag = true;
		boolean passwordFlag = true;
		
		// text checking TBD
		switch(v.getId())
		{
			case R.id.btnLogin :
				loginUser();
				//btnLogin.setClickable(false);
				break;
				
			case R.id.btnRegister :				// TBD - if successfully registers, immediately logs in
				//View focusView = null;		// or switches to the first tab
				//regUsername.setError(null);
				//regPassword.setError(null);
				//regEmail.setError(null);	
				String usernameData = regUsername.getText().toString();
				String passwordData = regPassword.getText().toString();
				String emailData = regEmail.getText().toString();	//example of validity checking
				String errorEmail = "";
				String errorPword = "";
				String errorUname = "";
				String errorMessage = "";
				
				if (!(emailData.contains("@")) || !(emailData.contains(".")) || TextUtils.isEmpty(emailData) || emailData.length() < 4) 
				{
					//regEmail.setError("invalid email");
					if (TextUtils.isEmpty(emailData) || emailData.length() < 4)
					{
						errorEmail += "Email is too short";
					}
					else
					{
						errorEmail += "Email provided is invalid";
					}
					emailFlag = false;

				}
				
				if(TextUtils.isEmpty(passwordData) || passwordData.length() < 4)
				{
					passwordFlag = false;
				}
				
				if (TextUtils.isEmpty(usernameData) || usernameData.length() < 3)
				{

					usernameFlag = false;
				}
				if (usernameFlag && passwordFlag && emailFlag)
				{
					registerUser();
				}
				else
				{
					if (!usernameFlag) errorUname = "Username is too short";
					if (!passwordFlag) errorPword = "Password is too short" ;
					if ((!usernameFlag) && (!passwordFlag) && (!emailFlag))
					{
						errorMessage = errorUname + "\n" + errorPword + "\n" + errorEmail;
					}
					else
					{
						if (!usernameFlag) errorMessage += errorUname + "\n" + errorEmail;
						else 
						{
							if (!passwordFlag)
							{
								errorMessage += errorPword + "\n" + errorEmail;
							}
							else errorMessage = errorEmail;
						}
					}
					Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
					
				}
				break;
				
			case R.id.checkBox1 :
				if (!chb.isChecked())
				{
					edit.clear().commit();
				}
				break;
			case R.id.logo:
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);break;
				      
		}		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		String username = sp.getString("USERNAME", "");
		String tempUsername = logUsername.getText().toString();
		if (v.getId() == R.id.txtUsername)
		{
			
			if (tempUsername.equals("") || tempUsername.length() == 0 )// || (!(username.equals(tempUsername))))
			{
				if (logPassword.getText().toString().equals("") == false)
				{
					logPassword.setText("");
				}
			}
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		try{
			if (v.getId() == R.id.loginLayout)
			{
				//InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(
	            //    android.content.Context.INPUT_METHOD_SERVICE);
				//imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
				
				InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public void loginUser()
	{
		String username = logUsername.getText().toString();
		String password = logPassword.getText().toString();
		if(username.trim().length()>0 && password.trim().length()>0)
		{
			new loginUserServer(username.trim(),password.trim()).execute();
			if (chb.isChecked() == true)
			{
				edit.putString("USERNAME", username);
				edit.putString("PASSWORD", password);
				edit.putBoolean("CHECKBOX", chb.isChecked());
				edit.commit();
			}
		}
		else
		{
			alert.showAlertDialogue(LoginActivity.this, "Login failed", "Please enter a username and password", false);
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkState = cm.getActiveNetworkInfo();
	    return networkState != null;
	}
	
	public void registerUser()
	{
		
		String username, password, email;
		username = regUsername.getText().toString();
		password = regPassword.getText().toString();
		email = regEmail.getText().toString(); // TBD
		//forename = "ratata"; //TBRemoved
		//surname = "matata"; //TBRemoved
		
		if(username.trim().length()>0 && password.trim().length()>0 && email.trim().length()>0)
		{
			//if(password.equals(password2))
			//{
				new registerUserServer(username.trim(),email.trim(),password.trim()).execute();
				//logUsername.setText(username);
				//logPassword.setText(password);
				//tabHost.setCurrentTab(0);

				//alert.showAlertDialogue(LoginActivity.this, "Registration complete", "Registration was successful, you may now login", false);
			//}
			//else
			//{
			//	alert.showAlertDialogue(LoginActivity.this, "Registration error", "Please ensure the passwords you provided match", false);
			//}
		}
		else
		{
			alert.showAlertDialogue(LoginActivity.this, "Registration error", "Please ensure you have filled all fields", false);
		}
		
	}
	
	class loginUserServer extends AsyncTask<String,String,String>
	{
		private String username;
		private String password;
		private boolean complete = false;
		//private String resultFirstname;
		//private String resultSurname;
		private String resultEmail;
		private int resultCode;
		
		ProgressDialog pDialog;
		
		private static final String KEY_USERNAME = "username";
		private static final String KEY_PASSWORD = "password";
		private static final String KEY_SUCCESS = "success";
		//private static final String KEY_RESULT_FIRSTNAME = "firstname";
		//private static final String KEY_RESULT_SURNAME = "surname";
		private static final String KEY_RESULT_EMAIL = "email";
		
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
			//convert plaintext -> sha256(hex)
			password = passwordToHex(password);
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
				if(success==1)
				{
					complete = true;
					//resultFirstname = json.getString(KEY_RESULT_FIRSTNAME);
					//resultSurname = json.getString(KEY_RESULT_SURNAME);
					resultEmail = json.getString(KEY_RESULT_EMAIL);
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
//TO BE REMOVED - REFACTORED BELOW
/*				if(db.userExists(username))
				{
					//they have an account - fastforward
					User user = db.getUser(username);
					db.close();
					session.createLoginSession(user.getName());
					Intent i = new Intent(getApplicationContext(), TabbedActivity.class);
					startActivity(i);
					finish();
					Toast.makeText(LoginActivity.this, "Local account found", Toast.LENGTH_LONG).show();
				}
				else
				{
					//they dont have a local account, create one from query result and supplied params
					db.addUser(new User(username,password,resultEmail));
					User user = db.getUser(username);
					db.close();
					session.createLoginSession(user.getName());
					Intent i = new Intent(getApplicationContext(), TabbedActivity.class);
					startActivity(i);
					finish();
					Toast.makeText(LoginActivity.this, "Local account added",Toast.LENGTH_SHORT).show();
				}*/
				
				if(!db.userExists(username))
				{
					db.addUser(new User(username,password,resultEmail));
					Toast.makeText(LoginActivity.this, "Local account added", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(LoginActivity.this, "Local account found", Toast.LENGTH_SHORT).show();
				}
				User user = db.getUser(username);
				db.close();
				session.createLoginSession(user.getName());
				new pullFromServer().execute();
				Intent i = new Intent(getApplicationContext(), TabbedActivity.class);
				startActivity(i);
				finish();
			}
			else if(resultCode==2)
			{
				Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//at this point they have failed the online check
				//might be a network issue though, lets check if they have a local account
				Toast.makeText(LoginActivity.this, "Possible connection issues,\nchecking for local account",Toast.LENGTH_SHORT).show();
				if(db.userExists(username))
				{
					//they have a local account, lets try and log them in with that
					User user = db.getUser(username);
					db.close();
					if(user.getName().equals(username) && user.getPassword().equals(password))
					{
						//they have a valid local account, log them in
						session.createLoginSession(user.getName());
						Intent i = new Intent(getApplicationContext(), TabbedActivity.class);
						startActivity(i);
						finish();
						Toast.makeText(LoginActivity.this, "Local account valid - no net", Toast.LENGTH_SHORT).show();
					}
					else
					{
						//They have a local account but have the wrong password!
						Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
		//private String firstname;
		//private String surname;
		private String email;
		private String password;
		private boolean complete;
		private int resultCode;
		
		DBHandlerUser db = new DBHandlerUser(LoginActivity.this);
		
		private static final String KEY_USERNAME = "username";
		//private static final String KEY_FIRSTNAME = "firstname";
		//private static final String KEY_SURNAME = "surname";
		private static final String KEY_EMAIL = "email";
		private static final String KEY_PASSWORD = "password";
		private static final String KEY_SUCCESS = "success";
		
		ProgressDialog pDialog;
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_push_register = "http://www.labs.callanwhite.co.uk/minitrainer/push_newuser.php";
		
		public registerUserServer(String u, String e, String p)
		{
			this.username = u;
			//this.firstname = f;
			//this.surname = s;
			this.email = e;
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
			password = passwordToHex(password);
			List<NameValuePair> dataToPush = new ArrayList<NameValuePair>();
			dataToPush.add(new BasicNameValuePair(KEY_USERNAME,username));
			//dataToPush.add(new BasicNameValuePair(KEY_FIRSTNAME,firstname));
			//dataToPush.add(new BasicNameValuePair(KEY_SURNAME,surname));
			dataToPush.add(new BasicNameValuePair(KEY_EMAIL,email));
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
			boolean isConnected = isNetworkAvailable();
			
			pDialog.dismiss();
			if(complete)
			{
				//register the user locally

				if (isConnected)
				{
					db.addUser(new User(username,password,email));
					db.close();
					alert.showAlertDialogue(LoginActivity.this, "Registration complete", "Registration was successful, you may now login", false);
					Toast.makeText(LoginActivity.this, "Registration successful",Toast.LENGTH_SHORT).show();
					logUsername.setText(regUsername.getText().toString());
					logPassword.setText(regPassword.getText().toString());
					tabHost.setCurrentTab(0);
				}
				else
				{
					alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful,\nno connection detected", false);
					Toast.makeText(LoginActivity.this, "Registration failure,\nno connection detected",Toast.LENGTH_SHORT).show();
				}
			}
			else if(resultCode==3){
				alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful due to server error", false);
			}
			else
			{
				if (!isConnected)
				{
					alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful,\nno connection detected", false);
					Toast.makeText(LoginActivity.this, "Registration failure,\nno connection detected",Toast.LENGTH_SHORT).show();
				}
				else
				{
					alert.showAlertDialogue(LoginActivity.this, "Registration failure", "Registration unsuccessful,\nthat username or email address already exists", false);
					Toast.makeText(LoginActivity.this, "Registration failure,\nusername in use",Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	//despite the function name, it actually hashes the string as SHA-256 first, then returns a hex
	public String passwordToHex(String pass)
	{
		try {
			MessageDigest mdigest = MessageDigest.getInstance("SHA-256");
			mdigest.update(pass.getBytes("UTF-8"));
			byte[] digest = mdigest.digest();
			return new String(String.format("%0" + (digest.length * 2) + 'x', new BigInteger(1, digest)));
		}
		catch(NoSuchAlgorithmException e){e.printStackTrace();}
		catch(UnsupportedEncodingException e){e.printStackTrace();}
		return null;
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	*/
	class pullFromServer extends AsyncTask<String,String,Boolean> {
		//private ProgressDialog pDialog;
		private SharedPreferences userState;
		private SharedPreferences sp;
		private static final String KEY_USERNAME = "username";
		private static final String KEY_EXERCISESTOTAL = "exercisesTotal";
		private static final String KEY_EXPERIENCETOTAL = "experienceTotal";
		private static final String KEY_LEVEL = "level";
		private static final String KEY_EMAIL = "email";
		private static final String KEY_PASSWORD = "password";
		private static final String KEY_SUCCESS = "success";
		private static final String KEY_PREF_EXERCISESTOTAL = "EXSCMPLT";
		private static final String KEY_PREF_EXPERIENCETOTAL = "EXPERIENCE";
		private static final String KEY_PREF_LEVEL = "LEVEL";
		private static final String KEY_LAST_UPDATE = "lastUpdate";
		
		private static final String KEY_QUESTIONSANSWERED = "questionsA";
		private static final String KEY_LASTTIMECOMPLETED = "lastExercise";
		
		JSONParser jsonParser = new JSONParser();
		private static final String url_pull_user = "http://www.labs.callanwhite.co.uk/minitrainer/pull_profile.php";
		
		protected void onPreExecute() {
			super.onPreExecute();
			//pDialog = new ProgressDialog(LoginActivity.this);
			//pDialog.setMessage("Updating user profile from server. Please wait");
			//pDialog.setIndeterminate(false);
			//pDialog.setCancelable(true);
			//pDialog.show();
		}
		
		protected Boolean doInBackground(String... params) {
			userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			sp = getApplicationContext().getSharedPreferences(userState.getString("username","username"),0);
			String username = userState.getString("username","username");
			List<NameValuePair> dataToPull = new ArrayList<NameValuePair>();
			dataToPull.add(new BasicNameValuePair(KEY_USERNAME,username));
			
			JSONObject json = jsonParser.makeHttpRequest(url_pull_user, "POST", dataToPull);
			try{
				if(json==null) {
					return Boolean.valueOf(false);
				}
				else {
					if(json.getInt(KEY_SUCCESS)==1)
					{
						long deviceUpdate = sp.getLong(KEY_LAST_UPDATE, 0L);
						long serverUpdate = json.getLong(KEY_LAST_UPDATE);
						System.out.println(deviceUpdate);
						System.out.println(serverUpdate);
						if(deviceUpdate < serverUpdate)
						{
							System.out.println("QUESTIONS ANSWERED "+ json.getString(KEY_QUESTIONSANSWERED));
							Editor edit = sp.edit();
							edit.putString(KEY_PREF_EXERCISESTOTAL,json.getString(KEY_EXERCISESTOTAL));
							edit.putString(KEY_PREF_EXPERIENCETOTAL, json.getString(KEY_EXPERIENCETOTAL));
							edit.putString(KEY_PREF_LEVEL, json.getString(KEY_LEVEL));
							edit.putLong(KEY_LAST_UPDATE, serverUpdate);
							edit.putString("QANSWERED", json.getString(KEY_QUESTIONSANSWERED));
							edit.putString("LASTCMPLT", json.getString(KEY_LASTTIMECOMPLETED));
							edit.commit();
							DBHandlerUser db = new DBHandlerUser(getApplicationContext());
							User user = db.getUser(username);
							user.setEmail(json.getString(KEY_EMAIL));
							user.setPassword(json.getString(KEY_PASSWORD));
							db.updateUser(user);
							db.close();
							
							new pullExerciseCooldowns().execute(); // new
							
						}
						//no need to push the device timestamp if server is outdated, it will happen next time the device syncs
						return Boolean.valueOf(true);
					}
					else{
						return Boolean.valueOf(false);
					}
				}
				
			}
			catch(JSONException e){e.printStackTrace();}
			return Boolean.valueOf(false);
		}
		
		protected void onPostExecute(Boolean result) {
			//pDialog.dismiss();
			if(result.booleanValue()) {
				Toast.makeText(LoginActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(LoginActivity.this, "Profile update was unsuccessful", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class resetPasswordEmail extends AsyncTask<String,String,Boolean> {
		
		private static final String KEY_SUCCESS = "success";
		private static final String KEY_EMAIL = "email";
		private static final String URL_PASSWORD_RESET = "http://www.labs.callanwhite.co.uk/minitrainer/reset.php";
		JSONParser jsonParser = new JSONParser();
		
		public Boolean doInBackground(String... params)	{
			String email = params[0];
			System.out.println(email);
			List<NameValuePair> dataPush = new ArrayList<NameValuePair>();
			dataPush.add(new BasicNameValuePair(KEY_EMAIL,email));
			
			JSONObject json = jsonParser.makeHttpRequest(URL_PASSWORD_RESET, "POST", dataPush);
			
			try{
				if(json==null)
				{
					return Boolean.valueOf(false);
				}
				else
				{
					if(json.getInt(KEY_SUCCESS)==1)
					{
						return Boolean.valueOf(true);

					}
					else
					{
						return Boolean.valueOf(false);
					}
				}
			}
			catch(JSONException e){e.printStackTrace();}
			return Boolean.valueOf(false);
			
		}
		
		public void onPostExecute(Boolean result) {
			if(result.booleanValue())
			{
				Toast.makeText(getApplicationContext(), "Instructions on resetting password sent to email provided", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Failed to resend password instruction", Toast.LENGTH_LONG).show();
			}
				
		}
	}
	
	

    class pullExerciseCooldowns extends AsyncTask<String,String,Boolean>
    {
        private static final String KEY_USERNAME = "username";
        private static final String KEY_SUCCESS = "success";
       
        JSONParser jsonParser = new JSONParser();
        private static final String URL_PULL_COOLDOWNS = "http://www.labs.callanwhite.co.uk/minitrainer/pull_exercises.php";
       
        protected Boolean doInBackground(String... params)
        {
                //get the sharedpref stuff
                SharedPreferences userState = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String username = userState.getString(KEY_USERNAME, KEY_USERNAME);
                sp = getApplicationContext().getSharedPreferences(username, 0);
               
                List<NameValuePair> dataToPull = new ArrayList<NameValuePair>();
                dataToPull.add(new BasicNameValuePair(KEY_USERNAME,username));
               
                JSONObject json = jsonParser.makeHttpRequest(URL_PULL_COOLDOWNS, "POST", dataToPull);
                try {
                        JSONArray arr = json.getJSONArray("ex");
                        Log.w("MINITRAINERTEST", "Len: "+String.valueOf(arr.length()));
                        edit = sp.edit();
                        for(int i=0;i<arr.length();i++)
                        {
                                JSONObject row = arr.getJSONObject(i);
                                //Log.w("MINITRAINERTEST","EXERCISE NAME IS (" + row.getString("exercise") + ") AND TIMESTAMP IS ("+ row.getLong("timestamp")+")");
                                edit.putLong("TS"+row.getString("exercise"),row.getLong("timestamp"));
                                edit.putBoolean(row.getString("exercise")+"BTN", false);
                        }
                        edit.commit();
                }
                catch(JSONException e){e.printStackTrace();}
                return Boolean.valueOf(true);
        }
    }
}
