package com.example.wordle_helper.Models;
import java.util.Iterator;
import java.util.List;


/**
 * Implementation of the WordleHelper interface
 */
public class WordFilter implements WordleHelper{
    private List<String> list = WordListGenerator.getDictionary();


    @Override
    public void resetWordList(){
        list = WordListGenerator.getDictionary();
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
