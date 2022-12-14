package com.example.wordle_helper.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.wordle_helper.R;
import com.example.wordle_helper.Utils.DisplayUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set the Dark mode from the settings
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        DisplayUtils.setNightModeOnOffFromPreferenceValue(
                getApplicationContext(), getString(R.string.dark_mode_key));



        startActivity (new Intent(getApplicationContext (),
                MainActivity.class));

        finish ();
    }
}