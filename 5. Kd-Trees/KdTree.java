import java.util.ArrayList;
import java.util.List;

/**
* Created by Sergey Zudin on 08.10.14.
*/
public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;             // root of BST

    private class Node {
        private Point2D val;           // sorted by key
        private Node left, right;  // left and right subtrees
        private int N = 1;             // number of nodes in subtree

        public Node(Point2D val) {
            this.val = val;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : node.N;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) return;
        root = insert(root, p, VERTICAL);
    }

    private Node insert(Node node, Point2D p, boolean level) {
        if (node == null) return new Node(p);
        int cmp = compare(p, node.val, level);
        if (cmp < 0) node.left = insert(node.left, p, !level);
        else if (cmp > 0) node.right = insert(node.right, p, !level);
        else node.val = p;
        node.N = 1 + size(node.left) + size(node.right);
        return node;
    }

    private int compare(Point2D p1, Point2D p2, boolean level) {
        boolean mark = level == VERTICAL ? p1.x() < p2.x() : p1.y() < p2.y();
        return mark ? -1 : (p1.x() == p2.x() && p1.y() == p2.y()) ? 0 : 1;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (isEmpty()) return false;
        else return contains(root, p, VERTICAL);
    }

    private boolean contains(Node node, Point2D p, boolean level) {
        if (node == null) return false;
        int cmp = compare(p, node.val, level);
        if (cmp < 0) return contains(node.left, p, !level);
        else return cmp == 0 || contains(node.right, p, !level);
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty()) return;
        root.val.draw();
        StdDraw.line(0, root.val.y(), 1, root.val.y());
        drawNode(root.left, root, HORIZONTAL);
    }

    private void drawNode(Node node, Node parent, boolean level) {
        if (node == null) return;
        node.val.draw();
        if (level == VERTICAL) {
            if (node.val.x() < parent.val.x()) {
                StdDraw.line(0, node.val.y(), parent.val.x(), node.val.y());
            } else {
                StdDraw.line(parent.val.x(), node.val.y(), 1, node.val.y());
            }
        } else {
            if (node.val.y() < parent.val.y()) {
                StdDraw.line(node.val.x(), 0, node.val.x(), parent.val.y());
            } else {
                StdDraw.line(node.val.x(), parent.val.y(), node.val.x(), 1);
            }
        }
        drawNode(node.left, node, !level);
        drawNode(node.right, node, !level);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return range(new ArrayList<Point2D>(), rect, root, VERTICAL);
    }

    private List<Point2D> range(List<Point2D> list, RectHV rect, Node node, boolean level) {
        if (node == null) return list;
        if (level == VERTICAL) {
            if (rect.xmax() < node.val.x()) list = range(list, rect, node.left, HORIZONTAL);
            else if (rect.xmin() > node.val.x()) list = range(list, rect, node.right, HORIZONTAL);
            else {
                if (isInside(node.val, rect)) list.add(node.val);
                list = range(list, rect, node.left, HORIZONTAL);
                list = range(list, rect, node.right, HORIZONTAL);
            }
        } else {
            if (rect.ymax() < node.val.y()) list = range(list, rect, node.left, VERTICAL);
            else if (rect.ymin() > node.val.y()) list = range(list, rect, node.right, VERTICAL);
            else {
                if (isInside(node.val, rect)) list.add(node.val);
                list = range(list, rect, node.left, VERTICAL);
                list = range(list, rect, node.right, VERTICAL);
            }
        }
        return list;
    }

    private boolean isInside(Point2D point, RectHV rect) {
        return position(point, rect) == 0;
    }

    private int position(Point2D point, RectHV rect) {
        if (point.x() < rect.xmin() || point.y() < rect.ymin()) return -1;
        if (point.x() > rect.xmax() || point.y() > rect.ymax()) return 1;
        return 0;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        return nearest(p, root, null, VERTICAL).val;
    }

    private Node nearest(Point2D p, Node node, Node closest, boolean level) {
        if (node == null) return closest;
        if (closest == null || p.distanceTo(node.val) < p.distanceTo(closest.val)) {
            closest = node;
        }
//        if (p.distanceTo(node.val) > p.distanceTo(closest.val)) return closest;
        if (level == VERTICAL) {
            if (p.x() >= node.val.x()) closest = nearest(p, node.right, closest, HORIZONTAL);
            if (p.x() <= node.val.x()) closest = nearest(p, node.left, closest, HORIZONTAL);
        } else {
            if (p.y() >= node.val.y()) closest = nearest(p, node.right, closest, VERTICAL);
            if (p.y() <= node.val.y()) closest = nearest(p, node.left, closest, VERTICAL);
        }
        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree set = new KdTree();
        RectHV rect = new RectHV(10, 10, 20, 20);
        set.insert(new Point2D(9, 10));
        set.insert(new Point2D(15, 15)); //+
        set.insert(new Point2D(10, 15)); //-
        set.insert(new Point2D(21, 15)); //--
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
