/*
 print-prime.c
 By David Broman.
 Last modified: 2015-09-15
 This file is in the public domain.
*/


#include <stdio.h>
#include <stdlib.h>

#define COLUMNS 6

void print_number(int n);

int column = 0;
int i = 0;

void print_primes(int n)
{
    for (i; i <= n; i++) // Går igenom alla tal
    {
        if (is_prime(i))
        {
            print_number(i);
        }
    }
}

void print_number(int n)
{
    if (column == COLUMNS)
    {
        column = 0;
        printf("\n");
    }

    printf("%10d", n);

    column++;
}

int is_prime(int n)
{
    int i = 2;
    if (n < 2)
    {
        return 0;
    }

    for (i; i < n; i++)
    {
        int b = n % i;
        if ( b == 0)
        {
            return 0;
        }
    }
    return 1;
}

// 'argc' contains the number of program arguments, and
// 'argv' is an array of char pointers, where each
// char pointer points to a null-terminated string.
int main(int argc, char *argv[]){
    if(argc == 2)
        print_primes(atoi(argv[1]));
    else
        printf("Please state an interger number.\n");
    return 0;
}

/*

int main(){
        print_primes(2);
    return 0;
}

*/
