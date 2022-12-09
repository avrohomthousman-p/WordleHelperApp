package com.example.wordle_helper.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.wordle_helper.Models.WordFilter;
import com.example.wordle_helper.Models.WordleHelper;
import com.example.wordle_helper.R;
import com.example.wordle_helper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //To make this accessible in the word_display activity, this needs to be static
    static WordleHelper mModel = null;

    final Spinner[] spinners = new Spinner[5];
    final EditText[] letterEntries = new EditText[5];

    ActivityMainBinding binding;

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarFile.toolbar);


        binding.fab.fab.setOnClickListener(new FabClickListener(this));

        setupSpinners();

        if (mModel == null) {
            mModel = new WordFilter(  this.getResources().getStringArray(R.array.full_word_list)  );
        }
    }


    private void setupSpinners(){
        this.spinners[0] = binding.contentMain.letterSection1.spinner;
        this.spinners[1] = binding.contentMain.letterSection2.spinner;
        this.spinners[2] = binding.contentMain.letterSection3.spinner;
        this.spinners[3] = binding.contentMain.letterSection4.spinner;
        this.spinners[4] = binding.contentMain.letterSection5.spinner;

        for(Spinner sp : this.spinners){
            sp.setAdapter(createSpinnerAdapter());
        }

        this.letterEntries[0] = binding.contentMain.letterSection1.letterEntry;
        this.letterEntries[1] = binding.contentMain.letterSection2.letterEntry;
        this.letterEntries[2] = binding.contentMain.letterSection3.letterEntry;
        this.letterEntries[3] = binding.contentMain.letterSection4.letterEntry;
        this.letterEntries[4] = binding.contentMain.letterSection5.letterEntry;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        for(int i = 0; i < letterEntries.length; i++){
            letterEntries[i].setText(data[i]);
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
        String[] data = new String[letterEntries.length];
        for(int i = 0; i < letterEntries.length; i++){
            data[i] = letterEntries[i].getText().toString();
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
        for(int i = 0; i < spinners.length; i++){
            spinners[i].setSelection(selections[i]);
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
        int[] data = new int[spinners.length];
        for(int i = 0; i < spinners.length; i++){
            data[i] = spinners[i].getSelectedItemPosition();
        }

        return data;
    }
}
