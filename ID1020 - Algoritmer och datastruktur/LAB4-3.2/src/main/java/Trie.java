public class Trie
{
    // Så jag slipper ändra på 111 ställen om jag skulle vilja byta storlek
    public static final int ASCII_SIZE = 256;

    // Standardvariablar
    public char c;
    public Trie[] children;
    public boolean word;
    public int wordCounter;
    public boolean hasChildren;
    public int charCounter;
    public Trie parent;

    // Standard constructor
    public Trie()
    {
        this.children = new Trie[ASCII_SIZE];
        this.word = false;
        this.wordCounter = 0;
        this.hasChildren = false;
        this.charCounter = 0;
        this.parent = null;
    }

    public void put(String s)
    {
        // Ökar charcounten för noden
        this.charCounter++;

        // Om vi kommit till slutet
        if (s.length() == 0)
        {
            // Ökar antalet words med +1
            // Och sätter word till true
            this.wordCounter++;
            this.word = true;

            // Avbryter
            return;
        }

        // Annars så tar vi index
        int index = s.charAt(0);

        // OM vi sedan innan inte lagt in något i platsen för index
        if (this.children[index] == null)
        {
            // Så skapar vi en ny nod med 256 platser
            // Sätter tidigare node som att den har barn
            // Och sparar bokstaven i nya Nodens plats så att den själv vet
            // Samt linkar till dess förälder
            this.children[index] = new Trie();
            this.hasChildren = true;
            this.children[index].c = s.charAt(0);
            this.children[index].parent = this;
        }

        // Ropar på sig själv med en kortare string!
        this.children[index].put(s.substring(1));
    }

    public int get(String s)
    {
        // Om vi kommit till slutet så returnar vi wordCounter (value)
        if (s.length() == 0)
            return this.wordCounter;

        // Annars så hämtar vi index
        char letter = s.charAt(0);
        int index = letter;

        // Om vi inte hittar djupare så existerar ju inte ordet! Returna 0!
        if (this.children[index] == null)
        {
            return 0;
        }

        // Annrs GÅR VI DJUPARE
        return this.children[index].get(s.substring(1));
    }


    public int count(String s)
    {
        // Skapar en temp Trie och initierar värdet
        Trie trie = this;
        int totalValue = 0;

        // Om längden är 0 på strängen så har dem skickat FEL.
        if (s.length() == 0)
            return 0;

        // Annars så loopar vi tills vi kommer till sista noden.
        while (s.length() != 1)
        {
            // Om den är null så returnar vi bara totalValue... som kommer vara 0
            if (trie.children[s.charAt(0)] == null)
                return totalValue;
            // Annars går vi djupare och kapar strängen
            trie = trie.children[s.charAt(0)];
            s = s.substring(1);
            //System.out.println("CUT TO: " + trie.c);
        }

        // I slutet så hämtar vi såklart även sista platsen som vi faktiskt ska skicka med!
        trie = trie.children[s.charAt(0)];

        // Samt gör en sista kontroll innan vi skickar ivög den att den inte är null
        if (trie != null)
        {
            totalValue = count(trie);
            //System.out.println("xxx: " + totalValue + "   " + trie.c + "   " + trie.children[102]);
        }

        return totalValue;
    }

    public int count(Trie trie)
    {
        // Lägger ihop och loopar igenom alla 256 platser
        int totalValue = trie.wordCounter;
        for (int i = 0; i < ASCII_SIZE; i++)
        {
            // OM den inte är null så går vi djupare!
            if (trie.children[i] != null)
            {
                totalValue = totalValue + count(trie.children[i]);
            }
        }

        // Returnar när vi kommer till slutet
        return totalValue;
    }

    public int distinct(String s)
    {
        // Skapar temp Node
        Trie trie = this;
        int totalValue = 0;

        // Så länge vi inte nått slutet, bokstavligen samma som Count
        while (s.length() != 1)
        {
            if (trie.children[s.charAt(0)] == null)
                return totalValue;
            trie = trie.children[s.charAt(0)];
            s = s.substring(1);
            // because fuck den gula texten
            int a = 0;
        }

        trie = trie.children[s.charAt(0)];

        if (trie != null)
        {
            totalValue = distinct(trie);
        }

        return totalValue;
    }

    public int distinct(Trie trie)
    {
        // Samma som Count fast vi ökar värdet för varje unika ord istället för att hämta wordCount
        int totalValue = 0;
        for (int i = 0; i < ASCII_SIZE; i++)
        {
            if (trie.children[i] != null)
            {
                if (trie.children[i].word)
                {
                    totalValue++;
                }
                totalValue = totalValue + distinct(trie.children[i]);
            }
        }

        return totalValue;
    }

    public Trie getNode(String s)
    {
        // Går till slutet och returnar den djupaste noden i ordet.
        Trie tempTrie = new Trie();
        if (s.length() == 0)
            return tempTrie;

        tempTrie = this;

        while (s.length() != 1)
        {
            if (tempTrie.children[s.charAt(0)] != null)
            {
                tempTrie = tempTrie.children[s.charAt(0)];
                s = s.substring(1);
            }
            else
                return null;
        }

        return tempTrie.children[s.charAt(0)];
    }

    public String prefixTwo()
    {
        // Beräknar den sträng med längd 2 som har flest wordCount.
        String temp = "";
        String prefix;
        int freq = 0;
        for (int i = 0; i < 256; i++)
        {
            for (int j = 0; j < 256; j++)
            {
                prefix = "" + (char) i + (char) j;
                if (temp == null || this.count(prefix) > freq)
                {
                    freq = this.count(prefix);
                    temp = prefix;
                }
            }
        }
        return "\'" + temp + "\'" + " med längden " + freq;
    }
}