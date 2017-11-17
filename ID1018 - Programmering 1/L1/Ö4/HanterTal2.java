/*
Hantera valfri m�ngd tal.

Detta program �r en egen version av HanterTal.java programmet.
I detta program kan du sj�lv ange v�lja antal tal du vill ange.

*/

class HanterTal2
{
	public static void main(String[] args)
	{
		System.out.println("HANTERA TAL VERSION 2");
		java.util.Scanner input = new java.util.Scanner (System.in);

		System.out.println("Hur m�nga tal vill du mata in?: ");
		int antalTal = input.nextInt();

		double[] talen = new double[antalTal];
		int counter = 0;
		if (antalTal > 0) {
			// L�ter anv�ndern skriv ain sina tal
			System.out.println("Ange talen: ");
			while (counter < antalTal)
			{
				System.out.print(counter + 1 + " --> ");
				talen[counter] = input.nextDouble();
				counter = counter + 1;
			}

			// Ber�knar summan av alla tal
			double summa = 0;
			for (int miniCounter = 0; miniCounter < talen.length; miniCounter++)
			{
				summa = summa + talen[miniCounter];
			}

			// Ber�knar medelv�rdet av talen
			double medel = summa / talen.length;

			// Ber�knar det minsta talet
			double minsta = talen[0];
			double minstaPos = 0;
			for (int miniCounter = 1; miniCounter < talen.length; miniCounter++)
			{
				if (talen[miniCounter] < minsta) {
					minsta = talen[miniCounter];
					minstaPos = miniCounter;
				}
			}

			// Ordningssorterar talen i �kande ordning
			double temporaryHolder = 0;
			int temporaryMinimal = 0;

			// Array r�knaren, kommer g� igenom varje plats, f�rutom den sista, s� den automatiskt kommer bli sorterad pga talet kommer switchas l�ngre ner och d�rmed kommer de tv� sista hamna r�tt.
			for (int arrayCounter = 0; arrayCounter < talen.length -1; arrayCounter++)
			{
				// Antar att det minimala talet ligger i platsen som array r�knaren befinner sig i, de andra �r redan kontrollerade och ordnade.
				temporaryMinimal = arrayCounter;
				// B�rjar g� igenom de �terst�ende talen, b�rjar p� talet efter det array r�knaren befinner sig i.
				for (int miniCheckerCounter = arrayCounter+1; miniCheckerCounter < talen.length; miniCheckerCounter++)
				{
					// Om talet som r�knaren kommer till �r mindre �n talet som integern temporaryMinimal redan h�ller s� ers�tts den.
					// Du kan �ven byta riktning p� '<' nedan s� kommer den att sortera allt i sjunkande ordning.
					if (talen[miniCheckerCounter] < talen[temporaryMinimal])
					{
						temporaryMinimal = miniCheckerCounter;
					}

				}

				// Byter plats talen om det uppt�ckta minimala talet inte redan ligger i platsen array r�knaren befinner sig i.
				if (temporaryMinimal != arrayCounter)
				{
					temporaryHolder = talen[arrayCounter];
					talen[arrayCounter] = talen[temporaryMinimal];
					talen[temporaryMinimal] = temporaryHolder;
				}
			}


			// Skriver ut allting.
			System.out.println("Summan \t\t --> \t" + summa);
			System.out.println("Medelv�rdet\t --> \t" + medel);
			System.out.println("Minsta talet\t --> \t" + minsta);
			for (int miniCounter = 0; miniCounter < talen.length; miniCounter++)
			{
				System.out.println(miniCounter + 1 + " --> " + talen[miniCounter]);
			}
		} else {
			System.out.println("Du angav inga v�rden att r�kna p�.");
		}
	}
}