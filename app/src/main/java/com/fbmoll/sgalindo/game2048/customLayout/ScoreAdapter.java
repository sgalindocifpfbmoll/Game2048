package com.fbmoll.sgalindo.game2048.customLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.activities.MainActivity;
import com.fbmoll.sgalindo.game2048.activities.ScoreEditActivity;
import com.fbmoll.sgalindo.game2048.data.ScoreDataHelper;
import com.fbmoll.sgalindo.game2048.engine.Score;

import java.util.List;

/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private ScoreDataHelper mDB;
    private List<Score> scoreList;

    /**
     *  Custom view holder with a text view and two buttons.
     */
    class ScoreViewHolder extends RecyclerView.ViewHolder {
        public final TextView userName;
        public final TextView userScore;
        Button deleteButton;
        Button editButton;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.scoreListItemUserName);
            userScore = itemView.findViewById(R.id.scoreListItemUserScore);
            deleteButton = itemView.findViewById(R.id.scoreListItemButtonDelete);
            editButton = itemView.findViewById(R.id.scoreListItemButtonEdit);
        }
    }

    private final LayoutInflater mInflater;
    Context mContext;

    public ScoreAdapter(Context context, ScoreDataHelper mDB) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mDB=mDB;
    }

    public ScoreAdapter(Context context, List<Score> scoreList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.scoreList = scoreList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.scorelist_item, parent, false);
        return new ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScoreViewHolder holder, final int position) {
        final Score current;
        if (mDB == null){
            current = scoreList.get(position);
        }else{
            current = mDB.query(position);
        }
        holder.userName.setText(current.getUserName());
        holder.userScore.setText(String.valueOf(current.getScore()));
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ScoreEditActivity.class);
                intent.putExtra("id", current.getId());
                intent.putExtra("name", holder.userName.getText());
                intent.putExtra("score", holder.userScore.getText());
                // Start an empty edit activity.
                ((Activity) mContext).startActivityForResult(
                        intent, 1);
                notifyDataSetChanged();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure that you want to delete this score?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDB.delete(current.getId());
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // Placeholder so we can see some mock data.
        if (mDB == null){
            return scoreList.size();
        }else{
            return (int) mDB.count();
        }
    }
}