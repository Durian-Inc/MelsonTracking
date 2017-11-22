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
    private RadioButton radioYes;
    private EditText songYear;
    private Button songFinsih;


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Notifying the adapter in the movie fragment that the data has changed so the it can be refreshed
        MovieFragment.adapter.notifyDataSetChanged();
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

        //Defining all of the views for this dialog
        songName = view.findViewById(R.id.editSongName);
        songYear = view.findViewById(R.id.editSongYear);
        radioYes = view.findViewById(R.id.radioSongYes);
        songFinsih = view.findViewById(R.id.btnSongFinsih);
        songFinsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getActivity());
                int orig;
                if (radioYes.isChecked()) orig = 1;
                else orig = 0;
                //Trying to insert the song into the database
                //If anything goes wrong, the exception will be caught and an error message will be displayed the user
                try {
                    //Checking to see if the song name and year are both inputted
                    //If they are then the app will attempt to insert the song into the database
                    //otherwise the dialog will be dismissed
                    if(songName.getText().toString().length() > 0 && songYear.getText().toString().length()>0) {
                        db.insertSong(songName.getText().toString(), Integer.parseInt(songYear.getText().toString()), orig);
                        dismiss();
                    }
                    else {
                        dismiss();
                    }
                }catch (Exception ex) {
                    Toast.makeText(getContext(), "Error inserting the song", Toast.LENGTH_SHORT).show();
                }finally {
                    db.close();
                }

            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
