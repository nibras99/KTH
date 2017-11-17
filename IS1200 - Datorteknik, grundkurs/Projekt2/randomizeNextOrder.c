#include "header.h"

int randomizeNextOrder(void)
{
    int option = 0;
    int answer = 0;

    /*
    char kaffe[32][32];
    char teMjolkSocker[32][32];
    char kaffeSocker[32][32];
    char kaffeMjolkSocker[32][32];
    char kaffeMjolk[32][32];
    char te[32][32];
    char teSocker[32][32];
    char teMjolk[32][32];
    */

    option = randomizer() % 9 + 3;

    switch(option)
    {
        case 4:
            answer = 0x0004;     // Te
            break;
        case 5:
            answer = 0x0005;     // Te + Socker
            break;
        case 6:
            answer = 0x0006;     // Te + Mjölk
            break;
        case 7:
            answer = 0x0007;     // Te + Mjölk + Socker
            break;
        case 8:
            answer = 0x0008;     // Kaffe
            break;
        case 9:
            answer = 0x0009;     // Kaffe + Socker
            break;
        case 10:
            answer = 0x000A;    // Kaffe + Mjölk
            break;
        case 11:
            answer = 0x000B;    // Kaffe + Mjölk + Socker
            break;
        default:
            answer = 0x000C;     // Kaffe
            break;
    }
    return answer;
}
