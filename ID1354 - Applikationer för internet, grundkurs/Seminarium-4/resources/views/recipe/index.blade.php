@extends('layout')

@section('content')
    <script type="text/javascript">
        var recipeTitle = '{{ $recipe->data->title }}';
        var csrf_field = '{{ csrf_field() }}';
        var logged_in = '{{ Auth::check() }}';
        var user_id = '{{ Auth::id() }}';
    </script>

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

        <div class="col col-lg-8 offset-lg-2" id="comment-errors">
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


        <div class="comments"></div>

        <script src="/js/insertComment.js"></script>
        <script src="/js/getRecipeComments.js"></script>
        <script src="/js/storeComment.js"></script>
        <script src="/js/destroyComment.js"></script>
        <script src="/js/longPolling.js"></script>

    </div>

@endsection
