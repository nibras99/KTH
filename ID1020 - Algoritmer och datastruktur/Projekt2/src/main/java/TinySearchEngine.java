import parser.Node;
import parser.Operator;
import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import se.kth.id1020.util.Word;

import java.util.*;
import java.lang.*;

public class TinySearchEngine implements TinySearchEngineBase
{
    // Ett Object som ligger i en hashmap som value och dess ord som key
    public class Container
    {
        // Kommer spara alla unika dokument för det ordet i en lista.
        public List<Document> doc = new ArrayList<Document>();
        // Kommer spara hur många gånger ordet kommer i det specifika dokumentet.
        public HashMap<Document, Integer> occurrence = new HashMap<Document, Integer>();

        // Constructor för första gången ordet förekommer
        public Container(Document doc)
        {
            this.doc.add(doc);
            this.occurrence.put(doc, 1);
        }

        // Om ordet kommer igen i, så uppdaterar vi de variablar som behöver uppdateras
        public void update(Document doc)
        {
            if (this.doc.contains(doc))
                this.occurrence.put(doc, this.occurrence.get(doc) + 1);
            else
            {
                this.doc.add(doc);
                this.occurrence.put(doc, 1);
            }
        }
    }

    // Ett Objekt som sparar dokumentet och dess relevance, används endast i sökmetoden.
    // Använder objekt så man kan använda Collections.sort med min implementerade compareTo
    public class DocRel implements Comparable<DocRel>
    {
        public Document doc;
        public double relevance;

        public DocRel(Document doc, double relevance)
        {
            this.doc = doc;
            this.relevance = relevance;
        }

        public int compareTo(DocRel d1)
        {
            return Double.compare(this.relevance, d1.relevance);
        }
    }

    // De tre huvud HashMapsen som används, en för varje ord, en för antalet ord i varje dokument, och en som används som "cache".
    HashMap<String, Container> mapCon = new HashMap<String, Container>();
    HashMap<Document, Integer> totalWords = new HashMap<Document, Integer>();
    HashMap<String, ArrayList<DocRel>> cache = new HashMap<String, ArrayList<DocRel>>();

    // Skapar node och iterator som "global" så den lätt kan användas i alla metoder
    Node node = new Node();
    public Node iterator;

    //StringBuilder sb = new StringBuilder();

    // Behövdes inte
    public void postInserts()
    {

    }

    public void preInserts()
    {

    }

    public void insert(Sentence sentence, Attributes attr)
    {
        // För varje ord i meningen
        for (int i = 0; i < sentence.getWords().size(); i++)
        {
            // Så kollar vi om dokumentet finns etc, om det gör det så uppdaterar vi antalet ord i den med +1
            // Annars så skapar vi en ny plats i hashmapen och lägger 1 som startvärde, för första ordet!
            if (!totalWords.containsKey(attr.document))
                totalWords.put(attr.document, 1);
            else
                totalWords.put(attr.document, totalWords.get(attr.document) + 1);

            // Wordet vi kommer utnyttja
            Word w = sentence.getWords().get(i);

            // Samma historia här, finns den sedan innan så uppdateras den
            // Annars så skapas det nytt
            if (!mapCon.containsKey(w.word))
            {
                ArrayList<Document> list = new ArrayList<Document>();
                list.add(attr.document);
                mapCon.put(w.word, new Container(attr.document));
            }
            else
            {
                Container con = mapCon.get(w.word);
                con.update(attr.document);
            }
        }
    }

