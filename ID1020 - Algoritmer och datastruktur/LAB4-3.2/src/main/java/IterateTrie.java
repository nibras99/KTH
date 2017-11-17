import java.util.AbstractMap.*;
import java.util.Map.Entry;
import java.util.Iterator;
import java.lang.UnsupportedOperationException;


class IterateTrie implements Iterator<Entry<String, Integer>>
{
    // Standard variablar som behövs
    public Trie topTrie;
    public Trie trie;
    public int subWords;
    public String startString = "";
    public int[] mostLetters;

    public IterateTrie(String s, Trie trie)
    {
        // Tar bort whitespace
        s = s.trim();

        // Används till att spara mest använda bokstaven, INGET annat.
        this.mostLetters = new int[256];

        // Om längden på ordet är 0 så vill vi iterera över alla ord
        // Annars vill vi iterera från sista bokstaven som motsvara sista noden blabla
        // Antalet iterationer som behövs == antalet distinct
        if (s.length() == 0)
            this.subWords = trie.distinct(trie);
        else
            this.subWords = trie.distinct(s);

        // När vi kommit till slutet så sparar vi "topnoden" och den vi börjar på
        if (s.length() == 0)
        {
            this.topTrie = trie;
            this.trie = trie;
        }
        else
        {
            this.topTrie = trie.getNode(s);
            this.trie = trie.getNode(s);
        }

        // Används när vi skickar tillbaka ordet, vi går bara tillbaka upp till topnoden
        // Sedan använde vi strinbuilder för att lägga ihop allt
        this.startString = s;
    }

    public boolean hasNext()
    {
        return subWords > 0;
    }

    public Entry<String, Integer> next()
    {
        // Vi har nu ett ord mindre att iterera över
        subWords--;
        // OM den har barn ska vi gå djupare
        // Annars ska vi upp ett steg.
        if (this.trie.hasChildren)
            nextChild(0);
        else
            nextChild(nextParent());

        // Sparar endast ord som börjar med motsvarande bokstav!
        mostLetters[getWord(this.trie).charAt(0)]++;

        // Returnar ordet och dess WordCount.
        Entry<String, Integer> se = null;
        se = new SimpleEntry<String, Integer>(getWord(this.trie), this.trie.wordCounter);
        return se;
    }

    public String getWord(Trie trie)
    {
        // Skapar det fulla ordet och returnar det.
        StringBuilder sb = new StringBuilder();
        while (trie != topTrie)
        {
            sb.insert(0, trie.c);
            trie = trie.parent;
        }
        sb.insert(0, this.startString);
        return sb.toString();
    }

    // Icke supportad metod
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    // Går till nästa barn, där vi skickat med startindex
    // Om vi har gått upp från ett barn så kommer startindex motsvara barnets char
    // Annars kommer det vara 0
    public void nextChild(int parentIndex)
    {
        for (int i = parentIndex; i < trie.ASCII_SIZE; i++)
        {
            // Så länge noden finns
            if (this.trie.children[i] != null)
            {
                // Om det är ett ord så sätter vi ordet som nya noden och returnar/avbryter
                if (this.trie.children[i].word)
                {
                    this.trie = this.trie.children[i];
                    return;
                }
                // Ananrs så sätter vi även det som sista ordet, men ropar på att gå djupare
                // Vi har ju inte hittat ett ord ännu!
                else
                {
                    this.trie = this.trie.children[i];
                    nextChild(0);
                    return;
                }
            }
        }
        // Om vi lyckas loopa igenom allt utan att hitta mera
        // Så vill vi gå up igen, och sedan såklart söka nästa barn
        nextChild(nextParent());
    }

    public int nextParent()
    {
        // Går upp ett steg samt skickar med indexet som vi var på + 1
        int lastParentIndex = this.trie.c;
        this.trie = this.trie.parent;
        return lastParentIndex + 1;
    }
}