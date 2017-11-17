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

*/

import java.util.*;

class BestamDenKortasteVagen extends DenKortasteVagen
{
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args)
	{
		System.out.print("-- BEST�M DEN KORTASTE V�GEN --\n");
		System.out.print("Hur m�nga mellanstationer finns det i Z2? ");
		int stationsZ2 = input.nextInt();
		System.out.print("Hur m�nga mellanstationer finns det i Z3? ");
		int stationsZ3 = input.nextInt();
		int biggestOfZ2Z3 = 0;

		double[] xToZ2 = new double[stationsZ2];
		double[][] z2ToZ3 = new double[stationsZ2][stationsZ3];
		double[] z3ToY = new double[stationsZ3];

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
		System.out.print("X");
		int[] shortestWayStations = DenKortasteVagen.middleStations(xToZ2, z2ToZ3, z3ToY);
		System.out.print("!");
		System.out.println("");
		System.out.println("Den kortaste v�gen best�r av " + DenKortasteVagen.shortestWayLength(xToZ2, z2ToZ3, z3ToY) + " enheter.");
		System.out.println("Den g�r genom punkterna X --> U" + (shortestWayStations[0] + 1) + " --> V" + (shortestWayStations[1] + 1) + " --> Y");
	}
}