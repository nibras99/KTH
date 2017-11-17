/* mipslabwork.c

   This file written 2015 by F Lundevall

   This file should be changed by YOU! So add something here:

   This file modified 2015-12-24 by Ture Teknolog

   Latest update 2015-08-28 by F Lundevall

   For copyright and licensing, see file COPYING */

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

int prime = 1234567;    // Primtalet vi börjar på
int mytime = 0x5957;    // Tiden vi börjar på
int timeoutcount = 0;   // Timeoutcount... enligt uppgiften
int count = 0;          // Vanlig count, används för att räkna antingen primcount eller secondcount

char textstring[] = "text, more text, and even more text!";

/* Interrupt Service Routine */
void user_isr( void ) {
    if (IFS(0) & 0x100)                                           // Om IFS(0) inte är 0 längre, så har vi uppnåt "10 gånger per sekund" som uppgiften kräver
    {
        //IFS(0) = 0;                             // Återställer IFS till 0, att vi upptäckt att den gått ett varv
        timeoutcount++;                         // Ökar timeoutcount till 10, enligt kravet
        if (timeoutcount == 10)                 // Om vi uppnår 10, så skriver vi ut, det har gått en sekund etc.
        {
            time2string(textstring, mytime);
            display_string(3, textstring);
            display_update();
            tick(&mytime);
            timeoutcount = 0;                   // Återställer timeoutcount
            //PORTE = (++count & 0x00ff);       // Om du vill att LEDsen uppdaterar för varje sekund istället för primtalen, avkommentera denna
                                                // och kommentera bort den liknande raden längst ner
        }
    }

    if (IFS(0) & 0x800)
    {
        tick(&mytime);
        tick(&mytime);
        tick(&mytime);
    }
    IFS(0) = 0;
}

/* Lab-specific initialization goes here */
void labinit( void )
{
    volatile int *trise = (volatile int*)0xBF886100;    // Skapar en volatile som pekar på samma som TRISE
    *trise &= ~0xFF;                                    // Maskar och har kvar alla andra utom dem bitarna, samt inverterar allt i slutet.

    TRISD |= 0xFE0;     // Sätter bitarna 5 -> 11 som input.

    T2CON = 0x070;      // 1:256 == 80 000 000Hz / 256 == 312 500
                        // 312 500 / 10 .... 10 gånger per sekund == 31250
                        // 31250 = 7A12
    TMR2 = 0x0;         // Clearar
    PR2 = 0x7A12;       // Så länge kommer den räkna, tills den uppnår det värdet, värdet vi räknat ut ovan
    // http://ww1.microchip.com/downloads/en/DeviceDoc/61143H.pdf Sida 90.
    /*
    IPCSET(2) = 0x0000001C; // Sätter priority level enligt manual.
    IPCSET(2) = 0x00000003; // Sätter priority level enligt manual.
    IFSCLR(0) = 0x00000100; // Clearar timerns interupt flagga enligt manual.
    IECSET(0) = 0x00000100; // Enablar timer interut enligt manual.
    */

    IPCSET(2) = 0x1C00001C; // Sätter priority level enligt manual.
    IPCSET(2) = 0x03000003; // Sätter priority level enligt manual.
    IFSCLR(0) = 0x00000900; // Clearar timerns interupt flagga enligt manual.
    IECSET(0) = 0x00000900; // Enablar timer interut enligt manual.

    enable_interrupt();     // Enablar interrupt

    T2CONSET = 0x8000; // Startar timern
    return;
}

/* This function is called repetitively from the main program */
void labwork( void ) {
    prime = nextprime( prime );
    display_string(0, itoaconv(prime));
    display_update();
    PORTE = (++count & 0x00ff); // Visar leden enligt varje primtal som ökar.
}
