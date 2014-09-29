/**
 * @author Sergey Zudin
 * @since 07.09.14
 */
public class PercolationStats {
    private double[] experiments;

    public PercolationStats(int N, int T) {    // perform T independent computational experiments on an N-by-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        experiments = new double[T];
        for (int i = 0; i < T; i++) {
            experiments[i] = doExperiment(N);
        }
    }

    public double mean() {                     // sample mean of percolation threshold
        return StdStats.mean(experiments);
    }

    public double stddev() {                  // sample standard deviation of percolation threshold
        return StdStats.stddev(experiments);
    }

    public double confidenceLo() {            // returns lower bound of the 95% confidence interval
        return mean() - (1.96 * stddev() / Math.sqrt(experiments.length));
    }

    public double confidenceHi() {            // returns upper bound of the 95% confidence interval
        return mean() + (1.96 * stddev() / Math.sqrt(experiments.length));
    }

    private double doExperiment(int N) {
        Percolation percolation = new Percolation(N);
        int times = 0;
        while (!percolation.percolates()) {
            times++;
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            while (percolation.isOpen(i, j)) {
                i = StdRandom.uniform(1, N + 1);
                j = StdRandom.uniform(1, N + 1);
            }
            percolation.open(i, j);
        }
        return times / (double) (N * N);
    }

    public static void main(String[] args) {  // test client, described below
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
