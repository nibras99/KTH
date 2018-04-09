#include <omp.h>

double start_time, end_time;

#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#define ELEMENTS 10000000
#define MAXWORKERS 8
//#define DEBUG

void quickSort(int, int);
void swap(int, int);

// Global "array".
int *matrix;

int main() {
    // Malloc so we can have a bit larger
    matrix = malloc(sizeof(int) * ELEMENTS);

    // Init array with random values.
    srand(time(NULL));
    for (int i = 0; i < ELEMENTS; i++)
        matrix[i] = rand() % 99;
    omp_set_num_threads(MAXWORKERS);

#ifdef DEBUG
    printf("\nUnsorted array: \n");
    for (int i = 0; i < ELEMENTS; i++)
        printf(" %d ", matrix[i]);
    printf("\n");
#endif

    // Everything initiated, start timer.
    start_time = omp_get_wtime();

    // Want the recursive to be parallel, but we only want this first call to run once.
    #pragma omp parallel
    {
        #pragma omp single nowait
        {
            quickSort(0, ELEMENTS - 1);
        }
    }

    // All done, stop timer, print time.
    end_time = omp_get_wtime();
    printf("The execution time is %g sec\n", end_time - start_time);

#ifdef DEBUG
    printf("\nSorted array: \n");
    for (int i = 0; i < ELEMENTS; ++i)
        printf(" %d ", matrix[i]);
    printf("\n");
#endif
    return 0;
}

void quickSort(int low, int high) {
    // Create some variables, set a pivot.
    int i = low;
    int j = high;
    int pivot = matrix[(low + high) / 2];
    while (i <= j) {
        // Until we find an element that is larger than the pivot
        while (matrix[i] < pivot)
            i++;
        // Until we find an element that is smaller than the pivot
        while (matrix[j] > pivot)
            j--;

        // If the left found element is smaller (or equal) to the right one, we swap them!
        if (i <= j) {
            swap(i, j);
            // Since we swapped elements, we want the next iteration to start on the next elements.
            i++;
            j--;
        }
    }

    // Two new tasks to be ran parallel. On for the lower, one for the upper.
    #pragma omp task
    {
        // Makes sure we break, sometime.
        if (low < j)
            quickSort(low, j);
    }
    #pragma omp task
    {
        // Makes sure we break, sometime.
        if (high > i)
            quickSort(i, high);
    }
}

// Simple swap function that moves the elements.
void swap(int a, int b) {
    int t = matrix[a];
    matrix[a] = matrix[b];
    matrix[b] = t;
}