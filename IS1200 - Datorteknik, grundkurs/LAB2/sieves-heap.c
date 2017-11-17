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
int eightCount = 0; // Ny variabel som lagrar antalet primtal de är 8 mellan.

void print_sieves(int n)
{
    int *sieves;
    sieves = malloc(n * sizeof(int));    // Skapar en pekar sieves som pekar på
                                         // Adressen till det allokerade minnet

    int i = 2; // Vi börjar på index 2, index 0 = 0, index 1 = 1, inga primtal. Index 2 = 2, skulle kunna skippa i allt utom utskriften, men nu vart det så här.
    for (i; i <= n; i++)
    {
        sieves[i] = 1; // Förmodar till en start att ALLA tal är ett primtal. Tal 2 är det lägsta prim-talet. Så vi sätter tal 2 på index 0.
    }

    i = 2;
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

    i = 2;
    for (i; i <= n - 8; i++) // För alla primtal förutom det n-8, för då kommer vi inte ha något större att jämföra med.
    {
        j = i + 1; // J sätts som nästa primtal
        while (sieves[j] != 1) // Ökar J tills vi kommer till ett till primtal
        {
            j++;
        }
        if (j - i == 8) // Om primtalet vi kommit till minus vårat standard i primtal är 8
            eightCount++; // Så kan vi öka counten med 8
    }

    /*
    for (i; i <= n - 8; i++)
    {
        printf("\n\n%d. %d", i, sieves[i + 8]);
        if (sieves[i + 8] == 1)
            eightCount++;
    }
    */

    // Friar upp minnet
    free(sieves);
    sieves = NULL; // Varför gör man detta?
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
    print_sieves(105);
    printf("\n\nAntalet primtal med 8 i mellan: %dst.", eightCount);
    return 0;
}
