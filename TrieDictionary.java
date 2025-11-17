package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * TrieDictionary is a trie-based implementation for managing a dictionary of words.
 * It supports efficient word insertion, retrieval, and prefix-based searches.
 * Additional features include word importance tracking and memory usage calculation.
 */
public class TrieDictionary {
    
    /**
     * Represents a node in the trie.
     * Each node contains a collection of child nodes stored in a RobinHoodHashing structure.
     */
    public class TrieDictionaryNode {
        private RobinHoodHashing children;

        /**
         * Constructor initializes the node with an empty RobinHoodHashing collection.
         */
        private TrieDictionaryNode() {
            this.children = new RobinHoodHashing();
        }
    }

    // Root node of the trie
    TrieDictionaryNode root;

    /**
     * Constructs an empty TrieDictionary.
     */
    public TrieDictionary() {
        this.root = null;
    }

    /**
     * Inserts a word into the trie. Creates new nodes for characters as needed.
     * Marks the last character of the word with its length.
     * @param word The word to insert.
     */
    public void insertWord(String word) {
        if (this.root == null)
            this.root = new TrieDictionaryNode();

        TrieDictionaryNode current = this.root;

        int n = word.length();
        for (int i = 0; i < n; i++) {
            if (current == null)
                current = new TrieDictionaryNode();

            char t = word.charAt(i);

            // Insert character if not already present
            if (!current.children.search(t))
                current.children.insert(t);

            // Mark the end of the word
            if (i == (n - 1)) {
                current.children.getElement(t).setWordLength(n);
                break;
            }

            // Ensure next node exists for the character
            if (current.children.getElement(t).getNext() == null)
                current.children.getElement(t).setNext(new TrieDictionaryNode());

            current = current.children.getElement(t).getNext();
        }
    }

    /**
     * Checks if a word exists in the trie.
     * @param word The word to search for.
     * @return True if the word exists, false otherwise.
     */
    public boolean existsWord(String word) {
        TrieDictionaryNode current = this.root;
        int n = word.length();
        for (int i = 0; i < n; i++) {
            if (current == null)
                return false;

            char t = word.charAt(i);

            if (!current.children.search(t))
                return false;

            if (i == (n - 1))
                break;

            current = current.children.getElement(t).getNext();
        }

        // Check if the last character marks the end of a valid word
        return current.children.getElement(word.charAt(n - 1)).getWordLength() > 0;
    }

    /**
     * Retrieves the RobinHoodHashing.element for the last character of a word.
     * @param word The word to retrieve.
     * @return The associated element, or null if the word does not exist.
     */
    public RobinHoodHashing.element getWord(String word) {
        TrieDictionaryNode current = this.root;
        int n = word.length();
        for (int i = 0; i < n; i++) {
            if (current == null)
                return null;

            char t = word.charAt(i);

            if (!current.children.search(t))
                return null;

            if (i == (n - 1))
                break;

            current = current.children.getElement(t).getNext();
        }

        RobinHoodHashing.element element = current.children.getElement(word.charAt(n - 1));
        return (element.getWordLength() > 0) ? element : null;
    }

