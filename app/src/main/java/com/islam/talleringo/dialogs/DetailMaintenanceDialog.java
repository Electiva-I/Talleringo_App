package com.islam.talleringo.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Objects;

public class DetailMaintenanceDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private LinearLayout cost_container;
    private EditText txt_cost;
    private int year, month, day;
    private final Maintenance maintenance;
    private final DataViewModel dataViewModel;

    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();


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
        btn_add.setVisibility(View.GONE);
        TextView txtVehicle = view.findViewById(R.id.vehicle_text);
        TextView txtDetail = view.findViewById(R.id.detail_text);
        TextView txtDate = view.findViewById(R.id.date_text);
        txt_cost = view.findViewById(R.id.txt_record_cost);
        SwitchMaterial switchMaterial = view.findViewById(R.id.switchCompleted);
        cost_container = view.findViewById(R.id.cost_container);
        cost_container.setVisibility(View.GONE);

        txtDetail.setText(maintenance.Detail);
        txtDate.setText(maintenance.Schedule_Date);
        txtVehicle.setText(db.vehicleDAO().getVehicle(maintenance.Vehicle_Id).toString());

        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btn_add.setVisibility(View.VISIBLE);
                cost_container.setVisibility(View.VISIBLE);
            }else {
                cost_container.setVisibility(View.GONE);
                btn_add.setVisibility(View.GONE);
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add.setOnClickListener(view -> {
            if(!validatedFields()){
                return;
            }
            float cost = Float.parseFloat(txt_cost.getText().toString());
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            Record record = new Record(maintenance.Vehicle_Id,maintenance.Detail, day+"/"+month+"/"+year, cost);
            db.maintenanceDAO().deleteId(maintenance.ID);
            db.recordDAO().insertAll(record);
            Objects.requireNonNull(getDialog()).dismiss();
            dataViewModel.getDeletedMaintenance().setValue(maintenance);
        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private boolean validatedFields(){

        if(txt_cost.getText().toString().isEmpty()){
            txt_cost.setError("This field can not be blank");
        }else{
            return true;
        }
        return false;
    }

}