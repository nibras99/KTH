/**
 * Created by Casper on 2016-09-02.
 */
public class RecursivePascal {
    int usedBinoms[][] = new int[2000][2000];
    boolean reversedTriangle = false;

    public void printPascal(int n)
    {
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
        // Simpel binom-räkning där vi sparar värdet i en multi-dimensionell array och följer den angivna formeln. Men hjälp av rekursion som önskat.
        if (usedBinoms[n][k] != 0)
            return usedBinoms[n][k];
        else if (n == k || k == 0)
            return usedBinoms[n][k] = 1;
        else
            return (usedBinoms[n - 1][k - 1] = binom(n - 1, k - 1)) + (usedBinoms[n - 1][k] = binom(n - 1, k));
            //return usedBinoms[n][k] = usedBinoms[n-1][k-1] + usedBinoms[n-1][k];
    }


    public static void main(String args[]) {
        System.out.println("Start");
        RecursivePascal pascal = new RecursivePascal();
        pascal.printPascal(10);
        System.out.println("End");
    }
}
