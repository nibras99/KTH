public class PolyTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//VPolylinje linje1 = new VPolylinje();
		//System.out.println(linje1.getFarg());
		
		//NPolylinje linje2 = new NPolylinje();
		//System.out.println(linje2.getFarg());
		
		/*Polylinje ivpoly1 = new VPolylinje();
		System.out.println(ivpoly1.getBredd());
		
		Polylinje inpoly1 = new NPolylinje();
		System.out.println(inpoly1.getFarg());
		
		Punkt[] p1 = new Punkt[4];
		p1[0] = new Punkt("A", 1, 2);
		p1[1] = new Punkt("B", 3, 4);
		p1[2] = new Punkt("C", 5, 6);
		p1[3] = new Punkt("D", 7, 8);
		
		String n = p1[0].getNamn();
		int x = p1[0].getX();
		int y = p1[0].getY();
		
		System.out.println(n + " " + x + " " + y);
		
		System.out.println("------ Skapat en massa punkter i en array, skrev ut punkten p1[0] manuellt ------");
		
		Polylinje vpoly1 = ivpoly1;
		System.out.println("AA");
		System.out.println(vpoly1.toString());
		System.out.println("BB");
		
		// ------------------------------------
		*/
		// Skapar en polylinje/punkt med 4 platser samt anger dess värden
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
				
				VPolylinje polylinje = new VPolylinje(p1);
				System.out.println(polylinje);
				
				System.out.println("------ Skapat polylinje av punkterna ------");

				System.out.println(polylinje.getFarg());
				System.out.println(polylinje.getBredd());
				
				System.out.println("------ Testar skriva ut polylinjens färg och bredd ------");
				
				polylinje.setFarg("red");
				polylinje.setBredd(3);
				
				System.out.println(polylinje.getFarg());
				System.out.println(polylinje.getBredd());
				
				System.out.println("------ Ändrade färgerna, skriver ut nya färgerna ------");
				
				Punkt p2 = new Punkt("E", 9, 10);
				System.out.println(polylinje);
				polylinje.laggTill(p2);
				System.out.println(polylinje);
				
				System.out.println("------ Skapat en ny punkt, E, lägger till den SIST i polylinjen, jämförs enkelt ------");
				
				Punkt p3 = new Punkt("F", 11, 12);
				Punkt p4 = new Punkt("G", 13, 14);
				System.out.println(polylinje);
				polylinje.laggTillFramfor(p3, "C");
				System.out.println(polylinje);
				polylinje.laggTillFramfor(p4, "F");
				System.out.println(polylinje);
				
				System.out.println("------ Skapat en ny punkt, lägger till F FRAMFÖR punkten C i polylinjen, sedan läggs G till framför F, jämförs enkelt ------");
				
				System.out.println(polylinje);
				polylinje.taBort("B");
				System.out.println(polylinje);
				
				System.out.println("------ Tar bort punkten B i polylinjen, jämförs enkelt ------");
				
				System.out.println(polylinje.langd());
				
				System.out.println("------ Beräknade polylinjens längd ------");
		
	}
}
