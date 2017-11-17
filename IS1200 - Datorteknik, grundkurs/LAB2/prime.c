/*
 prime.c
 By David Broman.
 Last modified: 2015-09-15
 This file is in the public domain.
*/

#include <stdio.h>

int is_prime(int n){
    int i = 2;
    if (i < 2) // Om talet �r mindre �n 2 s� �r det inget primtal, returnerar allts� 0.
    {
        return 0;
    }

    for (i; i < n; i++) // G�r igenom alla m�jliga tal och kollar om det �r ett primtal.
    {
        int b = n % i; // Om % av talet �r lika med 0, s� har vi lyckats dela det j�mnt, allts� �r talet inte ett primtal.
        if ( b == 0)
        {
            return 0;
        }
    }
    return 1; // Annars �r talet ett primtal.
}

int main(void){
    printf("%d\n", is_prime(11));  // 11 is a prime.      Should print 1.
    printf("%d\n", is_prime(383)); // 383 is a prime.     Should print 1.
    printf("%d\n", is_prime(987)); // 987 is not a prime. Should print 0.
    printf("%d\n", is_prime(4));
    printf("%d\n", is_prime(25));
    printf("%d\n", is_prime(47));
    printf("%d\n", is_prime(61));
}
