package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddMovieDialog extends DialogFragment {

    private EditText movieTitle;
    private EditText movieDurration;
    private EditText movieBudget;
    private EditText movieYear;
    private Button movieNext;


    public AddMovieDialog() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Displaying the people entry dialog once this dialog is dismissed
        FragmentManager fm = getFragmentManager();
        AddPersonDialog addPersonDialog = new AddPersonDialog();
        addPersonDialog.show(fm, "Person");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.dialog_movie_input, container);
        getDialog().setTitle("Enter a movie");

        this.setCancelable(false);
        //Defining EditTexts to get data from the user
        movieTitle = view.findViewById(R.id.editMovieTitle);
        movieBudget = view.findViewById(R.id.editMovieBudget);
        movieDurration = view.findViewById(R.id.editMovieDurration);
        movieNext = view.findViewById(R.id.btnMovieNext);
        movieYear = view.findViewById(R.id.editMovieYear);

        //Setting the focus to be on the title field at start
        movieTitle.requestFocus();
        movieNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getActivity());
                //Trying to insert the movie into the database with the given information
                //Only the budget can be empty, everything else must have valid data
                try{
                    MovieCard movie = new MovieCard();
                    String title = movieTitle.getText().toString();
                    int year = Integer.parseInt(movieYear.getText().toString());
                    int durration = Integer.parseInt(movieDurration.getText().toString());
                    //Checking to see if there is anything in the budget field
                    //If there is then the movie will have a budget, otherwise the budget will be set to -1
                    if (movieBudget.getText().length()<=0) {
                        movie.setReleaseYear(year);
                        movie.setRuntime(durration);
                        movie.setTitle(title);
                    }
                    else{
                        movie.setReleaseYear(year);
                        movie.setRuntime(durration);
                        movie.setTitle(title);
                        movie.setBudget(Integer.parseInt(movieBudget.getText().toString()));
                    }
                    //Trying to insert the movie into the database and setting the long returned to be the MID of the current movie object
                    try {
                        long MID = db.insertMovie(movie);
                        movie.setMID(MID);
                        MovieFragment.list.add(movie);
                        Toast.makeText(getContext(), movie.getTitle()+" has been inserted!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }catch (SQLException ex){
                        Toast.makeText(getContext(), "Movie inserting had an error", Toast.LENGTH_SHORT).show();
                    }finally {
                        db.close();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Please enter a movie title, a runtime, and a year", Toast.LENGTH_SHORT).show();
                }finally {
                    db.close();
                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }



}
