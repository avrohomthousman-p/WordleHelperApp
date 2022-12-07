package com.example.wordle_helper.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.wordle_helper.R;
import com.example.wordle_helper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
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

}