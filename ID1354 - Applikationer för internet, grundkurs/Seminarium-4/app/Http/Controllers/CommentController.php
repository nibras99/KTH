<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Auth;
use App\Comment;
use Illuminate\Support\Facades\DB;
use Response;
use Session;

class CommentController extends Controller
{
    public static function index(Request $request)
    {
        $recipe = $request->input("recipe");
        $comments = DB::table('comments')->select("comments.*", "users.name")->where("recipe", $recipe)->join("users", "users.id", "=", "comments.user_id")->get();
        return json_encode($comments);
    }

    public function store(Request $request, $recipe)
    {
        $validator = $this->validate(request(), [
            "comment" => "required"
        ]);

        if ($validator == null || $validator->passes())
        {
            $comment = new Comment;
            $comment->comment = request("comment");
            $comment->recipe = $recipe;
            $comment->user_id = Auth::id();

            $comment->save();

            $comment->name = Auth::user()->name;

            $pollings = resource_path("assets/polling/");
            $files = array_diff(scandir($pollings), array('.', '..'));

            foreach ($files as $file)
            {
                file_put_contents($pollings . $file, "store " . json_encode($comment) . PHP_EOL, FILE_APPEND);
            }

			return response()->json(['success'=>$comment]);
        }

    	return json_encode($validator->errors()->all());
    }

    public function destroy(Request $request, $recipe)
    {
        $validator = $this->validate(request(), [
            "comment_id" => "required"
        ]);

	    $comment = DB::table('comments')->where("recipe", $recipe)->where("id", request("comment_id"))->get();

	    if ($comment[0]->user_id != Auth::id())
	    {
            return Response::json(['user_id'=>"That comment doesn't belong to you."], 422);
	    }
	    else
	    {
		    DB::table('comments')->where('id', '=', request("comment_id"))->delete();

            $pollings = resource_path("assets/polling/");
            $files = array_diff(scandir($pollings), array('.', '..'));

            foreach ($files as $file)
            {
                file_put_contents($pollings . $file, "destroy " . request("comment_id") . "\n", FILE_APPEND);
            }

            return response()->json('success');
	    }

	    return json_encode($validator->errors()->all());
    }

    public function poll(Request $request, $recipe)
    {
        // Så att vi inte timear ut (eller ja... i normala fall.). Ska egentligen vara "0" såklart.
        set_time_limit(5);

        // Ser till så att file skapas / finns som iaf en tom fil.
        $filePath = resource_path("assets/polling/msg-to-user-" . Session::getId() . ".txt");
        touch($filePath);

        while (TRUE)
        {
            $data = $this->checkContent($filePath);

            if ($data !== false)
            {
                $data = str_replace(PHP_EOL, '', $data);
                $data = explode(" ", $data);
                $data[1] = json_decode($data[1]);

                return json_encode($data);
            }

            // Jag har testat varje möjlig kombination av nedan som jag kan komma på.
            // Oavsett vad så låser sig sidan för alla.
            // session_write_close();
            // $request->session()->save();
            // Session::store();
            // Session::save();
            sleep(1);
            //session_start();
        }
    }

    private function checkContent($filePath)
    {
        // Om det finns något i filen, så hämtar vi en rad, och tar bort den ur filen.
        // Sedan returnar vi den specifika raden.
        if (file_get_contents($filePath) !== '')
        {
            $file = file($filePath);
            $output = $file[0];
            unset($file[0]);
            file_put_contents($filePath, $file);

            return $output;
        }
        else
        {
            return false;
        }
    }
}
