package com.example.wordle_helper.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


        setupTextDisplays();



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Sets up the textViews by giving them the appropriate text (which is based on data
     * from the model).
     */
    private void setupTextDisplays() {
        //set up main display
        Thread workerThread = new SetupWordDisplayThread(this);
        workerThread.start();


        //set up header
        TextView header = findViewById(R.id.word_display_header);
        header.setText(String.format(HEADER_TEMPLATE, MainActivity.mModel.getNumWords()));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}