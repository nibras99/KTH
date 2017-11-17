#include "header.h"

/*
Denna fil inneh�ller en m�ngd funktioner som uppdaterar matrixen som sedan �ven skrivs till sk�rmen
Anledningen till att vi har olika funktioner �r beroende p� vilken "sk�rm" som skall uppdateras
Dessa uppdateringar har olika storlekar och platser det skall in p�, d�rav de olika funktionerna
*/

// Skriven av Axel Isaksson
// Modifierad av Casper Johansson & Jonny Sparrenh�k
// Kolla filen COPYING
void display_update_matrix() {
    int x, y, value;
    int k;
    for (y = 0; y < 4; y++) {
        DISPLAY_CHANGE_TO_COMMAND_MODE;
        spi_send_recv(0x22); // Set page start and end address
        spi_send_recv(y); // Column to use.

        spi_send_recv(0x00); // Set lower nibble of column start address
        spi_send_recv(0x10); // Set higher part of column start address

        DISPLAY_CHANGE_TO_DATA_MODE;

        // Load and send data for each segment
        for (x = 0; x < 128; x++) {
            value = (display_matrix[x][y * 8]);
            for(k = 1; k < 8; k++){
                value |= (display_matrix[x][y * 8 + k]) << k;
            }
            spi_send_recv(value); // Send value to the display
        }
    }
}

void reset_matrix(void)
{
    int x, y;
    for (x = 0; x < 128; x++)
    {
        for (y = 0; y < 32; y++)
        {
            display_matrix[x][y] = 0;
        }
    }
}

void update_matrix_5(char newMatrix1[5][5], char newMatrix2[5][5], char newMatrix3[5][5], int pos)
{
    int x, y;

    for (x = 7 + (32 * pos); x <= 11 + (32 * pos); x++)
    {
        for (y = 18; y <= 22; y++)
        {
            display_matrix[x][y] = newMatrix1[x - 7 - (32 * pos)][y - 18];
        }
    }

    for (x = 13 + (32 * pos); x <= 17 + (32 * pos); x++)
    {
        for (y = 18; y <= 22; y++)
        {
            display_matrix[x][y] = newMatrix2[x - 13 - (32 * pos)][y - 18];
        }
    }

    for (x = 19 + (32 * pos); x <= 23 + (32 * pos); x++)
    {
        for (y = 18; y <= 22; y++)
        {
            display_matrix[x][y] = newMatrix3[x - 19 - (32 * pos)][y - 18];
        }
    }
}

void update_matrix_32(char newMatrix[32][32], int pos)
{
    int x, y;
    x = 0 + (32 * pos);
    for (x = x; x < 32 + (pos * 32); x++)
    {
        for (y = 0; y < 32; y++)
        {
            display_matrix[x][y] = newMatrix[x - (pos * 32)][y];
        }
    }
}

void update_matrix_128(char newMatrix[128][32])
{
    int x, y;
    for (x = 0; x < 128; x++)
    {
        for (y = 0; y < 32; y++)
        {
            display_matrix[x][y] = newMatrix[x][y];
        }
    }
}

void matrix_remove_smoke(int removeAmount, int pos)
{
    int x, y;
    for (x = 5 + (32 * pos); x <= 21 + (32 * pos); x++)
    {
        for (y = 1; y <= removeAmount; y++)
        {
            display_matrix[x][y] = 0;
        }
    }
}

void update_matrix_welcome(char smoke[16][6], char welcomeText[128][32], char newMatrix2[109][5], bool displayStart)
{
    int x, y;
    for (x = 0; x < 128; x++)
    {
        for (y = 0; y < 32; y++)
        {
            display_matrix[x][y] = welcomeText[x][y];
        }
    }

    for (x = 73; x <= 88; x++)
    {
        for (y = 4; y <= 8; y++)
        {
            display_matrix[x][y] = smoke[x - 73][y - 4];
        }
    }

    if (displayStart == true)
    {
        for (x = 10; x <= 118; x++)
        {
            for (y = 24; y <= 28; y++)
            {
                display_matrix[x][y] = newMatrix2[x - 10][y - 24];
            }
        }
    }
}

