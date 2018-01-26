<?php

class DatabaseHandler
{
	private $host = 'localhost';
	private $db = 'recipe';
	private $user = 'root';
	private $pass = 'limsta';
	private static $instance;
	public $dbh;

	// Öppna DB, samt se till så att alla tabeller finns.
	// För users och kommentarer.
	private function __construct()
	{
		$dsn = 'mysql:host=' . $this->host . ';dbname=' . $this->db;
		$opt = [
			PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
		];
		$this->dbh = new PDO($dsn, $this->user, $this->pass, $opt);

		$this->createTables();
	}

	public static function getInstance()
	{
		if (!isset(self::$instance))
		{
			$object = __CLASS__;
			self::$instance = new $object;
		}
		return self::$instance;
	}

	private function createTables()
	{
		$this->createUserTable();
		$this->createCommentsTable();
	}

	private function createUserTable()
	{
		$table_name = 'users';
		$user_id = 'user_id';
		$username = 'username';
		$password = 'password';

		$stmt = $this->dbh->prepare("CREATE TABLE IF NOT EXISTS $table_name ($user_id INT(11) NOT NULL AUTO_INCREMENT, $username VARCHAR(255) NOT NULL, $password VARCHAR(255) NOT NULL, PRIMARY KEY($user_id))");
		$stmt->execute();
	}

	private function createCommentsTable()
	{
		$table_name = 'comments';
		$comment_id = 'comment_id';
		$user_id = 'user_id';
		$comment = 'comment';
		$recipe = "recipe";

		$stmt = $this->dbh->prepare("CREATE TABLE IF NOT EXISTS $table_name ($comment_id INT(11) NOT NULL AUTO_INCREMENT, $recipe VARCHAR(255) NOT NULL, $user_id INT(11) NOT NULL, $comment VARCHAR(255) NOT NULL, PRIMARY KEY($comment_id))");
		$stmt->execute();
	}
}