#include <stdio.h>

int main () {
    int x = 0x2000;
    int y=0x00;
    int z=0xf01;
    z=(x>>9)&&(y<<5);
    printf("%d",z);
   return 0;
}
