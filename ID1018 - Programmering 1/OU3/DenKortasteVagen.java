class DenKortasteVagen
{
	public static int[] middleStations(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		// Anta ett startv�rde som �r kortast, i detta fall v�gen 0 -> 0
		int[] shortestWay = {0, 0};

		// G� igenom varje m�jlig kombination av v�garna
		for (int z2 = 0; z2 < xToZ2.length; z2++)
		{
			// Testar dem "inre" arrayens l�ngd, inte den yttre!
			for (int z3 = 0; z3 < z3ToY.length; z3++)
			{
				// Om summan av de nya platserna �r mindre �n de gamla s� ers�tter vi dem
				if ((xToZ2[z2] + z2ToZ3[z2][z3] + z3ToY[z3]) < (xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]]))
				{
					shortestWay[0] = z2;
					shortestWay[1] = z3;
				}
			}
		}

	// Returnerar den kortaste v�gen
	return shortestWay;
	}

	// Ber�kna den kortaste v�gens str�cka
	public static double shortestWayLength(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		// Ber�kna kortaste v�gen igen och returnera sedan summan av de platser som �r kortaste v�gen
		int[] shortestWay = middleStations(xToZ2, z2ToZ3, z3ToY);
		return xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]];
	}
}