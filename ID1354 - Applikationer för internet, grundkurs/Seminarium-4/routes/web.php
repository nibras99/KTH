<?php

Route::get('/', 'IndexController@index');

Route::get('/calendar', 'CalendarController@index');

Route::get('/recipe/{recipe}', 'RecipeController@index');

Route::get('/home', 'HomeController@index')->name('home');

Route::get('/comments', 'CommentController@index');

Auth::routes();

Route::post('/comment/destroy/{recipe}', 'CommentController@destroy')->middleware('auth');

Route::post('/comment/store/{recipe}', 'CommentController@store')->middleware('auth');

Route::get('/comment/poll/{recipe}', 'CommentController@poll');
