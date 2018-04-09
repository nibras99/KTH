#include <iostream>
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

#define PRODUCERS 10
#define HONEYPOT_SIZE 10

using namespace std;

void *Producer(void *);

void *Consumer(void *);

/*
I would argue that the program is not intended or built to be fair, but kind of is anyway.
The pthread methods/functions etc guarantees not fairness, but the randomization of how long each
honeyBee sleeps will make it so not all bees are waiting on the cond at the same time.
That together with infinity in time will (most likely) make the program "fair".
*/

pthread_t honeyBee[10];
pthread_t bear;
int pot = 0;

class Monitor {
public:
    Monitor();

    // All using the same lock since they all modify the same INT "pot".
    // Different cond_wait since they are waiting on either to produce
    // Or to eat it.
    void wait_full() {
        pthread_mutex_lock(&lock);
        while (countFull == 0)
            pthread_cond_wait(&full, &lock);
        --countFull;
        pthread_mutex_unlock(&lock);
    }

    void wait_notFull() {
        pthread_mutex_lock(&lock);
        while (countNotFull == 0)
            pthread_cond_wait(&notFull, &lock);
        --countNotFull;
        pthread_mutex_unlock(&lock);
    }

    void post_full() {
        pthread_mutex_lock (&lock);

        if (countFull == 0)
            pthread_cond_broadcast(&full);
        ++countFull;

        pthread_mutex_unlock(&lock);
    }

    void post_notFull() {
        pthread_mutex_lock (&lock);

        if (countNotFull == 0)
            pthread_cond_broadcast(&notFull);
        ++countNotFull;

        pthread_mutex_unlock (&lock);
    }
private:
    unsigned int countFull;
    unsigned int countNotFull;
    pthread_mutex_t lock;
    pthread_cond_t full;
    pthread_cond_t notFull;
};

Monitor::Monitor() {
    countFull = 0;
    countNotFull = 1;
    pthread_mutex_init(&lock, 0);
    pthread_cond_init(&full, 0);
    pthread_cond_init(&notFull, 0);
}

Monitor mon;
int main() {
    int i;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    srand(time(NULL));

    pthread_create(&bear, &attr, Consumer, NULL);
    for (i = 0; i <= PRODUCERS; i++)
        pthread_create(&honeyBee[i], &attr, Producer, (void *) i);

    printf("All threads created. \n");

    for (i = 0; i <= PRODUCERS; i++)
        pthread_join(honeyBee[i], NULL);

    pthread_join(bear, NULL);

    printf("All threads joined (and done). \n");

    return 0;
}

void *Producer(void *arg) {
    int myID = *((int*)(&arg));
    while (1)
    {
        // Wait for the pot to not be full, produce,
        // check status and call monitor depending on what to do.
        mon.wait_notFull();
        pot = pot + 1;
        printf("[BEE %d] Bowl NOT full, producing 1. Currently %d.\n", myID, pot);

        if (pot < HONEYPOT_SIZE)
        {
            mon.post_notFull();
        }
        else
        {
            printf("[BEE %d] Bowl FULL! Go eat Bear!\n", myID);
            mon.post_full();
        }

        int sleepTime = rand() % 10;
        printf("[BEE %d] Sleep for %d before returning to work.\n", myID, sleepTime);
        sleep(sleepTime);
    }
}

void *Consumer(void *arg) {
    while (1)
    {
        // If pot is full, eat it all!
        // Tell those bees to better go get some more for this hungry bear.
        printf("[BEAR] Waiting for full bowl.\n");
        mon.wait_full();
        printf("[BEAR] Bowl is full, eating it all!\n");
        pot = 0;
        printf("[BEAR] Bowl eaten, calling 'notFull'.\n");
        mon.post_notFull();
    }
}
