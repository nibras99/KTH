// PersonligaUppgifter.java

// ett program som hanterar personliga uppgifter

/**********************************************************************

Användaren matar in sina personliga uppgifter. Dessa uppgifter sparas
sedan i en fil.

**********************************************************************/

/*
Egna kommentarer

Felet som gör så att det inte går att kompilera.

PersonligaUppgifter.java:34: error: unreported exception FileNotFoundException; must be caught or declared to be thrown
new java.io.PrintWriter ("persUpp.txt");
^


När man tar bort kommentaren på (1) så går programmet att kompilera och starta.
Det avslutas dock efter att man angett sitt födelseår.

När man tar bort (2) så körs programmet korrekt.
Det tar ens namn och födelsedatum som man matar in och sparar dem i en textfil som ligger i samma folder som programmet.
*/


class PersonligaUppgifter
{
    public static void main (String[] args)
                            throws Exception    // (1)
	{
		System.out.println ("PERSONLIGA UPPGIFTER");
		System.out.println ();

		// inmatningsverktyg
		java.util.Scanner    in = new java.util.Scanner (System.in);

		// mata in personliga uppgifter
		System.out.print ("Fodelsear: ");
		int    ar = in.nextInt ();
		 in.nextLine ();    // (2)
 		System.out.print ("Ditt fornamn och efternamn: ");
		String    namn = in.nextLine ();
		System.out.println ();

		// spara uppgifter i en fil
		java.io.PrintWriter    fout =
		          new java.io.PrintWriter ("persUpp.txt");
		fout.println (namn + ": " + ar);
		fout.flush ();

		// ett meddelande
		System.out.println ("Oppna filen persUpp.txt!");
	}
}
