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

#define MAXSIZE 20  /* maximum matrix size */
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
int size, stripSize;  /* assume size is multiple of numWorkers */
int matrix[MAXSIZE][MAXSIZE]; /* matrix */

// Global min, max and their positions. Also global total sum of all.
int min = -1, max = -1, minI, minJ, maxI, maxJ, totalSum;
// Mutex lock for when global calculations are being made.
pthread_mutex_t calc;

void *Worker(void *);

/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
    printf("Running program B \n");
    int i, j;
    long l; /* use long in case of a 64-bit system */
    pthread_attr_t attr;
    pthread_t workerid[MAXWORKERS];

    /* set global thread attributes */
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    /* initialize mutex and condition variable */
    pthread_mutex_init(&calc, NULL);

    /* read command line args if any */
    size = (argc > 1) ? atoi(argv[1]) : MAXSIZE;
    numWorkers = (argc > 2) ? atoi(argv[2]) : MAXWORKERS;
    if (size > MAXSIZE) size = MAXSIZE;
    if (numWorkers > MAXWORKERS) numWorkers = MAXWORKERS;
    stripSize = size / numWorkers;

    // Seedar så vi inte får samma skit så ofta.
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

    // Join all threads so that we wait for them all to finish, before printing.
    for (l = 0; l < numWorkers; l++)
        pthread_join(workerid[l], NULL);

    // All threads done, print.
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
    int i, j, first, last;
    // Since we cant use arrays, I created local variables for the needed things instead.
    // Maybe not a good solution either, could use the global ones and simply lock all the time.
    // But what is really the point of threads then?
    int myMin = -1, myMax = -1, myMinI, myMinJ, myMaxI, myMaxJ, localSum;

#ifdef DEBUG
    printf("worker %d (pthread id %d) has started\n", myid, pthread_self());
#endif

    /* determine first and last rows of my strip */
    first = myid * stripSize;
    last = (myid == numWorkers - 1) ? (size - 1) : (first + stripSize - 1);

    // Calculates local sum, min and max for this thread, through all its rows.
    localSum = 0;
    for (i = first; i <= last; i++) {
        for (j = 0; j < size; j++) {
            localSum += matrix[i][j];

            // We could replace all this and their local variables with a lock in this for loop instead.
            // But that would be bad imo, due to all threads needing to operate on the same variables all the time.

            if (matrix[i][j] < myMin || myMin == -1) {
                myMin = matrix[i][j];
                myMinI = i;
                myMinJ = j;
            }

            if (matrix[i][j] > myMax || myMax == -1) {
                myMax = matrix[i][j];
                myMaxI = i;
                myMaxJ = j;
            }
        }
    }

    // Put everything together into the global variables. Since they are global and shared, need to lock.
    pthread_mutex_lock(&calc);
    totalSum += localSum;

    // If some threads doesnt get any job done, it will have -1 as min/max, so we skip that!
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
    // All done, unlock and return (or pthread_exit())
    pthread_mutex_unlock(&calc);

    return 0;
}
