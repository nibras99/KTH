#include "16F690.h"
#include "MATH16.H"
#pragma config |= 0x00D4 

/* I/O-pin definitions								 */ 
/* change if you need a pin for a different purpose	 */
#pragma bit RS	@ PORTB.4
#pragma bit EN	@ PORTB.6
#pragma bit D7	@ PORTC.3
#pragma bit D6	@ PORTC.2
#pragma bit D5	@ PORTC.1
#pragma bit D4	@ PORTC.0

void init( void );
void update_lcd( void );
void initserial( void );
void delay( char ); // ms delay function
void lcd_init( void );
void lcd_putchar( char );
char text1( char );
char text2( char );
char split[3]; 
char key[4];
char code[4];
void printf(const char *string, char variable);
void putchar( char ch );

char i;
char q;/*
char old_new;
int cnt, old_cnt;*/
void main( void)
{
	i = 0;
	q = 0;
	char old_new = 0;
	int cnt = 0;
	int old_cnt = 0;
	init(); /* initiate portpins as input or output */ //rpg
	
	
	/*
							 _____________  _____________ 
							|             \/             |
					  +5V---|Vdd        16F690        Vss|---Gnd
				  SNURR A <-|RA5            RA0/AN0/(PGD)|
				  SNURR B <-|RA4/AN3            RA1/(PGC)|
							|RA3/!MCLR/(Vpp)  RA2/AN2/INT|
							|RC5/CCP                  RC0|
			  LED VÄNSTER <-|RC4                      RC1|
							|RC3/AN7                  RC2|
				LED HÖGER <-|RC6/AN8             AN10/RB4|
			   LED MITTEN <-|RC7/AN9               RB5/Rx|-> RESET KNAPPEN
			  SNURR TRYCK <-|RB7/Tx                   RB6|
							|____________________________|
														  
*/ 
	/* I/O-pin direction in/out definitions, change if needed  */
	ANSEL=0;	//	PORTC digital I/O
	ANSELH=0;
	TRISC = 0b1111.0000;  /* RC3,2,1,0 out*/
	TRISB.4=0;	// RB4 OUT
	TRISB.6=0;	// RB6 OUT
	TRISB.7=1;	// SNURR TRYCK
	TRISC.7=0;	// LED MITTEN
	TRISC.6=0;	// LED HÖGER
	TRISB.5=1;	// RESET KNAPPEN
	TRISC.4=0;	// LED VÄNSTER
	
	lcd_init();
	initserial();		//rpg

	key[0] = 0;
	key[1] = 0;
	key[2] = 0;
	key[3] = 0;
	
	code[0] = 1;
	code[1] = 3;
	code[2] = 3;
	code[3] = 7;
	
	RS = 1;	 					// LCD i skrivläge (första 8 platserna)
	
	// Skriver ut "Code" som ligger sparat i text1
	for(i=0; i<8; i++)
		lcd_putchar(text1(i));
	
	// Ompositionerar våran LCD till nästa 8 platserna
	RS = 0;	 					// LCD i kommandläge
	lcd_putchar(0b11000000);
	
	RS = 1;	 					// LCD i skrivläge (andra 8 platserna)
	//Skriver ut våran nuvarande kod, i detta fall 0000.
	for(i = 0; i<4; i++){
		lcd_putchar(key[i] + 48);
	}
	
	PORTC.6 = 0;
	PORTC.4 = 0;
	PORTC.7 = 0;
	while(1)
	{
		// Om man trycker på RESET-knappen.
		if (!PORTB.5)
		{
			// Reset ledden tänds
			PORTC.4 = 1;
			
			while (!PORTB.5)
			{
				// Do nothing, vänta på att vi släppt knappen
			}
			
			// "Rätt svar" ledden släcks
			PORTC.6 = 0;
			q = 0;
			
			// Resettar alla inskrivna värden
			key[0] = 0;
			key[1] = 0;
			key[2] = 0;
			key[3] = 0;
			
			// Reset ledden släcks
			PORTC.4 = 0;
		}
		
		if (q < 4)
		{
			if (!PORTB.7)
			{
				printf("TRYCKTE PÅ SNURR\r\n", 0);
				// Tänder "nästa" knappen
				PORTC.7 = 1;
				
				while (!PORTB.7)
				{
					// Do nothing, vänta på att vi släppt knappen
				}
				
				// Sparar värdet, går vidare till nästa plats och resettar lite saker
				key[q] = cnt;
				q = q + 1;
				cnt = 0;
				PORTC.7 = 0;
			}
		}
		
		if (q >= 4)
		{
			// Om koden stämmer överens så tänder vi "rätt svar" ledden
			if ((code[0] == key[0]) && (code[1] == key[1]) && (code[2] == key[2]) && (code[3] == key[3]))
			{
				PORTC.6 = 1;
			}
		}
		//read encoder new value
		
		old_new.0 = PORTA.5;  // read rpgA
		old_new.1 = PORTA.4;  // read rpgB
		
		//printf("OLD_NEW: %b\r\n", old_new);  /* this function call takes time! */

		/* compare with transitions in state diagram */
		if ((old_new & 0b000000.11) == 0b000000.10)  // from 00 -> 01, forward
		{
			cnt++;
			if(cnt == 10)
				cnt = 0;
			//printf("HÖGER: %d\r\n", 0);  /* this function call takes time! */
		}

		if ((old_new & 0b000000.11) == 0b000000.00)
		{
			if(cnt == 0)
				cnt = 10;
			cnt--;
			//printf("VÄNSTER: %d\r\n", 0);  /* this function call takes time! */
		}
		
		
		
		
		key[q] = cnt;
		
		/* replace the old values with the new values */
		
		RS = 0;	 					// LCD i kommandläge
		lcd_putchar(0b11000000);	// Kommanderar!
		
		RS = 1;						// LCD i skrivläge
		
		for(i = 0; i<4; i++)
		{
			lcd_putchar(key[i] + 48);
		}
		old_cnt = cnt;  /* update oldcnt */
		
		
		/*
		Testar olika saker, felsökning, tas bort i slutgiltigt program.
		*/
		
		//printf("OLD_NEW: %b\r\n", old_new);
		//printf("Count: %d\r\n", cnt);
		//printf("Q: %d\r\n", q);
		//printf("KEY0: %d\r\n", key[0]);
		//printf("------\r\n", 0);
		
	}
}

