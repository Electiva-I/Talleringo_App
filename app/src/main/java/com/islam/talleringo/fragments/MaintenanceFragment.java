package com.islam.talleringo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islam.talleringo.R;
import com.islam.talleringo.dialogs.AddCarDialog;
import com.islam.talleringo.dialogs.AddMaintenanceDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MaintenanceFragment extends Fragment {

    private FloatingActionButton fabAddMaint;

    public MaintenanceFragment(){

    }

    public static MaintenanceFragment newInstance(String param1, String param2) {
        MaintenanceFragment fragment = new MaintenanceFragment();
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
        View view= inflater.inflate(R.layout.fragment_maintenance, container, false);
        fabAddMaint = view.findViewById(R.id.fabAddMaintenance);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabAddMaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMaintenanceDialog().show(getFragmentManager(), "addMaintenance");
            }
        });
    }
}