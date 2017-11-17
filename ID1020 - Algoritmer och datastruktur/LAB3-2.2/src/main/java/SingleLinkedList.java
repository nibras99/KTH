public class SingleLinkedList
{
    // Variablar etc som behövs.
    protected Node head;
    protected Node tail;
    protected int size;

    // Constructor, tom, standard etcetc
    public SingleLinkedList()
    {
        head = null;
        tail = null;
        size = 0;
    }

    // Hur vi lägger till element, lägg bara till dem i slutet, behövs inget mera avancerat till denna uppgift.
    public void add(Node node) {
        if (head == null)
            head = node;
        else
            tail.next = node;

        node.next = null;
        tail = node;
        size++;
    }

    // Kan lägga till element direkt, skapar en node och lägger till i slutet av listan.
    public void add(int value) {
        Node temp = new Node(value);
        add(temp);
    }

    // Returnar första Noden
    public Node getFirst() {
        return head;
    }

    // Printar listan, för test-purpose
    public void printList() {
        Node temp = getFirst();
        for (int i = 0; i < this.size; i++) {
            System.out.println(temp.value);
            temp = temp.next;
        }
    }

    // Returnar sista Noden
    public Node getLast() {
        return tail;
    }

    // Returnar antal Node's i listan
    public int getSize() {
        return size;
    }
}