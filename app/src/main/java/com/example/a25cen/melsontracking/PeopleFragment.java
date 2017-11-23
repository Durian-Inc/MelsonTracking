package com.example.a25cen.melsontracking;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        //Defining the swipeRefresh layout that will refresh the cards when swiped up
        //The delay is set for half a second
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
        //Defining the functionality of the search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Trying to search for the person in the search field
                //If any errors occur they will be caught and an error message will be displayed
                try{
                    String personName = editPersonName.getText().toString();
                    Toast.makeText(getContext(), "Looking for "+personName,
                            Toast.LENGTH_SHORT).show();
                    editPersonName.clearFocus();
                    searchForPerson(personName);
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

    /*
        searchForPerson
        Function that will search for the name in the person name search field
        Pre: personName --> The value from the search field
        Post: None
     */
    private void searchForPerson(String personName){
        final DatabaseHelper db = new DatabaseHelper(getActivity());
        //Finding the PID of the person in question
        int PID = db.performSearch("Person", personName.trim());
        //If the PID does not equal -1 then the person is found and the
        // custom dialog can be created
        //Otherwise an error message is presented to the user
        if(PID != -1){
            createDialogForPerson(Long.parseLong(String.valueOf
                    (PID)), personName);
        }
        else{
            Toast.makeText(getContext(), personName+" is not found", Toast
                    .LENGTH_SHORT).show();
        }
        return;
    }

    /*
        createDialogForPerson
        Function that will create a custom dialog for the person that is
        being searched
        Pre: PID --> The PID of the person, used to find their awards and gender
             name --> The name of the person in question
        Post: None
     */
    private void createDialogForPerson(long PID, String name){
        DatabaseHelper db = new DatabaseHelper(getContext());
        //Getting arraylists of awards won and nominated for the person
        ArrayList<String> awardsWon = db.getNamesOfAwards("Won", PID);
        ArrayList<String> awardsNominated = db.getNamesOfAwards("Nominated", PID);
        final Dialog dialog = new Dialog(getContext());
        //Setting the values of all the views in the expanded people card
        dialog.setContentView(R.layout.dialog_person_expanded);
        dialog.setTitle(name);
        TextView personName = dialog.findViewById(R.id.personExpandedName);
        TextView personGender = dialog.findViewById(R.id.personExpandedGender);
        personGender.setText("Gender: "+db.getGender(PID));
        personName.setText(name);
        final Button closing = dialog.findViewById(R.id.personExpandedButton);
        closing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Defining the layouts for the award categories and then placing the awards into the categories
        LinearLayout wonLinear = dialog.findViewById(R.id.personExpandedWonLinearLayout);
        LinearLayout nominatedLinear = dialog.findViewById(R.id.personExpandedNominatedLinearLayout);
        insertAwardsToDialog(wonLinear, awardsWon);
        insertAwardsToDialog(nominatedLinear, awardsNominated);

        //Setting the size of the dialog window to be a decent size
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window dialogWindow = dialog.getWindow();
        layoutParams.copyFrom(dialogWindow.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(layoutParams);
        dialog.show();
    }

    /*
        insertAwardsToDialog
        Function that will insert the awards from the arraylist to the corresponding layout manager
        Pre: layout --> The linear layout where the values are going to be placed
             awards --> An arraylist that stores the giver and title of the award that was recived from the database
        Post: None
     */
    private void insertAwardsToDialog(LinearLayout layout, ArrayList<String> awards) {
        for(String award: awards){
            TextView newAward = new TextView(getContext());
            newAward.setText(award);
            newAward.setTextSize(20);
            newAward.setTextColor(getContext().getResources().getColor(R.color.textViewColor));
            layout.addView(newAward, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

