public class ColorEdge
{
    public int from;
    public int to;

    public ColorEdge(int from, int to)
    {
        this.from = from;
        this.to = to;
    }

    public String toString()
    {
        return "{" + this.from + " " + this.to + "}";
    }
}
