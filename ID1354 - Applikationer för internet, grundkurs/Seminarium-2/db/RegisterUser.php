<?php
require_once 'User.php';
require_once 'DatabaseHandler.php';

class RegisterUser
{
	private $db;
	private $user;

	public function __construct()
	{
		$this->db = DatabaseHandler::getInstance();
	}

	// Kör kontroller och registrerar användaren om allting är OK.
	// Returnar [false, egenfelmeddelandekod] om det inte lyckas
	public function registerUser()
	{
		if ($this->validInput() == false)
		{
			return [false, 1];
		}
		else if ($this->checkIfExists() == true)
		{
			return [false, 2];
		}
		else
		{
			if ($this->register() == false)
			{
				return [false, 3];
			}
			else
			{
				return [true];
			}
		}
	}

	private function register()
	{
		try
		{
			$stmt = $this->db->dbh->prepare("INSERT INTO users SET username = ?, password = ?");
			$stmt->bindValue(1, $this->user->getName());
			$stmt->bindValue(2, $this->user->getPassword());
			$stmt->execute();

			return true;
		}
		catch(PDOException $err)
		{
			return false;
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
			if (strlen(trim($_POST['username'])) <= 0 || strlen(trim($_POST['password'])) <= 0)
			{
				return false;
			}
			else
			{
				$this->user = new User(trim($_POST["username"]), trim($_POST["password"]));
				return true;
			}
		}
	}

	private function checkIfExists()
	{
		$stmt = $this->db->dbh->prepare("SELECT COUNT(*) AS count FROM users WHERE username = ?");
		$stmt->bindValue(1, $this->user->getName());
		$stmt->execute();
		$data = $stmt->fetch();

		if ($data["count"] > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}