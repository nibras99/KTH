#include <pic32mx.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>

#define DISPLAY_VDD PORTFbits.RF6
#define DISPLAY_VBATT PORTFbits.RF5
#define DISPLAY_COMMAND_DATA PORTFbits.RF4
#define DISPLAY_RESET PORTGbits.RG9
#define DISPLAY_VDD_PORT PORTF
#define DISPLAY_VDD_MASK 0x40
#define DISPLAY_VBATT_PORT PORTF
#define DISPLAY_VBATT_MASK 0x20
#define DISPLAY_COMMAND_DATA_PORT PORTF
#define DISPLAY_COMMAND_DATA_MASK 0x10
#define DISPLAY_RESET_PORT PORTG
#define DISPLAY_RESET_MASK 0x200
#define DISPLAY_CHANGE_TO_COMMAND_MODE (PORTFCLR = 0x10)
#define DISPLAY_CHANGE_TO_DATA_MODE (PORTFSET = 0x10)

void delay(int cyc);
uint8_t spi_send_recv(uint8_t data);

void display_update_matrix();
void reset_matrix(void);
void update_matrix_32(char newMatrix[32][32], int pos);
void update_matrix_128(char newMatrix[128][32]);
int initiate(void);

int getButtonPress(void);
int getSwitches(void);

unsigned randomizer();
int randomizeNextOrder(void);

void printOrder(int currentValue, int place);

char textbuffer[4][16];
char display_matrix[128][32];

char kaffe[32][32];
char teMjolkSocker[32][32];
char kaffeSocker[32][32];
char kaffeMjolkSocker[32][32];
char kaffeMjolk[32][32];
char te[32][32];
char teSocker[32][32];
char teMjolk[32][32];
char welcomeNoText[128][32];
char welcomeWithText[128][32];
