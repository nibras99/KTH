#include <stdlib.h>
#include <stdio.h>

void square_reverse(double *x, double *y, int len)
{
    int i = 0;
    for (i; i < len; i++)
        *(y + len - 1 - i) = *(x + i) * *(x + i);
}

int main()
{
    double in[] = {11, 20, 100, 999};
    double out[4];
    int i = 0;

    printf("IN: ");
    i = 0;
    for (i; i < 4; i++)
    {
        printf("%d...%lf | ", i, in[i]);
    }

    printf("\nOUT: ");
    i = 0;
    for (i; i < 4; i++)
    {
        printf("%d...%lf | ", i, out[i]);
    }

    printf("\nROPAR PÅ FUNKTIONEN\n");
    square_reverse(in, out, 4);

    printf("\nIN: ");
    i = 0;
    for (i; i < 4; i++)
    {
        printf("%d...%lf | ", i, in[i]);
    }

    printf("\nOUT: ");
    i = 0;
    for (i; i < 4; i++)
    {
        printf("%d...%lf | ", i, out[i]);
    }

    return 0;
}
