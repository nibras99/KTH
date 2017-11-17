import java.util.Iterator;

public class VPolylinje implements Polylinje
{
	private Punkt[] horn;
	private String farg = "röd";
	private int bredd = 5;
	
	
	// Är de två nedan metoder tillåtna, hur annars ?
	public VPolylinje() // Standard constructor
	{
		this.horn = new Punkt[0];
	}
	
	public VPolylinje(Punkt[] horn) // Constructor
	{
		this.horn = new Punkt[horn.length];
		for (int i = 0; i < horn.length; i++)
		{
			this.horn[i] = new Punkt(horn[i]);
		}
	}
	
	public String toString() // Returnerar polylinjen som en string i de format som efterfrågades.
	{
		StringBuilder builder = new StringBuilder("{[");
		for (int i = 0; i < horn.length; i++)
		{
			builder.append(horn[i]);
		}
		builder.append("], " + farg + ", " + bredd + "}");
		
		return builder.toString();
	}
	
	@Override
	public Punkt[] getHorn() {
		Punkt[] h = new Punkt[horn.length];
		for (int i = 0; i < horn.length; i++)
		{
			h[i] = new Punkt(horn[i]);
		}
		return h;
	}

	@Override
	public String getFarg() {
		return farg;
	}

	@Override
	public int getBredd() {
		return bredd;
	}

	@Override
	public double langd() {
		double langd = 0;
		for (int i = 0; i < horn.length - 1; i++)
		{
			langd = langd + horn[i].avstand(horn[i + 1]);
		}
		
		return langd;
	}

	@Override
	public void setFarg(String farg) {
		this.farg = farg;
	}

	@Override
	public void setBredd(int bredd) {
		this.bredd = bredd;
	}

	@Override
	public void laggTill(Punkt horn) {
		Punkt[] h = new Punkt[this.horn.length + 1];
		int i = 0;
		// Kopierar bara först alla värden från gamla punkten
		for (i = 0; i < this.horn.length; i++)
		{
			h[i] = this.horn[i];
		}
		
		// Sätter sist in det nya värdet sist.
		h[i] = new Punkt(horn);
		this.horn = h;
	}

	@Override
	public void laggTillFramfor(Punkt horn, String hornNamn) {
		Punkt[] h = new Punkt[this.horn.length + 1];
		int pos = 0;
		// Tar reda på vilken position in vektorn punkten man vill stoppa in framför har
		for (int i = 0; i < this.horn.length; i++)
		{
			if (hornNamn == this.horn[i].getNamn()) 
			{
				pos = i;
				break;
			}
		}
		
		// Kopierar alla värden fram tills den positionen		
		for (int i = 0; i < pos; i++)
		{
			h[i] = this.horn[i];
		}
		
		// Sätter sedan in den nya punkten i den positionen
		h[pos] = new Punkt(horn);
		
		// Samt fyller på med restrerande punkter i de platser som är kvar
		for (int i = pos + 1; i < this.horn.length + 1; i++)
		{
			h[i] = this.horn[i - 1];
			
		}
		this.horn = h;
	}

	@Override
	public void taBort(String hornNamn) {
		Punkt[] h = new Punkt[this.horn.length - 1];
		int pos = 0;
		// Tar reda på den plats punken har
		for (int i = 0; i < this.horn.length; i++)
		{
			if (hornNamn == this.horn[i].getNamn()) 
			{
				pos = i;
				break;
			}
		}
		
		// Kopierar alla punkter fram till den platsen
		for (int i = 0; i < pos; i++)
		{
			h[i] = this.horn[i];
		}
		
		// Samt alla punkter efter den platsen, vi skippar helt enkelt att kopiera över den punkten
		for (int i = pos + 1; i < this.horn.length; i++)
		{
			h[i - 1] = this.horn[i];
		}
		this.horn = h;
	}

	@Override
	public Iterator<Punkt> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
