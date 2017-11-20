package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by 25cen on 11/8/17.
 */

public class AddAwardDialog extends DialogFragment {

    private EditText awardName;
    private EditText awardGiver;
    private RadioGroup radioGroupTemp;
    private long selectedPID = -1;
    private int awardWon = -1, tempButtonID = 0;
    private EditText awardYear;
    private Button btnAwardAdd, btnAwardNext;
    RadioGroup.LayoutParams rprms;

    public AddAwardDialog() {
    }

    private long findPID(int radioButtonSelectedId, ArrayList<PersonCard> people){
        long PID = -1;

        for (PersonCard person: people){
            if (person.getPID() == radioButtonSelectedId){
                PID = person.getPID();
                break;
            }
        }
        return PID;
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

        radioGroupTemp = view.findViewById(R.id.radioGroupPeople);
        final ArrayList<PersonCard> people = db.getAllPeople(db.getRowCount("Movie"));
        for(PersonCard person: people){
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(person.getName()[0] + " "+ person.getName()[1]);
            radioButton.setId(Integer.parseInt(String.valueOf(person.getPID())));
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroupTemp.addView(radioButton, rprms);
        }

        btnAwardAdd = view.findViewById(R.id.btnAwardAdd);
        btnAwardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //Check that all input is given

                DatabaseHelper db = new DatabaseHelper(getContext());
                try{
                    String name = awardName.getText().toString().trim();
                    String giver = awardGiver.getText().toString().trim();
                    int year = Integer.parseInt(awardYear.getText().toString().trim());
                    tempButtonID = radioGroupTemp.getCheckedRadioButtonId();
                    selectedPID = findPID(tempButtonID, people);

                    radioGroupTemp = view.findViewById(R.id.radioGroupWon);
                    tempButtonID = radioGroupTemp.getCheckedRadioButtonId();
                    switch (tempButtonID){
                        case R.id.radioWonYes:
                            awardWon = 1;
                            break;
                        case R.id.radioWonNo:
                            awardWon = 0;
                            break;
                    }

                    db.insertAward(name, giver, year, selectedPID, awardWon);
                    Toast.makeText(getContext(), "Added "+name, Toast.LENGTH_SHORT).show();
                    awardYear.setText("");
                    awardName.setText("");
                    awardGiver.setText("");
                    radioGroupTemp.clearCheck();
                    RadioGroup people = view.findViewById(R.id.radioGroupPeople);
                    people.clearCheck();
                }catch (Exception ex) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }finally {
                    db.close();
                }

            }
        });
        btnAwardNext = view.findViewById(R.id.btnAwardNext);
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
