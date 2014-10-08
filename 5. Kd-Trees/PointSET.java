import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey Zudin on 08.10.14.
 */
public class PointSET {
    private RedBlackBST<Point2D, Point2D> tree;

    // construct an empty set of points
    public PointSET() {
        tree = new RedBlackBST<Point2D, Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        tree.put(p, p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : tree.keys()) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return iterate(rect, new ArrayList<Point2D>(), 0);
    }

    private List<Point2D> iterate(RectHV rect, List<Point2D> list, int index) {
        Point2D point = tree.select(index);
        if (point == null) return list;
        int pos = position(point, rect);
        if (pos == 0) list.add(point);
        list = iterate(rect, list, index * 2 + 2);
        list = iterate(rect, list, index * 2 + 1);
        return list;
    }

    private int position(Point2D point, RectHV rect) {
        if (point.x() < rect.xmin() || point.y() < rect.ymin()) return -1;
        if (point.x() > rect.xmax() || point.y() > rect.ymax()) return 1;
        return 0;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D p1 = tree.floor(p);
        Point2D p2 = tree.ceiling(p);
        if (p1 == null) return p2;
        else if (p2 == null) return p1;
        return p.distanceTo(p1) < p.distanceTo(p2) ? p1 : p2;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();
        RectHV rect = new RectHV(10, 10, 20, 20);
        set.insert(new Point2D(9, 10));
        set.insert(new Point2D(15, 15)); //+
        set.insert(new Point2D(10, 15)); //-
        set.insert(new Point2D(21, 15));
        set.insert(new Point2D(21, 21));
        set.insert(new Point2D(20, 20)); //-
        set.insert(new Point2D(10, 10)); //+
        for (Point2D p : set.range(rect)) {
            StdOut.println(p);
        }
        StdOut.println();
        StdOut.println(set.nearest(new Point2D(0, 0)));
        StdOut.println(set.nearest(new Point2D(20, 22)));
    }
}
