package com.example.wordle_helper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wordle_helper.R;
import com.example.wordle_helper.databinding.ActivityMainBinding;


/**
 * An activity for displaying all the words that are still included as potential solutions
 * for the game of Wordle.
 */
public class WordDisplayActivity extends AppCompatActivity {
    private static final String HEADER_TEMPLATE = "There Are %d words left";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_display);

        TextView header = findViewById(R.id.word_display_header);
        header.setText(String.format(HEADER_TEMPLATE, MainActivity.mModel.getNumWords()));
        displayListOfWords();
    }


    /**
     * Posts all the words that are still potential solutions on the text view display.
     */
    private void displayListOfWords() {
        TextView mainSection = findViewById(R.id.all_words);

        StringBuilder text = new StringBuilder();
        for(String current : MainActivity.mModel.getRemainingWords()){
            text.append(current);
            text.append("\t\t");
        }

        mainSection.setText(text.toString());
    }
}