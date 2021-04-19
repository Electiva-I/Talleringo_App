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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.utils.utils;
import com.bumptech.glide.Glide;

import java.io.File;

public class AddMaintenanceDialog extends DialogFragment {

    private Button btn_add, btn_cancel;
    private Spinner cboxVehicle;
    private EditText vehicle, detalle, costo, fecha;

    String[] Vehicles = {"Seleccione","Mazda RX","Toyota Tacoma","Nissan GTR"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_add_maintenance,container, true);
        //btnImg  = view.findViewById(R.id.btnImg);
        btn_add = view.findViewById(R.id.btn_add_vehicle);
        btn_cancel = view.findViewById(R.id.btn_cancel_vehicle);
        cboxVehicle = view.findViewById(R.id.vehicle_text);
        costo = view.findViewById(R.id.cost_text);
        costo.setEnabled(false);
        cboxVehicle.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Vehicles));
        return view;
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
                Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
