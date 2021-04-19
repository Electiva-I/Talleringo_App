package com.islam.talleringo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islam.talleringo.R;
import com.islam.talleringo.dialogs.AddCarDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VehicleFragment extends Fragment {

    private FloatingActionButton fabAddCar;

    public VehicleFragment() {
        // Required empty public constructor
    }


    public static VehicleFragment newInstance(String param1, String param2) {
        VehicleFragment fragment = new VehicleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);
        fabAddCar = view.findViewById(R.id.fabAddVehicle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCarDialog().show(getFragmentManager(), "hola");
            }
        });
    }
}