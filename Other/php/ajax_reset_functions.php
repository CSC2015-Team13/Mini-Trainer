<?php

	$username = ($_GET['username']) ? $_GET['username'] : $_POST['username'];
	$token = ($_GET['token']) ? $_GET['token'] : $_POST['token'];
	$pass1 = ($_GET['pass1']) ? $_GET['pass1'] : $_POST['pass1'];
	$pass2 = ($_GET['pass2']) ? $_GET['pass2'] : $_POST['pass2'];
	
	if($_POST) $post=1;

	$success = 1;
	$failTokenExpiry = "Token expired - Please request a new password";
	$failNotFound = "Token not found - Please request a new password";
	$failPassword = "The passwords entered do not match";
	$failSQL = "There was an error with the database, please try again later";
	


	if($pass1==$pass2)
	{
		//Do update
		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		$result = mysql_query("SELECT token_expiry FROM user WHERE username = '$username' AND token_hash = '$token'");
		if(mysql_num_rows($result)==1)
		{
			$row = mysql_fetch_assoc($result);
			$unix = strtotime($row['token_expiry']);
			if($unix>time())
			{
				$cur = time();
				$password = hash("sha256",$pass1);
				$result = mysql_query("UPDATE user SET password = '$password', token_expiry = '$cur', token_hash = 0 WHERE username = '$username'");
				if($result)
				{
					echo $success;
				}
				else
				{
					echo $failSQL;
				}
			}
			else
			{
				//token expired
				echo $failTokenExpiry;
				echo $token;
			}
		}	
		else
		{
			//No token or incorrect user
			echo $failNotFound;
			echo $token;
		}

	}
	else
	{
		//Errors encountered
		echo $failPassword;
	}

?>