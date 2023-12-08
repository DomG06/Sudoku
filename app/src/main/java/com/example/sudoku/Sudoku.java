package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Sudoku extends AppCompatActivity {

    public static final String TAG = "CPTR320";
    public static final String EXTRA_MESSAGE = "cptr320.myapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
    }

    public void onClick(View view) {
        Log.i(TAG, "Button clicked ");
        int vid = view.getId();
        if (vid == R.id.aboutbutton) {
           Intent intent = new Intent(this, About.class);
            startActivity(intent);
        } else if (vid == R.id.newgamebutton) {
            openNewGameDialog();
        } else if (vid == R.id.continuebutton) {
            startGame(Game.DIFFICULTY_CONTINUE);
        } else if (vid == R.id.exitbutton) {
            finish();
        }

    }

    private void openNewGameDialog() {
        new AlertDialog.Builder(this).setTitle("Difficulty").setItems(R.array.difficulty,
                (dialogInterface, i) -> startGame(i)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.settings){
            Intent i = new Intent(this, PrefActivity.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    public void startGame(int i){
        String[] array = getResources().getStringArray(R.array.difficulty);
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (PrefActivity.getMusic(this))
            Music.play(this, R.raw.main);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Music.stop(this);
    }


}