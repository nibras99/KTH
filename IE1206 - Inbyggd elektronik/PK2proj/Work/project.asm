
; CC5X Version 3.4H, Copyright (c) B Knudsen Data
; C compiler for the PICmicro family
; ************  26. Apr 2016  23:10  *************

	processor  16F690
	radix  DEC

	__config 0xD4

INDF        EQU   0x00
TMR0        EQU   0x01
PCL         EQU   0x02
FSR         EQU   0x04
PORTA       EQU   0x05
TRISA       EQU   0x85
PORTB       EQU   0x06
TRISB       EQU   0x86
PCLATH      EQU   0x0A
Carry       EQU   0
Zero_       EQU   2
RP0         EQU   5
RP1         EQU   6
IRP         EQU   7
OPTION_REG  EQU   0x81
PORTC       EQU   0x07
TRISC       EQU   0x87
ANSEL       EQU   0x11E
ANSELH      EQU   0x11F
arg1_5      EQU   0x2A
arg2_5      EQU   0x2C
rm          EQU   0x2D
counter_5   EQU   0x2E
tmp         EQU   0x2F
arg1_9      EQU   0x2A
arg2_9      EQU   0x2C
rm_5        EQU   0x2D
counter_9   EQU   0x2E
tmp_2       EQU   0x2F
RS          EQU   4
EN          EQU   6
D7          EQU   3
D6          EQU   2
D5          EQU   1
D4          EQU   0
key         EQU   0x33
code        EQU   0x37
i           EQU   0x3B
q           EQU   0x3C
old_new     EQU   0x20
cnt         EQU   0x21
old_cnt     EQU   0x22
ch          EQU   0x2A
bitCount    EQU   0x2B
ti          EQU   0x2C
string      EQU   0x23
variable    EQU   0x24
i_2         EQU   0x25
k           EQU   0x26
m           EQU   0x27
a           EQU   0x28
b           EQU   0x29
n           EQU   0x7F
i_3         EQU   0x7F
x           EQU   0x23
x_2         EQU   0x7F
data        EQU   0x23
millisec    EQU   0x24
ci          EQU   0x2A

	GOTO main

  ; FILE MATH16.H
			;// SIZE
			;
			;#pragma library 1
			;/*
			;uns16 operator* _mult8x8( uns8 arg1, uns8 arg2);
			;int16 operator* _multS8x8( int8 arg1, int8 arg2);
			;uns16 operator* _multU16x8( uns16 arg1, uns8 arg2);
			;uns16 operator* _mult16x16( uns16 arg1, uns16 arg2);
			;uns16 operator/ _divU16_8( uns16 arg1, uns8 arg2);
			;uns16 operator/ _divU16_16( uns16 arg1, uns16 arg2);
			;int16 operator/ _divS16_8( int16 arg1, int8 arg2);
			;int16 operator/ _divS16_16( int16 arg1, int16 arg2);
			;uns8 operator% _remU16_8( uns16 arg1, uns8 arg2);
			;uns16 operator% _remU16_16( uns16 arg1, uns16 arg2);
			;int8 operator% _remS16_8( int16 arg1, int8 arg2);
			;int16 operator% _remS16_16( int16 arg1, int16 arg2);
			;*/
			;
			;#if __CoreSet__ < 1410
			; #define genAdd(r,a) W=a; btsc(Carry); W=incsz(a); r+=W
			; #define genSub(r,a) W=a; btss(Carry); W=incsz(a); r-=W
			; #define genAddW(r,a) W=a; btsc(Carry); W=incsz(a); W=r+W
			; #define genSubW(r,a) W=a; btss(Carry); W=incsz(a); W=r-W
			;#else
			; #define genAdd(r,a) W=a; r=addWFC(r)
			; #define genSub(r,a) W=a; r=subWFB(r)
			; #define genAddW(r,a) W=a; W=addWFC(r)
			; #define genSubW(r,a) W=a; W=subWFB(r)
			;#endif
			;
			;
			;int8 operator*( int8 arg1, int8 arg2)  @
			;
			;uns16 operator* _mult8x8( uns8 arg1, uns8 arg2)
			;{
_const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF ci
	MOVLW 19
	SUBWF ci,W
	BTFSC 0x03,Carry
	RETLW 0
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  PCLATH
	MOVF  ci,W
	ADDWF PCL,1
	RETLW 84
	RETLW 82
	RETLW 89
	RETLW 67
	RETLW 75
	RETLW 84
	RETLW 69
	RETLW 32
	RETLW 80
	RETLW 197
	RETLW 32
	RETLW 83
	RETLW 78
	RETLW 85
	RETLW 82
	RETLW 82
	RETLW 13
	RETLW 10
	RETLW 0
_mult8x8
			;    uns16 rval;
			;    char counter = sizeof(arg2)*8;
			;    rval.high8 = 0;
			;    W = arg1;
			;    do  {
			;        arg2 = rr( arg2);
			;        if (Carry)
			;            rval.high8 += W;
			;        rval = rr( rval);
			;        counter = decsz(counter);
			;    } while (1);
			;    return rval;
			;}
			;
			;
			;int16 operator* _multS8x8( int8 arg1, int8 arg2)
			;{
_multS8x8
			;    uns16 rval;
			;    char counter = sizeof(arg2)*8;
			;    int8 tmpArg2 = arg2;
			;    rval.high8 = 0;
			;    W = arg1;
			;    do  {
			;        tmpArg2 = rr( tmpArg2);
			;        if (Carry)
			;            rval.high8 += W;
			;        rval = rr( rval);
			;        counter = decsz(counter);
			;    } while (1);
			;    W = arg2;
			;    if (arg1 < 0)
			;        rval.high8 -= W;
			;    W = arg1;
			;    if (arg2 < 0)
			;        rval.high8 -= W;
			;    return rval;
			;}
			;
			;
			;uns16 operator*( uns8 arg1, uns16 arg2) exchangeArgs @
			;
			;uns16 operator* _multU16x8( uns16 arg1, uns8 arg2)
			;{
_multU16x8
			;    uns16 rval;
			;    uns8 rvalH = 0;
			;    char counter = sizeof(arg1)*8;
			;    W = arg2;
			;    do  {
			;        arg1 = rr( arg1);
			;        if (Carry)
			;            rvalH += W;
			;        rvalH = rr(rvalH);
			;        rval = rr(rval);
			;        counter = decsz(counter);
			;    } while (1);
			;    return rval;
			;}
			;
			;
			;int16 operator*( int16 arg1, int16 arg2) @
			;
			;uns16 operator* _mult16x16( uns16 arg1, uns16 arg2)
			;{
_mult16x16
			;    uns16 rval;
			;    char counter = sizeof(arg1)*8;
			;    do  {
			;        Carry = 0;
			;        rval = rl( rval);
			;        arg1 = rl( arg1);
			;        if (Carry)
			;            rval += arg2;
			;        counter = decsz(counter);
			;    } while (1);
			;    return rval;
			;}
			;
			;
			;
			;uns16 operator/ _divU16_8( uns16 arg1, uns8 arg2)
			;{
_divU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF arg2_5
			;    uns8 rm = 0;
	CLRF  rm
			;    char counter = sizeof(arg1)*8+1;
	MOVLW 17
	MOVWF counter_5
			;    goto ENTRY_ML;
	GOTO  m002
			;    do  {
			;        rm = rl( rm);
m001	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   rm,1
			;        uns8 tmp = rl( tmp);
	RLF   tmp,1
			;        W = rm - arg2;
	MOVF  arg2_5,W
	SUBWF rm,W
			;        if (tmp&1)
	BTFSC tmp,0
			;            Carry = 1;
	BSF   0x03,Carry
			;        if (Carry)
	BTFSS 0x03,Carry
	GOTO  m002
			;            rm = W;
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF rm
			;       ENTRY_ML:
			;        arg1 = rl( arg1);
m002	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   arg1_5,1
	RLF   arg1_5+1,1
			;        counter = decsz(counter);
	DECFSZ counter_5,1
			;    } while (1);
	GOTO  m001
			;    return arg1;
	MOVF  arg1_5,W
	RETURN
			;}
			;
			;
			;uns16 operator/ _divU16_16( uns16 arg1, uns16 arg2)
			;{
_divU16_16
			;    uns16 rm = 0;
			;    char counter = sizeof(arg1)*8+1;
			;    goto ENTRY_ML;
			;    do  {
			;        rm = rl( rm);
			;        W = rm.low8 - arg2.low8;
			;        genSubW( rm.high8, arg2.high8);
			;        if (!Carry)
			;            goto ENTRY_ML;
			;        rm.high8 = W;
			;        rm.low8 -= arg2.low8;
			;        Carry = 1;
			;       ENTRY_ML:
			;        arg1 = rl( arg1);
			;        counter = decsz(counter);
			;    } while (1);
			;    return arg1;
			;}
			;
			;
			;int8  operator/ (int8 arg1, int8 arg2) @
			;
			;int16 operator/ _divS16_8( int16 arg1, int8 arg2)
			;{
_divS16_8
			;    uns8 rm = 0;
			;    char counter = 16+1;
			;    char sign = arg1.high8 ^ arg2.high8;
			;    if (arg1 < 0)  {
			;       INVERT_ML:
			;        arg1 = -arg1;
			;        if (!counter)
			;            return arg1;
			;    }
			;    if (arg2 < 0)
			;        arg2 = -arg2;
			;    goto ENTRY_ML;
			;    do  {
			;        rm = rl( rm);
			;        W = rm - arg2;
			;        if (Carry)
			;            rm = W;
			;       ENTRY_ML:
			;        arg1 = rl( arg1);
			;        counter = decsz(counter);
			;    } while (1);
			;    if (sign & 0x80)
			;        goto INVERT_ML;
			;    return arg1;
			;}
			;
			;
			;int16 operator/ _divS16_16( int16 arg1, int16 arg2)
			;{
_divS16_16
			;    uns16 rm = 0;
			;    char counter = sizeof(arg1)*8+1;
			;    char sign = arg1.high8 ^ arg2.high8;
			;    if (arg1 < 0)  {
			;       INVERT_ML:
			;        arg1 = -arg1;
			;        if (!counter)
			;            return arg1;
			;    }
			;    if (arg2 < 0)
			;        arg2 = -arg2;
			;    goto ENTRY_ML;
			;    do  {
			;        rm = rl( rm);
			;        W = rm.low8 - arg2.low8;
			;        genSubW( rm.high8, arg2.high8);
			;        if (!Carry)
			;            goto ENTRY_ML;
			;        rm.high8 = W;
			;        rm.low8 -= arg2.low8;
			;        Carry = 1;
			;       ENTRY_ML:
			;        arg1 = rl( arg1);
			;        counter = decsz(counter);
			;    } while (1);
			;    if (sign & 0x80)
			;        goto INVERT_ML;
			;    return arg1;
			;}
			;
			;
			;uns8 operator% _remU16_8( uns16 arg1, uns8 arg2)
			;{
_remU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF arg2_9
			;    uns8 rm = 0;
	CLRF  rm_5
			;    char counter = sizeof(arg1)*8;
	MOVLW 16
	MOVWF counter_9
			;    do  {
			;        arg1 = rl( arg1);
m003	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   arg1_9,1
	RLF   arg1_9+1,1
			;        rm = rl( rm);
	RLF   rm_5,1
			;        uns8 tmp = rl( tmp);
	RLF   tmp_2,1
			;        W = rm - arg2;
	MOVF  arg2_9,W
	SUBWF rm_5,W
			;        if (tmp&1)
	BTFSC tmp_2,0
			;            Carry = 1;
	BSF   0x03,Carry
			;        if (Carry)
	BTFSS 0x03,Carry
	GOTO  m004
			;            rm = W;
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF rm_5
			;        counter = decsz(counter);
m004	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ counter_9,1
			;    } while (1);
	GOTO  m003
			;    return rm;
	MOVF  rm_5,W
	RETURN
			;}
			;
			;
			;uns16 operator% _remU16_16( uns16 arg1, uns16 arg2)
			;{
_remU16_16
			;    uns16 rm = 0;
			;    char counter = sizeof(arg1)*8;
			;    do  {
			;        arg1 = rl( arg1);
			;        rm = rl( rm);
			;        W = rm.low8 - arg2.low8;
			;        genSubW( rm.high8, arg2.high8);
			;        if (!Carry)
			;            goto NOSUB;
			;        rm.high8 = W;
			;        rm.low8 -= arg2.low8;
			;      NOSUB:
			;        counter = decsz(counter);
			;    } while (1);
			;    return rm;
			;}
			;
			;
			;int8 operator% (int8 arg1, int8 arg2) @
			;
			;int8 operator% _remS16_8( int16 arg1, int8 arg2)
			;{
_remS16_8
			;    int8 rm = 0;
			;    char counter = 16;
			;    char sign = arg1.high8;
			;    if (arg1 < 0)
			;        arg1 = -arg1;
			;    if (arg2 < 0)
			;        arg2 = -arg2;
			;    do  {
			;        arg1 = rl( arg1);
			;        rm = rl( rm);
			;        W = rm - arg2;
			;        if (Carry)
			;            rm = W;
			;        counter = decsz(counter);
			;    } while (1);
			;    if (sign & 0x80)
			;        rm = -rm;
			;    return rm;
			;}
			;
			;
			;int16 operator% _remS16_16( int16 arg1, int16 arg2)
			;{
_remS16_16
			;    int16 rm = 0;
			;    char counter = sizeof(arg1)*8;
			;    char sign = arg1.high8;
			;    if (arg1 < 0)
			;        arg1 = -arg1;
			;    if (arg2 < 0)
			;        arg2 = -arg2;
			;    do  {
			;        arg1 = rl( arg1);
			;        rm = rl( rm);
			;        W = rm.low8 - arg2.low8;
			;        genSubW( rm.high8, arg2.high8);
			;        if (!Carry)
			;            goto NOSUB;
			;        rm.high8 = W;
			;        rm.low8 -= arg2.low8;
			;      NOSUB:
			;        counter = decsz(counter);
			;    } while (1);
			;    if (sign & 0x80)
			;        rm = -rm;
			;    return rm;

  ; FILE project.c
			;#include "16F690.h"
			;#include "MATH16.H"
			;#pragma config |= 0x00D4 
			;
			;/* I/O-pin definitions								 */ 
			;/* change if you need a pin for a different purpose	 */
			;#pragma bit RS	@ PORTB.4
			;#pragma bit EN	@ PORTB.6
			;#pragma bit D7	@ PORTC.3
			;#pragma bit D6	@ PORTC.2
			;#pragma bit D5	@ PORTC.1
			;#pragma bit D4	@ PORTC.0
			;
			;void init( void );
			;void update_lcd( void );
			;void initserial( void );
			;void delay( char ); // ms delay function
			;void lcd_init( void );
			;void lcd_putchar( char );
			;char text1( char );
			;char text2( char );
			;char split[3]; 
			;char key[4];
			;char code[4];
			;void printf(const char *string, char variable);
			;void putchar( char ch );
			;
			;char i;
			;char q;/*
			;char old_new;
			;int cnt, old_cnt;*/
			;void main( void)
			;{
main
			;	i = 0;
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  i
			;	q = 0;
	CLRF  q
			;	char old_new = 0;
	CLRF  old_new
			;	int cnt = 0;
	CLRF  cnt
			;	int old_cnt = 0;
	CLRF  old_cnt
			;	init(); /* initiate portpins as input or output */ //rpg
	CALL  init
			;	
			;	
			;	/*
			;							 _____________  _____________ 
			;							|             \/             |
			;					  +5V---|Vdd        16F690        Vss|---Gnd
			;				  SNURR A <-|RA5            RA0/AN0/(PGD)|
			;				  SNURR B <-|RA4/AN3            RA1/(PGC)|
			;							|RA3/!MCLR/(Vpp)  RA2/AN2/INT|
			;							|RC5/CCP                  RC0|
			;			  LED VÄNSTER <-|RC4                      RC1|
			;							|RC3/AN7                  RC2|
			;				LED HÖGER <-|RC6/AN8             AN10/RB4|
			;			   LED MITTEN <-|RC7/AN9               RB5/Rx|-> RESET KNAPPEN
			;			  SNURR TRYCK <-|RB7/Tx                   RB6|
			;							|____________________________|
			;														  
			;*/ 
			;	/* I/O-pin direction in/out definitions, change if needed  */
			;	ANSEL=0;	//	PORTC digital I/O
	BCF   0x03,RP0
	BSF   0x03,RP1
	CLRF  ANSEL
			;	ANSELH=0;
	CLRF  ANSELH
			;	TRISC = 0b1111.0000;  /* RC3,2,1,0 out*/
	MOVLW 240
	BSF   0x03,RP0
	BCF   0x03,RP1
	MOVWF TRISC
			;	TRISB.4=0;	// RB4 OUT
	BCF   TRISB,4
			;	TRISB.6=0;	// RB6 OUT
	BCF   TRISB,6
			;	TRISB.7=1;	// SNURR TRYCK
	BSF   TRISB,7
			;	TRISC.7=0;	// LED MITTEN
	BCF   TRISC,7
			;	TRISC.6=0;	// LED HÖGER
	BCF   TRISC,6
			;	TRISB.5=1;	// RESET KNAPPEN
	BSF   TRISB,5
			;	TRISC.4=0;	// LED VÄNSTER
	BCF   TRISC,4
			;	
			;	lcd_init();
	CALL  lcd_init
			;	initserial();		//rpg
	CALL  initserial
			;
			;	key[0] = 0;
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  key
			;	key[1] = 0;
	CLRF  key+1
			;	key[2] = 0;
	CLRF  key+2
			;	key[3] = 0;
	CLRF  key+3
			;	
			;	code[0] = 1;
	MOVLW 1
	MOVWF code
			;	code[1] = 3;
	MOVLW 3
	MOVWF code+1
			;	code[2] = 3;
	MOVLW 3
	MOVWF code+2
			;	code[3] = 7;
	MOVLW 7
	MOVWF code+3
			;	
			;	RS = 1;	 					// LCD i skrivläge (första 8 platserna)
	BSF   0x06,RS
			;	
			;	// Skriver ut "Code" som ligger sparat i text1
			;	for(i=0; i<8; i++)
	CLRF  i
m005	MOVLW 8
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF i,W
	BTFSC 0x03,Carry
	GOTO  m006
			;		lcd_putchar(text1(i));
	MOVF  i,W
	CALL  text1
	CALL  lcd_putchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i,1
	GOTO  m005
			;	
			;	// Ompositionerar våran LCD till nästa 8 platserna
			;	RS = 0;	 					// LCD i kommandläge
m006	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   0x06,RS
			;	lcd_putchar(0b11000000);
	MOVLW 192
	CALL  lcd_putchar
			;	
			;	RS = 1;	 					// LCD i skrivläge (andra 8 platserna)
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x06,RS
			;	//Skriver ut våran nuvarande kod, i detta fall 0000.
			;	for(i = 0; i<4; i++){
	CLRF  i
m007	MOVLW 4
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF i,W
	BTFSC 0x03,Carry
	GOTO  m008
			;		lcd_putchar(key[i] + 48);
	MOVLW 51
	ADDWF i,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVLW 48
	ADDWF INDF,W
	CALL  lcd_putchar
			;	}
	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i,1
	GOTO  m007
			;	
			;	PORTC.6 = 0;
m008	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   PORTC,6
			;	PORTC.4 = 0;
	BCF   PORTC,4
			;	PORTC.7 = 0;
	BCF   PORTC,7
			;	while(1)
			;	{
			;		// Om man trycker på RESET-knappen.
			;		if (!PORTB.5)
m009	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSC PORTB,5
	GOTO  m011
			;		{
			;			// Reset ledden tänds
			;			PORTC.4 = 1;
	BSF   PORTC,4
			;			
			;			while (!PORTB.5)
m010	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS PORTB,5
			;			{
			;				// Do nothing, vänta på att vi släppt knappen
			;			}
	GOTO  m010
			;			
			;			// "Rätt svar" ledden släcks
			;			PORTC.6 = 0;
	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   PORTC,6
			;			q = 0;
	CLRF  q
			;			
			;			// Resettar alla inskrivna värden
			;			key[0] = 0;
	CLRF  key
			;			key[1] = 0;
	CLRF  key+1
			;			key[2] = 0;
	CLRF  key+2
			;			key[3] = 0;
	CLRF  key+3
			;			
			;			// Reset ledden släcks
			;			PORTC.4 = 0;
	BCF   PORTC,4
			;		}
			;		
			;		if (q < 4)
m011	MOVLW 4
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF q,W
	BTFSC 0x03,Carry
	GOTO  m013
			;		{
			;			if (!PORTB.7)
	BTFSC PORTB,7
	GOTO  m013
			;			{
			;				printf("TRYCKTE PÅ SNURR\r\n", 0);
	CLRF  string
	MOVLW 0
	CALL  printf
			;				// Tänder "nästa" knappen
			;				PORTC.7 = 1;
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   PORTC,7
			;				
			;				while (!PORTB.7)
m012	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS PORTB,7
			;				{
			;					// Do nothing, vänta på att vi släppt knappen
			;				}
	GOTO  m012
			;				
			;				// Sparar värdet, går vidare till nästa plats och resettar lite saker
			;				key[q] = cnt;
	MOVLW 51
	BCF   0x03,RP0
	BCF   0x03,RP1
	ADDWF q,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVF  cnt,W
	MOVWF INDF
			;				q = q + 1;
	INCF  q,1
			;				cnt = 0;
	CLRF  cnt
			;				PORTC.7 = 0;
	BCF   PORTC,7
			;			}
			;		}
			;		
			;		if (q >= 4)
m013	MOVLW 4
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF q,W
	BTFSS 0x03,Carry
	GOTO  m014
			;		{
			;			// Om koden stämmer överens så tänder vi "rätt svar" ledden
			;			if ((code[0] == key[0]) && (code[1] == key[1]) && (code[2] == key[2]) && (code[3] == key[3]))
	MOVF  code,W
	XORWF key,W
	BTFSS 0x03,Zero_
	GOTO  m014
	MOVF  code+1,W
	XORWF key+1,W
	BTFSS 0x03,Zero_
	GOTO  m014
	MOVF  code+2,W
	XORWF key+2,W
	BTFSS 0x03,Zero_
	GOTO  m014
	MOVF  code+3,W
	XORWF key+3,W
	BTFSC 0x03,Zero_
			;			{
			;				PORTC.6 = 1;
	BSF   PORTC,6
			;			}
			;		}
			;		//read encoder new value
			;		
			;		old_new.0 = PORTA.5;  // read rpgA
m014	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   old_new,0
	BTFSC PORTA,5
	BSF   old_new,0
			;		old_new.1 = PORTA.4;  // read rpgB
	BCF   old_new,1
	BTFSC PORTA,4
	BSF   old_new,1
			;		
			;		//printf("OLD_NEW: %b\r\n", old_new);  /* this function call takes time! */
			;
			;		/* compare with transitions in state diagram */
			;		if ((old_new & 0b000000.11) == 0b000000.10)  // from 00 -> 01, forward
	MOVLW 3
	ANDWF old_new,W
	XORLW 2
	BTFSS 0x03,Zero_
	GOTO  m015
			;		{
			;			cnt++;
	INCF  cnt,1
			;			if(cnt == 10)
	MOVF  cnt,W
	XORLW 10
	BTFSC 0x03,Zero_
			;				cnt = 0;
	CLRF  cnt
			;			//printf("HÖGER: %d\r\n", 0);  /* this function call takes time! */
			;		}
			;
			;		if ((old_new & 0b000000.11) == 0b000000.00)
m015	MOVLW 3
	BCF   0x03,RP0
	BCF   0x03,RP1
	ANDWF old_new,W
	BTFSS 0x03,Zero_
	GOTO  m017
			;		{
			;			if(cnt == 0)
	MOVF  cnt,1
	BTFSS 0x03,Zero_
	GOTO  m016
			;				cnt = 10;
	MOVLW 10
	MOVWF cnt
			;			cnt--;
m016	BCF   0x03,RP0
	BCF   0x03,RP1
	DECF  cnt,1
			;			//printf("VÄNSTER: %d\r\n", 0);  /* this function call takes time! */
			;		}
			;		
			;		
			;		
			;		
			;		key[q] = cnt;
m017	MOVLW 51
	BCF   0x03,RP0
	BCF   0x03,RP1
	ADDWF q,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVF  cnt,W
	MOVWF INDF
			;		
			;		/* replace the old values with the new values */
			;		
			;		RS = 0;	 					// LCD i kommandläge
	BCF   0x06,RS
			;		lcd_putchar(0b11000000);	// Kommanderar!
	MOVLW 192
	CALL  lcd_putchar
			;		
			;		RS = 1;						// LCD i skrivläge
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x06,RS
			;		
			;		for(i = 0; i<4; i++)
	CLRF  i
m018	MOVLW 4
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF i,W
	BTFSC 0x03,Carry
	GOTO  m019
			;		{
			;			lcd_putchar(key[i] + 48);
	MOVLW 51
	ADDWF i,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVLW 48
	ADDWF INDF,W
	CALL  lcd_putchar
			;		}
	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i,1
	GOTO  m018
			;		old_cnt = cnt;  /* update oldcnt */
m019	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  cnt,W
	MOVWF old_cnt
			;		
			;		
			;		/*
			;		Testar olika saker, felsökning, tas bort i slutgiltigt program.
			;		*/
			;		
			;		//printf("OLD_NEW: %b\r\n", old_new);
			;		//printf("Count: %d\r\n", cnt);
			;		//printf("Q: %d\r\n", q);
			;		//printf("KEY0: %d\r\n", key[0]);
			;		//printf("------\r\n", 0);
			;		
			;	}
	GOTO  m009
			;}
			;
			;void putchar( char ch )  /* sends one char */
			;{
putchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF ch
			;  char bitCount, ti;
			;  PORTA.0 = 0; /* set startbit */
	BCF   PORTA,0
			;  for ( bitCount = 10; bitCount > 0 ; bitCount-- )
	MOVLW 10
	MOVWF bitCount
m020	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  bitCount,1
	BTFSC 0x03,Zero_
	GOTO  m022
			;   {
			;     /* delay one bit 104 usec at 4 MHz       */
			;     /* 5+18*5-1+1+9=104 without optimization */
			;     ti = 18; do ; while( --ti > 0); nop();
	MOVLW 18
	MOVWF ti
m021	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ ti,1
	GOTO  m021
	NOP  
			;     Carry = 1;     /* stopbit                    */
	BSF   0x03,Carry
			;     ch = rr( ch ); /* Rotate Right through Carry */
	RRF   ch,1
			;     PORTA.0 = Carry;
	BTFSS 0x03,Carry
	BCF   PORTA,0
	BTFSC 0x03,Carry
	BSF   PORTA,0
			;   }
	DECF  bitCount,1
	GOTO  m020
			;  return;
m022	RETURN
			;}
			;
			;void printf(const char *string, char variable)
			;{
printf
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF variable
			;  char i, k, m, a, b;
			;  for(i = 0 ; ; i++)
	CLRF  i_2
			;   {
			;     k = string[i];
m023	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  i_2,W
	ADDWF string,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k
			;     if( k == '\0') break;   // at end of string
	MOVF  k,1
	BTFSC 0x03,Zero_
	GOTO  m037
			;     if( k == '%')           // insert variable in string
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k,W
	XORLW 37
	BTFSS 0x03,Zero_
	GOTO  m035
			;      {
			;        i++;
	INCF  i_2,1
			;        k = string[i];
	MOVF  i_2,W
	ADDWF string,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k
			;        switch(k)
	MOVF  k,W
	XORLW 100
	BTFSC 0x03,Zero_
	GOTO  m024
	XORLW 17
	BTFSC 0x03,Zero_
	GOTO  m027
	XORLW 23
	BTFSC 0x03,Zero_
	GOTO  m028
	XORLW 1
	BTFSC 0x03,Zero_
	GOTO  m032
	XORLW 70
	BTFSC 0x03,Zero_
	GOTO  m033
	GOTO  m034
			;         {
			;           case 'd':         // %d  signed 8bit
			;             if( variable.7 ==1) putchar('-');
m024	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS variable,7
	GOTO  m025
	MOVLW 45
	CALL  putchar
			;             else putchar(' ');
	GOTO  m026
m025	MOVLW 32
	CALL  putchar
			;             if( variable > 127) variable = -variable;  // no break!
m026	MOVLW 128
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF variable,W
	BTFSS 0x03,Carry
	GOTO  m027
	COMF  variable,1
	INCF  variable,1
			;           case 'u':         // %u unsigned 8bit
			;             a = variable/100;
m027	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	MOVWF arg1_5
	CLRF  arg1_5+1
	MOVLW 100
	CALL  _divU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  arg1_5,W
	MOVWF a
			;             putchar('0'+a); // print 100's
	MOVLW 48
	ADDWF a,W
	CALL  putchar
			;             b = variable%100;
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	MOVWF arg1_9
	CLRF  arg1_9+1
	MOVLW 100
	CALL  _remU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF b
			;             a = b/10;
	MOVF  b,W
	MOVWF arg1_5
	CLRF  arg1_5+1
	MOVLW 10
	CALL  _divU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  arg1_5,W
	MOVWF a
			;             putchar('0'+a); // print 10's
	MOVLW 48
	ADDWF a,W
	CALL  putchar
			;             a = b%10;        
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  b,W
	MOVWF arg1_9
	CLRF  arg1_9+1
	MOVLW 10
	CALL  _remU16_8
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF a
			;             putchar('0'+a); // print 1's
	MOVLW 48
	ADDWF a,W
	CALL  putchar
			;             break;
	GOTO  m036
			;           case 'b':         // %b BINARY 8bit
			;             for( m = 0 ; m < 8 ; m++ )
m028	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  m
m029	MOVLW 8
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF m,W
	BTFSC 0x03,Carry
	GOTO  m036
			;              {
			;                if (variable.7 == 1) putchar('1');
	BTFSS variable,7
	GOTO  m030
	MOVLW 49
	CALL  putchar
			;                else putchar('0');
	GOTO  m031
m030	MOVLW 48
	CALL  putchar
			;                variable = rl(variable);
m031	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   variable,1
			;               }
	INCF  m,1
	GOTO  m029
			;              break;
			;           case 'c':         // %c  'char'
			;             putchar(variable);
m032	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	CALL  putchar
			;             break;
	GOTO  m036
			;           case '%':
			;             putchar('%');
m033	MOVLW 37
	CALL  putchar
			;             break;
	GOTO  m036
			;           default:          // not implemented
			;             putchar('!');  
m034	MOVLW 33
	CALL  putchar
			;         }  
			;      }
			;      else putchar(k);
	GOTO  m036
m035	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k,W
	CALL  putchar
			;   }
m036	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i_2,1
	GOTO  m023
			;}
m037	RETURN
			;
			;void update_lcd( void )
			;{
update_lcd
			;// reposition to "line 2" (the next 8 chars)
			;		/*RS = 0;	 // LCD in command-mode
			;		lcd_putchar( 0b11000000 );
			;	
			;		RS = 1;	 // LCD in character-mode
			;*/
			;
			;			/* FUNKTION SOM SKRIVER IN CNT I "LINE 2" */
			;			
			;			
			;			/*
			;			if(t % 2){
			;				PORTC.7 = 1;
			;			}
			;			else
			;				PORTC.7 = 0;
			;			*/
			;			
			;			//printf("Position: %d\r\n", cnt);	/* this function call takes time! */
			;			//lcd_putchar(cnt + 48);
			;			
			;			
			;			
			;			//for(i = 0; i<5; i++)
			;			//	lcd_putchar(' ');
			;}
	RETURN
			;
			;
			;/* *********************************** */
			;/*			  FUNCTIONS				   */
			;/* *********************************** */
			;//* ******************* *//
			;void init( void )
			;{
init
			;  ANSEL =0;		/* not AD-input */
	BCF   0x03,RP0
	BSF   0x03,RP1
	CLRF  ANSEL
			;  TRISA.5 = 1;	/* input rpgA	*/
	BSF   0x03,RP0
	BCF   0x03,RP1
	BSF   TRISA,5
			;  TRISA.4 = 1;	/* input rpgB	*/
	BSF   TRISA,4
			;
			;  /* Enable week pullup's		*/
			;  //OPTION.7 = 0; /* !RABPU bit	  */
			;  //WPUA.5	 = 1; /* rpgA pullup  */
			;  //WPUA.4	 = 1; /* rpgB pullup  */
			;  
			;  TRISC.0=0;
	BCF   TRISC,0
			;  TRISC.1=0;
	BCF   TRISC,1
			;  TRISC.2=0;
	BCF   TRISC,2
			;}
	RETURN
			;
			;
			;
			;void initserial( void )	 /* initialise PIC16F690 bitbang serialcom port */
			;{
initserial
			;   ANSEL.0 = 0; /* No AD on RA0				*/
	BCF   0x03,RP0
	BSF   0x03,RP1
	BCF   ANSEL,0
			;   ANSEL.1 = 0; /* No AD on RA1				*/
	BCF   ANSEL,1
			;   PORTA.0 = 1; /* marking line				*/
	BCF   0x03,RP1
	BSF   PORTA,0
			;   TRISA.0 = 0; /* output to PK2 UART-tool	*/
	BSF   0x03,RP0
	BCF   TRISA,0
			;   TRISA.1 = 1; /* input from PK2 UART-tool */
	BSF   TRISA,1
			;   return;	   
	RETURN
			;}
			;
			;void delay10( char n)
			;/*
			;  Delays a multiple of 10 milliseconds using the TMR0 timer
			;  Clock : 4 MHz	  => period T = 0.25 microseconds
			;  1 IS = 1 Instruction Cycle = 1 microsecond
			;  error: 0.16 percent. B Knudsen.
			;*/
			;{
delay10
	MOVWF n
			;	char i;
			;
			;	OPTION = 7;
	MOVLW 7
	BSF   0x03,RP0
	BCF   0x03,RP1
	MOVWF OPTION_REG
			;	do	{
			;		i = TMR0 + 39; /* 256 microsec * 39 = 10 ms */
m038	MOVLW 39
	BCF   0x03,RP0
	BCF   0x03,RP1
	ADDWF TMR0,W
	MOVWF i_3
			;		while ( i != TMR0)
m039	MOVF  i_3,W
	BCF   0x03,RP0
	BCF   0x03,RP1
	XORWF TMR0,W
	BTFSS 0x03,Zero_
			;			;
	GOTO  m039
			;	} while ( --n > 0);
	DECFSZ n,1
	GOTO  m038
			;}
	RETURN
			;//* ************* *//
			;
			;char text1( char x)	  // this is the way to store a sentence
			;{
text1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF x
			;   skip(x); /* internal function CC5x.	*/
	MOVLW 2
	MOVWF PCLATH
	MOVF  x,W
	ADDWF PCL,1
			;   #pragma return[] = "Code:   "	// 8 chars max!
	RETLW 67
	RETLW 111
	RETLW 100
	RETLW 101
	RETLW 58
	RETLW 32
	RETLW 32
	RETLW 32
			;}
			;
			;char text2( char x)	  // this is the way to store a sentence
			;{
text2
	MOVWF x_2
			;   skip(x); /* internal function CC5x.	*/
	MOVLW 2
	MOVWF PCLATH
	MOVF  x_2,W
	ADDWF PCL,1
			;   #pragma return[] = "		   "	// 8 chars max!
	RETLW 9
	RETLW 9
	RETLW 32
	RETLW 32
	RETLW 32
			;}
			;
			;
			;void lcd_init( void ) // must be run once before using the display
			;{
lcd_init
			;  delay(40);  // give LCD time to settle
	MOVLW 40
	CALL  delay
			;  RS = 0;	  // LCD in command-mode
	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   0x06,RS
			;  lcd_putchar(0b0011.0011); /* LCD starts in 8 bit mode			 */
	MOVLW 51
	CALL  lcd_putchar
			;  lcd_putchar(0b0011.0010); /* change to 4 bit mode				 */
	MOVLW 50
	CALL  lcd_putchar
			;  lcd_putchar(0b00101000);	/* two line (8+8 chars in the row)	 */ 
	MOVLW 40
	CALL  lcd_putchar
			;  lcd_putchar(0b00001100);	/* display on, cursor off, blink off */
	MOVLW 12
	CALL  lcd_putchar
			;  lcd_putchar(0b00000001);	/* display clear					 */
	MOVLW 1
	CALL  lcd_putchar
			;  lcd_putchar(0b00000110);	/* increment mode, shift off		 */
	MOVLW 6
	CALL  lcd_putchar
			;  RS = 1;	 // LCD in character-mode
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x06,RS
			;			 // initialization is done!
			;}
	RETURN
			;
			;
			;void lcd_putchar( char data )
			;{
lcd_putchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF data
			;  // must set LCD-mode before calling this function!
			;  // RS = 1 LCD in character-mode
			;  // RS = 0 LCD in command-mode
			;  // upper Nybble
			;  D7 = data.7;
	BTFSS data,7
	BCF   0x07,D7
	BTFSC data,7
	BSF   0x07,D7
			;  D6 = data.6;
	BTFSS data,6
	BCF   0x07,D6
	BTFSC data,6
	BSF   0x07,D6
			;  D5 = data.5;
	BTFSS data,5
	BCF   0x07,D5
	BTFSC data,5
	BSF   0x07,D5
			;  D4 = data.4;
	BTFSS data,4
	BCF   0x07,D4
	BTFSC data,4
	BSF   0x07,D4
			;  EN = 0;
	BCF   0x06,EN
			;  nop();
	NOP  
			;  EN = 1;
	BSF   0x06,EN
			;  delay(5);
	MOVLW 5
	CALL  delay
			;  // lower Nybble
			;  D7 = data.3;
	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS data,3
	BCF   0x07,D7
	BTFSC data,3
	BSF   0x07,D7
			;  D6 = data.2;
	BTFSS data,2
	BCF   0x07,D6
	BTFSC data,2
	BSF   0x07,D6
			;  D5 = data.1;
	BTFSS data,1
	BCF   0x07,D5
	BTFSC data,1
	BSF   0x07,D5
			;  D4 = data.0;
	BTFSS data,0
	BCF   0x07,D4
	BTFSC data,0
	BSF   0x07,D4
			;  EN = 0;
	BCF   0x06,EN
			;  nop();
	NOP  
			;	EN = 1;
	BSF   0x06,EN
			;  delay(5);
	MOVLW 5
	GOTO  delay
			;}
			;
			;void delay( char millisec)
			;/* 
			;  Delays a multiple of 1 milliseconds at 4 MHz (16F628 internal clock)
			;  using the TMR0 timer 
			;*/
			;{
delay
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF millisec
			;	OPTION = 2;	 /* prescaler divide by 8		 */
	MOVLW 2
	BSF   0x03,RP0
	MOVWF OPTION_REG
			;	do	{
			;		TMR0 = 0;
m040	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  TMR0
			;		while ( TMR0 < 125)	  /* 125 * 8 = 1000	 */
m041	MOVLW 125
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF TMR0,W
	BTFSS 0x03,Carry
			;			;
	GOTO  m041
			;	} while ( -- millisec > 0);
	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ millisec,1
	GOTO  m040
			;}
	RETURN

	END


; *** KEY INFO ***

; 0x0020 P0   28 word(s)  1 % : _divU16_8
; 0x003C P0   27 word(s)  1 % : _remU16_8
; 0x01EB P0   11 word(s)  0 % : init
; 0x01EA P0    1 word(s)  0 % : update_lcd
; 0x01F6 P0   10 word(s)  0 % : initserial
; 0x0270 P0   20 word(s)  0 % : delay
; 0x022C P0   21 word(s)  1 % : lcd_init
; 0x0241 P0   47 word(s)  2 % : lcd_putchar
; 0x0213 P0   15 word(s)  0 % : text1
; 0x0222 P0   10 word(s)  0 % : text2
; 0x0151 P0  153 word(s)  7 % : printf
; 0x0136 P0   27 word(s)  1 % : putchar
; 0x0057 P0  223 word(s) 10 % : main
; 0x0001 P0   31 word(s)  1 % : _const1
; 0x0200 P0   19 word(s)  0 % : delay10

; RAM usage: 29 bytes (16 local), 227 bytes free
; Maximum call level: 3
;  Codepage 0 has  644 word(s) :  31 %
;  Codepage 1 has    0 word(s) :   0 %
; Total of 644 code words (15 %)
