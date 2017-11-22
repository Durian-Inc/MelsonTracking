package com.example.a25cen.melsontracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            "    FOREIGN KEY (Star) REFERENCES Person(PID) ON DELETE CASCADE," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID) ON DELETE CASCADE" +
            ")";
    private final String TABLE_WRITES_CREATION = "CREATE TABLE Writes(" +
            "    Writer INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    PRIMARY KEY (Writer, Movie)," +
            "    FOREIGN KEY (Writer) REFERENCES Person(PID) ON DELETE CASCADE," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID) ON DELETE CASCADE" +
            ")";
    private final String TABLE_SONG_CREATION = "CREATE TABLE Song(" +
            "    Movie INT NOT NULL," +
            "    Name VARCHAR(80) NOT NULL," +
            "    Year INT NOT NULL," +
            "    Original INT NOT NULL CHECK (Original >= 0)," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID) ON DELETE CASCADE," +
            "    PRIMARY KEY (Movie, Name, Year)" +
            ")";
    private final String TABLE_DIRECTS_CREATION = "CREATE TABLE Directs(" +
            "    Director INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    PRIMARY KEY (Director, Movie)," +
            "    FOREIGN KEY (Director) REFERENCES Person(PID) ON DELETE CASCADE," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID) ON DELETE CASCADE" +
            ")";
    private final String TABLE_NOMINATED_CREATION = "CREATE TABLE Nominated(" +
            "    Award INT NOT NULL," +
            "    Movie INT NOT NULL," +
            "    Nominee INT," +
            "    Won INT NOT NULL CHECK (Won >= 0)," +
            "    Year INT NOT NULL," +
            "    PRIMARY KEY (Award, Movie)," +
            "    FOREIGN KEY (Award) REFERENCES Award(AID) ON DELETE CASCADE," +
            "    FOREIGN KEY (Movie) REFERENCES Movie(MID) ON DELETE CASCADE," +
            "    FOREIGN KEY (Nominee) REFERENCES Person(PID) ON DELETE CASCADE"+
            ")";

    private final String SQL_GET_DIRECTOR = "SELECT DISTINCT P.Fname, P.Lname" +
            " FROM Person as P, Directs as D, Movie as M" +
            " WHERE P.PID = D.Director and Movie = ";

    private final String SQL_GET_STARS = "SELECT DISTINCT P.Fname, P.Lname" +
            " FROM Person as P, Stars as S, Movie as M" +
            " WHERE P.PID = S.Star and Movie = ";

    private final String SQL_GET_GENDER = "SELECT Gender FROM Person WHERE PID = ";

    private ArrayList<String> tableCreations = new ArrayList<>();

    /*
        Function that will populate the database with values from the start
        .sql file
        Pre: Context --> Current context of the app
        Post: Returns the number of rows that were inserted into the database
     */
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

    /*
        Constructor for the database
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSRION);
    }

    /*
        onCreate override function that create the database scheme
        according to the table creation statements specified above
     */
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

    }


    /*
        onOpen override that checks to see if the database is readonly, if it
         is not then it will execute the provided command.
         This will allow for deletions to cascade properly
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

    }

    /*
        onUpgrade override that drops all the tables in reverse order and the
         recreates them accordingly
     */
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

    /*
        getRowsCount
        Function used to get the current row count of a given table
        Pre: tableName --> Name of the table that a row count is being
        requested from.
        Post: count --> Returns the number of rows in the
     */
    public long getRowCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tableName);
        return count;
    }


    /*
        insertMovie
        Function that inserts a given movie into the database
        Pre: movie --> An instance of the MovieCard class that has all of the
         attributes that are needed for inserting a movie
        Post: movieId --> The current row index of the given movie. Used to
        set the MID of the movie
     */
    public long insertMovie(MovieCard movie) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        //Creating a ContentValues instance to hold all the movie values for
        // insertion
        ContentValues values = new ContentValues();
        //Placing each attribute with the corresponding column value
        values.put("MID", getRowCount("Movie")+1);
        values.put("Title", movie.getTitle());
        values.put("Year", movie.getReleaseYear());
        values.put("Runtime", movie.getRuntime());
        //Checking if the value of the budget is -1
        //  if it is then it will not be inserted into the database
        //  if it is not then it will be inserted into the database
        if (movie.getBudget() != -1) {
            values.put("Budget", movie.getBudget());
        }

        long movieId = db.insertOrThrow("Movie", null, values);

        return movieId;

    }

    /*
        insertPerson
        Function that inserts a person into the person table and then into
        the person's respective subclass
        Pre: person --> An instance of the PersonCard that holds all of th
        person's attributes
        Post: personId --> The current row count for the newly inserted person
     */
    public long insertPerson(PersonCard person) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long PID = getRowCount("Person")+1, MID = getRowCount("Movie");
        values.put("PID", PID);
        values.put("Fname", person.getName()[0]);
        values.put("Lname", person.getName()[1]);
        values.put("Gender", person.getGender());
        long personId = db.insert("Person", null, values);
        //Switch statement that places the person into their respective
        // sub-class based on their role
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

    /*
        insertAward
        Function that inserts an award into the Award table and also into the
         nominated table with a boolean value of  it a person has won or not
        Pre: name --> name of the award
             giver --> The giver of the award
             year --> The year the award was announced
             PID --> Unique ID number for the person associated with the award
             won --> 1 or 0 value for if the person won the award
        Post: AID --> The Award ID number
     */
    public long insertAward(String name, String giver, int year, long PID, int won){
        SQLiteDatabase db = this.getWritableDatabase();
        long MID = getRowCount("Movie");
        long AID = getRowCount("Award")+1;
        String awardInsert = "INSERT INTO Award (AID, Giver, Title) VALUES(" + AID + ", \"" +
                giver + "\", \"" + name + "\")";
        db.execSQL(awardInsert);
        String nominatedInsert = "INSERT INTO Nominated (Award, Movie, Nominee, Won, Year)" +
                " VALUES (" +AID +", "+ MID +", "+ PID +", " + won+", "+year+")";
        db.execSQL(nominatedInsert);
        db.close();
        return AID;
    }

    /*
       insertSong
       Function that inserts a song into the Song table of the database
       Pre: name --> The name of the song
            year --> Release year of the song
            orig --> 1 or 0 value for the originality of the song
       Post: Returns the current row count of the song
     */
    public long insertSong(String name, int year, int orig) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Movie", getRowCount("Movie"));
        values.put("Name", name);
        values.put("Year", year);
        values.put("Original", orig);

        return db.insertOrThrow("Song", null, values);

    }

    /*
        getAllMovies
        Function that gets all the moves from the database and creates
        instances of the MovieCard for each one
        Pre: None
        Post: Returns an arraylist of MovieCard objects
     */
    public ArrayList<MovieCard> getAllMovies(){
        ArrayList<MovieCard> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlFindAll = "SELECT * FROM Movie" ;
        //Placing the result of the databasae query into a cursor for itterating
        Cursor c = db.rawQuery(sqlFindAll, null);
        //Moving the cursor to the first element and looping through all the
        // columns of the cursor and placing the values into the right
        // attribute of the current MovieCard
        //Adds the MovieCard instance to the list after all values are collected
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
        //Closing the cursor and database
        c.close();
        db.close();
        return movies;
    }

    /*
        getAllPeople
        Function that gets all the people from the database and creates
        instances of the PersonCard for each one
        Pre: None
        Post: Returns an arraylist of PersonCard objects
     */
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

    /*
        getAllPeople
        Function that gets all the people from the database that are
        associated with a given movie
        Pre: MID --> The movie that is requesting all of its people
        Post: An arraylist of PersonCard is returned
     */
    public ArrayList<PersonCard> getAllPeople(long MID){
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

    /*
        getMovieSong
        Function that will find the name of the song with which this movie is
         associated with, it it exists
        Pre: MID --> The movie that is requesting the song name
        Post: songName --> The name of the song from the movie
     */
    public String getMovieSong(long MID){
        String songName = "";
        String getSongSQL = "SELECT * FROM Song WHERE Movie="+MID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getSongSQL, null);
        if(c.moveToFirst()) {
            songName = c.getString(c.getColumnIndex("Name"));
        }
        c.close();
        db.close();
        return songName;
    }

    /*
        getAllPeopleInvolved
        Function that gets a list of all the people involved in a movie
        Pre: table --> The type of people that are being requested
             position --> The movie ID
        Post: peopleInvolved --> A string with the people's names delimited
        by commas
     */
    public String getAllPeopleInvolved(String table, long position){
        String peopleInvolved = "";
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
        db.close();
        //Removing the last comma from the string if it exists
        if (peopleInvolved.contains(","))
            return peopleInvolved.substring(0, peopleInvolved.length()-2);
        else
            return peopleInvolved;
    }

    /*
        getCount
        Function that performs a count method on the given data
        Pre: table --> The table that the count needs to be performed on
             PID --> The person who the count is based on
        Post: count --> The result from the count function
     */
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
        db.close();
        return count;

    }

    /*
        deleteMovie
        Function that removes a movie from the database. This will remove all
         references to this movie as well
        Pre: MID --> The movie that will be deleted
        Post: None
     */
    public void deleteMovie(long MID){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDeleteMovie = "DELETE FROM Movie WHERE MID = " + MID;
        db.execSQL(sqlDeleteMovie);
        db.close();
        return;
    }

    /*
        getGender
        Function that returns the gender of a person
        Pre: PID --> The ID of the person
        Post: gender --> The gender of the person
     */
    public String getGender(long PID){
        String gender;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SQL_GET_GENDER+PID, null);
        c.moveToFirst();
        gender = c.getString(c.getColumnIndex("Gender"));
        c.close();
        db.close();
        return gender;
    }

    /*
        getNamesOfAwards
        Function that gets the names of the awards associated with a person
        Pre: type --> The type of award that is being sought: won or nominated
        Post: PID --> The person who the awards are associated with
     */
    public ArrayList<String> getNamesOfAwards(String type, long PID){
        ArrayList<String> giverAndAward = new ArrayList<>();
        String sqlGetNominations = "SELECT Giver, Title FROM Nominated, Award WHERE (Award = AID) " +
                "AND (Nominee = "+PID+")";
        String sqlGetWinner = "SELECT Giver, Title FROM Nominated, Award WHERE (Award = AID) " +
                "AND (Won = 1) AND (Nominee = "+PID+")";
        final SQLiteDatabase db = this.getReadableDatabase();
        //Checking which type of award to get and using the corresponding
        // query statement
        switch (type){
            case "Won":
                Cursor c = db.rawQuery(sqlGetWinner, null);
                String giver, award;
                if(c.moveToFirst()){
                    do{
                        giver = c.getString(c.getColumnIndex("Giver"));
                        award = c.getString(c.getColumnIndex("Title"));
                        giverAndAward.add(giver+": "+award);
                    }while (c.moveToNext());
                }
                c.close();
                break;
            case "Nominated":
                c = db.rawQuery(sqlGetNominations, null);
                if(c.moveToFirst()){
                    do {
                        giver = c.getString(c.getColumnIndex("Giver"));
                        award = c.getString(c.getColumnIndex("Title"));
                        giverAndAward.add(giver+": "+award);
                    }while (c.moveToNext());
                }
                c.close();
                break;
        }
        db.close();
        return giverAndAward;
    }
}
