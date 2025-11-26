package def.src;

public class CompressedTrie {
	CompressedTrieNode root;
	

	public static void main(String[] args) {
		CompressedTrie tree = new CompressedTrie();
		tree.insert("sigma");
		System.out.println(tree.search("sigma"));
		tree.insert("carton");
		System.out.println(tree.search("carton"));
		tree.insert("car");
		System.out.println(tree.search("car"));
		tree.insert("cat");
		System.out.println(tree.search("cat"));
		tree.insert("stock");
		System.out.print(tree.search("stock")+"\n");
		tree.insert("stop");
		System.out.print(tree.search("stop")+"\n");


	}

	public CompressedTrie() {
		root = new CompressedTrieNode();
	}
	
	public void insert (String word) {
		// implementation
		if (word.isEmpty()){
			root.isEndOfWord = true;
			return;
		}
		insertRecursively(root, word);

	}

	private void insertRecursively(CompressedTrieNode node, String word){
		Edge temp = node.getEdgeByFirstChar(word.charAt(0));

		/* if there is no similarity with existing edges 
		 * we simply insert the new word in it's entirety */
		if (temp == null){ 
			Edge wordEdge = new Edge(word);
			node.insertEdge(wordEdge);
			wordEdge.child = new CompressedTrieNode();
			wordEdge.child.isEndOfWord = true;
			return;
		}

		int commonPrefix = commonPrefix(temp.label, word);

		/* Case 1: word is prefix of label 
		 * We must split the label into the prefix part
		 * and the other part, insert the other part as the
		 * prefix's child and then mark the prefix part as 
		 * end of word.
		*/
		if (commonPrefix==word.length() && commonPrefix<temp.label.length()){
			node.splitNode1(temp, word);
		}

		/* Case 2: label is prefix of word
		 * must move to child node of label and retry
		 * to insert the remaining substring of our word
		 */
		else if (commonPrefix == temp.label.length() && commonPrefix < word.length()){
			insertRecursively(temp.child, word.substring(commonPrefix));
		}

		/* Case 3: label and word share a prefix but have different suffixes 
		 * must split label node into prefix and add both label suffix
		 * and word suffix as it's children
		*/
		else if (commonPrefix < temp.label.length() && commonPrefix < word.length() && commonPrefix>0){
			node.splitNode2(temp, word, word.substring(0, commonPrefix));
		}

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
	
	public boolean search (String word) {
		// implementation
		return searchRecursively(root, word);
	}

	private boolean searchRecursively(CompressedTrieNode node, String word){
		if (word.isEmpty()){
			return node.isEndOfWord;
		}
		Edge temp = node.getEdgeByFirstChar(word.charAt(0));
		if (temp == null){
			return false;
		}
		int commonPrefix = commonPrefix(temp.label, word);

		// now time to check if label is a prefix of our word
		if (commonPrefix == temp.label.length() && commonPrefix<word.length() ){
			return searchRecursively(temp.child, word.substring(temp.label.length()));
		}
		// check if our label and word match exactly
		else if (commonPrefix==temp.label.length() && commonPrefix==word.length()){
			return temp.child.isEndOfWord;
		}
		// mismatch
		else {
			return false;
		}
	}
}
