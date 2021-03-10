package com.fbmoll.sgalindo.game2048.engine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Tile {
    // CONSTANTS
    private final int[] POSSIBLE_VALUES = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
    private final String[] HEX_BACKGROUND_COLORS = {"#CCC1B5", "#ECE3DA", "#EDE2CA", "#F0B37F",
            "#F29769", "#F27D63","#F26041","#EBD27A", "#EACE6A", "#EACA5C", "#EAC74E", "#EAC442"};
    private final String[] HEX_TEXT_COLORS = {"#766E66", "#F9F6F2"};

    // Variables
    private int value;
    private Color color;

    // Layout elements (views)
    private TextView valueTextView;
    private View view;

    /**
     * Default constructor. Empty.
     */
    public Tile() {
    }

    public Tile(int value, TextView valueTextView){
        this.value = value;
        this.valueTextView = valueTextView;
        //valueTextView.getLocationOnScreen(valueTextView);
    }

    public Tile (Tile tile){
        this.setValue(tile.getValue());
        this.setColor(tile.getColor());
    }

    //<editor-fold desc="GETTERS">
    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEmpty(){
        return value == 0;
    }
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE METHODS">
    public void updateView(){
        updateColor();  // Set the view color
        updateTextValue();   // Set the text
    }

    private void updateColor(){
        int background = 0, text= 0;
        switch(value){
            case 0:
                background = 0; text = 0;
                break;
            case 2:
                background = 1; text = 0;
                break;
            case 4:
                background = 2; text = 0;
                break;
            case 8:
                background = 3; text = 1;
                break;
            case 16:
                background = 4; text = 1;
                break;
            case 32:
                background = 5; text = 1;
                break;
            case 64:
                background = 6; text = 1;
                break;
            case 128:
                background = 7; text = 1;
                break;
            case 256:
                background = 9; text = 1;
                break;
            case 512:
                background = 10; text = 1;
                break;
            case 1024:
                background = 11; text = 1;
                break;
            case 2048:
                background = 12; text = 1;
                break;
        }
        valueTextView.setBackgroundColor(Color.parseColor((HEX_BACKGROUND_COLORS[background])));
        valueTextView.setTextColor(Color.parseColor(HEX_TEXT_COLORS[text]));
    }

    private void updateTextValue(){
        if (value == 0){
            valueTextView.setText("");
        }else{
            valueTextView.setText(String.valueOf(value));
        }
    }
    //</editor-fold>

    //<editor-fold desc="MOVE METHODS">
    public void moveLeft(){

    }
    //</editor-fold>

    //<editor-fold desc="ANIMATIONS">
    public void playNormalAnimation(){
        YoYo.with(Techniques.Pulse).duration(0).playOn(valueTextView);
    }

    public void cancelAnimation(){
        YoYo.with(Techniques.Pulse).duration(0).playOn(valueTextView);
    }

    public void playAppearingAnimation(){
        YoYo.with(Techniques.BounceIn).duration(700).playOn(valueTextView);
    }

    public void playSumAnimation(){
        YoYo.with(Techniques.Pulse).duration(350).repeat(1).playOn(valueTextView);
    }

    public void playMoveAnimationX(Point pointNextTile){
        int distanceX = pointNextTile.x - getLocationOnScreen().x;
        ObjectAnimator animationX = ObjectAnimator.ofFloat(valueTextView, "translationX", distanceX + valueTextView.getTextSize());
        animationX.setDuration(300);
        animationX.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation){
                cancelAnimation();
            }
            public void onAnimationEnd(Animator animation) {
                cancelAnimation();
            }
        });
        animationX.start();

    }

    public void playMoveAnimationY(Point pointNextTile){
        int distanceY = pointNextTile.y - getLocationOnScreen().y;
        ObjectAnimator animationX = ObjectAnimator.ofFloat(valueTextView, "translationY", distanceY + valueTextView.getTextSize());
        animationX.setDuration(200);
        animationX.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation){
                cancelAnimation();
            }
            public void onAnimationEnd(Animator animation) {
                cancelAnimation();
            }
        });
        animationX.start();
    }

    public void playMoveLeftAnimation(){
        YoYo.with(Techniques.SlideInLeft).duration(700).playOn(valueTextView);
    }

    public void playMoveRightAnimation(){
        YoYo.with(Techniques.SlideInRight).duration(700).playOn(valueTextView);
    }

    public void playMoveUpAnimation(){
        YoYo.with(Techniques.SlideInLeft).duration(700).playOn(valueTextView);
    }

    public void playMoveDownAnimation(){
        YoYo.with(Techniques.SlideInLeft).duration(700).playOn(valueTextView);
    }
    //</editor-fold>

    public Point getLocationOnScreen() {
        int[] location = new int[2];
        valueTextView.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }
}
