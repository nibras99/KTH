/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Main
{

    public static List<String> readWordList(BufferedReader input) throws IOException
    {
        LinkedList<String> list = new LinkedList<String>();
        while (true)
        {
            String s = input.readLine();
            if (s.equals("#"))
                break;
            list.add(s);
        }
        return list;
    }

    public static void main(String args[]) throws IOException
    {
        //long t1 = System.currentTimeMillis();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        List<String> wordList = readWordList(stdin);
        String word;

        // Enligt labbbeskrivningen så kan ordets längd max vara 40 tecken.
        // Skapar en array som vi "återanvänder" hela tiden.
        int[][] emptyMatrix = new int[40][40];

        // Initierar alla värden som kommer vara detsamma
        for (int i = 0; i < 40; i++)
        {
            emptyMatrix[0][i] = i;
            emptyMatrix[i][0] = i;
        }
        while ((word = stdin.readLine()) != null)
        {
            ClosestWords closestWords = new ClosestWords(word, wordList, emptyMatrix);
            System.out.print(word + " (" + closestWords.getMinDistance() + ")");
            for (String w : closestWords.getClosestWords())
                System.out.print(" " + w);
            System.out.println();
        }
        //long tottime = (System.currentTimeMillis() - t1);
        //System.out.println("CPU time: " + tottime + " ms");

    }
}
