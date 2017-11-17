public class Punkt
{
	private String namn = "";
	private int x = 0;
	private int y = 0;

	public Punkt() // #1 Standard värden, constructor
	{
		this.namn = "empty/null";
		this.x = 0;
		this.y = 0;
	}

	public Punkt(String namn, int x, int y) // #2 Antar de värden man skickat med i metoden
	{
		this.namn = namn;
		this.x = x;
		this.y = y;
	}

	public Punkt(Punkt p) // #11 Skapar en ny punkt med samma värden som punkten p man angett
	{
		this.namn = p.namn;
		this.x = p.x;
		this.y = p.y;
	}
	
	public String toString() // #3 Returnar en string av de värden den skall skriva ut
	{
		return("(" + namn + ", " + x + ", " + y + ")");
	}

	public String getNamn() // #4 Returnar namnet på punkten
	{
		return namn;
	}

	public int getX() // #5 Returnerar punktens X-värde
	{
		return this.x;
	}

	public int getY() // #6 Returnerar punktens Y-värde
	{
		return this.y;
	}

	public double avstand(Punkt p) // #7 Beräknar avståndet mellan två punkter, simpel pythagoras sats
	{
		return Math.sqrt(Math.pow((p.x - this.x), 2) + Math.pow((p.y - this.y), 2));
	}

	public boolean equals(Punkt p) // #8 Kolla om de två punkterna är identiska
	{
		// I standardfallet av PunkTest så är p1 = this.... och p = p2's värden
		return ((p.namn == this.namn) && (p.x == this.x)  &&  (p.y == this.y));
	}

	public void setX(int x) // #9 Anger en punkts X-värde
	{
		this.x = x;
	}

	public void setY(int y) // #10 Anger en punkts Y-värde
	{
		this.y = y;
	}
}