/*
MerPersonligaUppgifter.java

Detta �r en p�byggnad p� det ursprungliga PersonligaUppgifter programmet, mycket �r �ndrat och mycket �r tillagt.
*/

/*
Fr�gor

Varf�r m�ste 'in.nextLine();' skrivas p� raderna innan man vill mata in strings, annars funkar det inte ?
Men det m�ste endast skrivas in ovanf�r f�rsta stringen om man har tv� st i rad?
*/

class MerPersonligaUppgifter
{
    public static void main(String[] args)
    throws Exception
	{
		java.util.Scanner in = new java.util.Scanner(System.in);

		System.out.println("Detta program sparar de v�rden du anger nedan i en .txt fil.");

		System.out.println("Vilket �r f�ddes du?: ");
		int yearOfBirth = in.nextInt();

		in.nextLine();
		System.out.println("Vilken m�nad f�ddes du? (Januari, Februari, ...): ");
		String monthOfBirth = in.nextLine();

		System.out.println("Vilket datum f�ddes du? (1, 2, 3, ...): ");
		int dateOfBirth = in.nextInt();

		in.nextLine();
		System.out.println("Vad �r ditt f�rnamn?: ");
		String firstname = in.nextLine();

		System.out.println("Vad �r ditt efternamn?: ");
		String lastname = in.nextLine();

		java.io.PrintWriter saveToFile = new java.io.PrintWriter ("MerPersonligaUppgifter.txt");
		saveToFile.println("F�rnamn: " + firstname);
		saveToFile.println("Efternamn: " + lastname);
		saveToFile.println("F�delse�r: " + yearOfBirth);
		saveToFile.println("F�delsem�nad: " + monthOfBirth);
		saveToFile.println("F�delsedag: " + dateOfBirth);
		saveToFile.flush ();

		System.out.println ("Dina v�rden har sparats i filen 'MerPersonligaUppgifter.txt'");
	}
}