    public List<Document> search(String query)
    {
        /*
        // Printar cachen som existerar för tillfället
        for (Map.Entry<String, ArrayList<DocRel>> entry : cache.entrySet())
        {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        */
        // Bort med whitespace!
        query = query.trim();

        // Om längden är 0....
        if (query.length() == 0)
            return null;

        // Annars splittar vi så att vi har något att jobba med!
        String[] queryParts = query.split(" ");

        // Skapar en NY node
        node = new Node();
        // Och ett NYTT parse tree!
        node.createNodeTree(queryParts);

        // Hämtar "djupaste" noden
        iterator = node.getDeepest();

        // Ropar på själva sökfunktionen
        ArrayList<DocRel> searchList = searchWord(node.children.get(0));

        // Och definierar en lista som vi kommer returna
        List<Document> returnList = new ArrayList<Document>();

        // Om node har storleken 3 så har den en orderby etc
        if (node.children.size() == 3)
        {
            // Vi kan ju inte sortera null...
            if (searchList != null)
            {
                // Om vi ska sortera på popularity så gör vi det
                if (node.children.get(1).operator.equals(Operator.popularity))
                {
                    // Börjar med att för över alla Dokument då relevance i detta fall kommer vara onödigt
                    for (int i = 0; i < searchList.size(); i++)
                        returnList.add(searchList.get(i).doc);

                    // Sedan sorterar vi med Collections.sort
                    Collections.sort(returnList, new Comparator<Document>()
                    {
                        public int compare(Document d1, Document d2)
                        {
                            return Integer.compare(d1.popularity, d2.popularity);
                        }
                    });
                }
                // Annars om vi nu ska sortera på relevance så gör vi det.
                else if (node.children.get(1).operator.equals(Operator.relevance))
                {
                    Collections.sort(searchList);

                    // Och lägger in den sorterade listan i returnList
                    for (int i = 0; i < searchList.size(); i++)
                        returnList.add(searchList.get(i).doc);
                }


                /*
                // Printar listan, innan den reversats om det nu behövs
                if (returnList != null)
                    for (int i = 0; i < returnList.size(); i++)
                        System.out.println("RR + REL: " + returnList.get(i) + "  \t" + searchList.get(i).relevance);
                */
                if (node.children.get(2).operator.equals(Operator.desc))
                    Collections.reverse(returnList);
            }
        }
        else
        {
            // Om vi nu inte ska sortera
            if (searchList != null)
                for (int i = 0; i < searchList.size(); i++)
                    returnList.add(searchList.get(i).doc);

            /*
            // Printar listan
            if (returnList != null)
                for (int i = 0; i < returnList.size(); i++)
                    System.out.println("RR + REL: " + returnList.get(i) + "  \t" + searchList.get(i).relevance);
            */
        }

        // Och äntligen kan vi returna den!
        return returnList;
    }

