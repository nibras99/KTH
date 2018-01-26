<?php
class User
{
	private $username;
	private $password;
	private $userId;

	public function __construct($username, $password) {
		$this->username = $username;
		$this->password = $password;
	}

	public function getName()
	{
		return $this->username;
	}

	public function getPassword()
	{
		return $this->password;
	}

	public function getUserId()
	{
		return $this->userId;
	}

	public function setUserId($userId)
	{
		$this->userId = $userId;
	}
}