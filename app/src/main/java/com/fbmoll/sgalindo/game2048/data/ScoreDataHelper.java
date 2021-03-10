package com.fbmoll.sgalindo.game2048.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fbmoll.sgalindo.game2048.engine.Score;


public class ScoreDataHelper extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	5;
    private	static final String DATABASE_NAME = "score";
    private	static final String TABLE_SCORES = "scores";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "userName";
    public static final String COLUMN_NO = "score";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    private static final String CREATE_TABLE = "CREATE	TABLE " + TABLE_SCORES + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_NO + " INTEGER" + ")";

    public ScoreDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public Score query(int position){
        String query = "SELECT * FROM " + TABLE_SCORES +
                " ORDER BY " + COLUMN_NAME + " ASC " +
                "LIMIT " + position + ",1";

        Cursor cursor= null;

        Score entry = new Score();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            entry.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            entry.setScore(cursor.getInt(cursor.getColumnIndex(COLUMN_NO)));
        }catch (Exception e){
            Log.d("query: ", "EXCEPTION! " + e);
        }finally {
            cursor.close();
            return entry;
        }
    }

    public void insert(Score score) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, score.getId());
        values.put(COLUMN_NAME, score.getUserName());
        values.put(COLUMN_NO, score.getScore());

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            mWritableDB.insert(TABLE_SCORES, null, values);
        } catch (Exception e) {
            Log.w("insert", e.getMessage());
        }
    }

    public void delete(int id){
        try{
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            mWritableDB.delete(TABLE_SCORES,
                    COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});

        }catch (Exception e){
            Log.d("delete",e.getMessage());
        }
    }

    public void update(int id, String name, int scorePuntuation){
        Score score = new Score(id, name, scorePuntuation);
        delete(id);
        insert(score);
    }

    public Score getBestScore(){
        Score bestScore = query(0);
        for (int i = 0; i < count(); i++) {
            if (query(i).getScore() > bestScore.getScore()){
                bestScore = query(i);
            }
        }
        return bestScore;
    }

    public long count(){
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, TABLE_SCORES);
    }

    public Cursor search(String userName) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NO};
        String searchString =  "%" + userName + "%";
        String where = COLUMN_NAME + " LIKE ? ";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor= null;

        try{
            if (mReadableDB==null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_SCORES,columns,where,whereArgs,null,null,null);
        }catch (Exception e){
            Log.d("Table","search error");
        }
        return cursor;
    }

    public Cursor search(int value) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NO};
        String searchString =  "%" + value + "%";
        String where = COLUMN_NO + " LIKE ? ";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor= null;

        try{
            if (mReadableDB==null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_SCORES,columns,where,whereArgs,null,null,null);
        }catch (Exception e){
            Log.d("Table","search error");
        }
        return cursor;
    }

    public Cursor searchGreaterThan(int value) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NO};
        String searchString =  "" + value + "";
        String where = COLUMN_NO + " > ? ";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor= null;

        try{
            if (mReadableDB==null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_SCORES,columns,where,whereArgs,null,null,null);
        }catch (Exception e){
            Log.d("Table","search error");
        }
        return cursor;
    }

    public Cursor searchLessThan(int value) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NO};
        String searchString =  "" + value + "";
        String where = COLUMN_NO + " < ? ";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor= null;

        try{
            if (mReadableDB==null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_SCORES,columns,where,whereArgs,null,null,null);
        }catch (Exception e){
            Log.d("Table","search error");
        }
        return cursor;
    }
}