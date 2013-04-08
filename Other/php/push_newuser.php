<?php

	$response = array();
	
	if(isset($_POST['username']) && isset($_POST['email']) && isset($_POST['password']))
	{
		$username = $_POST['username'];
		$password = $_POST['password'];
		$email = $_POST['email'];

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		
		$exists = mysql_query("SELECT * FROM user WHERE username = '$username' OR email = '$email' LIMIT 1");
		
		if(mysql_num_rows($exists)==1)
		{
			$response['success'] = 0;
			$response['message'] = "There is already an account with that username and/or password!";
			echo json_encode($response);
		}
		else 
		{
			$insert = mysql_query("INSERT INTO user(username, email, password) VALUES('$username','$email','$password')");
			if($insert)
			{
				$response['success'] = 1;
				$response['message'] = "User account successfully created";
				echo json_encode($response);
			}
			else
			{
				$response['success'] = 3;
				$response['message'] = "Error creating user";
			}
		}
	}
	
	else
	{
		$response['success'] = 0;
		$response['message'] = "Registration failed";
		echo json_encode($response);
	}

?>