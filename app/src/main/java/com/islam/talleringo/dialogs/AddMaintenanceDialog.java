
package com.islam.talleringo.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.system.Os;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.islam.talleringo.R;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.utils.App;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddMaintenanceDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private Spinner spinnerVehicles;
    private EditText detail;
    private TextView date;
    private int year, month, day;
    private final DataViewModel dataViewModel;
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

    DatePickerDialog.OnDateSetListener setListener;


    public AddMaintenanceDialog(DataViewModel dataViewModel){
        this.dataViewModel = dataViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_add_maintenance,container, true);
        initDialog(view);
        return view;
    }
    private void initDialog(View view){
        btn_add = view.findViewById(R.id.btn_add_vehicle);
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        spinnerVehicles = view.findViewById(R.id.vehicle_text);
        detail = view.findViewById(R.id.detail_text);
        date = view.findViewById(R.id.date_text);
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, db.vehicleDAO().getAll());
        db.close();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicles.setAdapter(adapter);
        CallDateDialog();

    }

    public void CallDateDialog(){
        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = (datePicker, y, m, d) -> {
            month = m+1;
            String date = m+"/"+d+"/"+y;

            AddMaintenanceDialog.this.date.setText(date);
        };
        date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener,
                    year,
                    month,
                    day);
            date.setError(null);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
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
            Vehicle vehicle = (Vehicle) spinnerVehicles.getSelectedItem();
            String detail = AddMaintenanceDialog.this.detail.getText().toString();
            String creation_date = new Date().toString();
            String schedule_date = date.getText().toString();

            Maintenance maintenance = new Maintenance(vehicle.ID, detail, creation_date, schedule_date );
            db.maintenanceDAO().insertAll(maintenance);
            db.close();
            dataViewModel.getNewMaintenance().setValue(maintenance);
            Objects.requireNonNull(getDialog()).dismiss();
        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private boolean validatedFields(){

        if(detail.getText().toString().isEmpty()){
            detail.setError("This field can not be blank");
        }else{
            if(date.getText().toString().isEmpty()){
                date.setError("This field can not be blank");
            }else{
                return true;
            }
        }
        return false;
    }

}
