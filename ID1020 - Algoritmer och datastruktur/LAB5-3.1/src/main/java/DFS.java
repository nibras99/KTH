import se.kth.id1020.Edge;
import se.kth.id1020.Graph;
// e + v
public class DFS
{
    // Variablar som kommer behövas
    public int count;
    public boolean[] marked;
    public int[] id;

    public DFS(Graph g)
    {
        // Initierar
        marked = new boolean[g.numberOfVertices()];
        id = new int[g.numberOfVertices()];
        // För varje vertex
        for (int v = 0; v < g.numberOfVertices(); v++)
        {
            // Om den inte är kollad
            if (!marked[v])
            {
                // Så kollar vi den, samt ökar count
                dfs(g, v);
                count++;
            }
        }
    }

    public void dfs(Graph g, int v)
    {
        // I "kollen" sätter vi count som true
        // Samt dess ID till motsvarande count
        marked[v] = true;
        id[v] = count;

        // Vi loopar såklart även dennas edge's
        // Och om det den pekar på, inte är markerad så ropar vi på oss själva.
        // Det gör så att alla kommer få samma "count" / Subgrupp
        // För vi ökar inte count förens vi kommer tillbaka til huvudfunktionen
        for (Edge w : g.adj(v))
        {
            if (!marked[w.to])
                dfs(g, w.to);
        }
    }
}
