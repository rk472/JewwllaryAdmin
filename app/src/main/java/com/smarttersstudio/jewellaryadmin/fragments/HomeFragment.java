package com.smarttersstudio.jewellaryadmin.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarttersstudio.jewellaryadmin.R;

public class HomeFragment extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity main = (AppCompatActivity)getActivity();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        main.getSupportActionBar().setTitle("Home");
        NavigationView navigationView = (NavigationView) main.findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);

        return v;
    }


}
