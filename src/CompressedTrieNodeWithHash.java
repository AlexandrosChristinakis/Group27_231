package def.src;

public class CompressedTrieNodeWithHash {

    protected RobinHoodHashing edges = new RobinHoodHashing();
    protected boolean isEndOfWord;
    protected int importance;

    public CompressedTrieNodeWithHash() {
        this.isEndOfWord = false;
        this.importance = 0;
    }

    // ------------------ INSERT EDGE USING HASH ------------------
    public void insertEdge(EdgeForHashing edge) {
        edges.insert(edge);
    }

    // ------------------ LOOKUP BY FIRST CHARACTER ------------------
    public EdgeForHashing getEdgeByFirstChar(char c) {
        return edges.getEdgeByFirstChar(c);
    }

    // ------------------ DEBUG PRINT ------------------
    public void debug(String indent) {
        edges.debug(); // or rewrite a recursive version if needed
    }

    // ------------------ SPLIT NODE 1 ------------------
    // Case: word is prefix of label
    public void splitNode1(EdgeForHashing edge, String prefix) {

        String suffix = edge.label.substring(prefix.length());

        // new prefix edge
        EdgeForHashing prefixEdge = new EdgeForHashing(prefix);
        CompressedTrieNodeWithHash prefixChild = new CompressedTrieNodeWithHash();
        prefixEdge.child = prefixChild;

        // prefix is a word
        prefixChild.isEndOfWord = true;

        // suffix edge
        EdgeForHashing suffixEdge = new EdgeForHashing(suffix);
        suffixEdge.child = edge.child;

        // remove old edge, add prefix
        edges.deleteEdge(edge);
        edges.insert(prefixEdge);

        // attach suffix to prefix child
        prefixChild.insertEdge(suffixEdge);
    }

    // ------------------ SPLIT NODE 2 ------------------
    // Case: shared prefix but diverge
    public void splitNode2(EdgeForHashing edge, String word, String prefix) {

        String wordSuffix  = word.substring(prefix.length());
        String labelSuffix = edge.label.substring(prefix.length());

        // prefix edge
        EdgeForHashing prefixEdge = new EdgeForHashing(prefix);
        CompressedTrieNodeWithHash prefixChild = new CompressedTrieNodeWithHash();
        prefixEdge.child = prefixChild;

        // label branch
        EdgeForHashing labelChild = new EdgeForHashing(labelSuffix);
        labelChild.child = edge.child;

        // new word branch
        EdgeForHashing wordChild = new EdgeForHashing(wordSuffix);
        wordChild.child = new CompressedTrieNodeWithHash();
        wordChild.child.isEndOfWord = true;

        // replace old
        edges.deleteEdge(edge);
        edges.insert(prefixEdge);

        // attach children
        prefixChild.insertEdge(labelChild);
        prefixChild.insertEdge(wordChild);
    }
}
