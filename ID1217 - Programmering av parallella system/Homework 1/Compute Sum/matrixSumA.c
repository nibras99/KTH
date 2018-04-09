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
#define MAXWORKERS 500   /* maximum number of workers */

//#define DEBUG

pthread_mutex_t barrier;  /* mutex lock for the barrier */
pthread_cond_t go;        /* condition variable for leaving */
int numWorkers;           /* number of workers */
int numArrived = 0;       /* number who have arrived */

/* a reusable counter barrier */
void Barrier() {
    pthread_mutex_lock(&barrier);
    numArrived++;
    if (numArrived == numWorkers) {
        numArrived = 0;
        pthread_cond_broadcast(&go);
    } else
        pthread_cond_wait(&go, &barrier);
    pthread_mutex_unlock(&barrier);
}

/* timer */
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
int sums[MAXWORKERS]; /* partial sums */

// Partial mins, maxs and their positions etc, saved just like the partial sums.
int mins[MAXWORKERS];
int minsI[MAXWORKERS];
int minsJ[MAXWORKERS];
int maxs[MAXWORKERS];
int maxsI[MAXWORKERS];
int maxsJ[MAXWORKERS];

int matrix[MAXSIZE][MAXSIZE]; /* matrix */

// Final, global min, max, and their positions in the matrix.
int min = -1, max = -1, minI, minJ, maxI, maxJ;

void *Worker(void *);

/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
    printf("Running program A\n");
    int i, j;
    long l; /* use long in case of a 64-bit system */
    pthread_attr_t attr;

    // Create MAXWORKERS threads.
    pthread_t workerid[MAXWORKERS];

    /* set global thread attributes */
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    /* initialize mutex and condition variable */
    pthread_mutex_init(&barrier, NULL);
    pthread_cond_init(&go, NULL);

    /* read command line args if any */
    size = (argc > 1) ? atoi(argv[1]) : MAXSIZE;
    numWorkers = (argc > 2) ? atoi(argv[2]) : MAXWORKERS;
    if (size > MAXSIZE) size = MAXSIZE;
    if (numWorkers > MAXWORKERS) numWorkers = MAXWORKERS;
    stripSize = size / numWorkers;

    /* initialize the matrix, with random values */
    srand(time(NULL));
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
    pthread_exit(NULL);
}

/* Each worker sums the values in one strip of the matrix.
   After a barrier, worker(0) computes and prints the total */
void *Worker(void *arg) {
    long myid = (long) arg;
    int total, i, j, first, last;

    // Set the min and max for this worker to -1, since that value is not used.
    mins[myid] = -1;
    maxs[myid] = -1;

#ifdef DEBUG
    printf("worker %d (pthread id %d) has started\n", myid, pthread_self());
#endif

    /* determine first and last rows of my strip */
    first = myid * stripSize;
    last = (myid == numWorkers - 1) ? (size - 1) : (first + stripSize - 1);

    /* sum values in my strip */
    for (i = first; i <= last; i++) {
        for (j = 0; j < size; j++) {
            sums[myid] += matrix[i][j];

            // Simply check if its lower (or higher) than current min/max (or -1) and if so, replace it and the positions.
            if (matrix[i][j] < mins[myid] || mins[myid] == -1) {
                mins[myid] = matrix[i][j];
                minsI[myid] = i;
                minsJ[myid] = j;
            }

            if (matrix[i][j] > maxs[myid] || maxs[myid] == -1) {
                maxs[myid] = matrix[i][j];
                maxsI[myid] = i;
                maxsJ[myid] = j;
            }
        }
    }

    Barrier(); // Waits for all threads to be done.

    // Final calculations to puth everything together.
    if (myid == 0) {
        int total = 0;
        for (i = 0; i < numWorkers; i++) {
            total += sums[i];
            if ((mins[i] < min || min == -1) && mins[i] != -1) {
                min = mins[i];
                minI = minsI[i];
                minJ = minsJ[i];
            }

            if ((maxs[i] > max || max == -1) && maxs[i] != -1) {
                max = maxs[i];
                maxI = maxsI[i];
                maxJ = maxsJ[i];
            }
        }

        // Print it all.
        end_time = read_timer();
        printf("The total is %d\n", total);
        printf("The execution time is %g sec\n", end_time - start_time);
        printf("Min %d at I = %d, J = %d \n", min, minI, minJ);
        printf("Max %d at I = %d, J = %d \n", max, maxI, maxJ);
    }
}
