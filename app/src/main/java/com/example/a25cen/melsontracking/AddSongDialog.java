package com.example.a25cen.melsontracking;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by 25cen on 11/7/17.
 */

public class AddSongDialog extends DialogFragment {

    private EditText songName;
    private int tempRadioId;
    private RadioButton radioButtonSelected;
    private EditText songYear;
    private RadioGroup radioGroupOriginal;
    private Button songFinsih;


    public AddSongDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_song_input, container);
        getDialog().setTitle("Enter a song");

        songName = view.findViewById(R.id.editSongName);
        songYear = view.findViewById(R.id.editSongYear);
        radioGroupOriginal = view.findViewById(R.id.radioSongOriginal);
        tempRadioId = radioGroupOriginal.getCheckedRadioButtonId();
        radioButtonSelected = view.findViewById(tempRadioId);
        songFinsih = view.findViewById(R.id.btnSongFinsih);
        songFinsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
