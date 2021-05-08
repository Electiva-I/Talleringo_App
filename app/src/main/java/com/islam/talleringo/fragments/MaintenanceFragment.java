package com.islam.talleringo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islam.talleringo.R;
import com.islam.talleringo.adapters.Maintenance_Adapter;
import com.islam.talleringo.adapters.Vehicles_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.dialogs.AddCarDialog;
import com.islam.talleringo.dialogs.AddMaintenanceDialog;
import com.islam.talleringo.utils.App;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MaintenanceFragment extends Fragment {

    private FloatingActionButton fabAddMaint;

    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();
    private DataViewModel dataViewModel;
    private RecyclerView maintenanceRV;
    private Maintenance_Adapter maintenanceAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Maintenance> maintenanceList;

    public MaintenanceFragment(){}


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
        maintenanceRV = view.findViewById(R.id.recyclerViewMaintenance);
        layoutManager = new LinearLayoutManager(getActivity());
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        final Observer<Maintenance> namObserver = new Observer<Maintenance>() {
            @Override
            public void onChanged(Maintenance maintenance) {
                maintenanceList.add(maintenance);
                maintenanceAdapter.notifyItemInserted( maintenanceAdapter.getItemCount());
            }
        };
        dataViewModel.getNewMaintenance().observe(this, namObserver);
    }

    private void initFragment() {
        fabAddMaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMaintenanceDialog(dataViewModel).show(getFragmentManager(), "addMaintenance");
            }
        });
        maintenanceList = db.maintenanceDAO().getAll();
        maintenanceRV.setLayoutManager(layoutManager);

        maintenanceAdapter = new Maintenance_Adapter(maintenanceList, R.layout.card_view_maintenance, new Maintenance_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int id, int position) {

            }
        }, new Maintenance_Adapter.OnMenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int id, int position, ImageButton button) {

            }
        });
        maintenanceRV.setAdapter(maintenanceAdapter);

    }
}