public class CompressedTrieNode {
    private SinglyLinkedList edgeList;
    public boolean isEndOfWord;

    public CompressedTrieNode() {
        this.isEndOfWord = false;
        this.edgeList = new SinglyLinkedList();
    }

    public void insertEdge(Edge edge) {
        // check if there is another edge that starts with the same character
        Edge existing = this.edgeList.getEdge(edge.label.charAt(0));
        // if there is no edge starting with the desired character insert a new edge.
        if (existing == null) {
            edgeList.insert(edge);
            edge.child.isEndOfWord = true;
            return;
        }

        // compute the common prefix length
        int commonPrefixLength = computeCommonPrefixLength(existing.label, edge.label);

        String newLabel;
        Edge newEdge;

        // Case 1
        if (edge.label.length() == commonPrefixLength && existing.label.length() > commonPrefixLength) {
            newLabel = existing.label.substring(commonPrefixLength, existing.label.length());
            newEdge = new Edge(newLabel, existing.child);

            existing.label = edge.label;
            existing.child = new CompressedTrieNode();
            existing.child.isEndOfWord = true;

            existing.child.edgeList.insert(newEdge);

        }
        // Case 2 existing edge is prefix of edge to be inserted
        if (existing.label.length() == commonPrefixLength && edge.label.length() > commonPrefixLength) {
            newLabel = edge.label.substring(commonPrefixLength, edge.label.length());
            edge.label = newLabel;

            // move to the child of the existing node and insert the remaining of the edge
            existing.child.insertEdge(edge);
        }
        // Case 3
        if (commonPrefixLength < edge.label.length() && commonPrefixLength < existing.label.length()) {
            newLabel = existing.label.substring(0, commonPrefixLength);
            String remainingExistingLabel = existing.label.substring(commonPrefixLength, existing.label.length());
            existing.label = newLabel;
            newEdge = new Edge(remainingLabel, existing.child);


            CompressedTrieNode newNode = new CompressedTrieNode();
            newNode.edgeList.insert(newEdge);

            newEdge = new Edge()

            newNode.edgeList.insert()
        }

        // Case 4

    }

    public int computeCommonPrefixLength(String existingLabel, String labelToBeInserted) {
        int prefixLength = 0;
        if (existingLabel.length() >= labelToBeInserted.length()) {
            for (int i = 0; i < labelToBeInserted.length(); i++) {
                if (existingLabel.charAt(i) == labelToBeInserted.charAt(i)) {
                    prefixLength++;
                }
            }
        } else {
            for (int i = 0; i < existingLabel.length(); i++) {
                if (existingLabel.charAt(i) == labelToBeInserted.charAt(i)) {
                    prefixLength++;
                }
            }
        }

        return prefixLength;
    }

    public Edge getEdgeByFirstChar(char c) {

    }
}
