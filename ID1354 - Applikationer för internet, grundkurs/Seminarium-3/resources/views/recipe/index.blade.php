@extends('layout')

@section('content')

    <div class="container" id="recipe">
        <div class="row">
            <div class="col col-lg-8 offset-lg-2">
                <h2>{{ $recipe->data->title }} - Recipe</h2>
            </div>
        </div>

        <div class="row">
            <div class="col col-lg-8 offset-lg-2">
                <img src="/{{ $recipe->data->imagepath }}" class="img-fluid"
                     alt="Image off {{ $recipe->data->title }}">
            </div>
        </div>

        <div class="row">
            <div class="col col-lg-8 offset-lg-2">
                <ul class="list-inline">
                    <li class="list-inline-item">Preparation: {{ $recipe->data->preptime }}</li>
                    <li class="list-inline-item">Cooking: {{ $recipe->data->cooktime }}</li>
                    <li class="list-inline-item">Portions: {{ $recipe->data->quantity }}</li>
                </ul>
            </div>
        </div>

        <div class="col col-lg-8 offset-lg-2">
            <h3>Ingredients</h3>
        </div>

        <div class="row">
            <div class="col col-lg-8 offset-lg-2">
                <ul class="list-group">
                    @foreach ($recipe->data->ingredient->li as $item)
                        <li class="list-group-item">{{ $item }}</li>
                    @endforeach
                </ul>
            </div>
        </div>

        <div class="col col-lg-8 offset-lg-2">
            <h3>Instructions</h3>
        </div>

        <div class="row">
            <div class="col col-lg-8 offset-lg-2">
                <ul class="list-group">
                    @foreach ($recipe->data->recipetext->li as $item)
                        <li class="list-group-item">{{ $item }}</li>
                    @endforeach
                </ul>
            </div>
        </div>

        <div class="col col-lg-8 offset-lg-2">
            <h3>Comments</h3>
        </div>

        <div class="col col-lg-8 offset-lg-2">
            @foreach ($errors->all() as $message)
                <h5 class="text-danger">{{ $message }}</h5>
            @endforeach
        </div>

        @if (Auth::check())
            <div class="col col-lg-8 offset-lg-2 comment-create">
                <form method="post">
                    {{ csrf_field() }}

                    <div class="form-group">
                        <textarea name="comment" class="form-control" placeholder="Write a comment..."
                                  rows="3"></textarea>
                    </div>

                    <button type="submit" class="btn btn-block btn-primary">Post</button>
                </form>
            </div>
        @else
            <div class="col col-lg-8 offset-lg-2">
                <h6>Please login to comment.</h6>
                <hr>
            </div>
        @endif

        @foreach ($comments as $comment)
            <div class="comment">
                <div class="row">
                    <div class="col col-lg-8 offset-lg-2">
                        <h4>{{ $comment->name }}</h4>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-7 col-md-10 col-sm-9 col-8 offset-lg-2">
                        <p>{{ $comment->comment }}</p>
                    </div>
                    @if (Auth::check() && (Auth::id() == $comment->user_id))
                        <div class="col col-lg-1 col-md-2 col-sm-3 col-3">
                            <form method="post" action="/recipe/{{ $recipe->title }}/delete">
                                {{ csrf_field() }}

                                <input type="hidden" name="comment-id" value="{{ $comment->id }}">
                                <button type="submit" class="btn btn-outline-danger pull-right">DELETE</button>
                            </form>
                        </div>
                    @endif
                </div>
            </div>
        @endforeach

    </div>

@endsection
