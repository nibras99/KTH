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
    int sieves[n - 2]; // Tal 0, 1 är inga primtal, alltså kan vi ta antalet tal - 2.
    int i = 0;
    for (i; i <= n-2; i++) // Tar hänsyn till n - 2
    {
        sieves[i] = 1; // Förmodar att alla tal är primtal.
    }

    i = 0;
    j = 2;
    for (i; i <= n - 2; i++) // n - 2
    {
        if (sieves[i] == 1) // Om talet inte redan är "urbockat".
        {
            j = 2;
            for (j; (i + 2)* j <= n; j++) // Kontrollera alla tal som upfyller Sieve of Eratosthenes
            {
                sieves[((i + 2) * j) - 2] = 0; // Tar hänsyn till n - 2, lite klumpigt, finns säkert bättre lösning.
            }
        }
    }
    i = 0;
    for (i; i <= n - 2; i++) // Printar alla primtal, tar hänsyn till n - 2
    {
        if (sieves[i] == 1)
            print_number(i + 2);
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
        print_sieves(103);
    return 0;
}
