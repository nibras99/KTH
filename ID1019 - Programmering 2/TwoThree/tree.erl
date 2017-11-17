-module(tree).
-compile(export_all).

insertf(Key, Value, nil) ->
	{leaf, Key, Value};

insertf(K, V, {leaf, K1, _} = L) ->
	if
		K =< K1 ->
			{two, K, {leaf, K, V}, L};
		true ->
			{two, K1, L, {leaf, K, V}}
	end;

insertf(K, V, {two, K1, {leaf, K1, _} = L1, {leaf, K2, _} = L2}) ->
	if
		K =< K1 ->
			{three, _};
		K =< K2 ->
			{three, _};
		true ->
			{three, _}
	end;