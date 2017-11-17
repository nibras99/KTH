#include "header.h"

int getButtonPress(void)
{
    int a = (PORTD & 0x00E0) >> 4;
	int b = (PORTF & 0x0002) >> 1;
	int c = a | b;

	return c;
}

int getSwitches(void)
{
    int a = (PORTD & 0x0F00) >> 8;

    return a;
    // 0000 0000 0000 1111 om alla är upptryckta
}

int lives = 0x0007; // Ledsen är i binärt, 7 = 3 leds, 3 = 2 leds, 1 = 1 led, (liv)

bool waitingForGameToStart = true;
int waitingForGameToStartScreen = 0;

bool gameActive = true;
int orderChecker = 0;
int order[4] = {0, 0, 0, 0};

int timeLeft[4] = {50, 70, 80, 50};
int timeoutcount = 0;

int main(void) {
    initiate();

    while (waitingForGameToStart == true)
    {
        reset_matrix();

        if (waitingForGameToStartScreen == 0)
        {
             update_matrix_128(welcomeNoText);
             PORTE = (0xAA & 0x00FF);
        }


        else if (waitingForGameToStartScreen == 1)
        {
            update_matrix_128(welcomeWithText);
            PORTE = (0x55 & 0x00FF);
        }

        display_update_matrix();

        if (IFS(0) & 0x100)
        {
            IFS(0) = 0;
            timeoutcount++;
            if (timeoutcount == 30)
            {
                if (waitingForGameToStartScreen == 0)
                    waitingForGameToStartScreen = 1;
                else if (waitingForGameToStartScreen == 1)
                    waitingForGameToStartScreen = 0;
                timeoutcount = 0;
            }
        }

        if ((getButtonPress() & 0x01) == 1)
        {
            while ((getButtonPress() & 0x01) == 1)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
        waitingForGameToStart = false;
        }
    }

    PORTE = (lives & 0x00FF);
    while (gameActive == true)
    {
        if (lives <= 0)
            gameActive = false;
        for (orderChecker = 0; orderChecker < 4; orderChecker++)
        {
            if (order[orderChecker] == 0)
            {
                order[orderChecker] = randomizeNextOrder();
                timeLeft[orderChecker] = 100;
            }
            printOrder(order[orderChecker], orderChecker);
        }
        display_update_matrix();

        if ((getButtonPress() & 0x01) == 1)
        {
            while ((getButtonPress() & 0x01) == 1)
            {
                // Vi gör inget försänn knappen återställs till 0,
                // vi vill inte att det ska ske olyckor med att en IF sats registrerar
                // knapptrycket mer än en gång
            }
            if (getSwitches() == order[3])
                order[3] = 0;
            else
                lives = lives - 1;
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
                order[2] = 0;
            else
                lives = lives - 1;
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
                order[1] = 0;
            else
                lives = lives - 1;
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
                order[0] = 0;
            else
                lives = lives - 1;
        }
    }
    return 0;
}
        /*

        Gammalt test nedan.

        */
/*
    while (ready == 0)
    {
        while (gameNotStarted == 1)
        {
            display_string(0, "");
            display_string(1, "The Moccado Game");
            display_string(2, " BTN1 to Start");
            display_string(3, "");
            display_update();

            if ((getButtonPress() & 0x01) == 1)
            {
                while ((getButtonPress() & 0x01) == 1)
                {
                   // nada
                }
                gameNotStarted = 0;

                display_string(0, "");
                display_string(1, "");
                display_string(2, "");
                display_string(3, "");
                display_update();
            }

        }

        while (gameActivated == 1)
        {
            printedImages[0] = randomizeNextOrder();
            printedImages[1] = 0x1000;
            printedImages[2] = randomizeNextOrder();
            printedImages[3] = randomizeNextOrder();

            printOrder(printedImages[0], 0);
            printOrder(printedImages[1], 1);
            printOrder(printedImages[2], 2);
            printOrder(printedImages[3], 3);

            display_update_gus();
            delay(2500000);

        }
        while (1 == 1)
        {
            reset_matrix();

            //random_number = randy() % 4;
            update_matrix(kaffeKaffe, 0);
            update_matrix(teMjolkSocker, 1);
            update_matrix(kaffeMjolkSocker, 2);

            if (IFS(0) & 0x100)
            {
                IFS(0) = 0;
                timeoutcount++;                         // Ökar timeoutcount till 10, enligt kravet, 10 == 1 sek
                if (timeoutcount == 50)
                {
                    update_matrix(kaffeMjolk, 3);
                    timeoutcount--;
                }
            }
            display_update_gus();
        }


        if ((getButtonPress() & 0x01) == 1)
        {
            while ((getButtonPress() & 0x01) == 1)
            {
               // nada
            }
            display_string(0, "D");
            display_update();
            delay(2500000);
        }

        if ((getButtonPress() & 0x02) == 2)
        {
            while ((getButtonPress() & 0x02) == 2)
            {
               // nada
            }
            display_string(0, "C");
            display_update();
            delay(2500000);
        }

        if ((getButtonPress() & 0x04) == 4)
        {
            while ((getButtonPress() & 0x04) == 4)
            {
               // nada
            }
            display_string(0, "B");
            display_update();
            delay(2500000);
        }

        if ((getButtonPress() & 0x08) == 8)
        {
            while ((getButtonPress() & 0x08) == 8)
            {
               // nada
            }
            display_string(0, "A");
            display_update();
            delay(2500000);
        }
        display_string(0, "");

        display_update();
    }

	display_string(0, "Customers: 3");
	display_string(1, "Current order: ");
	display_string(2, "C + S + T + M");
	display_string(3, "Time left: 9 sec");
	display_update();

	for(;;) ;
	return 0;*/

