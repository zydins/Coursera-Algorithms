/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            return Double.compare(Point.this.slopeTo(o1), Point.this.slopeTo(o2));
        }
    };       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        isNull(that);
        if (this.x == that.x)
            return this.y == that.y ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        else
            return (that.y - this.y) / (double) (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        isNull(that);
        int result = Integer.compare(this.y, that.y);
        return result != 0 ? result : Integer.compare(this.x, that.x);
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private void isNull(Point that) {
        if (that == null) throw new NullPointerException();
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        StdOut.println(new Point(1, 1).compareTo(new Point(2, 2)) == -1);
        StdOut.println(new Point(1, 1).compareTo(new Point(1, 2)) == -1);
        StdOut.println(new Point(1, 1).compareTo(new Point(1, 1)) == 0);
        StdOut.println(new Point(2, 1).compareTo(new Point(1, 1)) == 1);
        Point[] arr = {new Point(1, 1), new Point(1, 4), new Point(2, 1),
                        new Point(3, 1), new Point(2, 2), new Point(3, 4)};
        Arrays.sort(arr, new Point(1, 1).SLOPE_ORDER);
        for (Point p : arr) {
            StdOut.println(p);
        }
    }
}