    public ArrayList<DocRel> searchWord(Node node2)
    {
        // Initirera lite variablar!
        ArrayList<DocRel> left = null;
        ArrayList<DocRel> right = null;
        ArrayList<DocRel> combined = new ArrayList<DocRel>();
        Container con;
        DocRel temp;

        // En stor clusterfuck, dock inte värre än gamla Infix

        // Om vi kommit till ett ORD, där det endast finns ett barn, special cas
        if (node2.children != null && node2.children.size() == 1)
        {
            // Skapar en ny Container med det vi hittar i hashmappen
            con = mapCon.get(node2.children.get(0).word);
            // Om vi hittade något
            if (con != null)
            {
                // Så skapar vi en DocRel object för det och skickar in allt i den.
                for (int i = 0; i < con.doc.size(); i++)
                {
                    temp = new DocRel(con.doc.get(i), 0.0);
                    combined.add(temp);
                }
            }
            return combined;
        }
        // Annars om vi inte har några barn så är vi ju också vid ett ord, längst ner, så vi söker och gör samma sak!
        else if (node2.children == null)
        {
            con = mapCon.get(node2.word);
            if (con == null)
                return null;
            else
            {
                for (int i = 0; i < con.doc.size(); i++)
                {
                    temp = new DocRel(con.doc.get(i), 0.0);
                    combined.add(temp);
                }
            }
        }
        // Annars om vi har två barn, så har vi mera att söka!
        else if (node2.children.size() == 2)
        {
            // Om våra barn inte har några barn, så kollar vi cachen, för då är ju dem ord!
            if (node2.children.get(0).children == null && node2.children.get(1).children == null)
            {
                // Kollar cachen med orden, returnar resultatet direkt om den hittar något!
                String s1 = operatorToChar(node2.operator.toString());
                String s2 = operatorToChar(node2.operator.toString());
                s1 = s1 + " " + node2.children.get(0).word + " " + node2.children.get(1).word;
                s2 = s2 + " " + node2.children.get(1).word + " " + node2.children.get(0).word;
                if (cache.containsKey(s1))
                {
                    combined = cache.get(s1);
                    return combined;
                }
                else if (!node2.operator.equals(Operator.minus))
                {
                    if (cache.containsKey(s2))
                    {
                        combined = cache.get(s2);
                        return combined;
                    }
                }
            }

            // Annars ropar vi på oss själva djupare ned!
            left = searchWord(node2.children.get(0));
            right = searchWord(node2.children.get(1));

            // Variablar som kommer till nytta!
            Document doc1;
            Document doc2;

            // Om vi ska plussa så vill vi ju lägga till allt som förekommer i båda dokumenten
            if (node2.operator.equals(Operator.plus))
            {
                // Vilket ställer krav på att båda orden faktiskt måste ha sökresultat
                if (left != null && right != null)
                {
                    // Och vi får helt enkelt loopa igenom skiten, och om den liknar varandra, så adderar vi!
                    for (int i = 0; i < left.size(); i++)
                    {
                        doc1 = left.get(i).doc;
                        for (int j = 0; j < right.size(); j++)
                        {
                            doc2 = right.get(j).doc;
                            if (doc1.equals(doc2))
                                combined.add(left.get(i));
                        }
                    }
                }
            }
            // Om det är union, så vill vi ju lägga till allt, men dubletter skippar vi!
            else if (node2.operator.equals(Operator.union))
            {
                // Om en är tom så är det ju bara att lägga in andra, vice versa
                if (left != null && right == null)
                    combined.addAll(left);
                else if (left == null && right != null)
                    combined.addAll(right);
                // Men om ingen av dem är tom
                else if (left != null && right != null)
                {
                    // Så lägger vi till vänstra, och loopar igenom varje dokument
                    // Och om den hittar ett dokument i högra som inte finns i vänstra, så läggs den till också!
                    combined.addAll(left);
                    boolean exists;
                    for (int i = 0; i < right.size(); i++)
                    {
                        exists = false;
                        doc1 = right.get(i).doc;
                        for (int j = 0; j < combined.size(); j++)
                        {
                            doc2 = combined.get(j).doc;
                            if (doc1.equals(doc2))
                                exists = true;
                        }
                        if (!exists)
                        {
                            combined.add(right.get(i));
                        }
                    }
                }
            }
            // Men om vi har minus
            else if (node2.operator.equals(Operator.minus))
            {
                // Om högra inte har några resultat så lägger vi till alla i vänstra, om den såklart inte är null
                if (right == null && left != null)
                    combined.addAll(left);
                // Så länge inte vänstra är null
                // Annars så går vi genom alla igen, och om om dokumentet inte finns i högra, alltså bara i vänstra, så läggs det in
                else if (left != null)
                {
                    boolean exists;
                    for (int i = 0; i < left.size(); i++)
                    {
                        exists = false;
                        for (int j = 0; j < right.size(); j++)
                        {
                            if (left.get(i).doc.equals(right.get(j).doc))
                                exists = true;
                        }
                        if (!exists)
                            combined.add(left.get(i));
                    }
                }
            }
        }

        // Beräknar relevance, if satsen är så att vi faktiskt beräknar på rätt värden
        if (node2.children != null && node2.children.size() == 2 && node2.children.get(0).children == null && node2.children.get(0) != null && node2.children.get(1) != null)
        {
            // Vi går igenom varje dokument
            for (int i = 0; i < combined.size(); i++)
            {
                // Hämtar Containerna som har keyen som matchar ordet
                Container conLeft = mapCon.get(node2.children.get(0).word);
                Container conRight = mapCon.get(node2.children.get(1).word);

                // Om containern inte är null och dess occurrence inte är null så sätter vi leftOccurrence som antal dokument det ocucrrade i
                int leftOccurrence = 0;
                if (conLeft != null && conLeft.occurrence.get(combined.get(i).doc) != null)
                    leftOccurrence = conLeft.occurrence.get(combined.get(i).doc);

                // Samam som ovan fast för "högra" ordet
                int rightOccurrence = 0;
                if (conRight != null && conRight.occurrence.get(combined.get(i).doc) != null)
                    rightOccurrence = conRight.occurrence.get(combined.get(i).doc);

                // Totala antalet ord i det specifika dokumentet
                int total;
                total = totalWords.get(combined.get(i).doc);

                // Storleken på antalet dokument som ordet inträffade i
                int leftSize = 0;
                int rightSize = 0;
                if (right != null)
                    rightSize = right.size();
                if (left != null)
                    leftSize = left.size();

                // Beräknar enligt formeln
                double leftOperation1 = ((double) leftOccurrence / total);
                double leftOperation2 = 0;
                if (leftSize != 0)
                    leftOperation2 = leftOperation1 * Math.log10((double)totalWords.size() / (double)leftSize);

                double rightOperation1 = ((double) rightOccurrence / total);
                double rightOperation2 = 0;
                if (rightSize != 0)
                    rightOperation2 = rightOperation1 * Math.log10((double)totalWords.size() / (double)rightSize);

                // Om det är "minus" så tar vi bara vänstra operationen, annars så är det vänstra + högra
                if (node2.operator.equals(Operator.minus))
                    combined.get(i).relevance = leftOperation2;
                else
                    combined.get(i).relevance = leftOperation2 + rightOperation2;
            }
        }

        // Samma sak fast för bara ett ord i special cases
        // T.ex.
        // nightmare
        // | | a b c <-- Sista ordet
        else if (node2.children == null && (node2.parent.children.size() == 1 || (node2.parent.children.size() == 2 && node2.parent.children.get(0).word == null) || (node2.parent.children.size() == 3)))
        {
            for (int i = 0; i < combined.size(); i++)
            {
                Container conTemp = mapCon.get(node2.word);
                int occurrence = 0;
                if (conTemp != null && conTemp.occurrence.get(combined.get(i).doc) != null)
                    occurrence = conTemp.occurrence.get(combined.get(i).doc);
                int total = 0;
                total = totalWords.get(combined.get(i).doc);
                int size = 1;
                if (combined != null)
                    size = combined.size();
                double op1 = ((double) occurrence / total);
                double op2 = op1 * Math.log10(500 / size);

                combined.get(i).relevance = op2;
            }
        }

        // Sparar i cachen, om vi nu är på rätt plats i rekursiven, alltså att vi har två ord och en operator
        if (node2.children != null && node2.children.get(0).children == null && node2.children.get(1).children == null)
        {
            String s1 = operatorToChar(node2.operator.toString());
            s1 = s1 + " " + node2.children.get(0).word + " " + node2.children.get(1).word;
            cache.put(s1, combined);

            // Om det inte är minus så sparar vi även "reversen"
            if (!node2.operator.equals(Operator.minus))
            {
                String s2 = operatorToChar(node2.operator.toString());
                s2 = s2 + " " + node2.children.get(1).word + " " + node2.children.get(0).word;
                cache.put(s2, combined);
            }
        }

        // Äntligen returnar vi svaret!
        return combined;
    }

