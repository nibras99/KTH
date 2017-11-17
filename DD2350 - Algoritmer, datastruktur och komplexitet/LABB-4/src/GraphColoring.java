import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GraphColoring
{
    /*
    Tänker att:
    Vertices = roller
    Edges = kopplade scener
    Colors = skådisar
    */

    Kattio io = new Kattio(System.in, System.out);
    int vertices;
    int edges;
    int colors;

    int scenes;
    int actors;
    int roles;

    ArrayList<ColorEdge> edgeList;
    int refactor[];

    GraphColoring()
    {
        /*try
        {
            io = new Kattio(new FileInputStream(new File("C:/Users/Casper/Documents/GitHub/ADK17/LABB-4/filezilla/test.in")), System.out);
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
        }*/

        readGraph();

        printGraph();

        io.flush();
    }

    private void readGraph()
    {
        vertices = io.getInt();
        edges = io.getInt();
        colors = io.getInt();
        edgeList = new ArrayList<>(edges);

        if (colors > vertices)
            colors = vertices;

        // Basfallen garanteras scen för roll 1 3 och 2 3
        // Skådisar 1, 2, 3
        scenes = edges + 2;
        actors = colors + 3;

        refactor = new int[vertices + 1];
        int from;
        int to;

        // Ignorera basfallen vi redan har
        roles = 3;

        for (int i = 0; i < edges; i++)
        {
            from = io.getInt();
            to = io.getInt();

            // Beräknar totala roller + döper om variablar så att vi inte har massa onödig skit
            if (refactor[from] == 0)
            {
                roles++;
                refactor[from] = roles;
            }
            if (refactor[to] == 0)
            {
                roles++;
                refactor[to] = roles;
            }
            edgeList.add(new ColorEdge(refactor[from], refactor[to]));
        }
    }

    private void printGraph()
    {
        // Skriver ut allt enligt input datan
        io.println(roles);
        io.println(scenes);
        io.println(actors);

        // Basfallen
        io.println("1 1");
        io.println("1 2");
        io.println("1 3");

        StringBuilder sb = new StringBuilder();
        // Alla andra roller kan antas av alla skådisar
        sb.append(actors - 3);

        // Lägg till skådisarna
        for (int i = 4; i <= actors; i++)
            sb.append(" ").append(i);

        // Och printa dem för alla roller
        for (int i = 4; i <= roles; i++)
            io.println(sb);

        // Basfallen för scenerna
        io.println("2 1 3");
        io.println("2 2 3");

        // Printa resterande scener
        for (int i = 0; i < edges; i++)
        {
            io.println(2 + " " + edgeList.get(i).from + " " + edgeList.get(i).to);
        }
    }

    public static void main(String args[])
    {
        new GraphColoring();
    }
}
