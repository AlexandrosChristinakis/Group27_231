package lab8;

public class CompressedTrieNode {
	private SinglyLinkedList edgeList;
	protected boolean isEndOfWord;
	
	public CompressedTrieNode() {
		isEndOfWord = false;
		edgeList = null;
	}
	
	public void insertEdge(Edge edge) {
		// insert edge in list of edges
		if (edgeList==null){
			edgeList = new SinglyLinkedList();
		}
		edgeList.insertFront(edge);
	}
	
	public Edge getEdgeByFirstChar(char c) {
		// find appropriate edge based on the first character
		if (edgeList==null) return null;
        SinglyLinkedList.Node temp = this.edgeList.head;
		while (temp!=null){
			if (temp.edge.label.charAt(0) == c){
				return temp.edge;
			}
			temp = temp.next;
		}

		return null;
	}

	public void splitNode1(Edge edge, String prefix){
		if (edgeList==null || edgeList.findNode(edge)==false) return;
		SinglyLinkedList.Node oldNode = edgeList.head;
		while (oldNode!=null){
			if (edge==oldNode.edge){
				break;
			}
			oldNode = oldNode.next;
		}
		if (oldNode==null){
			return;
		}
		
		/* must split the label */
		String str1 = prefix; //first part
		StringBuilder strB = new StringBuilder();
		for (int i=prefix.length();i<edge.label.length();i++){
			strB = strB.append(edge.label.charAt(i));
		}
		String str2 = strB.toString(); //second part

		
		Edge prefixEdge = new Edge(str1); // create edge for prefix part
		edgeList.insertFront(prefixEdge); // add first part of split
		prefixEdge.child = oldNode.edge.child; //temporarily hold old node's child
		if (prefixEdge.child == null){
			prefixEdge.child = new CompressedTrieNode();
		}
		edgeList.deleteNode(edge); // delete old label node
		Edge suffixEdge = new Edge(str2); // create suffix part of split
		suffixEdge.child = prefixEdge.child; // suffix inherits old one's children
		prefixEdge.child = new CompressedTrieNode(); // completely new child
		prefixEdge.child.isEndOfWord = true; // prefix is a word
		prefixEdge.child.insertEdge(suffixEdge); // add suffix as a child
	}

	public void splitNode2(Edge edge, String word, String prefix){
		if (edgeList==null || edgeList.findNode(edge)==false) return;
		SinglyLinkedList.Node oldNode = edgeList.head;
		while (oldNode!=null){
			if (edge==oldNode.edge){
				break;
			}
			oldNode = oldNode.next;
		}
		if (oldNode==null){
			return;
		}

		/* get first part of split */
		StringBuilder strB1 = new StringBuilder();
		for (int i=prefix.length();i<word.length();i++){
			strB1 = strB1.append(word.charAt(i));
		}
		String str1 = strB1.toString(); //first child

		/* get second part of split */
		StringBuilder strB2 = new StringBuilder();
		for (int i=prefix.length();i<edge.label.length();i++){
			strB2 = strB2.append(edge.label.charAt(i));
		}
		String str2 = strB2.toString(); //second child

		Edge edge0 = new Edge(prefix); // create the common prefix edge
		Edge wordChild = new Edge(str1); // create the new word suffix edge
		Edge labelChild = new Edge(str2); // create the label word suffix edge
		labelChild.child = oldNode.edge.child; // label word inherits children
		edgeList.insertFront(edge0); // insert prefix in list
		edgeList.deleteNode(oldNode.edge); // delete old wordf from list
		edge0.child = new CompressedTrieNode(); // create new child for prefix edge
		edge0.child.insertEdge(labelChild); // insert label child in prefix
		edge0.child.insertEdge(wordChild); // insert word child in prefix
		wordChild.child = new CompressedTrieNode();
		wordChild.child.isEndOfWord = true;
	}
}
