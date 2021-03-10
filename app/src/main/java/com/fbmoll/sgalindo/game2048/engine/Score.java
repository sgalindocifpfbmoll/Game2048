package com.fbmoll.sgalindo.game2048.engine;

public class Score {
    // CONSTANTS
    private final int INITIAL_SCORE = 0;

    // Variables
    private int id;
    private int score;
    private String userName;
    private static int bestScore;

    public Score(){
        this.score = 0;
    }

    public Score(int id, String userName, int score){
        this.id = id;
        this.userName = userName;
        this.score = score;
    }

    //<editor-fold desc="GETTERS">
    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getUserName() {
        return userName;
    }
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setScore(int score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    //</editor-fold>

    public void sumScore(int sumValue){
        this.score += sumValue;
    }

    public void restScore(int restValue){
        if (score >= restValue){
            this.score -= restValue;
        }
    }

}
