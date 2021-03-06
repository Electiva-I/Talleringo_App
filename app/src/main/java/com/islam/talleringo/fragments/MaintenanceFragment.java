package com.islam.talleringo.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.work.Data;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.islam.talleringo.R;
import com.islam.talleringo.adapters.Maintenance_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.dialogs.AddMaintenanceDialog;
import com.islam.talleringo.dialogs.DetailMaintenanceDialog;
import com.islam.talleringo.dialogs.UpdateMaintenanceDialog;
import com.islam.talleringo.schedulers.Scheduler;
import com.islam.talleringo.utils.App;

import java.util.List;
import java.util.Objects;

public class MaintenanceFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();
    private DataViewModel dataViewModel, updateDataViewModel, deletedDataViewModel;
    private RecyclerView maintenanceRV;
    private Maintenance_Adapter maintenanceAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Maintenance> maintenanceList;
    private LinearLayout linearLayoutInfoVehicles;

    public MaintenanceFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        floatingActionButton = view.findViewById(R.id.fabAddMaintenance);
        maintenanceRV = view.findViewById(R.id.recyclerViewMaintenance);
        linearLayoutInfoVehicles = view.findViewById(R.id.layoutInfoNoVehicles);
        layoutManager = new LinearLayoutManager(getActivity());
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (db.vehicleDAO().countVehicles() != 0) {
            linearLayoutInfoVehicles.setVisibility(View.GONE);
            initFragment();
        } else {
            floatingActionButton.setEnabled(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        updateDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        deletedDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        final Observer<Maintenance> updateObserver = maintenance -> {
            int position = maintenanceList.indexOf(maintenance);
            maintenanceList.set(position, maintenance);
            maintenanceAdapter.notifyDataSetChanged();
            Scheduler.getInstance().cancelAllWorkByTag(maintenance.uuid);
            if (maintenance.notify){
                Scheduler.Schedule_Maintenance(maintenance.Schedule_Date+" "+ maintenance.hour, maintenance.uuid,
                        new Data.Builder()
                                .putString("title", getString(R.string.txt_notifications_title))
                                .putString("detail", maintenance.Detail)
                                .putInt("maintenance_id", maintenance.ID)
                                .build()
                );
            }

            showMessage(R.string.txt_messages_maintenance_updated);
        };

        final Observer<Maintenance> deleteObserver = maintenance -> {
            maintenanceList.remove(maintenance);
            maintenanceAdapter.notifyDataSetChanged();
            Scheduler.getInstance().cancelAllWorkByTag(maintenance.uuid);
            showMessage(R.string.txt_messages_maintenance_transferred);
        };

        final Observer<Maintenance> createObserver = maintenance -> {
            Maintenance newMaintenance =db.maintenanceDAO().getLastMaintenance();
            maintenanceList.add(newMaintenance);
            if (newMaintenance.notify){
                Scheduler.Schedule_Maintenance(newMaintenance.Schedule_Date+" "+ newMaintenance.hour, newMaintenance.uuid,
                        new Data.Builder()
                                .putString("title", getString(R.string.txt_notifications_title))
                                .putString("detail", newMaintenance.Detail)
                                .putInt("maintenance_id", newMaintenance.ID)
                                .build()
                );
            }

            maintenanceAdapter.notifyItemInserted(maintenanceAdapter.getItemCount());
            showMessage(R.string.txt_messages_maintenance_created);
        };
        dataViewModel.getNewMaintenance().observe(this, createObserver);
        updateDataViewModel.getUpdatedMaintenance().observe(this, updateObserver);
        deletedDataViewModel.getDeletedMaintenance().observe(this, deleteObserver);
    }

    @SuppressLint("NonConstantResourceId")
    private void initFragment() {
        floatingActionButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            new AddMaintenanceDialog(dataViewModel).show(getFragmentManager(), "addMaintenance");
        });
        maintenanceList = db.maintenanceDAO().getAll();

        maintenanceRV.setLayoutManager(layoutManager);

        maintenanceAdapter = new Maintenance_Adapter(maintenanceList, R.layout.card_view_maintenance, (id, position) -> detailMaintenance(id, position), (id, position, button) -> {
            PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), button);
            popup.getMenuInflater().inflate(R.menu.vehicle_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.vehicle_menu_delete:
                        deleteMaintenance(id, position, db);
                        break;
                    case R.id.vehicle_menu_update:
                        updateMaintenance(id, position);
                        break;
                }
                return false;
            });
            popup.show();
        });
        maintenanceRV.setAdapter(maintenanceAdapter);
    }

    private void deleteMaintenance(int id, int position, AppDatabase db) {
        if (db.maintenanceDAO().getMaintenance(id) == null){
            maintenanceList.remove(position);
            maintenanceAdapter.notifyItemRemoved(position);
            showMessage(R.string.txt_messages_maintenance_transferred);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_delete).setPositiveButton(R.string.menu_delete, (dialog, which) -> {

            Scheduler.getInstance().cancelAllWorkByTag(db.maintenanceDAO().getMaintenance(id).uuid);
            db.maintenanceDAO().deleteId(id);
            maintenanceList.remove(position);
            maintenanceAdapter.notifyItemRemoved(position);
            showMessage(R.string.txt_messages_maintenance_deleted);
        }).setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.cancel()).setTitle(R.string.txt_dialog_warning).show();
    }

    private void updateMaintenance(int id, int position) {
        if (db.maintenanceDAO().getMaintenance(id) == null){
            maintenanceList.remove(position);
            maintenanceAdapter.notifyItemRemoved(position);
            showMessage(R.string.txt_messages_maintenance_transferred);
            return;
        }
        Maintenance maintenance = db.maintenanceDAO().getMaintenance(id);
        assert getFragmentManager() != null;
        new UpdateMaintenanceDialog(updateDataViewModel, maintenance).show(getFragmentManager(), "addRecord");
    }

    private void detailMaintenance(int id, int position) {
        if (db.maintenanceDAO().getMaintenance(id) == null){
            maintenanceList.remove(position);
            maintenanceAdapter.notifyItemRemoved(position);
            showMessage(R.string.txt_messages_maintenance_transferred);
            return;
        }
        Maintenance maintenance = db.maintenanceDAO().getMaintenance(id);
        assert getFragmentManager() != null;
        new DetailMaintenanceDialog(deletedDataViewModel, maintenance).show(getFragmentManager(), "addRecord");
    }

    private void showMessage(int message) {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }


}