/*
 print-prime.c
 By David Broman.
 Last modified: 2015-09-15
 This file is in the public domain.
*/


#include <stdio.h>
#include <stdlib.h>

#define COLUMNS 6

void print_sieves(int n);
void print_number(int n);

int column = 0;
int i = 0;
int j = 0;

void print_sieves(int n)
{
    int sieves[n];

    int i = 0;
    for (i; i <= n; i++)
    {
        sieves[i] = 1; // Förmodar att alla tal är primtal.
    }

    i = 2; // Vi börjar på index 2, index 0 = 0, index 1 = 1, inga primtal. Index 2 = 2, skulle kunna skippa i allt utom utskriften, men nu vart det så här.
    j = 2;
    for (i; i <= n; i++) // Medans vi inte gått igenom alla tal
    {
        if (sieves[i] == 1)
        {
            j = 2;
            for (j; i * j < n; j++) // Enligt Sieve of Eratosthenes
            {
                sieves[i * j] = 0;
            }
        }
    }
    i = 2;
    for (i; i <= n; i++) // Printar helt enkelt alla primtal.
    {
        if (sieves[i] == 1)
            print_number(i);
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

// 'argc' contains the number of program arguments, and
// 'argv' is an array of char pointers, where each
// char pointer points to a null-terminated string.
/*int main(int argc, char *argv[]){
    if(argc == 2)
        print_sieves(atoi(argv[1]));
    else
        printf("Please state an interger number.\n");
    return 0;
}

*/

int main(){
        print_sieves(500000);
    return 0;
}
