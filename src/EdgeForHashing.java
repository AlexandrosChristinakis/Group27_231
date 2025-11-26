package def.src;

public class EdgeForHashing {

    protected String label;
    protected CompressedTrieNodeWithHash child;

    public EdgeForHashing(String label, CompressedTrieNodeWithHash child) {
        this.label = label;
        this.child = child;
    }

    public EdgeForHashing(String label) {
        this(label, null);
    }
}
