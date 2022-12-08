package com.example.wordle_helper.Models;


/**
 * A replacement for the {@link java.util.function.Predicate Predicate interface} which cannot be
 * used here becuase it is not supported by all android API's
 */
@FunctionalInterface
public interface Predicate {

    boolean test(String s);
}