    /**
     * Imports words from a file and inserts them into the trie.
     * @param dFile The file containing words (one per line).
     */
    public void importWords(File dFile) {
        try (Scanner fileReader = new Scanner(dFile)) {
            while (fileReader.hasNextLine())
                insertWord(fileReader.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Error: Couldn't find the file.");
            System.exit(0);
        }
    }

    /**
     * Increases the importance score of a word by 1.
     * @param word The word whose importance is to be increased.
     */
    private void increaseImportance(String word) {
        RobinHoodHashing.element tmp = getWord(word);
        if (tmp != null)
            tmp.setImportance(tmp.getImportance() + 1);
    }

    /**
     * Removes leading and trailing non-alphabetic characters from a string.
     * @param str The string to clean.
     * @return The cleaned string.
     */
    private static String removePunctuation(String str) {
        if ((str.charAt(0) < 'a') || (str.charAt(0) > 'z'))
            str = str.substring(1);

        int n = str.length();
        if ((str.charAt(n - 1) < 'a') || (str.charAt(n - 1) > 'z'))
            str = str.substring(0, n - 1);

        return str;
    }

    /**
     * Reads a file of words and updates their importance scores in the trie.
     * @param iFile The file containing words.
     */
    public void readImportanceFile(File iFile) {
        try (Scanner fileReader = new Scanner(iFile)) {
            while (fileReader.hasNext())
                increaseImportance(removePunctuation(fileReader.next()));
        } catch (FileNotFoundException e) {
            System.out.println("Error: Couldn't find the file.");
            System.exit(0);
        }
    }

    /**
     * Finds all words starting with a given prefix and stores them in a min-heap based on importance.
     * @param prefix The prefix to search for.
     * @param altrntivs The min-heap to store matching words.
     */
    public void findWordsWithPrefix(String prefix, ImportanceMinHeap altrntivs) {
        TrieDictionaryNode current = root;

        for (char c : prefix.toCharArray()) {
            if (current == null || !current.children.search(c)) {
                System.out.println("No words found with prefix: " + prefix);
                return;
            }
            current = current.children.getElement(c).getNext();
        }

        collectWords(current, prefix, altrntivs);
    }

    /**
     * Recursively collects words starting from a given node.
     * @param node The starting node.
     * @param prefix The accumulated prefix for the words.
     * @param altrntivs The min-heap to store the words.
     */
    private void collectWords(TrieDictionaryNode node, String prefix, ImportanceMinHeap altrntivs) {
        if (node == null)
            return;

        for (int i = 0; i < node.children.getCapacity(); i++) {
            RobinHoodHashing.element element = node.children.getElementAt(i);

            if (element != null) {
                String newPrefix = prefix + element.getKey();

                // Add word to heap if it marks the end of a valid word
                if (element.getWordLength() > 0) {
                    altrntivs.insert(newPrefix, element.getImportance());
                }

                // Recursively explore child nodes
                collectWords(element.getNext(), newPrefix, altrntivs);
            }
        }
    }

    /**
     * Determines if a given character is present in a word.
     * @param word The word to check.
     * @param c The character to look for.
     * @return True if the character exists in the word, false otherwise.
     */
    private static boolean contains(String word, char c) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == c)
                return true;
        }
        return false;
    }

    /**
     * Recursively collects words of the same length as the target word.
     * Allows up to two mismatches.
     * @param node The current trie node.
     * @param targetWord The target word to match.
     * @param currentWord The accumulated current word during traversal.
     * @param depth The current depth in the trie.
     * @param mismatches The number of mismatches so far.
     * @param altrntivs The min-heap to store the matched words.
     */
    private void collectSameLength(TrieDictionaryNode node, String targetWord, String currentWord, int depth,
                                   int mismatches, ImportanceMinHeap altrntivs) {
        if (node == null) {
            return;
        }

        // Iterate through all children of the current node
        for (int i = 0; i < node.children.getCapacity(); i++) {
            RobinHoodHashing.element element = node.children.getElementAt(i);
            if (element != null) {
                char nextChar = element.getKey();

                // Update mismatch count based on whether the character exists in the target word
                int newMismatches = mismatches + (!contains(targetWord, nextChar) ? 1 : 0);

                // Check if we've reached the last character of the target word
                if (depth + 1 == targetWord.length()) {
                    if (mismatches <= 2) {
                        for (int j = 0; j < node.children.getCapacity(); j++) {
                            element = node.children.getElementAt(j);
                            if (element != null && element.getWordLength() == targetWord.length()) {
                                altrntivs.insert(currentWord + element.getKey(), element.getImportance());
                            }
                        }
                    }
                    return;
                }

                // Continue traversal if mismatches are within the allowed limit
                if (newMismatches <= 2) {
                    collectSameLength(element.getNext(), targetWord, currentWord + nextChar, depth + 1, newMismatches,
                                      altrntivs);
                }
            }
        }
    }

    /**
     * Finds words in the trie that match the length of the target word, allowing up to two mismatches.
     * @param targetWord The target word for length-based matching.
     * @param altrntivs The min-heap to store the matching words.
     */
    public void findWordsWithSameLength(String targetWord, ImportanceMinHeap altrntivs) {
        TrieDictionaryNode current = root;

        if (current == null) {
            System.out.println("No words in the dictionary.");
            return;
        }

        collectSameLength(current, targetWord, "", 0, 0, altrntivs);
    }

    /**
     * Finds all possible subsequences of a string by removing one character at a time.
     * @param str The input string.
     * @return A list containing all possible subsequences.
     */
    public static SingleList<String> findAllPossibleSequences(String str) {
        SingleList<String> sequences = new SingleList<>();

        for (int i = 0; i < str.length(); i++) {
            if (i > 0)
                sequences.insertLast(str.substring(0, i) + str.substring(i + 1));
            else
                sequences.insertLast(str.substring(1));
        }

        return sequences;
    }

    /**
     * Checks if a sequence is contained within a word in order.
     * @param word The word to search within.
     * @param sequence The sequence to check.
     * @return True if the sequence is found in order, false otherwise.
     */
    private static boolean check(String word, String sequence) {
        int sequenceIndex = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == sequence.charAt(sequenceIndex)) {
                sequenceIndex++;
                if (sequenceIndex == sequence.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if a word contains any sequence derived from the input string.
     * @param word The word to check.
     * @param sequence The sequence to compare against.
     * @return True if the word contains a valid sequence, false otherwise.
     */
    private static boolean containsSequence(String word, String sequence) {
        SingleList<String> sequences = findAllPossibleSequences(sequence);
        SingleList<String>.ListNode<String> node = sequences.getFrontNode();
        while (node != null) {
            if (check(word, node.getElement()))
                return true;
            node = node.getNext();
        }
        return false;
    }

    /**
     * Recursively collects words with different lengths compared to the target word.
     * @param node The current trie node.
     * @param targetWord The target word to match.
     * @param currentWord The accumulated current word during traversal.
     * @param depth The current depth in the trie.
     * @param altrntivs The min-heap to store the matched words.
     */
    private void collectDifferentLength(TrieDictionaryNode node, String targetWord, String currentWord, int depth,
                                        ImportanceMinHeap altrntivs) {
        if (node == null) {
            return;
        }

        // Iterate over children
        for (int i = 0; i < node.children.getCapacity(); i++) {
            RobinHoodHashing.element currentElement = node.children.getElementAt(i);
            if (currentElement != null) {
                char nextChar = currentElement.getKey();

                // Check if depth is within the allowed length range
                if ((depth + 1) <= (targetWord.length() + 2) && (depth + 1) >= (targetWord.length() - 1)) {
                    for (int j = 0; j < node.children.getCapacity(); j++) {
                        RobinHoodHashing.element element = node.children.getElementAt(j);
                        if (element != null && element.getWordLength() > 0) {
                            if (containsSequence(currentWord + element.getKey(), targetWord)) {
                                if (!altrntivs.existsNode(currentWord + element.getKey(), element.getImportance()))
                                    altrntivs.insert(currentWord + element.getKey(), element.getImportance());
                            }
                        }
                    }
                }

                // Recursively explore child nodes
                collectDifferentLength(currentElement.getNext(), targetWord, currentWord + nextChar, depth + 1,
                                       altrntivs);
            }
        }
    }

    /**
     * Finds words in the trie with lengths different from the target word.
     * Supports sequences derived from the target word.
     * @param targetWord The target word for length-based matching.
     * @param altrntivs The min-heap to store the matching words.
     */
    public void findWordsWithDifferentLength(String targetWord, ImportanceMinHeap altrntivs) {
        TrieDictionaryNode current = root;

        if (current == null) {
            System.out.println("No words in the dictionary.");
            return;
        }

        collectDifferentLength(current, targetWord, "", 0, altrntivs);
    }

    /**
     * Calculates the memory usage of the trie structure.
     * @return The total memory used in bytes.
     */
    public long calcMemoryDrainage() {
        return traverseTrieHelper(this.root, "", 0);
    }

    /**
     * Recursively traverses the trie to calculate memory usage.
     * @param node The current trie node.
     * @param prefix The accumulated prefix during traversal.
     * @param totalBytes The running total of memory usage.
     * @return The total memory used in bytes.
     */
    private long traverseTrieHelper(TrieDictionaryNode node, String prefix, long totalBytes) {
        if (node == null) {
            return totalBytes;
        }

        // Iterate over children
        for (int i = 0; i < node.children.getCapacity(); i++) {
            RobinHoodHashing.element element = node.children.getElementAt(i);

            // Add memory for each element and its fields
            totalBytes += 8;
            if (element != null) {
                totalBytes += getElementSize();
                totalBytes = traverseTrieHelper(element.getNext(), prefix + element.getKey(), totalBytes);
            }
        }

        return totalBytes;
    }

    /**
     * Estimates the memory size of an element object in bytes.
     * @return The size of an element object.
     */
    private int getElementSize() {
        return Character.BYTES + Integer.BYTES * 3 + 8; // key, probeLength, wordLength, importance, reference to next
    }

    /**
     * Main method for testing the TrieDictionary.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        TrieDictionary dict = new TrieDictionary();
        dict.insertWord("plan");
        dict.calcMemoryDrainage();
    }
}

