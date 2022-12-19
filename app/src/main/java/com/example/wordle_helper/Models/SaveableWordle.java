package com.example.wordle_helper.Models;

/**
 * A version of the wordle helper that supports saving and loading a game.
 *
 * Note: this interface is NOT designed to support saving more than one game at a time.
 */
public interface SaveableWordle extends WordleHelper {

    /**
     * Saves the current game in a way that it can be continued later,
     * even if the device restarts.
     */
    void saveGame();

    /**
     * Loads the game last saved, if there is one, and if there isn't, starts a new game.
     *
     * @return true if the saved game was loaded successfully, and false otherwise.
     */
    boolean loadGame();
}
