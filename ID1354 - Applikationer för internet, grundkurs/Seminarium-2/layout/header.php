<?php
require_once($_SERVER['DOCUMENT_ROOT'] . '/db/User.php');
session_start();
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="/css/reset.css">
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<title>Extremt goda recept!</title>
	<!--
	Bilder tagna från följande länkar:

	http://yfimq2at25v1pt2cj1ozsc4k.wpengine.netdna-cdn.com/wp-content/uploads/2016/01/swedish-meatballs-620x411.jpg
	https://www.arla.se/globalassets/bilder-recept/pannkakor-857x600.jpg

	Recept tagna från:
	https://www.arla.se/recept/kottbullar/
	https://www.arla.se/recept/pannkaka/
	-->
</head>
<body>
<div id="menu-wrapper">
    <ul>
        <li><a href="/">Start</a></li><li><a href="/kalender.php">Kalender</a></li><?php if (!isset($_SESSION["user"])) { ?><li><a href="/login.php">Logga In</a></li><li><a href="/register.php">Registrera</a></li><?php } else { ?><li><a href="/loggaut.php">Logga Ut</a><?php } ?>
    </ul>
</div>