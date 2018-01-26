<?php

Route::get('/', 'IndexController@index');

Route::get('/calendar', 'CalendarController@index');

Route::get('/recipe/{recipe}', 'RecipeController@index');

Route::post('/recipe/{recipe}', 'CommentController@store')->middleware('auth');

Route::post('/recipe/{recipe}/delete', 'CommentController@destroy')->middleware('auth');

Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');
