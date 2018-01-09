import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class RoleSettingProblem
{
    int roles;
    int scenes;
    int actors;
    Kattio io;

    // Arrayer med hashsets.

    // Varje hashSetArray -> Första ordet är arrayen, andra ordet är hashsettet.
    // T.ex. scenesRoles => scenesRoles[scene].contains(role)
    // Kan då kolla på instant tid.
    HashSet<Integer>[] scenesRoles;
    HashSet<Integer>[] actorsRoles;
    HashSet<Integer>[] actorsActiveRoles;
    HashSet<Integer>[] rolesActiveActors;

    // Simpelt HashSet, som lagrar roller som inte ännu fått en skådis.
    HashSet<Integer> unusedRoles;

    int totalActors = 0;
    int superActors = 0;
    int currentSuperActor;

    RoleSettingProblem()
    {
        io = new Kattio(System.in, System.out);

        try
        {
            io = new Kattio(new FileInputStream(new File("C:\\Users\\Casper\\Documents\\GitHub\\ADK17\\LABB-5\\testfall\\sample31.in")), System.out);
            /*
            sample01
            sample14
            sample25
            sample26
            sample31
            sample33
            sample36
             */
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
        }

        readData();

        createFirstSolution();

        printOutData();

        io.flush();
        io.close();
    }

    public void readData()
    {
        roles = io.getInt();
        scenes = io.getInt();
        actors = io.getInt();

        // Håller reda på vilken superactor vi ligger på.
        currentSuperActor = actors + 1;

        // initierar lite storlekar på dem olika sakerna, så att vi slipper det senare.
        scenesRoles = new HashSet[scenes + 1];
        actorsRoles = new HashSet[actors + roles + 1];
        rolesActiveActors = new HashSet[roles + 1];
        actorsActiveRoles = new HashSet[actors + roles + 1];
        unusedRoles = new HashSet(roles);

        // Skapar hashSet's i arrayens alla platser, bara på actorsActiveRoles då den inte passar in i ngn loop nedan.
        for (int i = 0; i < actorsActiveRoles.length; i++)
            actorsActiveRoles[i] = new HashSet();

        // Plats 0 kommer inte med i loopen nedan, skapar den. Förenklar iteration senare.
        scenesRoles[0] = new HashSet<>();

        // Läser in all data. Initierar arrayer med hashsets på en gång.
        int actor;
        for (int role = 1; role <= roles; role++)
        {
            rolesActiveActors[role] = new HashSet();
            unusedRoles.add(role);
            int tempLoop = io.getInt();
            for (int j = 0; j < tempLoop; j++)
            {
                actor = io.getInt();
                if (actorsRoles[actor] == null)
                    actorsRoles[actor] = new HashSet<>();
                actorsRoles[actor].add(role);
            }
        }

        // Läser in all data. Initierar arrayer med hashsets på en gång.
        for (int i = 1; i <= scenes; i++)
        {
            int temp = io.getInt();
            scenesRoles[i] = new HashSet<>(temp);
            for (int j = 0; j < temp; j++)
            {
                int role = io.getInt();
                scenesRoles[i].add(role);
            }
        }
    }

    // bara för att dela upp och göra tydliga funktioner.
    public void createFirstSolution()
    {
        setDivasRoles();

        setAllOtherRoles();
    }

    // Sätter alla icke roller.
    public void setAllOtherRoles()
    {
        // Går igenom övriga skådisar.
        for (int actor = 3; actor < actorsRoles.length; actor++)
        {
            if (actorsRoles[actor] != null)
            {
                for (Integer role : actorsRoles[actor])
                {
                    if (canPlayRole(actor, role))
                    {
                        addActorRole(actor, role);
                    }
                }
            }
        }

        // Måste göra om HashSettet till en array, kan inte iterera och ge dem en superskådis samt ta bort dem annars
        Integer[] arr = unusedRoles.toArray(new Integer[unusedRoles.size()]);
        for (Integer i : arr)
        {
            superActors++;
            addActorRole(currentSuperActor, i);
            currentSuperActor++;
        }
    }

    public void setDivasRoles()
    {
        // Spara undan vad Diva 1 fick, så att vi i värsta fall om Diva 2 inte lyckas få en roll tar bort denna från vad Diva 1 kan spela för roller.
        int divaOneTempRole = 0;

        // Ger Diva 1 vad fan hon vill ha
        for (Integer i : actorsRoles[1])
        {
            // Om Divan kan spela rollen
            if (canPlayRole(1, i))
            {
                // Ge hon roller, spara undan den för worst case def. ovan.
                divaOneTempRole = i;
                addActorRole(1, i);
                break;
            }
        }

        // Försöker ge Diva 2 vad fan hon vill ha (så länge inte Diva 1 redan spelar i scenen)
        boolean addedTwo = false;
        for (Integer i : actorsRoles[2])
        {
            if (canPlayRole(2, i))
            {
                // Ge Diva 2 rollen, sätter att hon fått en roll till true.
                addActorRole(2, i);
                addedTwo = true;
                break;
            }
        }

        // Om Diva 2 inte lyckades få en roll.
        if (!addedTwo)
        {
            // Återställer det vi skapade för Diva 1.
            // Tar bort den roll Diva 1 fick som möjlig roll hon kan spela. Kommer funkar tillslut pga. indata = JA-instans.
            actorsRoles[1].remove(divaOneTempRole);
            actorsActiveRoles[1] = new HashSet<>();

            for (int i = 0; i < rolesActiveActors.length; i++)
                rolesActiveActors[i] = new HashSet();

            // bör räcka med totalActors = 0, men why not play it safe.
            totalActors = 0;
            superActors = 0;
            currentSuperActor = actors + 1;

            // Kör allt igen!
            setDivasRoles();
        }

        // Eventuellt återsätt alla roller till Diva 1 som vi plockade bort innan, om dem går såklart.
        // EDIT: NVM, kommer gå långt över 2 sek.
    }

    // En funktion som kollar om en skådis kan spela en specifik roll utan ngn krock över huvud taget.
    public boolean canPlayRole(int actor, int role)
    {
        // Om rollen redan har fått en skådis, så kan den ju inte få en till såklart!
        if (!rolesActiveActors[role].isEmpty())
            return false;

        // Går igenom alla scener
        for (int scene = 0; scene <= scenes; scene++)
        {
            // Om vi snackar om skådis 1 eller 2 så blir det specialfall, dem får ju inte spela med varandra!
            if (actor == 1 || actor == 2)
            {
                // Räknar ut vem den andra Divan är.
                int otherDiva = (actor == 1) ? 2 : 1;

                // Går igenom alla roller som denna otherDiva redan spelar
                for (Integer i : actorsActiveRoles[otherDiva])
                {
                    // Om ngn av hennes roller redan finns med i scenen och denna nya roll finns, med, så blir det krock!
                    if (scenesRoles[scene].contains(i) && scenesRoles[scene].contains(role))
                        return false;
                }
            }

            // Om vi redan spelar en roll som finns i ngn scen där vi försöker sno en till roll.
            for (Integer i : actorsActiveRoles[actor])
            {
                if (scenesRoles[scene].contains(i) && scenesRoles[scene].contains(role))
                    return false;
            }
        }

        // Om inget inträffar ovan, så är vi gucci!
        return true;
    }

    public void addActorRole(int actor, int role)
    {
        // Om denna skådis inte spelar ngn annan roll, så är den ny!
        if (actorsActiveRoles[actor].isEmpty())
            totalActors++;

        // Rollen har nu fått en skådis, inte längre icke existerande!
        unusedRoles.remove(role);

        // Sätter rollen + skådisen till aktiv.
        // Vinklade hashSet's så att vi instnat kan kolla åt valfritt håll.
        actorsActiveRoles[actor].add(role);
        rolesActiveActors[role].add(actor);
    }

    // Skriver helt enkelt ut all data.
    public void printOutData()
    {
        io.println(totalActors);
        StringBuilder sb;
        // Fåt igenom alla skådisar
        for (int i = 0; i < actorsActiveRoles.length; i++)
        {
            // Om actorn har ngn roll
            if (actorsActiveRoles[i].size() > 0)
            {
                // Skapa ny stringbuilder, lägg till totala mängden roller.
                sb = new StringBuilder();
                sb.append(i + " " + actorsActiveRoles[i].size());

                // Gå igenom alla skådisens roller
                for (Integer role : actorsActiveRoles[i])
                {
                    // Lägg till rollerna.
                    sb.append(" " + role);
                }
                // Printa!
                io.println(sb.toString());
            }
        }
    }

    public static void main(String[] args)
    {
        new RoleSettingProblem();
    }
}
