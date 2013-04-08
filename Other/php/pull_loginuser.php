<?php

	$response = array();
	
	if(isset($_POST['username']) && isset($_POST['password']))
	{
		$username = $_POST['username'];
		$password = $_POST['password'];
		
		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		
		$result = mysql_query("SELECT * FROM user WHERE username = '$username' AND password = '$password'");
		
		if(mysql_num_rows($result)==1)
		{
			$row = mysql_fetch_assoc($result);
			$response['success'] = 1;
			$response['email'] = $row['email'];
			$response['message'] = "Login successful";
			echo json_encode($response);
		}
		else
		{
			$response['success'] = 2;
			$response['message'] = "Login details incorrect";
			echo json_encode($response);
		}
	}
	else
	{
		$response['success'] = 0;
		$response['message'] = "Login error - fields not sent";
		echo json_encode($response);
	}
?>