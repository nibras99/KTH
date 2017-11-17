-module(moves).
-compile(export_all).

single({_, 0}, {Main, One, Two}) ->
	{Main, One, Two};

single({one, Move}, {[], One, Two}) when Move > 0 ->
	{[], One, Two};
single({one, Move}, {Main, [], Two}) when Move < 0 ->
	{Main, [], Two};

single({one, Move}, {Main, One, Two}) when Move > 0 ->
	{list:take(Main, length(Main) - Move), list:append(One, list:drop(Main, length(Main) - Move)), Two};

single({two, Move}, {Main, One, Two}) when Move > 0 ->
	{list:take(Main, length(Main) - Move), One, list:append(Two, list:drop(Main, length(Main) - Move))};

single({one, Move}, {Main, One, Two}) when Move < 0 ->
	{list:append(Main, list:drop(One, length(One) - -Move)), list:take(One, length(One) - -Move), Two};

single({two, Move}, {Main, One, Two}) when Move < 0 ->
	{list:append(Main, list:drop(Two, length(Two) - -Move)), One, list:take(Two, length(Two) - -Move)}.

move([], _) ->
	[];
move([Current | []], List) ->
	[List, single(Current, List)];
move([Current | Rest], List) ->
	[List | move(Rest, single(Current, List))].

moveR([], _) ->
	[];
moveR([Current | []], List) ->
	single(Current, List);
moveR([Current | Rest], List) ->
	moveR(Rest, single(Current, List)).