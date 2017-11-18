package com.example.a25cen.melsontracking;

import android.content.DialogInterface;
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
import android.widget.Toast;


public class AddSongDialog extends DialogFragment {

    private EditText songName;
    private int tempRadioId;
    private RadioButton radioYes, radioNo;
    private EditText songYear;
    private RadioGroup radioGroupOriginal;
    private Button songFinsih;


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        MovieFragment.adapter.notifyDataSetChanged();;
        return;
    }

    public AddSongDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setCancelable(false);

        View view = inflater.inflate(R.layout.dialog_song_input, container);
        getDialog().setTitle("Enter a song");

        songName = view.findViewById(R.id.editSongName);
        songYear = view.findViewById(R.id.editSongYear);
        radioYes = view.findViewById(R.id.radioSongYes);
        radioNo = view.findViewById(R.id.radioSongNo);
        songFinsih = view.findViewById(R.id.btnSongFinsih);
        songFinsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getActivity());
                int orig;
                if (radioYes.isChecked()) orig = 1;
                else orig = 0;
                try {
                    db.insertSong(songName.getText().toString(), Integer.parseInt(songYear.getText().toString()), orig);
                    dismiss();
                }catch (Exception ex) {
                    Toast.makeText(getContext(), "Error inserting", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
