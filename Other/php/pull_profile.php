<?php

	$response = array();

	if(isset($_POST['username'])) {
		$username = $_POST['username'];

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();

		$result = mysql_query("SELECT * FROM user WHERE username = '$username'");
		if(mysql_num_rows($result)==1)
		{
			$row = mysql_fetch_assoc($result);
			$response['success'] = 1;
			$response['exercisesTotal'] = $row['exercisesTotal'];
			$response['experienceTotal'] = $row['experienceTotal'];
			$response['level'] = $row['level'];
			$response['email'] = $row['email'];
			$response['password'] = $row['password'];
			$response['lastUpdate'] = strtotime($row['lastUpdate']);
			$response['message'] = "Update successful";
			echo json_encode($response);
		}
		else
		{
			$response['success'] = 0;
			$response['message'] = "Update unsuccessful - mysql error";
			echo json_encode($response);
		}
	}
	else
	{
		$response['success'] = 0;
		$response['message'] = "Update unsuccessful - no params";
		echo json_encode($response);
	}

?>