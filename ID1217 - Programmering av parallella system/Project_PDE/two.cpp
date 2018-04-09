#include <iostream>
#include <cstdlib>
#include <string>
#include <omp.h>
#include <fstream>
#include <iomanip>

using namespace std;

void printArray(double *[]);

int gridSize, numIters, numWorkers;
double start, stop;

int main(int argc, char *argv[]) {
    if (argc != 4) {
        cerr << "Usage for program '" << argv[0] << "': gridSize numIters numWorkers" << endl;
        return -1;
    } else {
        gridSize = atoi(argv[1]);
        numIters = atoi(argv[2]);
        numWorkers = atoi(argv[3]);
    }

    // Create 2D arrays
    double **one = (double **) malloc(gridSize * sizeof(double));
    double **two = (double **) malloc(gridSize * sizeof(double));

    // Init array borders to 1, and inner values to 0
    for (int i = 0; i < gridSize; i++) {
        one[i] = (double *) malloc(gridSize * sizeof(double));
        two[i] = (double *) malloc(gridSize * sizeof(double));

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

    // Set number of threads for OpenMP to run with.
    omp_set_num_threads(numWorkers);

    int j;
    // Run Jacobi using OpenMP. Can't parralelize the Iters region since it needs to run sequential.
    start = omp_get_wtime();
    for (int iters = 0; iters < numIters; iters++) {
        #pragma omp parallel
        {
            // Static schedule default value is "chunk size is loop_count/number_of_threads"
            #pragma omp for private(j) schedule(static)
            for (int i = 1; i < gridSize - 1; i++) {
                for (j = 1; j < gridSize - 1; j++) {
                    two[i][j] = (one[i - 1][j] + one[i + 1][j] + one[i][j - 1] + one[i][j + 1]) * 0.25;
                }
            }
        }
        double **temp = one;
        one = two;
        two = temp;
    }
    stop = omp_get_wtime();

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
    cout << "Program line arguments: " << argv[0] << " " << atoi(argv[1]) << " " << atoi(argv[2]) << " " << atoi(argv[3]) << endl;
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