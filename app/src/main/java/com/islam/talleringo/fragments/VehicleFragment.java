package com.islam.talleringo.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.adapters.Vehicles_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.dialogs.AddCarDialog;
import com.islam.talleringo.utils.App;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class VehicleFragment extends Fragment {

    private FloatingActionButton fabAddCar;

    private DataViewModel dataViewModel;
    private RecyclerView vehiclesRV;
    private Vehicles_Adapter vehiclesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private  List<Vehicle> vehiclesList;
    public VehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);
        fabAddCar = view.findViewById(R.id.fabAddVehicle);
        vehiclesRV = view.findViewById(R.id.recyclerViewVehicles);
        layoutManager = new LinearLayoutManager(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        final Observer<Vehicle> namObserver = new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                vehiclesList.add(vehicle);
                vehiclesAdapter.notifyItemInserted(vehiclesAdapter.getItemCount());
            }
        };
        dataViewModel.getNewVehicle().observe(this, namObserver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCarDialog(dataViewModel).show(getFragmentManager(), "NewVehicle");
            }
        });

        AppDatabase db = Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "vehicle").allowMainThreadQueries().build();

        vehiclesRV.setLayoutManager(layoutManager);
        vehiclesList =  db.vehicleDAO().getAll();

        vehiclesAdapter = new Vehicles_Adapter(vehiclesList, R.layout.card_view_vehicle, new Vehicles_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int id, int position) {
                //abrir vehiculos
            }
        }, (id, position, button) -> {
            PopupMenu popup = new PopupMenu(getContext(), button);
            popup.getMenuInflater().inflate(R.menu.vehicle_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.vehicle_menu_delete:
                        deleteVehicle(id, position, db);
                        break;
                }
                return false;
            });
            popup.show();
        });

        vehiclesRV.setAdapter(vehiclesAdapter);
        vehiclesRV.setLayoutManager(layoutManager);
        /*vehiclesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    fabAddCar.setVisibility(View.GONE);
                } else  {
                    fabAddCar.setVisibility(View.VISIBLE);
                }
            }
        });*/
    }

    private void deleteVehicle(int id, int position, AppDatabase db){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_delete)
                .setPositiveButton(R.string.menu_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int[] ids = {id};
                db.vehicleDAO().deleteId(ids);
                db.maintenanceDAO().deleteIdByVehicle(ids);
                db.recordDAO().deleteIdByVehicle(ids);
                vehiclesList.remove(position);
                vehiclesAdapter.notifyItemRemoved(position);
            }
        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setTitle(R.string.txt_dialog_warning).show();
    }
}