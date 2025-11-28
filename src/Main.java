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

        // Update the dictionaryCompressedTrie importance
        updateCompressTrieNodeImportance(dictionaryCompressedTrie, randomTextWords);

        UserInterface ui = new UserInterface(dictionaryCompressedTrie);
        ui.start();

//        System.out.println("Q2 averageFrequency(\"a\") = " + dictionaryCompressedTrie.averageFrequency("a"));
//        System.out.println("Q2 averageFrequency(\"b\") = " + dictionaryCompressedTrie.averageFrequency("b"));
//        System.out.println("Q2 averageFrequency(\"c\") = " + dictionaryCompressedTrie.averageFrequency("c"));
//        System.out.println("Q2 averageFrequency(\"d\") = " + dictionaryCompressedTrie.averageFrequency("d"));
//        System.out.println("Q2 averageFrequency(\"f\") = " + dictionaryCompressedTrie.averageFrequency("f"));
//
//        System.out.println("Q2 averageFrequency(\"ap\") = " + dictionaryCompressedTrie.averageFrequency("ap"));
//        System.out.println("Q2 averageFrequency(\"ba\") = " + dictionaryCompressedTrie.averageFrequency("ba"));
//        System.out.println("Q2 averageFrequency(\"ca\") = " + dictionaryCompressedTrie.averageFrequency("ca"));
//        System.out.println("Q2 averageFrequency(\"fa\") = " + dictionaryCompressedTrie.averageFrequency("fa"));
//        System.out.println("Q2 averageFrequency(\"la\") = " + dictionaryCompressedTrie.averageFrequency("la"));
//
//        System.out.println("Q2 averageFrequency(\"app\") = " + dictionaryCompressedTrie.averageFrequency("app"));
//        System.out.println("Q2 averageFrequency(\"ban\") = " + dictionaryCompressedTrie.averageFrequency("ban"));
//        System.out.println("Q2 averageFrequency(\"cat\") = " + dictionaryCompressedTrie.averageFrequency("cat"));
//        System.out.println("Q2 averageFrequency(\"land\") = " + dictionaryCompressedTrie.averageFrequency("land"));
//        System.out.println("Q2 averageFrequency(\"fast\") = " + dictionaryCompressedTrie.averageFrequency("fast"));
//
//        System.out.println("Q2 averageFrequency(\"appl\") = " + dictionaryCompressedTrie.averageFrequency("appl"));
//        System.out.println("Q2 averageFrequency(\"caterp\") = " + dictionaryCompressedTrie.averageFrequency("caterp"));
//        System.out.println("Q2 averageFrequency(\"stor\") = " + dictionaryCompressedTrie.averageFrequency("stor"));
//        System.out.println("Q2 averageFrequency(\"sig\") = " + dictionaryCompressedTrie.averageFrequency("sig"));
//        System.out.println("Q2 averageFrequency(\"lan\") = " + dictionaryCompressedTrie.averageFrequency("lan"));
//        System.out.println("Q2 averageFrequency(\"lanx\") = " + dictionaryCompressedTrie.averageFrequency("lanx"));
//
//        System.out.println("============================================================");
//        System.out.println("Q3 predictNextLetter(\"a\") = " + dictionaryCompressedTrie.predictNextLetter("a"));
//        System.out.println("Q3 predictNextLetter(\"b\") = " + dictionaryCompressedTrie.predictNextLetter("b"));
//        System.out.println("Q3 predictNextLetter(\"c\") = " + dictionaryCompressedTrie.predictNextLetter("c"));
//        System.out.println("Q3 predictNextLetter(\"f\") = " + dictionaryCompressedTrie.predictNextLetter("f"));
//        System.out.println("Q3 predictNextLetter(\"l\") = " + dictionaryCompressedTrie.predictNextLetter("l"));
//
//        System.out.println("Q3 predictNextLetter(\"ap\") = " + dictionaryCompressedTrie.predictNextLetter("ap"));
//        System.out.println("Q3 predictNextLetter(\"ba\") = " + dictionaryCompressedTrie.predictNextLetter("ba"));
//        System.out.println("Q3 predictNextLetter(\"fa\") = " + dictionaryCompressedTrie.predictNextLetter("fa"));
//        System.out.println("Q3 predictNextLetter(\"la\") = " + dictionaryCompressedTrie.predictNextLetter("la"));
//        System.out.println("Q3 predictNextLetter(\"si\") = " + dictionaryCompressedTrie.predictNextLetter("si"));
//
//        System.out.println("Q3 predictNextLetter(\"app\") = " + dictionaryCompressedTrie.predictNextLetter("app"));
//        System.out.println("Q3 predictNextLetter(\"ban\") = " + dictionaryCompressedTrie.predictNextLetter("ban"));
//        System.out.println("Q3 predictNextLetter(\"cat\") = " + dictionaryCompressedTrie.predictNextLetter("cat"));
//        System.out.println("Q3 predictNextLetter(\"fast\") = " + dictionaryCompressedTrie.predictNextLetter("fast"));
//        System.out.println("Q3 predictNextLetter(\"stor\") = " + dictionaryCompressedTrie.predictNextLetter("stor"));
//
//        System.out.println("Q3 predictNextLetter(\"appl\") = " + dictionaryCompressedTrie.predictNextLetter("appl"));
//        System.out.println("Q3 predictNextLetter(\"caterp\") = " + dictionaryCompressedTrie.predictNextLetter("caterp"));
//        System.out.println("Q3 predictNextLetter(\"sig\") = " + dictionaryCompressedTrie.predictNextLetter("sig"));
//        System.out.println("Q3 predictNextLetter(\"lan\") = " + dictionaryCompressedTrie.predictNextLetter("lan"));
//        System.out.println("Q3 predictNextLetter(\"lanx\") = " + dictionaryCompressedTrie.predictNextLetter("lanx"));

//        dictionaryCompressedTrie.topKFrequentWordsWithPrefix("appl", 3);

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

    public static void updateCompressTrieNodeImportance(CompressedTrieWithRobinHoodHash dictionaryCompressedTrie, ArrayList<String> wordsList) {
        for (String word: wordsList) {
            dictionaryCompressedTrie.search(word);
        }
    }
}
