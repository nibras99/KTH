-module(isort).
-compile(export_all).

isort(L) -> isort(L, []).

isort([], SL) ->
	SL;
isort([H | T], SL) ->
	isort(T, insert(H, SL)).

insert(Element, []) ->
	[Element];

insert(Element, [Head | Tail]) ->
	if
		Element =< Head ->
			[Element] ++ [Head | Tail];
		true ->
			[Head] ++ insert(Element, Tail)
	end.