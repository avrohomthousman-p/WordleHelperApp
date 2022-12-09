package com.example.wordle_helper.Activities;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.wordle_helper.R;
import com.example.wordle_helper.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.logging.Logger;


/**
 * An implementation of OnClickListener designed for the fab in the main activity
 */
public class FabClickListener implements View.OnClickListener{
    private final MainActivity context;


    public FabClickListener(MainActivity context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if(makeErrorSnackbar(v)){
            return;
        }


        //update model and switch activities
        sendRequestsToModel();
        Intent intent = new Intent(context, WordDisplayActivity.class);
        context.startActivity(intent);
    }


    /**
     * Checks if there are any errors in the user input, and outputs a
     * snackbar message if there is one. If there are no errors, this method
     * just returns false and no snackbar is made.
     *
     * @return true if there was an error, and false otherwise
     */
    private boolean makeErrorSnackbar(View v){
        Character problem = compareContainAndNotContainForOverlap();
        if(problem != null){
            buildMultiLineSnackbar(v, String.format(
                    "Error: the letter %s was entered as \"Must Contain\" and \"Must Not Contain\"",
                    problem));

            return true;
        }

        problem = compareContainsInSpinnerToNotContainsForOverlap();
        if(problem != null){
            buildMultiLineSnackbar(v, String.format(
                    "Error: the letter %s was entered in the \"Must Not Contain\"" +
                            " section, and also appears in one of the letter slots " +
                            "as \"Can Contain\"",
                    problem));

            return true;
        }

        return false;
    }


    /**
     * Checks the letters the user entered in the "Must Contain" and
     * "Must Not Contain" fields to see if any letters exist in both.
     * Then the method returns the first letter it found that is
     * common to both fields. This means the user entered bad data.
     * If no such letter was found, the user entries are correct and
     * null is returned.
     *
     * @return the first letter common to both the contained and not
     * contained letters, or null if there are no common letters.
     */
    private Character compareContainAndNotContainForOverlap(){
        String contained = context.binding.contentMain.lettersContainedSection.lettersContained
                .getText().toString();


        String notContained = context.binding.contentMain.lettersNotContainedSection.lettersNotContained
                .getText().toString();


        return getOverlappingCharacter(contained, notContained);
    }



    /**
     * Checks that the letters the user entered in the "Must Not Contain" field
     * do not also appear in any spinner set to "Can Contain". If any letter
     * appears in both those fields, the user entered bad data, and this method
     * returns the character that was found in both fields. If no such letter
     * was found, the user entries are correct and null is returned.
     *
     * @return the first letter common to both the "Must Not Contain" field and
     * any spinner set to "Can Contain", or null if no such letter exists.
     */
    private Character compareContainsInSpinnerToNotContainsForOverlap(){
        final String lettersMayNotContain = context.binding.contentMain.lettersNotContainedSection
                .lettersNotContained.getText().toString();


        for(int i = 0; i < context.spinners.length; i++){
            Spinner current = context.spinners[i];

            if(current.getSelectedItem().equals(context.getResources().getStringArray(R.array.contains_array)[0])){
                String lettersEntered = context.letterEntries[i].getText().toString();

                //check if it has a character that is also in our "Must Not Contain" field
                Character overlap = getOverlappingCharacter(lettersEntered, lettersMayNotContain);
                if(overlap != null){
                    return overlap;
                }
            }
        }

        return null;
    }


    /**
     * Finds the first letter that is common to two different strings and returns it.
     * If there are no characters common to both strings, null is returned.
     *
     * @param word1 the first string to search
     * @param word2 the second string to search
     * @return the first letter found to exist in both strings, or null if there are
     *          no common letters.
     */
    private Character getOverlappingCharacter(String word1, String word2){
        for(int i = 0; i < word1.length(); i++){
            if( word2.indexOf(  word1.charAt(i)  ) != -1){      //if the character is found in both words
                return word1.charAt(i);                         //return that character
            }
        }

        return null;
    }


    /**
     * creates and displays a snackbar with the specified message
     *
     * @param v the view to put the snackbar on
     * @param message the message the snackbar should display
     */
    private void buildMultiLineSnackbar(View v, String message){
        Log.println(Log.INFO, "length", String.valueOf(R.integer.max_snackbar_lines));
        Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setTextMaxLines(context.getResources().getInteger(R.integer.max_snackbar_lines))
                .show();
    }


    /**
     * Makes all the required function calls to the model based on the letters the user
     * entered.
     */
    private void sendRequestsToModel(){
        //check letters the word must contain
        MainActivity.mModel.retainIfContains(context.binding.contentMain
                .lettersContainedSection.lettersContained.getText().toString());


        //check letters the word must not contain
        MainActivity.mModel.removeIfContains(context.binding.contentMain
                .lettersNotContainedSection.lettersNotContained.getText().toString());




        String regex = generateRegexFromSpinnerData();

        //Only iterate over the word list if the user actually entered letters for specific indexes
        if(!regex.equals("[a-z][a-z][a-z][a-z][a-z]")){
            //check the letters that can/can't be used in specific indexes
            MainActivity.mModel.retainIfMatches(regex);
        }
    }


    /**
     * Collects all the data the user entered for which characters are and are not allowed for
     * which indexes of the solution word, and generates a regex to check for that pattern.
     *
     * @return a regex that checks that a word uses only the desired characters in the right place.
     */
    private String generateRegexFromSpinnerData() {
        int[] spinnerSelections = context.collectSpinnerSettings();
        String[] letterEntries = context.collectLetterEntries();

        StringBuilder regex = new StringBuilder();
        for(int i = 0; i < spinnerSelections.length; i++){
            regex.append('[');

            String userEntry = letterEntries[i];

            if(userEntry.length() == 0){
                regex.append("a-z");
            }
            else{
                if(spinnerSelections[i] == 1){
                    regex.append('^');
                }

                regex.append(userEntry);
            }

            regex.append(']');
        }

        return regex.toString();
    }
}
