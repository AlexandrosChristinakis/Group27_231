package def.src;

public class EdgeForHashing {

    protected String label;
    protected CompressedTrieNodeWithHash child;
    protected boolean occupied;

    public EdgeForHashing() {
        this.label = "";
        this.child = null;
        this.occupied = false;

    }

    public EdgeForHashing(String label, CompressedTrieNodeWithHash child) {
        this.label = label;
        this.child = child;
        this.occupied = true;
    }

    public EdgeForHashing(String label) {
        this(label, null);
    }
}
