class DenKortasteVagen
{
	// mellanstationer returnerar en vektor med de mellanstationer som finns på den kortaste
	// vägen. Ordningsnummer av den första mellanstationen finns på index 1, och ordningsnummer
	// av den andra mellanstationen på index 2 i vektorn.
	public static int[] middleStations(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		int[] shortestWay = {0, 0};
		for (int z2 = 1; z2 < xToZ2.length; z2++)
		{
			for (int z3 = 1; z3 < z2ToZ3.length; z3++)
			{
				if ((xToZ2[z2] + z2ToZ3[z2][z3] + z3ToY[z3]) < (xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]]))
				{
					shortestWay[0] = z2;
					shortestWay[1] = z3;
				}
			}
		}
	return shortestWay;
	}

	public static double shortestWayLength(double[]xToZ2, double[][]z2ToZ3, double[]z3ToY)
	{
		int[] shortestWay = middleStations(xToZ2, z2ToZ3, z3ToY);
		return xToZ2[shortestWay[0]] + z2ToZ3[shortestWay[0]][shortestWay[1]] + z3ToY[shortestWay[1]];
	}
}