package com.example.wordle_helper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wordle_helper.Models.WordFilter;
import com.example.wordle_helper.Models.WordleHelper;
import com.example.wordle_helper.R;
import com.example.wordle_helper.Utils.DisplayUtils;
import com.example.wordle_helper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String APP_DESCRIPTION = "This app gives you words to help you solve a game " +
            "of Wordle. You can enter the letters on the home screen as prompted, and then click the" +
            " get words button. You will then be shown a list of words that match your requirements.";

    //To make this accessible in the word_display activity, this needs to be static
    static WordleHelper mModel = null;

    final Spinner[] mSpinners = new Spinner[5];
    final EditText[] mLetterEntries = new EditText[5];

    ActivityMainBinding binding;

    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> restoreSettingsFromPreferences());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarFile.toolbar);


        binding.fab.fab.setOnClickListener(new FabClickListener(this));

        setupSpinners();

        if (mModel == null) {
            mModel = new WordFilter(
                    this.getResources().getStringArray(R.array.full_word_list),
                    this.getApplicationContext().getFilesDir().getAbsolutePath());
        }
    }


    private void setupSpinners(){
        this.mSpinners[0] = binding.contentMain.letterSection1.spinner;
        this.mSpinners[1] = binding.contentMain.letterSection2.spinner;
        this.mSpinners[2] = binding.contentMain.letterSection3.spinner;
        this.mSpinners[3] = binding.contentMain.letterSection4.spinner;
        this.mSpinners[4] = binding.contentMain.letterSection5.spinner;

        for(Spinner sp : this.mSpinners){
            sp.setAdapter(createSpinnerAdapter());
        }

        this.mLetterEntries[0] = binding.contentMain.letterSection1.letterEntry;
        this.mLetterEntries[1] = binding.contentMain.letterSection2.letterEntry;
        this.mLetterEntries[2] = binding.contentMain.letterSection3.letterEntry;
        this.mLetterEntries[3] = binding.contentMain.letterSection4.letterEntry;
        this.mLetterEntries[4] = binding.contentMain.letterSection5.letterEntry;
    }


    private ArrayAdapter<CharSequence> createSpinnerAdapter(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contains_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_hide_keyboard){
            hideKeyboardIfPresent();
            return true;
        }
        else if (id == R.id.action_new_game) {
            startNewGame();
            return true;
        }
        else if(id == R.id.action_settings){
            displaySettings();
            return true;
        }
        else if(id == R.id.action_about){
            displayAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Hides the keyboard if it is on the screen, and does nothing otherwise.
     *
     * I found this code on {@link <a href="https://stackoverflow.com/questions/13593069/
     * androidhide-keyboard-after-button-click#:~:text=You%20could%20instead,
     * placed%20on%20it.">StackOverFlow</a>}
     */
    private void hideKeyboardIfPresent() {
        try{
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.contentMain.contentMain.getWindowToken(), 0);
        }
        catch(Exception e){
            Log.println(Log.WARN, "Keyboard", "hide keyboard was clicked but keyboard could  not be hidden");
        }
    }


    /**
     * Starts a new game by clearing all the text entry fields and resetting the model.
     */
    private void startNewGame(){
        binding.contentMain.lettersContainedSection.lettersContained.setText("");
        binding.contentMain.lettersNotContainedSection.lettersNotContained.setText("");

        for(EditText et : this.mLetterEntries){
            et.setText("");
        }

        mModel.resetWordList();
    }


    /**
     * Opens the Settings Activity.
     */
    private void displaySettings(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsLauncher.launch(intent);
    }


    /**
     * Uses the DialogUtils class to display a message about the app.
     */
    private void displayAbout(){
        DisplayUtils.showInfoDialog(this, "About", APP_DESCRIPTION);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("letter_entry_data", collectLetterEntries());
        outState.putIntArray("spinner_settings", collectSpinnerSettings());
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setLetterEntries(savedInstanceState.getStringArray("letter_entry_data"));
        setSpinnerSelections(savedInstanceState.getIntArray("spinner_settings"));
    }


    /**
     * Sets the text of each EditText associated with a spinner to the text specified
     * in the array. This should be used when loading the state of the app as would be
     * necessary with calls to onStart or when the orientation changes.
     *
     * @param data the text to be placed in all the EditText fields
     */
    private void setLetterEntries(String[] data){
        for(int i = 0; i < mLetterEntries.length; i++){
            mLetterEntries[i].setText(data[i]);
        }
    }


    /**
     * Collects the letters the user entered into each of the letter entry sections that
     * is associated with a spinner, and saves it to a string array. This array can be used
     * when we are saving the state of the app for methods like onStop or for changing the
     * orientation.
     *
     * @return an array of the letters the user entered.
     */
    String[] collectLetterEntries(){
        String[] data = new String[mLetterEntries.length];
        for(int i = 0; i < mLetterEntries.length; i++){
            data[i] = mLetterEntries[i].getText().toString();
        }

        return data;
    }



    /**
     * Sets the selected item of each spinner to the item with the ID specified in the array.
     * This should be used when loading the state of the app as would be necessary with calls
     * to onStart or when the orientation changes.
     *
     * @param selections the selections the spinners need to be set to.
     */
    private void setSpinnerSelections(int[] selections){
        for(int i = 0; i < mSpinners.length; i++){
            mSpinners[i].setSelection(selections[i]);
        }
    }

    /**
     * Collects the selected option of all the spinners and saves it to an array. This data can
     * be used to save the state of the of the app for methods like onStop or for changing the
     * orientation.
     *
     * @return an array of ID's representing what item each spinner has selected
     */
    int[] collectSpinnerSettings(){
        int[] data = new int[mSpinners.length];
        for(int i = 0; i < mSpinners.length; i++){
            data[i] = mSpinners[i].getSelectedItemPosition();
        }

        return data;
    }


    //TODO: rename function and give it javadoc
    private void restoreSettingsFromPreferences(){
        //TODO
    }
}
