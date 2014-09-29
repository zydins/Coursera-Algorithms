/**
 * @author Sergey Zudin
 * @since 14.09.14
 */
public class Subset {
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        if (num <= 0) throw new IllegalArgumentException();
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (true) {
            String str = StdIn.readLine();
            if (str == null) break;
            queue.enqueue(StdIn.readLine());
        }
        if (queue.size() < num) throw new IllegalArgumentException();
        for (int i = 0; i < num; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
