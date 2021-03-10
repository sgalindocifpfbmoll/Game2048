package com.fbmoll.sgalindo.game2048.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fbmoll.sgalindo.game2048.R;

public class MainActivity extends AppCompatActivity {
    // CONSTANTS
    private final int ANIMATION_APPEARING_TIME_MS = 500;
    private final int ANIMATION_BUTTON_APPEARING_TIME_MS = 400;

    //Variables
    private TextView rateApp;
    private RatingBar rateBar;
    private Button playButton, scoreButton;
    private RelativeLayout mainLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setElements();
        playInitialAnimation();
        playRateAnimation();
        // Button listener
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayGameDialog();
            }
        });
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToScoresActivity();
            }
        });
        rateBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showRateBar();
                }
                return true;
            }
        });
    }

    private void playInitialAnimation(){
        YoYo.with(Techniques.FadeIn).duration(ANIMATION_APPEARING_TIME_MS).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                playButtonPlayAnimation();
                playButtonScoreAnimation();
            }
        }).playOn(mainLayout);
    }

    private void playButtonPlayAnimation(){
        playButton.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp).duration(ANIMATION_BUTTON_APPEARING_TIME_MS).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {

            }
        }).playOn(playButton);
    }

    private void playButtonScoreAnimation(){
        scoreButton.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp).duration(ANIMATION_BUTTON_APPEARING_TIME_MS).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {

            }
        }).playOn(scoreButton);
    }

    private void playRateAnimation(){
        YoYo.with(Techniques.Pulse).duration(ANIMATION_BUTTON_APPEARING_TIME_MS).repeat(500).playOn(rateApp);
    }

    private void changeToGameActivity(){
        Intent mainActivity = new Intent(this, GameActivity.class);
        startActivity(mainActivity);
    }

    private void changeToScoresActivity(){
        Intent scoresActivity = new Intent(this, ScoresActivity.class);
        startActivity(scoresActivity);
    }

    private void showRateBar(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Vi7ULYt2ha0"));
        startActivity(browserIntent);
    }

    private void showPlayGameDialog(){
        // Create the dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        // Setup the dialog
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Start a new game?");
        alertDialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int id) {
                // Go to game
                changeToGameActivity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alertDialog, int id) {
                // Nothing happens. Go back to the main menu
            }
        });
        alertDialog.show();
    }

    private void setElements(){
        playButton = findViewById(R.id.mainActivityButtonPlay);
        scoreButton = findViewById(R.id.mainActivityButtonScore);
        mainLayout = findViewById(R.id.mainActivityLayout);
        rateApp = findViewById(R.id.rateAppText);
        rateBar = findViewById(R.id.ratingBar);
    }

    @Override
    public void onBackPressed() {
        // Nothing is done.
    }

}