    public String infix(String query)
    {
        // Bort med whitespace!
        query = query.trim();
        // Splitta så vi kan jobba me den
        String[] queryParts = query.split(" ");
        // Special cases
        if (queryParts.length == 0)
            return "";
        if (queryParts.length == 1)
            return "Query(" + queryParts[0] + ")";
        if (queryParts.length == 4 && queryParts[1].toUpperCase().equals("ORDERBY"))
            return "Query((" + queryParts[0] + ") " + queryParts[1].toUpperCase() + " " + queryParts[2].toUpperCase() + " " + queryParts[3].toUpperCase() + ")";

        // Variablar som komemr till nytta!
        int lastStolenOperator = 0;
        String returnString = "";

        // Loopar igenom ALLT
        for (int i = 0; i < queryParts.length; i++)
        {
            // Om dett och nästa är en operator som inte är Orderby, så lägger vi till en parantes i starten
            if (isOperator(queryParts[i]) && isOperator(queryParts[i + 1]) && !queryParts[i].toUpperCase().equals("ORDERBY"))
                returnString = "(" + returnString;
            // Annars om detta är en operator och nästa inte är det och inte INTE är orderby
            else if (isOperator(queryParts[i]) && !isOperator(queryParts[i + 1]) && !queryParts[i].toUpperCase().equals("ORDERBY"))
            {
                // Så lägger vi till ordet efter i, operatorn och ordet två efter i
                returnString = returnString + "(" + queryParts[i + 1] + " " + queryParts[i] + " " + queryParts[i + 2] + ")";
                // Och om det inte är första gången vi gör detta så lägger vi även på en parantes i slutet!
                if (lastStolenOperator != 0)
                {
                    returnString = returnString + ")";
                }
                // Special case, om vi har mer längd kvar
                if (queryParts.length > i + 3)
                {
                    // Om vi nästa ord inte ÄR LIKA MED ORDERBY så lägger vi till en operator som matchar "lastStoleOperator" positionen
                    // Vi snor alltså en operator från början av splitten
                    if (!queryParts[i + 3].toUpperCase().equals("ORDERBY"))
                        returnString = returnString + " " + queryParts[lastStolenOperator] + " ";

                    // Ökar lastStolenOperator
                    lastStolenOperator++;
                    // Special case
                    // Om nästa inte är en operator, och inte är orderby
                    // T.ex.: | + nightmare test index <-- INDEX
                    if (!isOperator(queryParts[i + 3]) && !queryParts[i + 3].toUpperCase().equals("ORDERBY"))
                    {
                        // Så lägger vi till den också
                        returnString = returnString + queryParts[i + 3] + ")";
                        lastStolenOperator++;
                    }
                }
            }
            // Men om vi nu äntligen kommit till orderby så lägger vi till det och dess property samt direction
            else if (queryParts[i].toUpperCase().equals("ORDERBY"))
            {
                returnString = returnString + " " + queryParts[i].toUpperCase() + " "  + queryParts[i + 1].toUpperCase() + " " + queryParts[i + 2].toUpperCase();
            }
        }

        // Och slutligen lägger vi till detta lilla krav med
        returnString = "Query(" + returnString + ")";

        // Returnar!
        return returnString;
    }

