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
while (inget av talen �r slut)
{
	tempTal1 = charen vid startpunkt1 - 48
	tempTal2 = charen vid startpunkt2 - 48

	tempSumma = carrySiffra plus tempTal1 plus tempTal2
	giltigaTalet = �veblivna talet d� tempSumma delas med 10.. t.ex. 19/10 = 1.9, allts� �r �verblivna 9
	slutSumma = giltigaTalet plus slutSumma
	carrySiffra = tempSumma delat med 10.. t.ex. 19/10 = 1.9, kapar vid punkt ger oss carry 1

	minska startpositionerna med 1
}

while(startposition2 �r slut men inte 1)
{
	tempTal1 = charen vid startpunkt1 - 48
	tempSumma = carrySiffra plus tempTal1
	giltigaTalet = �veblivna talet d� tempSumma delas med 10
	slutSumma = giltigaTalet plus slutSumma
	carrySiffra = tempSumma delat med 10

	minska startposition 1 med 1
}

while(startposition1 �r slut men inte 2)
{
	tempTal2 = charen vid startpunkt2 - 48
	tempSumma = carrySiffra plus tempTal2;
	giltigaTalet = �veblivna talet d� tempSumma delas med 10
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
int l�natAntal
int giltigaTalet
int l�nadeVid
String slutSumma
int startPosition1 = tal3.length - 1
int startPosition2 = tal4.length - 1

while(inget av talen �r slut)
{
	tempTal1 = charen vid startpunkt1 - 48 men �ven minus det l�nade antalet tal
	tempTal2 = charen vid startpunkt1 - 48

	if (startPosition1 �r densamma som den plats vi l�nade av)
	{
		s� minskar vi det l�nade antalet med 1
	}

	if (tempTal1 �r mindre �n tempTal2)
	{
		S� m�ste vi l�na...
		l�natAntal = l�natAntal plus 1
		l�nadeVid = startPosition1 minus 1 // Den plats vi l�nade av!
		tempTal1 = tempTal1 plus 10 (L�na av ett tiotal tal till ett ental s� minskar ju tiotalet med 1, men vi f�r ju 10 ental!)
	}
	slutSumma = tempTal1 minus tempTal2 plus slutSumma

	minska startpositionerna med 1
}

if (om det f�rsta talet inneh�ller fler "tal" �n det andra, dvs t.ex. f�rsta �r 10 och andra �r 1)
{
	while (startposition2 �r slut men inte 1)
	{
		tempTal1 = charen vid startpunkt1 - 48 men �ven minus det l�nade antalet tal

		if (startPosition1 �r densamma som den plats vi l�nade av)
		{
			s� minskar vi det l�nade antalet med 1
		}

		if (tempTal1 �r mindre �n 0 s� m�ste vi l�na)
		{
			l�natAntal = l�natAntal plus 1
			l�nadeVid = startPosition1 minus 1 // Den plats vi l�nade av!
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
		System.out.print("Summan av de tv� talen �r: " + summa);

		System.out.println();
		System.out.println();
		System.out.println();


		System.out.println("Skriv in 2st naturliga heltal som skall subtraheras, Tal 1 >= Tal2: ");
		String tal3 = input.next();
		String tal4 = input.next();
		String differans = subtrahera(tal3, tal4);
		System.out.println("Differansen av de tv� talen �r: " + differans);

		visa(tal1, tal2, summa, '+');
		visa(tal3, tal4, differans, '-');
	}


	// addera tar emot tv� naturliga heltal givna som teckenstr�ngar, och returnerar deras
	// summa som en teckenstr�ng.
	public static String addera(String tal1, String tal2)
	{
		// Anger n�gra startvariabler som kommer att beh�vas
		String slutSumma = "";
		int tempTal1 = 0;
		int tempTal2 = 0;
		int carrySiffra = 0;
		int tempSumma = 0;
		int startPosition1 = tal1.length() - 1; // Vi vill ju i ett fall d�r talet �r 123 b�rjan p� platsen d�r 3 �r, allts� plats "2", men svaret vi f�r ut l�ngden 3, s� vi tar den minus 1.
		int startPosition2 = tal2.length() - 1;
		int giltigaTalet = 0;

		// Medans inget av talen �r "slut"
		while(startPosition1 >= 0 && startPosition2 >= 0)
		{
			tempTal1 = tal1.charAt(startPosition1) - 48;
			tempTal2 = tal2.charAt(startPosition2) - 48;

			tempSumma = carrySiffra + tempTal1 + tempTal2;
			giltigaTalet = tempSumma % 10; // �veblivna talet d� tempSumma delas med 10.. t.ex. 19/10 = 1.9, allts� �r �verblivna 9
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10; // I och med att carrySiffra �r en Int s� kommer den helt enkelt att kapa talet vid punkten/kommat, vilket ger oss en perfect carry!

			// Minska med 1 och g� till n�sta plats i stringen
			startPosition1--;
			startPosition2--;
		}

		// D� tal 2 �r slut men inte 1
		while(startPosition1 >= 0)
		{
			tempTal1 = tal1.charAt(startPosition1) - 48;
			tempSumma = carrySiffra + tempTal1;
			giltigaTalet = tempSumma % 10;
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10;

			startPosition1--;
		}

		// D� tal 1 �r slut men inte 2
		while(startPosition2 >= 0)
		{
			tempTal2 = tal2.charAt(startPosition2) - 48;
			tempSumma = carrySiffra + tempTal2;
			giltigaTalet = tempSumma % 10;
			slutSumma = giltigaTalet + slutSumma;
			carrySiffra = tempSumma / 10;

			startPosition2--;
		}

		// Om det finns en carry fortfarande s� m�ste �ven den s�ttas in.
		if(carrySiffra > 0)
		{
			slutSumma = carrySiffra + slutSumma;
		}
		return slutSumma.toString();
	}


	// subtrahera tar emot tv� naturliga heltal givna som teckenstr�ngar, och returnerar
	// deras differens som en teckenstr�ng.
	// Det f�rsta heltalet �r inte mindre �n det andra heltalet.
	public static String subtrahera(String tal3, String tal4)
	{
		int tempTal1 = 0;
		int tempTal2 = 0;
		int l�natAntal = 0;
		int l�nadeVid = -1;
		String slutSumma = "";
		int startPosition1 = tal3.length() - 1; // Plats 0 finns ju med ocks�!
		int startPosition2 = tal4.length() - 1;

		if(Integer.parseInt(tal4) > Integer.parseInt(tal3))
			return '-' + subtrahera(tal4, tal3);


		// Medans inget av talen �r "slut"
		while(startPosition1 >= 0 && startPosition2 >= 0)
		{
			tempTal1 = (tal3.charAt(startPosition1) - 48) - l�natAntal; // Om vi tidigare har l�nat av talet s� m�ste vi ta h�nsyn till det och minska nuvarande temp1
			tempTal2 = tal4.charAt(startPosition2) - 48;

			// Om vi l�nade av startPositionens tal som vi �r p� nu s� m�ste vi minska det talet med 1, vi kommer aldrig beh�va l�na mer �n 1.
			if (startPosition1 == l�nadeVid)
			{
				l�natAntal = l�natAntal - 1;
			}

			// Om det �vre talet �r mindre �n det nedre s� m�ste vi l�na!
			if (tempTal1 < tempTal2)
			{
				l�natAntal = l�natAntal + 1;
				l�nadeVid = startPosition1 - 1; // Den plats vi l�nar av, inte den vi �r p�!
				tempTal1 = tempTal1 + 10;
			}
			slutSumma = tempTal1 - tempTal2 + slutSumma;

			// Minska b�da positionerna med 1 och g� vidare till n�sta
			startPosition1--;
			startPosition2--;
		}

			// basically g�r om samma sak som ovan fast vi tar ju minus 0 i slut�ndan, vi tar endast h�nsyn till den �vre talet och l�nat / l�na.
			while (startPosition1 >= 0)
			{
				tempTal1 = (tal3.charAt(startPosition1) - 48) - l�natAntal;

				if (startPosition1 == l�nadeVid)
				{
					l�natAntal = l�natAntal - 1;
				}

				if (tempTal1 < 0)
				{
					l�natAntal = l�natAntal + 1;
					l�nadeVid = startPosition1 - 1;
					tempTal1 = tempTal1 + 10;
				}
				slutSumma = tempTal1 + slutSumma;
				startPosition1--;
			}
		return slutSumma.toString();
	}


	// visa visar tv� givna naturliga heltal, och resultatet av en aritmetisk operation
	// utf�rd i samband med hetalen
	public static void visa (String tal1, String tal2, String resultat, char operator)
	{
		// s�tt en l�mplig l�ngd p� heltalen och resultatet
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


	// sattLen l�gger till ett angivet antal mellanslag i b�rjan av en given str�ng
	public static String sattLen (String s, int antal)
	{
		StringBuilder sb = new StringBuilder (s);
		for (int i = 0; i < antal; i++)
		sb.insert (0, " ");
		return sb.toString ();
	}
}