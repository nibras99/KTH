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

    TRISD |= 0xFE0;                                   // Sätter bitarna 5 -> 11 som input. Lämnar dem andra som dem var sedan innan.

    return;
}

/* This function is called repetitively from the main program */
void labwork( void )
{
  delay( 1000 );
  time2string( textstring, mytime );
  display_string( 3, textstring );
  display_update();
  tick( &mytime );
  display_image(96, icon);

  volatile int *porte = (volatile int*)0xBF886110;    // Skapar en volatile som pekar på samma som PORTE
  static int count = 0;                               // Skapar en counter som börjar på 0
  
  /*
  Knapp 4 = 4
  Knapp 3 = 2
  Knapp 2 = 1
  Knapp 1 = 0
  */

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

  *porte = count;                                           // Sätter LEDsen som det värde counten motsvarar
  count++;                                                  // Ökar LED counten med 1
}
