public class IterativePascal extends ErrorPascal implements Pascal {
    // Iterativ klass, ropar INTE på sig själva.
    public void printPascal(int n) {
        if (!sanityCheck(n)) {
            System.err.println("Din input är inte giltigt. Krav: rader > 0. Du angav: " + n);
            return;
        }

        if (reversedTriangle == false) {
            // Triangeln är vanligt uppställd, vi loopar igenom varje värde. (0 0) == 1, ||| (1 0) = 1, (1 1) = 1, ||| (2 0) = 1, (2 1) = 2, (2 2) = 1.......
            for (int i = 0; i < n; i++) {
                for (int j = i; j >= 0; j--)
                    System.out.print(binom(i, j) + " ");

                System.out.println();
            }
        }

        // Denna är reveresed, och eftersom vi måste iterera så är den lite speciell.
        // Först låter vi den iterera och spara alla värden i usedBinom's arrayen, därefter loopar vi genom den baklänges och skriver ut.
        if (reversedTriangle == true) {
            // Loopar och sparar alla värden, vi ignorerar return-värdet.
            for (int i = 0; i < n; i++)
                for (int j = i; j >= 0; j--)
                    binom(i, j);

            // Loopar baklänges genom arrayen, med start på n. Skriver ut talen som sparades sedan innan, bästa lösningen jag kunde komma på som var ITERATIV.
            for (int i = n - 1; i >= 0; i--) {
                for (int j = i; j >= 0; j--)
                    System.out.print(usedBinoms[i][j] + " ");

                System.out.println();
            }
        }
    }

    public int binom(int n, int k) {
        if (!sanityCheck(n, k)) {
            System.err.println("Ditt N eller K är inte giltigt. Krav: n, k >= 0 samt n > k. Dina värden är: n=" + n + ", k=" + k);
            System.exit(1);
        }

        // Simpel binom-räkning där vi sparar värdet i en multi-dimensionell array och följer den angivna formeln. Ingen Iteration ens behövd!
        if (usedBinoms[n][k] != 0)
            return usedBinoms[n][k];
        else if (n == k || k == 0)
            return usedBinoms[n][k] = 1;
        else
            return usedBinoms[n][k] = usedBinoms[n-1][k-1] + usedBinoms[n-1][k];
    }
}