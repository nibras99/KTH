/*
I och med att vanliga standard rand() inte funkar i denna dator och att det �r lite sm�pillig med att introducera den
pga att man m�ste ge den ett slumpat v�rde s� skapade vi en egen "random" funktion med hj�lp av en googling!
Den �r inte helt random, det kommer ske i samma ordning samma g�ng man startar programmet, men det �r bra nog
f�r att inte enkelt kunna komma ih�g vilken order som kommer d�rn�st
*/

unsigned short lfsr = 0xACE1u;
unsigned bit;

unsigned randomizer()
{
    bit  = ((lfsr >> 0) ^ (lfsr >> 2) ^ (lfsr >> 3) ^ (lfsr >> 5) ) & 1;
    return lfsr =  (lfsr >> 1) | (bit << 15);
}
