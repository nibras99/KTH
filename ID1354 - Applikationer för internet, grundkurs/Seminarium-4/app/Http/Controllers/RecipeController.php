<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\CommentController;
use App\Recipe;

class RecipeController extends Controller
{
    public function index($recipe)
    {
        //$comments = CommentController::index($recipe);
        $recipeModel = new Recipe($recipe);

        return view('recipe/index', [
            "recipe" => $recipeModel
            //"comments" => $comments
        ]);
    }
}
