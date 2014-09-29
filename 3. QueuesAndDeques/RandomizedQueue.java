import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Sergey Zudin
 * @since 14.09.14
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    public RandomizedQueue() {                // construct an empty randomized queue
        items = (Item[]) new Object[10];
    }

    public boolean isEmpty() {                // is the queue empty?
        return size == 0;
    }

    public int size() {                       // return the number of items on the queue
        return size;
    }

    public void enqueue(Item item) throws NullPointerException {          // add the item
        if (item == null) throw new NullPointerException();
        items[size] = item;
        size++;
        if (size == items.length) {
            Item[] array = (Item[]) new Object[size * 2];
            for (int i = 0; i < size; i++) {
                array[i] = items[i];
            }
            items = array;
        }
    }

    public Item dequeue() {                   // delete and return a random item
        int index;
        index = getRandomIndex();
        Item item = items[index];
        items[index] = items[size - 1];
        size--;
        if (size <= items.length / 4) {
            Item[] array = (Item[]) new Object[items.length / 2];
            for (int i = 0; i < size; i++) {
                array[i] = items[i];
            }
            items = array;
        }
        return item;
    }

    public Item sample() {                    // return (but do not delete) a random item
        return items[getRandomIndex()];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new RandomIterator();
    }

    private int getRandomIndex() {
        try {
            return StdRandom.uniform(size);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException();
        }
    }

    private class RandomIterator implements Iterator<Item> {
        Item[] iterItems;
        int number = 0;

        private RandomIterator() {
            iterItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                iterItems[i] = items[i];
            }
        }

        @Override
        public boolean hasNext() {
            return number != iterItems.length;
        }

        @Override
        public Item next() {
            if (size == number) throw new NoSuchElementException();
            int index = getRandomIndex();
            while (iterItems[index] == null) {
                index = getRandomIndex();
            }
            Item item = iterItems[index];
            iterItems[index] = null;
            number++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {  // unit testing
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        for (int i : queue) {
            StdOut.println(i);
        }
        StdOut.println();
        for (int i : queue) {
            StdOut.println(i);
        }
        StdOut.println();
        for (int i = 0; i < 5; i++) {
            StdOut.println(queue.sample());
        }
        StdOut.println();
        queue.dequeue();
        queue.dequeue();
        for (int i : queue) {
            StdOut.println(i);
        }
        StdOut.println();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        StdOut.println(queue.isEmpty());
        StdOut.println(queue.size());
    }
}
