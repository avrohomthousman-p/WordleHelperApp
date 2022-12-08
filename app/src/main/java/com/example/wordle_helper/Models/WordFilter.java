package com.example.wordle_helper.Models;
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
    public static final String DICTIONARY_FILE = "allWords.bin";
    private List<String> list;


    public WordFilter(String[] fullWordList){
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

        SerializationUtils.serializeObject(list, DICTIONARY_FILE);

        this.list = list;
    }


    @Override
    public void resetWordList(){
        //load the full un-modified list from the file
        list = (List<String>) SerializationUtils.deserializeObject(DICTIONARY_FILE);
    }


    @Override
    public void retainIfStartsWith(String start) {
        if(start == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(start.length() == 0)
            return;


        removeIf(current -> !current.startsWith(start));
    }


    @Override
    public void retainIfStartsWith(char start) {
        removeIf(current -> current.charAt(0) != start);
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
    }

    @Override
    public void retainIfEndsWith(char end) {
        removeIf(current -> current.charAt(current.length() - 1) != end);
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
    }

    @Override
    public void retainIfContains(String chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length() == 0)
            return;


        retainIfContains(chars.toCharArray());
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
    }

    @Override
    public void removeIfContains(String chars) {
        if(chars == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(chars.length() == 0)
            return;


        removeIfContains(chars.toCharArray());
    }

    @Override
    public void retainIfMatches(String regex) {
        if(regex == null){
            throw new NullPointerException("Beginning of String cannot be null");
        }
        if(regex.length() == 0)
            return;



        removeIf(s -> !s.matches(regex));
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
