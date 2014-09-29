import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Sergey Zudin
 * @since 14.09.14
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    public Deque() {                          // construct an empty deque
        first = null;
        size = 0;
    }

    public boolean isEmpty() {                // is the deque empty?
        return first == null;
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) throws NullPointerException {         // insert the item at the front
        if (item == null) throw new NullPointerException();
        Node oldFirst = first;
        first = new Node(item);
        first.next = oldFirst;
        if (oldFirst != null) oldFirst.prev = first;
        if (size == 0) last = first;
        size++;
    }

    public void addLast(Item item) throws NullPointerException {          // insert the item at the end
        if (item == null) throw new NullPointerException();
        Node oldLast = last;
        last = new Node(item);
        if (oldLast != null) oldLast.next = last;
        last.prev = oldLast;
        if (size == 0) first = last;
        size++;
    }

    public Item removeFirst() throws NoSuchElementException {              // delete and return the item at the front
        if (first == null) throw new NoSuchElementException();
        Node res = first;
        first = first.next;
        if (first != null) first.prev = null;
        else last = null;
        size--;
        return res.item;
    }

    public Item removeLast() throws NoSuchElementException {               // delete and return the item at the end
        if (last == null) throw new NoSuchElementException();
        Node res = last;
        last = last.prev;
        if (last != null) last.next = null;
        else first = null;
        size--;
        return res.item;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new FrontToEndIterator();
    }

    private class Node {
        Item item;
        Node next;
        Node prev;

        private Node(Item item) {
            this.item = item;
        }
    }

    private class FrontToEndIterator implements Iterator<Item> {
        Node curr;

        private FrontToEndIterator() {
            curr = first;
        }

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public Item next() {
            if (curr == null) throw new NoSuchElementException();
            Node res = curr;
            curr = curr.next;
            return res.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {  // unit testing
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        for (int i : deque) {
            System.out.println(i);
        }
        StdOut.println();
        deque.removeFirst();
        deque.removeLast();
        for (int i : deque) {
            System.out.println(i);
        }
        StdOut.println(deque.isEmpty());
        StdOut.println();
        deque.removeLast();
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.size());
        for (int i : deque) {
            System.out.println(i);
        }
    }
}
