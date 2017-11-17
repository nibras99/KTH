class DenKortasteVagen
{
	public static int[] middleStations(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		// Anta ett startvärde som är kortast, i detta fall vägen 0 -> 0
		int[] shortestWay = {0, 0};

		// Gå igenom varje möjlig kombination av vägarna
		for (int z2 = 0; z2 < xToZ2.length; z2++)
		{
			// Testar dem "inre" arrayens längd, inte den yttre!
			for (int z3 = 0; z3 < z3ToY.length; z3++)
			{
				// Om summan av de nya platserna är mindre än de gamla så ersätter vi dem
				if ((xToZ2[z2] + z2ToZ3[z2][z3] + z3ToY[z3]) < (xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]]))
				{
					shortestWay[0] = z2;
					shortestWay[1] = z3;
				}
			}
		}

	// Returnerar den kortaste vägen
	return shortestWay;
	}

	// Beräkna den kortaste vägens sträcka
	public static double shortestWayLength(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		// Beräkna kortaste vägen igen och returnera sedan summan av de platser som är kortaste vägen
		int[] shortestWay = middleStations(xToZ2, z2ToZ3, z3ToY);
		return xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]];
	}
}