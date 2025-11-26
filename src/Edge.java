package def.src;

public class Edge {
	protected String label;
	protected CompressedTrieNode child;
	
	public Edge (String label, CompressedTrieNode child) {
		this.label = label;
		this.child = child;
	}
	
	public Edge (String label) {
		this(label, null);
	}
}
