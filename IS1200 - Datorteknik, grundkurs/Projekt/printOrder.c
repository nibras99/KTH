/*
En fil som "Printar" till skärmen beroende på vad den får för input så skickar den rätt "värden" till skärmen.
Värden slumpas fram och sparas i main som sedan vidarebefodras hit
*/

#include "kaffeSorter.h"

void printOrder(int currentValue, int place)
{
    switch(currentValue)
    {
        case 0x0004:
            update_matrix_32(kopp, place);                  // Te
            update_matrix_5(te, tom, tom, place);
            break;
        case 0x0005:
            update_matrix_32(kopp, place);                  // Te + socker
            update_matrix_5(te, socker, tom, place);
            break;
        case 0x0006:
            update_matrix_32(kopp, place);                  // Te + Mjölk
            update_matrix_5(te, mjolk, tom, place);
            break;
        case 0x0007:
            update_matrix_32(kopp, place);                  // Te + Mjölk + Socker
            update_matrix_5(te, mjolk, socker, place);
            break;
        case 0x0008:
            update_matrix_32(kopp, place);                  // Kaffe
            update_matrix_5(kaffe, tom, tom, place);
            break;
        case 0x0009:
            update_matrix_32(kopp, place);                  // Kaffe + Socker
            update_matrix_5(kaffe, socker, tom, place);
            break;
        case 0x000A:
            update_matrix_32(kopp, place);                  // Kaffe + Mjölk
            update_matrix_5(kaffe, mjolk, tom, place);
            break;
        case 0x000B:
            update_matrix_32(kopp, place);                  // Kaffe + Mjölk + Socker
            update_matrix_5(kaffe, mjolk, socker, place);
            break;
        default:
            update_matrix_32(kopp, place);                  // Kaffe
            update_matrix_5(kaffe, tom, tom, place);        // kommer dock aldrig inträffa  i och med att vi har ett case för varje möjliga kombination
            break;                                          // Safety first dock^
    }
}
