// Number of randomly created elements to sort
#define ELEMENTS 10000

#ifndef _REENTRANT
#define _REENTRANT
#endif

//#define DEBUG

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>

// Normal timer provided
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

// Struct to be able to send multiple arguments to pthread_create
struct arg_struct {
    int low;
    int high;
};
typedef struct arg_struct Arg_struct;

void *quickSort(void *args);
int partition(int, int);
void swap(int *, int *);

// Global array.
int a[ELEMENTS];
double start_time, end_time;

int main() {
    srand(time(NULL));
    for (int i = 0; i < ELEMENTS; i++)
        a[i] = rand() % 99;

#ifdef DEBUG
    printf("\nUnsorted array: \n");
    for (int i = 0; i < ELEMENTS; i++)
        printf(" %d ", a[i]);
#endif

    // The first created thread / child that starts all calculations.
    pthread_t first;

    // Som attributes set.
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

    // Init the struct for the first thread
    struct arg_struct args;
    args.low = 0;
    args.high = ELEMENTS - 1;

    // All init done, start timer
    start_time = read_timer();

    // Create thread, join it so we wait for all calculations.
    pthread_create(&first, &attr, quickSort, (void *) &args);
    pthread_join(first, NULL);

    // All done, stop timer, print time.
    end_time = read_timer();
    printf("The execution time is %g sec\n", end_time - start_time);

#ifdef DEBUG
    printf("\nSorted array: \n");
    for (int i = 0; i < ELEMENTS; ++i)
        printf(" %d ", a[i]);
#endif
    return 0;
}

// The quickSort "threads".
void *quickSort(void *arguments) {
    //printf("\nStarted new thread. ('Split' of QS array)");

    /*
     The threads that it will be split into. Not really
     Not really efficient, since the parent (this thread) will just be waiting, instead of doing "half" of the calculations itself.
     */
    pthread_t left;
    pthread_t right;

    // The received arguments.
    struct arg_struct *args = (struct arg_struct *) arguments;

    // As long the min is lower than high, that means we have more elements to split.
    if (args->low < args->high)
    {
        // Do the QS partition, get the returned partition index.
        int index = partition(args->low, args->high);

        pthread_attr_t attr;
        pthread_attr_init(&attr);
        pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

        // Args for the new LEFT thread.
        struct arg_struct argsLeft;
        argsLeft.low = args->low;
        argsLeft.high = index - 1;

        // Args for the new RIGHT thread.
        struct arg_struct argsRight;
        argsRight.low = index + 1;
        argsRight.high = args->high;

        // Create both threads with their respective arguments.
        pthread_create(&left, &attr, quickSort, (void *) &argsLeft);
        pthread_create(&right, &attr, quickSort, (void *) &argsRight);

        // Join them, and wait before returning to parent.
        pthread_join(left, NULL);
        pthread_join(right, NULL);
    }

    // Return thread once all is done (including children).
    pthread_exit(NULL);
}

int partition(int low, int high) {
    // Set pivot to to last element.
    int pivot = a[high];
    // Set counter to the lowest element of this "run".
    int i = (low - 1);

    // Go through all elements between, swap the ones that are smaller or equal to the pivot.
    for (int j = low; j <= high- 1; j++)
    {
        if (a[j] <= pivot)
        {
            i++;
            swap(&a[i], &a[j]);
        }
    }

    swap(&a[i + 1], &a[high]);
    return (i + 1);
}


// Simple swap functions that moves the elements.
void swap(int* a, int* b)
{
    int t = *a;
    *a = *b;
    *b = t;
}