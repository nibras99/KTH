#include <iostream>
#include <cstdlib>
#include <string>
#include <fstream>
#include <time.h>
#include <sys/time.h>
#include <iomanip>

using namespace std;

void printArray(double *[]);

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

int main(int argc, char *argv[]) {
    if (argc != 3) {
        cerr << "Usage for program '" << argv[0] << "': gridSize numIters" << endl;
        return -1;
    } else {
        gridSize = atoi(argv[1]);
        numIters = atoi(argv[2]);
    }

    // Create 2D arrays
    double ** one = (double **) malloc(gridSize * sizeof(double));
    double ** two = (double **) malloc(gridSize * sizeof(double));

    // Init array borders to 1, and inner values to 0
    for (int i = 0; i < gridSize; i++) {
        one[i] = (double *)malloc(gridSize * sizeof(double));
        two[i] = (double *)malloc(gridSize * sizeof(double));

        for (int j = 0; j < gridSize; j++) {
            if (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1) {
                one[i][j] = 1.0;
                two[i][j] = 1.0;
            } else {
                one[i][j] = 0.0;
                two[i][j] = 0.0;
            }
        }
    }

    // Do the Jacobi calculations.
    start = read_timer();
    for (int iters = 0; iters < numIters; iters++) {
        for (int i = 1; i < gridSize - 1; i++) {
            for (int j = 1; j < gridSize - 1; j++) {
                two[i][j] = (one[i - 1][j] + one[i + 1][j] + one[i][j - 1] + one[i][j + 1]) * 0.25;
            }
        }
        double ** temp = one;
        one = two;
        two = temp;
    }
    stop = read_timer();

    // Calculate max offset.
    double maxOffset = 1.0;
    for (int i = 1; i < gridSize - 1; i++) {
        for (int j = 1; j < gridSize - 1; j++) {
            if (maxOffset > one[i][j]) {
                maxOffset = one[i][j];
            }
        }
    }

    // Print results, as according to project description.
    cout << "--- Program Done ---" << endl;
    cout << "Program line arguments: " << argv[0] << " " << atoi(argv[1]) << " " << atoi(argv[2]) << endl;
    cout << "Execution time for computes: " << stop - start << " seconds." << endl;
    cout << "Lowest value found was: " << fixed << setprecision(12) << maxOffset << " which gives an error of: " << fixed << setprecision(12) << 1.0 - maxOffset << endl;
    cout << "Data printed to filedata.out" << endl;

    ofstream file;
    file.open("filedata.out");
    for (int i = 0; i < gridSize; i++) {
        for (int j = 0; j < gridSize; j++) {
            file << fixed << setprecision(12) << one[i][j] << " ";
        }
        file << endl;
    }
    file.close();

    return 0;
}