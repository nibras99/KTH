/*
Bensinkostnad.java

Detta program beräknar din bils bensinkostnad per mil, du anger själv in-värdena.
*/

class Bensinkostnad
{
	public static void main(String[] args)
	{
		java.util.Scanner in = new java.util.Scanner(System.in);
		in.useLocale(java.util.Locale.US);

		System.out.print("Vad kostar bensinen per liter? (T.ex. 12.45): ");
		double bensinKostnad = in.nextDouble();
		System.out.print("Hur många liter bensin drar din bil per mil? (T.ex. 0.63): ");
		double literPerMil = in.nextDouble();

		double kostnadPerMil = literPerMil * bensinKostnad;

		System.out.println("Du betalar " + kostnadPerMil + "kr per mil.");
	}
}