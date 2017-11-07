package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 25cen on 11/2/17.
 */

public class MovieFragment extends Fragment {
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
                Toast.makeText(getActivity(), "A movie will be added", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    /* TODO
    *   Write the function that will expand and collapse
    * */

}