void putchar( char ch )  /* sends one char */
{
  char bitCount, ti;
  PORTA.0 = 0; /* set startbit */
  for ( bitCount = 10; bitCount > 0 ; bitCount-- )
   {
     /* delay one bit 104 usec at 4 MHz       */
     /* 5+18*5-1+1+9=104 without optimization */
     ti = 18; do ; while( --ti > 0); nop();
     Carry = 1;     /* stopbit                    */
     ch = rr( ch ); /* Rotate Right through Carry */
     PORTA.0 = Carry;
   }
  return;
}

void printf(const char *string, char variable)
{
  char i, k, m, a, b;
  for(i = 0 ; ; i++)
   {
     k = string[i];
     if( k == '\0') break;   // at end of string
     if( k == '%')           // insert variable in string
      {
        i++;
        k = string[i];
        switch(k)
         {
           case 'd':         // %d  signed 8bit
             if( variable.7 ==1) putchar('-');
             else putchar(' ');
             if( variable > 127) variable = -variable;  // no break!
           case 'u':         // %u unsigned 8bit
             a = variable/100;
             putchar('0'+a); // print 100's
             b = variable%100;
             a = b/10;
             putchar('0'+a); // print 10's
             a = b%10;        
             putchar('0'+a); // print 1's
             break;
           case 'b':         // %b BINARY 8bit
             for( m = 0 ; m < 8 ; m++ )
              {
                if (variable.7 == 1) putchar('1');
                else putchar('0');
                variable = rl(variable);
               }
              break;
           case 'c':         // %c  'char'
             putchar(variable);
             break;
           case '%':
             putchar('%');
             break;
           default:          // not implemented
             putchar('!');  
         }  
      }
      else putchar(k);
   }
}

void update_lcd( void )
{
// reposition to "line 2" (the next 8 chars)
		/*RS = 0;	 // LCD in command-mode
		lcd_putchar( 0b11000000 );
	
		RS = 1;	 // LCD in character-mode
*/

			/* FUNKTION SOM SKRIVER IN CNT I "LINE 2" */
			
			
			/*
			if(t % 2){
				PORTC.7 = 1;
			}
			else
				PORTC.7 = 0;
			*/
			
			//printf("Position: %d\r\n", cnt);	/* this function call takes time! */
			//lcd_putchar(cnt + 48);
			
			
			
			//for(i = 0; i<5; i++)
			//	lcd_putchar(' ');
}


