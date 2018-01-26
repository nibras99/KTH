<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/css/reset.css">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"></script>
    <title>(Two) Tasty Recipes</title>
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
<nav class="navbar navbar-expand navbar-dark bg-dark">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggle"
            aria-controls="navbarToggle" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse navbar-toggleable-md" id="navbarToggle">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/calendar">Calender</a>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            @if (!Auth::check())
                <li class="nav-item"><a class="nav-link" href="/register">Sign Up</a></li>
                <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
            @else
                <li class="nav-item"><a class="nav-link" href="{{ route('logout') }}" onclick="event.preventDefault(); document.getElementById('frm-logout').submit();">Logout</a></li>
                <form id="frm-logout" action="{{ route('logout') }}" method="POST" style="display: none;">
                    {{ csrf_field() }}
                </form>
            @endif
        </ul>
    </div>
</nav>
@yield('content')
</body>
</html>
