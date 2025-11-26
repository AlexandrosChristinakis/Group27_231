package def.src;
import java.util.Scanner;

public class UserInterface {
    private CompressedTrieWithRobinHoodHash dictionaryTrie;
    private Scanner reader;

    public UserInterface() {
        this(new CompressedTrieWithRobinHoodHash());
    }

    public UserInterface(CompressedTrieWithRobinHoodHash dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
        this.reader = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.print("Enter a command: ");
            String[] command = reader.nextLine().split(" ");

            if (command[0].equals("freq")) {
                double avgFrequency = averageFrequency(command[1]);
                System.out.println("avgFrequency: " + avgFrequency);
            }

            if (command[0].equals("exit")) {
                System.exit(0);
            }

        }
    }

    private double averageFrequency(String prefix) {
        CompressedTrieNodeWithHash prefixNode = this.dictionaryTrie.findNodeForPrefix(prefix);
        int frequencyCounter = frequencyCounterRecursive(prefixNode);
        int wordsCounter = wordsCounterFromPrefixRecursive(prefixNode);

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

            for (EdgeForHashing edge: prefixNode.edges.table) {
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
}
