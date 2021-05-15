package com.islam.talleringo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islam.talleringo.R;
import com.islam.talleringo.adapters.Maintenance_Adapter;
import com.islam.talleringo.adapters.Record_Adapter;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.dialogs.AddMaintenanceDialog;
import com.islam.talleringo.dialogs.AddRecordDialog;
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

public class RecordFragment extends Fragment {

    private FloatingActionButton fabAddRecord;

    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();
    private DataViewModel dataViewModel;
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

            }
        });

        recordRV.setAdapter(recordAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        final Observer<Record> namObserver = new Observer<Record>() {
            @Override
            public void onChanged(Record record) {
                recordList.add(record);
                recordAdapter.notifyItemInserted( recordAdapter.getItemCount());
            }
        };
        dataViewModel.getNewRecord().observe(this, namObserver);
    }
}