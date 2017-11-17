/*

------ ADDERA -------
String slutSumma
int tempTal1
int tempTal2
int carrySiffra
int tempSumma
int startPosition1 = tal1.length - 1
int startPosition2 = tal2.length - 1

int giltigaTalet
while (inget av talen är slut)
{
	tempTal1 = charen vid startpunkt1 - 48
	tempTal2 = charen vid startpunkt2 - 48

	tempSumma = carrySiffra plus tempTal1 plus tempTal2
	giltigaTalet = Öveblivna talet då tempSumma delas med 10.. t.ex. 19/10 = 1.9, alltså är överblivna 9
	slutSumma = giltigaTalet plus slutSumma
	carrySiffra = tempSumma delat med 10.. t.ex. 19/10 = 1.9, kapar vid punkt ger oss carry 1

	minska startpositionerna med 1
}

while(startposition2 är slut men inte 1)
{
	tempTal1 = charen vid startpunkt1 - 48
	tempSumma = carrySiffra plus tempTal1
	giltigaTalet = Öveblivna talet då tempSumma delas med 10
	slutSumma = giltigaTalet plus slutSumma
	carrySiffra = tempSumma delat med 10

	minska startposition 1 med 1
}

while(startposition1 är slut men inte 2)
{
	tempTal2 = charen vid startpunkt2 - 48
	tempSumma = carrySiffra plus tempTal2;
	giltigaTalet = Öveblivna talet då tempSumma delas med 10
	slutSumma = giltigaTalet plus slutSumma;
	carrySiffra = tempSumma delat med 10

	minska startposition 2 med 1
}

if (det fortfarande finns en carrySiffra)
{
	slutSumma = carrySiffra plus slutSumma
}


------ SUBTRAHERA ------
int tempTal1
int tempTal2
int lånatAntal
int giltigaTalet
int lånadeVid
String slutSumma
int startPosition1 = tal3.length - 1
int startPosition2 = tal4.length - 1

while(inget av talen är slut)
{
	tempTal1 = charen vid startpunkt1 - 48 men även minus det lånade antalet tal
	tempTal2 = charen vid startpunkt1 - 48

	if (startPosition1 är densamma som den plats vi lånade av)
	{
		så minskar vi det lånade antalet med 1
	}

	if (tempTal1 är mindre än tempTal2)
	{
		Så måste vi låna...
		lånatAntal = lånatAntal plus 1
		lånadeVid = startPosition1 minus 1 // Den plats vi lånade av!
		tempTal1 = tempTal1 plus 10 (Låna av ett tiotal tal till ett ental så minskar ju tiotalet med 1, men vi får ju 10 ental!)
	}
	slutSumma = tempTal1 minus tempTal2 plus slutSumma

	minska startpositionerna med 1
}

if (om det första talet innehåller fler "tal" än det andra, dvs t.ex. första är 10 och andra är 1)
{
	while (startposition2 är slut men inte 1)
	{
		tempTal1 = charen vid startpunkt1 - 48 men även minus det lånade antalet tal

		if (startPosition1 är densamma som den plats vi lånade av)
		{
			så minskar vi det lånade antalet med 1
		}

		if (tempTal1 är mindre än 0 så måste vi låna)
		{
			lånatAntal = lånatAntal plus 1
			lånadeVid = startPosition1 minus 1 // Den plats vi lånade av!
			tempTal1 = tempTal1 plus 10
		}
		slutSumma = tempTal1 plus slutSumma
		minska startposition1 med 1
	}
}

*/
import java.util.*;

