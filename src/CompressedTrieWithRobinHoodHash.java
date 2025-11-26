package def.src;


public class CompressedTrieWithRobinHoodHash {
	CompressedTrieNodeWithHash root;
	

	public static void main(String[] args) {
	    CompressedTrie tree = new CompressedTrie();

        System.out.println("=== INSERTING WORDS ===");
        tree.insert("sigma");
        tree.insert("carton");
        tree.insert("car");
        tree.insert("cat");
        tree.insert("stock");     
        tree.insert("stop");
       

        System.out.println("\n=== SEARCH TESTS ===");
        System.out.println("sigma -> " + tree.search("sigma"));
        System.out.println("carton -> " + tree.search("carton"));
        System.out.println("car -> " + tree.search("car"));
        System.out.println("cat -> " + tree.search("cat"));
        System.out.println("stock -> " + tree.search("stock"));
        System.out.println("stop -> " + tree.search("stop"));

        System.out.println("\n=== NEGATIVE TESTS ===");
        System.out.println("sig -> " + tree.search("sig"));
        System.out.println("cars -> " + tree.search("cars"));
        System.out.println("st -> " + tree.search("st"));
        System.out.println("cater -> " + tree.search("cater"));
        System.out.println("cart -> " + tree.search("cart"));
    }

	

	public CompressedTrieWithRobinHoodHash() {
		root = new CompressedTrieNodeWithHash();
	}
	
	public void insert(String word) {
	    if (word.isEmpty()) {
	        root.isEndOfWord = true;
	        return;
	    }
	    insertRecursively(root, word);
	}

	private void insertRecursively(CompressedTrieNodeWithHash node, String word) {

	    EdgeForHashing edge = node.getEdgeByFirstChar(word.charAt(0));

	    // Case 0: No matching edge → insert full word
	    if (edge == null) {
            EdgeForHashing newEdge = new EdgeForHashing(word);
            CompressedTrieNodeWithHash child = new CompressedTrieNodeWithHash();
	        child.isEndOfWord = true;
	        newEdge.child = child;
	        node.insertEdge(newEdge);
	        return;
	    }

	    int cp = commonPrefix(edge.label, word);

	    //--------------------------------------------------------------------
	    // CASE 1: EXACT MATCH (word == edge.label)
	    //--------------------------------------------------------------------
	    if (cp == word.length() && cp == edge.label.length()) {
	        // Mark the child node as a complete word
	        edge.child.isEndOfWord = true;
	        return;
	    }

	    //--------------------------------------------------------------------
	    // CASE 2: word is PREFIX of edge.label
	    // Example: insert "car", edge.label = "carton"
	    //--------------------------------------------------------------------
	    if (cp == word.length() && cp < edge.label.length()) {
	        node.splitNode1(edge, word);
	        return;
	    }

	    //--------------------------------------------------------------------
	    // CASE 3: edge.label is PREFIX of word
	    // Example: edge.label = "car", inserting "carton"
	    //--------------------------------------------------------------------
	    if (cp == edge.label.length() && cp < word.length()) {
	        insertRecursively(edge.child, word.substring(cp));
	        return;
	    }

	    //--------------------------------------------------------------------
	    // CASE 4: They share a prefix but diverge
	    // Example: insert "cat", existing edge "car..."
	    //--------------------------------------------------------------------
	    if (cp > 0 && cp < word.length() && cp < edge.label.length()) {
	        node.splitNode2(edge, word, word.substring(0, cp));
	        return;
	    }

	    //--------------------------------------------------------------------
	    // CASE 5: No shared prefix (should not happen here)
	    //--------------------------------------------------------------------
	    // (handled by Case 0 earlier)
	}


	private static int commonPrefix(String str1, String str2){
		int result = 0;

		int minLength = Math.min(str1.length(), str2.length());
		for (int i=0;i<minLength;i++){
			if (str1.charAt(i)==str2.charAt(i)){
				result++;
			}
			else break;
		}

		return result;
	}

	public boolean search(String word){
        // Call the nodeForPrefix method. This will return null if the prefix/word does not exist in the compressed trie
        // and if it exists, it will return the node where isEndOfWord = true.
        CompressedTrieNodeWithHash nodeForPrefix = findNodeForPrefix(word);

        // Check if nodeForPrefix is null
        if (nodeForPrefix == null) {
            return false;
        }
        // If nodeForPrefix is the end of a word, increment the importance of that word.
        if (nodeForPrefix.isEndOfWord) {
            nodeForPrefix.importance++;
            return true;
        }

        // If nodeForPrefix is not the end of a word, it means that this particular word is not stored in the trie.
        return false;
	}

    public CompressedTrieNodeWithHash findNodeForPrefix(String word) {
        return findNodeForPrefixRecursively(root, word);
    }

    private CompressedTrieNodeWithHash findNodeForPrefixRecursively(CompressedTrieNodeWithHash node, String word) {
        if (word.isEmpty()){
            return node;
        }
        EdgeForHashing temp = node.getEdgeByFirstChar(word.charAt(0));
        if (temp != null){
            int commonPrefix = commonPrefix(temp.label, word);
            // now time to check if label is a prefix of our word
            if (commonPrefix == temp.label.length() && commonPrefix<word.length() ){
                return findNodeForPrefixRecursively(temp.child, word.substring(temp.label.length()));
            }
            // check if our label and word match exactly
            else if (commonPrefix == temp.label.length() && commonPrefix == word.length()) {
                // We fully consumed the label and the word.
                // The correct end-of-word flag is on the CHILD node.
                return temp.child;
            }
        }
        return null;
    }

}