/* *********************************** */
/*			  FUNCTIONS				   */
/* *********************************** */
//* ******************* *//
void init( void )
{
  ANSEL =0;		/* not AD-input */
  TRISA.5 = 1;	/* input rpgA	*/
  TRISA.4 = 1;	/* input rpgB	*/

  /* Enable week pullup's		*/
  //OPTION.7 = 0; /* !RABPU bit	  */
  //WPUA.5	 = 1; /* rpgA pullup  */
  //WPUA.4	 = 1; /* rpgB pullup  */
  
  TRISC.0=0;
  TRISC.1=0;
  TRISC.2=0;
}



void initserial( void )	 /* initialise PIC16F690 bitbang serialcom port */
{
   ANSEL.0 = 0; /* No AD on RA0				*/
   ANSEL.1 = 0; /* No AD on RA1				*/
   PORTA.0 = 1; /* marking line				*/
   TRISA.0 = 0; /* output to PK2 UART-tool	*/
   TRISA.1 = 1; /* input from PK2 UART-tool */
   return;	   
}

void delay10( char n)
/*
  Delays a multiple of 10 milliseconds using the TMR0 timer
  Clock : 4 MHz	  => period T = 0.25 microseconds
  1 IS = 1 Instruction Cycle = 1 microsecond
  error: 0.16 percent. B Knudsen.
*/
{
	char i;

	OPTION = 7;
	do	{
		i = TMR0 + 39; /* 256 microsec * 39 = 10 ms */
		while ( i != TMR0)
			;
	} while ( --n > 0);
}
//* ************* *//

char text1( char x)	  // this is the way to store a sentence
{
   skip(x); /* internal function CC5x.	*/
   #pragma return[] = "Code:   "	// 8 chars max!
}

char text2( char x)	  // this is the way to store a sentence
{
   skip(x); /* internal function CC5x.	*/
   #pragma return[] = "		   "	// 8 chars max!
}


void lcd_init( void ) // must be run once before using the display
{
  delay(40);  // give LCD time to settle
  RS = 0;	  // LCD in command-mode
  lcd_putchar(0b0011.0011); /* LCD starts in 8 bit mode			 */
  lcd_putchar(0b0011.0010); /* change to 4 bit mode				 */
  lcd_putchar(0b00101000);	/* two line (8+8 chars in the row)	 */ 
  lcd_putchar(0b00001100);	/* display on, cursor off, blink off */
  lcd_putchar(0b00000001);	/* display clear					 */
  lcd_putchar(0b00000110);	/* increment mode, shift off		 */
  RS = 1;	 // LCD in character-mode
			 // initialization is done!
}


void lcd_putchar( char data )
{
  // must set LCD-mode before calling this function!
  // RS = 1 LCD in character-mode
  // RS = 0 LCD in command-mode
  // upper Nybble
  D7 = data.7;
  D6 = data.6;
  D5 = data.5;
  D4 = data.4;
  EN = 0;
  nop();
  EN = 1;
  delay(5);
  // lower Nybble
  D7 = data.3;
  D6 = data.2;
  D5 = data.1;
  D4 = data.0;
  EN = 0;
  nop();
	EN = 1;
  delay(5);
}

void delay( char millisec)
/* 
  Delays a multiple of 1 milliseconds at 4 MHz (16F628 internal clock)
  using the TMR0 timer 
*/
{
	OPTION = 2;	 /* prescaler divide by 8		 */
	do	{
		TMR0 = 0;
		while ( TMR0 < 125)	  /* 125 * 8 = 1000	 */
			;
	} while ( -- millisec > 0);
}

/*
             _____________  _____________ 
            |             \/             |
      +5V---|Vdd        16F690        Vss|---Gnd
        A <-|RA5            RA0/AN0/(PGD)|
        B <-|RA4/AN3            RA1/(PGC)|
            |RA3/!MCLR/(Vpp)  RA2/AN2/INT|
            |RC5/CCP                  RC0|
    LED V <-|RC4                      RC1|
            |RC3/AN7                  RC2|
    LED H <-|RC6/AN8             AN10/RB4|
    LED M <-|RC7/AN9               RB5/Rx|-> RESET KNAPPEN
  SNURR T <-|RB7/Tx                   RB6|
            |____________________________|
                                          
*/ 