import se.kth.id1020.Graph;
import se.kth.id1020.DataSource;

public class Paths
{
    public static void main(String[] args)
    {
        Graph g = DataSource.load();
        // Depth first search
        DFS dfs = new DFS(g);

        System.out.println("Antal sub-kategorier: " + dfs.count + "\n");

        // Anger lite startvariablar
        // Ändra bara namnen så kommer den hitta dess ID om det finns
        String from = "Renyn"; // 1006
        String to = "Parses"; // 918
        boolean countEdgeWeight = false;
        int start = -1;
        int end = -1;

        // Hämtar namnens ID's
        for (int i = 0; i < g.numberOfVertices(); i++)
        {
            if (g.vertex(i).label.equals(from))
                start = i;

            if (g.vertex(i).label.equals(to))
                end = i;
        }

        // Om någon av dem inte fanns så avbryter vi
        // Vi kan ju inte söka på punkter som inte finns...
        if (end == -1 || start == -1)
        {
            System.err.println("En av vertexerna hittades inte");
            System.exit(-1);
        }

        // System.out.println(g.vertex(start).distance(g.vertex(end)));


        // Använder Djikstras algoritm till båda sakerna.
        // En Bredth first sökning om man bara hittar kortaste vägen utan att bry sig om vikten hade varit "bättre".
        // Men det stod ju implementera EN algoritm.
        SPE spe = new SPE(g, start, countEdgeWeight);

        // Printar ut ALLA vertex som den går igenom, inklusive start och slut
        // Därefter printar den även "längden".
        // Om med edge weight så är det den totala vikten
        // Om utan edge weight så är det antalet hopp.
        System.out.println(spe.pathTo(end, g));
        System.out.println("Längden: " + spe.distTo[end]);

        // Itererar och printar alla sträckor från start till allt den når.
        /*double totalWeight;
        for (int v = 0; v < g.numberOfVertices(); v++)
        {
            System.out.println(start + " till " + v + " == " + spe.pathTo(v));
            totalWeight = 0;
            for (Edge e : spe.pathTo(v))
            {
                totalWeight = totalWeight + e.weight;
            }
            System.out.println("Totalt: " + totalWeight);
            System.out.println();
        }*/
    }
}