import java.util.*; // Scanner, Locale

class Temperaturmatning
{
	public static void main(String[] args)
	{
		System.out.println("TEMPERATURM�TNING\n");

		// Inmatningsverktyg
		Scanner input = new Scanner(System.in);

		// Anv�ndaren f�r ange antalet veckor och antalet m�tningar per vecka
		System.out.print("Antal veckor: ");
		int weeks = input.nextInt ();
		System.out.print("Antal m�tningar per vecka: ");
		int measurementsPerWeek = input.nextInt ();

		// En array f�r att lagra temperaturerna
		double[][] temperatures = new double[weeks + 1][measurementsPerWeek + 1];

		// Anv�ndaren f�r mata in alla temperaturer
		for (int week = 1; week <= weeks; week++)
		{
			System.out.println("\nTemperaturer, vecka " + week + ": ");
			for (int measurement = 1; measurement <= measurementsPerWeek; measurement++)
			{
				System.out.print("M�tning " + measurement + " --> ");
				temperatures[week][measurement] = input.nextDouble ();
			}
		}

		System.out.println();

		// Skriver ut alla temperaturer
		System.out.println ("Du har angett f�ljande temperaturer: ");
		for (int week = 1; week <= weeks; week++)
		{
			System.out.print("Vecka " + week + " -->    ");
			for (int measurement = 1; measurement <= measurementsPerWeek; measurement++)
			{
				System.out.print(temperatures[week][measurement] + "  \t");
			}

			System.out.println ();
		}

		System.out.println ();

		// Arrayer f�r den minsta, st�rsta, summan och medeltempen f�r varje vecka.
		double[] minTemp = new double[weeks + 1];
		double[] maxTemp = new double[weeks + 1];
		double[] sumTemp = new double[weeks + 1];
		double[] medTemp = new double[weeks + 1];

		// Tar reda p� minTemp, maxTemp, summa och medelTemp f�r varje vecka separat.
		for (int week = 1; week <= weeks; week++)
		{
			// B�rjar med att ange ett startv�rde att j�mf�ra med som �ven finns i arrayen, i detta fall den f�rsta m�tniongen i varje array.
			minTemp[week] = temperatures[week][1];
			maxTemp[week] = temperatures[week][1];
			for (int mes = 1; mes <= measurementsPerWeek; mes++)
			{
				// Vanliga j�mf�relser, om statementet st�mmer s� ers�tter vi tempen med den nya.
				if (temperatures[week][mes] < minTemp[week])
				{
					minTemp[week] = temperatures[week][mes];
				}

				if (temperatures[week][mes] > maxTemp[week])
				{
					maxTemp[week] = temperatures[week][mes];
				}
				// Summerar hela veckans summa
				sumTemp[week] = sumTemp[week] + temperatures[week][mes];
			}
			// Ber�knar medeltempen p� hela veckan
			medTemp[week] = sumTemp[week] / measurementsPerWeek;
		}

		// Nya variablar f�r den totala min/max/sum/med Tempen
		double minTempTotal = minTemp[1];
		double maxTempTotal = maxTemp[1];
		double sumTempTotal = 0;
		double medTempTotal = 0;

		// Ber�knar totalen p� mycket liknande s�tt som jag ber�knade f�r varje enskild vecka, dock endast med 1 for loop pga attt arrayen �r 1-dimensionell
		for (int week = 1; week <= weeks; week++)
		{
			if (minTempTotal > minTemp[week])
			{
				minTempTotal = minTemp[week];
			}

			if (maxTempTotal < maxTemp[week])
			{
				maxTempTotal = maxTemp[week];
			}

			sumTempTotal = sumTempTotal + sumTemp[week];
		}

		medTempTotal = sumTempTotal / (weeks * measurementsPerWeek);

		// Skriver ut allting i en fin tabell
		System.out.print("Vecka");
		for (int week = 1; week <= measurementsPerWeek; week++)
		{
			System.out.print("\tM#" + week);
		}
		System.out.print("\tminT\tmaxT\tsumT\tmedT");

		for (int week = 1; week <= weeks; week++)
		{
			System.out.print("\n #" + week);
			for (int mes = 1; mes <= measurementsPerWeek; mes++)
			{
				System.out.print("\t" + temperatures[week][mes]);
			}
			System.out.print("\t" + minTemp[week] + "\t" + maxTemp[week] + "\t" + sumTemp[week] + "\t" + medTemp[week]);
		}

		System.out.print("\n\n");
		System.out.print("Totalt");
		System.out.print("\nminTemp = " + minTempTotal);
		System.out.print("\nmaxTemp = " + maxTempTotal);
		System.out.print("\nsumTemp = " + sumTempTotal);
		System.out.print("\nmedTemp = " + medTempTotal);
		System.out.print("\n\n");
	}
}