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

            if (command[0].equals("predict")) {
                char maxChar = predictNextLetter(command[1]);
                if (maxChar != 0) {
                    System.out.println("Predicted character: " + maxChar);
                }
            }

        }
    }

    private double averageFrequency(String prefix) {
        EdgeForHashing prefixEdge = this.dictionaryTrie.findEdgeForPrefix(prefix);

        // check if the prefixNode is null.
        if (prefixEdge == null) {
            return 0.0;
        }

        // Get the edge of where the prefix is stored.
//        EdgeForHashing edge = prefixNode.getEdgeByFirstChar(prefix.charAt(0));

        // pass the edge.child to the frequencyCounterRecursive and
        // wordsCounterFromPrefixRecursive methods.
        int frequencyCounter = frequencyCounterRecursive(prefixEdge.child);
        int wordsCounter = wordsCounterFromPrefixRecursive(prefixEdge.child);

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

    private char predictNextLetter(String prefix) {
        EdgeForHashing prefixNode = this.dictionaryTrie.findEdgeForPrefix(prefix);

        if (prefixNode == null) {
            return 0;
        }

        double maxAverageFrequency = 0;
        char maxChar = 0;


        // Iterate over all the children of the prefixNode (if any)
//        for (EdgeForHashing edge: prefixNode.edges.table) {
//
//            // Handle null edges
//            if (edge == null) {
//                continue;
//            }
//
//            if (edge.occupied) {
//                // Edge is not null => access the label's first character.
//                char labelChar = edge.label.charAt(0);
//
//                // Compute the average frequency of the prefix + edge label.
//                // prefix + edge label is the same as prefix + first char of label.
//                // prefix + first char of label will give an avgFreq = Nan because
//                // such a word does not exist in the dictionary.
//                double averageFrequencyChar = averageFrequency(prefix + edge.label);
//
//                // If the averageFrequency of the prefix + labelChar is greater than maxAverageFrequency
//                // update the maxFrequency and save the labelChar in a separate variable
//                if (averageFrequencyChar > maxAverageFrequency) {
//                    maxChar = labelChar;
//                    maxAverageFrequency = averageFrequencyChar;
//                }
//            }
//        }
        return maxChar;
    }
}
