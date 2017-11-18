import java.util.ArrayList;
import java.util.Arrays;

/**
 * Exempel på in- och utdatahantering för maxflödeslabben i kursen
 * ADK.
 * <p>
 * Använder Kattio.java för in- och utläsning.
 * Se http://kattis.csc.kth.se/doc/javaio
 *
 * @author: Per Austrin
 */

public class BipRed {
    Kattio io;
    private int x;
    private int y;
    private int e;
    private int s;
    private int t;
    private ArrayList<Integer>[] match;

    void readGraph() {
        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();
        e = io.getInt();

        match = new ArrayList[x + 1];
        for (int i = 0; i <= x; i++)
        {
            match[i] = new ArrayList();
        }

        // Läs in kanterna
        for (int i = 0; i < e; i++) {
            int a = io.getInt();
            int b = io.getInt();
            match[a].add(b);
        }

        s = x + y + 1;
        t = x + y + 2;
    }


    void createFlow() {
        // Vertices
        io.println(x + y + 2);

        // S T
        io.println((x + y + 1) + " " + (x + y + 2));

        // Edges
        io.println(e + x + y);

        // S till alla X
        for (int i = 1; i <= x; i++) {
            io.println(s + " " + i + " " + 1);
        }

        // X till alla Y
        for (int i = 1; i < match.length; i++) {
            for (int j = 0; j < match[i].size(); j++) {
                io.println(i + " " + match[i].get(j) + " " + 1);
            }
        }

        // alla Y till T
        for (int i = x + 1; i <= x + y; i++) {
            io.println(i + " " + t + " " + 1);
        }

        // FLUSHAR, UTSKRIFT KLAR
        io.flush();
    }

    void printMaxFlow() {
        int v = io.getInt();
        int s1 = io.getInt();
        int t1 = io.getInt();
        int totflow = io.getInt();
        int edges = io.getInt();

        io.println(x + " " + y);
        io.println(totflow);

        for (int i = 0; i < edges; ++i) {
            // Flöde f från a till b
            int a = io.getInt();
            int b = io.getInt();
            int f = io.getInt();

            if (a != s && a != t && b != s && b != t) {
                io.println(a + " " + b);
                System.err.println(a + " " + b);
            }
        }

        io.flush();
    }

    BipRed() {
        io = new Kattio(System.in, System.out);

        readGraph();

        createFlow();

        printMaxFlow();

        io.close();
    }

    public static void main(String args[]) {

        new BipRed();
    }
}