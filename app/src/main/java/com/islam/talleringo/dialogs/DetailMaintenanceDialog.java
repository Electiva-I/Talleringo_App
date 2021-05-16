package com.islam.talleringo.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.islam.talleringo.R;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.utils.App;

import java.util.Calendar;

public class DetailMaintenanceDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private TextView txtDate, txtDetail, txtVehicle;
    private LinearLayout cost_container;
    private EditText txt_cost;
    private SwitchMaterial switchMaterial;
    private int year, month, day;
    private Maintenance maintenance;
    private final DataViewModel dataViewModel;

    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

    DatePickerDialog.OnDateSetListener setListener;


    public DetailMaintenanceDialog(DataViewModel dataViewModel, Maintenance maintenance){
        this.dataViewModel = dataViewModel;
        this.maintenance = maintenance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_complete_maintenace,container, true);
        initDialog(view);
        return view;
    }
    private void initDialog(View view){
        btn_add = view.findViewById(R.id.btn_add_vehicle);
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        txtVehicle = view.findViewById(R.id.vehicle_text);
        txtDetail = view.findViewById(R.id.detail_text);
        txtDate = view.findViewById(R.id.date_text);
        txt_cost = view.findViewById(R.id.txt_record_cost);
        switchMaterial = view.findViewById(R.id.switchCompleted);
        cost_container = view.findViewById(R.id.cost_container);
        cost_container.setVisibility(View.GONE);

        txtDetail.setText(maintenance.Detail);
        txtDate.setText(maintenance.Schedule_Date);
        txtVehicle.setText(db.vehicleDAO().getVehicle(maintenance.Vehicle_Id).toString());

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cost_container.setVisibility(View.VISIBLE);
                }else {
                    cost_container.setVisibility(View.GONE);
                }
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Float cost = Float.parseFloat(txt_cost.getText().toString());
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                Record record = new Record(maintenance.Vehicle_Id,maintenance.Detail, day+"/"+month+"/"+year, cost);
                db.maintenanceDAO().deleteId(maintenance.ID);
                db.recordDAO().insertAll(record);
                getDialog().dismiss();
                dataViewModel.getDeletedMaintenance().setValue(maintenance);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });
    }
}