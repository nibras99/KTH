#include "header.h"

void printOrder(int currentValue, int place)
{

    /*
    --char kaffe[32][32];
    --char teMjolkSocker[32][32];
    --char kaffeSocker[32][32];
    --char kaffeMjolkSocker[32][32];
    --char kaffeMjolk[32][32];
    --char te[32][32];
    --char teSocker[32][32];
    --char teMjolk[32][32];*/

    switch(currentValue)
    {
        case 0x0004:
            update_matrix_32(te, place);     // Te
            break;
        case 0x0005:
            update_matrix_32(teSocker, place);     // Te + Socker
            break;
        case 0x0006:
            update_matrix_32(teSocker, place);     // Te + Mjölk
            break;
        case 0x0007:
            update_matrix_32(teSocker, place);     // Te + Mjölk + Socker
            break;
        case 0x0008:
            update_matrix_32(kaffe, place);     // Kaffe
            break;
        case 0x0009:
            update_matrix_32(kaffeSocker, place);     // Kaffe + Socker
            break;
        case 0x000A:
            update_matrix_32(kaffeMjolk, place);    // Kaffe + Mjölk
            break;
        case 0x000B:
            update_matrix_32(kaffeMjolkSocker, place);    // Kaffe + Mjölk + Socker
            break;
        default:
            update_matrix_32(kaffe, place);     // Kaffe
            // kommer dock aldrig inträffa
            break;
    }
}
