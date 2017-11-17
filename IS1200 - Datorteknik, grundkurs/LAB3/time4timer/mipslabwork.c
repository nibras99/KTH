/* mipslabwork.c

   This file written 2015 by F Lundevall

   This file should be changed by YOU! So add something here:

   This file modified 2015-12-24 by Ture Teknolog

   Latest update 2015-08-28 by F Lundevall

   For copyright and licensing, see file COPYING */

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

int mytime = 0x5957;
int timeoutcount = 0;   // Global timeoutcount
char textstring[] = "text, more text, and even more text!";

/* Interrupt Service Routine */
void user_isr( void )
{
  return;
}

/* Lab-specific initialization goes here */
void labinit( void )
{
    volatile int *trise = (volatile int*)0xBF886100;  // Skapar en volatile som pekar på samma som TRISE
    *trise &= ~0xFF;                                  // Maskar och har kvar alla andra utom bitarna 0 -> 7 bitarna, samt inverterar allt i slutet. Lämnar dem andra som dem var sedan innan.

    TRISD |= 0xFE0;     // Sätter bitarna 5 -> 11 som input. Lämnar dem andra som dem var sedan innan.

    T2CON = 0x070;      // 1:256 == 80 000 000Hz / 256 = 312 500
                        // 312 500 / 10 .... 10 gånger per sekund, som uppgiten säger = 31250
                        // 31250 = 7A12 i HEX
    TMR2 = 0x0;         // Clearar
    PR2 = 0x7A12;       // Så länge kommer den räkna, tills den uppnår det värdet, värdet vi räknat ut ovan
    // http://ww1.microchip.com/downloads/en/DeviceDoc/61143H.pdf Sida 90.
    IPCSET(2) = 0x0000001C;       // Sätter priority level enligt manual.
    IPCSET(2) = 0x00000003;       // Sätter priority level enligt manual.
    IFSCLR(0) = 0x00000100;       // Clearar timerns interupt flagga enligt manual.
    IECSET(0) = 0x00000100;       // Enablar timer interut enligt manual.

    T2CONSET = 0x8000;            // Startar timern
    return;
}

/* This function is called repetitively from the main program */
void labwork( void )
{
    volatile int* porte = (volatile int*)0xBF886110;      // Skapar en volatile som pekar på samma som TRISE
    static int count = 0;                                 // Skapar en count
    if (IFS(0) & 0x100)                                           // Om IFS(0) inte är 0 längre, så har vi uppnåt "10 gånger per sekund" som uppgiften kräver
    {
      IFS(0) = 0;                                         // Så vi återställer den till 0
      if (timeoutcount == 10)                             // Om timeoutcount == 10, alltså om vi har uppnåt en sekund.
      {
        timeoutcount = 0;                                 // Återställer timeoutcount
        time2string( textstring, mytime );
        display_string( 3, textstring );
        display_update();
        tick( &mytime );
        display_image(96, icon);
        *porte = count;                                   // Sätter LEDsen som det värde counten motsvarar
        count++;                                          // Ökar LED counten med 1
      }
    timeoutcount++;                                       // Ökar timeoutcount med 1
  }
    if (getbtns)                                              // Om ett knapptryck registreras
    {
      if ((getbtns() & 0x04) == 4)                          // Om knapptrycket är knapp 4
      {
          mytime = (mytime & 0x0FFF) | (getsw() << 12);     // Skriver över "största" minuten med värdet som spakarna "visar"
      }
      if ((getbtns() & 0x02) == 2)                          // Om knapptrycket är knapp 3
      {
          mytime = (mytime & 0xF0FF) | (getsw() << 8);      // Skriver över "minsta" minuten med värdet som spakarna "visar"
      }
      if ((getbtns() & 0x01) == 1)                          // Om knapptrycket är knapp 2
      {
          mytime = (mytime & 0xFF0F) | (getsw() << 4);      // Skriver över "största" sekunden med värdet som spakarna "visar"
      }
  }
}
