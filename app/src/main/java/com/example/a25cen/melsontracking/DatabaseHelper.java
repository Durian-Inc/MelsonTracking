package com.example.a25cen.melsontracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Innocent on 11/4/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "Database Helper";
    private final static short DB_VERSRION = 1;
    private final static String DATABASE_NAME = "Movies Database";

    private final String TABLE_MOVIES_CREATION = "CREATE TABLE Movie(" +
            "    MID INT NOT NULL," +
            "    Title VARCHAR(80) NOT NULL," +
            "    Year INT NOT NULL," +
            "    Budget INT," +
            "    Runtime INT NOT NULL," +
            "    PRIMARY KEY (MID)" +
            ")";
    private final String TABLE_PERSON_CREATION = "CREATE TABLE Person(" +
            "    PID INT NOT NULL," +
            "    Fname VARCHAR(80) NOT NULL," +
            "    Lname VARCHAR(80) NOT NULL," +
            "    Gender VARCHAR(80) NOT NULL," +
            "    PRIMARY KEY (PID)" +
            ")";
    private final String TABLE_AWARD_CREATION = "CREATE TABLE Award(" +
            "    AID INT NOT NULL," +
            "    Giver VARCHAR(80) NOT NULL," +
            "    Title VARCHAR(80) NOT NULL," +
            "    Role VARCHAR(80) NOT NULL," +
            "    PRIMARY KEY(AID)" +
            ")";
    private final String TABLE_STARS_CREATION = "CREATE TABLE Stars(" +
            "    Star INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    PRIMARY KEY (Star, Movie)," +
            "    FOREIGN KEY (Star) REFERENCES Person(PID)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)" +
            ")";
    private final String TABLE_WRITES_CREATION = "CREATE TABLE Writes(" +
            "    Writer INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    PRIMARY KEY (Writer, Movie)," +
            "    FOREIGN KEY (Writer) REFERENCES Person(PID)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)" +
            ")";
    private final String TABLE_SONG_CREATION = "CREATE TABLE Song(" +
            "    Movie INT NOT NULL," +
            "    Name VARCHAR(80) NOT NULL," +
            "    Year INT NOT NULL," +
            "    Original INT NOT NULL CHECK (Original >= 0)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)," +
            "    PRIMARY KEY (Movie, Name, Year)" +
            ")";
    private final String TABLE_DIRECTS_CREATION = "CREATE TABLE Directs(" +
            "    Director INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    PRIMARY KEY (Director, Movie)," +
            "    FOREIGN KEY (Director) REFERENCES Person(PID)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)" +
            ")";
    private final String TABLE_NOMINATED_CREATION = "CREATE TABLE Nominated(" +
            "    Award INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    Won INT NOT NULL CHECK (Won >= 0)," +
            "    Year INT NOT NULL," +
            "    PRIMARY KEY (Award, Movie)," +
            "    FOREIGN KEY (Award) REFERENCES Award(AID)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)" +
            ")";

    private ArrayList<String> tableCreations = new ArrayList<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSRION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreations.add(TABLE_MOVIES_CREATION);
        tableCreations.add(TABLE_PERSON_CREATION);
        tableCreations.add(TABLE_AWARD_CREATION);
        tableCreations.add(TABLE_SONG_CREATION);
        tableCreations.add(TABLE_DIRECTS_CREATION);
        tableCreations.add(TABLE_WRITES_CREATION);
        tableCreations.add(TABLE_STARS_CREATION);
        tableCreations.add(TABLE_NOMINATED_CREATION);

        for(String table: tableCreations)
        {
            db.execSQL(table);
        }

        //TODO
        //Perform all of the data insert commands from the SQLite file
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Dropping
        db.execSQL("DROP TABLE IF EXISTS Nominated");
        db.execSQL("DROP TABLE IF EXISTS Stars");
        db.execSQL("DROP TABLE IF EXISTS Writes");
        db.execSQL("DROP TABLE IF EXISTS Directs");
        db.execSQL("DROP TABLE IF EXISTS Song");
        db.execSQL("DROP TABLE IF EXISTS Award");
        db.execSQL("DROP TABLE IF EXISTS Person");
        db.execSQL("DROP TABLE IF EXISTS Movie");

        onCreate(db);
    }

    public long getRowCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tableName);
        return count;
    }

    public long insertMovie(MovieCard movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MID", getRowCount("Movie")+1);
        values.put("Title", movie.getTitle());
        values.put("Year", movie.getReleaseYear());
        values.put("Budget", movie.getBudget());
        values.put("Runtime", movie.getRuntime());

        long movieId = db.insert("Movie", null, values);

        return movieId;

    }

    public long insertPerson(PersonCard person){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PID", getRowCount("Person")+1);
        values.put("Fname", person.getName()[0]);
        values.put("Lname", person.getName()[1]);
        values.put("Gender", person.getGender());
        long personId = db.insert("Person", null, values);

        return personId;
    }
    //Function to get all the movies using basic selects and then return a list of movies
    public ArrayList<MovieCard> getAllMovies(){
        ArrayList<MovieCard> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlFindAll = "SELECT * FROM Movie" ;
        Cursor c = db.rawQuery(sqlFindAll, null);
        if (c.moveToFirst()) {
            do {
                MovieCard currMovie = new MovieCard();
                currMovie.setTitle(c.getString(c.getColumnIndex("Title")));
                currMovie.setRuntime(c.getInt(c.getColumnIndex("Runtime")));
                currMovie.setBudget(c.getInt(c.getColumnIndex("Budget")));
                currMovie.setReleaseYear(c.getInt(c.getColumnIndex("Year")));

                movies.add(currMovie);

            } while(c.moveToNext());
        }

        return movies;
    }

    public ArrayList<PersonCard> getAllPeople(){
        ArrayList<PersonCard> people = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlFindAll = "SELECT * FROM People" ;
        Cursor c = db.rawQuery(sqlFindAll, null);
        if(c.moveToFirst()) {
            do {
                PersonCard person = new PersonCard();
                String[] name = {};
                name[0] = c.getString(c.getColumnIndex("Fname"));
                name[1] = c.getString(c.getColumnIndex("Lname"));
                person.setName(name);
                person.setGender(c.getString(c.getColumnIndex("Gender")));
                people.add(person);
            }while(c.moveToNext());
        }


        return people;
    }

}
