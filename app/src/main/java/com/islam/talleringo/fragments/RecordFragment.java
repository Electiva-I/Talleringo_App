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
import com.islam.talleringo.adapters.Record_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.dialogs.AddRecordDialog;
import com.islam.talleringo.dialogs.UpdateRecordDialog;
import com.islam.talleringo.utils.App;

import java.util.List;
import java.util.Objects;

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

    @SuppressLint("NonConstantResourceId")
    private void initRecord() {
        fabAddRecord.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            new AddRecordDialog(dataViewModel).show(getFragmentManager(), "addRecord");
        });

        recordList = db.recordDAO().getAll();
        db.close();
        recordRV.setLayoutManager(layoutManager);
        recordAdapter = new Record_Adapter(recordList, R.layout.card_view_record, (id, position) -> {

        }, (id, position, button) -> {
            PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), button);
            popup.getMenuInflater().inflate(R.menu.vehicle_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
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
        });

        recordRV.setAdapter(recordAdapter);
    }

    private void deleteRecord(int id, int position, AppDatabase db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_delete).setPositiveButton(R.string.menu_delete, (dialog, which) -> {
            int[] ids = {id};
            db.recordDAO().deleteId(ids);
            db.close();
            recordList.remove(position);
            recordAdapter.notifyItemRemoved(position);
            showMessage(R.string.txt_messages_record_deleted);
        }).setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.cancel()).setTitle(R.string.txt_dialog_warning).show();
    }

    private void updateRecord(int id) {
        Record record = db.recordDAO().getRecord(id);
        db.close();
        assert getFragmentManager() != null;
        new UpdateRecordDialog(updateDataViewModel, record).show(getFragmentManager(), "addRecord");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        updateDataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        final Observer<Record> updateObserver = record -> {
            int position = recordList.indexOf(record);
            recordList.set(position, record);
            recordAdapter.notifyDataSetChanged();
            showMessage(R.string.txt_messages_record_updated);
        };

        final Observer<Record> createdObserver = record -> {
            recordList.add(db.recordDAO().getLastRecord());
            db.close();
            recordAdapter.notifyItemInserted(recordAdapter.getItemCount());
            showMessage(R.string.txt_messages_record_created);
        };
        dataViewModel.getNewRecord().observe(this, createdObserver);
        updateDataViewModel.getUpdatedRecord().observe(this, updateObserver);
    }

    private void showMessage(int message) {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
}