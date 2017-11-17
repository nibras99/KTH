/**
 * Created by Casper on 2016-09-17.
 */
public class BubbleSortPointer {
    public Node swap(SingleLinkedList list, Node current, Node previous) {
        // Sparar undan nästa Node.
        Node next = current.next;

        // Om vi är på första noden så uppdaterar vi såklart "head"
        if (previous == null)
            list.head = next;
        // Annars så uppdaterar vi dess "pekare"
        else
            previous.next = next;

        // Om vi kollar första, så måste vi kolla om vi är på sista, för är vi det så uppdaterar vi tailen.
        if (next.next == null)
            list.tail = current;

        // Sedan uppdaterar vi resten av "pekarna"
        previous = next;
        current.next = next.next;
        next.next = current;

        // Och returnar den nya previous node, så vi slipper "hitta" den för varje gång.
        return previous;
    }

    public void bubbleSortPointer(SingleLinkedList list)
    {
        // Variablar vi kommer ha nytta av.
        int R = list.getSize() - 2;
        boolean swapped = true;
        int swaps = 0;

        // Så länge vi inte gått igenom listan och vi har swappat.
        while (R >= 0 && swapped == true)
        {
            // Återställer, hämta värden vi behöver för varje gång vi "går från början"
            swapped = false;
            Node current = list.getFirst();
            Node previous = null;

            // För varje värde vi inte sorterat.
            for(int i = 0; i <= R; i++)
            {
                // Om värdet vi är på är större så vill vi ju byta plats på dem!
                if (current.value > current.next.value)
                {
                    // Så vi byter, sätter swapped som true, och ökar swaps med 1!
                    swapped = true;
                    swaps++;
                    previous = swap(list, current, previous);
                }
                else
                {
                    // Annars går vi såklart endast vidare till nästa element.
                    previous = current;
                    current = current.next;
                }
            }
            // Minskar R med 1, så att vi inte kollar redan sorterade platser!
            R--;
        }
        // När allt är klart så skriver vi ut hur många Swaps vi gjorde.
        System.out.println("Swaps: " + swaps);
    }

    // Används inte, men funkar likadant fast den byter värden, inte pekare.
    public void bubbleSortValue(SingleLinkedList list) {
        Node currentNode;
        int size = list.size - 2;
        boolean swapped = true;
        while (size >= 0 && swapped == true)
        {
            swapped = false;
            currentNode = list.getFirst();
            for(int i = 0; i <= size; i++)
            {
                if (currentNode.value > currentNode.next.value)
                {
                    swapped = true;
                    int temp = currentNode.value;
                    currentNode.value = currentNode.next.value;
                    currentNode.next.value = temp;
                }
                currentNode = currentNode.next;
            }
            size = size - 1;
        }
    }
}
