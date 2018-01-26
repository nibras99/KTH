<?php
require_once '../../layout/header.php';
$xml = simplexml_load_file("cookbook.xml");
$recipe = json_decode(json_encode($xml), TRUE);
$recipe = $recipe["recipe"];

require_once '../../db/Comments.php';
$comments = new Comments();

if (!empty($_POST))
{
	if (isset($_POST["create"]))
	{
		$data = $comments->createComment();

		if ($data[0] == true)
		{
		}
		else
		{
			$error = $data[1];
		}
	}
	else
	{
		if (isset($_POST["delete"]))
		{
			$data = $comments->deleteComment();

			if ($data[0] == true)
			{
			}
			else
			{
				$errorDelete = $data[1];
			}
		}
	}
}

$data = $comments->getComments($recipe["title"]);

?>
    <div id="recipe-wrapper">
        <h1><?php echo $recipe["title"] ?> - Recept</h1>

        <div id="image-wrapper">
            <img src="/<?php echo $recipe["imagepath"] ?>" alt="<?php echo "Bild på " . $recipe["title"] ?>">
        </div>

        <div id="recipe-info">
            <span>Förberedelse: <?php echo $recipe["preptime"] ?></span>
            <span>Tillagning: <?php echo $recipe["cooktime"] ?></span>
            <span>Portioner: <?php echo $recipe["quantity"] ?></span>
        </div>

        <h2>Ingredienser</h2>
        <ul>
			<?php
			foreach ($recipe["ingredient"]["li"] as $item)
			{
				echo "<li>" . $item . "</li>";
			}
			?>
        </ul>

        <h2>Anvisningar</h2>
        <ul>
			<?php
			foreach ($recipe["recipetext"]["li"] as $item)
			{
				echo "<li>" . $item . "</li>";
			}
			?>
        </ul>

        <h2>Kommentarer</h2>
		<?php if (isset($error))
		{
			if ($error == 1)
			{
				echo "<h4>Du måste vara inloggad för att kommentera.</h4>";
			}
			else
			{
				if ($error == 2)
				{
					echo "<h4>Du kan inte skicka en tom kommentar.</h4>";
				}
			}
		}
		if (isset($errorDelete))
		{
			if ($errorDelete == 1)
			{
				echo "<h4>Du måste vara inloggad för att radera kommentarer.</h4>";
			}
			else
			{
				if ($errorDelete == 2)
				{
					echo "<h4>Inte fippla med HTML koden!</h4>";
				}
				else
				{
					if ($errorDelete == 3)
					{
						echo "<h4>Du kan bara radera dina egna kommentarer.</h4>";
					}
					else
					{
						echo "<h4>:/</h4>";
					}
				}
			}
		}
		if (isset($_SESSION["user"]))
		{
			?>
            <form method="post">
                <textarea name="comment" id="recipe-commente" placeholder="Skriv en kommentar!"></textarea>
                <input type="hidden" name="recipe" value="<?php echo $recipe["title"] ?>">
                <input type="submit" value="Skicka kommentar" name="create">
            </form>
		<?php } else { ?>
            <h5>Logga in för att kommentera!</h5>
		<?php } ?>
        <div id="user-comments">
			<?php
			foreach ($data as $item)
			{
				?>
                <div class="comment" data-comment-id="<?php echo $item["comment_id"] ?>">
                    <div class="comment-left">
                        <h3><?php echo $item["username"] ?></h3>
                        <p><?php echo $item["comment"] ?></p>
                    </div>
					<?php if (isset($_SESSION["user"]) && $_SESSION["user"]->getUserId() == $item["user_id"]) { ?>
                        <div class="comment-right">
                            <form method="post">
                                <input type="hidden" name="comment_id" value="<?php echo $item["comment_id"] ?>">
                                <input type="submit" value="X" name="delete">
                            </form>
                        </div>
					<?php } ?>
                </div>
				<?php
			}
			?>
        </div>
    </div>
<?php
require_once '../../layout/footer.php'
?>