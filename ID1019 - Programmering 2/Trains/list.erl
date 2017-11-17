-module(list).
-compile(export_all).

take([], _) ->
	[];
take(_, 0) ->
	[];
take([H | T], N) ->
	[H | take(T, N - 1)].

drop([], _) ->
	[];
drop(HT, 0) ->
	HT;
drop([_ | T], N) ->
	drop(T, N - 1).

append(Xs, Ys) ->
	Xs ++ Ys.

member([], _) ->
	false;

member([H | _], H) ->
	true;

member([_ | T], Y) ->
	member(T, Y).

position([H | _], H) ->
	1;
position([_ | T], Y) ->
	1 + position(T, Y).