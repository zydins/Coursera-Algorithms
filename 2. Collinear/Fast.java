import java.util.*;

/**
 * @author Sergey Zudin
 * @since 21.09.14
 */
public class Fast {
    public static void main(String[] args) {
        Point[] points = readInput(args);
        Map<Double, List<List<Point>>> map = new HashMap<Double, List<List<Point>>>();
        drawPoints(points);
        for (int i = 0; i < points.length - 3; i++) {
            Point[] temp = Arrays.copyOfRange(points, i + 1, points.length);
            Arrays.sort(temp, points[i].SLOPE_ORDER);
            int j = 0;
            while (j < temp.length - 2) {
                int k = j + 1;
                double slope = points[i].slopeTo(temp[j]);
                while (k < temp.length && slope == points[i].slopeTo(temp[k])) k++;
                if (k - j >= 3) {
                    if (map.containsKey(slope)) {
                        boolean wasBefore = false;
                        for (List<Point> list : map.get(slope)) {
                            if (list.contains(points[i])) {
                                wasBefore = true;
                            }
                        }
                        if (wasBefore) break;
                        List<Point> list = new ArrayList<Point>();
                        list.add(points[i]);
                        list.addAll(Arrays.asList(temp).subList(j, k));
                        map.get(slope).add(list);
                    } else {
                        List<List<Point>> parent = new ArrayList<List<Point>>();
                        List<Point> child = new ArrayList<Point>();
                        child.add(points[i]);
                        child.addAll(Arrays.asList(temp).subList(j, k));
                        parent.add(child);
                        map.put(slope, parent);
                    }
                }
                j = k;
            }
        }
        for (List<List<Point>> parent : map.values()) {
            for (List<Point> child : parent) {
                Point[] array = child.toArray(new Point[0]);
                Arrays.sort(array);
                StdOut.print(array[0]);
                for (int i = 1; i < array.length; i++) {
                    StdOut.print(" -> " + array[i]);
                }
                StdOut.println();
                array[0].drawTo(array[array.length - 1]);
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
