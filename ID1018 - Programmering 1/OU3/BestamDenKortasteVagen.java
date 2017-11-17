/*
Algoritmen går igenom alla möjliga alternativ / kombinationer från de vektorer som den ursprunbgliga informationen matades in i.
Den sparar den kortaste vägen "information" i en ny array.

medans z2 = 0 är mindre än antalet stationer i z2, öka Z2

	medans z3 = 0 är mindre än antalet stationer i z3, öka z3

		om värdet som finns sparat i "Kortaste Vägen" är större än den väg som tas fram av z2 och z3

			spara den nya ínformationen i

		stäng om

	stäng medans

stäng medans


PROBLEM
Det finns i ett trafiksystem fyra zoner: Z1, Z2, Z3 och Z4. I zonen Z1 finns endast stationen X, och zonen Z4 omfattar bara
stationen Y. I zonen Z2 finns stationerna U1, U2, …, Um, (m är ett positivt heltal), och zonen Z3 omfattar stationerna V1, V2, …,
Vn (n är ett positivt heltal).
Det finns direkta vägar mellan stationen X och alla stationer i zonen Z2. Zonerna Z2 och Z3 är väl kopplade med varandra: det
finns en direkt väg mellan vilken station som helst i den ena zonen och en godtycklig station i den andra zonen. Det finns
även en direkt väg mellan vilken station som helst i zonen Z3 och stationen Y. Det finns inga andra vägar mellan givna
stationer..

Användaren skall kunna ange antal stationer i Z2 och Z3 samt längderna mellan ALLA stationer.

ALGORITM
FÖRVILLKOR
Användaren skall ha matat in alla stationer och dess längder.
EFTERVILLKOR
Algoritmen skall ha bestämt den kortaste vägen samt dess längd.

STEG I ALGORITMEN - ORD
Algoritmen går igenom alla möjliga alternativ / kombinationer från de vektorer som den ursprunbgliga informationen matades in i.
Den sparar den kortaste vägen "information" i en ny array.

STEG I ALGORITMEN – PSEUDOKOD
metod int[] middleStations double[]xToZ2 double[][]z2ToZ3 double[]z3ToY
{
	int[] shortestWay == de första vägen, anta ett startvärde
	for (int z2 är mindre än antal platser i xToZ2 arrayen, öka z2 med 1 tills dem har samma värde)
	{
		for (int z3 är mindre än antal platser i andra arrayens ANDRA/INNRE plats)
		{
			if (summan av nuvarande platser i arrayerna är mindre än den sparade kortaste vägen sedan innan)
			{
				ersätt den kortaste sparade vägen med den nya infon
			}
		}
	}
returnera shortestWay;
}

metod double shortestWayLength double[]xToZ2 double[][]z2ToZ3 double[]z3ToY
{
	// Beräkna kortaste vägen på nytt, onödigt, men det skulle tydligen vara i två olika metoder
	int[] shortestWay = middleStations(xToZ2, z2ToZ3, z3ToY);
	return kortaste vägens längd direkt genom att helt enkelt plussa ihop de värden som finns i platserna som shortestWay returnerar
}

*/

import java.util.*;

class BestamDenKortasteVagen extends DenKortasteVagen
{
	// Scanner som tar emot inputen av användaren
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args)
	{
		// Ber användaren mata in antalet stationer
		System.out.print("-- BESTÄM DEN KORTASTE VÄGEN --\n");
		System.out.print("Hur många mellanstationer finns det i Z2? ");
		int stationsZ2 = input.nextInt();
		System.out.print("Hur många mellanstationer finns det i Z3? ");
		int stationsZ3 = input.nextInt();

		// Skapar nya arrayer som skall innehålla dess värden
		double[] xToZ2 = new double[stationsZ2];
		double[][] z2ToZ3 = new double[stationsZ2][stationsZ3];
		double[] z3ToY = new double[stationsZ3];

		// Ber användaren mata in alla längder på alla kombinationer
		for (int x = 0; x < stationsZ2; x++)
		{
			System.out.print("Hur lång är stationen X --> U" + (x + 1) + "? ");
			xToZ2[x] = input.nextDouble();
		}

		System.out.println("");

		for (int z2 = 0; z2 < stationsZ2; z2++)
		{
			for (int z3 = 0; z3 < stationsZ3; z3++)
			{
				System.out.print("Hur lång är stationen U" + (z2 + 1) + " --> V" + (z3 + 1) + "? ");
				z2ToZ3[z2][z3] = input.nextDouble();
			}
		}

		System.out.println("");

		for (int y = 0; y < stationsZ3; y++)
		{
			System.out.print("Hur lång är stationen V" + (y + 1) + " --> Y? ");
			z3ToY[y] = input.nextDouble();
		}

		// Anropar metoderna som beräknar allt och skriver sedan ut det.
		int[] shortestWayStations = DenKortasteVagen.middleStations(xToZ2, z2ToZ3, z3ToY);
		System.out.println("Den kortaste vägen består av " + DenKortasteVagen.shortestWayLength(xToZ2, z2ToZ3, z3ToY) + " enheter.");
		System.out.println("Den går genom punkterna X --> U" + (shortestWayStations[0] + 1) + " --> V" + (shortestWayStations[1] + 1) + " --> Y");
	}
}