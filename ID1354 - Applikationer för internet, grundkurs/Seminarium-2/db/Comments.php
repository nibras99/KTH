<?php

require_once 'User.php';
require_once 'DatabaseHandler.php';

class Comments
{
	private $db;
	private $comment;
	private $recipe;
	private $commentId;

	public function __construct()
	{
		$this->db = DatabaseHandler::getInstance();
	}

	// Hämtar alla kommentarer och returnar dem
	public function getComments($recipe)
	{
		$stmt = $this->db->dbh->prepare("SELECT comments.*, u1.username FROM comments LEFT JOIN users u1 ON u1.user_id = comments.user_id WHERE recipe = ? ORDER BY comment_id DESC");
		$stmt->bindValue(1, $recipe);
		$stmt->execute();

		$data = $stmt->fetchAll();

		return $data;
	}

	// Kör kontroller och skapar en kommentar om allting är OK.
	// Returnar [false, egenfelmeddelandekod] om det inte lyckas
	public function createComment()
	{
		if ($this->isValidUser() == false)
		{
			return [false, 1];
		}
		else if ($this->validInput() == false)
		{
			return [false, 2];
		}
		else if ($this->comment() == false)
		{
			return [false, 3];
		}
		else
		{
			return [true];
		}
	}

	private function validInput()
	{
		if (!isset($_POST['comment']) || !isset($_POST["recipe"]))
		{
			return false;
		}
		else
		{
			if (strlen(trim($_POST['comment'])) <= 0 || strlen(trim($_POST['recipe'])) <= 0)
			{
				return false;
			}
			else
			{
				$this->comment = trim($_POST['comment']);
				$this->recipe= trim($_POST['recipe']);
				return true;
			}
		}
	}

	private function isValidUser()
	{
		if (isset($_SESSION["user"]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private function comment()
	{
		try
		{
			$stmt = $this->db->dbh->prepare("INSERT INTO comments SET recipe = ?, user_id = ?, comment = ?");
			$stmt->bindValue(1, $this->recipe);
			$stmt->bindValue(2, $_SESSION["user"]->getUserId());
			$stmt->bindValue(3, $this->comment);
			$stmt->execute();

			return true;
		}
		catch(PDOException $err)
		{
			return false;
		}
	}

	// Kör kontroller och raderar kommentaren om allting är OK.
	// Returnar [false, egenfelmeddelandekod] om det inte lyckas
	public function deleteComment()
	{
		if ($this->isValidUser() == false)
		{
			return [false, 1];
		}
		else if ($this->validInputDelete() == false)
		{
			return [false, 2];
		}
		else if ($this->usersOwnComment() == false)
		{
			return [false, 3];
		}
		else if ($this->delete() == false)
		{
			return [false, 4];
		}
		else
		{
			return [true];
		}
	}

	private function validInputDelete()
	{
		if (!isset($_POST['comment_id']))
		{
			return false;
		}
		else
		{
			if (strlen(trim($_POST['comment_id'])) <= 0)
			{
				return false;
			}
			else
			{
				$this->commentId = trim($_POST['comment_id']);
				return true;
			}
		}
	}

	private function usersOwnComment()
	{
		$stmt = $this->db->dbh->prepare("SELECT user_id FROM comments WHERE comment_id = ?");
		$stmt->bindValue(1, $this->commentId);
		$stmt->execute();

		$data = $stmt->fetchAll();

		if (count($data) == 1 && $data[0]["user_id"] == $_SESSION["user"]->getUserId())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private function delete()
	{
		try
		{
			$stmt = $this->db->dbh->prepare("DELETE FROM comments WHERE comment_id = ?");
			$stmt->bindValue(1, $this->commentId);
			$stmt->execute();

			return true;
		}
		catch (PDOException $err)
		{
			return false;
		}
	}
}