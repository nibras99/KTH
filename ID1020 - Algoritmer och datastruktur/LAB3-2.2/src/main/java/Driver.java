public class Driver {
    public static void main(String[] args)
    {
        BubbleSortPointer sorter = new BubbleSortPointer();
        // Skapar en lista och lägger till värden i den givna ordningen.
        SingleLinkedList list = new SingleLinkedList();

        list.add(1);
        list.add(2);
        list.add(4);
        list.add(3);
        list.add(5);
        list.add(0);

        // Beräknar "swaps" med inversion
        System.out.println("Inversions: " + Inversions.inversions(list) + "\n-------");

        // Skriver ut listan samt dess Head och Tail.
        list.printList();
        System.out.println("Head: " + list.getFirst().value + " Tail: " + list.getLast().value);

        // Sorterar
        System.out.println("-------");
        sorter.bubbleSortPointer(list);
        System.out.println("-------");

        // Skriver ut den nya listan samt dess Head och Tail.
        list.printList();
        System.out.println("Head: " + list.getFirst().value + " Tail: " + list.getLast().value);
    }
}
