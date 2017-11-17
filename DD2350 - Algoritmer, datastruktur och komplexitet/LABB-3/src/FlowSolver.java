import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FlowSolver
{
    Kattio io;
    int v, s, t, e;
    int[] path;
    boolean[] visited;
    int totalFlow = 0;

    void readData()
    {
        v = io.getInt();
        s = io.getInt();
        t = io.getInt();
        e = io.getInt();

        path = new int[v + 1];
        @SuppressWarnings("unchecked")
        HashMap<Integer, FE>[] map = new HashMap[v + 1];

        FE content;
        FE contentReversed;

        for (int i = 0; i < e; i++)
        {
            int from = io.getInt();
            int to = io.getInt();
            int cap = io.getInt();

            content = new FE(cap, 0);
            contentReversed = new FE(0, 0);

            //System.err.println("from " + from + " to " + to);

            if (map[from] != null && map[from].containsKey(to))
            {
                map[from].get(to).capacity = cap;
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
        io.println(v);

        int posEdges = 0;

        for (int i = 0; i <= v; i++)
        {
            if (map[i] != null)
            {
                for (Integer key : map[i].keySet())
                {
                    if (map[i].get(key).flow > 0)
                    {
                        posEdges++;
                    }
                    if (i == s)
                    {
                        totalFlow += map[i].get(key).flow;
                    }
                }
            }
        }

        io.println(s + " " + t + " " + totalFlow);

        io.println(posEdges);

        for (int i = 0; i <= v; i++)
        {
            if (map[i] != null)
            {
                for (Integer key : map[i].keySet())
                {
                    if (map[i].get(key).flow > 0)
                    {
                        io.println(i + " " + key + " " + map[i].get(key).flow);
                    }
                }
            }
        }

        io.flush();
    }

    FlowSolver()
    {
        io = new Kattio(System.in, System.out);
        /*try
        {
            io = new Kattio(new FileInputStream(new File("./LABB-3/src/testfall/test")), System.out);
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
        }*/
        readData();

        io.close();
    }

    public static void main(String args[])
    {
        new FlowSolver();
    }
}
