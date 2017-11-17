-module(shunt).
-compile(export_all).

split(List, Value) ->
	{list:take(List, list:position(List, Value) - 1), list:drop(List, list:position(List, Value))}.

find(_, []) ->
	[];

find([Same | Ta], [Same | Tb]) ->
	find(Ta, Tb);

find([Ha | Ta], [Hb | Tb]) ->
	{A, B} = split([Ha | Ta], Hb),
	[{one, length(B) + 1}, {two, length(A)}, {one, -(length(B) + 1)}, {two, -length(A)} | find(B ++ A, Tb)].

compress(Ms) ->
	Ns = rules(Ms),
		if Ns == Ms ->
			Ms;
		true ->
			compress(Ns)
	end.

rules([]) ->
	[];

rules([{_, 0} | Rest]) ->
	rules(Rest);

rules([{Same, A}, {Same, B} | Rest]) when A + B == 0 ->
	rules(Rest);

rules([{Same, A}, {Same, B} | Rest]) ->
	[{Same, A + B}] ++ rules(Rest);

rules([{One, A}, {Two, B} | Rest]) ->
	[{One, A}] ++ rules([{Two, B}] ++ Rest);

rules([{One, A}]) ->
	[{One, A}].


test() ->
	ListOsorterad = [e,d,c,b,a,f],
	ListSorterad = [a,b,c,d,e,f],
	%ListOsorterad = [c,a,b],
	%ListSorterad = [c,b,a],
	FindMoves = find(ListOsorterad, ListSorterad),
	FixMoves = compress(FindMoves),
	MovesUse = moves:move(FixMoves, {ListOsorterad, [], []}),
	io:fwrite("FÖRFLYTTNINGAR SOM MÅSTE GÖRAS ~p~n",  [FindMoves]),
	io:fwrite("FIXADE FÖRFLYTTNINGAR ~p~n",  [FixMoves]),
	io:fwrite("SVAR EFTER FÖRFLYTTNINGAR ~p~n",  [MovesUse]).