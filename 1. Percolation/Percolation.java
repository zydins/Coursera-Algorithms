/**
 * @author Sergey Zudin
 * @since 07.09.14
 */
public class Percolation {
    private boolean[] openState;
    private WeightedQuickUnionUF wqu;
    private int size;

    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        if (N <= 0) throw new IllegalArgumentException();
        size = N;
        openState = new boolean[N*N];
        for (int i = 0; i < N*N; i++) {
            openState[i] = false;
        }
        wqu = new WeightedQuickUnionUF(N*N + 2);
        for (int i = 1; i <= N; i++) {
            wqu.union(0, i);
        }
        for (int i = getIndex(N, 1) + 1; i <= getIndex(N, N) + 1; i++) {
            wqu.union(N*N + 1, i);
        }
    }

    public void open(int i, int j) {           // open site (row i, column j) if it is not already
        if (isOpen(i, j)) return;
        openState[getIndex(i, j)] = true;
        if (i != 1 && isOpen(i - 1, j)) {
            wqu.union(getIndex(i - 1, j) + 1, getIndex(i, j) + 1);
        }
        if (i != size && isOpen(i + 1, j)) {
            wqu.union(getIndex(i + 1, j) + 1, getIndex(i, j) + 1);
        }
        if (j != 1 && isOpen(i, j - 1)) {
            wqu.union(getIndex(i, j - 1) + 1, getIndex(i, j) + 1);
        }
        if (j != size && isOpen(i, j + 1)) {
            wqu.union(getIndex(i, j + 1) + 1, getIndex(i, j) + 1);
        }
    }

    public boolean isOpen(int i, int j) {      // is site (row i, column j) open?
        return openState[getIndex(i, j)];
    }

    public boolean isFull(int i, int j) {      // is site (row i, column j) full?
        return openState[getIndex(i, j)] && wqu.connected(0, getIndex(i, j) + 1);
    }

    public boolean percolates() {            // does the system percolate?
        return wqu.connected(0, size*size + 1);
    }

    private int getIndex(int i, int j) {
        if (i > size || j > size || i < 1 || j < 1) throw new IndexOutOfBoundsException("i:" + i + " j:" + j);
        return (i - 1) * size + (j - 1);
    }
}
