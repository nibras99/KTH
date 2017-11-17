#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h"

int getsw(void)
{
    return ((PORTD & 0x0200) >> 9);		// Maskar allt utom de intressanta bitarna för SW1 -> SW4, vilket är bitarna 8 -> 11, shiftar höger 8 bitar så att dem hamnar på LSB
}

int getbtns(void)
{
	return ((PORTD & 0x00E0) >> 5);		// Maskar allt utom de intressanta bitarna för BTN2 -> BTN4, vilket är bitarna 5 -> 7, shiftar höger 5 bitar så att dem hamnar på LSB
}
