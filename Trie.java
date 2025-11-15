public class Trie {
    private class TrieNode {
        private TrieNode[] children;
        private boolean isEndOfWord;

        public TrieNode() {
            this.isEndOfWord = false;
            this.children = new TrieNode[26];
        }
    }

    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public boolean search(String word) {
        TrieNode pointer = this.root;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            int charIndex = (int) character - 'a';

            if (pointer.children[charIndex] == null) {
                return false;
            }
            pointer = pointer.children[charIndex];

        }
        return pointer.isEndOfWord;

    }

    public void insert(String word) {
        TrieNode pointer = this.root;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            int charIndex = (int) character - 'a';

            // check if the array element at charIndex is null or not.
            if (pointer.children[charIndex] == null) {
                TrieNode newNode = new TrieNode();
                pointer.children[charIndex] = newNode;
                pointer = newNode;
            } else {
                pointer = pointer.children[charIndex];
            }
        }
        pointer.isEndOfWord = true;

    }

}
