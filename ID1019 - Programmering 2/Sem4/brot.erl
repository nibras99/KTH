-module(brot).
-compile(export_all).

mandelbrot(C, M) ->
	ZStart = cmplx:new(0, 0),
	I = 0,
	test(I, ZStart, C, M).

test(_, _, _, 0) ->
	0;

test(I, ZStart, C, M) ->
	Z = cmplx:add(cmplx:sqr(ZStart), C),
	Absolute = cmplx:abs(Z),
	if
		Absolute >= 2 ->
			I;
		true ->
			test(I + 1, Z, C, M - 1)
	end.