import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

class MinstaHeltalet
{
	public static void main(String[] args) {
	    System.out.println("Det misnta heltalet \n");
	    Random random = new Random();

		// Slumpar fram en rimlig enkel testsekvens
		//int längd = 16;
		int längd = random.nextInt(100 + 1); // 0 -> 100
		int[] sekvens = new int[längd];
		for (int i = 0; i < längd; i++)
		{
			sekvens[i] = random.nextInt(100 + 1);
		}

		//int[] sekvens = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 0, 18, 19}; // 19 sekvenser, minsta på 17
		//int[] sekvens = {1, 2, 3, 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}; // 16 sekvenser, minsta på 4

		System.out.print("Inmatninsvärden:\n");
		System.out.println(java.util.Arrays.toString(sekvens));

		System.out.println("\nSvaret med originalmetoden: " + min(sekvens));


		System.out.println("\nSvaret med uppdateringstrategin: " + minUpdate(sekvens));
    }

	public static int min(int[] element) throws IllegalArgumentException
	{
		if (element.length == 0)
		{
			throw new IllegalArgumentException ("Arrayen med heltal är tom!");
		}

		// hör ihop med spårutskriften 2:
		// int antalVarv = 1;

		int[] sekvens = element;
		int antaletPar = sekvens.length / 2;
		int antaletOparadeElement = sekvens.length % 2;
		int antaletTankbaraElement = antaletPar + antaletOparadeElement;
		int[] delsekvens = new int[antaletTankbaraElement];
		int i = 0;
		int j = 0;

		while (antaletPar > 0) // Första felet, den loopar för alltid eftersom sekvens.length aldrig minskar under hälften av den originella sekvensens längd. antaletPar kommer dock det.
		{
			// skilj ur en delsekvens med de tänkbara elementen
			i = 0;
			j = 0;
			while (j < antaletPar)
			{
				delsekvens[j++] = (sekvens[i] < sekvens[i + 1]) ? sekvens[i] : sekvens[i + 1];
				i += 2;
			}

			if (antaletOparadeElement == 1)
			{
				//delsekvens[j] = (element == sekvens) ? sekvens[sekvens.length - 1] : sekvens[antaletPar * 2]; // Andra felet, satte alltid in originalsekvensens sista värde
				delsekvens[j] = sekvens[antaletPar * 2];
			}

			// utgå nu ifrån delsekvensen
			sekvens = delsekvens;
			antaletPar = antaletTankbaraElement / 2;
			antaletOparadeElement = antaletTankbaraElement % 2;
			antaletTankbaraElement = antaletPar + antaletOparadeElement;


			// spårutskrift 1 – för att följa sekvensen
			//System.out.println (java.util.Arrays.toString (sekvens));

			// spårutskrift 2 - för att avsluta loopen i förväg
			// (för att kunna se vad som händer i början)
			//if (antalVarv++ == 10){
			//	System.exit (0);
			//}

		}
		return sekvens[0]; // är det enda återstående tänkbara elementet
	}

	public static int minUpdate(int[] elements) throws IllegalArgumentException
	{
		if (elements.length == 0) {
			throw new IllegalArgumentException("Arrayen med heltal är tom!");
		}

		int result = elements[0];
		for (int current : elements) {
			result = (current > result) ? result : current;
		}

		return result;
    }
}