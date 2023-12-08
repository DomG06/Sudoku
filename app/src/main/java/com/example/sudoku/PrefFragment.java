package com.example.sudoku;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class PrefFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}