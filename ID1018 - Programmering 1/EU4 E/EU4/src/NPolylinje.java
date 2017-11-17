import java.util.Iterator;

public class NPolylinje implements Polylinje
{
	private static class Nod
	{
		public Punkt horn;
		public Nod nastaNod;
		
		public Nod (Punkt horn)
		{
			this.horn = horn;
			nastaNod = null;
		}
	}
	
	private Nod horn;
	private String farg = "svart";
	private int bredd = 1;
	
	public NPolylinje()
	{
		this.horn = null;
	}
	
	public NPolylinje (Punkt[] horn)
	{
		if (horn.length > 0)
		{
			Nod nod = new Nod (new Punkt (horn[0]));
			this.horn = nod;
			int pos = 1;
			
			while (pos < horn.length)
			{
				nod.nastaNod = new Nod (new Punkt (horn[pos++]));
				nod = nod.nastaNod;
			}
		}
	}
	// ytterligare kod här
}



public class NPolylinje implements Polylinje
{
	private Punkt[] horn;
	private String farg = "svart";
	private int bredd = 3;

	public String toString() // Returnerar polylinjen som en string i de format som efterfrågades.
	{
		System.out.print("Poly1");
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
		return this.horn;
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
		h[i] = horn;
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
		h[pos] = horn;

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
