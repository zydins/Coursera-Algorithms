import java.util.Arrays;

/**
 * @author Sergey Zudin
 * @since 21.09.14
 */
public class Brute {
    public static void main(String[] args) {
        Point[] points = readInput(args);
        drawPoints(points);
        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                for (int r = q + 1; r < points.length - 1; r++) {
                    if (points[p].slopeTo(points[q]) != points[q].slopeTo(points[r])) break;
                    for (int s = r + 1; s < points.length; s++) {
                        if (points[p].slopeTo(points[q]) == points[r].slopeTo(points[s])) {
                            Point[] out = new Point[4];
                            out[0] = points[p];
                            out[1] = points[q];
                            out[2] = points[r];
                            out[3] = points[s];
                            Arrays.sort(out);
                            StdOut.println(out[0] + " -> " + out[1] + " -> " + out[2] + " -> " + out[3]);
                            out[0].drawTo(out[3]);
                        }
                    }
                }
            }
        }
    }

    private static Point[] readInput(String[] args) {
        In in = new In(args[0]);
        int num = in.readInt();
        Point[] points = new Point[num];
        for (int i = 0; i < num; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        return points;
    }

    private static void drawPoints(Point[] points) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
    }
}
