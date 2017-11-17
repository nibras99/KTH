public class Inversions {
    public static int inversions(SingleLinkedList list) {
        int i = 0;
        Node x = list.getFirst();
        while (x != null) {
            Node y = x.next;
            while (y != null) {
                if (x.value > y.value)
                    i++;
                y = y.next;
            }
            x = x.next;
        }
        return i;
    }
}