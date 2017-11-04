package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Innocent on 11/2/17.
 */

public class PeopleFragment extends Fragment {
    private final String TAG = "People Fragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "The people are showing");

        View view = inflater.inflate(R.layout.people_fragment, container, false);

        return view;
    }
}

