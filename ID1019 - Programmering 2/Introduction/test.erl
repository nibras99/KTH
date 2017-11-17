-module(test).
-export([double/1, ftc/1, rec/2, sq/1, cir/1]).

double(N) -> (N * 2).

ftc(F) -> ((F - 32)/1.8).

rec(A, B) -> (A * B).

sq(A) -> rec(A, A).

cir(R) -> (3.14 * R * R).