void update_matrix_fired(char newMatrix[107][28])
{
    int x, y;
    for (x = 0; x < 107; x++)
    {
        for (y = 4; y < 32; y++)
        {
            display_matrix[x][y] = newMatrix[x][y - 4];
        }
    }
}

// Skriven av Axel Isaksson
// Kolla filen COPYING
uint8_t spi_send_recv(uint8_t data) {
	while(!(SPI2STAT & 0x08));
	SPI2BUF = data;
	while(!(SPI2STAT & 0x01));
	return SPI2BUF;
}

// Skriven av Axel Isaksson
// Kolla filen COPYING
void delay(int cyc) {
	int i;
	for(i = cyc; i > 0; i--);
}

// Skriven av Axel Isaksson
// Modifierad av Casper Johansson & Jonny Sparrenh�k
// Kolla filen COPYING
int initiate(void)
{
    /* Set up peripheral bus clock */
	OSCCON &= ~0x180000;
	OSCCON |= 0x080000;

	/* Set up output pins */
	AD1PCFG = 0xFFFF;
	ODCE = 0x0;
	TRISECLR = 0xFF;
	PORTE = 0x0;

	/* Output pins for display signals */
	PORTF = 0xFFFF;
	PORTG = (1 << 9);
	ODCF = 0x0;
	ODCG = 0x0;
	TRISFCLR = 0x70;
	TRISGCLR = 0x200;

	/* Set up input pins */
	TRISDSET = (1 << 8);
	TRISFSET = (1 << 1);

	/* Set up SPI as master */
	SPI2CON = 0;
	SPI2BRG = 4;

	/* Clear SPIROV*/
	SPI2STATCLR &= ~0x40;
	/* Set CKP = 1, MSTEN = 1; */
    SPI2CON |= 0x60;
	/* Turn on SPI */
	SPI2CONSET = 0x8000;

	//volatile int *trise = (volatile int*)0xBF886100;    // Skapar en volatile som pekar p� samma som TRISE
    //*trise &= ~0xFF;                                    // Maskar och har kvar alla andra utom dem bitarna, samt inverterar allt i slutet.

    TRISE &= ~0xFF;
    TRISD |= 0xFE0;     // BTN2 - BTN4, SW1 - SW4
    TRISF |= 0x2;       // BTN1

    T2CON = 0x070;      // 1:256 == 80 000 000Hz / 256 = 312 500
                        // 312 500 / 10 .... 10 g�nger per sekund, som uppgiten s�ger = 31250
                        // 31250 = 7A12 i HEX*/

    TMR2 = 0x0;         // Clearar

    PR2 = 0x7A12;       // S� l�nge kommer den r�kna, tills den uppn�r det v�rdet, v�rdet vi r�knat ut ovan

    IPCSET(2) = 0x0000001C;  // S�tter priority level enligt manual.
    IPCSET(2) = 0x00000003;  // S�tter priority level enligt manual.

    IFSCLR(0) = 0x00000100; // Clearar timerns interupt flagga enligt manual.
    IECSET(0) = 0x00000100; // Enablar timer interut enligt manual.

    T2CONSET = 0x8000;      // Startar timern

    DISPLAY_COMMAND_DATA_PORT &= ~DISPLAY_COMMAND_DATA_MASK;
	delay(10);
	DISPLAY_VDD_PORT &= ~DISPLAY_VDD_MASK;
	delay(1000000);

	spi_send_recv(0xAE);
	DISPLAY_RESET_PORT &= ~DISPLAY_RESET_MASK;
	delay(10);
	DISPLAY_RESET_PORT |= DISPLAY_RESET_MASK;
	delay(10);

	spi_send_recv(0x8D);
	spi_send_recv(0x14);

	spi_send_recv(0xD9);
	spi_send_recv(0xF1);

	DISPLAY_VBATT_PORT &= ~DISPLAY_VBATT_MASK;
	delay(10000000);

	spi_send_recv(0xA1);
	spi_send_recv(0xC8);

	spi_send_recv(0xDA);
	spi_send_recv(0x20);

	spi_send_recv(0xAF);
}
