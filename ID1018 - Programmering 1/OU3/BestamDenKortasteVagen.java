/*
Algoritmen g�r igenom alla m�jliga alternativ / kombinationer fr�n de vektorer som den ursprunbgliga informationen matades in i.
Den sparar den kortaste v�gen "information" i en ny array.

medans z2 = 0 �r mindre �n antalet stationer i z2, �ka Z2

	medans z3 = 0 �r mindre �n antalet stationer i z3, �ka z3

		om v�rdet som finns sparat i "Kortaste V�gen" �r st�rre �n den v�g som tas fram av z2 och z3

			spara den nya �nformationen i

		st�ng om

	st�ng medans

st�ng medans


PROBLEM
Det finns i ett trafiksystem fyra zoner: Z1, Z2, Z3 och Z4. I zonen Z1 finns endast stationen X, och zonen Z4 omfattar bara
stationen Y. I zonen Z2 finns stationerna U1, U2, �, Um, (m �r ett positivt heltal), och zonen Z3 omfattar stationerna V1, V2, �,
Vn (n �r ett positivt heltal).
Det finns direkta v�gar mellan stationen X och alla stationer i zonen Z2. Zonerna Z2 och Z3 �r v�l kopplade med varandra: det
finns en direkt v�g mellan vilken station som helst i den ena zonen och en godtycklig station i den andra zonen. Det finns
�ven en direkt v�g mellan vilken station som helst i zonen Z3 och stationen Y. Det finns inga andra v�gar mellan givna
stationer..

Anv�ndaren skall kunna ange antal stationer i Z2 och Z3 samt l�ngderna mellan ALLA stationer.

ALGORITM
F�RVILLKOR
Anv�ndaren skall ha matat in alla stationer och dess l�ngder.
EFTERVILLKOR
Algoritmen skall ha best�mt den kortaste v�gen samt dess l�ngd.

STEG I ALGORITMEN - ORD
Algoritmen g�r igenom alla m�jliga alternativ / kombinationer fr�n de vektorer som den ursprunbgliga informationen matades in i.
Den sparar den kortaste v�gen "information" i en ny array.

STEG I ALGORITMEN � PSEUDOKOD
metod int[] middleStations double[]xToZ2 double[][]z2ToZ3 double[]z3ToY
{
	int[] shortestWay == de f�rsta v�gen, anta ett startv�rde
	for (int z2 �r mindre �n antal platser i xToZ2 arrayen, �ka z2 med 1 tills dem har samma v�rde)
	{
		for (int z3 �r mindre �n antal platser i andra arrayens ANDRA/INNRE plats)
		{
			if (summan av nuvarande platser i arrayerna �r mindre �n den sparade kortaste v�gen sedan innan)
			{
				ers�tt den kortaste sparade v�gen med den nya infon
			}
		}
	}
returnera shortestWay;
}

metod double shortestWayLength double[]xToZ2 double[][]z2ToZ3 double[]z3ToY
{
	// Ber�kna kortaste v�gen p� nytt, on�digt, men det skulle tydligen vara i tv� olika metoder
	int[] shortestWay = middleStations(xToZ2, z2ToZ3, z3ToY);
	return kortaste v�gens l�ngd direkt genom att helt enkelt plussa ihop de v�rden som finns i platserna som shortestWay returnerar
}

*/

import java.util.*;

class BestamDenKortasteVagen extends DenKortasteVagen
{
	// Scanner som tar emot inputen av anv�ndaren
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args)
	{
		// Ber anv�ndaren mata in antalet stationer
		System.out.print("-- BEST�M DEN KORTASTE V�GEN --\n");
		System.out.print("Hur m�nga mellanstationer finns det i Z2? ");
		int stationsZ2 = input.nextInt();
		System.out.print("Hur m�nga mellanstationer finns det i Z3? ");
		int stationsZ3 = input.nextInt();

		// Skapar nya arrayer som skall inneh�lla dess v�rden
		double[] xToZ2 = new double[stationsZ2];
		double[][] z2ToZ3 = new double[stationsZ2][stationsZ3];
		double[] z3ToY = new double[stationsZ3];

		// Ber anv�ndaren mata in alla l�ngder p� alla kombinationer
		for (int x = 0; x < stationsZ2; x++)
		{
			System.out.print("Hur l�ng �r stationen X --> U" + (x + 1) + "? ");
			xToZ2[x] = input.nextDouble();
		}

		System.out.println("");

		for (int z2 = 0; z2 < stationsZ2; z2++)
		{
			for (int z3 = 0; z3 < stationsZ3; z3++)
			{
				System.out.print("Hur l�ng �r stationen U" + (z2 + 1) + " --> V" + (z3 + 1) + "? ");
				z2ToZ3[z2][z3] = input.nextDouble();
			}
		}

		System.out.println("");

		for (int y = 0; y < stationsZ3; y++)
		{
			System.out.print("Hur l�ng �r stationen V" + (y + 1) + " --> Y? ");
			z3ToY[y] = input.nextDouble();
		}

		// Anropar metoderna som ber�knar allt och skriver sedan ut det.
		int[] shortestWayStations = DenKortasteVagen.middleStations(xToZ2, z2ToZ3, z3ToY);
		System.out.println("Den kortaste v�gen best�r av " + DenKortasteVagen.shortestWayLength(xToZ2, z2ToZ3, z3ToY) + " enheter.");
		System.out.println("Den g�r genom punkterna X --> U" + (shortestWayStations[0] + 1) + " --> V" + (shortestWayStations[1] + 1) + " --> Y");
	}
}