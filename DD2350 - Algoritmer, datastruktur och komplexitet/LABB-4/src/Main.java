import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main
{
    Kattio io;

    Main()
    {
        io = new Kattio(System.in, System.out);
        try
        {
            io = new Kattio(new FileInputStream(new File("C:/Users/Casper/Documents/GitHub/ADK17/LABB-4/filezilla/test.in")), System.out);
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
        }

        /*
        Vertices = roller
        Edges = kopplade scener
        Colors = skådisar
        */

        int vertices = io.getInt();
        int edges = io.getInt();
        int colors = io.getInt();
        int roles = vertices + 3;
        int scenes = edges + 2;
        int actors = colors + 3;
        io.println(roles);      // Roller + 3 så vi kan försäkra oss om divorna
        io.println(scenes);     // Scener + 2 så att divorna får varsin scen, minst
        io.println(actors);     // Skådisar + 3 då vi måste säkra oss om divorna + den oskyldiga skådisen som måste spela med dem

        StringBuilder builder = new StringBuilder();
        builder.append(vertices);

        /*
        Roller
        Scener
        Skådisar
        *Roller st rader med skådisar som kan spela dem
        *Scener st rader med de roller som krävs
         */

        // Divorna + den stackare som måste spela med dem
        io.println(1 + " " + 1);
        io.println(1 + " " + 2);
        io.println(1 + " " + 3);

        // Resterande skådisar
        for (int i = 0; i <= actors - 3; i++)
        {
            builder.append(" ");
            builder.append(i + 3);
        }
        for (int i = 0; i < roles - 3; i++)
        {
            io.println(builder.toString());
        }

        // Divorna får varsin garanterad scen tillsammans med stackaren
        io.println(2 + " " + 1 + " " + 3);
        io.println(2 + " " + 2 + " " + 3);

        for (int i = 0; i < scenes - 2; i++)
        {
            io.println(2 + " " + (io.getInt() + 3) + " " + (io.getInt() + 3));
        }
        io.close();
    }

    public static void main(String[] args)
    {
        new Main();
    }
}
