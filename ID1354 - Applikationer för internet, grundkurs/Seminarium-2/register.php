<?php
require_once 'layout/header.php';

$error = false;
if (!empty($_POST))
{
	require_once 'db/RegisterUser.php';
	$registerUser = new RegisterUser();
	$data = $registerUser->registerUser();

	if ($data[0] == true)
	{
		$registerSuccess = true;
	}
	else
	{
		$error = $data[1];
	}
}
?>
	<div id="account-wrapper">
		<h3>- REGISTRERING -</h3>
		<a href="login.php">Har du redan ett konto? Logga in genom att klicka här!</a>
		<h1<?php if (isset($error) && $error == 1) { ?> class="required"<?php } ?>>* Alla fält obligatoriska</h1>
		<?php if (isset($error) && $error == 2) { ?><h1 class="required">Användarnamnet upptaget.</h1><?php } ?>
		<?php if (isset($registerSuccess) && $registerSuccess) { ?>
			<h1 class="success">Registrering lyckad!</h1>
		<?php } ?>
		<form method="post">
			<label for="login-username">Användarnamn</label>
			<input type="text" name="username" id="login-username" placeholder="Användarnamn">
			<label for="login-password">Lösenord</label>
			<input type="password" name="password" id="login-password" placeholder="Lösenord">
			<input type="submit" value="REGISTRERA">
		</form>
		<h2>För att avbryta, vänligen lämna bara sidan.</h2>
	</div>
<?php
require_once 'layout/footer.php'
?>