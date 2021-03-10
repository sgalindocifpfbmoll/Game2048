package com.fbmoll.sgalindo.game2048.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.data.ScoreDataHelper;
import com.fbmoll.sgalindo.game2048.engine.Matrix;
import com.fbmoll.sgalindo.game2048.engine.Score;

public class GameActivity extends AppCompatActivity {
    //CONSTANTS

    // Variables
    private Button buttonMenu, buttonUndo;
    private TextView currentScore, bestScore;
    private Matrix matrixFragment;
    private ScoreDataHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setContent();


        // Button listener
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayGameDialog();
            }
        });

        // Button listener
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matrixFragment.isUndoAvailable()){
                    matrixFragment.undoChanges();
                }
            }
        });
    }

    public void setCurrentScore(int score){
        this.currentScore.setText(String.valueOf(score));
    }

    private void setContent(){
        buttonMenu = findViewById(R.id.gameActivityButtonMenu);
        buttonUndo = findViewById(R.id.gameActivityButtonUndo);
        currentScore = findViewById(R.id.gameActivityActualScore);
        bestScore = findViewById(R.id.gameActivityBestScore);
        matrixFragment = (Matrix) getSupportFragmentManager().findFragmentById(R.id.matrixFragment);

        //creating the database helper
        mDB = new ScoreDataHelper(this);
        // Set the score and best score view
        setCurrentScore(0);
        bestScore.setText(String.valueOf(mDB.getBestScore().getScore()));
    }

    private void changeToMainActivity(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void changeToScoresActivity(){
        Intent scoresActivity = new Intent(this, ScoresActivity.class);
        startActivity(scoresActivity);
    }

    private void showPlayGameDialog(){
        // Create the dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        // Setup the dialog
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Return to home menu?");
        alertDialog.setMessage("The progress on this game will be lost.");
        alertDialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int id) {
                // Go to game
                changeToMainActivity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int id) {
                // Nothing happens. Go back to the main menu
            }
        });
        alertDialog.show();
    }

    public void endGame(){
        final EditText userInput = new EditText(this);
        // Create the dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        // Setup the dialog
        alertDialog.setCancelable(false);
        alertDialog.setTitle("GAME OVER");
        alertDialog.setMessage("Insert your name to save the score:");
        alertDialog.setView(userInput);
        alertDialog.setPositiveButton("Submit game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int id) {
                // Save the score into the ScoreDatabase
                Score finalScore = new Score((int)mDB.count(), userInput.getText().toString(), matrixFragment.getScore().getScore());
                mDB.insert(finalScore);
                changeToScoresActivity();
            }
        });
        alertDialog.show();
    }

    public void setUndo(boolean available){
        if (available){
            buttonUndo.setBackgroundColor(Color.parseColor("#F0B37F"));
        }else{
            buttonUndo.setBackgroundColor(Color.parseColor("#857777"));
        }
    }

    @Override
    public void onBackPressed() {
        showPlayGameDialog();
    }
}