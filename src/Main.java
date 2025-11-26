package def.src;

import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        CompressedTrieWithRobinHoodHash dictionaryCompressedTrie = new CompressedTrieWithRobinHoodHash();
        ArrayList<String> fillDictionaryWords = getWordsArrayFromFile("src/dictionary.txt");
        ArrayList<String> randomTextWords = getWordsArrayFromFile("src/text_input.txt");

        // Fill dictionary with the ArrayList obtained from the dictionary.txt file.
        fillDictionaryCompressedTrie(dictionaryCompressedTrie, fillDictionaryWords);

    }

    public static ArrayList<String> getWordsArrayFromFile(String fileName) {
        ArrayList<String> wordsList = new ArrayList<>();
        // Instantiate a File object.
        File dictionaryFile = new File(fileName);
        try(Scanner reader = new Scanner(dictionaryFile)) {
            while (reader.hasNextLine()) {
                wordsList.addAll(Arrays.asList(reader.nextLine().split("\\s*[^a-zA-Z]+\\s*")));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return wordsList;
    }

    public static void fillDictionaryCompressedTrie(CompressedTrieWithRobinHoodHash dictionaryCompressedTrie, ArrayList<String> wordsList) {
        for (String word: wordsList) {
            dictionaryCompressedTrie.insert(word);
        }
    }
}
