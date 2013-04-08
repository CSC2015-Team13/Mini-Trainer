<?php

	if(isset($_POST['email']))
	{
	
		$response = array();
		$email = $_POST['email'];
		
		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		
		$result = mysql_query("SELECT username FROM user WHERE email = '$email'");
		if(mysql_num_rows($result)==1)
		{
			$row = mysql_fetch_assoc($result);
			$username = $row['username'];
			$newExpiry = time() + 86400;
			$newToken = hash("sha256",$newExpiry.$username);
			$newExpiry = date("Y-m-d H:i:s",$newExpiry);
			sendReset($email,$newToken,$username);
			$result = mysql_query("UPDATE user SET token_hash = '$newToken', token_expiry = '$newExpiry' WHERE username = '$username'");
			if($result)
			{
				$response['success'] = 1;
				$response['message'] = "An link to reset your password was sent to your email adress";
				echo json_encode($response);
			}
			else
			{
				$response['success'] = 0;
				$response['message'] = "There was an error issuing your request";
				echo json_encode($response);
			}
			
		}
		else
		{
			$response['success'] = 0;
			$response['message'] = "The email you supplied does not exist!";
			echo json_encode($response);
		}
	}	
	
	function sendReset($email,$newToken,$username) {
		$to = $email;
		$link = 'http://www.labs.callanwhite.co.uk/minitrainer/reset3.php?username='.$username.'&token='.$newToken;
		$subject = "Minitrainer password reset";
		$message = 'A request was received to reset your minitrainer password. Please visit the link below to do so: <br> <a href="'.$link.'">'.$link.'</a>';
		$headers = "From: Minitrainer\r\nReply-To: donotreply";
		$headers .= "\r\nContent-Type: text/html; charset=iso-8859-1";
		@mail($to,$subject,$message,$headers);
	}

?>