package com.islam.talleringo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islam.talleringo.R;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.utils.App;


public class HomeFragment extends Fragment {
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initHome(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private  void initHome(View view) {
        TextView txtHistory = view.findViewById(R.id.txt_history_counter);
        TextView txtMaintenance = view.findViewById(R.id.txt_maintenance_counter);
        TextView txtVehicles = view.findViewById(R.id.txt_vehicle_counter);

        txtMaintenance.setText(db.maintenanceDAO().countMaintenance()+"");
        txtVehicles.setText(db.vehicleDAO().countVehicles()+"");
        txtHistory.setText(db.recordDAO().countHistory()+"");
        db.close();
    }
}