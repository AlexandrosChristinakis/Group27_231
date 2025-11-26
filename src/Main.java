package def.src;

import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        CompressedTrieWithRobinHoodHash dictionaryCompressedTrie = new CompressedTrieWithRobinHoodHash();
        ArrayList<String> wordsList = getWordsArrayFromFile("src/dictionary.txt");
    }

    public static ArrayList<String> getWordsArrayFromFile(String fileName) {
        ArrayList<String> wordsList = new ArrayList<>();
        // Instantiate a File object.
        File dictionaryFile = new File(fileName);
        try(Scanner reader = new Scanner(dictionaryFile)) {
            while (reader.hasNextLine()) {
                wordsList.add(reader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }

        return wordsList;
    }
}
