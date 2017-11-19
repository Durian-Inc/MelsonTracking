package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Innocent on 11/2/17.
 */

public class PeopleFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    static ArrayList<PersonCard> list;


    private final String TAG = "People Fragment";

    @Override
    public void onStart() {
        super.onStart();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new PeopleAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        list = db.getAllPeople();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "The people are showing");

        View view = inflater.inflate(R.layout.people_fragment, container, false);

        recyclerView = view.findViewById(R.id.personCardsRecycle);
        return view;
    }
}

