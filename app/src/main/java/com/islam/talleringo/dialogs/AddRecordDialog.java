
package com.islam.talleringo.dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.utils.App;
import com.islam.talleringo.utils.utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class AddRecordDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private Spinner spinnerVehicle;
    private EditText  txtDetail, txtCost;
    private TextView txtDate;
    private int year, month, day;
    private final DataViewModel dataViewModel;
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

    DatePickerDialog.OnDateSetListener setListener;


    public AddRecordDialog(DataViewModel dataViewModel){
        this.dataViewModel = dataViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_add_record,container, true);
        initDialog(view);
        return view;
    }
    private void initDialog(View view){
        btn_add = view.findViewById(R.id.btn_add_vehicle);
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        spinnerVehicle = view.findViewById(R.id.vehicle_text);
        txtDetail = view.findViewById(R.id.detail_text);
        txtDate = view.findViewById(R.id.date_text);
        txtCost = view.findViewById(R.id.cost_text);
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<Vehicle>(getContext(),
                R.layout.spinner_item, db.vehicleDAO().getAll());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicle.setAdapter(adapter);
        CallDateDialog();

    }

    public void CallDateDialog(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                month = m+1;
                String date = day+"/"+month+"/"+year;
                txtDate.setText(date);
            }
        };
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,
                        year,
                        month,
                        day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
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
                Vehicle vehicle = (Vehicle) spinnerVehicle.getSelectedItem();
                String detail = txtDetail.getText().toString();
                String creation_date = txtDate.getText().toString();
                float  cost = Float.parseFloat(txtCost.getText().toString());

                Record record = new Record(vehicle.ID, detail, creation_date, cost );
                db.recordDAO().insertAll(record);
                dataViewModel.getNewRecord().setValue(record);
                getDialog().dismiss();
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
