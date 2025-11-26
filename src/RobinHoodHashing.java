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
            rh.insert(new EdgeForHashing("fox"));

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

    // ====================== TABLE FIELDS ======================
    private EdgeForHashing[] table;
    private int capacity;
    private int size;
    private int maxProbe;
    
    private static final int[] capacities = {3, 7, 11, 17, 23, 29};
    private int capIndex = 0;

    // ====================== CONSTRUCTOR ======================
    public RobinHoodHashing() {
        this.capacity = capacities[0];
        this.table = new EdgeForHashing[this.capacity];
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

        char key = edge.label.charAt(0);
        int index = hash(key);

        // Initialize the probeLength to zero
        int probeLength = 0;

        while (true) {

            if (table[index] == null) {
                table[index] = edge;
                size++;

                // Check if the probeLength is greater than maxProbe
                if (probeLength > maxProbe) maxProbe = probeLength;
                return;
            }

            // Compute the probe length of te element that is currently at table[index]
            int currentProbeLength = getProbeLength(table[index].label.charAt(0), index);

            // Robin Hood swap if incoming has larger probe distance
            if (probeLength > currentProbeLength) {
                EdgeForHashing temp = table[index];
                table[index] = edge;
                edge = temp;

                // update the probeLength of the edge that is to be inserted.
                probeLength = currentProbeLength;
            }

            probeLength++;
            index = (index + 1) % capacity;
        }
    }

    // ====================== SEARCH BY FIRST CHAR ======================
    public EdgeForHashing getEdgeByFirstChar(char c) {
        int index = hash(c);

        for (int i = 0; i <= maxProbe; i++) {
            if (table[index] == null) return null;

            if (table[index].label.charAt(0) == c) {
                return table[index];
            }

            index = (index + 1) % capacity;
        }

        return null;
    }

    // ===================== PROBE LENGTH ====================================
    public int getProbeLength(char key, int keyLocation) {
        // Get the supposed position of the key.
        int index = hash(key);

        // If the key is stored after the supposed index
        if (keyLocation > index) {
            return (keyLocation - index);
        }

        // If the key is stored before the supposed index we need another formula
        // for computing the probeLength.
        if (keyLocation < index) {
            return (capacity - index + keyLocation);
        }

        // If the index is the same as the current location of the key. probeLength = 0.
        return 0;
    }

    // ====================== MAX PROBE LENGTH =================================
    public void findNewMaxProbeLength() {
        // set the maxProbeLength to zero.
        this.maxProbe = 0;

        // Variable to store the probeLength of each instance in the table.
        int probeLength;

        // Variable to store the edge at every iteration of the loop
        EdgeForHashing e;

        for (int i = 0; i < capacity; i++) {
            // Store the edge at index i
            e = table[i];

            // check if e is null
            if (e == null) {
                continue;
            }

            // Get the probeLength of the e edge.
            probeLength = getProbeLength(e.label.charAt(0), i);

            // Compare the probeLength of the current edge with the maxProbe
            if (probeLength > this.maxProbe) {
                this.maxProbe = probeLength;
            }
        }
    }

    // ====================== DELETE (for trie splitting) ======================
    public void deleteEdge(EdgeForHashing e) {
        char key = e.label.charAt(0);
        int index = hash(key);

        for (int i = 0; i <= maxProbe; i++) {

            if (table[index] == null) return;

            if (table[index] == e) {
                table[index] = null;
                size--;

                // SHIFT BACKWARD (Robin Hood deletion repair)
                int next = (index + 1) % capacity;

                while (table[next] != null && this.getProbeLength(table[next].label.charAt(0), next) > 0) {
                    EdgeForHashing move = table[next];
                    table[next] = null;
                    insert(move);
                    next = (next + 1) % capacity;
                }
                findNewMaxProbeLength();
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

        EdgeForHashing[] old = table;
        table = new EdgeForHashing[newCap];
        capacity = newCap;
        size = 0;
        maxProbe = 0;

        for (EdgeForHashing e : old) {
            if (e != null) {
                insert(e);
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
                    table[i].label +
                    ", probe=" + this.getProbeLength(table[i].label.charAt(0), i) + ")");
            }
        }

        System.out.println("Maximum probeLength: " + this.maxProbe);
    }
    
    
}
