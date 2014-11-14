/**
 * Created by Sergey Zudin on 07.11.14.
 */
public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        wordNet = wordnet;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        if (nouns.length < 2) throw new IllegalArgumentException();
        int[] sums = new int[nouns.length];
        for (int i = 0; i < nouns.length - 1; i++) {
            if (!wordNet.isNoun(nouns[i])) throw new IllegalArgumentException();
            for (int j = i + 1; j < nouns.length; j++) {
                sums[i] += wordNet.distance(nouns[i], nouns[j]);
                sums[j] += wordNet.distance(nouns[i], nouns[j]);
            }
        }
        int res = 0;
        for (int i = 1; i < sums.length; i++) {
            if (sums[i] > sums[res]) res = i;
        }
        return nouns[res];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
