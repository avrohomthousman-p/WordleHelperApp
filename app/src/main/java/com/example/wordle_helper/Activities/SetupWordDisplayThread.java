package com.example.wordle_helper.Activities;

import android.widget.TextView;

/**
 * Generates and inserts the text that should be placed into the main edit text of the
 * Word Display Activity.
 */
public class SetupWordDisplayThread extends Thread {
    private final TextView targetSection;

    public SetupWordDisplayThread(TextView target) {
        this.targetSection = target;
    }

    @Override
    public void run(){
        StringBuilder text = new StringBuilder();
        java.util.List<String> words = MainActivity.mModel.getRemainingWords();
        for(String current : words){
            text.append(current);
            text.append("\t\t");
        }

        targetSection.setText(text.toString());
    }
}
