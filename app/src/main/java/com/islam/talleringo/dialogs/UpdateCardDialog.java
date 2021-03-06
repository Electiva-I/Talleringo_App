package com.islam.talleringo.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.islam.talleringo.R;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.utils.App;

import java.util.Objects;

public class UpdateCardDialog  extends DialogFragment {

    private Button btn_add, btn_cancel;
    private AutoCompleteTextView brand, model;
    private EditText year;
    private final DataViewModel dataViewModel;
    private final Vehicle vehicle;

    public  UpdateCardDialog(DataViewModel dataViewModel, Vehicle vehicle){
        this.dataViewModel = dataViewModel;
        this.vehicle = vehicle;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_add_vehicle,container, true);
        initDialog(view);
        return view;
    }

    private void initDialog(View view){
        btn_add = view.findViewById(R.id.btn_add_vehicle);
        btn_add.setText(R.string.title_update_dialog);
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        brand = view.findViewById(R.id.brand_text);
        model = view.findViewById(R.id.model_text);
        year = view.findViewById(R.id.year_text);

        brand.setText(vehicle.Brand);
        model.setText(vehicle.Model);
        year.setText(vehicle.Year);

        String [] vehicles_array = getResources().getStringArray(R.array.vehicles_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, vehicles_array);
        brand.setAdapter(adapter);
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
            addVehicle(model.getText().toString(), brand.getText().toString(), year.getText().toString());
            Objects.requireNonNull(getDialog()).dismiss();
        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private void addVehicle(String model, String brand, String year){
        vehicle.Year = year;
        vehicle.Model = model;
        vehicle.Brand = brand;
        AppDatabase db = Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "vehicle").allowMainThreadQueries().build();

        db.vehicleDAO().update(vehicle);
        db.close();
        dataViewModel.getUpdatedVehicle().setValue(vehicle);
    }

    private boolean validatedFields(){

        if(brand.getText().toString().trim().isEmpty()){
            brand.setError(getString(R.string.validated_fields));
        }else{
            if(model.getText().toString().trim().isEmpty()){
                model.setError(getString(R.string.validated_fields));
            }else{
                if(year.getText().toString().trim().isEmpty()){
                    year.setError(getString(R.string.validated_fields));
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
