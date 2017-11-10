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
import android.widget.RadioGroup;

/**
 * Created by 25cen on 11/8/17.
 */

public class AddAwardDialog extends DialogFragment {

    private EditText awardName;
    private EditText awardGiver;
    private RadioGroup radioGroupAwardWon;
    private EditText awardYear;
    private Button btnAwardNext;

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
        View view = inflater.inflate(R.layout.dialog_award_input, container);
        awardName = view.findViewById(R.id.editAwardName);
        awardGiver = view.findViewById(R.id.editAwardGiver);
        awardYear = view.findViewById(R.id.editAwardYear);

        btnAwardNext = view.findViewById(R.id.btnAwardNext);
        btnAwardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //Check that all input is given
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }
}
