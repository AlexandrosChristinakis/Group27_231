package def.src;

/**
 * Robin Hood Hashing for storing trie edges.
 * Keys are based on the FIRST CHARACTER of the edge label.
 */
public class RobinHoodHashing {

	 public static void main(String[] args) {

	        System.out.println("=== Robin Hood Hash Test ===");

	        RobinHoodHashing rh = new RobinHoodHashing();

	        // Insert some edges
	        rh.insert(new EdgeForHashing("cat"));
	        rh.insert(new EdgeForHashing("dog"));
	        rh.insert(new EdgeForHashing("car"));
	        rh.insert(new EdgeForHashing("apple"));
	        rh.insert(new EdgeForHashing("ant"));
	        rh.insert(new EdgeForHashing("cow"));
	        rh.insert(new EdgeForHashing("duck"));
	        rh.insert(new EdgeForHashing("bat"));

	        System.out.println("\n=== Table After Insertions ===");
	        rh.debug();

	        // Search for edges
	        System.out.println("\n=== Search Tests ===");
	        System.out.println("dog -> " + (rh.getEdgeByFirstChar('d') != null));
	        System.out.println("cat -> " + (rh.getEdgeByFirstChar('c') != null));
	        System.out.println("apple -> " + (rh.getEdgeByFirstChar('a') != null));
	        System.out.println("zebra -> " + (rh.getEdgeByFirstChar('z') != null));

	        // Delete something
	        EdgeForHashing e = rh.getEdgeByFirstChar('c'); // delete 'cat' or 'car'
	        if (e != null) {
	            System.out.println("\n=== Deleting edge: " + e.label + " ===");
	            rh.deleteEdge(e);
	        }

	        System.out.println("\n=== Table After Deletion ===");
	        rh.debug();
	    }
	
    // ====================== ELEMENT ======================
    public static class Element {
        EdgeForHashing edge;        // The stored edge
        int probeLength;  // How far this element has probed

        Element(EdgeForHashing edge2) {
            this.edge = edge2;
            this.probeLength = 0;
        }
    }

    // ====================== TABLE FIELDS ======================
    private Element[] table;
    private int capacity;
    private int size;
    private int maxProbe;
    
    private static final int[] capacities = {3, 7, 11, 17, 23, 29};
    private int capIndex = 0;

    // ====================== CONSTRUCTOR ======================
    public RobinHoodHashing() {
        this.capacity = capacities[0];
        this.table = new Element[this.capacity];
        this.size = 0;
        this.maxProbe = 0;
    }

    // ====================== HASH FUNCTION ======================
    private int hash(char c) {
        return (c % capacity + capacity) % capacity;
    }

    // ====================== INSERT ======================
    public void insert(EdgeForHashing edge) {
        if (size >= 0.9 * capacity) {   // load factor 0.9
            rehash();
        }

        Element elem = new Element(edge);
        char key = edge.label.charAt(0);
        int index = hash(key);

        while (true) {

            if (table[index] == null) {
                table[index] = elem;
                size++;
                if (elem.probeLength > maxProbe) maxProbe = elem.probeLength;
                return;
            }

            // Robin Hood swap if incoming has larger probe distance
            if (elem.probeLength > table[index].probeLength) {
                Element temp = table[index];
                table[index] = elem;
                elem = temp;
            }

            elem.probeLength++;
            index = (index + 1) % capacity;
        }
    }

    // ====================== SEARCH BY FIRST CHAR ======================
    public EdgeForHashing getEdgeByFirstChar(char c) {
        int index = hash(c);

        for (int i = 0; i <= maxProbe; i++) {
            Element el = table[index];
            if (el == null) return null;

            if (el.edge.label.charAt(0) == c) {
                return el.edge;
            }

            index = (index + 1) % capacity;
        }

        return null;
    }

    // ====================== DELETE (for trie splitting) ======================
    public void deleteEdge(EdgeForHashing e) {
        char key = e.label.charAt(0);
        int index = hash(key);

        for (int i = 0; i <= maxProbe; i++) {

            if (table[index] == null) return;

            if (table[index].edge == e) {
                table[index] = null;
                size--;

                // SHIFT BACKWARD (Robin Hood deletion repair)
                int next = (index + 1) % capacity;

                while (table[next] != null && table[next].probeLength > 0) {
                    Element move = table[next];
                    table[next] = null;
                    move.probeLength--;
                    insert(move.edge);
                    next = (next + 1) % capacity;
                }

                return;
            }

            index = (index + 1) % capacity;
        }
    }

    // ====================== REHASH ======================
    private void rehash() {
        if (capIndex == capacities.length - 1) {
            System.out.println("Max capacity reached — cannot rehash.");
            return;
        }

        capIndex++;
        int newCap = capacities[capIndex];

        Element[] old = table;
        table = new Element[newCap];
        capacity = newCap;
        size = 0;
        maxProbe = 0;

        for (Element e : old) {
            if (e != null) {
                insert(e.edge);
            }
        }
    }

    // ====================== DEBUG PRINT ======================
    public void debug() {
        for (int i = 0; i < capacity; i++) {
            System.out.print(i + ": ");
            if (table[i] == null) {
                System.out.println("_");
            } else {
                System.out.println("(" +
                    table[i].edge.label + 
                    ", probe=" + table[i].probeLength + ")");
            }
        }
    }
    
    
}
