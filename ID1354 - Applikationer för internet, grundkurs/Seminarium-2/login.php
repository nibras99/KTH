<?php
require_once 'layout/header.php';

$error = false;
if (!empty($_POST))
{
	require_once 'db/LoginUser.php';
	$loginUser = new LoginUser();
	$data = $loginUser->userLogin();

	if ($data[0] == true)
	{
		header("Location: /");
	}
	else
	{
	    $error = $data[1];
	}
}
?>
    <div id="account-wrapper">
        <h3>- LOGGA IN -</h3>
        <a href="register.php">Inget konto? Registrera dig genom att klicka här!</a>
        <h1<?php if (isset($error) && $error == 1) { ?> class="required"<?php } ?>>* Alla fält obligatoriska</h1>
		<?php if (isset($error) && $error == 2) { ?><h1 class="required">Ingen kombination av användarnamnet och
            lösenordet hittades.</h1><?php } ?>
        <form method="post">
            <label for="login-username">Användarnamn</label>
            <input type="text" name="username" id="login-username" placeholder="Användarnamn">
            <label for="login-password">Lösenord</label>
            <input type="password" name="password" id="login-password" placeholder="Lösenord">
            <input type="submit" value="LOGGA IN">
        </form>
        <h2>För att avbryta, vänligen lämna bara sidan.</h2>
    </div>
<?php
require_once 'layout/footer.php'
?>