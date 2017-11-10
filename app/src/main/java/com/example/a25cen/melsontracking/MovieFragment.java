package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Innocent Niyibizi on 11/2/17.
 */


public class MovieFragment extends Fragment{


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<MovieCard> list;
    ArrayList<DialogFragment> dialogFragments;

    private  final String TAG = "Movie Fragment";
    @Nullable
    @Override
    //TODO
    //Create a dialog fragment

    public void onStart() {
        super.onStart();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MovieAdapter(list);
        recyclerView.setAdapter(adapter);
        FloatingActionButton addMovie = getActivity().findViewById(R.id.floatingActionButton);
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs();
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
        list = new ArrayList<>();
        list.add(new MovieCard("Bee Movie", 2007, 95,150000000));
        list.add(new MovieCard("Top Gun", 1986, 95, 15000000));
    }

    /* TODO
    *   Write the function that will expand and collapse
    * */

    private void showDialogs()
    {
        dialogFragments = new ArrayList<>();
        final FragmentManager fm = getFragmentManager();
        AddMovieDialog addMovieDialog = new AddMovieDialog();
        final AddSongDialog addSongDialog = new AddSongDialog();
        addMovieDialog.show(fm, addMovieDialog.getTag());

    }

}

