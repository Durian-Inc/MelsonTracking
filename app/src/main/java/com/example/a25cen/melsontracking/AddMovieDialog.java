package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
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
                if (movieYear.getText().length() > 0) {
                    MovieCard movie = new MovieCard(movieTitle.getText().toString(), Integer.parseInt(movieYear.getText().toString()), Integer.parseInt(movieDurration.getText().toString())
                            , Integer.parseInt(movieBudget.getText().toString()));
                    try {
                        db.insertMovie(movie);
                        MovieFragment.list.add(movie);
                        dismiss();
                    }catch (Exception ex){
                        Toast.makeText(getContext(), "Movie inserting had an error", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }



}
