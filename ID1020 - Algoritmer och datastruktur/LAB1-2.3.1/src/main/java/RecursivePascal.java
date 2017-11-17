public class RecursivePascal extends ErrorPascal implements Pascal {
    // Rekursiv klass, ropar på sig själva.
    public void printPascal(int n) {
        if (!sanityCheck(n)) {
            System.err.println("Din input är inte giltigt. Krav: rader > 0. Du angav: " + n);
            return;
        }

        if (reversedTriangle == false) {
            // Om n = 1 så är vi på sista raden, vi printar helt enkelt en etta.
            if (n == 1)
                System.out.println(1);
            if (n > 1) {
                // Annars så ropar vi på funktionen tills if-satsen uppfylls, vilket då kommer skriva detta i en urättordning iom att n = 0 kommer printas först, sedan n = 1 etc, det hamnar i en "kö"
                printPascal(n - 1);
                // Beräknar och printar binomet för varje k-värde.
                for (int k = 0; k <= n - 1; k++)
                    System.out.print(binom(n - 1, k) + " ");
                System.out.println();
            }
        }

        if (reversedTriangle == true) {
            // Så länge n != 0 så har vi mer binom att beräkna
            if (n == 1)
                System.out.println("1");
            if (n > 1) {
                // I detta fall så skriver gör vi likandat som ovan, fast skriver ut först och sedan ropar på allt, det gör att triangeln blir reveresed.
                for (int k = 0; k <= n - 1; k++)
                    System.out.print(binom(n - 1, k) + " ");
                System.out.println();
                printPascal(n - 1);
            }
        }
    }

    public int binom(int n, int k) {
        if (!sanityCheck(n, k)) {
            System.err.println("Något gick SNETT! Krav: n, k >= 0 samt n > k. Dina värden är: n=" + n + ", k=" + k);
            System.exit(1);
        }

        // Simpel binom-räkning där vi sparar värdet i en multi-dimensionell array och följer den angivna formeln. Men hjälp av rekursion som önskat.
        if (usedBinoms[n][k] != 0)
            return usedBinoms[n][k];
        else if (n == k || k == 0)
            return usedBinoms[n][k] = 1;
        else
            return (usedBinoms[n - 1][k - 1] = binom(n - 1, k - 1)) + (usedBinoms[n - 1][k] = binom(n - 1, k));
    }
}