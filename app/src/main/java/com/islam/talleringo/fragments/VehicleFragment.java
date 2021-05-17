package com.islam.talleringo.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.islam.talleringo.R;
import com.islam.talleringo.adapters.Vehicles_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.dialogs.AddCarDialog;
import com.islam.talleringo.dialogs.UpdateCardDialog;
import com.islam.talleringo.utils.App;

import java.util.List;
import java.util.Objects;

public class VehicleFragment extends Fragment {

    private FloatingActionButton fabAddCar;

    private DataViewModel dataViewModel, updateDataViewModel;
    private RecyclerView vehiclesRV;
    private Vehicles_Adapter vehiclesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Vehicle> vehiclesList;
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

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
        updateDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        final Observer<Vehicle> updateObserver = vehicle -> {
            int position = vehiclesList.indexOf(vehicle);
            vehiclesList.set(position, vehicle);
            vehiclesAdapter.notifyDataSetChanged();
            showMessage(R.string.txt_messages_vehicle_updated);
        };

        final Observer<Vehicle> createdObserver = vehicle -> {
            vehiclesList.add(db.vehicleDAO().getLastVehicle());
            db.close();
            vehiclesAdapter.notifyItemInserted(vehiclesAdapter.getItemCount());
            showMessage(R.string.txt_messages_vehicle_created);
        };

        dataViewModel.getNewVehicle().observe(this, createdObserver);
        updateDataViewModel.getUpdatedVehicle().observe(this, updateObserver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    @SuppressLint("NonConstantResourceId")
    private void initFragment() {
        fabAddCar.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            new AddCarDialog(dataViewModel).show(getFragmentManager(), "NewVehicle");
        });


        vehiclesRV.setLayoutManager(layoutManager);
        vehiclesList = db.vehicleDAO().getAll();
        db.close();

        vehiclesAdapter = new Vehicles_Adapter(vehiclesList, R.layout.card_view_vehicle, (id, position) -> {
            //open vehicles dialog
        }, (id, position, button) -> {
            PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), button);
            popup.getMenuInflater().inflate(R.menu.vehicle_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.vehicle_menu_delete:
                        deleteVehicle(id, position);
                        break;
                    case R.id.vehicle_menu_update:
                        updateVehicles(id);
                        break;
                }
                return false;
            });
            popup.show();
        });

        vehiclesRV.setAdapter(vehiclesAdapter);
        vehiclesRV.setLayoutManager(layoutManager);
    }

    private void deleteVehicle(int id, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_delete)
                .setPositiveButton(R.string.menu_delete, (dialog, which) -> {
                    int[] ids = {id};
                    db.vehicleDAO().deleteId(ids);
                    db.maintenanceDAO().deleteIdByVehicle(ids);
                    db.recordDAO().deleteIdByVehicle(ids);
                    db.close();
                    vehiclesList.remove(position);
                    vehiclesAdapter.notifyItemRemoved(position);
                    showMessage(R.string.txt_messages_vehicle_deleted);
                }).setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.cancel()).setTitle(R.string.txt_dialog_warning).show();
    }

    private void updateVehicles(int id) {
        Vehicle vehicle = db.vehicleDAO().getVehicle(id);
        assert getFragmentManager() != null;
        new UpdateCardDialog(updateDataViewModel, vehicle).show(getFragmentManager(), "addRecord");
    }

    private void showMessage(int message) {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
}