package com.fbmoll.sgalindo.game2048.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.customLayout.ScoreAdapter;
import com.fbmoll.sgalindo.game2048.data.ScoreDataHelper;
import com.fbmoll.sgalindo.game2048.engine.Score;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private TextView mTextView;
    private EditText mEditWordView;
    private ScoreDataHelper mDB;
    private Button button;
    private RecyclerView mRecyclerView;
    private ScoreAdapter mAdapter;
    private List<Score> scoreList = new ArrayList<>();
    private RadioButton equals, greater, less;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditWordView = findViewById(R.id.search_word);
        mDB = new ScoreDataHelper(this);

        // Create recycler view.
        mRecyclerView = findViewById(R.id.scoreRecyclerView);

        equals = findViewById(R.id.radio_equals);
        greater = findViewById(R.id.radio_greater);
        less = findViewById(R.id.radio_less);
    }

    public void showResult(View view){
        scoreList.clear();
        String word = mEditWordView.getText().toString();
        Cursor cursor;
        if (checkNumber(word)){
            int number = Integer.parseInt(word);
            // Search for the word in the database.
            if (greater.isChecked()){
                cursor = mDB.searchGreaterThan(number);
            }else if (less.isChecked()){
                cursor = mDB.searchLessThan(number);
            }else{
                cursor = mDB.search(number);
            }
        }else{
            // Search for the word in the database.
            cursor = mDB.search(word);
        }
        // Only process a non-null cursor with rows.
        if (cursor != null & cursor.getCount() > 0) {
            // You must move the cursor to the first item.
            cursor.moveToFirst();
            int indexName, indexId, indexScore;

            // Iterate over the cursor, while there are entries.
            do {
                // Don't guess at the column index.
                // Get the index for the named column.
                indexId = cursor.getColumnIndex(ScoreDataHelper.COLUMN_ID);
                indexName = cursor.getColumnIndex(ScoreDataHelper.COLUMN_NAME);
                indexScore = cursor.getColumnIndex(ScoreDataHelper.COLUMN_NO);
                Score score = new Score(cursor.getInt(indexId), cursor.getString(indexName), cursor.getInt(indexScore));
                scoreList.add(score);
                System.out.println(score.getUserName());
            } while (cursor.moveToNext()); // Returns true or false
            cursor.close();
        }
        // Create an mAdapter and supply the data to be displayed.
        mAdapter = new ScoreAdapter(this, scoreList);
        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean checkNumber(String number){
        try {
            int intValue = Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
            return false;
        }
    }
}