class OperationerMedNaturligaHeltalGivnaSomTeckenstrangar
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("OPERATIONER MED NATURLIGA HELTAL GIVNA SOM TECKENSTRANGAR\n");

		System.out.println("Skriv in 2st naturliga heltal som skall adderas: ");
		String tal1 = input.next();
		String tal2 = input.next();
		String summa = addera(tal1, tal2);
		System.out.print("Summan av de två talen är: " + summa);

		System.out.println();
		System.out.println();
		System.out.println();


		System.out.println("Skriv in 2st naturliga heltal som skall subtraheras, Tal 1 >= Tal2: ");
		String tal3 = input.next();
		String tal4 = input.next();
		String differans = subtrahera(tal3, tal4);
		System.out.println("Differansen av de två talen är: " + differans);

		visa(tal1, tal2, summa, '+');
		visa(tal3, tal4, differans, '-');
	}


	// addera tar emot två naturliga heltal givna som teckensträngar, och returnerar deras
	// summa som en teckensträng.
	public static String addera(String tal1, String tal2)
	{
		// Anger några startvariabler som kommer att behövas
		String slutSumma = "";
		int tempTal1 = 0;
		int tempTal2 = 0;
		int carrySiffra = 0;
		int tempSumma = 0;
		int startPosition1 = tal1.length() - 1; // Vi vill ju i ett fall där talet är 123 början på platsen där 3 är, alltså plats "2", men svaret vi får ut längden 3, så vi tar den minus 1.
		int startPosition2 = tal2.length() - 1;
		int giltigaTalet = 0;

		// Medans inget av talen är "slut"
		while(startPosition1 >= 0 && startPosition2 >= 0)
		{
			tempTal1 = tal1.charAt(startPosition1) - 48;
			tempTal2 = tal2.charAt(startPosition2) - 48;

			tempSumma = carrySiffra + tempTal1 + tempTal2;
			giltigaTalet = tempSumma % 10; // Öveblivna talet då tempSumma delas med 10.. t.ex. 19/10 = 1.9, alltså är överblivna 9
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10; // I och med att carrySiffra är en Int så kommer den helt enkelt att kapa talet vid punkten/kommat, vilket ger oss en perfect carry!

			// Minska med 1 och gå till nästa plats i stringen
			startPosition1--;
			startPosition2--;
		}

		// Då tal 2 är slut men inte 1
		while(startPosition1 >= 0)
		{
			tempTal1 = tal1.charAt(startPosition1) - 48;
			tempSumma = carrySiffra + tempTal1;
			giltigaTalet = tempSumma % 10;
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10;

			startPosition1--;
		}

		// Då tal 1 är slut men inte 2
		while(startPosition2 >= 0)
		{
			tempTal2 = tal2.charAt(startPosition2) - 48;
			tempSumma = carrySiffra + tempTal2;
			giltigaTalet = tempSumma % 10;
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10;

			startPosition2--;
		}

		// Om det finns en carry fortfarande så måste även den sättas in.
		if(carrySiffra > 0)
		{
			slutSumma = carrySiffra + slutSumma;
		}
		return slutSumma.toString();
	}


	// subtrahera tar emot två naturliga heltal givna som teckensträngar, och returnerar
	// deras differens som en teckensträng.
	// Det första heltalet är inte mindre än det andra heltalet.
	public static String subtrahera(String tal3, String tal4)
	{
		int tempTal1 = 0;
		int tempTal2 = 0;
		int lånatAntal = 0;
		int lånadeVid = -1;
		String slutSumma = "";
		int startPosition1 = tal3.length() - 1; // Plats 0 finns ju med också!
		int startPosition2 = tal4.length() - 1;

		if(Integer.parseInt(tal4) > Integer.parseInt(tal3))
			return '-' + subtrahera(tal4, tal3);


		// Medans inget av talen är "slut"
		while(startPosition1 >= 0 && startPosition2 >= 0)
		{
			tempTal1 = (tal3.charAt(startPosition1) - 48) - lånatAntal; // Om vi tidigare har lånat av talet så måste vi ta hänsyn till det och minska nuvarande temp1
			tempTal2 = tal4.charAt(startPosition2) - 48;

			// Om vi lånade av startPositionens tal som vi är på nu så måste vi minska det talet med 1, vi kommer aldrig behöva låna mer än 1.
			if (startPosition1 == lånadeVid)
			{
				lånatAntal = lånatAntal - 1;
			}

			// Om det övre talet är mindre än det nedre så måste vi låna!
			if (tempTal1 < tempTal2)
			{
				lånatAntal = lånatAntal + 1;
				lånadeVid = startPosition1 - 1; // Den plats vi lånar av, inte den vi är på!
				tempTal1 = tempTal1 + 10;
			}
			slutSumma = tempTal1 - tempTal2 + slutSumma;

			// Minska båda positionerna med 1 och gå vidare till nästa
			startPosition1--;
			startPosition2--;
		}

			// basically gör om samma sak som ovan fast vi tar ju minus 0 i slutändan, vi tar endast hänsyn till den övre talet och lånat / låna.
			while (startPosition1 >= 0)
			{
				tempTal1 = (tal3.charAt(startPosition1) - 48) - lånatAntal;

				if (startPosition1 == lånadeVid)
				{
					lånatAntal = lånatAntal - 1;
				}

				if (tempTal1 < 0)
				{
					lånatAntal = lånatAntal + 1;
					lånadeVid = startPosition1 - 1;
					tempTal1 = tempTal1 + 10;
				}
				slutSumma = tempTal1 + slutSumma;
				startPosition1--;
			}
		return slutSumma.toString();
	}


	// visa visar två givna naturliga heltal, och resultatet av en aritmetisk operation
	// utförd i samband med hetalen
	public static void visa (String tal1, String tal2, String resultat, char operator)
	{
		// sätt en lämplig längd på heltalen och resultatet
		int len1 = tal1.length ();
		int len2 = tal2.length ();
		int len = resultat.length ();
		int maxLen = Math.max (Math.max (len1, len2), len);
		tal1 = sattLen (tal1, maxLen - len1);
		tal2 = sattLen (tal2, maxLen - len2);
		resultat = sattLen (resultat, maxLen - len);
		// visa heltalen och resultatet
		System.out.println ("  " + tal1);
		System.out.println ("" + operator + " " + tal2);
		for (int i = 0; i < maxLen + 2; i++)
		System.out.print ("-");
		System.out.println ();
		System.out.println ("  " + resultat + "\n");
	}


	// sattLen lägger till ett angivet antal mellanslag i början av en given sträng
	public static String sattLen (String s, int antal)
	{
		StringBuilder sb = new StringBuilder (s);
		for (int i = 0; i < antal; i++)
		sb.insert (0, " ");
		return sb.toString ();
	}
}