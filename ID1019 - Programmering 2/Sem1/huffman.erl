-module(huffman).
-compile(export_all).

run() ->
	Sample = read('kallocain.txt', 1000), 	% 1000 första bytesen ur filen
	Freq = freq(Sample),					% Beräkna frekvensen för varje bokstav
	Tree = tree(Freq),						% Skapar ett Träd
	Encode = encode_table(Tree),			% Gör om varje bokstav till ett binärt tal
	Seq = encode(Sample, Encode),			% Bygger en string av binära tal, matchande Encoden
	Decoded = decode(Seq, Encode),			% Går baklänges, Binärt -> String!

	io:fwrite("FREKVENS: ~n~p~n~n", [Freq]),
	io:fwrite("TRÄDET: ~n~p~n~n", [Tree]),
	io:fwrite("ENCODAT: ~n~w~n~n", [Encode]),
	io:fwrite("SEKVENS: ~n~w~n~n", [Seq]),
	io:fwrite("DECODAT: ~n~s~n~n", [Decoded]).

read(File, N) ->
	{ok, Fd} = file:open(File, [read, binary]),
	{ok, Binary} = file:read(Fd, N),
	file:close(Fd),
	case unicode:characters_to_list(Binary, utf8) of
		{incomplete, List, _} ->
			List;
		List ->
			List
	end.


encode([], _) -> [];

encode([H | T], Encode) ->
	{_, FoundBinary} = lists:keyfind(H, 1, Encode),			% Hittar det binära värdet för H charen
	FoundBinary ++ encode(T, Encode).

decode([], _Table) ->
	[];

decode(Seq, Table) ->
	{Char, Rest} = decode_char(Seq, 1, Table),
	[Char] ++ decode(Rest, Table).

decode_char(Seq, N, Table) ->
	{Code, Rest} = lists:split(N, Seq),						% Splittar listan vid N, om vi får false, 
	case lists:keyfind(Code, 2, Table) of					% att den inte matchar så ökar vi N med 1
		{Key, _} ->											% och försöker igen!
			{Key, Rest};
		false ->
			decode_char(Seq, N + 1, Table)
	end.


freq(Text) ->
	freq(lists:sort(Text), []).								% Börja med att sortera, så att det är lätt att räkna freq!
															% Alla "lika" kommer ju ligga efter varandra!
freq([], Freq) ->
	Freq;

freq([Head | Tail], Freq) ->
	{Char, Rest} = lists:splitwith(fun (X) -> X == Head end, Tail),				% Hittar alla lika, kapar listan från där nästa olika börjar
	freq(Rest, [{Head, 1 + length(Char)} | Freq]).								% Samt skicka "resten" för att göra samma sak igen!



tree([{Char, _} | []]) ->
    Char;

tree(LT) ->
    [{Char1, Freq1}, {Char2, Freq2} | Rest] = lists:keysort(2, LT),
    tree([{{Char1, Char2}, Freq1 + Freq2} | Rest]).



encode_table({L, R}) ->
	encode_table(L, <<0>>) ++  encode_table(R, <<1>>).

encode_table({L, R}, <<B/bitstring>>) ->
	encode_table(L, <<B/bitstring, 0>>) ++  encode_table(R, <<B/bitstring, 1>>);

encode_table(Char, <<B/bitstring>>) ->
	[{Char, binary_to_list(B)}].