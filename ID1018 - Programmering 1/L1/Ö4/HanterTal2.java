/*
Hantera valfri mängd tal.

Detta program är en egen version av HanterTal.java programmet.
I detta program kan du själv ange välja antal tal du vill ange.

*/

class HanterTal2
{
	public static void main(String[] args)
	{
		System.out.println("HANTERA TAL VERSION 2");
		java.util.Scanner input = new java.util.Scanner (System.in);

		System.out.println("Hur många tal vill du mata in?: ");
		int antalTal = input.nextInt();

		double[] talen = new double[antalTal];
		int counter = 0;
		if (antalTal > 0) {
			// Låter användern skriv ain sina tal
			System.out.println("Ange talen: ");
			while (counter < antalTal)
			{
				System.out.print(counter + 1 + " --> ");
				talen[counter] = input.nextDouble();
				counter = counter + 1;
			}

			// Beräknar summan av alla tal
			double summa = 0;
			for (int miniCounter = 0; miniCounter < talen.length; miniCounter++)
			{
				summa = summa + talen[miniCounter];
			}

			// Beräknar medelvärdet av talen
			double medel = summa / talen.length;

			// Beräknar det minsta talet
			double minsta = talen[0];
			double minstaPos = 0;
			for (int miniCounter = 1; miniCounter < talen.length; miniCounter++)
			{
				if (talen[miniCounter] < minsta) {
					minsta = talen[miniCounter];
					minstaPos = miniCounter;
				}
			}

			// Ordningssorterar talen i ökande ordning
			double temporaryHolder = 0;
			int temporaryMinimal = 0;

			// Array räknaren, kommer gå igenom varje plats, förutom den sista, så den automatiskt kommer bli sorterad pga talet kommer switchas längre ner och därmed kommer de två sista hamna rätt.
			for (int arrayCounter = 0; arrayCounter < talen.length -1; arrayCounter++)
			{
				// Antar att det minimala talet ligger i platsen som array räknaren befinner sig i, de andra är redan kontrollerade och ordnade.
				temporaryMinimal = arrayCounter;
				// Börjar gå igenom de återstående talen, börjar på talet efter det array räknaren befinner sig i.
				for (int miniCheckerCounter = arrayCounter+1; miniCheckerCounter < talen.length; miniCheckerCounter++)
				{
					// Om talet som räknaren kommer till är mindre än talet som integern temporaryMinimal redan håller så ersätts den.
					// Du kan även byta riktning på '<' nedan så kommer den att sortera allt i sjunkande ordning.
					if (talen[miniCheckerCounter] < talen[temporaryMinimal])
					{
						temporaryMinimal = miniCheckerCounter;
					}

				}

				// Byter plats talen om det upptäckta minimala talet inte redan ligger i platsen array räknaren befinner sig i.
				if (temporaryMinimal != arrayCounter)
				{
					temporaryHolder = talen[arrayCounter];
					talen[arrayCounter] = talen[temporaryMinimal];
					talen[temporaryMinimal] = temporaryHolder;
				}
			}


			// Skriver ut allting.
			System.out.println("Summan \t\t --> \t" + summa);
			System.out.println("Medelvärdet\t --> \t" + medel);
			System.out.println("Minsta talet\t --> \t" + minsta);
			for (int miniCounter = 0; miniCounter < talen.length; miniCounter++)
			{
				System.out.println(miniCounter + 1 + " --> " + talen[miniCounter]);
			}
		} else {
			System.out.println("Du angav inga värden att räkna på.");
		}
	}
}