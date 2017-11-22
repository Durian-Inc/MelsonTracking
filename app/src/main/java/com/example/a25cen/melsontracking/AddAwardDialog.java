package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;


public class AddAwardDialog extends DialogFragment {

    private EditText awardName;
    private EditText awardGiver;
    private RadioGroup radioGroupPeople;
    private long selectedPID = -1;
    private int awardWon = -1, tempButtonID = 0;
    private EditText awardYear;
    private Button btnAwardAdd, btnAwardNext;
    RadioGroup.LayoutParams rprms;
    private RadioButton tempRadioButton;
    private RadioGroup awardWinOrLose;

    public AddAwardDialog() {
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentManager fm = getFragmentManager();
        AddSongDialog addSongDialog = new AddSongDialog();
        addSongDialog.show(fm, "Song!");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setCancelable(false);
        DatabaseHelper db = new DatabaseHelper(getContext());

        View view = inflater.inflate(R.layout.dialog_award_input, container);
        awardGiver = view.findViewById(R.id.editAwardGiver);
        awardYear = view.findViewById(R.id.editAwardYear);
        awardName = view.findViewById(R.id.editAwardName);

        radioGroupPeople = view.findViewById(R.id.radioGroupPeople);
        //Creating an arraylist of people from the database which are in the newly added movie
        final ArrayList<PersonCard> people = db.getAllPeople(db.getRowCount("Movie"));
        //Dynamically creating a radiobutton for each person
        for(PersonCard person: people){
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(person.getName()[0] + " "+ person.getName()[1]);
            radioButton.setId(Integer.parseInt(String.valueOf(person.getPID())));
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroupPeople.addView(radioButton, rprms);
        }

        awardWinOrLose = view.findViewById(R.id.radioGroupAwardWon);
        awardWinOrLose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioAwardWonYes:
                        awardWon = 1;
                        break;
                    case R.id.radioAwardWonNo:
                        awardWon = 0;
                        break;
                }
            }
        });

        btnAwardAdd = view.findViewById(R.id.btnAwardAdd);
        btnAwardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                //Trying to insert the award into the database based on the given information
                //If there are any errors then they will be caught and an error message will be displayed
                try{
                    String name = awardName.getText().toString().trim();
                    String giver = awardGiver.getText().toString().trim();
                    int year = Integer.parseInt(awardYear.getText().toString().trim());
                    tempButtonID = radioGroupPeople.getCheckedRadioButtonId();
                    selectedPID = tempButtonID;

                    db.insertAward(name, giver, year, selectedPID, awardWon);
                    Toast.makeText(getContext(), "Added "+name, Toast.LENGTH_SHORT).show();
                    //Clearing all of the elements on the dialog to allow for insertion of another award
                    awardYear.setText("");
                    awardName.setText("");
                    awardGiver.setText("");
                    radioGroupPeople.clearCheck();
                    awardWinOrLose.clearCheck();
                }catch (Exception ex) {
                    Log.d("AWARD INSERT", ex.toString());
                }finally {
                    db.close();
                }

            }
        });
        btnAwardNext = view.findViewById(R.id.btnAwardNext);
        //Dismissing the dialog when the next button is pressed
        btnAwardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }
}
