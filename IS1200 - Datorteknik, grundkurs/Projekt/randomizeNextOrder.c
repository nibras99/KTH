/*
Här inne slumpar vi fram nästa order och returner ett värde som printOrder sedan kan ta emot enkelt.
*/
int randomizeNextOrder(void)
{
    int option = 0;
    int answer = 0;

    option = randomizer() % 9 + 3;// Slumpar mellan 4 och 11

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
            answer = 0x0008;     // Kaffe
            break;
    }
    return answer;
}
