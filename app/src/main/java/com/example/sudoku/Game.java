package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class Game extends AppCompatActivity {

    private static final String TAG = "CPTR320";
    public static final String KEY_DIFFICULTY = "cptr320.difficulty";
    private static final int DIFFICULTY_EASY = 0;
    private static final int DIFFICULTY_MEDIUM = 1;
    private static final int DIFFICULTY_HARD = 2;
    protected static final int DIFFICULTY_CONTINUE = -1;
    private static final String PREF_CURR_DIFF = "current difficulty";
    private static final String PREF_PUZZLE = "puzzle";
    private int puzzle[] = new int[9*9];
    private final int[][][] used = new int[9][9][];
    private PuzzleView puzzleView;

    private final String easyPuzzle = "360000000004230800000004200" + "070460003820000014500013020" + "001900000007048300000000045" ;
    private final String mediumPuzzle = "650000070000506000014000005" + "007009000002314700000700800" + "500000630000201000030000097" ;
    private final String hardPuzzle = "009000000080605020501078000" + "000000700706040102004000000" + "000720903090301080000000600" ;
    private int currentDifficulty = 0;

    private static final String SELX = "selx", SELY = "sely";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        currentDifficulty = diff;
        puzzle = getPuzzle(diff);
        calculateUsedTiles();
        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
    }

    private void calculateUsedTiles(){
        for (int x = 0; x < 9; x++){
            for (int y = 0; y < 9; y++){
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }
    private int[] calculateUsedTiles(int x, int y){
        int c[] = new int[9];
        for (int i = 0; i < 9; i++){
            if (i == y) continue;
            int t = getTile(x, i);
            if (t != 0)
                c[t-1] = t;
        }
        for (int i = 0; i < 9; i++){
            if (i == x) continue;
            int t = getTile(i, y);
            if (t != 0)
                c[t-1] = t;
        }
        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;
        for (int i = startx; i < startx + 3; i++){
            for (int j = starty; j < starty + 3; j++){
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if(t != 0)
                    c[t-1] = t;
            }
        }
        int nused = 0;
        for (int t : c){
            if (t != 0)
                nused++;
        }
        int c1[] = new int[nused];
        nused = 0;
        for (int t : c){
            if (t != 0)
                c1[nused++] = t;
        }
        return c1;
    }
    private int getTile(int x, int y){
        return puzzle[y * 9 + x];
    }

    private int setTile(int x, int y, int value){
        return puzzle[y * 9 + x] = value;
    }
    protected String getTileString(int x, int y){
        int v = getTile(x, y);
        if (v == 0)
            return "";
        else
            return String.valueOf(v);
    }

    private int[] getPuzzle(int diff){
        String puz;
        switch (diff){
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
                currentDifficulty = getPreferences(MODE_PRIVATE).getInt(PREF_CURR_DIFF, 0);
                        //restore from storage
                        break;
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }
        return fromPuzzleString(puz);
    }

    static private String toPuzzleString(int[] puz){
        StringBuilder buf = new StringBuilder();
        for (int element : puz){
            buf.append(element);
        }
        return buf.toString();
    }

    static protected int[] fromPuzzleString(String string){
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++){
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }

    protected void showKeypadOrError(int x, int y){
        if (PrefActivity.getMutable(this) && isMutable(x,y)) {
            puzzleView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast toast = Toast.makeText(this, R.string.not_allowed, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        int tiles[] = getUsedTiles(x, y);
        if (tiles.length == 9){
            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
            Dialog v = new Keypad(this, tiles, puzzleView); v.show();
        }
    }

    private boolean isMutable(int x, int y) {

        return getPuzzle(currentDifficulty)[9*y + x] != 0;
    }

    protected boolean setTileIfValid(int x, int y, int value){
        int tiles[] = getUsedTiles(x,y);
        if (value != 0){
            for (int tile : tiles){
                if (tile == value)
                    return false;
            }
        }
        setTile(x, y, value); calculateUsedTiles(); return true;
    }

    protected int[] getUsedTiles(int x, int y) {

        return used[x][y];
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PrefActivity.getMusic(this))
            Music.play(this, R.raw.game);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Music.stop(this);
        getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE,
                toPuzzleString(puzzle)).commit();
    }

    @Override
    protected void onStop() {
       getPreferences(MODE_PRIVATE).edit().putInt(PREF_CURR_DIFF,
               currentDifficulty).commit();
        //should be saved into preference
        super.onStop();
    }



    public int[] getStartingPuzzle() {
        return getPuzzle(currentDifficulty);
    }
}