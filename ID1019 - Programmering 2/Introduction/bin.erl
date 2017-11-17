-module(bin).
-compile(export_all).

bin1(I) ->
	integer_to_list(list_to_binary(I)).
	