import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;
import se.kth.id1020.Edge;
import se.kth.id1020.Graph;
import se.kth.id1020.Vertex;
// e + v * log v
// e * log v

public class SPE
{
    // Den sista kanten mellan start och slut, det gör så att vi enkelt kan följa vägen tillbaka till starten
    public Edge[] edgeTo;
    // Den totala längden från start till slut för slutpunkten.
    public double[] distTo;
    // Index prioritets-kö
    public IndexMinPQ<Double> pq;
    public boolean edge;

    public SPE(Graph g, int s, boolean ed)
    {
        // Initierar lite nya variablar
        edge = ed;
        edgeTo = new Edge[g.numberOfVertices()];
        distTo = new double[g.numberOfVertices()];
        pq = new IndexMinPQ<Double>(g.numberOfVertices());

        // Sätter längden till infinity.
        for (int v = 0; v < g.numberOfVertices(); v++)
        {
            distTo[v] = Double.POSITIVE_INFINITY;
        }

        // Men startpunkten är ju sig själv, så dess längd är 0.
        distTo[s] = 0.0;

        // Lägger in startpunkten i kön
        pq.insert(s, 0.0);

        // Så länge kön inte är tom så har vi mer att kolla!
        while (!pq.isEmpty())
        {
            // Tar bort den kant som är närmast källan
            // Och relaxar alla som är kopplade till den
            int v = pq.delMin();
            for (Edge e : g.adj(v))
                relax(e);
        }
    }

    public void relax(Edge e)
    {
        // v == vart kanten kom ifrån, w == vart den pekar
        int v = e.from;
        int w = e.to;

        // En boolean som bestämmer om vi har med edge weight eller inte att göra.
        if (this.edge)
        {
            // Om vi hittar en "kortare" väg så vill vi ta bort den gamla, och ersätta den med den nya
            if (distTo[w] > distTo[v] + e.weight)
            {
                // Så ersätter vi den gamla vikten med den nya
                distTo[w] = distTo[v] + e.weight;
                // Samt ersätter pekaren till den sista noden.
                edgeTo[w] = e;

                // Uppdaterar priotitetskön
                // Om w nu finns sedan innan så har vi ju hittat en kortare väg
                // Så vi minskar "nyckeln"
                // Om den inte finns sedan innan så sätter vi bara in den.
                if (pq.contains(w))
                    pq.decreaseKey(w, distTo[w]);
                else
                    pq.insert(w, distTo[w]);
            }
        }
        else
        {
            // Samma som ovan fast med +1 istället för weight.
            // Bredth first vore MYCKET bättre / effektivare. Men som sagt, står "EN ALGORITM"
            if (distTo[w] > distTo[v] + 1)
            {
                distTo[w] = distTo[v] + 1;
                edgeTo[w] = e;
                if (pq.contains(w))
                    pq.decreaseKey(w, distTo[w]);
                else
                    pq.insert(w, distTo[w]);
            }
        }
    }

    // Istället för att printar alla ID's, så printar den alla VERTEX i den ordning den går.
    // Enklare att följa enligt mig.
    public Iterable<Vertex> pathTo(int v, Graph g)
    {
        Stack<Vertex> path = new Stack<Vertex>();
        path.push(g.vertex(v));
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.from])
        {
            Vertex vex;
            vex = g.vertex(e.from);
            path.push(vex);
        }
        //
        System.out.println("Storleken (antal vertexes) är: " + path.size());
        return path;
    }
}
