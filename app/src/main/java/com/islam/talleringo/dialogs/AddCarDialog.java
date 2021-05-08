package com.islam.talleringo.dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.utils.App;
import com.islam.talleringo.utils.utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddCarDialog extends DialogFragment {
    //private ImageButton btnImg;
    private Button btn_add, btn_cancel;
    private AutoCompleteTextView brand, model;
    private  EditText year;
    private DataViewModel dataViewModel;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, vehicles_array);
        brand.setAdapter(adapter);
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
                addVehicle(model.getText().toString(), brand.getText().toString(), year.getText().toString());
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

    private void addVehicle(String model, String brand, String year){
        Vehicle vehicle =  new Vehicle(model, brand, year);
        AppDatabase db = Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "vehicle").allowMainThreadQueries().build();
        db.vehicleDAO().insertAll(vehicle);
        dataViewModel.getNewVehicle().setValue(vehicle);
    }
}
