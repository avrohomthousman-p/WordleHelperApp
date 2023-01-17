package com.example.wordle_helper.Activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
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
    //Keys for shared preferences
    private static final String CONTAINS_KEY = "must_contain", NOT_CONTAINS_KEY = "must_not_contain";
    private static final String[] SPINNER_KEYS = {"spinner1", "spinner2", "spinner3", "spinner4", "spinner5"};
    private static final String[] LETTER_ENTRY_KEYS = {"first_character", "second_character",
            "third_character", "fourth_character", "fifth_character"};


    //Text for "About" screen
    public static final String APP_DESCRIPTION = "This app gives you words to help you solve a game " +
            "of Wordle. You can enter the letters on the home screen as prompted, and then click the" +
            " get words button. You will then be shown a list of words that match your requirements.";


    //To make this accessible in the word_display activity, this needs to be static
    static WordleHelper mModel = null;

    //UI objects
    final Spinner[] mSpinners = new Spinner[5];
    final EditText[] mLetterEntries = new EditText[5];
    EditText lettersContained = null;
    EditText lettersNotContained = null;


    ActivityMainBinding binding;

    boolean mAutoSave;

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
        lettersContained = binding.contentMain.lettersContainedSection.lettersContained;
        lettersNotContained = binding.contentMain.lettersNotContainedSection.lettersNotContained;


        if (mModel == null) {
            mModel = new WordFilter(
                    this.getResources().getStringArray(R.array.full_word_list),
                    this.getApplicationContext().getFilesDir().getAbsolutePath());
        }
    }


    /**
     * Sets up the local variables that reference each spinner and editText, and gives
     * each spinner an adapter. This method does not set the local variables for the
     * top two EditText's (the ones for what letters are and are not contained).
     */
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


    /**
     * Creates an Adapter object that contains the options that need to be displayed in
     * a spinner.
     *
     * @return an adapter filled with the appropriate spinner options.
     */
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
     */
    private void hideKeyboardIfPresent() {
        try{
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.contentMain.contentMain.getWindowToken(), 0);
        }
        catch(Exception e){
            Log.println(Log.WARN, "Keyboard", "hide keyboard was clicked but keyboard could not be hidden");
        }
    }


    /**
     * Starts a new game by clearing all the text entry fields and resetting the model.
     */
    private void startNewGame(){
        lettersContained.setText("");
        lettersNotContained.setText("");

        for(EditText et : this.mLetterEntries){
            et.setText("");
        }

        for(Spinner sp : this.mSpinners){
            sp.setSelection(0);
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


    @Override
    protected void onStart(){
        super.onStart();
        restoreSettingsFromPreferences();
        loadGameIfNeeded();
    }


    /**
     * Loads the game when the app starts up.
     */
    protected void loadGameIfNeeded(){
        if(mAutoSave){
            SharedPreferences preferences = getDefaultSharedPreferences(this);

            lettersContained.setText(    preferences.getString(CONTAINS_KEY, "")    );

            lettersNotContained.setText(    preferences.getString(NOT_CONTAINS_KEY, "")    );


            for(int i = 0; i < mLetterEntries.length; i++){
                mLetterEntries[i].setText(    preferences.getString(LETTER_ENTRY_KEYS[i], "")    );
                mSpinners[i].setSelection(    preferences.getInt(SPINNER_KEYS[i], 0)    );
            }
        }
    }


    @Override
    protected void onStop(){
        super.onStop();
        saveSettings();
    }


    /**
     * Tells the model to save this game, if we are set to save the current game for next time
     * the app starts up. Also manages other settings.
     *
     * If new settings are added to this app, they will need to be handled here.
     */
    protected void saveSettings() {
        saveGameIfNeeded();

        /*   Manage any new settings here   */

    }


    /**
     * Saves the user entries of the current game so they can be used when the user
     * next logs in.
     */
    protected void saveGameIfNeeded(){
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        if(mAutoSave){
            int[] spinnerSettings = collectSpinnerSettings();
            String[] letterEntries = collectLetterEntries();

            editor.putString(CONTAINS_KEY, this.lettersContained.getText().toString());

            editor.putString(NOT_CONTAINS_KEY, this.lettersNotContained.getText().toString());


            for(int i = 0; i < spinnerSettings.length; i++){
                editor.putInt(SPINNER_KEYS[i], spinnerSettings[i]);
                editor.putString(LETTER_ENTRY_KEYS[i], letterEntries[i]);
            }
        }
        else{
            editor.remove(CONTAINS_KEY);
            editor.remove(NOT_CONTAINS_KEY);

            for(int i = 0; i < SPINNER_KEYS.length; i++){
                editor.remove(SPINNER_KEYS[i]);
                editor.remove(LETTER_ENTRY_KEYS[i]);
            }
        }

        editor.apply();
    }


    /**
     * Updates all settings based on the user selections in the settings activity.
     *
     * Given that the only settings we currently have do not effect gameplay, we don't
     * really need to use this method, but it is here in case more settings are added in
     * the future.
     */
    protected void restoreSettingsFromPreferences(){
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mAutoSave = sp.getBoolean(getString(R.string.auto_save_key), true);

        /*  New Settings should be added here  */
    }
}
