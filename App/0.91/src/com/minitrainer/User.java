package com.minitrainer;

/*
 * User class to store user information such as login,password,email
 */
public class User {
	int _id;
	String _username;
	String _password;
	String _email;
	
	public User(){}
	
	public User(int id, String username, String password, String email)
	{
		this._id = id;
		this._username = username;
		this._password = password;
		this._email = email;
	}
	
	public User(String username, String password, String email)
	{
		this._username = username;
		this._password = password;
		this._email = email;
	}
	
	public int getId(){return _id;}
	public String getName(){return _username;}
	public String getPassword(){return _password;}
	public String getEmail(){return _email;}
	
	public void setId(int id){_id = id;}
	public void setUsername(String username){_username = username;}
	public void setPassword(String password){_password = password;}
	public void setEmail(String email){_email = email;}
}
