@extends('layout')

@section('content')

    <div class="container" id="index">
        <div class="row">
            <div class="col col-12 text-center">
                <h1>Welcome, {{ Auth::user()->name }}</h1>
                <p class="lead">You are now logged in!</p>
            </div>
        </div>
    </div>

@endsection
