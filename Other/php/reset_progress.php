<?php

	if(isset($_POST['username']) && isset($_POST['reset']) && $_POST['reset'] == 1)
	{

		require_once __DIR__ . '/db_connect.php';
		$db =  new DB_CONNECT();

		$username = $_POST['username'];

		$id = mysql_fetch_assoc(mysql_query("SELECT id FROM user WHERE username = '$username'"));
		$id = $id['id'];
		echo $id;
		echo $username;

		$result = mysql_query("UPDATE user SET exercisesTotal=0, experienceTotal=0 WHERE id='$id'");
		$result2 = mysql_query("DELETE FROM exercises_complete WHERE id='$id'");

		if($result && $result2)
		{
			echo "Success";
		}
		else
		{
			echo "failed";
		}


	}
	else
	{
		echo "You have reached this page by accident";
	}
?>