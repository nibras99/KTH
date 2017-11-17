-module(reverse).
-compile(export_all).

nreverse([]) ->
	[];
nreverse([H|T]) ->
	R = nreverse(T),
	lists:append(R, [H]).

reverse(L) ->
	reverse(L, []).
reverse([], R) ->
	R;
reverse([H|T], R) ->
	lists:reverse(T, [H|R]).

bench() ->
	Ls = [16, 32, 64, 128, 256, 512, 1024, 2048, 4096],
	N = 100,
	Bench = fun(L) ->
		S = lists:seq(1,L),
		Tn = time(N, fun() -> nreverse(S) end),
		Tr = time(N, fun() -> reverse(S) end),
		io:format("length: ~10w NormalRev-> ~8w us AdvRev-> ~8w us~n", [L, Tn, Tr])
	end,
	lists:foreach(Bench, Ls).

time(N, F)->
	%% time in micro seconds
	T1 = erlang:system_time(micro_seconds),
	loop(N, F),
	T2 = erlang:system_time(micro_seconds),
	(T2 -T1).

loop(N, Fun) ->
	if N == 0 -> ok; true -> Fun(), loop(N-1, Fun) end.
