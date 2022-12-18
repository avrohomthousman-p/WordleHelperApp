package com.example.wordle_helper.Models;
import android.util.Log;

import com.example.wordle_helper.Utils.SerializationUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Implementation of the WordleHelper interface.
 *
 * This implementation requires an array of words to use as a list of all
 * possible solutions for a game of Wordle.
 */
public class WordFilter implements WordleHelper{
    public final String dictionary_file;
    private List<String> list;
    private boolean listWasModified = false;


    public WordFilter(String[] fullWordList, String filesDirectory){
        dictionary_file = filesDirectory + "allWords.bin";

        Log.println(Log.INFO, "word list filepath", filesDirectory);

        initailizeNewDictionary(fullWordList);
    }


    /**
     * Generates a new List of words based on the contents of the specified word array.
     * The list is serialized to a file for future re-use.
     *
     * @param fullWordList all the words to be placed in the new list of words
     */
    private void initailizeNewDictionary(String[] fullWordList){
        List<String> list = new LinkedList<>();
        for(String current : fullWordList){
            list.add(current);
        }

        SerializationUtils.serializeObject(list, dictionary_file);

        this.list = list;
    }


    @Override
    public void resetWordList(){
        //only build a new list if the old list was changed
        if(listWasModified){

            //load the full un-modified list from the file
            list = (List<String>) SerializationUtils.deserializeObject(dictionary_file);
            listWasModified = false;
        }
    }


    @Override
    public void retainIfStartsWith(String start) {
        if(start == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(start.length() == 0)
            return;


        removeIf(current -> !current.startsWith(start));
        listWasModified = true;
    }


    @Override
    public void retainIfStartsWith(char start) {
        removeIf(current -> current.charAt(0) != start);
        listWasModified = true;
    }


    @Override
    public void retainIfEndsWith(String end) {
        if(end == null){
            throw new NullPointerException("End of String cannot be null");
        }
        if(end.length() == 0){
            return;
        }


        removeIf(current -> !current.endsWith(end));
        listWasModified = true;
    }

    @Override
    public void retainIfEndsWith(char end) {
        removeIf(current -> current.charAt(current.length() - 1) != end);
        listWasModified = true;
    }

    @Override
    public void retainIfContains(char[] chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length == 0)
            return;



        removeIf(current -> {
            for(char c : chars){
                if(current.indexOf(c) == -1){
                    return true;
                }
            }
            return false;
        });

        listWasModified = true;
    }

    @Override
    public void retainIfContains(String chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length() == 0)
            return;


        retainIfContains(chars.toCharArray());
        listWasModified = true;
    }

    @Override
    public void removeIfContains(char[] chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length == 0)
            return;



        removeIf(current -> {
            for(char c : chars){
                if(current.indexOf(c) != -1){
                    return true;
                }
            }
            return false;
        });

        listWasModified = true;
    }

    @Override
    public void removeIfContains(String chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length() == 0)
            return;


        removeIfContains(chars.toCharArray());
        listWasModified = true;
    }

    @Override
    public void retainIfMatches(String regex) {
        if(regex == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(regex.length() == 0)
            return;



        removeIf(s -> !s.matches(regex));
        listWasModified = true;
    }

    @Override
    public List<String> getRemainingWords() {
        return new java.util.LinkedList<String>(list);
    }


    @Override
    public int getNumWords(){
        return list.size();
    }

    /**
     * A replacement for the {@link java.util.Collection#removeIf Collection.removeIf} function,
     * which cannot be used here as it is not supported by all android API's
     *
     * @param condition a function that tells us whether or not to remove a String from the list.
     */
    private void removeIf(Predicate condition){
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if(condition.test(it.next())){
                it.remove();
            }
        }
    }
}
