import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TinySearchEngine implements TinySearchEngineBase
{
    // Min simpla main() funktion
    public static void main(String[] args) throws Exception{
        TinySearchEngineBase searchEngine = new TinySearchEngine();
        Driver.run(searchEngine);
    }

    // Skapar en global ArrayList som endast tar emot WordAttribute
    ArrayList<WordAttribute> list = new ArrayList<WordAttribute>();

    // Min WordAttribute class. Lagrar Word samt en ArrayList som innehåller Attributes.
    public class WordAttribute
    {
        public Word word;
        public ArrayList attributes = new ArrayList();

        public WordAttribute(Word word)
        {
            this.word = word;
        }

        public void insertAttributes(Attributes attributes)
        {
            this.attributes.add(attributes);
        }
    }

    public void insert(Word word, Attributes attr)
    {
        // Min Insert, skapar en ny WordAttribute och sparar ordet.
        // Skickar därefter det till binaryInsert som sätter in den i ArrayListen list.
        // Därefter lägger vi till alla attributes.
        WordAttribute wa = new WordAttribute(word);
        WordAttribute a = binaryInsert(wa);
        a.insertAttributes(attr);
    }

    public List<Document> search(String query)
    {
        // Tar bort whitespace samt skapar lite standard variablar vi kommer behöva
        query = query.trim();
        String property = "";
        String direction = "";
        Attributes attr;
        Attributes attrNext;
        WordAttribute wa = new WordAttribute(null);
        WordAttribute tempwa;

        // Splittar strängen samt sätter order-positionen till första.
        String[] parts = query.split(" ");
        int orderbyStringPosition = parts.length - 1;

        // Om vi faktiskt skrivit in något
        if (query.length() != 0)
        {
            // Loopar tills vi hittar orderby eller tills det tar slut, går att göra på andra sätt, men ja, såhär blev det!
            for (int i = 0; i < parts.length; i++)
            {
                // Om vi hittar "orderby" så
                if (parts[i].compareTo("orderby") == 0)
                {
                    // Om "orderby" hittas på första platsen så finns inget sökord.
                    // Så vi skriver ut felet och returnar null.
                    if (i == 0)
                    {
                        System.err.println("Ditt 'orderby' kan inte ligga på första platsen.");
                        return null;
                    }
                    // Annars så kollar vi så att längden har plats för både "property" och "direction", samt sätter respektive variable som det.
                    else if (parts.length == i + 3)
                    {
                        // Kollar så att property och direction har giltiga värden
                        if ((parts[i + 1].equals("popularity") || parts[i + 1].equals("occurrence") || parts[i + 1].equals("count")) && parts[i + 2].equals("asc") || parts[i + 2].equals("desc"))
                        {
                            orderbyStringPosition = i;
                            property = parts[i + 1];
                            direction = parts[i + 2];
                            i = parts.length;
                        }
                        // Annars skriver vi ut felet och returnar null.
                        else
                        {
                            System.err.println("Du angav ogiltiga property eller direction.");
                            return null;
                        }
                    }
                    // Annars skriver vi ut felet och returnar null.
                    else
                    {
                        System.err.println("Antingen glömde du ange ett sökord, eller glömde ange property och direction.");
                        return null;
                    }
                }
            }
        }
        // Annars skriver vi ut felet och returnar null.
        else
        {
            System.err.println("Du måste ange ett sökord.");
            return null;
        }

        // Fram tills "orderby" så söker vi efter varje ordet
        for (int i = 0; i <= orderbyStringPosition; i++)
        {
            tempwa = binarySearch(parts[i]);
            // Om sökningen gav resultat
            if (tempwa != null)
            {
                wa.attributes.addAll(tempwa.attributes);

                // Var menat att ta bort dubletter om vi inte har count, men den fuckar ur totalt.
                // Så jag skiter i den då tiden börjar rinna ut.
//                // Om vi har orderby och "count" så lägger vi till alla dokument
//                if (property.compareTo("count") == 0 )
//                {
//                    wa.attributes.addAll(tempwa.attributes);
//                }
//                // Annars så sorterar vi ut dem och sparar det med lägst occurrence om det är samma dokument. Detta förkortar ned tiden extremt.
//                // Våra värden kommer in i minst först om det är samma dokument, för det är så sökningen sker.
//                else
//                {
//                    System.out.println("KOM IN SOM GUSTAF");
//                    for (int j = 0; j < tempwa.attributes.size() - 2; j++)
//                    {
//                        attr = (Attributes) tempwa.attributes.get(j);
//                        attrNext = (Attributes) tempwa.attributes.get(j + 1);
//                        if (attr.document.compareTo(attrNext.document) == 0)
//                        {
//                            tempwa.attributes.remove(j + 1);
//                            j--;
//                        }
//                    }
//                    // Efter att vi rensat, insertar vi dem bara i attributeArrayen
//                    wa.attributes.addAll(tempwa.attributes);
//                }
            }
        }

        // Om vi efter sökningen INTE hittat något ord så är det onödigt att gå vidare, så vi returnar null.
        if (wa == null)
            return null;
        // Annars har vi fått resultat!
        else
        {
            // Vi skapar en lista som alla dokument kommer tas emot i.
            List<Document> searchedList = new ArrayList<Document>();

            // Om vi hittade en "orderby" så är orderbyStringPosition inte sist, alltså skall det sorteras!
            if (orderbyStringPosition != parts.length - 1)
            {
                searchedList = bubbleSort(wa, property, direction);
                return searchedList;
            }

            // Annars lägger vi bara till dem i en lista och tar bort dubletter.
            else
            {
                for (int i = 0; i < wa.attributes.size(); i++)
                {
                    attr = (Attributes) wa.attributes.get(i);
                    if (!searchedList.contains(attr.document))
                    {
                        //System.out.println("OC: " + attr.occurrence + " POP: " + attr.document.popularity);
                        searchedList.add(attr.document);
                    }
                }
            }

            // Och slutligen så returnar vi!
            return searchedList;
        }
    }

    public List<Document> bubbleSort(WordAttribute temp, String property, String direction)
    {
        // Skapar variablar som kommer behövas!
        int size;
        boolean swapped = true;
        Attributes attr;
        Attributes attrNext;
        int[] countArray = new int[temp.attributes.size()];
        int tempCount;

        // Avkommentera så printas listan som kommer in i bubbleSort tillsammans med lite nyttiga värden!
        /*for (int i = 0; i < temp.attributes.size(); i++)
        {
            attr = (Attributes) temp.attributes.get(i);
            System.out.println("1DOC: " + attr.document + "OC: " + attr.occurrence + " POP: " + attr.document.popularity);
        }*/

        // Om vi vill räkna så görs det i sökningen. Vi går igenom VARJE ord tills vi når slutet och räknar dem
        // i en separat array där varje index motsvarar det i listan.
        // tar även bort dubletter.
        // Förklaras lättast med papper och penna, så kommenterar inte mer än så här.
        if (property.compareTo("count") == 0)
        {
            for (int i = 0; i < temp.attributes.size() - 1; i++)
            {
                attr = (Attributes) temp.attributes.get(i);
                for (int j = i + 1; j < temp.attributes.size(); j++)
                {
                    attrNext = (Attributes) temp.attributes.get(j);
                    if (attr.document.compareTo(attrNext.document) == 0)
                    {
                        countArray[i]++;
                        temp.attributes.remove(j);
                        j--;
                    }
                }
            }
        }

        // Vår gamla kära bubbelsort!
        size = temp.attributes.size() - 2;
        while (size >= 0 && swapped == true)
        {
            swapped = false;
            //for (int i = 0; i <= temp.attributes.size() - 2; i++)
            for (int i = 0; i <= size; i++)
            {
                // Kollar värden samt vilken proprty och agerar därefter.
                attr = (Attributes) temp.attributes.get(i);
                attrNext = (Attributes) temp.attributes.get(i + 1);

                // Kommer sorteras som ASC
                if (property.compareTo("popularity") == 0)
                {
                    if (attr.document.popularity > attrNext.document.popularity)
                    {
                        temp.attributes.set(i, attrNext);
                        temp.attributes.set(i + 1, attr);

                        swapped = true;
                    }
                }

                // Kommer sorteras som DESC pga vi vill behålla minsta "occurrence" så vart det såhär för att behålla dem gamla looparna.
                else if (property.compareTo("occurrence") == 0)
                {
                    if (attr.occurrence < attrNext.occurrence)
                    {
                        temp.attributes.set(i, attrNext);
                        temp.attributes.set(i + 1, attr);

                        swapped = true;
                    }
                }

                // Kommer sorteras som ASC
                else if (property.compareTo("count") == 0)
                {
                    if (countArray[i] > countArray[i + 1])
                    {
                        tempCount = countArray[i];

                        temp.attributes.set(i, attrNext);
                        temp.attributes.set(i + 1, attr);

                        countArray[i] = countArray[i + 1];
                        countArray[i + 1] = tempCount;

                        swapped = true;
                    }
                }
                else
                    return null;
            }
            size = size - 1;
        }

        /*
        Popularity och count är i "standard" ASC
        Occurrence är i "standard" DESC
        Ett och samma ord kan ha olika Occurrence. Jag tar det som är MINST.
        Plockar även bort dubletter här.
        */
        List<Document> returnList = new ArrayList<Document>();
        for (int i = temp.attributes.size() - 1; i >= 0; i--)
        {
            attr = (Attributes) temp.attributes.get(i);
            if (!returnList.contains(attr.document))
            {
                // Avkommentera så printas listan som är sorterad tillsammans med lite nyttiga värden, DOCK INNAN ASC/DESC APPLIATS!!
                // System.out.println("DOC: " + attr.document + "OC: " + attr.occurrence + " POP: " + attr.document.popularity + " COUNT: " + countArray[i]);
                returnList.add(attr.document);
            }
        }

        // PGA hur det vart med Occurrence så är ASC / DSC lite olika beroende på propertyn som det sorteras med
        // Hade förmodligen kunnat undvikits, men ja, vart som det vart och det funkar så!
        if (direction.compareTo("desc") == 0)
        {
            if (property.compareTo("occurrence") == 0 )
                Collections.reverse(returnList);
        }
        else
            if (property.compareTo("popularity") == 0 || property.compareTo("count") == 0)
                Collections.reverse(returnList);

        // Åååååååså returnar vi, sorterad och VACKER!
        return returnList;
    }

    public WordAttribute binaryInsert(WordAttribute wa)
    {
        // Vanligt "binaryInsert", while loopar tills den hittar matchande ord-värde eller tills den nått slutet och då hittat "bästa plats" att inserta på
        int low = 0;
        int high = list.size();
        WordAttribute tmp;
        while (low != high)
        {
            int mid = (low + high) / 2;
            tmp = list.get(mid);
            int compare = wa.word.word.compareTo(tmp.word.word);

            // Om vi hittar ordet så lägger så returnar vi bara WordAttributen, lägga till behövs inte, är ju samma ord!
            if (compare == 0)
                return tmp;
            else if (compare < 0)
                high = mid;
            else
                low = mid + 1;
        }
        // Lägger till vår WordAttribute på bästa matchande lista om det inte redan finns!
        list.add(low, wa);
        return wa;
    }

    public WordAttribute binarySearch(String query)
    {
        int low = 0;
        int high = list.size();
        WordAttribute tmp;
        while (low != high)
        {
            int mid = (low + high) / 2;
            tmp = list.get(mid);
            int compare = query.compareTo(tmp.word.word);

            // Om vi hittar ordet returnar vi WordAttributen det befinner sig i!
            if (compare == 0)
                return tmp;
            else if (compare < 0)
                high = mid;
            else
                low = mid + 1;
        }
        // Annars om vi nått slutet så returnar vi null, inget hittades!
        return null;
    }
}
