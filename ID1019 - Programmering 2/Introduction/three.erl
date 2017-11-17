-module(three).
-export([product/2, exp/2]).

product(0, _) ->
	0;
product(M, N) ->
	N + product(M - 1, N).

exp(_, 0) ->
	1;
exp(X, 1) ->
	X;
exp(X, Y) ->
	M = Y rem 2,
	case M of
		0 -> 
			exp(X, Y div 2) * exp(X, Y div 2);
		1 -> 
			exp(X, Y - 1) * X
	end.