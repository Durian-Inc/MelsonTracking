package com.example.a25cen.melsontracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


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
            "    Nominee INT," +
            "    Won INT NOT NULL CHECK (Won >= 0)," +
            "    Year INT NOT NULL," +
            "    PRIMARY KEY (Award, Movie)," +
            "    FOREIGN KEY (Award) REFERENCES Award(AID)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID)," +
            "    FOREIGN KEY (Nominee) REFERENCES Person(PID)"+
            ")";

    private final String SQL_GET_DIRECTOR = "SELECT DISTINCT P.Fname, P.Lname" +
            " FROM Person as P, Directs as D, Movie as M" +
            " WHERE P.PID = D.Director and Movie = ";

    private final String SQL_GET_STARS = "SELECT DISTINCT P.Fname, P.Lname" +
            " FROM Person as P, Stars as S, Movie as M" +
            " WHERE P.PID = S.Star and Movie = ";

    private ArrayList<String> tableCreations = new ArrayList<>();

    //Function used to populate DB with some values. Demo reasons
    public int populateDB(Context context) throws IOException{
        int rows = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        InputStream insertsStream = context.getResources().openRawResource(R.raw.start);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        while(insertReader.ready()) {
            String statement = insertReader.readLine();
            db.execSQL(statement);
            rows++;
        }
        insertReader.close();

        return rows;
    }

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


    //TODO
    //Translate all the JAVA code into SQL

    public long insertMovie(MovieCard movie) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MID", getRowCount("Movie")+1);
        values.put("Title", movie.getTitle());
        values.put("Year", movie.getReleaseYear());
        values.put("Runtime", movie.getRuntime());
        if (movie.getBudget() != -1) {
            values.put("Budget", movie.getBudget());
        }

        long movieId = db.insertOrThrow("Movie", null, values);

        return movieId;

    }

    public long insertPerson(PersonCard person) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long PID = getRowCount("Person")+1, MID = getRowCount("Movie");
        values.put("PID", PID);
        values.put("Fname", person.getName()[0]);
        values.put("Lname", person.getName()[1]);
        values.put("Gender", person.getGender());
        long personId = db.insert("Person", null, values);
        switch (person.getRole()) {
            case "Star":
                values.clear();
                values.put("Star", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Stars", null, values);
                break;
            case "Writer":
                values.clear();
                values.put("Writer", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Writes", null, values);
                break;

            case "Director":
                values.clear();
                values.put("Director", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Directs", null, values);
                break;

            case "All":
                //Inserting the person into every category
                values.clear();
                values.put("Star", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Stars", null, values);

                values.clear();
                values.put("Director", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Directs", null, values);

                values.clear();
                values.put("Writer", personId);
                values.put("Movie", MID);
                db.insertOrThrow("Writes", null, values);
                break;
        }

        return personId;
    }

    public long insertAward(String name, String giver, int year, long PID, int won){
        SQLiteDatabase db = this.getWritableDatabase();
        long MID = getRowCount("Movie");
        long AID = getRowCount("Award")+1;
        String awardInsert = "INSERT INTO Award (AID, Giver, Title) VALUES(" + AID + ", " +
                giver + ", " + name + ")";
        db.execSQL(awardInsert);
        String nominatedInsert = "INSERT INTO Nominated (Award, Movie, Nominee, Won, Year)" +
                " VALUES (" +AID +", "+ MID +", "+ PID +", " + won+", "+year+")";
        db.execSQL(nominatedInsert);
        db.close();
        return AID;
    }

    public long insertSong(String name, int year, int orig) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Movie", getRowCount("Movie"));
        values.put("Name", name);
        values.put("Year", year);
        values.put("Original", orig);

        return db.insertOrThrow("Song", null, values);

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
                currMovie.setMID(c.getLong(c.getColumnIndex("MID")));
                movies.add(currMovie);

            } while(c.moveToNext());
        }
        c.close();
        return movies;
    }

    public ArrayList<PersonCard> getAllPeople(){
        ArrayList<PersonCard> people = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlFindAll = "SELECT * FROM Person" ;
        Cursor c = db.rawQuery(sqlFindAll, null);
        if(c.moveToFirst()) {
            do {
                PersonCard person = new PersonCard();
                String[] name = {"",""};
                name[0] = c.getString(c.getColumnIndex("Fname"));
                name[1] = c.getString(c.getColumnIndex("Lname"));
                person.setName(name);
                person.setGender(c.getString(c.getColumnIndex("Gender")));
                person.setPID(c.getLong(c.getColumnIndex("PID")));
                people.add(person);
            }while(c.moveToNext());
        }

        c.close();
        return people;
    }

    public ArrayList<PersonCard> getAllPeople(long MID){
        //TODO App crashes
        ArrayList<PersonCard> people = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlFindAll = "SELECT DISTINCT PID, Fname, Lname " +
                "FROM Movie, Directs, Writes, Stars, Person " +
                "Where ((Movie.MID = Directs.Movie and Directs.Director = PID) or " +
                "(Movie.MID = Writes.Movie and Writes.Writer = PID) or " +
                "(Movie.MID = Stars.Movie and Stars.Star = PID)) AND Movie.MID = " + MID;
        Cursor c = db.rawQuery(sqlFindAll, null);
        if(c.moveToFirst()){
            do{
                PersonCard person = new PersonCard();
                String[] name = {"",""};
                name[0] = c.getString(c.getColumnIndex("Person.Fname"));
                name[1] = c.getString(c.getColumnIndex("Person.Lname"));
                person.setName(name);
                person.setPID(c.getLong(c.getColumnIndex("PID")));
                people.add(person);
            }while (c.moveToNext());
        }
        return people;
    }
    public String getMovieSong(long MID){
        String songName = "";
        String getSongSQL = "SELECT * FROM Song WHERE Movie="+MID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getSongSQL, null);
        if(c.moveToFirst()) {
            songName = c.getString(c.getColumnIndex("Name"));
        }
        c.close();
        return songName;
    }

    public String getAllPeopleInvolved(String table, long position){
        String peopleInvolved = "", temp[] ={};
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "";
        switch (table){
            case "Director":
                sqlQuery = SQL_GET_DIRECTOR + position;
                break;
            case "Stars":
                sqlQuery = SQL_GET_STARS + position;
                break;
        }
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                peopleInvolved += c.getString(c.getColumnIndex("P.Fname"));
                peopleInvolved += " ";
                peopleInvolved += c.getString(c.getColumnIndex("P.Lname"));
                if (table == "Stars") peopleInvolved += ", ";

            }while(c.moveToNext());
        }
        c.close();
        if (peopleInvolved.contains(","))
            return peopleInvolved.substring(0, peopleInvolved.length()-2);
        else
            return peopleInvolved;
    }

    public int getCount(String table, long PID){
        int count = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        switch (table){
            case "Stars":
                String starCount = "SELECT count(*) AS count FROM Stars WHERE Star = " + PID;
                c = db.rawQuery(starCount, null);
                if(c.moveToFirst()){
                    count = c.getInt(c.getColumnIndex("count"));
                }
                c.close();
                break;
            case "Directs":
                String directsCount = "SELECT count(*) AS count FROM Directs WHERE Director = " + PID;
                c = db.rawQuery(directsCount, null);
                if(c.moveToFirst()){
                    count = c.getInt(c.getColumnIndex("count"));
                }
                c.close();
                break;
            case "Writes":
                String writeCount = "SELECT count(*) AS count FROM Writes WHERE Writer = " + PID;
                c = db.rawQuery(writeCount, null);
                if(c.moveToFirst()){
                    count = c.getInt(c.getColumnIndex("count"));
                }
                c.close();
                break;
            case "Nominated":
                String nomiatedCount = "SELECT count(*) AS count FROM Nominated WHERE Nominee = " + PID;
                c = db.rawQuery(nomiatedCount, null);
                if(c.moveToFirst()){
                    count = c.getInt(c.getColumnIndex("count"));
                }
                c.close();
                break;
            case "Won":
                String wonCount = "SELECT count(*) AS count FROM Nominated WHERE Nominee = " + PID +" AND Won = 1";
                c = db.rawQuery(wonCount, null);
                if(c.moveToFirst()){
                    count = c.getInt(c.getColumnIndex("count"));
                }
                c.close();
                break;
        }
        return count;

    }
}
