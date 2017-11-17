/*
En j�kla massa testa f�r Polylinje, ValjPolylinje samt PolylinjeIterator
 */
class PolylinjeTest
{
	public static void main(String[] args)
	{
		// Skapar en polylinje/punkt med 4 platser samt anger dess v�rden
		// Konstruktor + Transformator
		Punkt[] p1 = new Punkt[4]; // #1
		p1[0] = new Punkt("A", 1, 2); // #2
		p1[1] = new Punkt("B", 3, 4);
		p1[2] = new Punkt("C", 5, 6);
		p1[3] = new Punkt("D", 7, 8);
		
		String n = p1[0].getNamn(); // #4
		int x = p1[0].getX(); // #5
		int y = p1[0].getY(); // #6
		System.out.println(n + " " + x + " " + y);
		
		System.out.println("------ Skapat en massa punkter i en array, skrev ut punkten p1[0] manuellt ------");
		
		Polylinje polylinje = new Polylinje(p1);
		System.out.println(polylinje);
		
		System.out.println("------ Skapat polylinje av punkterna ------");

		System.out.println(polylinje.getFarg());
		System.out.println(polylinje.getBredd());
		
		System.out.println("------ Testar skriva ut polylinjens f�rg och bredd ------");
		
		polylinje.setFarg("red");
		polylinje.setBredd(3);
		
		System.out.println(polylinje.getFarg());
		System.out.println(polylinje.getBredd());
		
		System.out.println("------ �ndrade f�rgerna, skriver ut nya f�rgerna ------");
		
		Punkt p2 = new Punkt("E", 9, 10);
		System.out.println(polylinje);
		polylinje.laggTill(p2);
		System.out.println(polylinje);
		
		System.out.println("------ Skapat en ny punkt, E, l�gger till den SIST i polylinjen, j�mf�rs enkelt ------");
		
		Punkt p3 = new Punkt("F", 11, 12);
		Punkt p4 = new Punkt("G", 13, 14);
		System.out.println(polylinje);
		polylinje.laggTillFramfor(p3, "C");
		System.out.println(polylinje);
		polylinje.laggTillFramfor(p4, "F");
		System.out.println(polylinje);
		
		System.out.println("------ Skapat en ny punkt, l�gger till F FRAMF�R punkten C i polylinjen, sedan l�ggs G till framf�r F, j�mf�rs enkelt ------");
		
		System.out.println(polylinje);
		polylinje.taBort("B");
		System.out.println(polylinje);
		
		System.out.println("------ Tar bort punkten B i polylinjen, j�mf�rs enkelt ------");
		
		System.out.println(polylinje.langd());
		
		System.out.println("------ Ber�knade polylinjens l�ngd ------");
		
		Polylinje.PolylinjeIterator iterator = polylinje.new PolylinjeIterator();
		while(iterator.finnsHorn())
		{
			Punkt aktuellHorn = iterator.horn();
			System.out.println(aktuellHorn);
			iterator.gaFram();
		}
		
		System.out.println("------ Itererade igenom hela polylinjen ------");
	}
}