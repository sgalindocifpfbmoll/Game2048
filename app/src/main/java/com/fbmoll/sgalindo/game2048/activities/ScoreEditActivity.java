package com.fbmoll.sgalindo.game2048.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.customLayout.ScoreAdapter;
import com.fbmoll.sgalindo.game2048.data.ScoreDataHelper;

public class ScoreEditActivity extends AppCompatActivity {
    public String NAME, SCORE;
    private EditText editName, editScore;
    private Button submitButton, cancelButton;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_edit);
        setContent();

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();
        NAME = (String) extras.get("name");
        SCORE = (String) extras.get("score");
        id = (int) extras.get("id");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLastActivity();
            }
        });
        editName.setHint(NAME);
        editScore.setHint(SCORE);
    }

    private void setContent(){
        submitButton = findViewById(R.id.scoreEditActivityButtonSubmit);
        cancelButton = findViewById(R.id.scoreEditActivityButtonCancel);
        editName = findViewById(R.id.scoreEditName);
        editScore = findViewById(R.id.scoreEditScore);
    }

    private void submitChanges(){
        // Get the values
        String newName = editName.getText().toString();
        int newScore = Integer.parseInt(editScore.getText().toString());
        Intent replyIntent = new Intent();
        replyIntent.putExtra("replyName", newName);
        replyIntent.putExtra("replyScore", newScore);
        replyIntent.putExtra("newId", id);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void returnToLastActivity(){
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}