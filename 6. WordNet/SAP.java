import java.util.*;
import java.util.Queue;

/**
 * Created by Sergey Zudin on 07.11.14.
 */
public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G.V());
        for (int i = 0; i < G.V(); i++) {
            Iterable<Integer> adj = G.adj(i);
            for (int j : adj) {
                this.G.addEdge(i, j);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkBounds(v, w);
        int ancestor = ancestor(v, w);
        if (ancestor == -1) return -1;
        return new BreadthFirstDirectedPaths(G, v).distTo(ancestor)
                + new BreadthFirstDirectedPaths(G, w).distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkBounds(v, w);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(G, w);
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(G, v);
        Queue<Integer> queue = new LinkedList<Integer>();
        Set<Integer> prev = new HashSet<Integer>();
        int shortPath = Integer.MAX_VALUE, acId = -1;
        if (bfdpv.hasPathTo(w)) {
            shortPath = bfdpv.distTo(w);
            acId = w;
        }
        queue.add(v);
        while (true) {
            if (queue.isEmpty()) return acId;
            int id = queue.poll();
            if (prev.contains(id)) continue;
            if (bfdpw.hasPathTo(id)) {
                int dist = bfdpw.distTo(id) + bfdpv.distTo(id);
                if (dist < shortPath) {
                    shortPath = dist;
                    acId = id;
                }
            }
            for (int i : G.adj(id)) queue.add(i);
            prev.add(id);
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkParameters(v, w);
        checkBounds(v);
        checkBounds(w);
        int ancestor = ancestor(v, w);
        if (ancestor == -1) return -1;
        return new BreadthFirstDirectedPaths(G, v).distTo(ancestor)
                + new BreadthFirstDirectedPaths(G, w).distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkParameters(v, w);
        checkBounds(v);
        checkBounds(w);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(G, w);
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(G, v);
        Queue<Integer> queue = new LinkedList<Integer>();
        Set<Integer> prev = new HashSet<Integer>();
        int shortPath = Integer.MAX_VALUE, acId = -1;
        for (int i : w)
        if (bfdpv.hasPathTo(i) && bfdpv.distTo(i) < shortPath) {
            shortPath = bfdpv.distTo(i);
            acId = i;
        }
        for (int i : v) queue.add(i);
        while (true) {
            if (queue.isEmpty()) return acId;
            int id = queue.poll();
            if (prev.contains(id)) continue;
            if (bfdpw.hasPathTo(id)) {
                int dist = bfdpw.distTo(id) + bfdpv.distTo(id);
                if (dist < shortPath) {
                    shortPath = dist;
                    acId = id;
                } else {
                    continue;
                }
            }
            for (int i : G.adj(id)) queue.add(i);
            prev.add(id);
        }
    }

    private void checkParameters(Iterable... args) {
        for (Iterable arg : args)
            if (arg == null) throw new NullPointerException("parameter is null");
    }

    private void checkBounds(int... ints) {
        for (int i : ints)
            if (i < 0 || i >= G.V()) throw new IndexOutOfBoundsException();
    }

    private void checkBounds(Iterable<Integer> ints) {
            for (int i : ints)
                if (i < 0 || i >= G.V()) throw new IndexOutOfBoundsException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v1 = StdIn.readInt();
            int v2 = StdIn.readInt();
            int w1 = StdIn.readInt();
            int w2 = StdIn.readInt();
            ArrayList<Integer> v = new ArrayList<Integer>();
            v.add(v1);
            v.add(v2);
            ArrayList<Integer> w = new ArrayList<Integer>();
            w.add(w1);
            w.add(w2);
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
