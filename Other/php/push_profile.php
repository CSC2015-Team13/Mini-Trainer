<?php
	
	$response = array();
	
	if (isset($_POST['id']) && isset($_POST['username']) && isset($_POST['exercisesTotal']) && isset($_POST['experienceTotal']) && isset($_POST['level'])) {
	
		$id = $_POST['id'];
		$username = $_POST['username'];
		$exercisesTotal = $_POST['exercisesTotal'];
		$experienceTotal = $_POST['experienceTotal'];
		$level = $_POST['level'];
		
		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		
		$result = mysql_query("UPDATE user SET 
							exercisesTotal = '$exercisesTotal',
							experienceTotal = '$experienceTotal',
							level = '$level'
							WHERE username = '$username'");
		
		if($result) {
			$response["success"] = 1;
			$response["message"] = "Update successful";
			
			echo json_encode($response);
		}
		else {
			print($username);
			$response["success"] = 0;
			$response["message"] = "failed<br>" . print_r($_POST);
			echo json_encode($response);
		}
	}
	else {
		$response["success"] = 0;
		$response["message"] = print_r($_POST);
		echo json_encode($response);
	}
	
?>	
	