    // Förklarar sig själv
    public String operatorToChar(String s)
    {
        if (s.equals("plus"))
            return "+";
        else if (s.equals("minus"))
            return "-";
        else if (s.equals("union"))
            return "|";
        else if (s.equals("orderby"))
            return "ORDERBY";
        else if (s.equals("popularity"))
            return "POPULARITY";
        else if (s.equals("relevance"))
            return "RELEVANCE";
        else if (s.equals("asc"))
            return "ASC";
        else if (s.equals("desc"))
            return "DESC";
        else
            return "?";
    }

    // Förklarar sig själv
    public boolean isOperator(String s)
    {
        return (s.equals("+") || s.equals("-") || s.equals("|"));
    }
}

// Gammal infix metod, funkade nästan perfekt, men var för lånmg och krånglig att följa så jag skrotade den
// Använde dock parse trädet!

/*
    public String infix(String query)
    {
        query = query.trim();
        if (query.length() == 0)
            return null;

        sb.setLength(0);
        sb.trimToSize();

        String[] queryParts = query.split(" ");

        if (queryParts.length == 1)
        {
            sb.append("Query((" + node.children.get(0).word + "))");
            return sb.toString();
        }

        sb.append("Query(");
        int prefixOperators = 0;
        for (int i = 0; i < queryParts.length; i++)
            if (isOperator(queryParts[i]) && isOperator(queryParts[i + 1]))
                sb.append("(");

        iterator = node.getDeepest();

        int startSize = queryParts.length;
        while (startSize > 0)
        {
            startSize--;

            if (iterator.children != null && iterator.children.size() != 0)
                nextWordInfix(0);
            else
                nextWordInfix(nextParent());
        }
        //if (queryParts.length >= 4 && queryParts[queryParts.length - 3].toUpperCase().equals("ORDERBY"))
        sb.append(")");

        return sb.toString();
    }

    public void nextWordInfix(int n)
    {
        if (iterator.children != null)
        {
            for (int i = n; i < iterator.children.size(); i++)
            {
                if (iterator.nodeType == NodeType.T)
                {
                    if (i == 1)
                    {
                        sb.append(" " + operatorToChar(iterator.operator.toString()) + " ");
                        iterator = iterator.children.get(i);
                        nextWordInfix(0);
                        return;
                    }
                    iterator = iterator.children.get(i);
                    nextWordInfix(0);
                    return;
                }
                else if (iterator.nodeType == NodeType.E)
                {
                    if (i == 1)
                    {
                        System.out.println("AT E: " + iterator.children.size());
                        sb.append(" " + operatorToChar(iterator.operator.toString()));
                        sb.append(" " + operatorToChar(iterator.children.get(i).operator.toString()));
                        nextWordInfix(i + 1);
                    }
                    else if (i == 2)
                    {
                        sb.append(" " + operatorToChar(iterator.children.get(i).operator.toString()));
                    }
                    return;
                }
            }
        }
        else
        {
            if (iterator.parent.children.size() == 1)
                sb.append("(" + iterator.word + ")");
            else if (iterator.parent.children.indexOf(iterator) == 0)
                sb.append("(" + iterator.word);
            else if (iterator.parent.children.indexOf(iterator) == 1)
            {
                sb.append(iterator.word + ")");
                if (node.getDeepest().children.get(1) != iterator)
                    sb.append(")");
            }
            return;
        }

        if (iterator.nodeType != NodeType.E)
            nextWordInfix(nextParent());
        else
        {
            return;
        }
    }

    public int nextParent()
    {
        if (iterator.nodeType == NodeType.E)
        {
            int lastParentIndex = iterator.parent.children.indexOf(iterator);
            iterator = iterator.parent;
            return lastParentIndex + 1;
        }
        else
        {
            int lastParentIndex = iterator.parent.children.indexOf(iterator);
            iterator = iterator.parent;
            return lastParentIndex + 1;
        }
    }*/

