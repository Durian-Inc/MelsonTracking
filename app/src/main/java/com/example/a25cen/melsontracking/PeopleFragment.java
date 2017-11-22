package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class PeopleFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    static ArrayList<PersonCard> list;
    SwipeRefreshLayout swipeRefreshLayout;
    private Button btnSearch;
    private EditText editPersonName;


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
        db.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "The people are showing");

        View view = inflater.inflate(R.layout.people_fragment, container, false);

        recyclerView = view.findViewById(R.id.personCardsRecycle);
        swipeRefreshLayout = view.findViewById(R.id.peopleRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        onStart();
                    }
                }, 500);
            }
        });
        editPersonName = view.findViewById(R.id.editPersonSearch);
        btnSearch = view.findViewById(R.id.btnPersonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String personName = editPersonName.getText().toString();
                    Toast.makeText(getContext(), "Looking for "+personName,
                            Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "That person is not found. Try again",
                            Toast.LENGTH_SHORT).show();
                }finally {
                    //TODO Close DB if necessary
                }
            }
        });
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onStart();
    }
}

