import edu.princeton.cs.introcs.In;

import java.net.URL;
import java.util.Map;

public class Driver
{
    public static void main(String[] args)
    {
        URL url = ClassLoader.getSystemResource("kap1.txt");

        Trie trie = new Trie();

        if (url != null)
        {
            System.out.println("Reading from: " + url);
        }
        else
        {
            System.out.println("Couldn't find file: kap1.txt");
        }

        In input = new In(url);
        int words2 = 0;

        while (!input.isEmpty())
        {
            String line = input.readLine().trim();
            String[] words = line.split("(\\. )|:|,|;|!|\\?|( - )|--|(\' )| ");
            String lastOfLine = words[words.length - 1];

            if (lastOfLine.endsWith("."))
            {
                words[words.length - 1] = lastOfLine.substring(0, lastOfLine.length() - 1);
            }

            for (String word : words)
            {
                String word2 = word.replaceAll("\"|\\(|\\)", "");

                if (word2.length() == 0)
                {
                    continue;
                }

                //System.out.println(word2.toLowerCase());
                // Sparar ordet i Trie
                trie.put(word2.toLowerCase());
            }
        }

        // Arrayer som 10 största värdena sparas i för högsta och lägsta frekvens
        Map.Entry[] hFreq = new Map.Entry[10];
        Map.Entry[] lFreq = new Map.Entry[10];

        // Ordet som ska skickas / jämföras. Tom sträng == alla ord.
        String wordToSend = "";

        // Printar Get och COUNT
        System.out.println("GET:\t\t\t\t" + trie.get(wordToSend));
        System.out.println("COUNT OF:\t\t\t" + trie.count(wordToSend));
        if (!wordToSend.equals(""))
            System.out.println("DISTINCT OF:\t\t" + trie.distinct(wordToSend));

        // Itererar igenom alla ord med start på wordToSend
        IterateTrie iterator = new IterateTrie(wordToSend, trie);
        System.out.println("ITERERAR PÅ ORDET:\t" + wordToSend);
        Map.Entry<String, Integer> kv = null;

        while (iterator.hasNext())
        {
            kv = iterator.next();
            System.out.println(kv.getValue() + "\t" + kv.getKey());

            // Med hjälp av for loopen så sparar vi 10 högsta och 10 lägsta orden
            for (int i = 0; i < 10; i++)
            {
                boolean forceTen = false;
                if (hFreq[i] == null)
                {
                    hFreq[i] = kv;
                    forceTen = true;
                }
                else if ((Integer) hFreq[i].getValue() < kv.getValue())
                {
                    for (int j = 9; j > i; j--)
                    {
                        hFreq[j] = hFreq[j - 1];
                    }
                    hFreq[i] = kv;
                    forceTen = true;
                }

                if (lFreq[i] == null)
                {
                    lFreq[i] = kv;
                    forceTen = true;
                }
                else if ((Integer) lFreq[i].getValue() > kv.getValue())
                {
                    // FUCK YOU DUPLICATE WARNING
                    int k = 0;

                    for (int j = 9; j > i; j--)
                    {
                        lFreq[j] = lFreq[j - 1];
                    }
                    lFreq[i] = kv;
                    forceTen = true;
                }
                if (forceTen)
                    i = 10;
            }
        }

        // Om vi loopat igenom ALLT så printar vi även högsta och lägsta orden.
        if (wordToSend.equals(""))
        {
            System.out.println("\nHÖGSTA");

            for (int i = 0; i < 10; i++)
            {
                System.out.println(hFreq[i].getValue() + "\t" + hFreq[i].getKey());
            }

            System.out.println("\nLÄGSTA");

            for (int i = 0; i < 10; i++)
            {
                System.out.println(lFreq[i].getValue() + "\t" + lFreq[i].getKey());
            }


            // Beräknar och sparar den bokstav som flest ord börjar på.
            // Hade gått att göra med en for loop som går från i = 0, i < 256
            // Där vi för varje i räknar distinct och sorterar därefter.
            // Men nu gjordes den med Iteratorn!
            int mostUsedLetter = 0;
            int mostUsedLetterCount = 0;
            for (int i = 0; i < trie.ASCII_SIZE; i++)
            {
                if (iterator.mostLetters[i] > mostUsedLetterCount)
                {
                    mostUsedLetter = i;
                    mostUsedLetterCount = iterator.mostLetters[i];
                }
            }

            System.out.println("\nBokstav som flest ord börjar på: " + (char) mostUsedLetter + " count: " + mostUsedLetterCount);

            System.out.println("\nPrefix med längd 2 var " + trie.prefixTwo());
        }
    }
}