package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String message = getString(R.string.about_info);
        LinearLayout layout = new LinearLayout(this);
        TextView textView = new TextView(this);
        textView.setText(message);
        layout.addView(textView);
        setContentView(layout);
        textView.setText(message);
    }
}