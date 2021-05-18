
package com.islam.talleringo.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.utils.App;

import java.util.Calendar;
import java.util.Objects;

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
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, db.vehicleDAO().getAll());
        db.close();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicle.setAdapter(adapter);
        CallDateDialog();

    }

    public void CallDateDialog(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = (datePicker, y, m, d) -> {
            month = m+1;
            String date = month+"/"+day+"/"+year;
            txtDate.setText(date);
        };
        txtDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener,
                    year,
                    month,
                    day);
            txtDate.setError(null);
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
            Vehicle vehicle = (Vehicle) spinnerVehicle.getSelectedItem();
            String detail = txtDetail.getText().toString();
            String creation_date = txtDate.getText().toString();
            float  cost = Float.parseFloat(txtCost.getText().toString());

            Record record = new Record(vehicle.ID, detail, creation_date, cost );
            db.recordDAO().insertAll(record);
            db.close();
            dataViewModel.getNewRecord().setValue(record);
            Objects.requireNonNull(getDialog()).dismiss();
        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private boolean validatedFields(){

        if(txtDetail.getText().toString().trim().isEmpty()){
            txtDetail.setError(getString(R.string.validated_fields));
        }else{
            if(txtCost.getText().toString().trim().isEmpty()){
                txtCost.setError(getString(R.string.validated_fields));
            }else{
                if(txtDate.getText().toString().trim().isEmpty()){
                    txtDate.setError(getString(R.string.validated_fields));
                }else{
                    return true;
                }
            }
        }
        return false;
    }

}
