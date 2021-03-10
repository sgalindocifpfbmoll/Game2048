package com.fbmoll.sgalindo.game2048.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.customLayout.ScoreAdapter;
import com.fbmoll.sgalindo.game2048.data.ScoreDataHelper;
import com.fbmoll.sgalindo.game2048.engine.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ScoreAdapter mAdapter;
    private ScoreDataHelper mDB;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //creating the database helper
        mDB = new ScoreDataHelper(this);
        // Create recycler view.
        mRecyclerView = findViewById(R.id.scoreRecyclerView);
        // Create an mAdapter and supply the data to be displayed.
        mAdapter = new ScoreAdapter(this, this.mDB);
        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void changeToMainActivity(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Add code to update the database.
        if(requestCode== 1){
            if (resultCode == RESULT_OK){
                String newName = data.getStringExtra("replyName");
                int newScore = data.getIntExtra("replyScore", 0);
                if (!TextUtils.isEmpty(newName)){
                    int id = data.getIntExtra("newId",-99);
                    mDB.update(id, newName, newScore);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        changeToMainActivity();
    }
}