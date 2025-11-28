package def.src;


public class CompressedTrieWithRobinHoodHash {
	CompressedTrieNodeWithHash root;
	

	public static void main(String[] args) {
        CompressedTrieWithRobinHoodHash tree = new CompressedTrieWithRobinHoodHash();

        System.out.println("=== INSERTING WORDS ===");
        tree.insert("sigma");
        tree.insert("carton");
        tree.insert("car");
        tree.insert("cat");
        tree.insert("stock");     
        tree.insert("stop");
        tree.insert("apple");
        tree.insert("application");
       

        System.out.println("\n=== SEARCH TESTS ===");
        System.out.println("sigma -> " + tree.search("sigma"));
        System.out.println("carton -> " + tree.search("carton"));
        System.out.println("car -> " + tree.search("car"));
        System.out.println("cat -> " + tree.search("cat"));
        System.out.println("stock -> " + tree.search("stock"));
        System.out.println("stop -> " + tree.search("stop"));
        System.out.println("apple -> " + tree.search("apple"));

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

    protected String getPrefixToNode(CompressedTrieNodeWithHash prefixChildNode, String prefixPartial) {
        String word = "";
        CompressedTrieNodeWithHash tempNode = this.root;

        EdgeForHashing tempEdge = tempNode.getEdgeByFirstChar(prefixPartial.charAt(0));


        while (tempEdge.child != prefixChildNode) {
            word += tempEdge.label;

            prefixPartial = prefixPartial.substring(tempEdge.label.length());

            tempNode = tempEdge.child;
            tempEdge = tempNode.getEdgeByFirstChar(prefixPartial.charAt(0));
        }

        word += tempEdge.label;

        return word;
    }


	protected static int commonPrefix(String str1, String str2){
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

    public boolean search(String word) {
        return searchRecursively(this.root, word);
    }

	public boolean searchRecursively(CompressedTrieNodeWithHash node, String word){
        // Check if the word is empty
        if (word.isEmpty()){
            return false;
        }

        EdgeForHashing temp = node.getEdgeByFirstChar(word.charAt(0));

        if (temp != null){

            int commonPrefix = commonPrefix(temp.label, word);

            // Case 1: Now time to check if label is a prefix of our word
            if (commonPrefix == temp.label.length() && commonPrefix < word.length()){
                return searchRecursively(temp.child, word.substring(temp.label.length()));
            }

            // Perfect match
            if (word.length() == commonPrefix && temp.label.length() == commonPrefix) {
                if (temp.child.isEndOfWord) {
                    temp.child.importance++;
                    return true;
                } else {
                    return false;
                }
            }
        }
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

            // Case 1: Now time to check if label is a prefix of our word
            if (commonPrefix == temp.label.length() && commonPrefix < word.length()){
                return findNodeForPrefixRecursively(temp.child, word.substring(temp.label.length()));
            }

            // Case 2: Check if the word is exactly equal to prefix but label is larger.
            // This is the case in which the word is a prefix of the given edge.
            if (word.length() == commonPrefix && temp.label.length() > commonPrefix) {
                return node;
            }

            if (commonPrefix == temp.label.length() && commonPrefix == word.length()) {
                return node;
            }
        }
        return null;

    }

    public EdgeForHashing findEdgeForPrefix(String word) {
        return findEdgeForPrefixRecursively(root, word);
    }

    private EdgeForHashing findEdgeForPrefixRecursively(CompressedTrieNodeWithHash node, String word) {

        EdgeForHashing temp = node.getEdgeByFirstChar(word.charAt(0));
        if (temp != null){

            int commonPrefix = commonPrefix(temp.label, word);

            // Case 1: Now time to check if label is a prefix of our word
            if (commonPrefix == temp.label.length() && commonPrefix < word.length()){
                return findEdgeForPrefixRecursively(temp.child, word.substring(temp.label.length()));
            }

            // Case 2: Check if the word is exactly equal to prefix but label is larger.
            // This is the case in which the word is a prefix of the given edge.
            if (word.length() == commonPrefix && temp.label.length() > commonPrefix) {
                return temp;
            }

            if (commonPrefix == temp.label.length() && commonPrefix == word.length()) {
                return temp;
            }
        }
        return null;
    }

    public double averageFrequency(String prefix) {
        EdgeForHashing prefixEdge = this.findEdgeForPrefix(prefix);

        // check if the prefixNode is null.
        if (prefixEdge == null) {
            return 0.0;
        }

        // Get the edge of where the prefix is stored.
//        EdgeForHashing edge = prefixNode.getEdgeByFirstChar(prefix.charAt(0));

        // pass the edge.child to the frequencyCounterRecursive and
        // wordsCounterFromPrefixRecursive methods.
        int frequencyCounter = frequencyCounterRecursive(prefixEdge.child);
        int wordsCounter = wordsCounterFromPrefixRecursive(prefixEdge.child);

        return (double) frequencyCounter / wordsCounter;
    }

    private int wordsCounterFromPrefixRecursive(CompressedTrieNodeWithHash prefixNode) {
        int wordsCounter = 0;
        // Base case is when prefixNode == null
        if (prefixNode != null) {
            if (prefixNode.isEndOfWord) {
                wordsCounter++;
            }

            for (EdgeForHashing edge: prefixNode.edges.table) {
                if (edge == null) {
                    continue;
                }
                if (edge.occupied) {
                    wordsCounter += wordsCounterFromPrefixRecursive(edge.child);
                }
            }
        }
        return wordsCounter;
    }

    private int frequencyCounterRecursive(CompressedTrieNodeWithHash prefixNode) {
        int frequencyCounter = 0;

        // Base case is when prefixNode == null
        if (prefixNode != null) {
            if (prefixNode.isEndOfWord) {
                frequencyCounter += prefixNode.importance;
            }

            for (EdgeForHashing edge : prefixNode.edges.table) {
                // check if edge is null
                if (edge == null) {
                    continue;
                }
                if (edge.occupied) {
                    frequencyCounter += frequencyCounterRecursive(edge.child);
                }
            }
        }
        return frequencyCounter;
    }

    public char predictNextLetter(String prefix) {
        EdgeForHashing prefixEdge = this.findEdgeForPrefix(prefix);

        if (prefixEdge == null) {
            return 0;
        }

        // Get the full prefix word until the current edge.
        String fullPrefix = this.getPrefixToNode(prefixEdge.child, prefix);

        // We have to check if the fullPrefix is the same as prefix. If fullPrefix contains a larger word
        // then we should print the immediate character after the common prefix between prefix and fullPrefix
        if (!fullPrefix.equals(prefix)) {
            int commonPrefix = CompressedTrieWithRobinHoodHash.commonPrefix(fullPrefix, prefix);

            if (prefix.length() > commonPrefix) {
                System.out.println("Prefix is longer than common prefix");
                System.exit(1);
            }

            // The prefixEdge.label word is larger than the prefix word and therefore we should return the first character
            // after the common prefix.
            System.out.println("Prediction is based on word splitting current node.");
            return fullPrefix.substring(prefix.length()).charAt(0);
        }


        // Get the node of the child of prefix edge.
        CompressedTrieNodeWithHash prefixNode = prefixEdge.child;

        double maxAverageFrequency = 0;
        char maxChar = 0;


        // Iterate over all the children of the prefixNode (if any)
        for (EdgeForHashing edge: prefixNode.edges.table) {

            // Handle null edges
            if (edge == null) {
                continue;
            }

            if (edge.occupied) {
                // Edge is not null => access the label's first character.
                char labelChar = edge.label.charAt(0);

                // Compute the average frequency of the prefix + edge label.
                // prefix + edge label is the same as prefix + first char of label.
                // prefix + first char of label will give an avgFreq = Nan because
                // such a word does not exist in the dictionary.
                double averageFrequencyChar = averageFrequency(prefix + edge.label);

                // If the averageFrequency of the prefix + labelChar is greater than maxAverageFrequency
                // update the maxFrequency and save the labelChar in a separate variable
                if (averageFrequencyChar > maxAverageFrequency) {
                    maxChar = labelChar;
                    maxAverageFrequency = averageFrequencyChar;
                }
            }
        }
        return maxChar;
    }

    public void topKFrequentWordsWithPrefix(String prefix, int k) {
        // Create the minHeap that will store the k most important words that start with prefix.
        ImportanceMinHeap minHeap = new ImportanceMinHeap(k);

        // Get the edge that contains the prefix.
        EdgeForHashing prefixEdge = this.findEdgeForPrefix(prefix);

        if (prefixEdge == null) {
            System.out.println("No word exists with that prefix.");
            return;
        }

        buildImportanceMinHeap(prefixEdge.child, minHeap, prefix);
        minHeap.displayDescending();
    }

    private void buildImportanceMinHeap(CompressedTrieNodeWithHash prefixNode, ImportanceMinHeap minHeap, String prefix) {
        // check if prefixNode is null
        if (prefixNode == null) {
            return;
        }

        // check if the prefixNode is the end of a word.
        if (prefixNode.isEndOfWord) {
            // Get the full word that ends at this node.
            String word = getPrefixToNode(prefixNode, prefix);
            minHeap.insert(word, prefixNode.importance);
        }

        for (EdgeForHashing edge: prefixNode.edges.table) {
            if (edge == null || !edge.occupied) {
                continue;
            }
            // Edge is not null and it contains a label. Call again the method on the child node with the updated prefix.
            buildImportanceMinHeap(edge.child, minHeap, prefix + edge.label);
        }


    }

}