package com.minitrainer;

public class User {
	int _id;
	String _username;
	String _password;
	String _forename;
	String _surname;
	
	public User(){}
	
	public User(int id, String username, String password, String forename, String surname)
	{
		this._id = id;
		this._username = username;
		this._password = password;
		this._forename = forename;
		this._surname = surname;
	}
	
	public User(String username, String password, String forename, String surname)
	{
		this._username = username;
		this._password = password;
		this._forename = forename;
		this._surname = surname;
	}
	
	public int getId(){return _id;}
	public String getName(){return _username;}
	public String getPassword(){return _password;}
	public String getForename(){return _forename;}
	public String getSurname(){return _surname;}
	
	public void setId(int id){_id = id;}
	public void setUsername(String username){_username = username;}
	public void setPassword(String password){_password = password;}
	public void setForename(String forename){_forename = forename;}
	public void setSurname(String surname){_surname = surname;}
}
