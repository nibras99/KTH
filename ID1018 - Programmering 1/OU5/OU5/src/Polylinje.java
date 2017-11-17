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

	public String toString() // Returnerar polylinjen som en string i de format som efterfr�gades.
	{
		StringBuilder builder = new StringBuilder("{[");
		for (int i = 0; i < horn.length; i++)
		{
			builder.append(horn[i]);
		}
		builder.append("], " + farg + ", " + bredd + "}");

		return builder.toString();
	}

	public Punkt[] getHorn() // Returnerar polylinjens alla h�rn
	{
		Punkt[] h = new Punkt[horn.length];
		for (int i = 0; i < horn.length; i++)
		{
			h[i] = new Punkt(horn[i]);
		}
		return h;
	}

	public String getFarg() // Returnerar polylinjens f�rg
	{
		return farg;
	}

	public int getBredd() // Returnerar polylinjens bredd
	{
		return bredd;
	}

	public void setFarg(String farg) // S�tter polylinjens f�rg
	{
		this.farg = farg;
	}

	public void setBredd(int bredd) // S�tterpolylinjens bredd
	{	
		this.bredd = bredd;
	}

	public double langd() // Ber�knar polylinjens l�ngd med hj�lp av den tidigare avst�nd metoden i klassen Punkt
	{
		double langd = 0;
		for (int i = 0; i < horn.length - 1; i++)
		{
			langd = langd + horn[i].avstand(horn[i + 1]);
		}

		return langd;
	}

	public void laggTill(Punkt horn) // L�gger till en punkt i slutet av polylinjen
	{
		Punkt[] h = new Punkt[this.horn.length + 1];
		int i = 0;
		// Kopierar bara f�rst alla v�rden fr�n gamla punkten
		for (i = 0; i < this.horn.length; i++)
		{
			h[i] = this.horn[i];
		}

		// S�tter sist in det nya v�rdet sist.
		h[i] = new Punkt(horn);
		this.horn = h;
	}

	public void laggTillFramfor(Punkt horn, String hornNamn) // L�gger till en punkt p� platsen f�re den punkt med stringnamnet som matchar det namn man skickat med
	{
		Punkt[] h = new Punkt[this.horn.length + 1];
		int pos = 0;
		// Tar reda p� vilken position in vektorn punkten man vill stoppa in framf�r har
		for (int i = 0; i < this.horn.length; i++)
		{
			if (hornNamn == this.horn[i].getNamn()) 
			{
				pos = i;
				break;
			}
		}

		// Kopierar alla v�rden fram tills den positionen		
		for (int i = 0; i < pos; i++)
		{
			h[i] = this.horn[i];
		}

		// S�tter sedan in den nya punkten i den positionen
		h[pos] = new Punkt(horn);

		// Samt fyller p� med restrerande punkter i de platser som �r kvar
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
		// Tar reda p� den plats punken har
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

		// Samt alla punkter efter den platsen, vi skippar helt enkelt att kopiera �ver den punkten
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