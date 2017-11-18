package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
import android.provider.MediaStore;
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

/**
 * Created by 25cen on 11/8/17.
 */

public class AddAwardDialog extends DialogFragment {

    private String awardName;
    private String awardGiver;
    private RadioGroup radioGroupTemp;
    private RadioButton radioButtonSelected;
    private int personRole = -1;
    private int awardWon = -1;
    private String awardYear;
    private Button btnAwardAdd, btnAwardNext;

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

        View view = inflater.inflate(R.layout.dialog_award_input, container);
        awardName = view.findViewById(R.id.editAwardName).toString();
        awardGiver = view.findViewById(R.id.editAwardGiver).toString();
        awardYear = view.findViewById(R.id.editAwardYear).toString();

        radioGroupTemp = view.findViewById(R.id.radioGroupWon);
        radioButtonSelected = view.findViewById(radioGroupTemp .getCheckedRadioButtonId()) ;
        awardWon = radioGroupTemp .indexOfChild(radioButtonSelected);

        radioGroupTemp = view.findViewById(R.id.radioAwardRoles);
        radioButtonSelected = view.findViewById(radioGroupTemp.getCheckedRadioButtonId());
        personRole = radioGroupTemp.indexOfChild(radioButtonSelected);


        btnAwardAdd = view.findViewById(R.id.btnAwardAdd);
        btnAwardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //Check that all input is given
                /*
                DatabaseHelper db = new DatabaseHelper(getContext());
                try{
                    db.insertAward();
                }catch (Exception ex) {

                }finally {
                    db.close();
                }
                */
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
