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

public class AddCarDialog extends DialogFragment {
    //private ImageButton btnImg;
    private Button btn_add, btn_cancel;
    private AutoCompleteTextView brand, model;
    private  EditText year;
    private final DataViewModel dataViewModel;

    public  AddCarDialog(DataViewModel dataViewModel){
        this.dataViewModel = dataViewModel;
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
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        brand = view.findViewById(R.id.brand_text);
        model = view.findViewById(R.id.model_text);
        year = view.findViewById(R.id.year_text);

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
            if(!validatedFields()) {
                return;
            }
            addVehicle(model.getText().toString(), brand.getText().toString(), year.getText().toString());
            Objects.requireNonNull(getDialog()).dismiss();

        });
        btn_cancel.setOnClickListener(view -> Objects.requireNonNull(getDialog()).cancel());
    }

    private void addVehicle(String model, String brand, String year){
        Vehicle vehicle =  new Vehicle(model, brand, year);
        AppDatabase db = Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "vehicle").allowMainThreadQueries().build();
        db.vehicleDAO().insertAll(vehicle);
        db.close();
        dataViewModel.getNewVehicle().setValue(vehicle);
    }

    private boolean validatedFields(){

        if(brand.getText().toString().isEmpty()){
            brand.setError("This field can not be blank");
        }else{
            if(model.getText().toString().isEmpty()){
                model.setError("This field can not be blank");
            }else{
                if(year.getText().toString().isEmpty()){
                    year.setError("This field can not be blank");
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
