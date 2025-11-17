public class CompressedTrie {
    CompressedTrieNode root;

    public CompressedTrie() {
        this.root = new CompressedTrieNode();
    }

    public void insert(String word) {
        if (word.isEmpty()) {
            root.isEndOfWord = true;
        }

        CompressedTrieNode child = new CompressedTrieNode();
        Edge edge = new Edge(word, child);

        root.insertEdge(edge);

    }

    public boolean search(String word) {
        return false;
    }
}
