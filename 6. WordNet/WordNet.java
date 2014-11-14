import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sergey Zudin on 07.11.14.
 */
public class WordNet {
    private class Synset {
        private int id;
        private String[] nouns;

        public Synset(int id, String[] synonyms) {
            this.id = id;
            this.nouns = synonyms;
        }

        public String[] getNouns() {
            return nouns;
        }

        public int getId() {
            return id;

        }
    }
    private Map<String, List<Synset>> map;
    private Synset[] synsets;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkParameters(synsets, hypernyms);
        this.map = new HashMap<String, List<Synset>>();
        List<Synset> temp = new ArrayList<Synset>();
        In in = new In(synsets);
        if (!in.exists()) throw new IllegalArgumentException();
        while (in.hasNextLine()) {
            try {
                String[] strings = in.readLine().split(",");
                String[] nouns = strings[1].split(" ");
                Synset synset = new Synset(Integer.parseInt(strings[0]), nouns);
                temp.add(synset);
                for (String str : synset.getNouns()) {
                    List<Synset> list = map.containsKey(str) ? map.get(str) : new ArrayList<Synset>();
                    list.add(synset);
                    this.map.put(str, list);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }
        this.synsets = new Synset[temp.size()];
        temp.toArray(this.synsets);
        Digraph G = new Digraph(this.synsets.length);
        in = new In(hypernyms);
        if (!in.exists()) throw new IllegalArgumentException();
        while (in.hasNextLine()) {
            try {
                String[] strings = in.readLine().split(",");
                int id = Integer.parseInt(strings[0]);
                for (int i = 1; i < strings.length; i++) {
                    int idh = Integer.parseInt(strings[i]);
                    G.addEdge(id, idh);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }
        int num = 0;
        for (Synset s : this.synsets) {
            Bag b = (Bag) G.adj(s.getId());
            if (b.size() == 0) num++;
        }
        if (num != 1) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkParameters(word);
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkParameters(nounA, nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Not a noun");
        if (isMultiple(nounA) || isMultiple (nounB)) return sap.length(getIds(nounA), getIds(nounB));
        else return sap.length(map.get(nounA).get(0).getId(), map.get(nounB).get(0).getId());
    }

    private boolean isMultiple(String noun) {
        return map.get(noun).size() > 1;
    }

    private Iterable<Integer> getIds(String noun) {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Synset s : map.get(noun)) ids.add(s.getId());
        return ids;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkParameters(nounA, nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Not a noun");
        int id;
        if (isMultiple(nounA) || isMultiple (nounB)) id = sap.ancestor(getIds(nounA), getIds(nounB));
        else id = sap.ancestor(map.get(nounA).get(0).getId(), map.get(nounB).get(0).getId());
        StringBuilder builder = new StringBuilder();
        for (String str : synsets[id].getNouns()) {
            builder.append(str);
            builder.append(" ");
        }
        return builder.toString();
    }

    private void checkParameters(String... args) {
        for (String arg : args)
            if (arg == null) throw new NullPointerException("parameter is null");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.isNoun("vicinity"));
        StdOut.println(wordnet.isNoun("hellout"));
        StdOut.println(wordnet.isNoun("Aberdeen"));
        StdOut.println(((Set<String>) wordnet.nouns()).size());
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wordnet.distance(v, w);
            String ancestor = wordnet.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}
