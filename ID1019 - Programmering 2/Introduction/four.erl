-module(four).
-export([nth/2, number/1, sum/1, duplicate/1, unique/1, reverse/1, pack/1]).

nth(1, [H | _]) ->
	H;

nth(N, [_ | T]) ->
	nth(N - 1, T).

number([_ | T]) ->
	case [] == T of
		true -> 1;
		false -> 1 + number(T)
	end.

sum([H | T]) ->
	case [] == T of
		true -> H;
		false -> H + sum(T)
	end.


duplicate([H | T]) ->
	case [] == T of
		true -> [H, H];
		false -> [H, H] ++ duplicate(T)
	end.

unique([]) ->
	[];
unique([H | T]) ->
	unique([H], T).

unique(L, []) ->
	L;
unique(L, [H | T]) ->
	A = is_unique(H, L),
	if
		H == [] ->
			L;
		A == true ->
			unique(L ++ [H], T);
		true ->
			unique(L, T)
	end.

is_unique(_, []) ->
	true;
is_unique(V, [H | T]) ->
		if
		H == [] ->
			false;
		V == H ->
			false;
		true -> 
			is_unique(V, T)
	end.

reverse([H | T]) ->
	if
		T == [] ->
			[H];
		true ->
			reverse(T) ++ [H]
	end.

pack([]) ->
	[];

pack([H | T]) ->
	pack([[H]], T).

pack(L, []) ->
	L;

pack([L | N], [H | T]) ->
	A = list_exists([H], [L | N]),
	if
		H == [] ->
			[L | N];
		A == false ->
			pack([L | N] ++ [[H]], T);
		A /= false ->
			pack(A, T)
	end.

list_exists(_, []) ->
	false;

list_exists([NewValue | ListExtra], [[ValueDeepList | RestDeepList] | RestList]) ->
	if
		NewValue == ValueDeepList ->
			if
				ListExtra == [] ->
					[[ValueDeepList | RestDeepList] ++ [NewValue] | RestList];
				true ->
					ListExtra ++ [[ValueDeepList | RestDeepList] ++ [NewValue] | RestList]
			end;
		true ->
			if
				ListExtra == [] ->
					NewListExtra = [NewValue, [ValueDeepList | RestDeepList]],
					list_exists(NewListExtra, RestList);
				true ->
					Lista = is_list(ListExtra),
					if
						Lista == true ->
							ReturnList = [NewValue] ++ ListExtra ++ [[ValueDeepList | RestDeepList]];
						true ->
							ReturnList = [NewValue, [ListExtra ++ ValueDeepList]]
					end,
					list_exists(ReturnList, RestList)
			end
	end.