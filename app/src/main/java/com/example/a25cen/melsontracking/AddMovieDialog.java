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
        FragmentManager fm = getFragmentManager();
        AddPersonDialog addPersonDialog = new AddPersonDialog();
        addPersonDialog.show(fm, "Song");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.dialog_movie_input, container);
        getDialog().setTitle("Enter a movie");

        this.setCancelable(false);
        //Defining EditTexts
        movieTitle = view.findViewById(R.id.editMovieTitle);
        movieBudget = view.findViewById(R.id.editMovieBudget);
        movieDurration = view.findViewById(R.id.editMovieDurration);
        movieNext = view.findViewById(R.id.btnMovieNext);
        movieYear = view.findViewById(R.id.editMovieYear);

        movieTitle.requestFocus();
        movieNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getActivity());
                try{
                    MovieCard movie = new MovieCard();
                    String title = movieTitle.getText().toString();
                    int year = Integer.parseInt(movieYear.getText().toString());
                    int durration = Integer.parseInt(movieDurration.getText().toString());
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
                    try {
                        db.insertMovie(movie);
                        MovieFragment.list.add(movie);
                        dismiss();
                    }catch (SQLException ex){
                        Toast.makeText(getContext(), "Movie inserting had an error", Toast.LENGTH_SHORT).show();
                    }finally {
                        db.close();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Please enter a movie title", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }



}
