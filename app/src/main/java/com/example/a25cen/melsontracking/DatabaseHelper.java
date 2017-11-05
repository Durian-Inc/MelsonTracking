package com.example.a25cen.melsontracking;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 25cen on 11/4/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "Database Helper";
    private final static short DB_VERSRION = 1;
    private final String DB_NAME = "Movies Database";
    private final String TABLE_MOVIES = "movies";
    private final String TABLE_DIRECTOR = "directors";
    private final String TABLE_STAR = "stars";
    private final String TABLE_WRITER = "writers";
    private final String TABLE_SONG = "songs";


    //Table creation strings
    private final String CREATE_MOVIE = "CREATE TABLE "+ TABLE_MOVIES +
            "MovieID INT PRIMARY KEY ";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
