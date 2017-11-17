package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
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

/**
 * Created by 25cen on 11/7/17.
 */

public class AddPersonDialog extends DialogFragment {

    private EditText personName;
    private int personRole = -1;
    private int personGender = -1;
    private Button btnNextPerson;
    private RadioGroup radioGroupRoles;
    private RadioGroup radioGroupGender;
    private RadioButton tempRadio;
    private int tempRadioId;


    public AddPersonDialog() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
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

        personName = view.findViewById(R.id.editName);
        btnNextPerson = view.findViewById(R.id.btnPerseonNext);
        radioGroupGender = view.findViewById(R.id.radioGender);
        radioGroupRoles = view.findViewById(R.id.radioRoles);
        btnNextPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempRadioId = radioGroupGender.getCheckedRadioButtonId();
                tempRadio = view.findViewById(tempRadioId);
                personGender = radioGroupGender.indexOfChild(tempRadio);

                tempRadioId = radioGroupGender.getCheckedRadioButtonId();
                tempRadio = view.findViewById(tempRadioId);
                personRole = radioGroupRoles.indexOfChild(tempRadio);
                if (personGender == -1 && personRole == -1 && personName.getText().length() < 1) {
                    Toast.makeText(getContext(), "Please enter data in all fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    PersonCard person;
                    String role;
                    String gender;
                    String[] name = personName.getText().toString().trim().split("\\s+");
                    if(personGender == 0) {
                        gender = "Male";
                    }
                    else {
                        gender = "Female";
                    }
                    switch (personRole) {
                        case 0:
                            role = "Star";
                            break;
                        case 1:
                            role = "Writer";
                            break;
                        case 2:
                            role = "Director";
                            break;
                        default:
                            role = "All";
                            break;
                    }
                    person = new PersonCard(name, gender, role);
                    try{
                        db.insertPerson(person);
                        PeopleFragment.list.add(person);
                        dismiss();
                    }catch (Exception ex){
                        Toast.makeText(getContext(), "Person insetion failed", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }
}
