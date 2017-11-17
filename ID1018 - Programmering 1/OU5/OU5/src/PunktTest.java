class PunktTest
{
	public static void main (String[] args)
	{
		// testa en konstruktor och en transformator
		Punkt p1 = new Punkt("A", 3, 4); // #2
		Punkt p2 = new Punkt("B", 5, 6); // #2
		System.out.println(p1 + " <--> " + p2);  // #3

		// testa inspektorer
		String n = p1.getNamn(); // #4
		int x = p1.getX(); // #5
		int y = p1.getY(); // #6
		System.out.println(n + " " + x + " " + y);

		// testa en kombinator och en komparator
		double d = p1.avstand(p2);  // #7
		System.out.println(d);
		boolean b = p1.equals(p2);  // #8
		System.out.println(b);

		// testa mutatorer
		p2.setX(1); // #9
		p2.setY(2); // #10
		System.out.println(p2);

		// testa en konstruktor till
		Punkt p = new Punkt(p1); // #11
		System.out.println("---");
		System.out.println(p);
		System.out.println(p1);
		System.out.println(p2);
	}
}
