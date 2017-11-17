import java.util.Random;

public class ValjPolylinje {
	public static final Random rand = new Random ();
	public static final int ANTAL_POLYLINJER = 10;
	
	public static void main (String[] args)
	{
		// skapa ett antal slumpm�ssiga polylinjer
		Polylinje[] polylinjer = new Polylinje[ANTAL_POLYLINJER];
		for (int i = 0; i < ANTAL_POLYLINJER; i++)
		{
			polylinjer[i] = slumpPolylinje();
		}
		
		// visa polylinjerna
		for (int i = 0; i < ANTAL_POLYLINJER; i++)
		{
			System.out.println(polylinjer[i]);
		}

		// best�m den kortaste av de polylinjer som �r gula
		// visa den valda polylinjen
		
		// Kollar f�rst om det ens existerar en gul polylinje eftersom de slumpas fram.
		// Om inte s� skriver programmet helt enkelt ut att ingen gul polylinje finns
		int pos = 0;
		// Loopar tills vi hittar en gul polylinje eller tills inga fler polylinjer finns
		while(pos < polylinjer.length && polylinjer[pos].getFarg() != "gul")
		{
			pos++;
		}
		
		// Om vi n�r det faktum att pos �r detsamma som polylinjens l�ngd s� finns inga gula polylinjer.
		if(pos == polylinjer.length)
		{
			System.out.println("Ingen gul polylinje existerar.");
		}
		
		// Skapar lite nya variablar som kommer beh�vas
		// En ny polylinje som inneh�ller den pos som vi hittade i ovans polylinje
		// Allts� kopierar den gula polylinje vi vet om och antar att den �r den kortaste
		Polylinje minGulPolylinje = polylinjer[pos];
		double minLangd = minGulPolylinje.langd();
		
		// Forts�tter att loopa igenom resten av polylinjerna
		while (pos < polylinjer.length)
		{
			// Om polylinjen �r gul s� och dess l�ngd �r kortare �n den nuvarande sparade polylinjens
			// S� ers�tter vi den gamla med den nya!
			if (polylinjer[pos].getFarg() == "gul")
			{
				// Om den nya polylinjens l�ngd �r kortare s� byter vi ut den kortaste l�ngden till dess
				if(polylinjer[pos].langd() < minLangd)
				{
					minLangd = polylinjer[pos].langd();
					minGulPolylinje = polylinjer[pos];
				}
			}
			pos++;
		}
		// Skriver ut den kortaste polylinjen
		System.out.println("Den kortaste polylinjen �r: " + minGulPolylinje + " med l�ngden " + minLangd);
	}
	
	// slumpPunkt returnerar en punkt med ett slumpm�ssigt namn, som �r en stor bokstav i
	// det engelska alfabetet, och slumpm�ssiga koordinater.
	public static Punkt slumpPunkt()
	{
		String n = "" + (char) (65 + rand.nextInt(26));
		int x = rand.nextInt (11);
		int y = rand.nextInt (11);
		return new Punkt (n, x, y);
	}
	
	// slumpPolylinje returnerar en slumpm�ssig polylinje, vars f�rg �r antingen bl�, eller r�d
	// eller gul. Namn p� polylinjens h�rn �r stora bokst�ver i det engelska alfabetet. Tv� h�rn
	// kan inte ha samma namn.
	public static Polylinje slumpPolylinje ()
	{
		// skapa en tom polylinje, och l�gg till h�rn till den
		Polylinje polylinje = new Polylinje();
		// Polylinjen kommer ha minst 2 punkter + upp till 6st till. (0 -> 6 == 7)
		int antalHorn = 2 + rand.nextInt(7);
		int antalValdaHorn = 0; // antal h�rn/punkter vi skapat
		boolean[] valdaNamn = new boolean[26]; // En bool med en plats f�r varje bokstav i alfabetet
		// ett och samma namn kan inte f�rekomma flera g�nger
		Punkt valdPunkt = null;
		char valtChar = 0;
		
		// Medans antal h�rn vi skapat �r mindre �n antalet h�rn som skall vara i polylinjen
		while (antalValdaHorn < antalHorn)
		{
			// Skapar en ny slumpad punkt och ger den namn samt kordinater
			valdPunkt = slumpPunkt();
			valtChar = valdPunkt.getNamn().charAt(0);
			// Kollar s� att dens namn inte redan finns i en annan punkt.
			if (valdaNamn[valtChar - 65] == false)
			{
				// Om den inte finns, s� l�gger vi till den i polylinjen 
				// samt s�tter dens bokstavs boolean som true s� att vi inte kan skapa en till punkt med samma namn
				polylinje.laggTill(valdPunkt);
				valdaNamn[valtChar - 65] = true;
				// Sj�lvklart g�r vi vidare till n�sta plats i polylinjen som d� skall fyllas
				antalValdaHorn++;
			}
		}
		// S�tter helt enkelt polylinjens f�rg i slut�ndan samt returnerar hela polylinjen.
		String[] farger = {"bl�" ,"gul" ,"r�d"};
		int fargPosition = rand.nextInt(3);
		polylinje.setFarg(farger[fargPosition]);

		return polylinje;
	}
}
