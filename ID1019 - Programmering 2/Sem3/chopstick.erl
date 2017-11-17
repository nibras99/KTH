-module(chopstick).
-export([start/0, request/1, return/1, quit/1]).


%%%
%
%	"Privata" funktioner
%
%%%

start() ->
	spawn_link(fun() -> available() end).

available() ->
	receive
		{request, From} ->
			From ! granted,
			gone();
		quit ->
			ok
	end.

gone() ->
	receive
		return ->
			returned,
			available();
		quit ->
			ok
	end.

request(Stick) ->
	Stick ! {request, self()},
	receive
		granted ->
			ok
	end.

return(Stick) ->
	Stick ! return.

quit(Stick) ->
	Stick ! quit.