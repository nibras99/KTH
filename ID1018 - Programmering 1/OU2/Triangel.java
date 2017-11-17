import java.util.*;

class Triangel
{
	// Skapar en ny scanner som tar emot input
	static Scanner input = new Scanner(System.in);

	public static double bisektris(double b, double c, double alfa)
	{
		double p = 2 * b * c * Math.cos (alfa / 2);
		double bis = p / (b + c);

		return bis;
	}

	// Huvudmetoden
	public static void main(String[] args)
	{
		// L�ter anv�ndaren g�ra ett val g�llande vad han vill utf�ra f�r ber�kningar
		System.out.println("TRIANGLAR");
		System.out.println("Vad vill du g�ra?");
		System.out.println("1. Ber�kna arean av en triangel.");
		System.out.println("2. Ber�kna en bisektris l�ngd.");
		System.out.println("3. En triangel och dess cirklar.");
		System.out.print("Ditt val: ");

		int choice = input.nextInt();
		System.out.println("");

		// Skapar standardvariablar f�r allt som kan t�nkas f�rekomma utanf�r metoderna
		double lengthA = 0;
		double lengthB = 0;
		double lengthC = 0;
		double angleAB = 0;
		double angleAC = 0;
		double angleBC = 0;
		double base = 0;
		double height = 0;
		double perimeter = 0;
		double halfPerimeter = 0;

		// En switch-sats som beror p� g�llande vad anv�ndaren g�r f�r val tidigare
		// L�ter sedan anv�ndaren mata in de olika v�rden som beh�vs f�r att g�ra utr�kningarna
		switch(choice)
		{
			case 1:
				System.out.print("Ange triangels basl�ngd: ");
				base = input.nextDouble();
				System.out.print("Ange triangelns h�jd: ");
				height = input.nextDouble();
				System.out.println("Triangels area �r " + areaOfTriangle(base, height) + " i den enhet du angav v�rdena.");
				break;
			case 2:
				System.out.print("Ange l�ngden p� sida A: ");
				lengthA = input.nextDouble();
				System.out.print("Ange l�ngden p� sida B: ");
				lengthB = input.nextDouble();
				System.out.print("Ange l�ngden p� sida C: ");
				lengthC = input.nextDouble();
				System.out.print("Ange vinkeln mellan sidorna AB: ");
				angleAB = input.nextDouble();
				System.out.print("Ange vinkeln mellan sidorna AC: ");
				angleAC = input.nextDouble();
				System.out.print("Ange vinkeln mellan sidorna BC: ");
				angleBC = input.nextDouble();
				bisektrisLength(lengthA, lengthB, lengthC, angleAB, angleAC, angleBC);
				break;
			case 3:
				System.out.print("Ange l�ngden p� f�rsta sidan: ");
				lengthA = input.nextDouble();
				System.out.print("Ange l�ngden p� andra sidan: ");
				lengthB = input.nextDouble();
				System.out.print("Ange l�ngden p� tredje sidan: ");
				lengthC = input.nextDouble();

				System.out.println("Omskrivna cirkeln radie: " + omskrivnaRadien(lengthA, lengthB, lengthC));
				System.out.println("Inskrivna cirkeln radie: " + inskrivnaRadien(lengthA, lengthB, lengthC));
				System.out.println("I den enhet du angav v�rdena.");

				break;
			default:
				System.out.println("Du valde inget giltigt alternativ.");
				break;
		}
		System.out.println("");
	}

	// Ber�knar arean av triangeln
	public static double areaOfTriangle(double base, double height)
	{
		double area = (base * height) / 2;

		return area;
	}

	// Ber�knar triangelns omkrets
	public static double trianglePerimeter(double lengthA, double lengthB, double lengthC)
	{
		double perimeter = lengthA + lengthB + lengthC;

		return perimeter;
	}

	// Ber�knar triangelns halva omkrets
	public static double triangleHalfPerimeter(double perimeter)
	{
		double halfPerimeter = perimeter / 2;

		return halfPerimeter;
	}

	// Ber�knar triangelns alla bisktrisl�ngder samt skriver ut dem p� en g�ng f�r att det �r relativt sv�rt at returnera flera olika variablar
	public static void bisektrisLength(double lengthA, double lengthB, double lengthC, double angleAB, double angleAC, double angleBC)
	{
		double bisektrisAB = (2 * lengthA * lengthB * Math.cos(Math.toRadians(angleAB / 2))) / (lengthA + lengthB);
		double bisektrisAC = (2 * lengthA * lengthC * Math.cos(Math.toRadians(angleAC / 2))) / (lengthA + lengthC);
		double bisektrisBC = (2 * lengthB * lengthC * Math.cos(Math.toRadians(angleBC / 2))) / (lengthB + lengthC);

		System.out.println("Bisktrisl�ngd AB: " + bisektrisAB + " i den enhet du angav v�rdena.");
		System.out.println("Bisktrisl�ngd AC: " + bisektrisAC + " i den enhet du angav v�rdena.");
		System.out.println("Bisktrisl�ngd BC: " + bisektrisBC + " i den enhet du angav v�rdena.");
	}

	// Ber�knar triangelns h�jd
	public static double triangleHeight(double lengthA, double lengthB, double lengthC)
	{
		double halfPerimeter = triangleHalfPerimeter(trianglePerimeter(lengthA, lengthB, lengthC));
		double height = (2 / lengthA) * Math.sqrt(halfPerimeter * (halfPerimeter - lengthA) * (halfPerimeter - lengthB) * (halfPerimeter - lengthC));

		return height;
	}

	// Ber�knar triangelns omskrivna radie
	public static double omskrivnaRadien(double lengthA, double lengthB, double lengthC)
	{
		double height = triangleHeight(lengthA, lengthB, lengthC);
		double area = areaOfTriangle(lengthA, height);

		double radieOmskrivna = (lengthA * lengthB * lengthC) / (4 * area);

		return radieOmskrivna;
	}

	// Ber�knar triangels inskrivna radie
	public static double inskrivnaRadien(double lengthA, double lengthB, double lengthC)
	{
		double halfPerimeter = triangleHalfPerimeter(trianglePerimeter(lengthA, lengthB, lengthC));

		double radieInskrivna = Math.sqrt(((halfPerimeter - lengthA) * (halfPerimeter - lengthB) * (halfPerimeter - lengthC)) / halfPerimeter);

		return radieInskrivna;
	}
}