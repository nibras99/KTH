-module(msort).
-compile(export_all).

merge(A, []) -> A;
merge([], B) -> B;
merge([Ha | Ta], [Hb | Tb]) ->
  if
    Ha < Hb -> [Ha | merge(Ta, [Hb|Tb])];
    true -> [Hb | merge([Ha|Ta], Tb)]
  end.

msort([]) ->
	[];
msort([L]) ->
	[L];
msort(L) ->
	{A, B} = msplit(L, [], []),
	merge(msort(A), msort(B)).


mergesort([]) -> [];
mergesort([E]) -> [E];
mergesort(L) ->
  {A, B} = msplit(L, [], []),
  merge(mergesort(A), mergesort(B)).


msplit([], A, B) ->
	{A, B};

msplit([H | []], A, B) ->
	msplit([], A ++ H, B);

msplit([H, H2 | T], A, B) ->
	msplit([T], A ++ [H], B ++ [H2]).