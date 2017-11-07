package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Innocent Niyibizi on 11/2/17.
 */


public class MovieFragment extends Fragment{

    private  final String TAG = "Movie Fragment";
    @Nullable
    @Override
    //TODO
    //Create a dialog fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "The movie's are showing");
        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        FloatingActionButton addMovie = view.findViewById(R.id.floatingActionButton);
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs();
            }
        });
        return view;
    }

    /* TODO
    *   Write the function that will expand and collapse
    * */

    private void showDialogs()
    {
        Toast.makeText(getActivity(), "Show dialog function has been called", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getFragmentManager();
        AddMovieDialog addMovieDialog = new AddMovieDialog();
        addMovieDialog.show(fm, "AddMovieFragment");
        Toast.makeText(getActivity(), "Movie has been added", Toast.LENGTH_SHORT).show();
    }

}
