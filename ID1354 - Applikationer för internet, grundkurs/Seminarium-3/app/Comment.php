<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Comment extends Model
{
    public $timestamps = false;

	protected $fillable = [
		'id', 'user_id', 'recipe', 'comment'
	];
}
