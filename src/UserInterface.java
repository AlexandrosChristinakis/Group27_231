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

            if (command[0].equals("frequency")) {
                double avgFrequency = dictionaryTrie.averageFrequency(command[1]);
                System.out.println("avgFrequency: " + avgFrequency);
            }

            if (command[0].equals("exit")) {
                System.exit(0);
            }

            if (command[0].equals("predict")) {
                char maxChar = dictionaryTrie.predictNextLetter(command[1]);
                if (maxChar != 0) {
                    System.out.println("Predicted character: " + maxChar);
                }
            }

            if (command[0].equals("k")) {
                dictionaryTrie.topKFrequentWordsWithPrefix(command[1], Integer.valueOf(command[2]));
            }

        }
    }


}
