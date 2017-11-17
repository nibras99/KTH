public class Node {
    // Variablar vi använder för varje Node
    public int value;
    public Node next;

    // Vanlig constructor
    public Node()
    {
        this.value = 0;
        this.next = null;
    }

    // Constructor med värde och "next"
    public Node(int value, Node next)
    {
        this.value = value;
        this.next = next;
    }

    // Constructor med värde, sätter "next" som null
    public Node(int value)
    {
        this.value = value;
        this.next = null;
    }

    // Constructor som skapar en Node med samma variablar, en kopia kan man säga! Använder inte i koden just nu. Användes för tester tidigare.
    public Node(Node a)
    {
        this.value = a.value;
        this.next = a.next;
    }
}