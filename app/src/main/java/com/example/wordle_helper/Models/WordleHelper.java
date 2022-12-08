package com.example.wordle_helper.Models;

import java.util.List;


/**
 * The methods of this interface are used to scan a list of 5 letter English words
 * and remove the ones that cannot be the correct answer in a game of Wordle.
 *
 * Once a word is removed by a function call, the word is not put back until the
 * resetWordList method is called. As a result, a call to removeIfContains("b")
 * following a call to removeIfContains("a") will only give words that contain neither
 * 'a' nor 'b'
 */
public interface WordleHelper {

    /**
     * Resets the object to consider all words valid solutions. This
     * effectively undoes all previous calls made to the remove and retain
     * functions below.
     */
    void resetWordList();

    /**
     * Removes, from the list of words, all words that don't start with the specified
     * String.
     *
     * @param start the String that all words must start with (or be removed).
     */
    void retainIfStartsWith(String start);


    /**
     * Removes, from the list of words, all words that don't start with the specified
     * String.
     *
     * @param start the character that all words must start with (or be removed).
     */
    void retainIfStartsWith(char start);


    /**
     * Removes, from the list of words, all words that don't end with the specified String.
     *
     * @param end the String that all words must end with (or be removed).
     */
    void retainIfEndsWith(String end);


    /**
     * Removes, from the list of words, all words that don't end with the specified character.
     *
     * @param end the character that all words must end with (or be removed).
     */
    void retainIfEndsWith(char end);


    /**
     * Removes, from the list of words, all words that don't contain the specified characters.
     * A String is considered to contain the specified characters even if they do not appear
     * in that String in the same order as they appear in the chars parameter specified in the
     * call to this function.
     *
     * @param chars the characters that words need to contain, or be removed from the list.
     */
    void retainIfContains(char[] chars);


    /**
     * Removes, from the list of words, all words that don't contain the specified characters.
     * A String is considered to contain the specified characters even if they do not appear
     * in that String in the same order as they appear in the chars parameter specified in the
     * call to this function.
     *
     * @param chars the characters that words need to contain, or be removed from the list.
     */
    void retainIfContains(String chars);


    /**
     * Removes, from the list of words, all words that contain the specified characters.
     * A String is considered to contain the specified characters even if they do not appear
     * in that String in the same order as they appear in the chars parameter specified in the
     * call to this function.
     *
     * @param chars the characters that words must not contain, or be removed from the list.
     */
    void removeIfContains(char[] chars);


    /**
     * Removes, from the list of words, all words that contain the specified characters.
     * A String is considered to contain the specified characters even if they do not appear
     * in that String in the same order as they appear in the chars parameter specified in the
     * call to this function.
     *
     * @param chars the characters that words must not contain, or be removed from the list.
     */
    void removeIfContains(String chars);


    /**
     * removes all words that do not match the specified pattern.
     *
     * @param regex the pattern that all words must match to still be
     *             considered a possible solution.
     */
    void retainIfMatches(String regex);


    /**
     * Gets a list of all the words that are still possible solutions
     *
     * @return a list of all the words that are still possible solutions
     */
    List<String> getRemainingWords();


    /**
     * Gets the number of words that are still possible solutions.
     *
     * @return the number of words that can still be the solution.
     */
    int getNumWords();
}
