/*
I och med att vanliga standard rand() inte funkar i denna dator och att det är lite småpillig med att introducera den
pga att man måste ge den ett slumpat värde så skapade vi en egen "random" funktion med hjälp av en googling!
Den är inte helt random, det kommer ske i samma ordning samma gång man startar programmet, men det är bra nog
för att inte enkelt kunna komma ihåg vilken order som kommer därnäst
*/

unsigned short lfsr = 0xACE1u;
unsigned bit;

unsigned randomizer()
{
    bit  = ((lfsr >> 0) ^ (lfsr >> 2) ^ (lfsr >> 3) ^ (lfsr >> 5) ) & 1;
    return lfsr =  (lfsr >> 1) | (bit << 15);
}
