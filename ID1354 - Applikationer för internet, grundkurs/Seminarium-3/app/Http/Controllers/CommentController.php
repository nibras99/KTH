<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Auth;
use App\Comment;
use Illuminate\Support\Facades\DB;

class CommentController extends Controller
{
    public static function index($recipe)
    {
        return DB::table('comments')->select("comments.*", "users.name")->where("recipe", $recipe)->join("users", "users.id", "=", "comments.user_id")->get();
    }

    public function store($recipe)
    {
        $this->validate(request(), [
            "comment" => "required"
        ]);

        $comment = new Comment;
        $comment->comment = request("comment");
        $comment->recipe = $recipe;
        $comment->user_id = Auth::id();

        $comment->save();

        return redirect("/recipe/" . $recipe);
    }

    public function destroy($recipe)
    {
        $validator = $this->validate(request(), [
            "comment-id" => "required"
        ]);

	    $comment = DB::table('comments')->where("recipe", $recipe)->where("id", request("comment-id"))->get();

	    if ($comment[0]->user_id != Auth::id())
	    {
		    $validator->errors()->add('user_id', "That comment doesn't belong to you.");
	    }
	    else
	    {
		    DB::table('comments')->where('id', '=', request("comment-id"))->delete();
	    }

	    return redirect("/recipe/" . $recipe)->withErrors($validator);
    }
}
