<?php

require_once 'User.php';
require_once 'DatabaseHandler.php';

class LoginUser
{
	private $db;
	private $user;

	public function __construct()
	{
		$this->db = DatabaseHandler::getInstance();
	}

	// Kör kontroller och loggar in användaren om allting är OK.
	// Returnar [false, egenfelmeddelandekod] om det inte lyckas
	public function userLogin()
	{
		if ($this->validInput() == false)
		{
			return [false, 1];
		}
		else
		{
			if ($this->login() == false)
			{
				return [false, 2];
			}
			else
			{
				return [true];
			}
		}
	}

	private function validInput()
	{
		if (!isset($_POST['username']) || !isset($_POST['password']))
		{
			return false;
		}
		else
		{
			$this->user = new User(trim($_POST["username"]), trim($_POST["password"]));
			return true;
		}
	}

	private function login()
	{
		$stmt = $this->db->dbh->prepare("SELECT * FROM users WHERE username = ? AND password = ?");
		$stmt->bindValue(1, $this->user->getName());
		$stmt->bindValue(2, $this->user->getPassword());
		$stmt->execute();

		$data = $stmt->fetchAll();

		if (count($data) == 1)
		{
			$this->user->setUserId($data[0]["user_id"]);
			$_SESSION['user'] = $this->user;

			return true;
		}
		else
		{
			return false;
		}
	}
}