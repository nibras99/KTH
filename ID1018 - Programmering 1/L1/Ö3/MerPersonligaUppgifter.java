/*
MerPersonligaUppgifter.java

Detta är en påbyggnad på det ursprungliga PersonligaUppgifter programmet, mycket är ändrat och mycket är tillagt.
*/

/*
Frågor

Varför måste 'in.nextLine();' skrivas på raderna innan man vill mata in strings, annars funkar det inte ?
Men det måste endast skrivas in ovanför första stringen om man har två st i rad?
*/

class MerPersonligaUppgifter
{
    public static void main(String[] args)
    throws Exception
	{
		java.util.Scanner in = new java.util.Scanner(System.in);

		System.out.println("Detta program sparar de värden du anger nedan i en .txt fil.");

		System.out.println("Vilket år föddes du?: ");
		int yearOfBirth = in.nextInt();

		in.nextLine();
		System.out.println("Vilken månad föddes du? (Januari, Februari, ...): ");
		String monthOfBirth = in.nextLine();

		System.out.println("Vilket datum föddes du? (1, 2, 3, ...): ");
		int dateOfBirth = in.nextInt();

		in.nextLine();
		System.out.println("Vad är ditt förnamn?: ");
		String firstname = in.nextLine();

		System.out.println("Vad är ditt efternamn?: ");
		String lastname = in.nextLine();

		java.io.PrintWriter saveToFile = new java.io.PrintWriter ("MerPersonligaUppgifter.txt");
		saveToFile.println("Förnamn: " + firstname);
		saveToFile.println("Efternamn: " + lastname);
		saveToFile.println("Födelseår: " + yearOfBirth);
		saveToFile.println("Födelsemånad: " + monthOfBirth);
		saveToFile.println("Födelsedag: " + dateOfBirth);
		saveToFile.flush ();

		System.out.println ("Dina värden har sparats i filen 'MerPersonligaUppgifter.txt'");
	}
}