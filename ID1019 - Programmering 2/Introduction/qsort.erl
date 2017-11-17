-module(qsort).
-compile(export_all).

qsort([]) -> [];

qsort([Pivot | T]) ->
	{Small, Large} = qsplit(Pivot, T, [], []),
	SmallSorted = qsort(Small),
	LargeSorted = qsort(Large),
	SmallSorted ++ [Pivot] ++ LargeSorted.

qsplit(_Pivot, [], Small, Large) ->
	{Small, Large};

qsplit(Pivot, [H | T], Small, Large) ->
	if
		H =< Pivot ->
			qsplit(Pivot, T, [H | Small], Large);
		true ->
			qsplit(Pivot, T, Small, [H | Large])
	end.