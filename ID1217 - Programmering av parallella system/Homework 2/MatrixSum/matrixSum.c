#include <omp.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <stdbool.h>

#define MAXSIZE 10
#define MAXWORKERS 8

int matrix[MAXSIZE][MAXSIZE];
double start_time, end_time;

// Struct to save values and their indexes.
struct Compare {
    int val;
    int i;
    int j;
};

// Can apparently declare custom reductions using struct in OpenMP, thank you StackOverflow!
#pragma omp declare reduction(minimum : struct Compare : omp_out = omp_in.val < omp_out.val ? omp_in : omp_out)
#pragma omp declare reduction(maximum : struct Compare : omp_out = omp_in.val > omp_out.val ? omp_in : omp_out)

int main() {
    int i, j, total = 0;

    omp_set_num_threads(MAXWORKERS);

    // Init matrix
    srand(time(NULL));
    for (i = 0; i < MAXSIZE; i++) {
        //printf("%d [ ", i);
        for (j = 0; j < MAXSIZE; j++) {
            //printf("Accessed %d %d\n", i, j);
            matrix[i][j] = rand() % 99;
            //printf(" %d", matrix[i][j]);
        }
        //printf(" ]\n");
    }

    // Create structs to be used for min and max reductions. Set their initial values (waste?).
    struct Compare min;
    struct Compare max;
    min.val = matrix[0][0];
    max.val = matrix[0][0];
    // Special variable to be used once for each thread as a private.
    bool inited = false;

    // All inits done, start timer!
    start_time = omp_get_wtime();

    // We take the firstprivate for inited, so that we can set the structs initial value ONCE inside each thread.
    // Could not use firstprivate(min) for example, got errors. Else that would be better of course.
    // Use + reduction for total, and custom ones for min and max. j is private for each thread.
#pragma omp parallel for firstprivate(inited) reduction (+:total), reduction(minimum:min), reduction(maximum:max) private(j)
    for (i = 0; i < MAXSIZE; i++) {
        if (!inited)
        {
            // To make sure we have a value from the matrix as min and max.
            // firstprivate(minV) did not work.
            // only run once, so that we dont overwrite values set by the actual thread.
            min.val = matrix[0][0];
            min.i = 0;
            min.j = 0;

            max.val = matrix[0][0];
            max.i = 0;
            max.j = 0;

            inited = true;
        }

        // Get and set total, min and max.
        for (j = 0; j < MAXSIZE; j++) {
            total += matrix[i][j];
            if (matrix[i][j] < min.val) {
                min.val = matrix[i][j];
                min.i = i;
                min.j = j;
            }
            if (matrix[i][j] > max.val) {
                max.val = matrix[i][j];
                max.i = i;
                max.j = j;
            }
        }
    }
    // Implicit barrier

    end_time = omp_get_wtime();

    // Makes sure master threads prints results.
    #pragma omp master
    {
        printf("Total: %d\n", total);
        printf("Time: %g seconds\n", end_time - start_time);
        printf("Min: %d at [%d][%d]\n", min.val, min.i, min.j);
        printf("Max: %d at [%d][%d]\n", max.val, max.i, max.j);
    }
}