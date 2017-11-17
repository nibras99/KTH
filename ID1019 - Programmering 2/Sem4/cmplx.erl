-module(cmplx).
-compile(export_all).

new(X, Y) ->
	{X, Y}.

add({Ra, Ia}, {Rb, Ib}) ->
	{Ra + Rb, Ia + Ib}.

sqr({R, I}) ->
	NewR = R * R - I * I,
	NewI = 2 * R * I,
	{NewR, NewI}.

abs({R, I}) ->
	math:sqrt(R * R + I * I).