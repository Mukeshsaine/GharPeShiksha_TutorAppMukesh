package com.gharpeshiksha.tutorapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gharpeshiksha.tutorapp.R;

public class Community extends Fragment {

    View view;

    public Community() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        return view;
    }

}
