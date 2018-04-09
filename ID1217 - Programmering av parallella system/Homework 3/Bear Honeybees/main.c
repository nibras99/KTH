#include <pthread.h>
#include <semaphore.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

// How many bees, and the size of the honeypot.
#define PRODUCERS 10
#define HONEYPOT_SIZE 10

void *Producer(void *);
void *Consumer();

// Threads for the bees and the bear.
pthread_t honeyBee[10];
pthread_t bear;

// Semaphores used.
sem_t notFull, full;
int pot = 0;

int main() {
    // Init some stuff.
    int i;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    // Semaphores used, in the beginning it's empty (notFull).
    sem_init(&notFull, 0, 1);
    sem_init(&full, 0, 0);

    srand(time(NULL));

    printf("Semaphores inited. %d\n");

    // Create threads, join them.
    pthread_create(&bear, &attr, Consumer,  NULL);

    for (i = 0; i <= PRODUCERS; i++)
        pthread_create(&honeyBee[i], &attr, Producer, (void *) i);

    printf("All threads created.\n");

    for (i = 0; i <= PRODUCERS; i++)
        pthread_join(honeyBee[i], NULL);

    pthread_join(bear, NULL);

    printf("All threads joined and done.\n");

    return 0;
}

void *Producer(void *arg) {
    int myID = (int) arg;
    while (1)
    {
        // If the pot is not full (bear ate everything).
        // Get the semaphore, gather one. If still not full, tell other bees to continue, sleep, and repeat.
        // If full, tell bear to go ahead and eat!
        sem_wait(&notFull);
        pot = pot + 1;
        printf("[BEE %d] Bowl NOT full, producing 1. Currently %d.\n", myID, pot);

        if (pot < HONEYPOT_SIZE)
        {
            sem_post(&notFull);

            int sleepTime = rand() % 10;
            printf("[BEE %d] Sleep for %d before returning to work.\n", myID, sleepTime);
            sleep(sleepTime);
        }
        else
        {
            printf("[BEE %d] Bowl FULL! Go eat Bear!\n", myID);

            sem_post(&full);
        }
    }
}

void *Consumer() {
    while (1)
    {
        // If pot is full, eat it all!
        // Tell those bees to better go get some more for this hungry bear.
        printf("[BEAR] Waiting for full bowl.\n");
        sem_wait(&full);
        printf("[BEAR] Bowl is full, eating it all!\n");
        pot = 0;
        printf("[BEAR] Bowl eaten, calling 'notFull'.\n");
        sem_post(&notFull);
    }
}

