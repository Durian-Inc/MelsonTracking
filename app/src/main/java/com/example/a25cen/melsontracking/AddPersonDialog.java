package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddPersonDialog extends DialogFragment {

    private EditText personName;
    private String personRole = "";
    private int personGender = -1;
    private Button btnNextPerson, btnAddPerson;
    private RadioGroup radioGroupRoles;
    private RadioGroup radioGroupGender;
    private int tempRadioId;


    public AddPersonDialog() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Displaying the award dialog once this dialog is dismissed
        FragmentManager fm = getFragmentManager();
        AddAwardDialog addAwardDialog = new AddAwardDialog();
        addAwardDialog.show(fm, "Awards");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setCancelable(false);

        View view = inflater.inflate(R.layout.dialog_person_input, container);
        getDialog().setTitle("Enter a person");

        //Defining all of the views on this dialog
        personName = view.findViewById(R.id.editName);
        btnAddPerson = view.findViewById(R.id.btnPerseonAdd);
        btnNextPerson = view.findViewById(R.id.btnPerseonNext);
        radioGroupGender = view.findViewById(R.id.radioGender);
        radioGroupRoles = view.findViewById(R.id.radioRoles);
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Trying to parse all of the data accordingly
                //If anything goes wrong the exception will be caught and displayed to the user in a toast message
                try {
                    tempRadioId = radioGroupGender.getCheckedRadioButtonId();
                    switch (tempRadioId) {
                        case R.id.radioMale:
                            personGender = 0;
                            break;
                        case R.id.radioFemale:
                            personGender = 1;
                            break;
                    }
                    tempRadioId = radioGroupRoles.getCheckedRadioButtonId();
                    personRole = checkRole(tempRadioId);
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    PersonCard person;
                    String gender;
                    //Setting the value of the name by parsing the name input by spaces
                    String[] name = personName.getText().toString().trim().split("\\s+");
                    //Setting the gender based on the integer value
                    if (personGender == 0) {
                        gender = "Male";
                    } else {
                        gender = "Female";
                    }
                    person = new PersonCard(name, gender, personRole);
                    //Trying to insert the person into the database
                    //If there are any SQL exceptions they will be caught and an error message will be displayed to the user
                    try {
                        long PID = db.insertPerson(person);
                        person.setPID(PID);
                        PeopleFragment.list.add(person);
                        Toast.makeText(getContext(), person.getName()[0] + " " + person.getName()[1] +
                                " has been inserted", Toast.LENGTH_SHORT).show();
                        personName.setText("");
                        radioGroupGender.clearCheck();
                        radioGroupRoles.clearCheck();
                    } catch (SQLException ex) {
                        Toast.makeText(getContext(), "Person insertion failed",
                                Toast.LENGTH_SHORT).show();
                    } finally {
                        db.close();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error! Please enter all the information",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnNextPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }

    /*
        checkRole
        Function that will check the role of the user and return a string value of that role
        Pre: id --> The integer representation of the person's role
        Post: role --> The string representation of the person's role
     */
    private String checkRole(int id){
        String role = "";
        switch (id){
            case R.id.radioStar:
                role = "Star";
                break;
            case R.id.radioDirector:
                role = "Director";
                break;
            case R.id.radioWriter:
                role = "Writer";
                break;
            case R.id.radioAll:
                role = "All";
                break;
        }
        return role;
    }
}
