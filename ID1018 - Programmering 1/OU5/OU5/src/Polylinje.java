public class Polylinje
{
	private Punkt[] horn;
	private String farg = "svart";
	private int bredd = 1;

	public Polylinje() // Standard constructor
	{
		this.horn = new Punkt[0];
	}

	public Polylinje(Punkt[] horn) // Constructor
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

	public Punkt[] getHorn() // Returnerar polylinjens alla hörn
	{
		Punkt[] h = new Punkt[horn.length];
		for (int i = 0; i < horn.length; i++)
		{
			h[i] = new Punkt(horn[i]);
		}
		return h;
	}

	public String getFarg() // Returnerar polylinjens färg
	{
		return farg;
	}

	public int getBredd() // Returnerar polylinjens bredd
	{
		return bredd;
	}

	public void setFarg(String farg) // Sätter polylinjens färg
	{
		this.farg = farg;
	}

	public void setBredd(int bredd) // Sätterpolylinjens bredd
	{	
		this.bredd = bredd;
	}

	public double langd() // Beräknar polylinjens längd med hjälp av den tidigare avstånd metoden i klassen Punkt
	{
		double langd = 0;
		for (int i = 0; i < horn.length - 1; i++)
		{
			langd = langd + horn[i].avstand(horn[i + 1]);
		}

		return langd;
	}

	public void laggTill(Punkt horn) // Lägger till en punkt i slutet av polylinjen
	{
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

	public void laggTillFramfor(Punkt horn, String hornNamn) // Lägger till en punkt på platsen före den punkt med stringnamnet som matchar det namn man skickat med
	{
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

	public void taBort(String hornNamn) // Tar bort den punkt med stringnamnet som matchar det man skickat med.
	{
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

	public class PolylinjeIterator
	{
		private int aktuell = -1;
		public PolylinjeIterator()
		{
			if (Polylinje.this.horn.length > 0)
				aktuell = 0;
		}

		public boolean finnsHorn()
		{
			return aktuell != -1;
		}

		public Punkt horn() throws java.util.NoSuchElementException
		{
			if (!this.finnsHorn())
				throw new java.util.NoSuchElementException("slut av iterationen");
			Punkt horn = Polylinje.this.horn[aktuell];
			
			return horn;
		}

		public void gaFram()
		{
			if (aktuell >= 0 && aktuell < Polylinje.this.horn.length - 1)
			{
				aktuell++;
			}
			else
			{
				aktuell = -1;
			}
		}
	}
}