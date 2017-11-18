/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */

import java.util.LinkedList;
import java.util.List;

public class ClosestWords
{
    LinkedList<String> closestWords = null;
    int closestDistance = -1;

    // Nya variablar som vi använder.
    String previousWord = "";
    int matchingLetters = 0;

    int partDist(String s1, String s2, int[][] d)
    {
        // s1 är ordet vi försöker rätta
        int l1 = s1.length();
        int l2 = s2.length();

        // Kollar hur många bokstäver som matchar, så kan vi skippa dem.
        // Detta då de, ej behöver rättas.
        matchingLetters = matchingLength(s2, previousWord);

        // Plats 0 har standardvärden, kan skippa dem.
        for (int j = matchingLetters + 1; j < l2 + 1; j++)
        {
            // Hoppar över alla ord som matchar
            for (int i = 1; i < l1 + 1; i++)
            {
                // Om orden matchar, så är det bara att gå vidare, ingen ändring krävs
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                {
                    d[i][j] = d[i - 1][j - 1];
                }
                // Annars så beräknar vi min av dem tidigare platserna, och tar det minsta!
                else
                {
                    d[i][j] = Math.min(Math.min(d[i - 1][j - 1], d[i - 1][j]), d[i][j - 1]) + 1;
                }
            }
        }

        previousWord = s2;
        return d[l1][l2];
    }

    public ClosestWords(String searchWord, List<String> wordList, int[][] emptyMatrix)
    {
        for (String s : wordList)
        {
            int dist = partDist(searchWord, s, emptyMatrix);
            // System.out.println("d(" + w + "," + s + ")=" + dist);
            if (dist < closestDistance || closestDistance == -1)
            {
                closestDistance = dist;
                closestWords = new LinkedList<String>();
                closestWords.add(s);
            } else if (dist == closestDistance)
                closestWords.add(s);
        }
    }

    // Ny funktion som kollar hur många bokstäver som matchar i början av ordet.
    // Dessa behöver inte "rättas" och kan därmed skippas.
    int matchingLength(String s1, String s2)
    {
        int shortestWord = Math.min(s1.length(), s2.length());

        for (int i = 0; i < shortestWord; i++)
        {
            if (s1.charAt(i) != s2.charAt(i))
            {
                return i;
            }
        }

        return shortestWord;
    }

    int getMinDistance()
    {
        return closestDistance;
    }

    List<String> getClosestWords()
    {
        return closestWords;
    }
}
