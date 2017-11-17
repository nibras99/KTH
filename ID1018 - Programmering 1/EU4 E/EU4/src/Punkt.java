public class Punkt
{
	private String namn = "";
	private int x = 0;
	private int y = 0;

	public Punkt() // #1 Standard v�rden, constructor
	{
		this.namn = "empty/null";
		this.x = 0;
		this.y = 0;
	}

	public Punkt(String namn, int x, int y) // #2 Antar de v�rden man skickat med i metoden
	{
		this.namn = namn;
		this.x = x;
		this.y = y;
	}

	public Punkt(Punkt p) // #11 Skapar en ny punkt med samma v�rden som punkten p man angett
	{
		this.namn = p.namn;
		this.x = p.x;
		this.y = p.y;
	}
	
	public String toString() // #3 Returnar en string av de v�rden den skall skriva ut
	{
		return("(" + namn + ", " + x + ", " + y + ")");
	}

	public String getNamn() // #4 Returnar namnet p� punkten
	{
		return namn;
	}

	public int getX() // #5 Returnerar punktens X-v�rde
	{
		return this.x;
	}

	public int getY() // #6 Returnerar punktens Y-v�rde
	{
		return this.y;
	}

	public double avstand(Punkt p) // #7 Ber�knar avst�ndet mellan tv� punkter, simpel pythagoras sats
	{
		return Math.sqrt(Math.pow((p.x - this.x), 2) + Math.pow((p.y - this.y), 2));
	}

	public boolean equals(Punkt p) // #8 Kolla om de tv� punkterna �r identiska
	{
		// I standardfallet av PunkTest s� �r p1 = this.... och p = p2's v�rden
		return ((p.namn == this.namn) && (p.x == this.x)  &&  (p.y == this.y));
	}

	public void setX(int x) // #9 Anger en punkts X-v�rde
	{
		this.x = x;
	}

	public void setY(int y) // #10 Anger en punkts Y-v�rde
	{
		this.y = y;
	}
}