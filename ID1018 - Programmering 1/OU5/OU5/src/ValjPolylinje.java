import java.util.Random;

public class ValjPolylinje {
	public static final Random rand = new Random ();
	public static final int ANTAL_POLYLINJER = 10;
	
	public static void main (String[] args)
	{
		// skapa ett antal slumpmässiga polylinjer
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

		// bestäm den kortaste av de polylinjer som är gula
		// visa den valda polylinjen
		
		// Kollar först om det ens existerar en gul polylinje eftersom de slumpas fram.
		// Om inte så skriver programmet helt enkelt ut att ingen gul polylinje finns
		int pos = 0;
		// Loopar tills vi hittar en gul polylinje eller tills inga fler polylinjer finns
		while(pos < polylinjer.length && polylinjer[pos].getFarg() != "gul")
		{
			pos++;
		}
		
		// Om vi når det faktum att pos är detsamma som polylinjens längd så finns inga gula polylinjer.
		if(pos == polylinjer.length)
		{
			System.out.println("Ingen gul polylinje existerar.");
		}
		
		// Skapar lite nya variablar som kommer behövas
		// En ny polylinje som innehåller den pos som vi hittade i ovans polylinje
		// Alltså kopierar den gula polylinje vi vet om och antar att den är den kortaste
		Polylinje minGulPolylinje = polylinjer[pos];
		double minLangd = minGulPolylinje.langd();
		
		// Fortsätter att loopa igenom resten av polylinjerna
		while (pos < polylinjer.length)
		{
			// Om polylinjen är gul så och dess längd är kortare än den nuvarande sparade polylinjens
			// Så ersätter vi den gamla med den nya!
			if (polylinjer[pos].getFarg() == "gul")
			{
				// Om den nya polylinjens längd är kortare så byter vi ut den kortaste längden till dess
				if(polylinjer[pos].langd() < minLangd)
				{
					minLangd = polylinjer[pos].langd();
					minGulPolylinje = polylinjer[pos];
				}
			}
			pos++;
		}
		// Skriver ut den kortaste polylinjen
		System.out.println("Den kortaste polylinjen är: " + minGulPolylinje + " med längden " + minLangd);
	}
	
	// slumpPunkt returnerar en punkt med ett slumpmässigt namn, som är en stor bokstav i
	// det engelska alfabetet, och slumpmässiga koordinater.
	public static Punkt slumpPunkt()
	{
		String n = "" + (char) (65 + rand.nextInt(26));
		int x = rand.nextInt (11);
		int y = rand.nextInt (11);
		return new Punkt (n, x, y);
	}
	
	// slumpPolylinje returnerar en slumpmässig polylinje, vars färg är antingen blå, eller röd
	// eller gul. Namn på polylinjens hörn är stora bokstäver i det engelska alfabetet. Två hörn
	// kan inte ha samma namn.
	public static Polylinje slumpPolylinje ()
	{
		// skapa en tom polylinje, och lägg till hörn till den
		Polylinje polylinje = new Polylinje();
		// Polylinjen kommer ha minst 2 punkter + upp till 6st till. (0 -> 6 == 7)
		int antalHorn = 2 + rand.nextInt(7);
		int antalValdaHorn = 0; // antal hörn/punkter vi skapat
		boolean[] valdaNamn = new boolean[26]; // En bool med en plats för varje bokstav i alfabetet
		// ett och samma namn kan inte förekomma flera gånger
		Punkt valdPunkt = null;
		char valtChar = 0;
		
		// Medans antal hörn vi skapat är mindre än antalet hörn som skall vara i polylinjen
		while (antalValdaHorn < antalHorn)
		{
			// Skapar en ny slumpad punkt och ger den namn samt kordinater
			valdPunkt = slumpPunkt();
			valtChar = valdPunkt.getNamn().charAt(0);
			// Kollar så att dens namn inte redan finns i en annan punkt.
			if (valdaNamn[valtChar - 65] == false)
			{
				// Om den inte finns, så lägger vi till den i polylinjen 
				// samt sätter dens bokstavs boolean som true så att vi inte kan skapa en till punkt med samma namn
				polylinje.laggTill(valdPunkt);
				valdaNamn[valtChar - 65] = true;
				// Självklart går vi vidare till nästa plats i polylinjen som då skall fyllas
				antalValdaHorn++;
			}
		}
		// Sätter helt enkelt polylinjens färg i slutändan samt returnerar hela polylinjen.
		String[] farger = {"blå" ,"gul" ,"röd"};
		int fargPosition = rand.nextInt(3);
		polylinje.setFarg(farger[fargPosition]);

		return polylinje;
	}
}
