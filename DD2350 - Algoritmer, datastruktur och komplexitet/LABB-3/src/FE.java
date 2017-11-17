public class FE
{
    public int capacity;
    public int flow;

    public FE(int c, int f)
    {
        this.capacity = c;
        this.flow = f;
    }

    public String toString()
    {
        return "{ c: " + capacity + " f:" + flow + " }";
    }
}
