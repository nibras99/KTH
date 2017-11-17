#include "header.h"
#include "welcomeText.h"

#define HEALTH 3
#define TIMEPERORDER 200 //200 = 20 sec

int getButtonPress(void)    // Returnerar vilken / vilka knappar som är nedtryckta
{
    int a = (PORTD & 0x00E0) >> 4;
    // 1110
	int b = (PORTF & 0x0002) >> 1;
	// 0001
	int c = a | b;

	return c;
}

int getSwitches(void)       // Returnerar alla switchars status
{
    int a = (PORTD & 0x0F00) >> 8;
    // 1111
    return a;
}

int lives = HEALTH;          // Antal liv spelaren har
int points = 0x0000;         // Spelaren börjar med 0 poäng, HEX pga vi använder ledsen när vi skriver ut dem i slutet
int timePerOrder = 200;

bool displayStart = false;              // Variabel vi använder för att bestämma vilken version av "Welcome" displayen vi använder
bool waitingForGameToStart = true;      // Variabel som gör att "Welcome" skärmen visas
int waitingForGameToStartScreen = 0;    // Bestämmer vilken

bool gameActive = true;             // bool som säger att spelet är aktivt
bool gameOver = true;               // bool som säger att det är game over
int orderChecker = 0;               // int som används i en forLoop för att gå igenom alla ordrar gällande tid, vad de har för värden, vad ordern är etc.
int order[4] = {0, 0, 0, 0};        // int array som innehåller ordrarnas hex värde, som sedan användas i andra funktioner och Switch cases

int timeLeft[4] = {0, 0, 0, 0};     // int array som innehåller tiden kvar för varje order
int timeSwitch = 0;                 // int som används som räknare när skärmen skall byta innehåll på welcome

