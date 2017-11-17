#include <stdio.h>

int main()
{
    int a = 10;
    int *b = &a;
    a++;
    int c = a + *b;
    char *d = "HELLO";
    char *e = d + 4;
    a = c + (int)*e + (int)d[5];
    printf("%d\n", a);
}
