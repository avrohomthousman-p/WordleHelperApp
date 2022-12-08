package com.example.wordle_helper.Models;

import com.example.wordle_helper.R;

import java.io.*;
import java.util.*;

/**
 * provides utility methods for getting a list of all 5 letter words
 */
public class WordListGenerator {
    public static final String DICTIONARY_FILE = "allWords.bin";
    public static final String WORDS_FILE = "allWords.txt";


    /**
     * Gets the full list of all possible words with 5 letters.
     * @return the full list of 5 letter words
     */
    public static List<String> getDictionary(){
        File f = new File(DICTIONARY_FILE);
        if(f.exists()){
            return deserializeDict();
        }
        else{
            return createDict();
        }
    }


    /**
     * Reads words from a plain text file and puts each word in a list.
     *
     * @return a list of all the words that were read from the file.
     */
    private static List<String> createDict(){
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(WORDS_FILE));
            List<String> dict = new LinkedList<>();
            while (fileInput.hasNext()){
                dict.add(fileInput.next());
            }
            fileInput.close();

            serializeDict(dict);
            return dict;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Deserializes a list of words that has be serialized to a file.
     *
     * @return the list of words desierialized from the file.
     */
    private static List<String> deserializeDict(){
        List<String> dict = null;
        try{
            FileInputStream fileIn = new FileInputStream(DICTIONARY_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            dict = (List<String>) in.readObject();
            in.close();
            fileIn.close();

        }
        catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return dict;
    }


    private static void serializeDict(List<String> dict){
        try {
            FileOutputStream fileOut = new FileOutputStream(DICTIONARY_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dict);
            out.close();
            fileOut.close();

        } catch (IOException i) {
            i.printStackTrace();
        }

    }
}
