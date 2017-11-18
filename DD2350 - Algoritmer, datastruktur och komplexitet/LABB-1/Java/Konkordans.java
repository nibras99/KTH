import java.io.*;
import java.util.ArrayList;

public class Konkordans {
    public static void main(String[] args) throws IOException {
        long startTime;
        long endTime;

        // Hela text filen
        File fileKorpus = new File("");

        // Alla ord, ett per rad
        File fileData = new File("");

        // Alla hashes
        File fileHash = new File("");

        // Om vi inte tar emot ett arg så räknar vi det som att hashfilen skall genereras
        // Om det är större än 1 så är det ju mer än ett ord....
        if (args.length == 0) {
            startTime = System.nanoTime();
            createHashFile(fileData, fileHash);
            endTime = System.nanoTime();
            //System.out.println("Hashningen tog: " + (endTime - startTime) / 1e6 + "ms");

            return;
        } else if (args.length != 1) {
            throw new Error("Ange ett ord för att söka, eller inget för att genererar filerna.");
        }
        

        startTime = System.nanoTime();
        // Läser in hashade värden från fil till array. (Fast storlek)
        int[] hashValues = getHashValues(fileHash);

        // Söker efter ett ord som kommit in som args
        ArrayList<String> foundWord = findWord(args[0], hashValues, fileData);
        endTime = System.nanoTime();
	System.out.println("Du sökte på: " + args[0]);
        System.out.println("Sökningen tog: " + (endTime - startTime) / 1e6 + "ms");

        // Öppnar läsare, för att eventuellt skriva ut alla träffar
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileKorpus), "ISO-8859-1"));

        // Beroende på sökningens resultat så hanteras det olika.
        // Om vi hittar något och anv. eventuellt vill skriva ut, så görs det helt enkelt med hjälp ut av printLines funktionen.
        if (foundWord.get(0).equalsIgnoreCase("not found")) {
            startTime = System.nanoTime();
            System.out.println("Sökningen matchade inga ord i textfilen.");
        } else if (foundWord.size() > 25) {
            char read;
            System.out.println("Det finns " + foundWord.size() + " förekomster av ordet.");
            System.out.println("Vill du skriva ut alla? (y/n)");
            read = (char) System.in.read();

            startTime = System.nanoTime();
            if (read == 'y' || read == 'Y') {
                bufferedReader.skip(Long.parseLong(foundWord.get(0)));
                for (int i = 0; i < foundWord.size(); i++)
                    printLines(Long.parseLong(foundWord.get(i)), fileKorpus, args[0].length());
            }
        } else {
            startTime = System.nanoTime();
            System.out.println("Det finns " + foundWord.size() + " förekomster av ordet.");
            bufferedReader.skip(Long.parseLong(foundWord.get(0)));
            for (int i = 0; i < foundWord.size(); i++)
                printLines(Long.parseLong(foundWord.get(i)), fileKorpus, args[0].length());
        }
        endTime = System.nanoTime();
        // System.out.println("Utskrivningen tog: " + (endTime - startTime) / 1e6 + "ms");
	System.out.println();
	System.out.println();
        bufferedReader.close();
    }

    // Skriver ut och hanterar specialfallen om vi är i början av filen eller i slutet av filen.
    private static void printLines(long pos, File fileKorpus, int wordLength) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileKorpus), "ISO-8859-1"));

        long startPos = 0;
        if (pos >= 30) {
            startPos = pos - 30;
        }

        bufferedReader.skip(startPos);

        char readChar;
        StringBuilder sb = new StringBuilder();
        for (long i = startPos; i < pos + wordLength + 30; i++) {
            readChar = (char) bufferedReader.read();
            if ((int) readChar == ' ' || (int) readChar == '\t' || (int) readChar == '\n' || (int) readChar == '\r') {
                sb.append(" ");
            } else {
                sb.append(readChar);
            }
        }
        bufferedReader.close();
        System.out.println(sb.toString());
    }

    // Genererar den hashfil som sparas.
    private static void createHashFile(File fileData, File fileHash) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileData), "ISO-8859-1"));

        FileWriter fileWriter = new FileWriter(fileHash);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String line;
        String[] lineSplitted;

        long lineNumber = 0;
        char a = 0;
        char b = 0;
        char c = 0;

        String wordToHash;
        String previousHash = "";
        long hashedValue;

        // Läser alla rader i "ut" filen, tar reda på ordet, hashar upp till dem 3 första bokstäverna, samt sparar det i den nya filen "hash" tillsammans med positionen på första bokstaven i "ut" filen
        while ((line = bufferedReader.readLine()) != null) {
            lineSplitted = line.split(" ");

            if (lineSplitted[0].length() >= 3) {
                wordToHash = lineSplitted[0].substring(0, 3);
            } else {
                wordToHash = lineSplitted[0];
            }

            if (!previousHash.equalsIgnoreCase(wordToHash)) {
                if (wordToHash.length() == 3) {
                    a = parseChar(wordToHash.charAt(0));
                    b = parseChar(wordToHash.charAt(1));
                    c = parseChar(wordToHash.charAt(2));
                } else if (wordToHash.length() == 2) {
                    a = parseChar(wordToHash.charAt(0));
                    b = parseChar(wordToHash.charAt(1));
                    c = 0;
                } else if (wordToHash.length() == 1) {
                    a = parseChar(wordToHash.charAt(0));
                    b = 0;
                    c = 0;
                }

                hashedValue = ((int) a * 900) + ((int) b * 30) + ((int) c);

                bufferedWriter.write(Long.toString(hashedValue) + " " + lineNumber);
                bufferedWriter.newLine();
                previousHash = wordToHash;
            }
            lineNumber = lineNumber + line.length() + 1;
        }
        bufferedReader.close();
        bufferedWriter.close();
    }

    // Vid sökning så läser denna funktion in hash värden till en fixed size array.
    // Platsen i arrayen motsvarar hashen, samt dess värde är positionen till början av raden för första förekomsten i filen "ut"
    private static int[] getHashValues(File fileHash) throws IOException {
        int[] hashValues = new int[27000];

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileHash), "ISO-8859-1"));

        String line;
        String[] lineSplitted;
        while ((line = bufferedReader.readLine()) != null) {
            lineSplitted = line.split(" ");
            hashValues[Integer.parseInt(lineSplitted[0])] = Integer.parseInt(lineSplitted[1]);
        }

        bufferedReader.close();
        return hashValues;
    }

    // Går igenom hashArrayen och hittar vart nästa hash finns. Om inget finns så räknar vi det som sista platsen.
    private static int nextValue(int currentValue, int[] hashArray) {
        for (int i = currentValue + 1; i < hashArray.length; i++) {
            if (hashArray[i] > 0)
                return i;
        }
        return 26999;
    }

    // Konverterar till char. 1=a, 2=b, 3=c...., 29=ö för latmanshashning
    private static char parseChar(char letter) {
        if (letter == 'å') {
            return (char) 27;
        } else if (letter == 'ä') {
            return (char) 28;
        } else if (letter == 'ö') {
            return (char) 29;
        } else {
            return (char) (letter - 96);
        }
    }

    // Själva sökfunktionen
    private static ArrayList<String> findWord(String word, int[] hashedArray, File fileData) throws IOException {
        word = word.toLowerCase();
        String prefix = word;

        // bufferedReader för att hitta alla occurences när position bestämts
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileData), "ISO-8859-1"));

        // raf för att snabbt kunna hoppa fram och tillbaka i filen vid binärsökning.
        // Inge mycket data kommer att läsas med denna funktion, så den gör jobbet enklast för osss
        RandomAccessFile raf = new RandomAccessFile(fileData, "r");

        char a = 0;
        char b = 0;
        char c = 0;

        int hashedValue;

        if (word.length() > 3)
            prefix = word.substring(0, 3);

        if (prefix.length() == 3) {
            a = parseChar(prefix.charAt(0));
            b = parseChar(prefix.charAt(1));
            c = parseChar(prefix.charAt(2));
        } else if (prefix.length() == 2) {
            a = parseChar(prefix.charAt(0));
            b = parseChar(prefix.charAt(1));
            c = 0;
        } else if (prefix.length() == 1) {
            a = parseChar(prefix.charAt(0));
            b = 0;
            c = 0;
        }

        hashedValue = ((int) a * 900) + ((int) b * 30) + ((int) c);

        // Bestämmer startpositionerna. i blir själva hashningen av ordet.
        // j blir nästa funna ord.
        int i = hashedArray[hashedValue];
        int j = hashedArray[nextValue(hashedValue, hashedArray)];
        int m;

        ArrayList<String> returnData = new ArrayList<>();

        // Binär sökning med hjälp av raf.
        String s[] = new String[2];
        String ss;
        while (j - i > 1000) {
            m = (i + j) / 2;
            raf.seek(m);
            if ((raf.readLine()) != null) {
                s = raf.readLine().split(" ");
            }
            if (s[0].compareTo(word) < 0) {
                i = m;
            } else {
                j = m;
            }
        }

        // Vi har nu hittat ungefär vart i filen ordet eventuellt finns.
        // Nu skall det kanske läsas en del, så hoppa dit med bufferedReader!
        bufferedReader.skip(i);

        // Läser tills vid hittar första eventuella occurence.
        // Därefter läser vi alla som matchar, sparar dem i en array och sedan returnar.
        // Vid ingen matchning, returnar vi att inget ord hittades!
        while (true) {
            s = bufferedReader.readLine().split(" ");
            

            
            if (s[0].compareTo(word) == 0) {
                while (s[0].compareTo(word) == 0) {
                    returnData.add(s[1]);
                    if ((ss = bufferedReader.readLine()) != null)
                        s = ss.split(" ");
                    else
                        break;
                }
                break;
            }

            if (s[0].compareToIgnoreCase(word) == 1) {
                returnData.add("not found");
                break;
            }
        }

        return returnData;
    }
}
