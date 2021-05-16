package com.islam.talleringo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.islam.talleringo.R;
import com.islam.talleringo.adapters.Maintenance_Adapter;
import com.islam.talleringo.adapters.Record_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.dialogs.AddMaintenanceDialog;
import com.islam.talleringo.dialogs.AddRecordDialog;
import com.islam.talleringo.dialogs.UpdateRecordDialog;
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

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment {

    private FloatingActionButton fabAddRecord;

    AppDatabase db = Room.databaseBuilder(App.getContext(), AppDatabase.class, "vehicle")
            .allowMainThreadQueries()
            .build();
    private DataViewModel dataViewModel, updateDataViewModel;
    private RecyclerView recordRV;
    private Record_Adapter recordAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Record> recordList;


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);
        fabAddRecord = view.findViewById(R.id.fabAddRecord);
        recordRV = view.findViewById(R.id.recyclerViewRecord);
        layoutManager = new LinearLayoutManager(getActivity());
        initRecord();
        return view;
    }

    private  void initRecord(){
        fabAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddRecordDialog(dataViewModel).show(getFragmentManager(), "addRecord");
            }
        });

        recordList = db.recordDAO().getAll();
        recordRV.setLayoutManager(layoutManager);
        recordAdapter = new Record_Adapter(recordList, R.layout.card_view_record, new Record_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int id, int position) {

            }
        }, new Record_Adapter.OnMenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int id, int position, ImageButton button) {
                PopupMenu popup = new PopupMenu(getContext(), button);
                popup.getMenuInflater().inflate(R.menu.vehicle_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()){
                        case R.id.vehicle_menu_delete:
                            deleteRecord(id, position, db);
                            break;
                        case R.id.vehicle_menu_update:
                            updateRecord(id);
                            break;
                    }
                    return false;
                });
                popup.show();
            }
        });

        recordRV.setAdapter(recordAdapter);
    }

    private void deleteRecord(int id, int position, AppDatabase db){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_delete).setPositiveButton(R.string.menu_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int[] ids = {id};
                db.recordDAO().deleteId(ids);
                recordList.remove(position);
                recordAdapter.notifyItemRemoved(position);
                showMessage(R.string.txt_messages_record_deleted);
            }
        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setTitle(R.string.txt_dialog_warning).show();
    }

    private void updateRecord(int id){
        Record record= db.recordDAO().getRecord(id);
        new UpdateRecordDialog(updateDataViewModel, record).show(getFragmentManager(), "addRecord");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        updateDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        final  Observer<Record> updateObserver = new Observer<Record>() {
            @Override
            public void onChanged(Record record) {
                int position = recordList.indexOf(record);
                recordList.set(position, record);
                recordAdapter.notifyDataSetChanged();
                showMessage(R.string.txt_messages_record_updated);
            }
        };

        final Observer<Record> createdObserver = new Observer<Record>() {
            @Override
            public void onChanged(Record record) {
                recordList.add(db.recordDAO().getLastRecord());
                recordAdapter.notifyItemInserted( recordAdapter.getItemCount());
                showMessage(R.string.txt_messages_record_created);
            }
        };
        dataViewModel.getNewRecord().observe(this, createdObserver);
        updateDataViewModel.getUpdatedRecord().observe(this, updateObserver);
    }

    private void showMessage(int message) {
        Snackbar snackbar = Snackbar.make(getView(), message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
}