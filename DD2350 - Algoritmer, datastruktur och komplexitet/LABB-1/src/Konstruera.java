import java.io.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Konstruera {
    public static void main(String args[]) throws IOException {

        /*








        SKROT ANVÄNDS EJ
        TESTADE SKAPA EGEN "TOKENIZER"










         */
        File fileText = new File("C:\\Users\\Casper\\Documents\\GitHub\\ADK17\\LABB-1\\korpus");
        //File fileText = new File("/var/temp/korpus");
        File fileRows = new File("C:\\Users\\Casper\\Documents\\GitHub\\ADK17\\LABB-1\\rows");
        //File fileRows = new File("/var/temp/rows");
        File fileHash = new File("C:\\Users\\Casper\\Documents\\GitHub\\ADK17\\LABB-1\\hash");
        //File fileHash = new File("/var/temp/hash");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileText), "ISO-8859-1"));

        PrintWriter fileWriter = new PrintWriter(fileRows, "ISO-8859-1");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        long pos = 0;
        int word;
        boolean lastBreak = true;
        int[] values = new int[256];

        for (int i = 97; i <= 122; i++) {
            values[i] = i;
        }

        for (int i = 65; i <= 90; i++) {
            values[i] = i + 32;
        }

        // http://www.htmlhelp.com/reference/charset/latin1.gif

        // Å Ä Ö
        values[197] = 229;
        values[196] = 228;
        values[214] = 246;

        // å ä ö
        values[229] = 229;
        values[228] = 228;
        values[246] = 246;

        values[230] = 228;
        values[231] = 99;
        values[241] = 110;
        values[248] = 246;
        values[253] = 121;
        values[255] = 121;

        // Egna special
        values[192] = 97;
        values[193] = 97;
        values[194] = 97;
        values[195] = 97;
        values[207] = 105;

        for (int i = 224; i <= 227; i++) {
            values[i] = 97;
        }

        for (int i = 232; i <= 235; i++) {
            values[i] = 101;
        }

        for (int i = 236; i <= 239; i++) {
            values[i] = 105;
        }

        for (int i = 242; i <= 245; i++) {
            values[i] = 111;
        }

        for (int i = 249; i <= 252; i++) {
            values[i] = 117;
        }


        long currPos = 0;
        long posToWrite = 0;
        while ((word = bufferedReader.read()) != -1) {
            if (lastBreak && values[word] != 0)
                posToWrite = currPos;
            /*char[] special = new char[]{10, 13, 32, '.', ',', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '?', '!', ':', 39, ';', '/', '*', 164, 38, 183, 62, 43, 180, '@'};
            if (values[word] == 0) {
                if (!(new String(special).contains(Character.toString((char) word)))) {
                    System.out.println("Special " + (char) word + " ... " + word);
                }
            }*/

            if (values[word] == 0 && !lastBreak) {
                bufferedWriter.write(" " + Long.toString(posToWrite));
                bufferedWriter.newLine();
                lastBreak = true;
            } else if (values[word] != 0) {
                bufferedWriter.write(values[word]);
                lastBreak = false;
            }
            currPos++;
        }

        bufferedWriter.close();
    }
}
