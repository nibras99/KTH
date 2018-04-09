#include <iostream>
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

#define CONSUMERS 10
#define WORMS 10

using namespace std;

void *Producer(void *);

void *Consumer(void *);

/*
I would argue that the program is not intended or built to be fair, but kind of is anyway.
The pthread methods/functions etc guarantees not fairness, but the randomization of how long each
babyBird sleeps will make it so not all birds are waiting on the cond at the same time.
That together with infinity in time will (most likely) make the program "fair".
*/

pthread_t babyBird[10];
pthread_t parentBird;
int worms = WORMS;

class Monitor {
public:
    Monitor();

    // All using the same lock since they all modify the same INT "pot".
    // Different cond_wait since they are waiting on either to produce
    // Or to eat it.
    void wait_empty() {
        pthread_mutex_lock(&lock);
        while (countEmpty == 0)
            pthread_cond_wait(&empty, &lock);
        --countEmpty;
        pthread_mutex_unlock (&lock);
    }

    void wait_notEmpty() {
        pthread_mutex_lock(&lock);
        while (countNotEmpty == 0)
            pthread_cond_wait(&empty, &lock);
        --countNotEmpty;
        pthread_mutex_unlock(&lock);
    }

    void post_empty() {
        pthread_mutex_lock (&lock);

        if (countEmpty == 0)
            pthread_cond_broadcast(&empty);
        ++countEmpty;

        pthread_mutex_unlock (&lock);
    }

    void post_notEmpty() {
        pthread_mutex_lock (&lock);

        if (countNotEmpty == 0)
            pthread_cond_broadcast(&notEmpty);
        ++countNotEmpty;

        pthread_mutex_unlock (&lock);
    }
private:
    unsigned int countEmpty;
    unsigned int countNotEmpty;
    pthread_mutex_t lock;
    pthread_cond_t empty;
    pthread_cond_t notEmpty;
};

Monitor::Monitor() {
    countEmpty = 0;
    countNotEmpty = 1;
    pthread_mutex_init(&lock, 0);
    pthread_cond_init(&empty, 0);
    pthread_cond_init(&notEmpty, 0);
}

Monitor mon;
int main() {
    int i;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    srand(time(NULL));

    pthread_create(&parentBird, &attr, Producer, NULL);
    for (i = 0; i <= CONSUMERS; i++)
        pthread_create(&babyBird[i], &attr, Consumer, (void *) i);

    printf("All threads created. \n");

    for (i = 0; i <= CONSUMERS; i++)
        pthread_join(babyBird[i], NULL);

    pthread_join(parentBird, NULL);

    printf("All threads joined (and done). \n");

    return 0;
}

void *Producer(void *arg) {
    while (1)
    {
        // Print some interesting stuff.
        // Wait for the bowl to be empty, gather more worms,
        // tell mon it's now full.
        printf("[PARENT] Waiting for empty bowl.\n");
        mon.wait_empty();
        printf("[PARENT] Bowl is empty, filling up.\n");
        worms = WORMS;
        printf("[PARENT] Bowl filled, 'full' called.\n");
        mon.post_notEmpty();
    }
}

void *Consumer(void *arg) {
    int myID = *((int*)(&arg));
    while (1)
    {
        // Print some interesting stuff, wait for the bowl to be full (rather it's like "notEmpty")
        // Eat, if still worms, tell the others im done, sleep for a bit and repeat.
        // If empty bowl, SCREAM!
        mon.wait_notEmpty();
        worms = worms - 1;
        printf("[CHILD %d] Bowl NOT empty, eating 1. %d LEFT.\n", myID, worms);

        if (worms > 0)
        {
            mon.post_notEmpty();
            int sleepTime = rand() % 10;
            printf("[CHILD %d] Sleep for %d seconds.\n", myID, sleepTime);
            sleep(sleepTime);
        }
        else
        {
            printf("[CHILD %d] Bowl EMPTY! Scream!\n", myID);
            mon.post_empty();
        }
    }
}
