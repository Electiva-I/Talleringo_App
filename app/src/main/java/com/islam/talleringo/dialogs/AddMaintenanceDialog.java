
package com.islam.talleringo.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.concurrent.atomic.AtomicInteger;

public class AddMaintenanceDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private Spinner spinnerVehicles;
    private EditText detail;
    private TextView txt_date, txtHour;
    private int year, month, day;
    private CheckBox checkBoxNotification;
    private LinearLayout linearLayoutNotification;
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
        txt_date = view.findViewById(R.id.date_text);

        txtHour = view.findViewById(R.id.txt_maintenance_notification_hour);
        checkBoxNotification = view.findViewById(R.id.checkboxNotification);
        linearLayoutNotification = view.findViewById(R.id.linearNotificationLayout);

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, db.vehicleDAO().getAll());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicles.setAdapter(adapter);
        CallDateDialog();


        txtHour.setOnClickListener(view1 -> {
            AtomicInteger hour = new AtomicInteger();
            AtomicInteger minute = new AtomicInteger();
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourT, minuteT) -> {
                hour.set(hourT);
                minute.set(minuteT);
                calendar.set(0, 0, 0, hourT, minuteT);
                txtHour.setText(DateFormat.format("HH:mm",calendar));
                txtHour.setError(null);

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)+30, false);

            timePickerDialog.updateTime(hour.get(), minute.get());
            timePickerDialog.show();
        });


        checkBoxNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxNotification.isChecked()) {
                    linearLayoutNotification.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNotification.setVisibility(View.GONE);
                }
            }
        });

    }

    public void CallDateDialog(){
        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = (datePicker, y, m, d) -> {
            m++;
            String date = m+"/"+d+"/"+y;


            this.txt_date.setText(date);
        };
        txt_date.setOnClickListener(view -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener,
                    year,
                    month,
                    day);
            txt_date.setError(null);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
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
            String schedule_date = txt_date.getText().toString();
            String hour = txtHour.getText().toString();

            Maintenance maintenance = new Maintenance(
                    vehicle.ID,
                    detail,
                    creation_date,
                    schedule_date,
                    checkBoxNotification.isChecked(),
                    hour);

            db.maintenanceDAO().insertAll(maintenance);
            dataViewModel.getNewMaintenance().setValue(maintenance);
            Objects.requireNonNull(getDialog()).dismiss();
        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private boolean validatedFields(){

        if(detail.getText().toString().trim().isEmpty()){
            detail.setError(getString(R.string.validated_fields));
        }else if(txt_date.getText().toString().trim().isEmpty()){
            txt_date.setError(getString(R.string.validated_fields));
        } else if (checkBoxNotification.isChecked() && txtHour.getText().toString().trim().isEmpty()){
            txtHour.setError(getString(R.string.validated_fields));
        } else {
            return true;
        }
        return false;
    }

}
