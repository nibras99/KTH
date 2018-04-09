/* matrix summation using pthreads

   features: uses a barrier; the Worker[0] computes
             the total sum from partial sums computed by Workers
             and prints the total sum to the standard output

   usage under Linux:
     gcc matrixSum.c -lpthread
     a.out size numWorkers

*/
#ifndef _REENTRANT
#define _REENTRANT
#endif

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>

#define MAXSIZE 10  /* maximum matrix size */
#define MAXWORKERS 5   /* maximum number of workers */

#define DEBUG

int numWorkers;           /* number of workers */

double read_timer() {
    static bool initialized = false;
    static struct timeval start;
    struct timeval end;
    if (!initialized) {
        gettimeofday(&start, NULL);
        initialized = true;
    }
    gettimeofday(&end, NULL);
    return (end.tv_sec - start.tv_sec) + 1.0e-6 * (end.tv_usec - start.tv_usec);
}

double start_time, end_time; /* start and end times */
int size;  /* assume size is multiple of numWorkers */
int matrix[MAXSIZE][MAXSIZE]; /* matrix */

// Global variables in use.
int min = -1, max = -1, minI, minJ, maxI, maxJ, totalSum = 0, nextRow = 0;
// Locks for when to merge calculations, and when a need to get a row.
pthread_mutex_t calc;
pthread_mutex_t getRow;

void *Worker(void *);

/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
    printf("Running program C \n");
    int i, j;
    long l; /* use long in case of a 64-bit system */
    pthread_attr_t attr;
    pthread_t workerid[MAXWORKERS];

    /* set global thread attributes */
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    /* initialize mutex and condition variable */
    pthread_mutex_init(&calc, NULL);
    pthread_mutex_init(&getRow, NULL);

    /* read command line args if any */
    size = (argc > 1) ? atoi(argv[1]) : MAXSIZE;
    numWorkers = (argc > 2) ? atoi(argv[2]) : MAXWORKERS;
    if (size > MAXSIZE) size = MAXSIZE;
    if (numWorkers > MAXWORKERS) numWorkers = MAXWORKERS;

    srand(time(NULL));
    /* initialize the matrix */
    for (i = 0; i < size; i++) {
        for (j = 0; j < size; j++) {
            matrix[i][j] = rand() % 99;
        }
    }

    /* print the matrix */
#ifdef DEBUG
    for (i = 0; i < size; i++) {
        printf("%d [ ", i);
        for (j = 0; j < size; j++) {
            printf(" %d", matrix[i][j]);
        }
        printf(" ]\n");
    }
#endif

    /* do the parallel work: create the workers */
    start_time = read_timer();
    for (l = 0; l < numWorkers; l++)
        pthread_create(&workerid[l], &attr, Worker, (void *) l);

    // Join all threads so that we wait for them to be done, before presenting data.
    for (l = 0; l < numWorkers; l++)
        pthread_join(workerid[l], NULL);

    // Alla trådar / ungar klar. Printa svaren!
    end_time = read_timer();
    printf("The total is %d\n", totalSum);
    printf("The execution time is %g sec\n", end_time - start_time);
    printf("Min %d at I = %d, J = %d \n", min, minI, minJ);
    printf("Max %d at I = %d, J = %d \n", max, maxI, maxJ);

    return 0;
}

/* Each worker sums the values in one strip of the matrix.
   After a barrier, worker(0) computes and prints the total */
void *Worker(void *arg) {
    long myid = (long) arg;
    // Local variables, explained why this way in matrixSumB.
    int j, myMin = -1, myMax = -1, myMinI, myMinJ, myMaxI, myMaxJ, localSum = 0, rowToWork = 0;

#ifdef DEBUG
    printf("worker %d (pthread id %d) has started\n", myid, pthread_self());
#endif

    /* determine first and last rows of my strip */

    localSum = 0;
    while (true) {
        // While we can, let the thread get a new row to work on.
        // As long as we havent used them all, give it one.
        // Else we break the while loop.
        pthread_mutex_lock(&getRow);
        if (nextRow != size) {
            rowToWork = nextRow;
            nextRow++;
        } else {
            pthread_mutex_unlock(&getRow);
            break;
        }
        pthread_mutex_unlock(&getRow);

        // Calculate localSum, min, max and their posses, for this thread.
        for (j = 0; j < size; j++) {
            localSum += matrix[rowToWork][j];

            if (matrix[rowToWork][j] < myMin || myMin == -1) {
                myMin = matrix[rowToWork][j];
                myMinI = rowToWork;
                myMinJ = j;
            }

            if (matrix[rowToWork][j] > myMax || myMax == -1) {
                myMax = matrix[rowToWork][j];
                myMaxI = rowToWork;
                myMaxJ = j;
            }
        }
    }

    // All calculations for this thread done, merge it all together.
    pthread_mutex_lock(&calc);
    totalSum += localSum;

    // Om vissa threads inte hinner få ett jobb, så kan dem råka ha myMin = -1, no good
    if ((myMin < min || min == -1) && myMin != -1) {
        min = myMin;
        minI = myMinI;
        minJ = myMinJ;
    }

    if ((myMax > max || max == -1) && myMax != -1) {
        max = myMax;
        maxI = myMaxI;
        maxJ = myMaxJ;
    }
    pthread_mutex_unlock(&calc);

    // Return!
    return 0;
}