int main(void) {
while (1)                           // Gör så att spelet alltid kan startas om och aldrig är slut, kommer alltid vara true
{
    initiate();                     // Initierar variablar som gör att alla saker funkar såsom skärmen, switches, buttons, timer etc

    while (waitingForGameToStart == true)
    {
        reset_matrix();             // Återställer hela matrixen som skrivs ut på skärmen så att ingen pixel är "tänd"

        if (waitingForGameToStartScreen == 0)       // Ena versionen av Welcome skärmen
        {
             update_matrix_welcome(smokeOne, welcome, pressToContinue, displayStart);
             PORTE = (0xAA & 0x00FF);
        }


        else if (waitingForGameToStartScreen == 1)  // Andra versionen av Welcome skärmen
        {
            update_matrix_welcome(smokeTwo, welcome, pressToContinue, displayStart);
            PORTE = (0x55 & 0x00FF);
        }

        display_update_matrix();        // Uppdaterar skärmen med vad som står i display_matrix arrayen

        if (IFS(0) & 0x100)             // Om det har gått 1/10 sek, uppdatera vissa variablar på Welcome skärmen
        {
            IFS(0) = 0;
            timeSwitch++;
            if (timeSwitch == 15)
            {
                if (waitingForGameToStartScreen == 0)
                {
                    waitingForGameToStartScreen = 1;
                    displayStart = true;
                }

                else if (waitingForGameToStartScreen == 1)
                {
                    waitingForGameToStartScreen = 0;
                    displayStart = false;
                }
                timeSwitch = 0;
            }
        }

        if (getButtonPress() != 0)      // Om någon knapp över huvud taget trycks ned och släpps upp, starta!
        {
            while (getButtonPress() != 0)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }

            if (getSwitches() == 0x0001)
            {
                timePerOrder = 200;
            }
            else if (getSwitches() == 0x0002)
            {
                timePerOrder = 150;
            }
            else if (getSwitches() == 0x0004)
            {
                timePerOrder = 100;
            }
            else if (getSwitches() == 0x0008)
            {
                timePerOrder = 50;
            }
            else
            {
                timePerOrder = 200;
            }

        waitingForGameToStart = false;
        }
    }

    IFS(0) = 0;
    while (gameActive == true)
    {
        if (lives <= 0)     // Om dina liva är lika med 0 eller mindre (just in case) så är det game over!
        {
            gameActive = false;
            gameOver = true;
        }

        if (lives == 1)                 // Beroende på hur mycket liv vi har kvar så ändras ledsen, vi ville att 1 led = ett liv så vi fick kompromissa med if satser
            PORTE = (0x0001 & 0x00FF);
        if (lives == 2)
            PORTE = (0x0003 & 0x00FF);
        if (lives == 3)
            PORTE = (0x0007 & 0x00FF);
        if (lives == 4)
            PORTE = (0x000F & 0x00FF);
        if (lives == 5)
            PORTE = (0x001F & 0x00FF);
        if (lives == 6)
            PORTE = (0x003F & 0x00FF);
        if (lives == 7)
            PORTE = (0x007F & 0x00FF);
        if (lives >= 8)
            PORTE = 0x00FF;

        if (IFS(0) & 0x100)     // För varje 1/10 sec så ökar vi hur mycket av tiden som gått
        {
            IFS(0) = 0;
            timeLeft[0]++;
            timeLeft[1]++;
            timeLeft[2]++;
            timeLeft[3]++;
        }

        reset_matrix();         // Återställer skärmen så vi kan skriva nya saker till den

        for (orderChecker = 0; orderChecker < 4; orderChecker++)            // Går igenom varje orderplats
        {
            if (timeLeft[orderChecker] >= timePerOrder)                     // Om vi har uppnåt de sekunder som vi vill för den specifika ordern
            {                                                               // Så minskar vi livet och återställer ordern i och med att dem inte hunnit i tid
                order[orderChecker] = 0;
                lives = lives - 1;
            }

            if (order[orderChecker] == 0)                                   // Här "slumpar" vi fram en ny order och återställer tiden för den ordern
            {
                order[orderChecker] = randomizeNextOrder();
                timeLeft[orderChecker] = 0;
            }
            printOrder(order[orderChecker], orderChecker);                  // "Printar" ordern till skärmen
            matrix_remove_smoke(timeLeft[orderChecker] / (timePerOrder / 10), orderChecker);       // Tar bort röken för den specifika ordern beroende på hur mycket tid det har gått
        }

        display_update_matrix();        // Uppdaterar skärmen på riktigt

        if (getButtonPress() != 0 && getSwitches() == 0x000F)      // Ett litet "fusk" som ger oss 8 liv
        {
            while (getButtonPress() != 0)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            lives = 8;
        }

        if ((getButtonPress() & 0x01) == 1)     // Om vi har tryckt på knapp ett, jämför switchen med knappens order-värde
        {                                       // Matchar det så ger vi + i points och återställer den, matchar det inte så ger vi - i liv och återställer
            while ((getButtonPress() & 0x01) == 1)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            if (getSwitches() == order[3])
            {
                order[3] = 0;
                points = points + 0x0001;
            }

            else
            {
                lives = lives - 1;
                order[3] = 0;
            }
        }

        if ((getButtonPress() & 0x02) == 2)
        {
            while ((getButtonPress() & 0x02) == 2)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            if (getSwitches() == order[2])
            {
                order[2] = 0;
                points = points + 0x0001;
            }
            else
            {
                lives = lives - 1;
                order[2] = 0;
            }
        }

        if ((getButtonPress() & 0x04) == 4)
        {
            while ((getButtonPress() & 0x04) == 4)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            if (getSwitches() == order[1])
            {
                order[1] = 0;
                points = points + 0x0001;
            }
            else
            {
                lives = lives - 1;
                order[1] = 0;
            }

        }

        if ((getButtonPress() & 0x08) == 8)
        {
            while ((getButtonPress() & 0x08) == 8)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            if (getSwitches() == order[0])
            {
                order[0] = 0;
                points = points + 0x0001;
            }
            else
            {
                lives = lives - 1;
                order[0] = 0;
            }
        }
    }

    while (gameOver == true)            // När det är gameover så skriver vi ut det och visar hur många ordrar du klarade med hjälp av LEDsen
    {
        reset_matrix();
        update_matrix_fired(fired);
        display_update_matrix();
        PORTE = (points & 0x00ff);

        if (getButtonPress() != 0)      // Om något knapptryck görs här så återställer vi programemt och man kan spela igen!
        {
            while (getButtonPress() != 0)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
        waitingForGameToStart = true;
        gameActive = true;
        lives = HEALTH;
        order[0] = 0;
        order[1] = 0;
        order[2] = 0;
        order[3] = 0;
        points = 0x0000;
        gameOver = false;
        }

    }
}
return 0;
}
