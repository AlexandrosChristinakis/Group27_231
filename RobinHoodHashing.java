/**
 * Implements Robin Hood Hashing, an advanced hashing algorithm
 * where elements with higher probe lengths are displaced
 * to ensure efficient insertion and retrieval operations.
 */
public class RobinHoodHashing {

    /**
     * Represents an individual element in the hash table.
     */
    public class element {
        private char key; // The key of the element.
        private int probeLength; // Probe length for displacement tracking.
        private int wordLength; // Length of the word this key is part of (if applicable).
        private int importance; // Priority/importance of this element.
        private TrieDictionary.TrieDictionaryNode next; // Pointer to the next Trie node.

        public element(char key) {
            this.key = key;
        }

        // Getters and setters for the element fields.
        public TrieDictionary.TrieDictionaryNode getNext() { return this.next; }
        public int getWordLength() { return this.wordLength; }
        public int getImportance() { return this.importance; }
        public char getKey() { return this.key; }
        public void setNext(TrieDictionary.TrieDictionaryNode next) { this.next = next; }
        public void setWordLength(int wordLength) { this.wordLength = wordLength; }
        public void setImportance(int importance) { this.importance = importance; }

        /**
         * Displays the element in a human-readable format.
         */
        public void display() {
            System.out.print("(" + key + "," + probeLength + ")");
        }
    }

    private static final int[] capacitySizes = {5, 11, 19, 29}; // Predefined table sizes for rehashing.
    private element[] table; // The hash table.
    private int capacity; // Current capacity of the hash table.
    private int size; // Current number of elements in the hash table.
    private int maxProbeLength; // Tracks the maximum probe length among elements.
    private int capacityIndex; // Index to track resizing progress.

    // Constructors for initializing the hash table.
    public RobinHoodHashing() {
        this(capacitySizes[0]);
        capacityIndex = 0;
    }

    public RobinHoodHashing(int capacity) {
        this.capacity = capacity;
        table = new element[this.capacity];
    }

    public RobinHoodHashing(int capacity, int maxProbeLength) {
        this(capacity);
        this.maxProbeLength = maxProbeLength;
    }

    /**
     * Rehashes the hash table into a larger capacity.
     */
    private void rehash() {
        if (capacityIndex == capacitySizes.length - 1) {
            System.out.println("Hash table is full: Maximum capacity reached.");
            return;
        }

        capacityIndex++;
        int newCapacity = capacitySizes[capacityIndex];
        element[] oldTable = table;
        table = new element[newCapacity];
        capacity = newCapacity;
        size = 0;
        maxProbeLength = 0;

        // Reinsert all existing elements into the new table.
        for (element e : oldTable) {
            if (e != null) {
                insert(e.key);
            }
        }
    }

    /**
     * Inserts a key into the hash table.
     * Uses the Robin Hood hashing algorithm to manage collisions.
     * @param key The key to insert.
     */
    public void insert(char key) {
        if (size == capacity) {
            rehash();
            if (size == capacity) { return; }
        }

        element elmnt = new element(key);
        elmnt.probeLength = 0;

        int index = key % capacity;

        while (true) {
            if (table[index] == null) {
                table[index] = elmnt;
                size++;
                return;
            }

            if (elmnt.probeLength > table[index].probeLength) {
                element temp = table[index];
                table[index] = elmnt;
                elmnt = temp;
            }

            elmnt.probeLength++;
            if (elmnt.probeLength > maxProbeLength) {
                maxProbeLength = elmnt.probeLength;
            }

            index = (index + 1) % capacity;
        }
    }

    /**
     * Searches for a key in the hash table.
     * @param key The key to search.
     * @return True if the key is found, false otherwise.
     */
    public boolean search(char key) {
        element elmnt = new element(key);
        int index = (elmnt.key % capacity);

        for (int i = 0; i <= maxProbeLength; i++) {
            if (table[index] == null) return false;

            if (table[index].key == elmnt.key) return true;

            index = (index + 1) % capacity;
        }

        return false;
    }

    /**
     * Retrieves an element by its key.
     * @param key The key to find.
     * @return The element or null if not found.
     */
    public element getElement(char key) {
        if (!search(key)) return null;

        int index = (key % capacity);
        for (int i = 0; i <= maxProbeLength; i++) {
            if (table[index].key == key) return table[index];

            index = (index + 1) % capacity;
        }

        return null;
    }

    /**
     * Displays the entire hash table.
     */
    public void display() {
        System.out.print("[");

        for (int i = 0; i < (capacity - 1); i++) {
            if (table[i] == null) {
                System.out.print("_,");
                continue;
            }

            table[i].display();
            System.out.print(",");
        }

        if (table[capacity - 1] == null) System.out.print("_");
        else table[capacity - 1].display();

        System.out.println("]");
    }

    // Getters for capacity and size.
    public int getCapacity() { return this.capacity; }
    public int getSize() { return this.size; }

    /**
     * Gets the element at a specific index in the hash table.
     * @param index Index to fetch from.
     * @return The element at the specified index.
     */
    public element getElementAt(int index) { return table[index]; }

    /**
     * Main method for testing Robin Hood Hashing.
     */
    public static void main(String[] args) {
        RobinHoodHashing rh = new RobinHoodHashing();

        // Insertions and automatic rehash testing.
        rh.insert('a');
        rh.insert('b');
        rh.insert('c');
        rh.insert('n');
        rh.insert('e');
        rh.insert('f'); // Will trigger rehash
        rh.insert('g');
        rh.insert('h');
        rh.insert('i');
        rh.insert('j');
        rh.insert('k'); // Will trigger rehash again
        rh.insert('l');
        rh.insert('m');
        rh.insert('o'); // Will trigger rehash again

        rh.display();
        System.out.println("Max Probe Length: " + rh.maxProbeLength);
        System.out.println("Search 'c': " + rh.search('c'));
    }
}

