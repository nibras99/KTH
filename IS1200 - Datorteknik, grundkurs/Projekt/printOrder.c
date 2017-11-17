/*
En fil som "Printar" till sk�rmen beroende p� vad den f�r f�r input s� skickar den r�tt "v�rden" till sk�rmen.
V�rden slumpas fram och sparas i main som sedan vidarebefodras hit
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
            update_matrix_32(kopp, place);                  // Te + Mj�lk
            update_matrix_5(te, mjolk, tom, place);
            break;
        case 0x0007:
            update_matrix_32(kopp, place);                  // Te + Mj�lk + Socker
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
            update_matrix_32(kopp, place);                  // Kaffe + Mj�lk
            update_matrix_5(kaffe, mjolk, tom, place);
            break;
        case 0x000B:
            update_matrix_32(kopp, place);                  // Kaffe + Mj�lk + Socker
            update_matrix_5(kaffe, mjolk, socker, place);
            break;
        default:
            update_matrix_32(kopp, place);                  // Kaffe
            update_matrix_5(kaffe, tom, tom, place);        // kommer dock aldrig intr�ffa  i och med att vi har ett case f�r varje m�jliga kombination
            break;                                          // Safety first dock^
    }
}
