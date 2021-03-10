package com.fbmoll.sgalindo.game2048.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fbmoll.sgalindo.game2048.R;

public class SplashActivity extends AppCompatActivity {
    // CONSTANTS
    private final int ANIMATION_APPEARING_TIME_MS = 1000;

    // Variables
    private TextView mainLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Get the elements
        setElements();
        // Play the animations
        playFadeInAnimation();
    }

    public void playFadeInAnimation(){
        YoYo.with(Techniques.FadeIn).duration(ANIMATION_APPEARING_TIME_MS).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                playShakeAnimation();
            }
        }).playOn(mainLogo);
    }

    public void playShakeAnimation(){
//        YoYo.with(Techniques.SlideOutRight).duration(200).repeat(3).onEnd(new YoYo.AnimatorCallback() {
        YoYo.with(Techniques.SlideOutRight).duration(ANIMATION_APPEARING_TIME_MS).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                // Change to main activity
                changeToMainActivity();
            }
        }).playOn(mainLogo);
    }

    private void changeToMainActivity(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void setElements(){
        mainLogo = findViewById(R.id.splashActivityMainLogo);
    }
}