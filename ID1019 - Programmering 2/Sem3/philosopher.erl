-module(philosopher).
-export([start/5]).

-import(timer,[sleep/1]).
-import(rand,[uniform/1]).

-import(chopstick, [start/0]).
-import(chopstick, [return/1]).
-import(chopstick, [quit/1]).

start(Hungry, Right, Left, Name, Ctrl) ->
	spawn_link(fun() -> dreaming(Hungry, Right, Left, Name, Ctrl) end).


dreaming(Hungry, Right, Left, Name, Ctrl) ->
	io:format("Dröm start (~s) ~n", [Name]),
	sleep(1000, 1000),
	io:format("Dröm klar  (~s) ~n", [Name]),

	chopsticks(Hungry, Right, Left, Name, Ctrl).

chopsticks(Hungry, Right, Left, Name, Ctrl) ->
	io:format("R väntar   (~s) ~n", [Name]),
	chopstick:request(Right),
	io:format("R fått     (~s) ~n", [Name]),

	io:format("L väntar   (~s) ~n", [Name]),
	chopstick:request(Left),
	io:format("L fått     (~s) ~n", [Name]),

	eating(Hungry, Right, Left, Name, Ctrl).

eating(Hungry, Right, Left, Name, Ctrl) ->
	io:format("Äter start (~s) ~n", [Name]),
	sleep(1000, 1000),
	io:format("Äter klar  (~s) ~n", [Name]),

	chopstick:return(Right),
	io:format("R return   (~s) ~n", [Name]),
	chopstick:return(Left),
	io:format("L return   (~s) ~n", [Name]),
	if
		Hungry == 0 ->
			Ctrl ! done;
		true ->
			dreaming(Hungry - 1, Right, Left, Name, Ctrl)
	end.

sleep(Min, Rand) ->
	timer:sleep(Min + rand:uniform(Rand)).