#include <pthread.h>
#include <semaphore.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

// How many babybirds, and how many times they can eat the worms before empty.
#define CONSUMERS 10
#define WORMS 10

void *Producer();
void *Consumer(void *);

// Threads for the baby birds and the parent.
pthread_t babyBird[10];
pthread_t parentBird;
// Semaphores used
sem_t empty, full;
int worms = WORMS;

int main() {
    // Init some stuff
    int i;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    // Init the Semaphores, in the beginning the basket is full.
    sem_init(&empty, 0, 0);
    sem_init(&full, 0, 1);

    srand(time(NULL));

    printf("Semaphores inited. %d\n");

    // Create threads. Join them.
    pthread_create(&parentBird, &attr, Producer,  NULL);

    for (i = 0; i <= CONSUMERS; i++)
        pthread_create(&babyBird[i], &attr, Consumer, (void *) i);

    printf("All threads created.\n");

    for (i = 0; i <= CONSUMERS; i++)
        pthread_join(babyBird[i], NULL);

    pthread_join(parentBird, NULL);

    printf("All threads joined and done.\n");

    return 0;
}

void *Producer() {
    while (1)
    {
        // Print some interesting stuff.
        // Wait for the bowl to be empty, gather more worms, post that it's now full.
        printf("[PARENT] Waiting for empty bowl.\n");
        sem_wait(&empty);
        printf("[PARENT] Bowl is empty, filling up.\n");
        worms = WORMS;
        printf("[PARENT] Bowl filled, 'full' called.\n");
        sem_post(&full);
    }
}

void *Consumer(void *arg) {
    int myID = (int) arg;
    while (1)
    {
        // Print some interesting stuff, wait for the bowl to be full (rather it's like "notEmpty")
        // Eat, if still worms, tell the others im done, sleep for a bit and repeat.
        // If empty bowl, SCREAM!
        sem_wait(&full);
        worms = worms - 1;
        printf("[CHILD %d] Bowl NOT empty, eating 1. %d LEFT.\n", myID, worms);

        if (worms > 0)
        {
            sem_post(&full);
            int sleepTime = rand() % 10;
            printf("[CHILD %d] Sleep for %d seconds.\n", myID, sleepTime);
            sleep(sleepTime);
        }
        else
        {
            printf("[CHILD %d] Bowl EMPTY! Scream!\n", myID);
            sem_post(&empty);
        }
    }
}

