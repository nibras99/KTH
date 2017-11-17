#include <stdio.h>
#include <stdlib.h>

char *text1 = "This is a string.";//111111111111111111111111111111111111111"; // F�r plats med EXAKT 80 chars. L�gg till 1st 1:a s� komemr programmet att krascha!
char *text2 = "Yet another thing."; // Dessa tv� text1 och text tv� �r pointers som pekar till ineh�llet i dem.

int *list1; // Skapar en global pointer variabel
int *list2; // Skapar en global pointer variabel

int count = 0; // Skapar en simpel int som h�ller reda p� hur m�nga chars vi r�knar.

void work(); // Initierar, cause C.
void copycodes(); // Initierar, cause C.

void work() // Allt som g�rs i "work" i MIPS �r att ge alla listor etc adressv�rden, detta �r dock redan gjort som globala variablar s� det som �terst�r �r att calla p� copycodes.
{
    copycodes(&count, list1, text1); // Ropar p� copycodes, skickar med list1 som inneh�ller 4 * 80 "tomma" platser och text1 som inneh�ller stringsen med texten.
    copycodes(&count, list2, text2); //
}

void copycodes(int *count2, int *list, char *text)
{
    while (*text) // Medans text INTE �r NULL. Allts� fram tills vi inte n�tt slutet av str�ngen.
    {
        *list = *text; // Kopiera �ver inneh�llet i text's minnesadress till list's minnesadress.
        *text++; // �kar text's minnesplats med 1, s� att vi �r redo f�r n�sta v�rde.
        *list++; // �kar list's minnesplats med 1, s� att vi �r redo f�r n�sta v�rde.

        (*count2)++; // Vi har nu r�knat "1" char till, s� �ka count med 1.
    }
    *list = NULL; // Vi avslutar med att s�tta sista platsen i nya minnet lika med NULL, s� att loopen print sedan kommer sluta n�r den ska sluta.
}

void printlist(const int* lst){
    printf("ASCII codes and corresponding characters.\n");
    while(*lst != 0)
    {
        printf("0x%03X '%c' ", *lst, (char)*lst);
        lst++;
    }
    printf("\n");
}

void endian_proof(const char* c){
    printf("\nEndian experiment: 0x%02x,0x%02x,0x%02x,0x%02x\n",
    (int)*c,(int)*(c+1), (int)*(c+2), (int)*(c+3));

}

int main(void){
    list1 = malloc(sizeof(int) * 80); // Vi m�ste ju ge list1 "plats" n�gonstans, b�sta platsen att g�ra det �r h�r d� det ej g�r i det globala "omr�det".
    list2 = malloc(sizeof(int) * 80); // Dessa tv� f�r vardera 80 platser med 4 byte var, 4 byte per int allts� plats f�r 80 int's.

    work(); // Ropar p� work.

    printf("\nlist1: ");
    printlist(list1);
    printf("\nlist2: ");
    printlist(list2);
    printf("\nCount = %d\n", count);

    endian_proof((char*) &count);

    free(list1); // N�r det �r slut och vi inte l�ngre beh�ver det s� friar vi upp allt.
    free(list2); // N�r det �r slut och vi inte l�ngre beh�ver det s� friar vi upp allt.
}
