#include <iostream>
#include <cstdlib>
#include <string>
#include <fstream>
#include <time.h>
#include <sys/time.h>
#include <iomanip>

using namespace std;

void jacobi(int);

void compute(int);

void interpolation(int);

int gridSize, numIters, numWorkers;
double start, stop;

// Courtesy of course material.
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

double **one;
double **two;
int maxSize;

int main(int argc, char *argv[]) {
    if (argc != 3) {
        cerr << "Usage for program '" << argv[0] << "': gridSize numIters" << endl;
        return -1;
    } else {
        gridSize = atoi(argv[1]);
        numIters = atoi(argv[2]);
    }

    // V level 4 times
    maxSize = 4 * (2 * gridSize + 1) + 4;

    // Create 2D arrays
    one = (double **) malloc(maxSize * sizeof(double));
    two = (double **) malloc(maxSize * sizeof(double));

    // Init array borders to 1, and inner values to 0
    for (int i = 0; i < maxSize; i++) {
        one[i] = (double *) malloc(maxSize * sizeof(double));
        two[i] = (double *) malloc(maxSize * sizeof(double));

        for (int j = 0; j < maxSize; j++) {
            if (i == 0 || j == 0 || i == maxSize - 1 || j == maxSize - 1) {
                one[i][j] = 1.0;
                two[i][j] = 1.0;
            } else {
                one[i][j] = 0.0;
                two[i][j] = 0.0;
            }
        }
    }

    // All parts below need to run in Sequential order.
    // Their inner workings can run in parallel though, which is handeled in each specific function/method.
    start = read_timer();

    jacobi(4);
    compute(2);

    jacobi(4);
    compute(4);

    jacobi(4);
    compute(8);

    // "Use exactly four iterations on each of the finer grids,
    // and use the command-line argument numIters (see below) for the number of iterations on the coarsest (smallest) grid."

    // This is now the coarsest, so we go numIters jacobi.
    jacobi(numIters);

    interpolation(8);
    jacobi(4);

    interpolation(4);
    jacobi(4);

    interpolation(2);
    jacobi(4);
    stop = read_timer();

    // Calculate max offset.
    double maxOffset = 1.0;
    for (int i = 1; i < maxSize - 1; i++) {
        for (int j = 1; j < maxSize - 1; j++) {
            if (maxOffset > one[i][j]) {
                maxOffset = one[i][j];
            }
        }
    }

    // Print results, as according to project description.
    cout << "--- Program Done ---" << endl;
    cout << "Program line arguments: " << argv[0] << " " << atoi(argv[1]) << " " << atoi(argv[2]) << endl;
    cout << "Execution time for computes: " << stop - start << " seconds." << endl;
    cout << "Lowest value found was: " << fixed << setprecision(12) << maxOffset << " which gives an error of: "
         << fixed << setprecision(12) << 1.0 - maxOffset << endl;
    cout << "Data printed to filedata.out" << endl;

    ofstream file;
    file.open("filedata.out");
    for (int i = 0; i < maxSize; i++) {
        for (int j = 0; j < maxSize; j++) {
            file << fixed << setprecision(12) << one[i][j] << " ";
        }
        file << endl;
    }
    file.close();

    return 0;
}

void jacobi(int iterations) {
    for (int iters = 0; iters < iterations; iters++) {
        for (int i = 1; i < maxSize - 1; i++) {
            for (int j = 1; j < maxSize - 1; j++) {
                two[i][j] = (one[i - 1][j] + one[i + 1][j] + one[i][j - 1] + one[i][j + 1]) * 0.25;
            }
        }
        double **temp = one;
        one = two;
        two = temp;
    }

    return;
}

void compute(int increase) {
    // 2 to maxSize -2 cause we don't want a coarse point direct neighbour to the border. Will cause segmentation fault.
    for (int i = 2; i < maxSize - 2; i = i + increase) {
        for (int j = 2; j < maxSize - 2; j = j + increase) {
            two[i][j] = one[i][j] * 0.5 + (one[i - 1][j] + one[i + 1][j] + one[i][j - 1] + one[i][j + 1]) * 0.125;
        }
    }
    double **temp = one;
    one = two;
    two = temp;

    return;
}

void interpolation(int increase) {
    // 2 to maxSize -2 cause we don't place any coarse points direct neighbour to the border. Will cause segmentation fault.
    for (int i = 2; i < maxSize - 2; i = i + increase) {
        for (int j = 2; j < maxSize - 2; j = j + increase) {
            /*
             * 1/4 1/2 1/4
             * 1/2  1  1/2
             * 1/4 1/2 1/4
             */

            // 1/2 points.
            two[i - 1][j] = (one[i - 2][j] + one[i][j]) * 0.5;
            two[i + 1][j] = (one[i + 2][j] + one[i][j]) * 0.5;
            two[i][j - 1] = (one[i][j - 2] + one[i][j]) * 0.5;
            two[i][j + 1] = (one[i][j + 2] + one[i][j]) * 0.5;

            // 1/4 corners.
            two[i - 1][j - 1] = (one[i - 1][j - 2] + one[i - 1][j]) * 0.5;
            two[i - 1][j + 1] = (one[i - 1][j + 2] + one[i - 1][j]) * 0.5;
            two[i + 1][j - 1] = (one[i + 1][j - 2] + one[i + 1][j]) * 0.5;
            two[i + 1][j + 1] = (one[i + 1][j + 2] + one[i + 1][j]) * 0.5;
        }
    }
    double **temp = one;
    one = two;
    two = temp;

    return;
}