package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.os.Bundle;

public class PrefActivity extends AppCompatActivity {

    private static final String OPT_MUSIC = "music";
    private static final boolean OPT_MUSIC_DEF = true;
    private static final String OPT_HINTS = "hints";
    private static final boolean OPT_HINTS_DEF = true;
    private static final String OPT_MUTABLE = "mutable";
    private static final boolean OPT_MUTABLE_DEF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);

        getSupportActionBar().setTitle("settings");

        if(findViewById(R.id.settings_container) != null){
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.settings_container, new PrefFragment()).commit();
        }
    }
    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
    }

    public static boolean getHints(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS, OPT_HINTS_DEF);
    }

    public static boolean getMutable(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUTABLE, OPT_MUTABLE_DEF);
    }
}