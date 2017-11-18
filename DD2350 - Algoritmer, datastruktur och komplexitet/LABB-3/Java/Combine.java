import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Combine
{
    Kattio io;
    int v, s, t, e, x, y;
    int[] path;
    boolean[] visited;

    void readGraph() {
        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();
        e = io.getInt();

        s = x + y + 1;
        t = x + y + 2;
        v = x + y + 2;

        path = new int[v + 1];
        @SuppressWarnings("unchecked")
        HashMap<Integer, FE>[] map = new HashMap[v + 1];

        FE content;
        FE contentReversed;

        for (int i = 0; i < e; i++)
        {
            int from = io.getInt();
            int to = io.getInt();

            content = new FE(1, 0);
            contentReversed = new FE(0, 0);

            //System.err.println("from " + from + " to " + to);

            if (map[from] != null && map[from].containsKey(to))
            {
                map[from].get(to).capacity = 1;
            } else
            {

                if (map[from] == null)
                {
                    HashMap<Integer, FE> temp = new HashMap<>();
                    temp.put(to, content);
                    map[from] = temp;
                } else
                {
                    map[from].put(to, content);
                }

                if (map[to] == null)
                {
                    HashMap<Integer, FE> temp = new HashMap<>();
                    temp.put(from, contentReversed);
                    map[to] = temp;
                } else
                {
                    map[to].put(from, contentReversed);
                }
            }

        }

        // S till alla X
        for (int i = 1; i <= x; i++) {
            content = new FE(1, 0);
            contentReversed = new FE(0, 0);

            if (map[s] == null)
            {
                HashMap<Integer, FE> temp = new HashMap<>();
                temp.put(i, content);
                map[s] = temp;
            } else
            {
                map[s].put(i, content);
            }

            if (map[i] == null)
            {
                HashMap<Integer, FE> temp = new HashMap<>();
                temp.put(s, contentReversed);
                map[i] = temp;
            } else
            {
                map[i].put(s, contentReversed);
            }
        }

        // alla Y till T
        for (int i = x + 1; i <= x + y; i++) {
            content = new FE(1, 0);
            contentReversed = new FE(0, 0);

            if (map[i] == null)
            {
                HashMap<Integer, FE> temp = new HashMap<>();
                temp.put(t, content);
                map[i] = temp;
            } else
            {
                map[i].put(t, content);
            }

            if (map[t] == null)
            {
                HashMap<Integer, FE> temp = new HashMap<>();
                temp.put(i, contentReversed);
                map[t] = temp;
            } else
            {
                map[t].put(i, contentReversed);
            }
        }

        io.flush();
        //System.err.println(Arrays.toString(map));
        solveFlow(map);
    }
    public void solveFlow(HashMap<Integer, FE>[] map)
    {
        while (true)
        {
            visited = new boolean[v + 1];
            visited[s] = true;

            Queue<Integer> q = new LinkedList<Integer>();
            q.add(s);

            boolean check = false;
            int current;

            // BFS
            while (!q.isEmpty())
            {
                current = q.peek();

                // Vi har nått fram till målet, ännu en gång!
                if (current == t)
                {
                    check = true;
                    break;
                }
                q.remove();

                if (map[current] != null)
                {
                    for (Integer key : map[current].keySet())
                    {
                        if (!visited[key] && map[current].get(key).capacity > map[current].get(key).flow)
                        {
                            visited[key] = true;
                            q.add(key);
                            path[key] = current;
                        }
                    }
                }
            }

            // Om vi inte lyckades nå fram till t, men vi kom ur loopen, så är vi "klara"
            if (check == false)
                break;

            // Om vi lyckade, så uppdaterar vi flödet.
            int temp = map[path[t]].get(t).capacity - map[path[t]].get(t).flow;
            for (int i = t; i != s; i = path[i])
            {
                temp = Math.min(temp, (map[path[i]].get(i).capacity - map[path[i]].get(i).flow));
            }

            for (int i = t; i != s; i = path[i])
            {
                map[path[i]].get(i).flow += temp;
                map[i].get(path[i]).flow -= temp;
            }
        }

        //System.err.println(Arrays.toString(map));

        printFlow(map);
    }

    void printFlow(HashMap<Integer, FE>[] map)
    {
        io.println(x + " " + y);

        int posEdges = 0;

        for (int i = 0; i <= v; i++)
        {
            if (i != s && i != t && map[i] != null)
            {
                for (Integer key : map[i].keySet())
                {
                    if (key != s && key != t && map[i].get(key).flow > 0)
                    {
                        posEdges++;
                    }
                }
            }
        }

        io.println(posEdges);

        for (int i = 0; i <= v; i++)
        {
            if (i != s && i != t && map[i] != null)
            {
                for (Integer key : map[i].keySet())
                {
                    if (key != s && key != t && map[i].get(key).flow > 0)
                    {
                        io.println(i + " " + key);
                    }
                }
            }
        }

        io.flush();
    }

    Combine()
    {
        io = new Kattio(System.in, System.out);
/*        try
        {
            io = new Kattio(new FileInputStream(new File("./LABB-3/src/testfall/maffigttest.indata")), System.out);
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
        }*/

        readGraph();

        io.close();
    }

    public static void main(String args[])
    {
        new Combine();
    }
}