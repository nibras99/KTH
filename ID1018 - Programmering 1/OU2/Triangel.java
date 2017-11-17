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
		// Låter användaren göra ett val gällande vad han vill utföra för beräkningar
		System.out.println("TRIANGLAR");
		System.out.println("Vad vill du göra?");
		System.out.println("1. Beräkna arean av en triangel.");
		System.out.println("2. Beräkna en bisektris längd.");
		System.out.println("3. En triangel och dess cirklar.");
		System.out.print("Ditt val: ");

		int choice = input.nextInt();
		System.out.println("");

		// Skapar standardvariablar för allt som kan tänkas förekomma utanför metoderna
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

		// En switch-sats som beror på gällande vad användaren gör för val tidigare
		// Låter sedan användaren mata in de olika värden som behövs för att göra uträkningarna
		switch(choice)
		{
			case 1:
				System.out.print("Ange triangels baslängd: ");
				base = input.nextDouble();
				System.out.print("Ange triangelns höjd: ");
				height = input.nextDouble();
				System.out.println("Triangels area är " + areaOfTriangle(base, height) + " i den enhet du angav värdena.");
				break;
			case 2:
				System.out.print("Ange längden på sida A: ");
				lengthA = input.nextDouble();
				System.out.print("Ange längden på sida B: ");
				lengthB = input.nextDouble();
				System.out.print("Ange längden på sida C: ");
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
				System.out.print("Ange längden på första sidan: ");
				lengthA = input.nextDouble();
				System.out.print("Ange längden på andra sidan: ");
				lengthB = input.nextDouble();
				System.out.print("Ange längden på tredje sidan: ");
				lengthC = input.nextDouble();

				System.out.println("Omskrivna cirkeln radie: " + omskrivnaRadien(lengthA, lengthB, lengthC));
				System.out.println("Inskrivna cirkeln radie: " + inskrivnaRadien(lengthA, lengthB, lengthC));
				System.out.println("I den enhet du angav värdena.");

				break;
			default:
				System.out.println("Du valde inget giltigt alternativ.");
				break;
		}
		System.out.println("");
	}

	// Beräknar arean av triangeln
	public static double areaOfTriangle(double base, double height)
	{
		double area = (base * height) / 2;

		return area;
	}

	// Beräknar triangelns omkrets
	public static double trianglePerimeter(double lengthA, double lengthB, double lengthC)
	{
		double perimeter = lengthA + lengthB + lengthC;

		return perimeter;
	}

	// Beräknar triangelns halva omkrets
	public static double triangleHalfPerimeter(double perimeter)
	{
		double halfPerimeter = perimeter / 2;

		return halfPerimeter;
	}

	// Beräknar triangelns alla bisktrislängder samt skriver ut dem på en gång för att det är relativt svårt at returnera flera olika variablar
	public static void bisektrisLength(double lengthA, double lengthB, double lengthC, double angleAB, double angleAC, double angleBC)
	{
		double bisektrisAB = (2 * lengthA * lengthB * Math.cos(Math.toRadians(angleAB / 2))) / (lengthA + lengthB);
		double bisektrisAC = (2 * lengthA * lengthC * Math.cos(Math.toRadians(angleAC / 2))) / (lengthA + lengthC);
		double bisektrisBC = (2 * lengthB * lengthC * Math.cos(Math.toRadians(angleBC / 2))) / (lengthB + lengthC);

		System.out.println("Bisktrislängd AB: " + bisektrisAB + " i den enhet du angav värdena.");
		System.out.println("Bisktrislängd AC: " + bisektrisAC + " i den enhet du angav värdena.");
		System.out.println("Bisktrislängd BC: " + bisektrisBC + " i den enhet du angav värdena.");
	}

	// Beräknar triangelns höjd
	public static double triangleHeight(double lengthA, double lengthB, double lengthC)
	{
		double halfPerimeter = triangleHalfPerimeter(trianglePerimeter(lengthA, lengthB, lengthC));
		double height = (2 / lengthA) * Math.sqrt(halfPerimeter * (halfPerimeter - lengthA) * (halfPerimeter - lengthB) * (halfPerimeter - lengthC));

		return height;
	}

	// Beräknar triangelns omskrivna radie
	public static double omskrivnaRadien(double lengthA, double lengthB, double lengthC)
	{
		double height = triangleHeight(lengthA, lengthB, lengthC);
		double area = areaOfTriangle(lengthA, height);

		double radieOmskrivna = (lengthA * lengthB * lengthC) / (4 * area);

		return radieOmskrivna;
	}

	// Beräknar triangels inskrivna radie
	public static double inskrivnaRadien(double lengthA, double lengthB, double lengthC)
	{
		double halfPerimeter = triangleHalfPerimeter(trianglePerimeter(lengthA, lengthB, lengthC));

		double radieInskrivna = Math.sqrt(((halfPerimeter - lengthA) * (halfPerimeter - lengthB) * (halfPerimeter - lengthC)) / halfPerimeter);

		return radieInskrivna;
	}
}