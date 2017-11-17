#include <stdio.h>
#include <stdlib.h>

char *text1 = "This is a string.";//111111111111111111111111111111111111111"; // Får plats med EXAKT 80 chars. Lägg till 1st 1:a så komemr programmet att krascha!
char *text2 = "Yet another thing."; // Dessa två text1 och text två är pointers som pekar till inehållet i dem.

int *list1; // Skapar en global pointer variabel
int *list2; // Skapar en global pointer variabel

int count = 0; // Skapar en simpel int som håller reda på hur många chars vi räknar.

void work(); // Initierar, cause C.
void copycodes(); // Initierar, cause C.

void work() // Allt som görs i "work" i MIPS är att ge alla listor etc adressvärden, detta är dock redan gjort som globala variablar så det som återstår är att calla på copycodes.
{
    copycodes(&count, list1, text1); // Ropar på copycodes, skickar med list1 som innehåller 4 * 80 "tomma" platser och text1 som innehåller stringsen med texten.
    copycodes(&count, list2, text2); //
}

void copycodes(int *count2, int *list, char *text)
{
    while (*text) // Medans text INTE är NULL. Alltså fram tills vi inte nått slutet av strängen.
    {
        *list = *text; // Kopiera över innehållet i text's minnesadress till list's minnesadress.
        *text++; // Ökar text's minnesplats med 1, så att vi är redo för nästa värde.
        *list++; // Ökar list's minnesplats med 1, så att vi är redo för nästa värde.

        (*count2)++; // Vi har nu räknat "1" char till, så öka count med 1.
    }
    *list = NULL; // Vi avslutar med att sätta sista platsen i nya minnet lika med NULL, så att loopen print sedan kommer sluta när den ska sluta.
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
    list1 = malloc(sizeof(int) * 80); // Vi måste ju ge list1 "plats" någonstans, bästa platsen att göra det är här då det ej går i det globala "området".
    list2 = malloc(sizeof(int) * 80); // Dessa två får vardera 80 platser med 4 byte var, 4 byte per int alltså plats för 80 int's.

    work(); // Ropar på work.

    printf("\nlist1: ");
    printlist(list1);
    printf("\nlist2: ");
    printlist(list2);
    printf("\nCount = %d\n", count);

    endian_proof((char*) &count);

    free(list1); // När det är slut och vi inte längre behöver det så friar vi upp allt.
    free(list2); // När det är slut och vi inte längre behöver det så friar vi upp allt.
}
