package com.example.a25cen.melsontracking;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;




public class MovieFragment extends Fragment{


    RecyclerView recyclerView;
    static RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    static ArrayList<MovieCard> list;
    EditText movieSearch;

    private  final String TAG = "Movie Fragment";

    @Nullable
    @Override
    public void onStart() {
        super.onStart();
        //Defining the functionality and values of the fragment on start
        movieSearch = getActivity().findViewById(R.id.movieSearch);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MovieAdapter(this.getActivity(), list);
        recyclerView.setAdapter(adapter);
        FloatingActionButton addMovie = getActivity().findViewById(R.id.floatingActionButton);
        //Defining the values of the addition button
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getFragmentManager();
                AddMovieDialog addMovieDialog = new AddMovieDialog();
                addMovieDialog.show(fm, addMovieDialog.getTag());
            }
        });
        Button searchMovie = getActivity().findViewById(R.id.btnMovieSearch);
        //Defining the functionality of the search button
        searchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getActivity());
                //Tying to search or populate the database when the search button is pressed
                //If any exception is thrown, it will be caught an error message will be displayed to the user
                try {
                    //Checking to see if the database is completely empty
                    boolean init = (db.getRowCount("Movie")>0) &&
                            (db.getRowCount("Person")>0) &&
                            (db.getRowCount("Award")>0) &&
                            (db.getRowCount("Song")>0) &&
                            (db.getRowCount("Directs")>0) &&
                            (db.getRowCount("Writes")>0) &&
                            (db.getRowCount("Stars")>0) &&
                            (db.getRowCount("Nominated")>0);
                    if(init == false) {
                        //Populating the database if it is empty
                        db.populateDB(getContext());
                    }
                    else {
                        //Finding the movie that matches the search term
                        findMovie(db, movieSearch);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }finally {
                    db.close();
                }
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "The movie's are showing");
        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        recyclerView = view.findViewById(R.id.movieCardsRecycle);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        list = db.getAllMovies();
    }

    /*
        findMovie
        Function that finds the movie that corresponds to the value given in
        the search field
         Pre: db --> An instance of the database helper class used to perform
          SQL queries
              movieSearchField --> EditText that has the value of the search
              item
         Post: None
     */
    private void findMovie(DatabaseHelper db, EditText movieSearchField){
        try{

            String movieTitle = movieSearchField.getText().toString();
            int MID = db.performSearch("Movie", movieTitle);
            if(MID != -1){
                checkForMovie(MID);
            }
            else{
                Toast.makeText(getContext(), movieTitle+" could not be " +
                        "found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), "Error in searching", Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
        }
        return;
    }

    /*
        checkForMovie
        Function that finds the MovieCard object of the given MID
        Pre: MID --> The ID of the movie that was found
        Post: None
     */
    private void checkForMovie(int MID){
        MovieCard theMovie = new MovieCard();
        for(MovieCard movieCard: list){
            if (movieCard.getMID() == MID){
                theMovie = movieCard;
                break;
            }
        }

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_movie_expanded);
        dialog.setTitle(theMovie.getTitle());
        Button movieClose = dialog.findViewById(R.id.movieExpandedCloseButton);
        movieClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView movieTitle, movieBudget, movieRuntime, movieYear;
        movieTitle = dialog.findViewById(R.id.movieExpandedTitle);
        movieBudget = dialog.findViewById(R.id.movieExpandedBudget);
        movieRuntime = dialog.findViewById(R.id.movieExpandedRuntime);
        movieYear = dialog.findViewById(R.id.movieExpandedYear);

        movieTitle.setText("Title: "+theMovie.getTitle());
        movieYear.setText("Release Year: "+String.valueOf(theMovie
                .getReleaseYear()));
        movieRuntime.setText("Runtime: "+String.valueOf(theMovie.getRuntime()
                )+" mins");
        if(theMovie.getBudget() != -1) {
            movieBudget.setText("Budget: $"+String.valueOf(theMovie.getBudget
                    ()));
        }
        else{
            movieBudget.setText("Budget: $0");
        }
        //Setting the size of the dialog window to be a decent size
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window dialogWindow = dialog.getWindow();
        layoutParams.copyFrom(dialogWindow.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(layoutParams);
        dialog.show();
        return;
    }


}

